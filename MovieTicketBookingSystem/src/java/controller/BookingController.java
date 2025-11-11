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
    private DiscountDAO discountDAO;
    private CustomerDAO customerDAO;

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
        discountDAO = new DiscountDAO();
        customerDAO = new CustomerDAO();
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
            } else if (action.equals("applyDiscount")) {
                applyDiscount(request, response);
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
            
            // Lấy danh sách mã giảm giá phù hợp
            List<Discount> availableDiscounts = discountDAO.getAvailableDiscounts();
            
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
            request.setAttribute("availableDiscounts", availableDiscounts);

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
            HttpSession session = request.getSession();
            Customer logged = (Customer) session.getAttribute("loggedInUser");
            if (logged == null) { response.sendRedirect("login"); return; }

            String selectedSeatsStr = request.getParameter("selectedSeats");
            String seatTypesStr = request.getParameter("seatTypes"); // seatId:adult|child,comma-separated
            String selectedProductsStr = request.getParameter("selectedProducts");
            String maGiamGiaStr = request.getParameter("maGiamGia");
            String paymentMethod = request.getParameter("paymentMethod"); // VNPAY or CASH

            if (selectedSeatsStr == null || selectedSeatsStr.trim().isEmpty()) {
                response.sendRedirect("booking?action=selectSeats&maSuatChieu=" + maSuatChieu);
                return;
            }

            // Parse seat age types map
            java.util.Map<Integer, String> seatIdToAge = new java.util.HashMap<>();
            if (seatTypesStr != null && !seatTypesStr.trim().isEmpty()) {
                for (String pair : seatTypesStr.split(",")) {
                    String[] p = pair.split(":");
                    if (p.length == 2) {
                        try {
                            seatIdToAge.put(Integer.parseInt(p[0].trim()), p[1].trim());
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }

            Showtime showtime = showtimeDAO.getShowtimeById(maSuatChieu);
            Movie movie = movieDAO.getMovieById(showtime.getMaPhim());
            java.math.BigDecimal giaVeCoSo = showtime.getGiaVeCoSo() != null ? showtime.getGiaVeCoSo() : java.math.BigDecimal.ZERO;
            java.math.BigDecimal giaVeTreEm = showtime.getGiaVeTreEm() != null ? showtime.getGiaVeTreEm() : giaVeCoSo;
            java.math.BigDecimal vatPercent = showtime.getVat() != null ? showtime.getVat() : java.math.BigDecimal.ZERO; // e.g., 10.00

            // Parse selected seats and compute adult/child totals
            String[] selectedSeatIds = selectedSeatsStr.split(",");
            List<Seat> selectedSeats = new java.util.ArrayList<>();
            List<java.math.BigDecimal> originalTicketPrices = new java.util.ArrayList<>();
            List<Boolean> isAdultList = new java.util.ArrayList<>();
            int soLuongVeNguoiLon = 0;

            for (String seatId : selectedSeatIds) {
                try {
                    int maGhe = Integer.parseInt(seatId.trim());
                    Seat seat = seatDAO.getSeatById(maGhe);
                    if (seat != null) {
                        selectedSeats.add(seat);
                        String ageType = seatIdToAge.getOrDefault(maGhe, "adult");
                        boolean isAdult = !"child".equalsIgnoreCase(ageType);
                        isAdultList.add(isAdult);
                        if (isAdult) soLuongVeNguoiLon++;
                        java.math.BigDecimal multiplier = java.math.BigDecimal.ONE;
                        if ("VIP".equalsIgnoreCase(seat.getLoaiGhe())) multiplier = new java.math.BigDecimal("1.2");
                        else if ("Couple".equalsIgnoreCase(seat.getLoaiGhe())) multiplier = new java.math.BigDecimal("1.5");
                        java.math.BigDecimal base = isAdult ? giaVeCoSo : giaVeTreEm;
                        originalTicketPrices.add(base.multiply(multiplier));
                    }
                } catch (NumberFormatException ignored) {}
            }

            // Parse selected products
            java.util.List<java.util.Map<String, Object>> selectedProducts = new java.util.ArrayList<>();
            java.math.BigDecimal productTotalPrice = java.math.BigDecimal.ZERO;
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
                                java.util.Map<String, Object> productInfo = new java.util.HashMap<>();
                                productInfo.put("product", product);
                                productInfo.put("quantity", quantity);
                                productInfo.put("subtotal", product.getDonGia() * quantity);
                                selectedProducts.add(productInfo);
                                productTotalPrice = productTotalPrice.add(new java.math.BigDecimal(product.getDonGia()).multiply(new java.math.BigDecimal(quantity)));
                            }
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }

            // Ticket totals per adult/child
            java.math.BigDecimal ticketTotalOriginal = java.math.BigDecimal.ZERO;
            for (java.math.BigDecimal p : originalTicketPrices) ticketTotalOriginal = ticketTotalOriginal.add(p);

            // Apply discount only to adult tickets
            java.math.BigDecimal discountAmount = java.math.BigDecimal.ZERO;
            Integer maGiamGia = null;
            if (maGiamGiaStr != null && !maGiamGiaStr.trim().isEmpty()) {
                try {
                    maGiamGia = Integer.parseInt(maGiamGiaStr.trim());
                    Discount discount = discountDAO.getDiscountById(maGiamGia);
                    if (discount != null && "Hoạt Động".equals(discount.getTrangThai())) {
                        java.time.LocalDateTime now = java.time.LocalDateTime.now();
                        boolean valid = discount.getNgayBatDau() != null && discount.getNgayBatDau().isBefore(now)
                                && (discount.getNgayKetThuc() == null || discount.getNgayKetThuc().isAfter(now))
                                && (discount.getSoLanSuDung() == null || discount.getDaSuDung() < discount.getSoLanSuDung());
                        if (valid) {
                            String loai = discount.getLoaiGiamGia();
                            String hinhThuc = discount.getHinhThucGiam();
                            java.math.BigDecimal val = discount.getGiaTriGiam();
                            java.math.BigDecimal max = discount.getGiaTriToiDa();
                            boolean isPct = "Phần trăm".equals(hinhThuc) || "PhanTram".equals(hinhThuc);
                            boolean isMoney = "Tiền mặt".equals(hinhThuc) || "TienMat".equals(hinhThuc);

                            if ("Vé".equals(loai)) {
                                // sum adult ticket prices
                                java.math.BigDecimal adultTicketsSum = java.math.BigDecimal.ZERO;
                                for (int i = 0; i < originalTicketPrices.size(); i++) {
                                    if (isAdultList.get(i)) adultTicketsSum = adultTicketsSum.add(originalTicketPrices.get(i));
                                }
                                if (isPct) {
                                    discountAmount = adultTicketsSum.multiply(val).divide(new java.math.BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
                                } else if (isMoney) {
                                    discountAmount = val.multiply(new java.math.BigDecimal(soLuongVeNguoiLon));
                                    if (discountAmount.compareTo(adultTicketsSum) > 0) discountAmount = adultTicketsSum;
                                }
                                if (max != null && discountAmount.compareTo(max) > 0) discountAmount = max;
                            } else if ("Đồ ăn".equals(loai)) {
                                if (isPct) discountAmount = productTotalPrice.multiply(val).divide(new java.math.BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
                                else if (isMoney) discountAmount = val.min(productTotalPrice);
                            } else if ("Toàn đơn".equals(loai)) {
                                java.math.BigDecimal grand = ticketTotalOriginal.add(productTotalPrice);
                                if (isPct) discountAmount = grand.multiply(val).divide(new java.math.BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
                                else if (isMoney) discountAmount = val.min(grand);
                            }
                        }
                    }
                } catch (NumberFormatException ignored) {}
            }

            // Totals after discount
            java.math.BigDecimal grandAfterDiscount = ticketTotalOriginal.add(productTotalPrice).subtract(discountAmount);
            if (grandAfterDiscount.compareTo(java.math.BigDecimal.ZERO) < 0) grandAfterDiscount = java.math.BigDecimal.ZERO;
            // VAT after discount
            java.math.BigDecimal vatAmount = grandAfterDiscount.multiply(vatPercent).divide(new java.math.BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
            java.math.BigDecimal payableTotal = grandAfterDiscount.add(vatAmount);

            // Tạo danh sách giá vé cuối cùng (đã xử lý theo loại mã giảm áp trên vé)
            java.util.List<java.math.BigDecimal> finalTicketPrices = new java.util.ArrayList<>();
            if (discountAmount.compareTo(java.math.BigDecimal.ZERO) > 0 && maGiamGia != null) {
                Discount discount = discountDAO.getDiscountById(maGiamGia);
                if (discount != null && "Vé".equals(discount.getLoaiGiamGia())) {
                    String h = discount.getHinhThucGiam();
                    boolean pct = "Phần trăm".equals(h) || "PhanTram".equals(h);
                    boolean money = "Tiền mặt".equals(h) || "TienMat".equals(h);
                    if (pct) {
                        java.math.BigDecimal percent = discount.getGiaTriGiam();
                        for (int i = 0; i < originalTicketPrices.size(); i++) {
                            java.math.BigDecimal orig = originalTicketPrices.get(i);
                            if (isAdultList.get(i)) {
                                java.math.BigDecimal dec = orig.multiply(percent).divide(new java.math.BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
                                java.math.BigDecimal fin = orig.subtract(dec);
                                if (fin.compareTo(java.math.BigDecimal.ZERO) < 0) fin = java.math.BigDecimal.ZERO;
                                finalTicketPrices.add(fin);
                            } else {
                                finalTicketPrices.add(orig);
                            }
                        }
                    } else if (money) {
                        // subtract fixed amount per adult ticket
                        java.math.BigDecimal per = discount.getGiaTriGiam();
                        for (int i = 0; i < originalTicketPrices.size(); i++) {
                            java.math.BigDecimal orig = originalTicketPrices.get(i);
                            if (isAdultList.get(i)) {
                                java.math.BigDecimal fin = orig.subtract(per);
                                if (fin.compareTo(java.math.BigDecimal.ZERO) < 0) fin = java.math.BigDecimal.ZERO;
                                finalTicketPrices.add(fin);
                            } else {
                                finalTicketPrices.add(orig);
                            }
                        }
                    } else {
                        finalTicketPrices.addAll(originalTicketPrices);
                    }
                } else {
                    finalTicketPrices.addAll(originalTicketPrices);
                }
            } else {
                finalTicketPrices.addAll(originalTicketPrices);
            }

            // Tạo Order và chi tiết theo đúng thứ tự như VNPay (Order -> Ticket -> OrderFoodDetail)
            model.Order order = new model.Order();
            order.setMaKH(logged.getMaKH());
            order.setOrderCode(orderDAO.generateOrderCode());
            order.setKenhDat("Web");
            boolean isCash = "CASH".equalsIgnoreCase(paymentMethod);
            String asciiCashStatus = "Thanh toan tien mat";
            order.setTrangThai(isCash ? asciiCashStatus : "Chờ thanh toán");
            order.setTongTien(payableTotal);
            order.setCreatedAt(java.time.LocalDateTime.now());
            if (isCash) order.setPaidAt(java.time.LocalDateTime.now());
            order.setMaGiamGia(maGiamGia);

            int maOrder = insertOrderAndDetails(order, selectedSeats, finalTicketPrices, selectedProducts, maSuatChieu, isCash ? "Đã thanh toán" : "Chờ thanh toán");
 
            // Save session for payment (if VNPAY)
            session.setAttribute("pay_maOrder", maOrder);
            session.setAttribute("pay_orderCode", order.getOrderCode());
            session.setAttribute("pay_amount", payableTotal);
            session.setAttribute("pay_maSuatChieu", maSuatChieu);
            // Hold seats saved earlier in flow; keep as-is

            if ("CASH".equalsIgnoreCase(paymentMethod)) {
                // Bỏ qua VNPAY: tăng số lần sử dụng mã giảm (nếu có) và gửi email tóm tắt, KHÔNG xóa hold ở đây
                try {
                    if (maGiamGia != null) {
                        discountDAO.incrementDiscountUsage(maGiamGia);
                    }
                } catch (Exception ignored) {}
                try {
                    Customer customer = customerDAO.getCustomerById(logged.getMaKH());
                    if (customer != null && customer.getEmail() != null && !customer.getEmail().trim().isEmpty()) {
                        util.EmailService.sendOrderSummaryEmail(customer.getEmail(), customer.getHoTen(), maOrder);
                    }
                } catch (Exception e) {
                    System.err.println("[BookingController] Error sending order summary email (cash): " + e.getMessage());
                }
                // Chuyển trang thành công
                request.getRequestDispatcher("Views/common/payment-success.jsp").forward(request, response);
                return;
            }

            // VNPAY
            String amountParam = payableTotal.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString();
            response.sendRedirect("payment?action=create&amount=" + amountParam + "&orderInfo=" + java.net.URLEncoder.encode(order.getOrderCode(), "UTF-8"));
            return;

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
            
            if (maOrder <= 0) {
                request.setAttribute("errorMessage", "Không tạo được đơn hàng (MaOrder rỗng). Vui lòng thử lại.");
                request.getRequestDispatcher("Views/error.jsp").forward(request, response);
                return;
            }
            // Xác thực Order đã tồn tại trong DB
            try {
                model.Order checkOrder = orderDAO.getOrderById(maOrder);
                if (checkOrder == null) {
                    request.setAttribute("errorMessage", "Đơn hàng chưa sẵn sàng trong hệ thống. Vui lòng thử lại sau vài giây.");
                    request.getRequestDispatcher("Views/error.jsp").forward(request, response);
                    return;
                }
            } catch (Exception ex) {
                // Không thể xác thực, tránh chèn chi tiết để không lỗi FK
                request.setAttribute("errorMessage", "Không xác thực được đơn hàng vừa tạo. Vui lòng thử lại.");
                request.getRequestDispatcher("Views/error.jsp").forward(request, response);
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
    
    /**
     * Xử lý áp dụng mã giảm giá (AJAX)
     */
    private void applyDiscount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        java.io.PrintWriter out = response.getWriter();
        
        try {
            String maGiamGiaStr = request.getParameter("maGiamGia");
            String selectedSeatsStr = request.getParameter("selectedSeats");
            String selectedProductsStr = request.getParameter("selectedProducts");
            String seatTypesStr = request.getParameter("seatTypes"); // seatId:adult|child
            
            if (maGiamGiaStr == null || maGiamGiaStr.trim().isEmpty()) {
                out.print("{\"success\":false,\"message\":\"Mã giảm giá không hợp lệ\"}");
                return;
            }
            
            int maGiamGia = Integer.parseInt(maGiamGiaStr.trim());
            Discount discount = discountDAO.getDiscountById(maGiamGia);
            
            if (discount == null || !"Hoạt Động".equals(discount.getTrangThai())) {
                out.print("{\"success\":false,\"message\":\"Mã giảm giá không tồn tại hoặc không hoạt động\"}");
                return;
            }
            
            // Kiểm tra thời hạn và số lần sử dụng
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            boolean isValid = discount.getNgayBatDau() != null && 
                             discount.getNgayBatDau().isBefore(now) &&
                             (discount.getNgayKetThuc() == null || discount.getNgayKetThuc().isAfter(now)) &&
                             (discount.getSoLanSuDung() == null || discount.getDaSuDung() < discount.getSoLanSuDung());
            
            if (!isValid) {
                out.print("{\"success\":false,\"message\":\"Mã giảm giá đã hết hạn hoặc đã hết số lần sử dụng\"}");
                return;
            }
            
            String maSuatChieuStr = request.getParameter("maSuatChieu");
            if (maSuatChieuStr == null || maSuatChieuStr.trim().isEmpty()) {
                out.print("{\"success\":false,\"message\":\"Thiếu thông tin suất chiếu\"}");
                return;
            }
            
            int maSuatChieu = Integer.parseInt(maSuatChieuStr.trim());
            Showtime showtime = showtimeDAO.getShowtimeById(maSuatChieu);
            if (showtime == null || showtime.getGiaVeCoSo() == null) {
                out.print("{\"success\":false,\"message\":\"Không tìm thấy suất chiếu hoặc giá vé cơ sở\"}");
                return;
            }
            java.math.BigDecimal giaVeCoSo = showtime.getGiaVeCoSo();
            java.math.BigDecimal giaVeTreEm = showtime.getGiaVeTreEm() != null ? showtime.getGiaVeTreEm() : giaVeCoSo;
            java.math.BigDecimal vatPercent = showtime.getVat() != null ? showtime.getVat() : java.math.BigDecimal.ZERO;
            
            // Parse seatTypes map
            java.util.Map<Integer, String> seatIdToAge = new java.util.HashMap<>();
            if (seatTypesStr != null && !seatTypesStr.trim().isEmpty()) {
                for (String pair : seatTypesStr.split(",")) {
                    String[] p = pair.split(":");
                    if (p.length == 2) {
                        try { seatIdToAge.put(Integer.parseInt(p[0].trim()), p[1].trim()); } catch (NumberFormatException ignored) {}
                    }
                }
            }
            
            // Tính tổng giá ghế theo adult/child và VIP/Couple
            java.math.BigDecimal seatTotalPrice = java.math.BigDecimal.ZERO;
            java.math.BigDecimal adultSum = java.math.BigDecimal.ZERO; // tổng giá các vé người lớn (để giảm đúng)
            int soLuongVe = 0;
            int soLuongVeNguoiLon = 0;
            if (selectedSeatsStr != null && !selectedSeatsStr.trim().isEmpty()) {
                String[] selectedSeatIds = selectedSeatsStr.split(",");
                for (String seatId : selectedSeatIds) {
                    try {
                        int maGhe = Integer.parseInt(seatId.trim());
                        Seat seat = seatDAO.getSeatById(maGhe);
                        if (seat != null) {
                            java.math.BigDecimal multiplier = java.math.BigDecimal.ONE;
                            if ("VIP".equalsIgnoreCase(seat.getLoaiGhe())) multiplier = new java.math.BigDecimal("1.2");
                            else if ("Couple".equalsIgnoreCase(seat.getLoaiGhe())) multiplier = new java.math.BigDecimal("1.5");
                            String ageType = seatIdToAge.getOrDefault(maGhe, "adult");
                            boolean isAdult = !"child".equalsIgnoreCase(ageType);
                            if (isAdult) soLuongVeNguoiLon++;
                            java.math.BigDecimal base = isAdult ? giaVeCoSo : giaVeTreEm;
                            java.math.BigDecimal val = base.multiply(multiplier);
                            seatTotalPrice = seatTotalPrice.add(val);
                            if (isAdult) adultSum = adultSum.add(giaVeCoSo.multiply(multiplier));
                            soLuongVe++;
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
            
            // Tổng giá combo
            java.math.BigDecimal productTotalPrice = java.math.BigDecimal.ZERO;
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
                                productTotalPrice = productTotalPrice.add(
                                    new java.math.BigDecimal(product.getDonGia()).multiply(new java.math.BigDecimal(quantity))
                                );
                            }
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
            
            java.math.BigDecimal grandTotal = seatTotalPrice.add(productTotalPrice);
            java.math.BigDecimal discountAmount = java.math.BigDecimal.ZERO;
            
            // Áp dụng mã giảm giá chuẩn theo yêu cầu (Vé/Đồ ăn/Toàn đơn)
            java.math.BigDecimal discountValue = discount.getGiaTriGiam();
            java.math.BigDecimal maxDiscount = discount.getGiaTriToiDa();
            String hinhThucGiam = discount.getHinhThucGiam();
            boolean isPhanTram = "Phần trăm".equals(hinhThucGiam) || "PhanTram".equals(hinhThucGiam);
            boolean isTienMat = "Tiền mặt".equals(hinhThucGiam) || "TienMat".equals(hinhThucGiam);
            String loai = discount.getLoaiGiamGia();
            
            if (isPhanTram) {
                if ("Vé".equals(loai)) {
                    // Chỉ giảm trên vé người lớn
                    // Ước lượng tổng giá vé người lớn: phân rã tương tự tính seatTotalPrice ở trên
//                    java.math.BigDecimal adultSum = java.math.BigDecimal.ZERO;
                    if (selectedSeatsStr != null && !selectedSeatsStr.trim().isEmpty()) {
                        for (String seatId : selectedSeatsStr.split(",")) {
                            try {
                                int maGhe = Integer.parseInt(seatId.trim());
                                Seat seat = seatDAO.getSeatById(maGhe);
                                if (seat != null) {
                                    String age = seatIdToAge.getOrDefault(maGhe, "adult");
                                    if (!"child".equalsIgnoreCase(age)) {
                                        java.math.BigDecimal mul = java.math.BigDecimal.ONE;
                                        if ("VIP".equalsIgnoreCase(seat.getLoaiGhe())) mul = new java.math.BigDecimal("1.2");
                                        else if ("Couple".equalsIgnoreCase(seat.getLoaiGhe())) mul = new java.math.BigDecimal("1.5");
                                        adultSum = adultSum.add(giaVeCoSo.multiply(mul));
                                    }
                                }
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                    discountAmount = adultSum.multiply(discountValue).divide(new java.math.BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
                } else if ("Đồ ăn".equals(loai)) {
                    discountAmount = productTotalPrice.multiply(discountValue).divide(new java.math.BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
                } else if ("Toàn đơn".equals(loai)) {
                    discountAmount = grandTotal.multiply(discountValue).divide(new java.math.BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
                }
                if (maxDiscount != null && discountAmount.compareTo(maxDiscount) > 0) discountAmount = maxDiscount;
            } else if (isTienMat) {
                if ("Vé".equals(loai)) {
                    java.math.BigDecimal per = discountValue;
                    java.math.BigDecimal totalAdult = per.multiply(new java.math.BigDecimal(soLuongVeNguoiLon));
                    discountAmount = totalAdult.compareTo(adultSum) < 0 ? totalAdult : adultSum;
                } else if ("Đồ ăn".equals(loai)) {
                    discountAmount = discountValue.compareTo(productTotalPrice) < 0 ? discountValue : productTotalPrice;
                } else if ("Toàn đơn".equals(loai)) {
                    discountAmount = discountValue.compareTo(grandTotal) < 0 ? discountValue : grandTotal;
                }
            }
            
            java.math.BigDecimal finalTotal = grandTotal.subtract(discountAmount);
            if (finalTotal.compareTo(java.math.BigDecimal.ZERO) < 0) finalTotal = java.math.BigDecimal.ZERO;
            java.math.BigDecimal vatAmount = finalTotal.multiply(vatPercent).divide(new java.math.BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
            java.math.BigDecimal finalWithVat = finalTotal.add(vatAmount);
            
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"success\":true,");
            json.append("\"maGiamGia\":" ).append(maGiamGia).append(",");
            json.append("\"maCode\":\"").append(discount.getMaCode() != null ? discount.getMaCode().replace("\"", "\\\"") : "").append("\",");
            json.append("\"tenGiamGia\":\"").append(discount.getTenGiamGia() != null ? discount.getTenGiamGia().replace("\"", "\\\"") : "").append("\",");
            json.append("\"loaiGiamGia\":\"").append(discount.getLoaiGiamGia() != null ? discount.getLoaiGiamGia().replace("\"", "\\\"") : "").append("\",");
            json.append("\"hinhThucGiam\":\"").append(discount.getHinhThucGiam() != null ? discount.getHinhThucGiam().replace("\"", "\\\"") : "").append("\",");
            json.append("\"seatTotal\":").append(seatTotalPrice.toPlainString()).append(",");
            json.append("\"productTotal\":").append(productTotalPrice.toPlainString()).append(",");
            json.append("\"grandTotal\":").append(grandTotal.toPlainString()).append(",");
            json.append("\"discountAmount\":").append(discountAmount.toPlainString()).append(",");
            json.append("\"finalTotal\":").append(finalTotal.toPlainString()).append(",");
            json.append("\"vatPercent\":").append(vatPercent.toPlainString()).append(",");
            json.append("\"vatAmount\":").append(vatAmount.toPlainString()).append(",");
            json.append("\"finalWithVat\":").append(finalWithVat.toPlainString());
            json.append("}");
            out.print(json.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Lỗi hệ thống: " + e.getMessage().replace("\"", "'") + "\"}");
        } finally {
            out.flush();
        }
    }
    
    private int insertOrderAndDetails(model.Order order,
                                      java.util.List<Seat> seats,
                                      java.util.List<java.math.BigDecimal> ticketPrices,
                                      java.util.List<java.util.Map<String, Object>> selectedProducts,
                                      int maSuatChieu,
                                      String ticketStatus) throws Exception {
        int maOrder = orderDAO.createOrder(order);
        if (maOrder <= 0) throw new IllegalStateException("Không tạo được đơn hàng");
        // Xác thực tồn tại
        model.Order checkOrder = orderDAO.getOrderById(maOrder);
        if (checkOrder == null) throw new IllegalStateException("Đơn hàng chưa tồn tại sau khi tạo");

        // Chèn vé
        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            java.math.BigDecimal finalPrice = ticketPrices.get(i);
            Ticket t = new Ticket();
            t.setMaSuatChieu(maSuatChieu);
            t.setMaGhe(seat.getMaGhe());
            t.setGiaVe(finalPrice);
            t.setTrangThai(ticketStatus);
            ticketDAO.addTicketWithOrder(maOrder, t);
        }
        // Chèn combo
        if (selectedProducts != null && !selectedProducts.isEmpty()) {
            dal.OrderFoodDetailDAO ofdDAO = new dal.OrderFoodDetailDAO();
            for (java.util.Map<String, Object> pi : selectedProducts) {
                Product p = (Product) pi.get("product");
                int qty = (Integer) pi.get("quantity");
                java.math.BigDecimal price = new java.math.BigDecimal(p.getDonGia()).multiply(new java.math.BigDecimal(qty));
                model.OrderFoodDetail od = new model.OrderFoodDetail();
                od.setMaOrder(maOrder);
                od.setMaSP(p.getMaSP());
                od.setSoLuong(qty);
                od.setDonGia(price.divide(new java.math.BigDecimal(qty), 2, java.math.RoundingMode.HALF_UP));
                od.setThanhTien(price);
                ofdDAO.createOrderFoodDetail(od);
            }
        }
        return maOrder;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
