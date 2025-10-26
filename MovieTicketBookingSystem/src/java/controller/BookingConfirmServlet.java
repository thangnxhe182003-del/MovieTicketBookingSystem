package controller;

import dal.CinemaDAO;
import dal.MovieDAO;
import dal.ProductDAO;
import dal.ShowtimeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Cinema;
import model.Movie;
import model.Product;
import model.Showtime;

public class BookingConfirmServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String maPhimStr = request.getParameter("maPhim");
        String maSuatChieuStr = request.getParameter("maSuatChieu");
        String selectedSeats = request.getParameter("selectedSeats");
        String selectedProducts = request.getParameter("selectedProducts");
        
        if (maPhimStr == null || maSuatChieuStr == null || selectedSeats == null) {
            response.sendRedirect("home");
            return;
        }

        try {
            int maPhim = Integer.parseInt(maPhimStr);
            int maSuatChieu = Integer.parseInt(maSuatChieuStr);
            
            MovieDAO movieDAO = new MovieDAO();
            Movie movie = movieDAO.getMovieById(maPhim);
            
            ShowtimeDAO showtimeDAO = new ShowtimeDAO();
            Showtime showtime = showtimeDAO.getShowtimeById(maSuatChieu);
            
            if (movie == null || showtime == null) {
                response.sendRedirect("home");
                return;
            }

            // Parse selected products if any
            List<Product> selectedProductList = new ArrayList<>();
            if (selectedProducts != null && !selectedProducts.trim().isEmpty()) {
                ProductDAO productDAO = new ProductDAO();
                String[] productItems = selectedProducts.split(",");
                for (String item : productItems) {
                    if (!item.trim().isEmpty()) {
                        String[] parts = item.split(":");
                        if (parts.length == 2) {
                            try {
                                int productId = Integer.parseInt(parts[0]);
                                int quantity = Integer.parseInt(parts[1]);
                                Product product = productDAO.getProductById(productId);
                                if (product != null && quantity > 0) {
                                    selectedProductList.add(product);
                                }
                            } catch (NumberFormatException e) {
                                // Skip invalid items
                            }
                        }
                    }
                }
            }

            request.setAttribute("movie", movie);
            request.setAttribute("showtime", showtime);
            request.setAttribute("selectedSeats", selectedSeats);
            request.setAttribute("selectedProducts", selectedProductList);
            request.getRequestDispatcher("Views/common/booking-confirm.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle booking confirmation and payment
        // This would typically process the payment and create booking records
        request.setAttribute("success", "Đặt vé thành công! Thông tin vé đã được gửi qua email.");
        response.sendRedirect("home");
    }
}
