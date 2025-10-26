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
import model.CustomerToken;

/**
 *
 * @author thang
 */
public class OTPVerifyServlet extends HttpServlet {

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
        String email = request.getParameter("email");
        if (email != null && !email.isEmpty()) {
            request.setAttribute("email", email);
        }
        request.getRequestDispatcher("Views/common/otppage.jsp").forward(request, response);
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

        String email = request.getParameter("email");
        String inputOtp = request.getParameter("otp");
        String purpose = request.getParameter("purpose");
        
        // Default purpose is register verification if not specified
        if (purpose == null || purpose.isEmpty()) {
            purpose = CustomerToken.PURPOSE_REGISTER_VERIFY;
        }

        if (email == null || inputOtp == null || inputOtp.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            request.setAttribute("email", email);
            request.getRequestDispatcher("Views/common/otppage.jsp").forward(request, response);
            return;
        }

        CustomerDAO dao = new CustomerDAO();
        int maKH = dao.verifyTokenByEmail(email, inputOtp.trim(), purpose);

        if (maKH > 0) {
            // Token is valid
            if (CustomerToken.PURPOSE_REGISTER_VERIFY.equals(purpose)) {
                // For registration verification
                boolean activated = dao.activateCustomer(maKH);
                if (activated) {
                    request.setAttribute("success", "Đăng ký và kích hoạt tài khoản thành công! Bạn có thể đăng nhập ngay.");
                    request.getRequestDispatcher("Views/common/login.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Kích hoạt tài khoản thất bại. Vui lòng liên hệ admin!");
                    request.setAttribute("email", email);
                    request.getRequestDispatcher("Views/common/otppage.jsp").forward(request, response);
                }
            } else if (CustomerToken.PURPOSE_PASSWORD_RESET.equals(purpose)) {
                // For password reset
                request.setAttribute("success", "Xác thực OTP thành công! Vui lòng đặt mật khẩu mới.");
                request.setAttribute("maKH", maKH);
                request.setAttribute("email", email);
                request.getRequestDispatcher("Views/common/reset-password.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Mã OTP không đúng hoặc đã hết hạn. Vui lòng thử lại!");
            request.setAttribute("email", email);
            request.getRequestDispatcher("Views/common/otppage.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "OTP Verification Servlet";
    }
}