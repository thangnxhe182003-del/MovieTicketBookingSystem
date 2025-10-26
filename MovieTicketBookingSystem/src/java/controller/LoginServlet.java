/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.CustomerDAO;
import dal.StaffDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Customer;
import model.Staff;

/**
 *
 * @author thang
 */
public class LoginServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("Views/common/login.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean remember = request.getParameter("remember") != null;
        
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            request.getRequestDispatcher("Views/common/login.jsp").forward(request, response);
            return;
        }
        
        // Thử đăng nhập với Customer trước
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.checkLogin(username, password);
        
        if (customer != null) {
            // Kiểm tra trạng thái customer
            if (!"Active".equals(customer.getTrangThai())) {
                String statusMessage = getStatusMessage(customer.getTrangThai());
                request.setAttribute("error", statusMessage);
                request.setAttribute("username", username);
                request.getRequestDispatcher("Views/common/login.jsp").forward(request, response);
                return;
            }
            
            if (!customer.isIsEmailVerified()) {
                // Nếu chưa xác thực email: tạo (hoặc gia hạn) OTP và chuyển sang trang OTP
                String otp = customerDAO.createToken(customer.getMaKH(), model.CustomerToken.PURPOSE_REGISTER_VERIFY, 10);
                boolean emailSent = util.EmailService.sendOtpEmail(customer.getEmail(), customer.getTenDangNhap(), otp);
                request.setAttribute("warning", "Tài khoản chưa kích hoạt. Vui lòng nhập OTP vừa gửi tới email.");
                request.setAttribute("email", customer.getEmail());
                request.getRequestDispatcher("Views/common/otppage.jsp").forward(request, response);
                return;
            }
            
            // Login successful cho customer
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", customer);
            
            // Remember me functionality
            if (remember) {
                // Implementation for remember me functionality
                // This would typically involve cookies
            }
            
            response.sendRedirect("home");
            return;
        }
        
        // Nếu không phải customer, thử đăng nhập với Staff
        StaffDAO staffDAO = new StaffDAO();
        Staff staff = staffDAO.checkLogin(username, password);
        
        if (staff != null) {
            // Kiểm tra trạng thái staff
            if (!"Active".equals(staff.getTrangThai())) {
                String statusMessage = getStatusMessage(staff.getTrangThai());
                request.setAttribute("error", statusMessage);
                request.setAttribute("username", username);
                request.getRequestDispatcher("Views/common/login.jsp").forward(request, response);
                return;
            }
            
            // Login successful cho staff
            HttpSession session = request.getSession();
            session.setAttribute("loggedInStaff", staff);
            
            // Redirect đến admin dashboard
            response.sendRedirect("admin-dashboard");
            return;
        }
        
        // Nếu không tìm thấy user nào
        request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
        request.setAttribute("username", username);
        request.getRequestDispatcher("Views/common/login.jsp").forward(request, response);
    }

    /**
     * Lấy thông báo lỗi dựa trên trạng thái tài khoản
     */
    private String getStatusMessage(String trangThai) {
        switch (trangThai) {
            case "Pending":
                return "Tài khoản đang chờ kích hoạt. Vui lòng kiểm tra email để xác thực tài khoản.";
            case "Suspended":
                return "Tài khoản đã bị tạm dừng. Vui lòng liên hệ quản trị viên để được hỗ trợ.";
            case "Inactive":
                return "Tài khoản đã bị vô hiệu hóa. Vui lòng liên hệ quản trị viên để được hỗ trợ.";
            case "Deleted":
                return "Tài khoản đã bị xóa. Vui lòng liên hệ quản trị viên để được hỗ trợ.";
            default:
                return "Tài khoản không thể đăng nhập. Vui lòng liên hệ quản trị viên để được hỗ trợ.";
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Login Servlet";
    }
}
