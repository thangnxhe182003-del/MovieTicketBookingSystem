package controller;

import dal.CinemaDAO;
import dal.MovieDAO;
import dal.ShowtimeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Cinema;
import model.Movie;
import model.Showtime;

public class ShowtimeSelectionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String maPhimStr = request.getParameter("maPhim");
        
        if (maPhimStr == null) {
            response.sendRedirect("home");
            return;
        }

        try {
            int maPhim = Integer.parseInt(maPhimStr);
            
            MovieDAO movieDAO = new MovieDAO();
            Movie movie = movieDAO.getMovieById(maPhim);
            
            if (movie == null) {
                response.sendRedirect("home");
                return;
            }

            ShowtimeDAO showtimeDAO = new ShowtimeDAO();
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

            // Nhóm suất chiếu theo ngày và format date/time
            java.util.Map<String, java.util.List<Showtime>> showtimesByDate = new java.util.HashMap<>();
            java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", java.util.Locale.forLanguageTag("vi"));
            java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
            
            for (Showtime showtime : filteredShowtimes) {
                java.time.LocalDate date = showtime.getNgayChieu().toLocalDate();
                String formattedDate = date.format(dateFormatter);
                showtimesByDate.computeIfAbsent(formattedDate, k -> new java.util.ArrayList<>()).add(showtime);
            }

            request.setAttribute("movie", movie);
            request.setAttribute("showtimesByDate", showtimesByDate);
            request.setAttribute("timeFormatter", timeFormatter);
            request.getRequestDispatcher("Views/common/showtime-selection.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String maPhim = request.getParameter("maPhim");
        String maSuatChieu = request.getParameter("maSuatChieu");
        
        if (maPhim != null && maSuatChieu != null) {
            response.sendRedirect("seat-selection?maPhim=" + maPhim + "&maSuatChieu=" + maSuatChieu);
        } else {
            response.sendRedirect("home");
        }
    }
}
