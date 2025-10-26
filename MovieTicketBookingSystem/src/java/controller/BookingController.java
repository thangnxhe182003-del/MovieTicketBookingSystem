package controller;

import dal.*;
import model.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "BookingController", urlPatterns = {"/booking"})
public class BookingController extends HttpServlet {

    private ShowtimeDAO showtimeDAO;
    private SeatDAO seatDAO;
    private MovieDAO movieDAO;
    private RoomDAO roomDAO;
    private TicketDAO ticketDAO;

    @Override
    public void init() throws ServletException {
        showtimeDAO = new ShowtimeDAO();
        seatDAO = new SeatDAO();
        movieDAO = new MovieDAO();
        roomDAO = new RoomDAO();
        ticketDAO = new TicketDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("selectShowtime")) {
                selectShowtime(request, response);
            } else if (action.equals("selectSeats")) {
                selectSeats(request, response);
            } else if (action.equals("confirmBooking")) {
                confirmBooking(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống");
            }
        }
    }

    /**
     * Chọn suất chiếu - hiển thị danh sách suất chiếu của phim
     */
    private void selectShowtime(HttpServletRequest request, HttpServletResponse response)
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

            // Lấy danh sách suất chiếu của phim
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

            request.setAttribute("movie", movie);
            request.setAttribute("showtimesByDate", showtimesByDate);

            request.getRequestDispatcher("Views/common/bookingTicket.jsp").forward(request, response);
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
     * Chọn ghế - hiển thị layout ghế của suất chiếu
     */
    private void selectSeats(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int maSuatChieu = Integer.parseInt(request.getParameter("maSuatChieu"));

            // Lấy thông tin suất chiếu
            Showtime showtime = showtimeDAO.getShowtimeById(maSuatChieu);
            if (showtime == null) {
                if (!response.isCommitted()) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Suất chiếu không tồn tại");
                }
                return;
            }

            // Lấy thông tin phòng
            Room room = roomDAO.getRoomById(showtime.getMaPhong());
            if (room == null) {
                if (!response.isCommitted()) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Phòng chiếu không tồn tại");
                }
                return;
            }

            // Lấy danh sách ghế của phòng
            List<Seat> seats = seatDAO.getSeatsByRoom(showtime.getMaPhong());

            // Lấy danh sách ticket đã đặt/hold
            List<Ticket> bookedTickets = ticketDAO.getBookedTicketsByShowtime(maSuatChieu);
            List<Integer> bookedSeatIds = new ArrayList<>();
            for (Ticket ticket : bookedTickets) {
                bookedSeatIds.add(ticket.getMaGhe());
            }

            // Lấy thông tin phim
            Movie movie = movieDAO.getMovieById(showtime.getMaPhim());

            // Lấy danh sách ticket để hiển thị giá vé
            List<Ticket> allTickets = ticketDAO.getTicketsByShowtime(maSuatChieu);
            
            // Format ngày chiếu và giờ bắt đầu cho JSP
            String formattedDate = showtime.getNgayChieu() != null ? 
                showtime.getNgayChieu().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
            String formattedStartTime = showtime.getGioBatDau() != null ? 
                showtime.getGioBatDau().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) : "";
            
            request.setAttribute("showtime", showtime);
            request.setAttribute("formattedDate", formattedDate);
            request.setAttribute("formattedStartTime", formattedStartTime);
            request.setAttribute("room", room);
            request.setAttribute("seats", seats);
            request.setAttribute("bookedSeatIds", bookedSeatIds);
            request.setAttribute("tickets", allTickets);
            request.setAttribute("movie", movie);
            request.setAttribute("maSuatChieu", maSuatChieu);

            request.getRequestDispatcher("Views/common/seatSelection.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID suất chiếu không hợp lệ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Xác nhận đặt vé (chưa lưu vào database, chỉ để hiển thị và nhận dữ liệu)
     */
    private void confirmBooking(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int maSuatChieu = Integer.parseInt(request.getParameter("maSuatChieu"));
            String selectedSeatsStr = request.getParameter("selectedSeats");

            if (selectedSeatsStr == null || selectedSeatsStr.trim().isEmpty()) {
                response.sendRedirect("booking?action=selectSeats&maSuatChieu=" + maSuatChieu);
                return;
            }

            // Parse danh sách ghế được chọn
            String[] selectedSeatIds = selectedSeatsStr.split(",");
            List<Seat> selectedSeats = new java.util.ArrayList<>();
            java.math.BigDecimal totalPrice = java.math.BigDecimal.ZERO;

            for (String seatId : selectedSeatIds) {
                try {
                    int maGhe = Integer.parseInt(seatId.trim());
                    Seat seat = seatDAO.getSeatById(maGhe);
                    if (seat != null) {
                        selectedSeats.add(seat);
                        
                        // Tính giá vé theo loại ghế
                        Showtime showtime = showtimeDAO.getShowtimeById(maSuatChieu);
                        java.math.BigDecimal basePrice = showtime.getGiaVeCoSo();
                        java.math.BigDecimal priceMultiplier = java.math.BigDecimal.ONE;

                        if ("VIP".equalsIgnoreCase(seat.getLoaiGhe())) {
                            priceMultiplier = new java.math.BigDecimal("1.2"); // +20%
                        } else if ("Couple".equalsIgnoreCase(seat.getLoaiGhe())) {
                            priceMultiplier = new java.math.BigDecimal("1.5"); // +50%
                        }

                        totalPrice = totalPrice.add(basePrice.multiply(priceMultiplier));
                    }
                } catch (NumberFormatException ignored) {
                }
            }

            Showtime showtime = showtimeDAO.getShowtimeById(maSuatChieu);
            Movie movie = movieDAO.getMovieById(showtime.getMaPhim());
            
            // Get room information
            Room room = roomDAO.getRoomById(showtime.getMaPhong());

            // Format dates for JSP
            String formattedDate = showtime.getNgayChieu() != null ? 
                showtime.getNgayChieu().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
            String formattedStartTime = showtime.getGioBatDau() != null ? 
                showtime.getGioBatDau().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) : "";
            String formattedEndTime = showtime.getGioKetThuc() != null ? 
                showtime.getGioKetThuc().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) : "";

            request.setAttribute("showtime", showtime);
            request.setAttribute("room", room);
            request.setAttribute("formattedDate", formattedDate);
            request.setAttribute("formattedStartTime", formattedStartTime);
            request.setAttribute("formattedEndTime", formattedEndTime);
            request.setAttribute("movie", movie);
            request.setAttribute("selectedSeats", selectedSeats);
            request.setAttribute("totalPrice", totalPrice);
            request.setAttribute("maSuatChieu", maSuatChieu);

            request.getRequestDispatcher("Views/common/bookingConfirm.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu không hợp lệ");
            }
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
