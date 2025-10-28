package controller;

import dal.OrderDAO;
import dal.OrderFoodDetailDAO;
import dal.TicketDAO;
import dal.ShowtimeDAO;
import dal.SeatDAO;
import dal.MovieDAO;
import dal.RoomDAO;
import dal.ProductDAO;
import model.Customer;
import model.Order;
import model.OrderFoodDetail;
import model.Ticket;
import model.Showtime;
import model.Seat;
import model.Movie;
import model.Room;
import model.Product;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "OrderHistoryServlet", urlPatterns = {"/order-history"})
public class OrderHistoryServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private TicketDAO ticketDAO;
    private OrderFoodDetailDAO orderFoodDetailDAO;
    private ShowtimeDAO showtimeDAO;
    private SeatDAO seatDAO;
    private MovieDAO movieDAO;
    private RoomDAO roomDAO;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        ticketDAO = new TicketDAO();
        orderFoodDetailDAO = new OrderFoodDetailDAO();
        showtimeDAO = new ShowtimeDAO();
        seatDAO = new SeatDAO();
        movieDAO = new MovieDAO();
        roomDAO = new RoomDAO();
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("loggedInUser");
        if (customer == null) {
            response.sendRedirect("login");
            return;
        }

        List<Order> orders = orderDAO.getPaidOrdersByCustomer(customer.getMaKH());
        List<Map<String, Object>> orderViews = new ArrayList<>();
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter paidFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Order o : orders) {
            Map<String, Object> view = new HashMap<>();
            view.put("order", o);
            view.put("formattedPaidAt", o.getPaidAt() != null ? o.getPaidAt().format(paidFmt) : "");

            // Tickets and showtime/movie meta
            List<Ticket> tickets = ticketDAO.getTicketsByOrder(o.getMaOrder());
            view.put("tickets", tickets);

            if (!tickets.isEmpty()) {
                Ticket first = tickets.get(0);
                Showtime st = showtimeDAO.getShowtimeById(first.getMaSuatChieu());
                if (st != null) {
                    view.put("showDate", st.getNgayChieu() != null ? st.getNgayChieu().format(dateFmt) : "");
                    view.put("startTime", st.getGioBatDau() != null ? st.getGioBatDau().format(timeFmt) : "");
                    view.put("endTime", st.getGioKetThuc() != null ? st.getGioKetThuc().format(timeFmt) : "");
                    Movie mv = movieDAO.getMovieById(st.getMaPhim());
                    if (mv != null) {
                        view.put("movieTitle", mv.getTenPhim());
                        view.put("moviePoster", mv.getPoster());
                        view.put("duration", mv.getThoiLuong());
                        // new meta
                        view.put("movieType", mv.getLoaiPhim());
                        view.put("subtitleLang", mv.getNgonNguPhuDe());
                        view.put("ageLimit", mv.getDoTuoiGioiHan());
                    }
                    Room rm = roomDAO.getRoomById(st.getMaPhong());
                    if (rm != null) {
                        view.put("roomName", rm.getTenPhong());
                        view.put("cinemaName", rm.getTenRap());
                    }
                }
                // Map seat labels
                List<Map<String, Object>> seatViews = new ArrayList<>();
                for (Ticket t : tickets) {
                    Seat seat = seatDAO.getSeatById(t.getMaGhe());
                    Map<String, Object> sv = new HashMap<>();
                    sv.put("label", seat != null ? (seat.getHangGhe() + String.valueOf(seat.getSoGhe())) : ("#" + t.getMaGhe()));
                    sv.put("price", t.getGiaVe());
                    seatViews.add(sv);
                }
                view.put("seatViews", seatViews);
            }

            // Food items with product details
            List<OrderFoodDetail> items = orderFoodDetailDAO.getByOrder(o.getMaOrder());
            List<Map<String, Object>> itemViews = new ArrayList<>();
            for (OrderFoodDetail it : items) {
                Product p = productDAO.getProductById(it.getMaSP());
                Map<String, Object> iv = new HashMap<>();
                iv.put("name", p != null ? p.getTenSP() : ("SP #" + it.getMaSP()));
                iv.put("thumb", p != null ? p.getThumbnailUrl() : null);
                iv.put("qty", it.getSoLuong());
                iv.put("unit", it.getDonGia());
                BigDecimal subtotal = it.getDonGia() != null ? it.getDonGia().multiply(new BigDecimal(it.getSoLuong())) : BigDecimal.ZERO;
                iv.put("subtotal", subtotal);
                itemViews.add(iv);
            }
            view.put("itemViews", itemViews);

            orderViews.add(view);
        }

        request.setAttribute("orders", orderViews);
        request.getRequestDispatcher("Views/common/order-history.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
