package controller;

import dal.CinemaDAO;
import dal.StaffDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Staff;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class AdminStaffServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chỉ Admin mới được truy cập
        if (!util.RoleChecker.requireAdmin(request, response)) return;
        
        String action = request.getParameter("action");
        if (action == null) action = "list";
        
        StaffDAO staffDAO = new StaffDAO();
        
        switch (action) {
            case "list":
                listStaff(request, response, staffDAO);
                break;
            case "add":
                showAddForm(request, response, staffDAO);
                break;
            case "edit":
                showEditForm(request, response, staffDAO);
                break;
            case "delete":
                deleteStaff(request, response, staffDAO);
                break;
            case "search":
                searchStaff(request, response, staffDAO);
                break;
            case "status":
                updateStaffStatus(request, response, staffDAO);
                break;
            default:
                listStaff(request, response, staffDAO);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chỉ Admin mới được truy cập
        if (!util.RoleChecker.requireAdmin(request, response)) return;
        
        String action = request.getParameter("action");
        StaffDAO staffDAO = new StaffDAO();
        
        switch (action) {
            case "add":
                createStaff(request, response, staffDAO);
                break;
            case "update":
                updateStaff(request, response, staffDAO);
                break;
            default:
                listStaff(request, response, staffDAO);
        }
    }

    /**
     * Hiển thị danh sách nhân viên
     */
    private void listStaff(HttpServletRequest request, HttpServletResponse response, StaffDAO staffDAO)
            throws ServletException, IOException {
        List<Staff> staffList = staffDAO.getAllStaff();
        
        // Format dates for JSP display
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Staff staff : staffList) {
            if (staff.getNgayTao() != null) {
                String formattedDate = staff.getNgayTao().format(formatter);
                request.setAttribute("staffDate_" + staff.getMaNhanVien(), formattedDate);
            }
        }
        
        // Thống kê
        long totalStaff = staffList.size();
        long activeStaff = staffList.stream().filter(s -> "Active".equals(s.getTrangThai())).count();
        long adminCount = staffList.stream().filter(s -> "Admin".equals(s.getChucVu())).count();
        long managerCount = staffList.stream().filter(s -> "Manager".equals(s.getChucVu())).count();
        long staffCount = staffList.stream().filter(s -> "Staff".equals(s.getChucVu())).count();
        
        request.setAttribute("staffList", staffList);
        request.setAttribute("totalStaff", totalStaff);
        request.setAttribute("activeStaff", activeStaff);
        request.setAttribute("adminCount", adminCount);
        request.setAttribute("managerCount", managerCount);
        request.setAttribute("staffCount", staffCount);
        
        request.getRequestDispatcher("Views/admin/staff-list.jsp").forward(request, response);
    }

    /**
     * Hiển thị form thêm nhân viên
     */
    private void showAddForm(HttpServletRequest request, HttpServletResponse response, StaffDAO staffDAO)
            throws ServletException, IOException {
        // Lấy danh sách rạp phim
        CinemaDAO cinemaDAO = new CinemaDAO();
        request.setAttribute("cinemas", cinemaDAO.getAllCinemas());
        
        List<String> positions = staffDAO.getDistinctPositions();
        request.setAttribute("positions", positions);
        request.getRequestDispatcher("Views/admin/staff-form.jsp").forward(request, response);
    }

    /**
     * Hiển thị form sửa nhân viên
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, StaffDAO staffDAO)
            throws ServletException, IOException {
        try {
            int maNhanVien = Integer.parseInt(request.getParameter("maNhanVien"));
            Staff staff = staffDAO.getStaffById(maNhanVien);
            
            if (staff == null) {
                request.setAttribute("error", "Không tìm thấy nhân viên");
                listStaff(request, response, staffDAO);
                return;
            }

            // Lấy danh sách rạp phim
            CinemaDAO cinemaDAO = new CinemaDAO();
            request.setAttribute("cinemas", cinemaDAO.getAllCinemas());
            
            List<String> positions = staffDAO.getDistinctPositions();
            request.setAttribute("staff", staff);
            request.setAttribute("positions", positions);
            request.getRequestDispatcher("Views/admin/staff-form.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mã nhân viên không hợp lệ");
            listStaff(request, response, staffDAO);
        }
    }

    /**
     * Tạo nhân viên mới
     */
    private void createStaff(HttpServletRequest request, HttpServletResponse response, StaffDAO staffDAO)
            throws ServletException, IOException {
        
        String maRapStr = request.getParameter("maRap");
        String tenDangNhap = request.getParameter("tenDangNhap");
        String matKhau = request.getParameter("matKhau");
        String hoTen = request.getParameter("hoTen");
        String soDienThoai = request.getParameter("soDienThoai");
        String email = request.getParameter("email");
        String chucVu = request.getParameter("chucVu");
        String trangThai = request.getParameter("trangThai");
        
        // Validation
        if (maRapStr == null || maRapStr.trim().isEmpty() ||
            tenDangNhap == null || tenDangNhap.trim().isEmpty() ||
            matKhau == null || matKhau.trim().isEmpty() ||
            hoTen == null || hoTen.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            chucVu == null || chucVu.trim().isEmpty()) {
            
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin bắt buộc");
            showAddForm(request, response, staffDAO);
            return;
        }
        
        // Kiểm tra username và email trùng
        if (staffDAO.usernameExists(tenDangNhap, 0)) {
            request.setAttribute("error", "Tên đăng nhập đã tồn tại");
            showAddForm(request, response, staffDAO);
            return;
        }
        
        if (staffDAO.emailExists(email, 0)) {
            request.setAttribute("error", "Email đã tồn tại");
            showAddForm(request, response, staffDAO);
            return;
        }
        
        // Parse maRap
        int maRap;
        try {
            maRap = Integer.parseInt(maRapStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mã rạp phim không hợp lệ");
            showAddForm(request, response, staffDAO);
            return;
        }
        
        // Tạo staff object
        Staff staff = new Staff();
        staff.setMaRap(maRap);
        staff.setTenDangNhap(tenDangNhap.trim());
        staff.setMatKhau(matKhau.trim());
        staff.setHoTen(hoTen.trim());
        staff.setSoDienThoai(soDienThoai != null ? soDienThoai.trim() : "");
        staff.setEmail(email.trim());
        staff.setChucVu(chucVu.trim());
        staff.setTrangThai(trangThai != null ? trangThai : "Active");
        
        if (staffDAO.addStaff(staff)) {
            request.setAttribute("success", "Thêm nhân viên thành công");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi thêm nhân viên");
        }
        
        listStaff(request, response, staffDAO);
    }

    /**
     * Cập nhật nhân viên
     */
    private void updateStaff(HttpServletRequest request, HttpServletResponse response, StaffDAO staffDAO)
            throws ServletException, IOException {
        
        try {
            int maNhanVien = Integer.parseInt(request.getParameter("maNhanVien"));
            String maRapStr = request.getParameter("maRap");
            String tenDangNhap = request.getParameter("tenDangNhap");
            String matKhau = request.getParameter("matKhau");
            String hoTen = request.getParameter("hoTen");
            String soDienThoai = request.getParameter("soDienThoai");
            String email = request.getParameter("email");
            String chucVu = request.getParameter("chucVu");
            String trangThai = request.getParameter("trangThai");
            
            // Validation
            if (maRapStr == null || maRapStr.trim().isEmpty() ||
                tenDangNhap == null || tenDangNhap.trim().isEmpty() ||
                matKhau == null || matKhau.trim().isEmpty() ||
                hoTen == null || hoTen.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                chucVu == null || chucVu.trim().isEmpty()) {
                
                request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin bắt buộc");
                showEditForm(request, response, staffDAO);
                return;
            }
            
            // Parse maRap
            int maRap;
            try {
                maRap = Integer.parseInt(maRapStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Mã rạp phim không hợp lệ");
                showEditForm(request, response, staffDAO);
                return;
            }
            
            // Kiểm tra username và email trùng
            if (staffDAO.usernameExists(tenDangNhap, maNhanVien)) {
                request.setAttribute("error", "Tên đăng nhập đã tồn tại");
                showEditForm(request, response, staffDAO);
                return;
            }
            
            if (staffDAO.emailExists(email, maNhanVien)) {
                request.setAttribute("error", "Email đã tồn tại");
                showEditForm(request, response, staffDAO);
                return;
            }
            
            // Lấy staff hiện tại để giữ ngayTao
            Staff existingStaff = staffDAO.getStaffById(maNhanVien);
            if (existingStaff == null) {
                request.setAttribute("error", "Không tìm thấy nhân viên");
                listStaff(request, response, staffDAO);
                return;
            }
            
            // Cập nhật staff object
            existingStaff.setMaRap(maRap);
            existingStaff.setTenDangNhap(tenDangNhap.trim());
            existingStaff.setMatKhau(matKhau.trim());
            existingStaff.setHoTen(hoTen.trim());
            existingStaff.setSoDienThoai(soDienThoai != null ? soDienThoai.trim() : "");
            existingStaff.setEmail(email.trim());
            existingStaff.setChucVu(chucVu.trim());
            existingStaff.setTrangThai(trangThai != null ? trangThai : "Active");
            
            if (staffDAO.updateStaff(existingStaff)) {
                request.setAttribute("success", "Cập nhật nhân viên thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật nhân viên");
            }
            
            listStaff(request, response, staffDAO);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mã nhân viên không hợp lệ");
            listStaff(request, response, staffDAO);
        }
    }

    /**
     * Xóa nhân viên
     */
    private void deleteStaff(HttpServletRequest request, HttpServletResponse response, StaffDAO staffDAO)
            throws ServletException, IOException {
        
        try {
            int maNhanVien = Integer.parseInt(request.getParameter("maNhanVien"));
            
            if (staffDAO.deleteStaff(maNhanVien)) {
                request.setAttribute("success", "Xóa nhân viên thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi xóa nhân viên");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mã nhân viên không hợp lệ");
        }
        
        listStaff(request, response, staffDAO);
    }

    /**
     * Tìm kiếm nhân viên
     */
    private void searchStaff(HttpServletRequest request, HttpServletResponse response, StaffDAO staffDAO)
            throws ServletException, IOException {
        
        String keyword = request.getParameter("keyword");
        List<Staff> staffList;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            staffList = staffDAO.searchStaff(keyword.trim());
            request.setAttribute("searchKeyword", keyword.trim());
        } else {
            staffList = staffDAO.getAllStaff();
        }
        
        // Format dates for JSP display
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Staff staff : staffList) {
            if (staff.getNgayTao() != null) {
                String formattedDate = staff.getNgayTao().format(formatter);
                request.setAttribute("staffDate_" + staff.getMaNhanVien(), formattedDate);
            }
        }
        
        // Thống kê
        long totalStaff = staffList.size();
        long activeStaff = staffList.stream().filter(s -> "Active".equals(s.getTrangThai())).count();
        long adminCount = staffList.stream().filter(s -> "Admin".equals(s.getChucVu())).count();
        long managerCount = staffList.stream().filter(s -> "Manager".equals(s.getChucVu())).count();
        long staffCount = staffList.stream().filter(s -> "Staff".equals(s.getChucVu())).count();
        
        request.setAttribute("staffList", staffList);
        request.setAttribute("totalStaff", totalStaff);
        request.setAttribute("activeStaff", activeStaff);
        request.setAttribute("adminCount", adminCount);
        request.setAttribute("managerCount", managerCount);
        request.setAttribute("staffCount", staffCount);
        
        request.getRequestDispatcher("Views/admin/staff-list.jsp").forward(request, response);
    }

    /**
     * Cập nhật trạng thái nhân viên
     */
    private void updateStaffStatus(HttpServletRequest request, HttpServletResponse response, StaffDAO staffDAO)
            throws ServletException, IOException {
        
        try {
            int maNhanVien = Integer.parseInt(request.getParameter("maNhanVien"));
            String trangThai = request.getParameter("trangThai");
            
            if (staffDAO.updateStaffStatus(maNhanVien, trangThai)) {
                request.setAttribute("success", "Cập nhật trạng thái thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật trạng thái");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mã nhân viên không hợp lệ");
        }
        
        listStaff(request, response, staffDAO);
    }
}
