package controller;

import dal.*;
import model.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "AdminTicketServlet", urlPatterns = {"/admin-tickets"})
public class AdminTicketServlet extends HttpServlet {

    private TicketDAO ticketDAO;
    private OrderDAO orderDAO;
    private ShowtimeDAO showtimeDAO;
    private MovieDAO movieDAO;
    private RoomDAO roomDAO;
    private OrderFoodDetailDAO ofdDAO;
    private ProductDAO productDAO;
    private SeatDAO seatDAO;
    private CinemaDAO cinemaDAO;

    @Override
    public void init() throws ServletException {
        ticketDAO = new TicketDAO();
        orderDAO = new OrderDAO();
        showtimeDAO = new ShowtimeDAO();
        movieDAO = new MovieDAO();
        roomDAO = new RoomDAO();
        ofdDAO = new OrderFoodDetailDAO();
        productDAO = new ProductDAO();
        seatDAO = new SeatDAO();
        cinemaDAO = new CinemaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chỉ Manager mới được truy cập
        if (!util.RoleChecker.requireManager(request, response)) return;
        
        String action = request.getParameter("action");
        if ("checkin".equals(action)) { doCheckin(request, response); return; }
        if ("details".equals(action)) { serveDetails(request, response); return; }

        String status = request.getParameter("status");
        boolean onlyFromToday = request.getParameter("from") == null || "today".equals(request.getParameter("from"));
        boolean onlySuccessByDefault = (status == null || status.isEmpty());
        
        // Lấy filter theo phim, rạp, ngày
        String maPhimStr = request.getParameter("maPhim");
        String maRapStr = request.getParameter("maRap");
        String ngayFilter = request.getParameter("ngay");
        Integer maPhimFilter = null;
        Integer maRapFilter = null;
        try {
            if (maPhimStr != null && !maPhimStr.trim().isEmpty()) {
                maPhimFilter = Integer.parseInt(maPhimStr.trim());
            }
        } catch (NumberFormatException e) {}
        try {
            if (maRapStr != null && !maRapStr.trim().isEmpty()) {
                maRapFilter = Integer.parseInt(maRapStr.trim());
            }
        } catch (NumberFormatException e) {}
        
        System.out.println("[AdminTicketServlet] list status=" + status + ", fromToday=" + onlyFromToday + ", onlySuccessByDefault=" + onlySuccessByDefault 
                + ", maPhim=" + maPhimFilter + ", maRap=" + maRapFilter + ", ngay=" + ngayFilter);

        List<TicketDAO.GroupedTicketsRow> groups;
        if (onlySuccessByDefault && (status == null || status.isEmpty())) {
            // Mặc định: hiển thị cả hai trạng thái đã thanh toán (VNPAY) và tiền mặt
            List<TicketDAO.GroupedTicketsRow> paid = ticketDAO.getGroupedTickets("Đã thanh toán", onlyFromToday, false, maPhimFilter, maRapFilter, ngayFilter);
            List<TicketDAO.GroupedTicketsRow> cash = ticketDAO.getGroupedTickets("Thanh toan tien mat", onlyFromToday, false, maPhimFilter, maRapFilter, ngayFilter);
            groups = new java.util.ArrayList<>();
            if (paid != null) groups.addAll(paid);
            if (cash != null) groups.addAll(cash);
        } else {
            // Nếu lọc 'Thanh toan tien mat' thì vé vẫn có trạng thái 'Đã thanh toán'
            String ticketStatusToFetch = status;
            if ("Thanh toan tien mat".equals(status)) {
                ticketStatusToFetch = "Đã thanh toán";
            }
            groups = ticketDAO.getGroupedTickets(ticketStatusToFetch, onlyFromToday, onlySuccessByDefault, maPhimFilter, maRapFilter, ngayFilter);
        }
        System.out.println("[AdminTicketServlet] groups.size=" + (groups != null ? groups.size() : 0));

        // Map trạng thái Order theo MaOrder để hiển thị đúng (Order.TrangThai có thể là 'Thanh toan tien mat')
        Map<Integer, String> orderStatusMap = new HashMap<>();
        if (groups != null) {
            for (TicketDAO.GroupedTicketsRow g : groups) {
                try {
                    Order od = orderDAO.getOrderById(g.getMaOrder());
                    if (od != null && od.getTrangThai() != null) {
                        orderStatusMap.put(g.getMaOrder(), od.getTrangThai());
                    }
                } catch (Exception ignored) {}
            }
        }

        // Lấy danh sách phim và rạp cho filter dropdown
        List<Movie> movies = movieDAO.getAllMovies();
        List<Cinema> cinemas = cinemaDAO.getAllCinemas();
        
        // Áp dụng filter theo trạng thái dựa trên trạng thái Order để phân biệt VNPAY vs Tiền mặt
        if (status != null && !status.isEmpty()) {
            if ("Thanh toan tien mat".equals(status) || "Đã thanh toán".equals(status)) {
                List<TicketDAO.GroupedTicketsRow> filtered = new ArrayList<>();
                for (TicketDAO.GroupedTicketsRow g : groups) {
                    String os = orderStatusMap.get(g.getMaOrder());
                    if (status.equals(os)) filtered.add(g);
                }
                groups = filtered;
            }
        }

        request.setAttribute("groups", groups);
        request.setAttribute("orderStatusMap", orderStatusMap);
        request.setAttribute("filterStatus", status);
        request.setAttribute("from", onlyFromToday ? "today" : "all");
        request.setAttribute("movies", movies);
        request.setAttribute("cinemas", cinemas);
        request.setAttribute("selectedMaPhim", maPhimFilter);
        request.setAttribute("selectedMaRap", maRapFilter);
        request.setAttribute("selectedNgay", ngayFilter);
        request.setAttribute("currentTime", LocalDateTime.now());

        request.getRequestDispatcher("Views/admin/ticket-list.jsp").forward(request, response);
    }

