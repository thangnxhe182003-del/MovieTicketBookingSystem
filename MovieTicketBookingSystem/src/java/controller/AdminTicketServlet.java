package controller;

import dal.*;
import model.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("checkin".equals(action)) { doCheckin(request, response); return; }
        if ("details".equals(action)) { serveDetails(request, response); return; }

        String status = request.getParameter("status");
        boolean onlyFromToday = request.getParameter("from") == null || "today".equals(request.getParameter("from"));
        boolean onlySuccessByDefault = (status == null || status.isEmpty());
        System.out.println("[AdminTicketServlet] list status=" + status + ", fromToday=" + onlyFromToday + ", onlySuccessByDefault=" + onlySuccessByDefault);

        List<TicketDAO.GroupedTicketsRow> groups = ticketDAO.getGroupedTickets(status, onlyFromToday, onlySuccessByDefault);
        System.out.println("[AdminTicketServlet] groups.size=" + (groups != null ? groups.size() : 0));

        request.setAttribute("groups", groups);
        request.setAttribute("filterStatus", status);
        request.setAttribute("from", onlyFromToday ? "today" : "all");

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
