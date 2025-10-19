package controller;

import dal.*;
import model.*;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "MovieController", urlPatterns = {"/movie"})
public class MovieController extends HttpServlet {

    private MovieDAO movieDAO;
    private TrailerDAO trailerDAO;
    private ShowtimeDAO showtimeDAO;
    private RatingDAO ratingDAO;

    @Override
    public void init() throws ServletException {
        movieDAO = new MovieDAO();
        trailerDAO = new TrailerDAO();
        showtimeDAO = new ShowtimeDAO();
        ratingDAO = new RatingDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("list")) {
                listMovies(request, response);
            } else if (action.equals("detail")) {
                showMovieDetail(request, response);
            } else if (action.equals("search")) {
                searchMovies(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống");
        }
    }

    /**
     * Hiển thị danh sách phim (dùng cho trang chủ)
     */
    private void listMovies(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy tất cả phim
            List<Movie> allMovies = movieDAO.getAllMovies();
            
            // Lấy phim đang chiếu
            List<Movie> upcomingMovies = movieDAO.getUpcomingMovies();

            // Phân loại phim
            List<Movie> nowShowingMovies = allMovies.stream()
                    .filter(m -> m.getNgayKhoiChieu() != null 
                            && !m.getNgayKhoiChieu().isAfter(java.time.LocalDate.now()))
                    .limit(12)
                    .toList();

            request.setAttribute("nowShowingMovies", nowShowingMovies);
            request.setAttribute("upcomingMovies", upcomingMovies.stream().limit(12).toList());
            request.setAttribute("allMovies", allMovies);

            request.getRequestDispatcher("Views/common/home.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Hiển thị chi tiết phim (tên, poster, mô tả, trailer, suất chiếu)
     */
    private void showMovieDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int maPhim = Integer.parseInt(request.getParameter("maPhim"));
            
            // Lấy thông tin phim
            Movie movie = movieDAO.getMovieById(maPhim);
            if (movie == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Phim không tồn tại");
                return;
            }

            // Lấy trailer của phim
            List<Trailer> trailers = trailerDAO.getTrailersByMovie(maPhim);

            // Lấy suất chiếu của phim (từ ngày hôm nay trở đi)
            List<Showtime> showtimes = showtimeDAO.getShowtimesByMovie(maPhim);

            // Lấy rating trung bình và số lượng đánh giá
            Double avgRating = ratingDAO.getAverageRatingByMovie(maPhim);
            int ratingCount = ratingDAO.getRatingCountByMovie(maPhim);

            request.setAttribute("movie", movie);
            request.setAttribute("trailers", trailers);
            request.setAttribute("showtimes", showtimes);
            request.setAttribute("avgRating", avgRating);
            request.setAttribute("ratingCount", ratingCount);

            request.getRequestDispatcher("Views/common/movieDetail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID phim không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Tìm kiếm phim theo tên hoặc thể loại
     */
    private void searchMovies(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String keyword = request.getParameter("keyword");
            List<Movie> searchResults;

            if (keyword != null && !keyword.trim().isEmpty()) {
                searchResults = movieDAO.searchMovies(keyword.trim());
            } else {
                searchResults = movieDAO.getAllMovies();
            }

            request.setAttribute("searchResults", searchResults);
            request.setAttribute("keyword", keyword);

            request.getRequestDispatcher("Views/common/searchResults.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
