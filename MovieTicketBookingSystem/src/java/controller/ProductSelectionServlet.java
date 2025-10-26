package controller;

import dal.MovieDAO;
import dal.ProductDAO;
import dal.ShowtimeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Movie;
import model.Product;
import model.Showtime;

public class ProductSelectionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String maPhimStr = request.getParameter("maPhim");
        String maSuatChieuStr = request.getParameter("maSuatChieu");
        String selectedSeats = request.getParameter("selectedSeats");
        
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

            ProductDAO productDAO = new ProductDAO();
            List<Product> products = productDAO.getAllProducts();

            request.setAttribute("movie", movie);
            request.setAttribute("showtime", showtime);
            request.setAttribute("selectedSeats", selectedSeats);
            request.setAttribute("products", products);
            request.getRequestDispatcher("Views/common/product-selection.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String maPhim = request.getParameter("maPhim");
        String maSuatChieu = request.getParameter("maSuatChieu");
        String selectedSeats = request.getParameter("selectedSeats");
        String selectedProducts = request.getParameter("selectedProducts");
        
        if (maPhim != null && maSuatChieu != null && selectedSeats != null) {
            // Redirect to booking confirmation page
            response.sendRedirect("booking-confirm?maPhim=" + maPhim + 
                                "&maSuatChieu=" + maSuatChieu + "&selectedSeats=" + selectedSeats + 
                                "&selectedProducts=" + (selectedProducts != null ? selectedProducts : ""));
        } else {
            response.sendRedirect("home");
        }
    }
}
