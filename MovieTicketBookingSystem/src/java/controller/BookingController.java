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
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;

@WebServlet(name = "BookingController", urlPatterns = {"/booking"})
public class BookingController extends HttpServlet {

    private ShowtimeDAO showtimeDAO;
    private SeatDAO seatDAO;
    private MovieDAO movieDAO;
    private RoomDAO roomDAO;
    private TicketDAO ticketDAO;
    private ProductDAO productDAO;
    private SeatHoldDAO seatHoldDAO;
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        showtimeDAO = new ShowtimeDAO();
        seatDAO = new SeatDAO();
        movieDAO = new MovieDAO();
        roomDAO = new RoomDAO();
        ticketDAO = new TicketDAO();
        productDAO = new ProductDAO();
        seatHoldDAO = new SeatHoldDAO();
        orderDAO = new OrderDAO();
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
            } else if (action.equals("processPayment")) {
                processPayment(request, response);
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

            request.getRequestDispatcher("Views/common/showtime-selection.jsp").forward(request, response);
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
            String maSuatChieuParam = request.getParameter("maSuatChieu");
            if (maSuatChieuParam == null || maSuatChieuParam.trim().isEmpty()) {
                response.sendRedirect("home");
                return;
            }
            int maSuatChieu = Integer.parseInt(maSuatChieuParam);

            // Yêu cầu đăng nhập
            HttpSession session = request.getSession();
            Customer logged = (Customer) session.getAttribute("loggedInUser");
            if (logged == null) {
                response.sendRedirect("login");
                return;
            }

            // Lấy thông tin suất chiếu
            Showtime showtime = showtimeDAO.getShowtimeById(maSuatChieu);
            if (showtime == null) {
                response.sendRedirect("home");
                return;
            }

            // Lấy thông tin phòng
            Room room = roomDAO.getRoomById(showtime.getMaPhong());
            if (room == null) {
                response.sendRedirect("home");
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
            // Thêm ghế đã thanh toán
            List<Integer> paidSeatIds = ticketDAO.getPaidSeatIdsByShowtime(maSuatChieu);
            if (paidSeatIds != null) bookedSeatIds.addAll(paidSeatIds);
            // Thêm ghế đã sử dụng
            List<Integer> usedSeatIds = ticketDAO.getUsedSeatIdsByShowtime(maSuatChieu);
            if (usedSeatIds != null) bookedSeatIds.addAll(usedSeatIds);
            // Thêm ghế đang hold hợp lệ
            List<Integer> heldSeatIds = seatHoldDAO.getHeldSeatIds(maSuatChieu);
            if (heldSeatIds != null) bookedSeatIds.addAll(heldSeatIds);

            // Lấy thông tin phim
            Movie movie = movieDAO.getMovieById(showtime.getMaPhim());

            // Lấy danh sách ticket để hiển thị giá vé
            List<Ticket> allTickets = ticketDAO.getTicketsByShowtime(maSuatChieu);
            
            // Lấy danh sách sản phẩm combo
            List<Product> products = productDAO.getAllProducts();
            
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
            request.setAttribute("products", products);
            request.setAttribute("movie", movie);
            request.setAttribute("maSuatChieu", maSuatChieu);

            request.getRequestDispatcher("Views/common/seatSelection.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("home");
        } catch (Exception e) {
            e.printStackTrace();
            if (!response.isCommitted()) {
                response.sendRedirect("home");
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

            // Yêu cầu đăng nhập
            HttpSession session = request.getSession();
            Customer logged = (Customer) session.getAttribute("loggedInUser");
            if (logged == null) {
                response.sendRedirect("login");
                return;
            }

            System.out.println("[confirmBooking] START maSuatChieu=" + maSuatChieu + ", maKH=" + logged.getMaKH());

            String selectedSeatsStr = request.getParameter("selectedSeats");
            String selectedProductsStr = request.getParameter("selectedProducts");

            if (selectedSeatsStr == null || selectedSeatsStr.trim().isEmpty()) {
                response.sendRedirect("booking?action=selectSeats&maSuatChieu=" + maSuatChieu);
                return;
            }

            // Parse danh sách ghế được chọn
            String[] selectedSeatIds = selectedSeatsStr.split(",");
            List<Seat> selectedSeats = new java.util.ArrayList<>();
            java.math.BigDecimal seatTotalPrice = java.math.BigDecimal.ZERO;

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

                        seatTotalPrice = seatTotalPrice.add(basePrice.multiply(priceMultiplier));
                    }
                } catch (NumberFormatException ignored) {
                }
            }

            // Parse danh sách sản phẩm được chọn
            java.util.List<java.util.Map<String, Object>> selectedProducts = new java.util.ArrayList<>();
            java.math.BigDecimal productTotalPrice = new java.math.BigDecimal("0");
            
            if (selectedProductsStr != null && !selectedProductsStr.trim().isEmpty()) {
                String[] productEntries = selectedProductsStr.split(",");
                
                for (String entry : productEntries) {
                    try {
                        String[] parts = entry.split(":");
                        if (parts.length == 2) {
                            int productId = Integer.parseInt(parts[0].trim());
                            int quantity = Integer.parseInt(parts[1].trim());
                            
                            Product product = productDAO.getProductById(productId);
                            if (product != null) {
                                // Tạo Map để lưu thông tin product với quantity
                                java.util.Map<String, Object> productInfo = new java.util.HashMap<>();
                                productInfo.put("product", product);
                                productInfo.put("quantity", quantity);
                                productInfo.put("subtotal", product.getDonGia() * quantity);
                                
                                selectedProducts.add(productInfo);
                                productTotalPrice = productTotalPrice.add(
                                    new java.math.BigDecimal(product.getDonGia()).multiply(new java.math.BigDecimal(quantity))
                                );
                            }
                        }
                    } catch (NumberFormatException ignored) {
                    }
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

            // Lấy customer từ session
            Integer maKH = logged != null ? logged.getMaKH() : null;
            
            // Create seat holds (15 phút)
            List<Long> holdIds = new java.util.ArrayList<>();
            if (maKH != null) {
                for (Seat seat : selectedSeats) {
                    model.SeatHold seatHold = new model.SeatHold();
                    seatHold.setMaSuatChieu(maSuatChieu);
                    seatHold.setMaGhe(seat.getMaGhe());
                    seatHold.setMaKH(maKH);
                    // Sử dụng UTC để đồng bộ với SYSUTCDATETIME()
                    java.time.LocalDateTime nowUtc = java.time.LocalDateTime.now(java.time.Clock.systemUTC());
                    seatHold.setHeldAt(nowUtc);
                    seatHold.setExpiresAt(nowUtc.plusMinutes(15)); // 15 phút
                    
                    Long holdId = seatHoldDAO.createSeatHold(seatHold);
                    if (holdId != null) {
                        holdIds.add(holdId);
                    }
                }
            }
            System.out.println("[confirmBooking] CREATED SeatHold ids=" + holdIds);
            
            // Tính tổng tiền
            java.math.BigDecimal grandTotal = seatTotalPrice.add(productTotalPrice);
            System.out.println("[confirmBooking] Calculated totals seat=" + seatTotalPrice + ", combo=" + productTotalPrice + ", grand=" + grandTotal);

            // Tạo Order CHỜ THANH TOÁN + Ticket CHỜ + Upsert Combo
            model.Order order = new model.Order();
            order.setMaKH(logged.getMaKH());
            order.setOrderCode(orderDAO.generateOrderCode());
            order.setKenhDat("Web");
            order.setTrangThai("Chờ thanh toán");
            order.setTongTien(grandTotal);
            order.setCreatedAt(java.time.LocalDateTime.now());
            int maOrder = orderDAO.createOrder(order);
            System.out.println("[confirmBooking] CREATED Order maOrder=" + maOrder + ", orderCode=" + order.getOrderCode());
            
            // Tickets chờ
            java.math.BigDecimal basePrice = showtime.getGiaVeCoSo();
            for (Seat seat : selectedSeats) {
                java.math.BigDecimal multiplier = new java.math.BigDecimal("1.0");
                if ("VIP".equalsIgnoreCase(seat.getLoaiGhe())) multiplier = new java.math.BigDecimal("1.2");
                else if ("Couple".equalsIgnoreCase(seat.getLoaiGhe())) multiplier = new java.math.BigDecimal("1.5");
                Ticket t = new Ticket();
                t.setMaSuatChieu(maSuatChieu);
                t.setMaGhe(seat.getMaGhe());
                t.setGiaVe(basePrice.multiply(multiplier));
                t.setTrangThai("Chờ thanh toán");
                ticketDAO.addTicketWithOrder(maOrder, t);
            }
            System.out.println("[confirmBooking] INSERTED Tickets count=" + selectedSeats.size());
            
            if (!selectedProducts.isEmpty()) {
                dal.OrderFoodDetailDAO ofdDAO = new dal.OrderFoodDetailDAO();
                int count = 0;
                for (java.util.Map<String, Object> pi : selectedProducts) {
                    Product p = (Product) pi.get("product");
                    int qty = (Integer) pi.get("quantity");
                    model.OrderFoodDetail od = new model.OrderFoodDetail();
                    od.setMaOrder(maOrder);
                    od.setMaSP(p.getMaSP());
                    od.setSoLuong(qty);
                    od.setDonGia(new java.math.BigDecimal(p.getDonGia()));
                    od.setThanhTien(new java.math.BigDecimal(p.getDonGia()).multiply(new java.math.BigDecimal(qty)));
                    ofdDAO.createOrderFoodDetail(od);
                    count++;
                }
                System.out.println("[confirmBooking] UPSERT OrderFoodDetail count=" + count);
            }
            
            // Lưu dữ liệu cho payment
            session.setAttribute("pay_maOrder", maOrder);
            session.setAttribute("pay_orderCode", order.getOrderCode());
            session.setAttribute("pay_amount", grandTotal);
            session.setAttribute("pay_maSuatChieu", maSuatChieu);
            session.setAttribute("pay_holdIds", holdIds);
            System.out.println("[confirmBooking] SESSION saved for payment. Redirecting VNPAY...");
            
            // Redirect sang VNPAY
            String amountParam = grandTotal.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString();
            response.sendRedirect("payment?action=create&amount=" + amountParam + "&orderInfo=" + java.net.URLEncoder.encode(order.getOrderCode(), "UTF-8"));
            System.out.println("[confirmBooking] END");
            return;

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

    /**
     * Xử lý thanh toán - Insert vào Order, Ticket, OrderFoodDetail
     */
    private void processPayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String maSuatChieuStr = request.getParameter("maSuatChieu");

            // Yêu cầu đăng nhập
            HttpSession session = request.getSession();
            Customer logged = (Customer) session.getAttribute("loggedInUser");
            if (logged == null) {
                response.sendRedirect("login");
                return;
            }

            String selectedSeatsStr = request.getParameter("selectedSeats");
            String selectedProductsStr = request.getParameter("selectedProducts");
            String holdIdsStr = request.getParameter("holdIds");
            
            int maSuatChieu = Integer.parseInt(maSuatChieuStr);
            int maKH = logged.getMaKH();
            
            // 1. Create Order
            model.Order order = new model.Order();
            order.setMaKH(maKH);
            order.setOrderCode(orderDAO.generateOrderCode());
            order.setKenhDat("Web");
            order.setTrangThai("Đã thanh toán");
            order.setCreatedAt(java.time.LocalDateTime.now());
            order.setPaidAt(java.time.LocalDateTime.now());
            
            // Parse seats và tính total
            String[] selectedSeatIds = selectedSeatsStr.split(",");
            List<Seat> selectedSeats = new java.util.ArrayList<>();
            java.math.BigDecimal seatTotalPrice = java.math.BigDecimal.ZERO;
            
            for (String seatId : selectedSeatIds) {
                int maGhe = Integer.parseInt(seatId.trim());
                Seat seat = seatDAO.getSeatById(maGhe);
                if (seat != null) {
                    selectedSeats.add(seat);
                    Showtime showtime = showtimeDAO.getShowtimeById(maSuatChieu);
                    java.math.BigDecimal basePrice = showtime.getGiaVeCoSo();
                    java.math.BigDecimal priceMultiplier = java.math.BigDecimal.ONE;
                    
                    if ("VIP".equalsIgnoreCase(seat.getLoaiGhe())) {
                        priceMultiplier = new java.math.BigDecimal("1.2");
                    } else if ("Couple".equalsIgnoreCase(seat.getLoaiGhe())) {
                        priceMultiplier = new java.math.BigDecimal("1.5");
                    }
                    
                    seatTotalPrice = seatTotalPrice.add(basePrice.multiply(priceMultiplier));
                }
            }
            
            // Parse products và tính total
            java.math.BigDecimal productTotalPrice = new java.math.BigDecimal("0");
            if (selectedProductsStr != null && !selectedProductsStr.trim().isEmpty()) {
                String[] productEntries = selectedProductsStr.split(",");
                for (String entry : productEntries) {
                    String[] parts = entry.split(":");
                    if (parts.length == 2) {
                        int productId = Integer.parseInt(parts[0].trim());
                        int quantity = Integer.parseInt(parts[1].trim());
                        Product product = productDAO.getProductById(productId);
                        if (product != null) {
                            productTotalPrice = productTotalPrice.add(
                                new java.math.BigDecimal(product.getDonGia()).multiply(new java.math.BigDecimal(quantity))
                            );
                        }
                    }
                }
            }
            
            order.setTongTien(seatTotalPrice.add(productTotalPrice));
            
            int maOrder = orderDAO.createOrder(order);
            
            if (maOrder == 0) {
                request.setAttribute("errorMessage", "Lỗi khi tạo đơn hàng");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // 2. Create Tickets
            Showtime showtime = showtimeDAO.getShowtimeById(maSuatChieu);
            java.math.BigDecimal basePrice = showtime.getGiaVeCoSo();
            
            for (Seat seat : selectedSeats) {
                java.math.BigDecimal priceMultiplier = new java.math.BigDecimal("1.0");
                if ("VIP".equalsIgnoreCase(seat.getLoaiGhe())) {
                    priceMultiplier = new java.math.BigDecimal("1.2");
                } else if ("Couple".equalsIgnoreCase(seat.getLoaiGhe())) {
                    priceMultiplier = new java.math.BigDecimal("1.5");
                }
                
                Ticket ticket = new Ticket();
                ticket.setMaSuatChieu(maSuatChieu);
                ticket.setMaGhe(seat.getMaGhe());
                ticket.setGiaVe(basePrice.multiply(priceMultiplier));
                ticket.setTrangThai("Đã thanh toán");
                
                ticketDAO.addTicketWithOrder(maOrder, ticket);
            }
            
            // 3. Create OrderFoodDetail
            if (selectedProductsStr != null && !selectedProductsStr.trim().isEmpty()) {
                dal.OrderFoodDetailDAO orderFoodDetailDAO = new dal.OrderFoodDetailDAO();
                String[] productEntries = selectedProductsStr.split(",");
                
                for (String entry : productEntries) {
                    String[] parts = entry.split(":");
                    if (parts.length == 2 && parts[0].trim().length() > 0 && parts[1].trim().length() > 0) {
                        int productId = Integer.parseInt(parts[0].trim());
                        int quantity = Integer.parseInt(parts[1].trim());
                        Product product = productDAO.getProductById(productId);
                        if (product != null) {
                            model.OrderFoodDetail orderFoodDetail = new model.OrderFoodDetail();
                            orderFoodDetail.setMaOrder(maOrder);
                            orderFoodDetail.setMaSP(productId);
                            orderFoodDetail.setSoLuong(quantity);
                            orderFoodDetail.setDonGia(new java.math.BigDecimal(product.getDonGia()));
                            orderFoodDetail.setThanhTien(new java.math.BigDecimal(product.getDonGia()).multiply(new java.math.BigDecimal(quantity)));
                            
                            orderFoodDetailDAO.createOrderFoodDetail(orderFoodDetail);
                        }
                    }
                }
            }
            
            // 4. Delete seat holds
            if (holdIdsStr != null && !holdIdsStr.trim().isEmpty()) {
                String[] holdIdArray = holdIdsStr.replace("[", "").replace("]", "").split(",");
                for (String holdIdStr : holdIdArray) {
                    String s = holdIdStr.trim();
                    if (s.isEmpty()) continue;
                    Long holdId = Long.parseLong(s);
                    seatHoldDAO.deleteSeatHold(holdId);
                }
            }
            
            // Redirect to success page
            response.sendRedirect("booking?action=bookingSuccess&maOrder=" + maOrder);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi xử lý thanh toán: " + e.getMessage());
            request.getRequestDispatcher("Views/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
