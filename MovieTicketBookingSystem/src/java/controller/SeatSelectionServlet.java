package controller;

import dal.*;
import model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeatSelectionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String maPhimStr = request.getParameter("maPhim");
        String maSuatChieuStr = request.getParameter("maSuatChieu");
        
        if (maPhimStr == null || maSuatChieuStr == null) {
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

            // Lấy thông tin phòng
            RoomDAO roomDAO = new RoomDAO();
            Room room = roomDAO.getRoomById(showtime.getMaPhong());
            
            // Lấy danh sách ghế của phòng
            SeatDAO seatDAO = new SeatDAO();
            List<Seat> seats = seatDAO.getSeatsByRoom(showtime.getMaPhong());

            // Lấy danh sách ticket đã đặt/hold
            TicketDAO ticketDAO = new TicketDAO();
            List<Ticket> bookedTickets = ticketDAO.getBookedTicketsByShowtime(maSuatChieu);
            List<Integer> bookedSeatIds = new ArrayList<>();
            for (Ticket ticket : bookedTickets) {
                bookedSeatIds.add(ticket.getMaGhe());
            }

            // Lấy danh sách sản phẩm đang bán
            ProductDAO productDAO = new ProductDAO();
            List<Product> products = productDAO.getAllProducts();

            // Tính giá vé cho từng ghế dựa trên loại ghế và giá vé cơ sở
            java.math.BigDecimal basePrice = showtime.getGiaVeCoSo();
            java.util.Map<Integer, java.math.BigDecimal> seatPrices = new java.util.HashMap<>();
            
            for (Seat seat : seats) {
                java.math.BigDecimal price = basePrice;
                if ("VIP".equalsIgnoreCase(seat.getLoaiGhe())) {
                    price = basePrice.multiply(new java.math.BigDecimal("1.5")); // VIP = 1.5x giá cơ sở
                } else if ("Couple".equalsIgnoreCase(seat.getLoaiGhe())) {
                    price = basePrice.multiply(new java.math.BigDecimal("2.0")); // Couple = 2x giá cơ sở
                }
                seatPrices.put(seat.getMaGhe(), price);
            }
            
            // Format ngày chiếu và giờ bắt đầu cho JSP
            String formattedDate = showtime.getNgayChieu() != null ? 
                showtime.getNgayChieu().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
            String formattedStartTime = showtime.getGioBatDau() != null ? 
                showtime.getGioBatDau().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) : "";

            request.setAttribute("movie", movie);
            request.setAttribute("showtime", showtime);
            request.setAttribute("formattedDate", formattedDate);
            request.setAttribute("formattedStartTime", formattedStartTime);
            request.setAttribute("room", room);
            request.setAttribute("seats", seats);
            request.setAttribute("bookedSeatIds", bookedSeatIds);
            request.setAttribute("seatPrices", seatPrices);
            request.setAttribute("products", products);
            request.setAttribute("maSuatChieu", maSuatChieu);

            request.getRequestDispatcher("Views/common/seatSelection.jsp").forward(request, response);
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
        
        if (maPhim != null && maSuatChieu != null && selectedSeats != null && !selectedSeats.trim().isEmpty()) {
            // Redirect to booking confirmation with both seats and products
            String redirectUrl = "booking?action=confirmBooking&maPhim=" + maPhim + 
                               "&maSuatChieu=" + maSuatChieu + 
                               "&selectedSeats=" + selectedSeats;
            if (selectedProducts != null && !selectedProducts.trim().isEmpty()) {
                redirectUrl += "&selectedProducts=" + selectedProducts;
            }
            response.sendRedirect(redirectUrl);
        } else {
            request.setAttribute("error", "Vui lòng chọn ít nhất một ghế.");
            doGet(request, response);
        }
    }
}
