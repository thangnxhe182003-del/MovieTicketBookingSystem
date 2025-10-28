package controller;

import dal.MovieDAO;
import dal.RatingDAO;
import model.Movie;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {

    private MovieDAO movieDAO;
    private RatingDAO ratingDAO;

    @Override
    public void init() throws ServletException {
        movieDAO = new MovieDAO();
        ratingDAO = new RatingDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String query = request.getParameter("q");
        
        if (query == null || query.trim().isEmpty()) {
            response.sendRedirect("home");
            return;
        }

        List<Movie> movies = movieDAO.searchMovies(query.trim());
        Map<Integer, Double> avgRatingsMap = new HashMap<>();
        Map<Integer, String> dateLabelMap = new HashMap<>();
        for (Movie m : movies) {
            Double avg = ratingDAO.getAverageRatingByMovie(m.getMaPhim());
            if (avg != null) avgRatingsMap.put(m.getMaPhim(), avg);
            if (m.getNgayKhoiChieu() != null) {
                String formatted = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        .format(m.getNgayKhoiChieu());
                dateLabelMap.put(m.getMaPhim(), formatted);
            }
        }
        request.setAttribute("avgRatingsMap", avgRatingsMap);
        request.setAttribute("dateLabelMap", dateLabelMap);

        request.setAttribute("movies", movies);
        request.setAttribute("searchQuery", query.trim());
        request.setAttribute("searchResults", true);
        
        request.getRequestDispatcher("Views/common/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
