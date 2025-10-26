package controller;

import dal.CustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Customer;

public class ChangePasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect("login");
            return;
        }
        request.getRequestDispatcher("Views/common/change-password.jsp").forward(request, response);
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
        Customer user = (Customer) session.getAttribute("loggedInUser");
        String oldPass = request.getParameter("oldPassword");
        String newPass = request.getParameter("newPassword");
        String confirm = request.getParameter("confirmPassword");

        if (newPass == null || confirm == null || !newPass.equals(confirm) || newPass.length() < 6) {
            request.setAttribute("error", "Mật khẩu mới không hợp lệ hoặc không khớp.");
            request.getRequestDispatcher("Views/common/change-password.jsp").forward(request, response);
            return;
        }

        CustomerDAO dao = new CustomerDAO();
        boolean ok = dao.changePassword(user.getMaKH(), oldPass, newPass);
        if (ok) {
            request.setAttribute("success", "Đổi mật khẩu thành công.");
        } else {
            request.setAttribute("error", "Mật khẩu hiện tại không đúng.");
        }
        request.getRequestDispatcher("Views/common/change-password.jsp").forward(request, response);
    }
}


