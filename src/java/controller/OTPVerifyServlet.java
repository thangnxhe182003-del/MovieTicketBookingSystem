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

/**
 *
 * @author thang
 */
public class OTPVerifyServlet extends HttpServlet {

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

        if (email == null || inputOtp == null || inputOtp.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            request.getRequestDispatcher("otppage.jsp").forward(request, response);
            return;
        }

        CustomerDAO dao = new CustomerDAO();
        boolean verified = dao.verifyAndActivateOTP(email, inputOtp.trim());

        if (verified) {
            // Kích hoạt thành công, chuyển đến login.jsp với message
            request.setAttribute("success", "Đăng ký và kích hoạt tài khoản thành công! Bạn có thể đăng nhập ngay.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Mã OTP không đúng hoặc đã hết hạn. Vui lòng thử lại!");
            request.getRequestDispatcher("otppage.jsp").forward(request, response);
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
