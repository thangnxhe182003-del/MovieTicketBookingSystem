package controller;

import dal.CustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Customer;

public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect("login");
            return;
        }
        request.getRequestDispatcher("Views/common/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect("login");
            return;
        }

        Customer logged = (Customer) session.getAttribute("loggedInUser");
        String hoTen = request.getParameter("hoten");
        String ngaySinh = request.getParameter("dob");
        String gioiTinh = request.getParameter("gender");
        String soDT = request.getParameter("phone");
        String email = request.getParameter("email");

        Customer updated = new Customer();
        updated.setMaKH(logged.getMaKH());
        updated.setHoTen(hoTen);
        updated.setNgaySinh(ngaySinh);
        updated.setGioiTinh(gioiTinh);
        updated.setSoDienThoai(soDT);
        updated.setEmail(email);

        CustomerDAO dao = new CustomerDAO();
        boolean ok = dao.updateProfile(updated);
        if (ok) {
            // refresh session user minimal fields
            logged.setHoTen(hoTen);
            logged.setNgaySinh(ngaySinh);
            logged.setGioiTinh(gioiTinh);
            logged.setSoDienThoai(soDT);
            logged.setEmail(email);
            session.setAttribute("loggedInUser", logged);
            request.setAttribute("success", "Cập nhật thông tin thành công.");
        } else {
            request.setAttribute("error", "Cập nhật thất bại. Vui lòng thử lại.");
        }
        request.getRequestDispatcher("Views/common/profile.jsp").forward(request, response);
    }
}


