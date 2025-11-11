package controller;

import dal.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import model.Product;

@MultipartConfig
public class AdminProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }

        ProductDAO productDAO = new ProductDAO();

        switch (action) {
            case "list":
                listProducts(request, response, productDAO);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response, productDAO);
                break;
            default:
                listProducts(request, response, productDAO);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendRedirect("admin-products");
            return;
        }

        ProductDAO productDAO = new ProductDAO();

        switch (action) {
            case "create":
                createProduct(request, response, productDAO);
                break;
            case "update":
                updateProduct(request, response, productDAO);
                break;
            case "delete":
                deleteProduct(request, response, productDAO);
                break;
            default:
                response.sendRedirect("admin-products");
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response, ProductDAO productDAO)
            throws ServletException, IOException {
        List<Product> products = productDAO.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("Views/admin/product-list.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("Views/admin/product-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, ProductDAO productDAO)
            throws ServletException, IOException {
        String maSPStr = request.getParameter("maSP");
        
        if (maSPStr == null) {
            response.sendRedirect("admin-products");
            return;
        }

        try {
            int maSP = Integer.parseInt(maSPStr);
            Product product = productDAO.getProductById(maSP);
            
            if (product == null) {
                request.setAttribute("error", "Không tìm thấy sản phẩm");
                response.sendRedirect("admin-products");
                return;
            }

            request.setAttribute("product", product);
            request.getRequestDispatcher("Views/admin/product-form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("admin-products");
        }
    }

    private void createProduct(HttpServletRequest request, HttpServletResponse response, ProductDAO productDAO)
            throws ServletException, IOException {
        String tenSP = request.getParameter("tenSP");
        String donGiaStr = request.getParameter("donGia");
        String trangThai = request.getParameter("trangThai");
        Part thumbnailPart = null;
        try { thumbnailPart = request.getPart("thumbnailFile"); } catch (IllegalStateException ex) {}

        if (tenSP == null || donGiaStr == null || trangThai == null) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin");
            request.getRequestDispatcher("Views/admin/product-form.jsp").forward(request, response);
            return;
        }

        try {
            String normalizedPrice = donGiaStr.replace(".", "").replace(",", "").replaceAll("[^0-9]", "");
            double donGia = Double.parseDouble(normalizedPrice);

            String thumbnailFileName = null;
            if (thumbnailPart != null && thumbnailPart.getSize() > 0) {
                String submitted = getSubmittedFileName(thumbnailPart);
                if (submitted != null && !submitted.isBlank()) {
                    thumbnailFileName = submitted;
                    saveUploadedFile(thumbnailPart, getUploadDir(), thumbnailFileName);
                }
            }

            if (thumbnailFileName == null || thumbnailFileName.isBlank()) {
                request.setAttribute("error", "Vui lòng chọn hình ảnh sản phẩm");
                request.getRequestDispatcher("Views/admin/product-form.jsp").forward(request, response);
                return;
            }

            Product product = new Product();
            product.setTenSP(tenSP);
            product.setDonGia(donGia);
            product.setThumbnailUrl(thumbnailFileName);
            product.setTrangThai(trangThai);

            boolean success = productDAO.createProduct(product);

            if (success) {
                request.setAttribute("success", "Tạo sản phẩm thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi tạo sản phẩm");
            }

            response.sendRedirect("admin-products");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Giá sản phẩm không hợp lệ");
            request.getRequestDispatcher("Views/admin/product-form.jsp").forward(request, response);
        }
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response, ProductDAO productDAO)
            throws ServletException, IOException {
        String maSPStr = request.getParameter("maSP");
        String tenSP = request.getParameter("tenSP");
        String donGiaStr = request.getParameter("donGia");
        String trangThai = request.getParameter("trangThai");
        Part thumbnailPart = null;
        try { thumbnailPart = request.getPart("thumbnailFile"); } catch (IllegalStateException ex) {}

        if (maSPStr == null || tenSP == null || donGiaStr == null || trangThai == null) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin");
            response.sendRedirect("admin-products");
            return;
        }

        try {
            int maSP = Integer.parseInt(maSPStr);
            String normalizedPrice = donGiaStr.replace(".", "").replace(",", "").replaceAll("[^0-9]", "");
            double donGia = Double.parseDouble(normalizedPrice);

            Product current = productDAO.getProductById(maSP);
            String thumbnailFileName = (current != null) ? current.getThumbnailUrl() : null;
            if (thumbnailPart != null && thumbnailPart.getSize() > 0) {
                String submitted = getSubmittedFileName(thumbnailPart);
                if (submitted != null && !submitted.isBlank()) {
                    thumbnailFileName = submitted;
                    saveUploadedFile(thumbnailPart, getUploadDir(), thumbnailFileName);
                }
            }

            Product product = new Product();
            product.setMaSP(maSP);
            product.setTenSP(tenSP);
            product.setDonGia(donGia);
            product.setThumbnailUrl(thumbnailFileName);
            product.setTrangThai(trangThai);

            boolean success = productDAO.updateProduct(product);

            if (success) {
                request.setAttribute("success", "Cập nhật sản phẩm thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật sản phẩm");
            }

            response.sendRedirect("admin-products");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ");
            response.sendRedirect("admin-products");
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response, ProductDAO productDAO)
            throws ServletException, IOException {
        String maSPStr = request.getParameter("maSP");
        
        if (maSPStr == null) {
            response.sendRedirect("admin-products");
            return;
        }

        try {
            int maSP = Integer.parseInt(maSPStr);
            boolean success = productDAO.deleteProduct(maSP);
            
            if (success) {
                request.setAttribute("success", "Xóa sản phẩm thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi xóa sản phẩm");
            }
            
            response.sendRedirect("admin-products");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID sản phẩm không hợp lệ");
            response.sendRedirect("admin-products");
        }
    }

    private String getSubmittedFileName(Part part) {
        String header = part.getHeader("content-disposition");
        if (header == null) return null;
        for (String cd : header.split(";")) {
            String trimmed = cd.trim();
            if (trimmed.startsWith("filename")) {
                String fileName = trimmed.substring(trimmed.indexOf('=') + 1).trim().replace("\"", "");
                return new File(fileName).getName();
            }
        }
        return null;
    }

    private String getUploadDir() {
        return "D:\\JavaProject\\MovieTicketSystem\\MovieTicketBookingSystem\\web\\assets\\image";
    }

    private void saveUploadedFile(Part part, String uploadDir, String fileName) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Path target = new File(dir, fileName).toPath();
        try {
            Files.copy(part.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            part.delete();
        }
    }
}
