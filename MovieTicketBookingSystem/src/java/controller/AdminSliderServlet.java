package controller;

import dal.SliderDAO;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import model.Slider;

@MultipartConfig
public class AdminSliderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }

        SliderDAO sliderDAO = new SliderDAO();

        switch (action) {
            case "list":
                listSliders(request, response, sliderDAO);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response, sliderDAO);
                break;
            default:
                listSliders(request, response, sliderDAO);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendRedirect("admin-sliders");
            return;
        }

        SliderDAO sliderDAO = new SliderDAO();

        switch (action) {
            case "create":
                createSlider(request, response, sliderDAO);
                break;
            case "update":
                updateSlider(request, response, sliderDAO);
                break;
            case "delete":
                deleteSlider(request, response, sliderDAO);
                break;
            default:
                response.sendRedirect("admin-sliders");
        }
    }

    private void listSliders(HttpServletRequest request, HttpServletResponse response, SliderDAO sliderDAO)
            throws ServletException, IOException {
        List<Slider> sliders = sliderDAO.getAllSliders();
        
        // Format dates for JSP
        java.util.Map<Integer, String> ngayBatDauFormatted = new java.util.HashMap<>();
        java.util.Map<Integer, String> ngayKetThucFormatted = new java.util.HashMap<>();
        
        for (Slider slider : sliders) {
            if (slider.getNgayBatDau() != null) {
                ngayBatDauFormatted.put(slider.getMaSlider(), 
                    slider.getNgayBatDau().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
            if (slider.getNgayKetThuc() != null) {
                ngayKetThucFormatted.put(slider.getMaSlider(), 
                    slider.getNgayKetThuc().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
        }
        
        request.setAttribute("sliders", sliders);
        request.setAttribute("ngayBatDauFormatted", ngayBatDauFormatted);
        request.setAttribute("ngayKetThucFormatted", ngayKetThucFormatted);
        request.getRequestDispatcher("Views/admin/slider-list.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("Views/admin/slider-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, SliderDAO sliderDAO)
            throws ServletException, IOException {
        String maSliderStr = request.getParameter("maSlider");
        
        if (maSliderStr == null) {
            response.sendRedirect("admin-sliders");
            return;
        }

        try {
            int maSlider = Integer.parseInt(maSliderStr);
            Slider slider = sliderDAO.getSliderById(maSlider);
            
            if (slider == null) {
                request.setAttribute("error", "Không tìm thấy slider");
                response.sendRedirect("admin-sliders");
                return;
            }

            // Format datetime cho JSP
            if (slider.getNgayBatDau() != null) {
                String ngayBatDauFormatted = slider.getNgayBatDau().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                request.setAttribute("ngayBatDauFormatted", ngayBatDauFormatted);
            }
            
            if (slider.getNgayKetThuc() != null) {
                String ngayKetThucFormatted = slider.getNgayKetThuc().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                request.setAttribute("ngayKetThucFormatted", ngayKetThucFormatted);
            }

            request.setAttribute("slider", slider);
            request.getRequestDispatcher("Views/admin/slider-form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("admin-sliders");
        }
    }

    private void createSlider(HttpServletRequest request, HttpServletResponse response, SliderDAO sliderDAO)
            throws ServletException, IOException {
        String tieuDe = request.getParameter("tieuDe");
        String moTa = request.getParameter("moTa");
        String thuTuHienThiStr = request.getParameter("thuTuHienThi");
        String trangThai = request.getParameter("trangThai");
        String ngayBatDauStr = request.getParameter("ngayBatDau");
        String ngayKetThucStr = request.getParameter("ngayKetThuc");
        Part anhSlidePart = null;
        try { anhSlidePart = request.getPart("anhSlideFile"); } catch (IllegalStateException ex) {}

        if (tieuDe == null || thuTuHienThiStr == null || trangThai == null) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin");
            request.getRequestDispatcher("Views/admin/slider-form.jsp").forward(request, response);
            return;
        }

        try {
            int thuTuHienThi = Integer.parseInt(thuTuHienThiStr);

            String anhSlideFileName = null;
            if (anhSlidePart != null && anhSlidePart.getSize() > 0) {
                String submitted = getSubmittedFileName(anhSlidePart);
                if (submitted != null && !submitted.isBlank()) {
                    anhSlideFileName = submitted;
                    saveUploadedFile(anhSlidePart, getUploadDir(), anhSlideFileName);
                }
            }

            if (anhSlideFileName == null || anhSlideFileName.isBlank()) {
                request.setAttribute("error", "Vui lòng chọn hình ảnh slider");
                request.getRequestDispatcher("Views/admin/slider-form.jsp").forward(request, response);
                return;
            }

            LocalDateTime ngayBatDau = null;
            if (ngayBatDauStr != null && !ngayBatDauStr.trim().isEmpty()) {
                ngayBatDau = LocalDateTime.parse(ngayBatDauStr.replace(" ", "T"));
            } else {
                ngayBatDau = LocalDateTime.now();
            }

            LocalDateTime ngayKetThuc = null;
            if (ngayKetThucStr != null && !ngayKetThucStr.trim().isEmpty()) {
                ngayKetThuc = LocalDateTime.parse(ngayKetThucStr.replace(" ", "T"));
            }

            Slider slider = new Slider();
            slider.setTieuDe(tieuDe);
            slider.setMoTa(moTa);
            slider.setAnhSlide(anhSlideFileName);
            slider.setThuTuHienThi(thuTuHienThi);
            slider.setTrangThai(trangThai);
            slider.setNgayBatDau(ngayBatDau);
            slider.setNgayKetThuc(ngayKetThuc);

            boolean success = sliderDAO.createSlider(slider);

            if (success) {
                request.setAttribute("success", "Tạo slider thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi tạo slider");
            }

            response.sendRedirect("admin-sliders");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Thứ tự hiển thị không hợp lệ");
            request.getRequestDispatcher("Views/admin/slider-form.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("Views/admin/slider-form.jsp").forward(request, response);
        }
    }

    private void updateSlider(HttpServletRequest request, HttpServletResponse response, SliderDAO sliderDAO)
            throws ServletException, IOException {
        String maSliderStr = request.getParameter("maSlider");
        String tieuDe = request.getParameter("tieuDe");
        String moTa = request.getParameter("moTa");
        String thuTuHienThiStr = request.getParameter("thuTuHienThi");
        String trangThai = request.getParameter("trangThai");
        String ngayBatDauStr = request.getParameter("ngayBatDau");
        String ngayKetThucStr = request.getParameter("ngayKetThuc");
        Part anhSlidePart = null;
        try { anhSlidePart = request.getPart("anhSlideFile"); } catch (IllegalStateException ex) {}

        if (maSliderStr == null || tieuDe == null || thuTuHienThiStr == null || trangThai == null) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin");
            response.sendRedirect("admin-sliders");
            return;
        }

        try {
            int maSlider = Integer.parseInt(maSliderStr);
            int thuTuHienThi = Integer.parseInt(thuTuHienThiStr);

            Slider current = sliderDAO.getSliderById(maSlider);
            String anhSlideFileName = (current != null) ? current.getAnhSlide() : null;
            if (anhSlidePart != null && anhSlidePart.getSize() > 0) {
                String submitted = getSubmittedFileName(anhSlidePart);
                if (submitted != null && !submitted.isBlank()) {
                    anhSlideFileName = submitted;
                    saveUploadedFile(anhSlidePart, getUploadDir(), anhSlideFileName);
                }
            }

            LocalDateTime ngayBatDau = null;
            if (ngayBatDauStr != null && !ngayBatDauStr.trim().isEmpty()) {
                ngayBatDau = LocalDateTime.parse(ngayBatDauStr.replace(" ", "T"));
            } else {
                ngayBatDau = LocalDateTime.now();
            }

            LocalDateTime ngayKetThuc = null;
            if (ngayKetThucStr != null && !ngayKetThucStr.trim().isEmpty()) {
                ngayKetThuc = LocalDateTime.parse(ngayKetThucStr.replace(" ", "T"));
            }

            Slider slider = new Slider();
            slider.setMaSlider(maSlider);
            slider.setTieuDe(tieuDe);
            slider.setMoTa(moTa);
            slider.setAnhSlide(anhSlideFileName);
            slider.setThuTuHienThi(thuTuHienThi);
            slider.setTrangThai(trangThai);
            slider.setNgayBatDau(ngayBatDau);
            slider.setNgayKetThuc(ngayKetThuc);

            boolean success = sliderDAO.updateSlider(slider);

            if (success) {
                request.setAttribute("success", "Cập nhật slider thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật slider");
            }

            response.sendRedirect("admin-sliders");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ");
            response.sendRedirect("admin-sliders");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            response.sendRedirect("admin-sliders");
        }
    }

    private void deleteSlider(HttpServletRequest request, HttpServletResponse response, SliderDAO sliderDAO)
            throws ServletException, IOException {
        String maSliderStr = request.getParameter("maSlider");
        
        if (maSliderStr == null) {
            response.sendRedirect("admin-sliders");
            return;
        }

        try {
            int maSlider = Integer.parseInt(maSliderStr);
            boolean success = sliderDAO.deleteSlider(maSlider);
            
            if (success) {
                request.setAttribute("success", "Vô hiệu hóa slider thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi vô hiệu hóa slider");
            }
            
            response.sendRedirect("admin-sliders");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID slider không hợp lệ");
            response.sendRedirect("admin-sliders");
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

