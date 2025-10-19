package controller;

import dal.*;
import model.*;
import java.io.IOException;
import java.time.LocalDate;
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

    @Override
    public void init() throws ServletException {
        showtimeDAO = new ShowtimeDAO();
        seatDAO = new SeatDAO();
        movieDAO = new MovieDAO();
        roomDAO = new RoomDAO();
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
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống");
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
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Phim không tồn tại");
                return;
            }

            // Lấy danh sách suất chiếu của phim
            List<Showtime> showtimes = showtimeDAO.getShowtimesByMovie(maPhim);

            request.setAttribute("movie", movie);
            request.setAttribute("showtimes", showtimes);

            request.getRequestDispatcher("Views/common/bookingTicket.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID phim không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Suất chiếu không tồn tại");
                return;
            }

            // Lấy thông tin phòng
            Room room = roomDAO.getRoomById(showtime.getMaPhong());
            if (room == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Phòng chiếu không tồn tại");
                return;
            }

            // Lấy danh sách ghế của phòng
            List<Seat> seats = seatDAO.getSeatsByRoom(showtime.getMaPhong());

            // Lấy danh sách ghế đã đặt
            List<Integer> bookedSeatIds = seatDAO.getBookedSeatIdsByRoom(showtime.getMaPhong());

            // Lấy thông tin phim
            Movie movie = movieDAO.getMovieById(showtime.getMaPhim());

            request.setAttribute("showtime", showtime);
            request.setAttribute("room", room);
            request.setAttribute("seats", seats);
            request.setAttribute("bookedSeatIds", bookedSeatIds);
            request.setAttribute("movie", movie);
            request.setAttribute("maSuatChieu", maSuatChieu);

            request.getRequestDispatcher("Views/common/seatSelection.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID suất chiếu không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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

            request.setAttribute("showtime", showtime);
            request.setAttribute("movie", movie);
            request.setAttribute("selectedSeats", selectedSeats);
            request.setAttribute("totalPrice", totalPrice);
            request.setAttribute("maSuatChieu", maSuatChieu);

            request.getRequestDispatcher("Views/common/bookingConfirm.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu không hợp lệ");
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