    private void serveDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String mo = request.getParameter("maOrder");
        System.out.println("[AdminTicketServlet] details maOrder=" + mo);
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            int maOrder = Integer.parseInt(mo);
            Order order = orderDAO.getOrderById(maOrder);
            List<Ticket> tickets = ticketDAO.getTicketsByOrder(maOrder);
            System.out.println("[AdminTicketServlet] details tickets.size=" + (tickets != null ? tickets.size() : 0));
            Map<String,Object> data = new HashMap<>();
            data.put("maOrder", maOrder);
            if (order != null) {
                data.put("orderCode", order.getOrderCode());
                data.put("tongTien", order.getTongTien());
                data.put("trangThai", order.getTrangThai());
                data.put("paidAt", order.getPaidAt() != null ? order.getPaidAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "");
            }
            if (!tickets.isEmpty()) {
                int maSuatChieu = tickets.get(0).getMaSuatChieu();
                Showtime st = showtimeDAO.getShowtimeById(maSuatChieu);
                if (st != null) {
                    Movie mv = movieDAO.getMovieById(st.getMaPhim());
                    Room rm = roomDAO.getRoomById(st.getMaPhong());
                    Map<String,Object> show = new HashMap<>();
                    show.put("date", st.getNgayChieu() != null ? st.getNgayChieu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
                    show.put("start", st.getGioBatDau() != null ? st.getGioBatDau().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
                    show.put("end", st.getGioKetThuc() != null ? st.getGioKetThuc().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
                    if (mv != null) {
                        show.put("movieTitle", mv.getTenPhim());
                        show.put("poster", mv.getPoster());
                    }
                    if (rm != null) {
                        show.put("cinema", rm.getTenRap());
                        show.put("room", rm.getTenPhong());
                    }
                    data.put("showtime", show);
                }
            }
            List<Map<String,Object>> ticketViews = new ArrayList<>();
            for (Ticket t : tickets) {
                Map<String,Object> tv = new HashMap<>();
                Seat seat = seatDAO.getSeatById(t.getMaGhe());
                String seatLabel = seat != null ? seat.getHangGhe() + String.valueOf(seat.getSoGhe()) : ("#" + t.getMaGhe());
                tv.put("soGhe", seatLabel);
                tv.put("donGia", t.getGiaVe());
                ticketViews.add(tv);
            }
            data.put("tickets", ticketViews);

            List<OrderFoodDetail> items = ofdDAO.getByOrder(maOrder);
            List<Map<String,Object>> itemViews = new ArrayList<>();
            for (OrderFoodDetail it : items) {
                Product p = productDAO.getProductById(it.getMaSP());
                Map<String,Object> iv = new HashMap<>();
                iv.put("name", p != null ? p.getTenSP() : ("SP #" + it.getMaSP()));
                iv.put("thumb", p != null ? p.getThumbnailUrl() : null);
                iv.put("qty", it.getSoLuong());
                iv.put("unit", it.getDonGia());
                iv.put("subtotal", it.getDonGia() != null ? it.getDonGia().multiply(new java.math.BigDecimal(it.getSoLuong())) : java.math.BigDecimal.ZERO);
                itemViews.add(iv);
            }
            data.put("items", itemViews);

            out.print(toJson(data));
        } catch (Exception e) {
            System.err.println("[AdminTicketServlet] details ERROR: " + e.getMessage());
            out.print("{\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}");
        } finally {
            out.flush();
        }
    }

    private String toJson(Object obj) {
        if (obj instanceof Map) {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?,?> e : ((Map<?,?>) obj).entrySet()) {
                if (!first) sb.append(','); first = false;
                sb.append('"').append(e.getKey()).append('"').append(':').append(toJson(e.getValue()));
            }
            return sb.append('}').toString();
        } else if (obj instanceof List) {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object o : (List<?>) obj) { if (!first) sb.append(','); first=false; sb.append(toJson(o)); }
            return sb.append(']').toString();
        } else if (obj instanceof String) {
            return "\"" + ((String) obj).replace("\\","\\\\").replace("\"","\\\"") + "\"";
        } else if (obj instanceof Number || obj instanceof Boolean) {
            return String.valueOf(obj);
        } else if (obj == null) {
            return "null";
        }
        return "\"" + obj.toString().replace("\"","\\\"") + "\"";
    }

    private void doCheckin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String mo = request.getParameter("maOrder");
        if (mo != null && !mo.trim().isEmpty()) {
            try {
                int maOrder = Integer.parseInt(mo.trim());
                ticketDAO.updateTicketsStatusByOrder(maOrder, "Đã sử dụng");
            } catch (NumberFormatException ignored) {}
        }
        response.sendRedirect("admin-tickets");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
