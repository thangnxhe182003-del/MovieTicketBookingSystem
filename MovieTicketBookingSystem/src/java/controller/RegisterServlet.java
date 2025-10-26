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
 *
 * @author thang
 */
public class RegisterServlet extends HttpServlet {

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
        request.getRequestDispatcher("Views/common/register.jsp").forward(request, response);
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

        String hoTen = request.getParameter("hoten");
        String ngaySinhStr = request.getParameter("dob");
        String gioiTinh = request.getParameter("gender");
        String soDienThoai = request.getParameter("phone");
        String email = request.getParameter("email");
        String tenDangNhap = request.getParameter("username");
        String matKhau = request.getParameter("password");

        // Validate ngaySinh format
        String ngaySinh = null;
        if (ngaySinhStr != null && !ngaySinhStr.trim().isEmpty()) {
            try {
                java.sql.Date.valueOf(ngaySinhStr);  // Throw nếu format sai
                ngaySinh = ngaySinhStr;
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Định dạng ngày sinh không hợp lệ (yyyy-MM-dd)!");
                request.getRequestDispatcher("Views/common/register.jsp").forward(request, response);
                return;
            }
        }

        try {
            CustomerDAO dao = new CustomerDAO();

            if (dao.checkAccountExists(tenDangNhap, email, soDienThoai)) {
                // Nếu đã tồn tại user nhưng chưa xác thực email => gửi lại OTP và chuyển tới trang OTP
                Customer existing = dao.getByUsernameOrEmail(tenDangNhap, email);
                if (existing != null && !existing.isIsEmailVerified()) {
                    String otp = dao.createToken(existing.getMaKH(), CustomerToken.PURPOSE_REGISTER_VERIFY, 10);
                    boolean emailSent = EmailService.sendOtpEmail(existing.getEmail(), existing.getTenDangNhap(), otp);
                    if (!emailSent) {
                        request.setAttribute("warning", "Không gửi được OTP. Vui lòng thử lại hoặc liên hệ hỗ trợ.");
                    }
                    request.setAttribute("warning", "Tài khoản đã tồn tại nhưng chưa xác thực. Vui lòng nhập OTP gửi tới email.");
                    request.setAttribute("email", existing.getEmail());
                    request.getRequestDispatcher("Views/common/otppage.jsp").forward(request, response);
                    return;
                }
                request.setAttribute("error", "Tên đăng nhập, email hoặc số điện thoại đã tồn tại!");
                request.getRequestDispatcher("Views/common/register.jsp").forward(request, response);
            } else {
                Customer c = new Customer(tenDangNhap, matKhau, hoTen, ngaySinh, gioiTinh, soDienThoai, email);
                c.setTrangThai("Pending");

                boolean success = dao.insertCustomer(c);
                if (success) {
                    int maKH = dao.getLastInsertedMaKH();
                    System.out.println("Debug: Insert success, maKH=" + maKH);  // Log
                    if (maKH > 0) {
                        // Generate OTP with 10 minute expiry
                        String otp = dao.createToken(maKH, CustomerToken.PURPOSE_REGISTER_VERIFY, 10);
                        if (otp != null) {
                            boolean emailSent = EmailService.sendOtpEmail(email, tenDangNhap, otp);
                            System.out.println("Debug: Email sent=" + emailSent + ", OTP=" + otp);  // Log
                            if (!emailSent) {
                                request.setAttribute("warning", "Tài khoản tạo thành công nhưng gửi OTP thất bại. Vui lòng liên hệ admin.");
                            }
                        } else {
                            request.setAttribute("warning", "Tạo OTP thất bại. Vui lòng thử lại.");
                        }
                    } else {
                        request.setAttribute("warning", "Lấy ID user thất bại. Vui lòng thử lại.");
                    }

                    request.setAttribute("success", "🎉 Đăng ký thành công! Vui lòng kiểm tra email để lấy mã OTP.");
                    request.setAttribute("email", email);
                    request.getRequestDispatcher("Views/common/otppage.jsp").forward(request, response);
                } else {
                    System.out.println("Insert fail");  // Log
                    request.setAttribute("error", "Lỗi khi tạo tài khoản. Vui lòng thử lại!");
                    request.getRequestDispatcher("Views/common/register.jsp").forward(request, response);
                }
            }
        } catch (ServletException | IOException e) {
            System.out.println("Exception in doPost: " + e.getMessage());  // Log full exception
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("Views/common/register.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Register Servlet";
    }
}