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
 * Servlet to handle the password reset form submission
 * 
 * @author thang
 */
public class ResetPasswordServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     * Process the password reset form
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
        
        String maKHStr = request.getParameter("maKH");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        if (maKHStr == null || password == null || confirmPassword == null || 
                maKHStr.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            request.setAttribute("maKH", maKHStr);
            request.getRequestDispatcher("Views/common/reset-password.jsp").forward(request, response);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            request.setAttribute("maKH", maKHStr);
            request.getRequestDispatcher("Views/common/reset-password.jsp").forward(request, response);
            return;
        }
        
        if (password.length() < 6) {
            request.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
            request.setAttribute("maKH", maKHStr);
            request.getRequestDispatcher("Views/common/reset-password.jsp").forward(request, response);
            return;
        }
        
        try {
            int maKH = Integer.parseInt(maKHStr);
            CustomerDAO dao = new CustomerDAO();
            boolean updated = dao.updatePassword(maKH, password);
            
            if (updated) {
                request.setAttribute("success", "Đặt lại mật khẩu thành công! Bạn có thể đăng nhập ngay bây giờ.");
                request.getRequestDispatcher("Views/common/login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Đặt lại mật khẩu thất bại. Vui lòng thử lại!");
                request.setAttribute("maKH", maKHStr);
                request.getRequestDispatcher("Views/common/reset-password.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Thông tin không hợp lệ!");
            request.getRequestDispatcher("Views/common/forgot-password.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Reset Password Servlet";
    }
}
