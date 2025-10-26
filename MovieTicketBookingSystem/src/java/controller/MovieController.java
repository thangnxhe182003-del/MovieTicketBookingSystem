package controller;

import dal.*;
import model.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;
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
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        movieDAO = new MovieDAO();
        trailerDAO = new TrailerDAO();
        showtimeDAO = new ShowtimeDAO();
        ratingDAO = new RatingDAO();
        productDAO = new ProductDAO();
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
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống");
            }
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
                            && !m.getNgayKhoiChieu().isAfter(java.time.LocalDateTime.now()))
                    .limit(12)
                    .toList();

            request.setAttribute("nowShowingMovies", nowShowingMovies);
            request.setAttribute("upcomingMovies", upcomingMovies.stream().limit(12).toList());
            request.setAttribute("allMovies", allMovies);

            request.getRequestDispatcher("Views/common/home.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
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
                if (!response.isCommitted()) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Phim không tồn tại");
                }
                return;
            }

            // Lấy trailer của phim
            List<Trailer> trailers = trailerDAO.getTrailersByMovie(maPhim);

            // Lấy suất chiếu của phim (từ ngày hôm nay trở đi)
            List<Showtime> allShowtimes = showtimeDAO.getShowtimesByMovie(maPhim);
            
            // Lọc suất chiếu: ngày hôm nay chỉ hiển thị suất chiếu > thời gian hiện tại
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            List<Showtime> filteredShowtimes = new java.util.ArrayList<>();
            
            for (Showtime showtime : allShowtimes) {
                if (showtime.getNgayChieu() != null && showtime.getGioBatDau() != null) {
                    // Nếu là ngày hôm nay, chỉ hiển thị suất chiếu chưa bắt đầu
                    if (showtime.getNgayChieu().toLocalDate().equals(now.toLocalDate())) {
                        if (showtime.getGioBatDau().isAfter(now)) {
                            filteredShowtimes.add(showtime);
                        }
                    } else {
                        // Các ngày khác hiển thị bình thường
                        filteredShowtimes.add(showtime);
                    }
                }
            }

            // Nhóm suất chiếu theo ngày
            java.util.Map<java.time.LocalDate, java.util.List<Showtime>> showtimesByDate = new java.util.HashMap<>();
            for (Showtime showtime : filteredShowtimes) {
                java.time.LocalDate date = showtime.getNgayChieu().toLocalDate();
                showtimesByDate.computeIfAbsent(date, k -> new java.util.ArrayList<>()).add(showtime);
            }

            // Lấy rating trung bình và số lượng đánh giá
            Double avgRating = ratingDAO.getAverageRatingByMovie(maPhim);
            int ratingCount = ratingDAO.getRatingCountByMovie(maPhim);

            // Lấy danh sách sản phẩm (combo đồ ăn)
            List<Product> products = productDAO.getAllProducts();

            // Format dates and times for display
            Map<Integer, String> dateLabelMap = new HashMap<>();
            Map<Integer, String> timeLabelMap = new HashMap<>();
            for (Showtime s : filteredShowtimes) {
                if (s.getNgayChieu() != null) {
                    dateLabelMap.put(s.getMaSuatChieu(), s.getNgayChieu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
                if (s.getGioBatDau() != null) {
                    timeLabelMap.put(s.getMaSuatChieu(), s.getGioBatDau().format(DateTimeFormatter.ofPattern("HH:mm")));
                }
            }

            request.setAttribute("movie", movie);
            request.setAttribute("trailers", trailers);
            request.setAttribute("showtimes", filteredShowtimes);
            request.setAttribute("dateLabelMap", dateLabelMap);
            request.setAttribute("timeLabelMap", timeLabelMap);
            request.setAttribute("avgRating", avgRating);
            request.setAttribute("ratingCount", ratingCount);
            request.setAttribute("products", products);

            request.getRequestDispatcher("Views/common/movieDetail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID phim không hợp lệ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
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
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
