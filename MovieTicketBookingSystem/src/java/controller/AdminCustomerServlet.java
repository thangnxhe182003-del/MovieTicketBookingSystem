package controller;

import dal.CustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Customer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class AdminCustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) action = "list";
        
        CustomerDAO customerDAO = new CustomerDAO();
        
        switch (action) {
            case "list":
                listCustomers(request, response, customerDAO);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response, customerDAO);
                break;
            case "delete":
                deleteCustomer(request, response, customerDAO);
                break;
            case "search":
                searchCustomers(request, response, customerDAO);
                break;
            case "status":
                updateCustomerStatus(request, response, customerDAO);
                break;
            default:
                listCustomers(request, response, customerDAO);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        CustomerDAO customerDAO = new CustomerDAO();
        
        switch (action) {
            case "add":
                createCustomer(request, response, customerDAO);
                break;
            case "update":
                updateCustomer(request, response, customerDAO);
                break;
            default:
                listCustomers(request, response, customerDAO);
        }
    }

    /**
     * Hiển thị danh sách khách hàng
     */
    private void listCustomers(HttpServletRequest request, HttpServletResponse response, CustomerDAO customerDAO)
            throws ServletException, IOException {
        List<Customer> customers = customerDAO.getAllCustomers();
        
        // Thống kê
        long totalCustomers = customers.size();
        long activeCustomers = customers.stream().filter(c -> "Active".equals(c.getTrangThai())).count();
        long pendingCustomers = customers.stream().filter(c -> "Pending".equals(c.getTrangThai())).count();
        long verifiedCustomers = customers.stream().filter(Customer::isIsEmailVerified).count();
        
        request.setAttribute("customers", customers);
        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("activeCustomers", activeCustomers);
        request.setAttribute("pendingCustomers", pendingCustomers);
        request.setAttribute("verifiedCustomers", verifiedCustomers);
        
        request.getRequestDispatcher("Views/admin/customer-list.jsp").forward(request, response);
    }

    /**
     * Hiển thị form thêm khách hàng
     */
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("Views/admin/customer-form.jsp").forward(request, response);
    }

    /**
     * Hiển thị form sửa khách hàng
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, CustomerDAO customerDAO)
            throws ServletException, IOException {
        try {
            int maKH = Integer.parseInt(request.getParameter("maKH"));
            Customer customer = customerDAO.getCustomerById(maKH);
            
            if (customer == null) {
                request.setAttribute("error", "Không tìm thấy khách hàng");
                listCustomers(request, response, customerDAO);
                return;
            }

            request.setAttribute("customer", customer);
            request.getRequestDispatcher("Views/admin/customer-form.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mã khách hàng không hợp lệ");
            listCustomers(request, response, customerDAO);
        }
    }

    /**
     * Tạo khách hàng mới
     */
    private void createCustomer(HttpServletRequest request, HttpServletResponse response, CustomerDAO customerDAO)
            throws ServletException, IOException {
        
        String tenDangNhap = request.getParameter("tenDangNhap");
        String matKhau = request.getParameter("matKhau");
        String hoTen = request.getParameter("hoTen");
        String ngaySinh = request.getParameter("ngaySinh");
        String gioiTinh = request.getParameter("gioiTinh");
        String soDienThoai = request.getParameter("soDienThoai");
        String email = request.getParameter("email");
        String trangThai = request.getParameter("trangThai");
        
        // Validation
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty() ||
            matKhau == null || matKhau.trim().isEmpty() ||
            hoTen == null || hoTen.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {
            
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin bắt buộc");
            showAddForm(request, response);
            return;
        }
        
        // Kiểm tra username và email trùng
        if (customerDAO.usernameExists(tenDangNhap, 0)) {
            request.setAttribute("error", "Tên đăng nhập đã tồn tại");
            showAddForm(request, response);
            return;
        }
        
        if (customerDAO.emailExists(email, 0)) {
            request.setAttribute("error", "Email đã tồn tại");
            showAddForm(request, response);
            return;
        }
        
        // Tạo customer object
        Customer customer = new Customer();
        customer.setTenDangNhap(tenDangNhap.trim());
        customer.setMatKhau(matKhau.trim());
        customer.setHoTen(hoTen.trim());
        customer.setNgaySinh(ngaySinh != null ? ngaySinh.trim() : "");
        customer.setGioiTinh(gioiTinh != null ? gioiTinh.trim() : "");
        customer.setSoDienThoai(soDienThoai != null ? soDienThoai.trim() : "");
        customer.setEmail(email.trim());
        customer.setIsEmailVerified(false);
        customer.setTrangThai(trangThai != null ? trangThai : "Pending");
        
        if (customerDAO.addCustomer(customer)) {
            request.setAttribute("success", "Thêm khách hàng thành công");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi thêm khách hàng");
        }
        
        listCustomers(request, response, customerDAO);
    }

    /**
     * Cập nhật khách hàng
     */
    private void updateCustomer(HttpServletRequest request, HttpServletResponse response, CustomerDAO customerDAO)
            throws ServletException, IOException {
        
        try {
            int maKH = Integer.parseInt(request.getParameter("maKH"));
            String tenDangNhap = request.getParameter("tenDangNhap");
            String hoTen = request.getParameter("hoTen");
            String ngaySinh = request.getParameter("ngaySinh");
            String gioiTinh = request.getParameter("gioiTinh");
            String soDienThoai = request.getParameter("soDienThoai");
            String email = request.getParameter("email");
            String trangThai = request.getParameter("trangThai");
            boolean isEmailVerified = "true".equals(request.getParameter("isEmailVerified"));
            
            // Validation (không bao gồm mật khẩu)
            if (tenDangNhap == null || tenDangNhap.trim().isEmpty() ||
                hoTen == null || hoTen.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {
                
                request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin bắt buộc");
                showEditForm(request, response, customerDAO);
                return;
            }
            
            // Kiểm tra username và email trùng
            if (customerDAO.usernameExists(tenDangNhap, maKH)) {
                request.setAttribute("error", "Tên đăng nhập đã tồn tại");
                showEditForm(request, response, customerDAO);
                return;
            }
            
            if (customerDAO.emailExists(email, maKH)) {
                request.setAttribute("error", "Email đã tồn tại");
                showEditForm(request, response, customerDAO);
                return;
            }
            
            // Lấy customer hiện tại để giữ ngayTao
            Customer existingCustomer = customerDAO.getCustomerById(maKH);
            if (existingCustomer == null) {
                request.setAttribute("error", "Không tìm thấy khách hàng");
                listCustomers(request, response, customerDAO);
                return;
            }
            
            // Cập nhật customer object (không cập nhật mật khẩu)
            existingCustomer.setTenDangNhap(tenDangNhap.trim());
            // Bỏ qua setMatKhau - không cho phép cập nhật mật khẩu
            existingCustomer.setHoTen(hoTen.trim());
            existingCustomer.setNgaySinh(ngaySinh != null ? ngaySinh.trim() : "");
            existingCustomer.setGioiTinh(gioiTinh != null ? gioiTinh.trim() : "");
            existingCustomer.setSoDienThoai(soDienThoai != null ? soDienThoai.trim() : "");
            existingCustomer.setEmail(email.trim());
            existingCustomer.setIsEmailVerified(isEmailVerified);
            existingCustomer.setTrangThai(trangThai != null ? trangThai : "Pending");
            
            if (customerDAO.updateCustomer(existingCustomer)) {
                request.setAttribute("success", "Cập nhật khách hàng thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật khách hàng");
            }
            
            listCustomers(request, response, customerDAO);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mã khách hàng không hợp lệ");
            listCustomers(request, response, customerDAO);
        }
    }

    /**
     * Chuyển trạng thái khách hàng thành "Suspended" (soft delete)
     */
    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response, CustomerDAO customerDAO)
            throws ServletException, IOException {
        
        try {
            int maKH = Integer.parseInt(request.getParameter("maKH"));
            
            // Lấy thông tin customer hiện tại
            Customer customer = customerDAO.getCustomerById(maKH);
            if (customer == null) {
                request.setAttribute("error", "Không tìm thấy khách hàng");
                listCustomers(request, response, customerDAO);
                return;
            }
            
            // Chuyển trạng thái thành "Suspended" thay vì xóa
            customer.setTrangThai("Suspended");
            
            if (customerDAO.updateCustomer(customer)) {
                request.setAttribute("success", "Đã chuyển trạng thái khách hàng thành 'Suspended'");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật trạng thái khách hàng");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mã khách hàng không hợp lệ");
        }
        
        listCustomers(request, response, customerDAO);
    }

    /**
     * Tìm kiếm khách hàng
     */
    private void searchCustomers(HttpServletRequest request, HttpServletResponse response, CustomerDAO customerDAO)
            throws ServletException, IOException {
        
        String keyword = request.getParameter("keyword");
        List<Customer> customers;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            customers = customerDAO.searchCustomers(keyword.trim());
            request.setAttribute("searchKeyword", keyword.trim());
        } else {
            customers = customerDAO.getAllCustomers();
        }
        
        // Thống kê
        long totalCustomers = customers.size();
        long activeCustomers = customers.stream().filter(c -> "Active".equals(c.getTrangThai())).count();
        long pendingCustomers = customers.stream().filter(c -> "Pending".equals(c.getTrangThai())).count();
        long verifiedCustomers = customers.stream().filter(Customer::isIsEmailVerified).count();
        
        request.setAttribute("customers", customers);
        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("activeCustomers", activeCustomers);
        request.setAttribute("pendingCustomers", pendingCustomers);
        request.setAttribute("verifiedCustomers", verifiedCustomers);
        
        request.getRequestDispatcher("Views/admin/customer-list.jsp").forward(request, response);
    }

    /**
     * Cập nhật trạng thái khách hàng
     */
    private void updateCustomerStatus(HttpServletRequest request, HttpServletResponse response, CustomerDAO customerDAO)
            throws ServletException, IOException {
        
        try {
            int maKH = Integer.parseInt(request.getParameter("maKH"));
            String trangThai = request.getParameter("trangThai");
            
            if (customerDAO.updateCustomerStatus(maKH, trangThai)) {
                request.setAttribute("success", "Cập nhật trạng thái thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật trạng thái");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mã khách hàng không hợp lệ");
        }
        
        listCustomers(request, response, customerDAO);
    }
}
