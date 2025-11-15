package controller;

import dal.DiscountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Discount;

public class AdminDiscountServlet extends HttpServlet {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chỉ Manager mới được truy cập
        if (!util.RoleChecker.requireManager(request, response)) return;
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }

        DiscountDAO discountDAO = new DiscountDAO();

        switch (action) {
            case "list":
                listDiscounts(request, response, discountDAO);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response, discountDAO);
                break;
            default:
                listDiscounts(request, response, discountDAO);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chỉ Manager mới được truy cập
        if (!util.RoleChecker.requireManager(request, response)) return;
        
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendRedirect("admin-discounts");
            return;
        }

        DiscountDAO discountDAO = new DiscountDAO();

        switch (action) {
            case "create":
                createDiscount(request, response, discountDAO);
                break;
            case "update":
                updateDiscount(request, response, discountDAO);
                break;
            case "delete":
                deleteDiscount(request, response, discountDAO);
                break;
            default:
                response.sendRedirect("admin-discounts");
        }
    }

    private void listDiscounts(HttpServletRequest request, HttpServletResponse response, DiscountDAO discountDAO)
            throws ServletException, IOException {
        List<Discount> discounts = discountDAO.getAllDiscounts();
        
        // Format dates for display - create a map of formatted dates
        Map<Integer, String> ngayBatDauFormatted = new HashMap<>();
        Map<Integer, String> ngayKetThucFormatted = new HashMap<>();
        
        for (Discount discount : discounts) {
            if (discount.getNgayBatDau() != null) {
                String formatted = discount.getNgayBatDau().format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                ngayBatDauFormatted.put(discount.getMaGiamGia(), formatted);
            }
            if (discount.getNgayKetThuc() != null) {
                String formatted = discount.getNgayKetThuc().format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                ngayKetThucFormatted.put(discount.getMaGiamGia(), formatted);
            }
        }
        
        request.setAttribute("discounts", discounts);
        request.setAttribute("ngayBatDauFormatted", ngayBatDauFormatted);
        request.setAttribute("ngayKetThucFormatted", ngayKetThucFormatted);
        request.getRequestDispatcher("Views/admin/discount-list.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("Views/admin/discount-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, DiscountDAO discountDAO)
            throws ServletException, IOException {
        String maGiamGiaStr = request.getParameter("maGiamGia");
        
        if (maGiamGiaStr == null) {
            response.sendRedirect("admin-discounts");
            return;
        }

        try {
            int maGiamGia = Integer.parseInt(maGiamGiaStr);
            Discount discount = discountDAO.getDiscountById(maGiamGia);
            
            if (discount == null) {
                request.setAttribute("error", "Không tìm thấy mã giảm giá");
                response.sendRedirect("admin-discounts");
                return;
            }

            // Format datetime for datetime-local input
            if (discount.getNgayBatDau() != null) {
                String ngayBatDauFormatted = discount.getNgayBatDau().toString().replace(" ", "T").substring(0, 16);
                request.setAttribute("ngayBatDauFormatted", ngayBatDauFormatted);
            }
            if (discount.getNgayKetThuc() != null) {
                String ngayKetThucFormatted = discount.getNgayKetThuc().toString().replace(" ", "T").substring(0, 16);
                request.setAttribute("ngayKetThucFormatted", ngayKetThucFormatted);
            }
            
            // Format giá trị giảm - bỏ .00 nếu có
            if (discount.getGiaTriGiam() != null) {
                BigDecimal giaTriGiam = discount.getGiaTriGiam();
                String giaTriGiamFormatted;
                if (discount.getHinhThucGiam() != null && discount.getHinhThucGiam().equals("PhanTram")) {
                    // Nếu là phần trăm, format số nguyên
                    giaTriGiamFormatted = giaTriGiam.stripTrailingZeros().toPlainString();
                } else {
                    // Nếu là tiền mặt, format số nguyên (bỏ .00)
                    giaTriGiamFormatted = giaTriGiam.stripTrailingZeros().toPlainString();
                }
                request.setAttribute("giaTriGiamFormatted", giaTriGiamFormatted);
            }
            
            // Format giá trị tối đa - bỏ .00 nếu có
            if (discount.getGiaTriToiDa() != null) {
                String giaTriToiDaFormatted = discount.getGiaTriToiDa().stripTrailingZeros().toPlainString();
                request.setAttribute("giaTriToiDaFormatted", giaTriToiDaFormatted);
            }
            
            request.setAttribute("discount", discount);
            request.getRequestDispatcher("Views/admin/discount-form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("admin-discounts");
        }
    }

    private void createDiscount(HttpServletRequest request, HttpServletResponse response, DiscountDAO discountDAO)
            throws ServletException, IOException {
        String maCode = request.getParameter("maCode");
        String tenGiamGia = request.getParameter("tenGiamGia");
        String moTa = request.getParameter("moTa");
        String loaiGiamGia = request.getParameter("loaiGiamGia");
        String hinhThucGiam = request.getParameter("hinhThucGiam");
        String giaTriGiamStr = request.getParameter("giaTriGiam");
        String giaTriToiDaStr = request.getParameter("giaTriToiDa");
        String ngayBatDauStr = request.getParameter("ngayBatDau");
        String ngayKetThucStr = request.getParameter("ngayKetThuc");
        String trangThai = request.getParameter("trangThai");
        String soLanSuDungStr = request.getParameter("soLanSuDung");

        // Validation
        if (maCode == null || maCode.trim().isEmpty() ||
            tenGiamGia == null || tenGiamGia.trim().isEmpty() ||
            loaiGiamGia == null || hinhThucGiam == null ||
            giaTriGiamStr == null || giaTriGiamStr.trim().isEmpty() ||
            ngayBatDauStr == null || ngayBatDauStr.trim().isEmpty() ||
            trangThai == null) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin bắt buộc");
            request.getRequestDispatcher("Views/admin/discount-form.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mã code đã tồn tại chưa
        if (discountDAO.isCodeExists(maCode.trim())) {
            request.setAttribute("error", "Mã code đã tồn tại. Vui lòng chọn mã khác");
            request.getRequestDispatcher("Views/admin/discount-form.jsp").forward(request, response);
            return;
        }

        try {
            Discount discount = new Discount();
            discount.setMaCode(maCode.trim().toUpperCase());
            discount.setTenGiamGia(tenGiamGia.trim());
            discount.setMoTa(moTa != null ? moTa.trim() : null);
            discount.setLoaiGiamGia(loaiGiamGia);
            discount.setHinhThucGiam(hinhThucGiam);
            discount.setGiaTriGiam(new BigDecimal(giaTriGiamStr.replace(",", "").replace(".", "")));
            
            if (giaTriToiDaStr != null && !giaTriToiDaStr.trim().isEmpty()) {
                discount.setGiaTriToiDa(new BigDecimal(giaTriToiDaStr.replace(",", "").replace(".", "")));
            }
            
            discount.setNgayBatDau(LocalDateTime.parse(ngayBatDauStr, DATETIME_FORMATTER));
            
            if (ngayKetThucStr != null && !ngayKetThucStr.trim().isEmpty()) {
                discount.setNgayKetThuc(LocalDateTime.parse(ngayKetThucStr, DATETIME_FORMATTER));
            }
            
            discount.setTrangThai(trangThai);
            
            if (soLanSuDungStr != null && !soLanSuDungStr.trim().isEmpty()) {
                discount.setSoLanSuDung(Integer.parseInt(soLanSuDungStr));
            }
            
            discount.setDaSuDung(0);

            boolean success = discountDAO.createDiscount(discount);

            if (success) {
                request.setAttribute("success", "Tạo mã giảm giá thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi tạo mã giảm giá");
            }

            response.sendRedirect("admin-discounts");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
            request.getRequestDispatcher("Views/admin/discount-form.jsp").forward(request, response);
        }
    }

    private void updateDiscount(HttpServletRequest request, HttpServletResponse response, DiscountDAO discountDAO)
            throws ServletException, IOException {
        String maGiamGiaStr = request.getParameter("maGiamGia");
        String maCode = request.getParameter("maCode");
        String tenGiamGia = request.getParameter("tenGiamGia");
        String moTa = request.getParameter("moTa");
        String loaiGiamGia = request.getParameter("loaiGiamGia");
        String hinhThucGiam = request.getParameter("hinhThucGiam");
        String giaTriGiamStr = request.getParameter("giaTriGiam");
        String giaTriToiDaStr = request.getParameter("giaTriToiDa");
        String ngayBatDauStr = request.getParameter("ngayBatDau");
        String ngayKetThucStr = request.getParameter("ngayKetThuc");
        String trangThai = request.getParameter("trangThai");
        String soLanSuDungStr = request.getParameter("soLanSuDung");

        if (maGiamGiaStr == null || maCode == null || maCode.trim().isEmpty() ||
            tenGiamGia == null || tenGiamGia.trim().isEmpty() ||
            loaiGiamGia == null || hinhThucGiam == null ||
            giaTriGiamStr == null || giaTriGiamStr.trim().isEmpty() ||
            ngayBatDauStr == null || ngayBatDauStr.trim().isEmpty() ||
            trangThai == null) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin bắt buộc");
            response.sendRedirect("admin-discounts");
            return;
        }

        try {
            int maGiamGia = Integer.parseInt(maGiamGiaStr);
            
            // Kiểm tra mã code đã tồn tại chưa (trừ mã hiện tại)
            if (discountDAO.isCodeExists(maCode.trim(), maGiamGia)) {
                request.setAttribute("error", "Mã code đã tồn tại. Vui lòng chọn mã khác");
                Discount discount = discountDAO.getDiscountById(maGiamGia);
                request.setAttribute("discount", discount);
                request.getRequestDispatcher("Views/admin/discount-form.jsp").forward(request, response);
                return;
            }

            Discount current = discountDAO.getDiscountById(maGiamGia);
            if (current == null) {
                response.sendRedirect("admin-discounts");
                return;
            }

            Discount discount = new Discount();
            discount.setMaGiamGia(maGiamGia);
            discount.setMaCode(maCode.trim().toUpperCase());
            discount.setTenGiamGia(tenGiamGia.trim());
            discount.setMoTa(moTa != null ? moTa.trim() : null);
            discount.setLoaiGiamGia(loaiGiamGia);
            discount.setHinhThucGiam(hinhThucGiam);
            discount.setGiaTriGiam(new BigDecimal(giaTriGiamStr.replace(",", "").replace(".", "")));
            
            if (giaTriToiDaStr != null && !giaTriToiDaStr.trim().isEmpty()) {
                discount.setGiaTriToiDa(new BigDecimal(giaTriToiDaStr.replace(",", "").replace(".", "")));
            }
            
            discount.setNgayBatDau(LocalDateTime.parse(ngayBatDauStr, DATETIME_FORMATTER));
            
            if (ngayKetThucStr != null && !ngayKetThucStr.trim().isEmpty()) {
                discount.setNgayKetThuc(LocalDateTime.parse(ngayKetThucStr, DATETIME_FORMATTER));
            }
            
            discount.setTrangThai(trangThai);
            
            if (soLanSuDungStr != null && !soLanSuDungStr.trim().isEmpty()) {
                discount.setSoLanSuDung(Integer.parseInt(soLanSuDungStr));
            }
            
            discount.setDaSuDung(current.getDaSuDung());
            discount.setCreatedAt(current.getCreatedAt());

            boolean success = discountDAO.updateDiscount(discount);

            if (success) {
                request.setAttribute("success", "Cập nhật mã giảm giá thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật mã giảm giá");
            }

            response.sendRedirect("admin-discounts");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
            response.sendRedirect("admin-discounts");
        }
    }

    private void deleteDiscount(HttpServletRequest request, HttpServletResponse response, DiscountDAO discountDAO)
            throws ServletException, IOException {
        String maGiamGiaStr = request.getParameter("maGiamGia");
        
        if (maGiamGiaStr == null) {
            response.sendRedirect("admin-discounts");
            return;
        }

        try {
            int maGiamGia = Integer.parseInt(maGiamGiaStr);
            boolean success = discountDAO.deleteDiscount(maGiamGia);
            
            if (success) {
                request.setAttribute("success", "Vô hiệu hóa mã giảm giá thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi vô hiệu hóa mã giảm giá");
            }
            
            response.sendRedirect("admin-discounts");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID mã giảm giá không hợp lệ");
            response.sendRedirect("admin-discounts");
        }
    }
}

