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
import java.io.IOException;
import model.Customer;
import model.CustomerToken;
import util.EmailService;

/**
 * Servlet to handle password reset requests
 * 
 * @author thang
 */
public class ForgotPasswordServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     * Shows the forgot password form
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("Views/common/forgot-password.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * Processes the forgot password request and sends OTP if email exists
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
        String email = request.getParameter("email");
        
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập email!");
            request.getRequestDispatcher("Views/common/forgot-password.jsp").forward(request, response);
            return;
        }
        
        CustomerDAO dao = new CustomerDAO();
        Customer customer = dao.getCustomerByEmail(email);
        
        if (customer != null) {
            // Customer exists, generate and send OTP
            String otp = dao.createToken(customer.getMaKH(), CustomerToken.PURPOSE_PASSWORD_RESET, 10);
            
            if (otp != null) {
                boolean emailSent = EmailService.sendPasswordResetEmail(email, customer.getTenDangNhap(), otp);
                
                if (emailSent) {
                    request.setAttribute("success", "Mã OTP đã được gửi đến email của bạn. Vui lòng kiểm tra và nhập mã để đặt lại mật khẩu.");
                    request.setAttribute("email", email);
                    request.setAttribute("purpose", CustomerToken.PURPOSE_PASSWORD_RESET);
                    request.getRequestDispatcher("Views/common/otppage.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Không thể gửi email. Vui lòng thử lại sau.");
                    request.getRequestDispatcher("Views/common/forgot-password.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "Lỗi khi tạo mã OTP. Vui lòng thử lại.");
                request.getRequestDispatcher("Views/common/forgot-password.jsp").forward(request, response);
            }
        } else {
            // For security reasons, don't reveal that the email doesn't exist
            request.setAttribute("success", "Nếu email tồn tại trong hệ thống, bạn sẽ nhận được hướng dẫn đặt lại mật khẩu.");
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
        return "Forgot Password Servlet";
    }
}
