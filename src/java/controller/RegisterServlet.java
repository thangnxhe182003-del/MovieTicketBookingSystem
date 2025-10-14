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
import java.io.PrintWriter;
import util.EmailService;
import model.Customer;

/**
 *
 * @author thang
 */
public class RegisterServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegisterServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        request.getRequestDispatcher("register.jsp").forward(request, response);
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
        String captchaInput = request.getParameter("captchaInput");
        String captchaCode = request.getParameter("captchaCode");

        System.out.println("Debug: Captcha Input=" + captchaInput + ", Code=" + captchaCode);  // Log để check

        // Server-side captcha validate
        if (captchaInput == null || captchaCode == null || !captchaInput.equalsIgnoreCase(captchaCode)) {
            System.out.println("Captcha fail: Input=" + captchaInput + ", Expected=" + captchaCode);
            request.setAttribute("error", "Mã bảo vệ không đúng!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Validate ngaySinh format
        String ngaySinh = null;
        if (ngaySinhStr != null && !ngaySinhStr.trim().isEmpty()) {
            try {
                java.sql.Date.valueOf(ngaySinhStr);  // Throw nếu format sai
                ngaySinh = ngaySinhStr;
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Định dạng ngày sinh không hợp lệ (yyyy-MM-dd)!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
        }

        try {
            CustomerDAO dao = new CustomerDAO();

            if (dao.checkAccountExists(tenDangNhap, email, soDienThoai)) {
                request.setAttribute("error", "Tên đăng nhập, email hoặc số điện thoại đã tồn tại!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            } else {
                Customer c = new Customer(
                        null, tenDangNhap, matKhau, hoTen, ngaySinh,
                        gioiTinh, soDienThoai, email, "0", "Inactive", false, null
                );

                boolean success = dao.insertCustomer(c);
                if (success) {
                    int maKH = dao.getLastInsertedMaKH();
                    System.out.println("Debug: Insert success, maKH=" + maKH);  // Log
                    if (maKH > 0) {
                        String otp = dao.generateAndSaveOTP(maKH);
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
                    request.getRequestDispatcher("otppage.jsp").forward(request, response);
                } else {
                    System.out.println("Insert fail");  // Log
                    request.setAttribute("error", "Lỗi khi tạo tài khoản. Vui lòng thử lại!");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                }
            }
        } catch (ServletException | IOException e) {
            System.out.println("Exception in doPost: " + e.getMessage());  // Log full exception
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
