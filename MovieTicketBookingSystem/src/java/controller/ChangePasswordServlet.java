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

        // Validation phía server
        String errorMessage = null;

        // Kiểm tra các trường không null hoặc rỗng
        if (oldPass == null || oldPass.trim().isEmpty() ||
            newPass == null || newPass.trim().isEmpty() ||
            confirm == null || confirm.trim().isEmpty()) {
            errorMessage = getErrorMessage(request, "Vui lòng nhập đầy đủ các trường mật khẩu.");
        }
        // Kiểm tra độ dài mật khẩu mới
        else if (newPass.length() < 6) {
            errorMessage = getErrorMessage(request, "Mật khẩu mới phải có ít nhất 6 ký tự.");
        }
        // Kiểm tra mật khẩu mới khác mật khẩu hiện tại
        else if (newPass.equals(oldPass)) {
            errorMessage = getErrorMessage(request, "Mật khẩu mới phải khác mật khẩu hiện tại.");
        }
        // Kiểm tra mật khẩu nhập lại khớp với mật khẩu mới
        else if (!newPass.equals(confirm)) {
            errorMessage = getErrorMessage(request, "Mật khẩu nhập lại không khớp.");
        }
        // Kiểm tra mật khẩu mạnh (tùy chọn)
        else if (!isStrongPassword(newPass)) {
            errorMessage = getErrorMessage(request, "Mật khẩu mới phải chứa chữ hoa, chữ thường, số và ký tự đặc biệt.");
        }

        if (errorMessage != null) {
            request.setAttribute("error", errorMessage);
            request.getRequestDispatcher("Views/common/change-password.jsp").forward(request, response);
            return;
        }

        CustomerDAO dao = new CustomerDAO();
        boolean success = dao.changePassword(user.getMaKH(), oldPass, newPass);
        if (success) {
            request.setAttribute("success", getSuccessMessage(request, "Đổi mật khẩu thành công."));
        } else {
            request.setAttribute("error", getErrorMessage(request, "Mật khẩu hiện tại không đúng."));
        }
        request.getRequestDispatcher("Views/common/change-password.jsp").forward(request, response);
    }

    // Hàm kiểm tra mật khẩu mạnh
    private boolean isStrongPassword(String password) {
        // Regex yêu cầu: ít nhất 1 chữ hoa, 1 chữ thường, 1 số, 1 ký tự đặc biệt
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";
        return password.matches(regex);
    }

    // Hàm lấy thông báo lỗi theo ngôn ngữ
    private String getErrorMessage(HttpServletRequest request, String vnMessage) {
        String lang = (String) request.getSession().getAttribute("lang");
        if ("en".equals(lang)) {
            switch (vnMessage) {
                case "Vui lòng nhập đầy đủ các trường mật khẩu.":
                    return "Please fill in all password fields.";
                case "Mật khẩu mới phải có ít nhất 6 ký tự.":
                    return "New password must be at least 6 characters.";
                case "Mật khẩu mới phải khác mật khẩu hiện tại.":
                    return "New password must be different from the current password.";
                case "Mật khẩu nhập lại không khớp.":
                    return "Confirm password does not match.";
                case "Mật khẩu mới phải chứa chữ hoa, chữ thường, số và ký tự đặc biệt.":
                    return "New password must contain uppercase, lowercase, number, and special character.";
                case "Mật khẩu hiện tại không đúng.":
                    return "Current password is incorrect.";
                default:
                    return "An error occurred.";
            }
        }
        return vnMessage;
    }

    // Hàm lấy thông báo thành công theo ngôn ngữ
    private String getSuccessMessage(HttpServletRequest request, String vnMessage) {
        String lang = (String) request.getSession().getAttribute("lang");
        if ("en".equals(lang)) {
            return "Password changed successfully.";
        }
        return vnMessage;
    }
}