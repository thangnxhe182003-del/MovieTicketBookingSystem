/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.CustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Customer;

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
        
        CustomerDAO dao = new CustomerDAO();
        Customer customer = dao.checkLogin(username, password);
        
        if (customer != null) {
            if (!customer.isIsEmailVerified()) {
                // Nếu chưa xác thực email: tạo (hoặc gia hạn) OTP và chuyển sang trang OTP
                String otp = dao.createToken(customer.getMaKH(), model.CustomerToken.PURPOSE_REGISTER_VERIFY, 10);
                boolean emailSent = util.EmailService.sendOtpEmail(customer.getEmail(), customer.getTenDangNhap(), otp);
                request.setAttribute("warning", "Tài khoản chưa kích hoạt. Vui lòng nhập OTP vừa gửi tới email.");
                request.setAttribute("email", customer.getEmail());
                request.getRequestDispatcher("Views/common/otppage.jsp").forward(request, response);
                return;
            }
            
            // Login successful
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", customer);
            
            // Remember me functionality
            if (remember) {
                // Implementation for remember me functionality
                // This would typically involve cookies
            }
            
            response.sendRedirect("home");
        } else {
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            request.setAttribute("username", username);
            request.getRequestDispatcher("Views/common/login.jsp").forward(request, response);
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
