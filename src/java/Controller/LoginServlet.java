
package Controller;

import Model.Customer;
import Model.Staff;
import dal.CustomerDAO;
import dal.StaffDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("Login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        CustomerDAO customerDAO = new CustomerDAO();
        StaffDAO staffDAO = new StaffDAO();
        HttpSession session = request.getSession();

        try {
            // Thử authenticate customer trước
            Customer customer = customerDAO.authenticate(username, password);
            if (customer != null) {
                session.setAttribute("user", customer);
                session.setAttribute("userType", "customer");
                session.setAttribute("fullName", customer.getHoTen());
                session.setAttribute("birthDate", customer.getNgaySinh());
                session.setAttribute("gender", customer.getGioiTinh());
                session.setAttribute("phone", customer.getSoDienThoai());
                session.setAttribute("email", customer.getEmail());
                session.setAttribute("points", customer.getDiemHienCo());
                session.setAttribute("maKH", customer.getMaKH());
                session.setAttribute("avatar", customer.getAvatar()); // Thêm avatar cho Customer
                session.setAttribute("role", null);
                response.sendRedirect("home.jsp");
                return;
            }

            // Nếu không phải customer, thử staff
            Staff staff = staffDAO.authenticate(username, password);
            if (staff != null) {
                session.setAttribute("user", staff);
                session.setAttribute("userType", "staff");
                session.setAttribute("fullName", staff.getHoTen());
                session.setAttribute("phone", staff.getSoDienThoai());
                session.setAttribute("email", staff.getEmail());
                session.setAttribute("maNhanVien", staff.getMaNhanVien());
                session.setAttribute("role", staff.getChucVu());
                session.setAttribute("khuVucQuanLy", staff.getKhuVucQuanLy()); // Thêm KhuVucQuanLy cho Staff
                session.setAttribute("avatar", staff.getAvatar()); // Thêm avatar cho Staff
                response.sendRedirect("home.jsp");
                return;
            }

            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi khi kết nối đến cơ sở dữ liệu: " + e.getMessage());
            System.err.println("Login failed: " + e.getMessage());
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
    }
}
