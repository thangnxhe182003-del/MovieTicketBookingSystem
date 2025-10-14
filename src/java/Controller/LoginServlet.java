package Controller;

import dal.CustomerDAO;
import Model.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private CustomerDAO customerDAO;

    @Override
    public void init() throws ServletException {
        customerDAO = new CustomerDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("Login attempt at " + new java.util.Date() + 
                          ": Received username='" + username + "', password='" + password + "'");

        if (customerDAO == null) {
            throw new ServletException("CustomerDAO not initialized");
        }

        Customer customer = customerDAO.authenticate(username, password);

        if (customer != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userType", "customer");
            session.setAttribute("fullName", customer.getHoTen());
            session.setAttribute("customer", customer);
            session.setAttribute("maKH", customer.getMaKH());
            session.setAttribute("diemHienCo", customer.getDiemHienCo());

            System.out.println("Login successful for: " + customer.getHoTen());
            response.sendRedirect("home.jsp?success=true"); // Chuyển hướng với thông báo
        } else {
            System.out.println("Login failed for username: " + username);
            request.setAttribute("username", username);
            request.setAttribute("passwordError", "Tên đăng nhập hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
    }
}