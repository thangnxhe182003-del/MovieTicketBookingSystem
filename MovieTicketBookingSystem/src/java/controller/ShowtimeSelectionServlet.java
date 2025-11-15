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
            CinemaDAO cinemaDAO = new CinemaDAO();
            
            // Lấy tham số filter
            String cinemaFilter = request.getParameter("cinema");
            String dateFilter = request.getParameter("date");
            
            // Lấy tất cả suất chiếu của phim (chỉ lấy suất chiếu tương lai)
            List<Showtime> allShowtimes = showtimeDAO.getShowtimesByMovie(maPhim);
            
            // Lọc suất chiếu: chỉ hiển thị suất chiếu chưa bắt đầu
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            List<Showtime> filteredShowtimes = new java.util.ArrayList<>();
            
            for (Showtime showtime : allShowtimes) {
                if (showtime.getGioBatDau() != null && showtime.getGioBatDau().isAfter(now)) {
                    // Áp dụng filter theo rạp
                    if (cinemaFilter != null && !cinemaFilter.isEmpty()) {
                        if (showtime.getTenRap() == null || !showtime.getTenRap().equalsIgnoreCase(cinemaFilter)) {
                            continue;
                        }
                    }
                    
                    // Áp dụng filter theo ngày
                    if (dateFilter != null && !dateFilter.isEmpty()) {
                        try {
                            java.time.LocalDate filterDate = java.time.LocalDate.parse(dateFilter);
                            if (showtime.getNgayChieu() == null || !showtime.getNgayChieu().toLocalDate().equals(filterDate)) {
                                continue;
                            }
                        } catch (Exception e) {
                            // Nếu parse lỗi, bỏ qua filter này
                        }
                    }
                    
                    filteredShowtimes.add(showtime);
                }
            }

            // Nhóm suất chiếu theo ngày và format date/time
            java.util.Map<String, java.util.List<Showtime>> showtimesByDate = new java.util.LinkedHashMap<>();
            java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", java.util.Locale.forLanguageTag("vi"));
            java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
            
            // Sắp xếp theo ngày tăng dần
            filteredShowtimes.sort((s1, s2) -> {
                if (s1.getNgayChieu() == null || s2.getNgayChieu() == null) return 0;
                int dateCompare = s1.getNgayChieu().toLocalDate().compareTo(s2.getNgayChieu().toLocalDate());
                if (dateCompare != 0) return dateCompare;
                if (s1.getGioBatDau() == null || s2.getGioBatDau() == null) return 0;
                return s1.getGioBatDau().compareTo(s2.getGioBatDau());
            });
            
            for (Showtime showtime : filteredShowtimes) {
                java.time.LocalDate date = showtime.getNgayChieu().toLocalDate();
                String formattedDate = date.format(dateFormatter);
                showtimesByDate.computeIfAbsent(formattedDate, k -> new java.util.ArrayList<>()).add(showtime);
            }
            
            // Lấy danh sách rạp để filter
            List<Cinema> allCinemas = cinemaDAO.getAllCinemas();
            java.util.Set<String> cinemaNames = new java.util.LinkedHashSet<>();
            for (Showtime st : filteredShowtimes) {
                if (st.getTenRap() != null) {
                    cinemaNames.add(st.getTenRap());
                }
            }
            
            // Lấy danh sách ngày có suất chiếu
            java.util.Set<java.time.LocalDate> availableDates = new java.util.LinkedHashSet<>();
            for (Showtime st : filteredShowtimes) {
                if (st.getNgayChieu() != null) {
                    availableDates.add(st.getNgayChieu().toLocalDate());
                }
            }

            request.setAttribute("movie", movie);
            request.setAttribute("showtimesByDate", showtimesByDate);
            request.setAttribute("timeFormatter", timeFormatter);
            request.setAttribute("cinemaNames", cinemaNames);
            request.setAttribute("availableDates", availableDates);
            request.setAttribute("selectedCinema", cinemaFilter);
            request.setAttribute("selectedDate", dateFilter);
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
