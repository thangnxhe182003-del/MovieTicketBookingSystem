package controller;

import dal.*;
import model.*;
import util.VnPayConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "PaymentServlet", urlPatterns = {"/payment"})
public class PaymentServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private ProductDAO productDAO;
    private TicketDAO ticketDAO;
    private ShowtimeDAO showtimeDAO;
    private SeatDAO seatDAO;
    private SeatHoldDAO seatHoldDAO;
    private DiscountDAO discountDAO;
    private CustomerDAO customerDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        productDAO = new ProductDAO();
        ticketDAO = new TicketDAO();
        showtimeDAO = new ShowtimeDAO();
        seatDAO = new SeatDAO();
        seatHoldDAO = new SeatHoldDAO();
        discountDAO = new DiscountDAO();
        customerDAO = new CustomerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("create".equals(action)) {
            createPayment(req, resp);
        } else if ("return".equals(action)) {
            handleReturn(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void createPayment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Customer customer = (Customer) session.getAttribute("loggedInUser");
        if (customer == null) {
            resp.sendRedirect("login");
            return;
        }
        String amountStr = req.getParameter("amount");
        String orderInfo = (String) session.getAttribute("pay_orderCode");
        if (orderInfo == null) orderInfo = req.getParameter("orderInfo");
        if (amountStr == null) {
            BigDecimal amount = (BigDecimal) session.getAttribute("pay_amount");
            amountStr = amount != null ? amount.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString() : "0";
        }
        long amount = Long.parseLong(amountStr) * 100;

        Map<String, String> vnp_Params = new java.util.TreeMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", VnPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", String.valueOf(System.currentTimeMillis()));
        vnp_Params.put("vnp_OrderInfo", orderInfo != null ? orderInfo : "MovieNow thanh toan ve xem phim");
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", req.getRequestURL().toString() + "?action=return");
        vnp_Params.put("vnp_IpAddr", req.getRemoteAddr());
        vnp_Params.put("vnp_CreateDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        String query = util.VnPayConfig.buildQueryAndHash(vnp_Params, VnPayConfig.vnp_HashSecret);
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + query;
        resp.sendRedirect(paymentUrl);
    }

    private void handleReturn(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Map<String, String> fields = new java.util.TreeMap<>();
        req.getParameterMap().forEach((k, v) -> {
            if (k.startsWith("vnp_")) {
                fields.put(k, v[0]);
            }
        });
        String secureHash = fields.remove("vnp_SecureHash");
        String rawData = fields.entrySet().stream()
                .map(e -> VnPayConfig.encode(e.getKey()) + "=" + VnPayConfig.encode(e.getValue()))
                .collect(java.util.stream.Collectors.joining("&"));
        String calcHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, rawData);

        boolean verified = secureHash != null && secureHash.equalsIgnoreCase(calcHash);
        String responseCode = req.getParameter("vnp_ResponseCode");
        String orderCode = req.getParameter("vnp_OrderInfo");

        HttpSession session = req.getSession();
        Integer maSuatChieu = (Integer) session.getAttribute("pay_maSuatChieu");
        @SuppressWarnings("unchecked")
        java.util.List<Long> holdIds = (java.util.List<Long>) session.getAttribute("pay_holdIds");

        if (verified && "00".equals(responseCode)) {
            // SUCCESS
            Order order = orderDAO.getOrderByCode(orderCode);
            if (order != null) {
                orderDAO.markOrderAsPaid(order.getMaOrder());
                // Update tickets
                // Không có method bulk, dùng SQL update theo MaOrder
                try (java.sql.PreparedStatement ps = orderDAO.connection.prepareStatement(
                        "UPDATE dbo.Ticket SET TrangThai = N'Đã thanh toán' WHERE MaOrder = ?")) {
                    ps.setInt(1, order.getMaOrder());
                    ps.executeUpdate();
                } catch (Exception ignored) {}
                // Delete seat holds
                if (holdIds != null) {
                    for (Long hid : holdIds) {
                        seatHoldDAO.deleteSeatHold(hid);
                    }
                }
                // Tăng số lần sử dụng mã giảm giá nếu có
                if (order.getMaGiamGia() != null) {
                    discountDAO.incrementDiscountUsage(order.getMaGiamGia());
                }
                // Gửi email tóm tắt đơn hàng
                try {
                    Customer customer = customerDAO.getCustomerById(order.getMaKH());
                    if (customer != null && customer.getEmail() != null && !customer.getEmail().trim().isEmpty()) {
                        util.EmailService.sendOrderSummaryEmail(customer.getEmail(), customer.getHoTen(), order.getMaOrder());
                    }
                } catch (Exception e) {
                    System.err.println("[PaymentServlet] Error sending order summary email: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            req.getRequestDispatcher("Views/common/payment-success.jsp").forward(req, resp);
        } else {
            // FAIL: mark order/tickets failed and delete holds
            Order order = orderDAO.getOrderByCode(orderCode);
            if (order != null) {
                orderDAO.updateOrderStatus(order.getMaOrder(), "Thanh toán thất bại");
                try (java.sql.PreparedStatement ps = orderDAO.connection.prepareStatement(
                        "UPDATE dbo.Ticket SET TrangThai = N'Thanh toán thất bại' WHERE MaOrder = ?")) {
                    ps.setInt(1, order.getMaOrder());
                    ps.executeUpdate();
                } catch (Exception ignored) {}
                if (holdIds != null) {
                    for (Long hid : holdIds) {
                        seatHoldDAO.deleteSeatHold(hid);
                    }
                }
            }
            req.getRequestDispatcher("Views/common/payment-fail.jsp").forward(req, resp);
        }
    }
}
