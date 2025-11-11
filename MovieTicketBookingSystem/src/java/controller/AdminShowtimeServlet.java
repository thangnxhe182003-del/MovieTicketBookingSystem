package controller;

import dal.MovieDAO;
import dal.RoomDAO;
import dal.ShowtimeDAO;
import dal.TicketDAO;
import dal.SeatDAO;
import util.ShowtimeSuggestionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Movie;
import model.Room;
import model.Showtime;
import model.Ticket;
import model.Seat;
import model.TimeSlot;

public class AdminShowtimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        ShowtimeDAO showtimeDAO = new ShowtimeDAO();
        MovieDAO movieDAO = new MovieDAO();
        RoomDAO roomDAO = new RoomDAO();

        switch (action) {
            case "list":
                listShowtimes(request, response, showtimeDAO);
                break;
            case "create":
                showCreateForm(request, response, movieDAO, roomDAO);
                break;
            case "edit":
                showEditForm(request, response, showtimeDAO, movieDAO, roomDAO);
                break;
            case "suggest":
                suggestShowtimes(request, response, movieDAO, roomDAO);
                break;
            case "delete":
                deleteShowtime(request, response, showtimeDAO);
                break;
            default:
                response.sendRedirect("admin-showtimes");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("admin-showtimes");
            return;
        }

        ShowtimeDAO showtimeDAO = new ShowtimeDAO();

        switch (action) {
            case "create":
                createShowtime(request, response, showtimeDAO);
                break;
            case "create-bulk":
                createBulkShowtimes(request, response, showtimeDAO);
                break;
            case "update":
                updateShowtime(request, response, showtimeDAO);
                break;
            case "delete":
                deleteShowtime(request, response, showtimeDAO);
                break;
            default:
                response.sendRedirect("admin-showtimes");
        }
    }

    private void listShowtimes(HttpServletRequest request, HttpServletResponse response, ShowtimeDAO showtimeDAO)
            throws ServletException, IOException {
        // Lấy tham số filter từ request
        String cinemaFilter = request.getParameter("cinema");
        String roomFilter = request.getParameter("room");
        String dateFilter = request.getParameter("date");
        
        // Xác định khoảng ngày (mặc định 7 ngày tới)
        java.time.LocalDate startDate = java.time.LocalDate.now();
        java.time.LocalDate endDate = startDate.plusDays(7);
        
        if (dateFilter != null && !dateFilter.isEmpty()) {
            try {
                startDate = java.time.LocalDate.parse(dateFilter);
                endDate = startDate.plusDays(1); // Chỉ lấy ngày được chọn
            } catch (Exception e) {
                // Nếu parse lỗi, dùng giá trị mặc định
            }
        }

        List<Showtime> allShowtimes = showtimeDAO.getShowtimesInDateRange(startDate, endDate);

        // Apply filters
        List<Showtime> filteredShowtimes = new ArrayList<>();
        for (Showtime showtime : allShowtimes) {
            boolean shouldInclude = true;
            
            if (cinemaFilter != null && !cinemaFilter.isEmpty()) {
                if (showtime.getTenRap() == null || !showtime.getTenRap().toLowerCase().contains(cinemaFilter.toLowerCase())) {
                    shouldInclude = false;
                }
            }
            
            if (roomFilter != null && !roomFilter.isEmpty()) {
                if (showtime.getTenPhong() == null || !showtime.getTenPhong().toLowerCase().contains(roomFilter.toLowerCase())) {
                    shouldInclude = false;
                }
            }
            
            if (shouldInclude) {
                filteredShowtimes.add(showtime);
            }
        }

        // Nhóm suất chiếu theo rạp > ngày > phòng
        Map<String, Map<String, Map<String, List<Showtime>>>> organizedShowtimes = new java.util.LinkedHashMap<>();
        java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", java.util.Locale.forLanguageTag("vi"));
        java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");

        // Set current time để kiểm tra suất chiếu quá khứ
        LocalDateTime now = LocalDateTime.now();
        request.setAttribute("currentTime", now);
        
        for (Showtime showtime : filteredShowtimes) {
            if (showtime.getNgayChieu() != null) {
                String cinema = showtime.getTenRap() != null ? showtime.getTenRap() : "Rạp không xác định";
                java.time.LocalDate date = showtime.getNgayChieu().toLocalDate();
                String formattedDate = date.format(dateFormatter);
                String room = showtime.getTenPhong() != null ? showtime.getTenPhong() : "Phòng không xác định";
                
                organizedShowtimes
                    .computeIfAbsent(cinema, k -> new java.util.LinkedHashMap<>())
                    .computeIfAbsent(formattedDate, k -> new java.util.LinkedHashMap<>())
                    .computeIfAbsent(room, k -> new ArrayList<>())
                    .add(showtime);
            }
        }
        
        // Sort showtimes by time within each room
        for (Map<String, Map<String, List<Showtime>>> dateMap : organizedShowtimes.values()) {
            for (Map<String, List<Showtime>> roomMap : dateMap.values()) {
                for (List<Showtime> showtimes : roomMap.values()) {
                    showtimes.sort((s1, s2) -> s1.getGioBatDau().compareTo(s2.getGioBatDau()));
                }
            }
        }

        // Lấy danh sách rạp và phòng để populate filter dropdowns
        RoomDAO roomDAO = new RoomDAO();
        List<Room> allRooms = roomDAO.getAllRooms();
        
        // Extract unique cinema names
        java.util.Set<String> cinemaNames = new java.util.LinkedHashSet<>();
        for (Room room : allRooms) {
            if (room.getTenRap() != null) {
                cinemaNames.add(room.getTenRap());
            }
        }

        // Convert rooms to JSON for dynamic filtering
        StringBuilder roomsJson = new StringBuilder("[");
        for (int i = 0; i < allRooms.size(); i++) {
            Room room = allRooms.get(i);
            if (i > 0) roomsJson.append(",");
            roomsJson.append("{")
                .append("\"tenPhong\":\"").append(room.getTenPhong() != null ? room.getTenPhong().replace("\"", "\\\"") : "").append("\",")
                .append("\"tenRap\":\"").append(room.getTenRap() != null ? room.getTenRap().replace("\"", "\\\"") : "").append("\"")
                .append("}");
        }
        roomsJson.append("]");
        
        request.setAttribute("organizedShowtimes", organizedShowtimes);
        request.setAttribute("timeFormatter", timeFormatter);
        request.setAttribute("allRooms", allRooms);
        request.setAttribute("allRoomsJson", roomsJson.toString());
        request.setAttribute("cinemaNames", cinemaNames);
        request.setAttribute("selectedCinema", cinemaFilter);
        request.setAttribute("selectedRoom", roomFilter);
        request.setAttribute("selectedDate", dateFilter);
        request.setAttribute("totalShowtimes", filteredShowtimes.size());
        
        request.getRequestDispatcher("Views/admin/showtime-list.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response,
            MovieDAO movieDAO, RoomDAO roomDAO)
            throws ServletException, IOException {
        List<Movie> movies = movieDAO.getAllMovies();
        List<Room> rooms = roomDAO.getAllRooms();

        request.setAttribute("movies", movies);
        request.setAttribute("rooms", rooms);
        request.getRequestDispatcher("Views/admin/showtime-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response,
            ShowtimeDAO showtimeDAO, MovieDAO movieDAO, RoomDAO roomDAO)
            throws ServletException, IOException {
        try {
            int maSuatChieu = Integer.parseInt(request.getParameter("maSuatChieu"));
            Showtime showtime = showtimeDAO.getShowtimeById(maSuatChieu);

            if (showtime == null) {
                response.sendRedirect("admin-showtimes");
                return;
            }

            List<Movie> movies = movieDAO.getAllMovies();
            List<Room> rooms = roomDAO.getAllRooms();

            // Format date and time for display
            String formattedDate = showtime.getNgayChieu().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String formattedStartTime = showtime.getGioBatDau().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            String formattedEndTime = showtime.getGioKetThuc().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));

            request.setAttribute("showtime", showtime);
            request.setAttribute("formattedDate", formattedDate);
            request.setAttribute("formattedStartTime", formattedStartTime);
            request.setAttribute("formattedEndTime", formattedEndTime);
            request.setAttribute("movies", movies);
            request.setAttribute("rooms", rooms);
            request.getRequestDispatcher("Views/admin/showtime-form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("admin-showtimes");
        }
    }

    private void createShowtime(HttpServletRequest request, HttpServletResponse response, ShowtimeDAO showtimeDAO)
            throws ServletException, IOException {
        try {
            int maPhim = Integer.parseInt(request.getParameter("maPhim"));
            int maPhong = Integer.parseInt(request.getParameter("maPhong"));
            java.time.LocalDateTime ngayChieu = java.time.LocalDateTime.parse(
                    request.getParameter("ngayChieu") + "T00:00:00",
                    java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
            );
            java.time.LocalTime gioBatDauTime = java.time.LocalTime.parse(request.getParameter("gioBatDau"));
            java.time.LocalTime gioKetThucTime = java.time.LocalTime.parse(request.getParameter("gioKetThuc"));
            java.time.LocalDateTime gioBatDau = ngayChieu.toLocalDate().atTime(gioBatDauTime);
            java.time.LocalDateTime gioKetThuc = ngayChieu.toLocalDate().atTime(gioKetThucTime);
            BigDecimal giaVeCoSo = parseDecimalOrDefault(request.getParameter("giaVeCoSo"), new BigDecimal("75000"));
            BigDecimal giaVeTreEm = parseDecimalOrDefault(request.getParameter("giaVeTreEm"), new BigDecimal("50000"));
            BigDecimal vat = parseDecimalOrDefault(request.getParameter("vat"), new BigDecimal("10.00"));
            String ngonNguAmThanh = request.getParameter("ngonNguAmThanh");

            // Debug: In ra dữ liệu từ form
            System.out.println("=== DEBUG createShowtime ===");
            System.out.println("MaPhim: " + maPhim);
            System.out.println("MaPhong: " + maPhong);
            System.out.println("NgayChieu (from form): " + request.getParameter("ngayChieu"));
            System.out.println("GioBatDau (from form): " + request.getParameter("gioBatDau"));
            System.out.println("GioKetThuc (from form): " + request.getParameter("gioKetThuc"));
            System.out.println("Parsed ngayChieu: " + ngayChieu);
            System.out.println("Parsed gioBatDau: " + gioBatDau);
            System.out.println("Parsed gioKetThuc: " + gioKetThuc);
            System.out.println("GiaVeCoSo: " + giaVeCoSo);
            System.out.println("NgonNguAmThanh: " + ngonNguAmThanh);
            System.out.println("=================================");
            
            // Kiểm tra trùng giờ và lấy thông tin chi tiết
            Showtime conflictingShowtime = showtimeDAO.getConflictingShowtime(maPhong, ngayChieu.toLocalDate(), gioBatDau.toLocalTime(), gioKetThuc.toLocalTime(), 0);
            if (conflictingShowtime != null) {
                String errorMessage = String.format("Không thể tạo suất chiếu! Phòng đã có suất chiếu phim '%s' từ %s đến %s trong khung giờ này.", 
                    conflictingShowtime.getTenPhim(),
                    conflictingShowtime.getGioBatDau().toLocalTime().toString(),
                    conflictingShowtime.getGioKetThuc().toLocalTime().toString()
                );
                response.sendRedirect("admin-showtimes?action=create&error=" + java.net.URLEncoder.encode(errorMessage, "UTF-8"));
                return;
            }

            Showtime showtime = new Showtime();
            showtime.setMaPhim(maPhim);
            showtime.setMaPhong(maPhong);
            showtime.setNgayChieu(ngayChieu);
            showtime.setGioBatDau(gioBatDau);
            showtime.setGioKetThuc(gioKetThuc);
            showtime.setGiaVeCoSo(giaVeCoSo);
            showtime.setGiaVeTreEm(giaVeTreEm);
            showtime.setVat(vat);
            showtime.setNgonNguAmThanh(ngonNguAmThanh);

            if (showtimeDAO.addShowtime(showtime)) {
                // Tạo ticket cho tất cả ghế trong phòng
                createTicketsForShowtime(showtime.getMaSuatChieu(), maPhong, giaVeCoSo);
                response.sendRedirect("admin-showtimes?success=" + java.net.URLEncoder.encode("Tạo suất chiếu thành công", "UTF-8"));
            } else {
                response.sendRedirect("admin-showtimes?action=create&error=" + java.net.URLEncoder.encode("Lỗi khi tạo suất chiếu", "UTF-8"));
            }
        } catch (Exception e) {
            response.sendRedirect("admin-showtimes?action=create&error=" + java.net.URLEncoder.encode("Dữ liệu không hợp lệ", "UTF-8"));
        }
    }

    private void updateShowtime(HttpServletRequest request, HttpServletResponse response, ShowtimeDAO showtimeDAO)
            throws ServletException, IOException {
        try {
            int maSuatChieu = Integer.parseInt(request.getParameter("maSuatChieu"));
            int maPhim = Integer.parseInt(request.getParameter("maPhim"));
            int maPhong = Integer.parseInt(request.getParameter("maPhong"));
            java.time.LocalDateTime ngayChieu = java.time.LocalDateTime.parse(
                    request.getParameter("ngayChieu") + "T00:00:00",
                    java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
            );
            java.time.LocalTime gioBatDauTime = java.time.LocalTime.parse(request.getParameter("gioBatDau"));
            java.time.LocalTime gioKetThucTime = java.time.LocalTime.parse(request.getParameter("gioKetThuc"));
            java.time.LocalDateTime gioBatDau = ngayChieu.toLocalDate().atTime(gioBatDauTime);
            java.time.LocalDateTime gioKetThuc = ngayChieu.toLocalDate().atTime(gioKetThucTime);
            BigDecimal giaVeCoSo = parseDecimalOrDefault(request.getParameter("giaVeCoSo"), new BigDecimal("75000"));
            BigDecimal giaVeTreEm = parseDecimalOrDefault(request.getParameter("giaVeTreEm"), new BigDecimal("50000"));
            BigDecimal vat = parseDecimalOrDefault(request.getParameter("vat"), new BigDecimal("10.00"));
            String ngonNguAmThanh = request.getParameter("ngonNguAmThanh");

            // Kiểm tra trùng giờ và lấy thông tin chi tiết (loại trừ suất chiếu hiện tại)
            Showtime conflictingShowtime = showtimeDAO.getConflictingShowtime(maPhong, ngayChieu.toLocalDate(), gioBatDau.toLocalTime(), gioKetThuc.toLocalTime(), maSuatChieu);
            if (conflictingShowtime != null) {
                String errorMessage = String.format("Không thể cập nhật suất chiếu! Phòng đã có suất chiếu phim '%s' từ %s đến %s trong khung giờ này.", 
                    conflictingShowtime.getTenPhim(),
                    conflictingShowtime.getGioBatDau().toLocalTime().toString(),
                    conflictingShowtime.getGioKetThuc().toLocalTime().toString()
                );
                response.sendRedirect("admin-showtimes?action=edit&maSuatChieu=" + maSuatChieu + "&error=" + java.net.URLEncoder.encode(errorMessage, "UTF-8"));
                return;
            }

            Showtime showtime = new Showtime();
            showtime.setMaSuatChieu(maSuatChieu);
            showtime.setMaPhim(maPhim);
            showtime.setMaPhong(maPhong);
            showtime.setNgayChieu(ngayChieu);
            showtime.setGioBatDau(gioBatDau);
            showtime.setGioKetThuc(gioKetThuc);
            showtime.setGiaVeCoSo(giaVeCoSo);
            showtime.setGiaVeTreEm(giaVeTreEm);
            showtime.setVat(vat);
            showtime.setNgonNguAmThanh(ngonNguAmThanh);

            if (showtimeDAO.updateShowtime(showtime)) {
                response.sendRedirect("admin-showtimes?success=" + java.net.URLEncoder.encode("Cập nhật suất chiếu thành công", "UTF-8"));
            } else {
                response.sendRedirect("admin-showtimes?action=edit&maSuatChieu=" + maSuatChieu + "&error=" + java.net.URLEncoder.encode("Lỗi khi cập nhật suất chiếu", "UTF-8"));
            }
        } catch (Exception e) {
            response.sendRedirect("admin-showtimes?error=" + java.net.URLEncoder.encode("Dữ liệu không hợp lệ", "UTF-8"));
        }
    }

    private void deleteShowtime(HttpServletRequest request, HttpServletResponse response, ShowtimeDAO showtimeDAO)
            throws ServletException, IOException {
        try {
            int maSuatChieu = Integer.parseInt(request.getParameter("maSuatChieu"));

            // Debug log
            System.out.println("Debug - Attempting to delete showtime: " + maSuatChieu);

            // Kiểm tra xem có ticket nào đã được đặt cho suất chiếu này không
            TicketDAO ticketDAO = new TicketDAO();
            if (ticketDAO.hasBookedTickets(maSuatChieu)) {
                System.out.println("Debug - Cannot delete showtime " + maSuatChieu + " - has booked tickets");
                request.setAttribute("error", "Không thể xóa suất chiếu này vì đã có vé được đặt. Vui lòng hủy các vé đã đặt trước.");
                listShowtimes(request, response, showtimeDAO);
                return;
            }

            if (showtimeDAO.deleteShowtime(maSuatChieu)) {
                System.out.println("Debug - Successfully deleted showtime: " + maSuatChieu);
                request.setAttribute("success", "Đã gỡ suất chiếu thành công");
            } else {
                System.out.println("Debug - Failed to delete showtime: " + maSuatChieu);
                request.setAttribute("error", "Lỗi khi gỡ suất chiếu");
            }

            // Forward to list page
            listShowtimes(request, response, showtimeDAO);

        } catch (NumberFormatException e) {
            System.out.println("Debug - Invalid maSuatChieu parameter");
            request.setAttribute("error", "Dữ liệu không hợp lệ");
            listShowtimes(request, response, showtimeDAO);
        } catch (Exception e) {
            System.out.println("Debug - Exception during delete: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống khi gỡ suất chiếu");
            listShowtimes(request, response, showtimeDAO);
        }
    }

    /**
     * Tạo ticket cho tất cả ghế trong phòng của một suất chiếu
     */
    private void createTicketsForShowtime(int maSuatChieu, int maPhong, BigDecimal giaVeCoSo) {
        try {
            TicketDAO ticketDAO = new TicketDAO();
            SeatDAO seatDAO = new SeatDAO();

            // Lấy danh sách ghế trong phòng
            List<Seat> seats = seatDAO.getSeatsByRoom(maPhong);

            // Tạo ticket cho mỗi ghế
            List<Ticket> tickets = new ArrayList<>();
            for (Seat seat : seats) {
                // Tính giá vé theo loại ghế
                BigDecimal giaVe = calculateTicketPrice(giaVeCoSo, seat.getLoaiGhe());

                Ticket ticket = new Ticket();
                ticket.setMaSuatChieu(maSuatChieu);
                ticket.setMaGhe(seat.getMaGhe());
                ticket.setLoaiGhe(seat.getLoaiGhe());
                ticket.setGiaVe(giaVe);
                ticket.setTrangThai("Có sẵn");
                ticket.setThoiGianTao(java.time.LocalDateTime.now());
                ticket.setThoiGianCapNhat(java.time.LocalDateTime.now());
                ticket.setGhiChu("Tự động tạo khi tạo suất chiếu");

                tickets.add(ticket);
            }

            // Lưu tất cả ticket
            ticketDAO.bulkCreateTicketsForShowtime(maSuatChieu, tickets);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tính giá vé theo loại ghế
     */
    private BigDecimal calculateTicketPrice(BigDecimal giaVeCoSo, String loaiGhe) {
        if (loaiGhe == null) {
            return giaVeCoSo;
        }

        switch (loaiGhe.toLowerCase()) {
            case "vip":
                return giaVeCoSo.multiply(new BigDecimal("1.5")); // VIP = 150% giá cơ sở
            case "couple":
                return giaVeCoSo.multiply(new BigDecimal("2.0")); // Couple = 200% giá cơ sở
            case "thuong":
            default:
                return giaVeCoSo; // Thường = 100% giá cơ sở
        }
    }

    /**
     * Xử lý đề xuất suất chiếu
     */
    private void suggestShowtimes(HttpServletRequest request, HttpServletResponse response,
            MovieDAO movieDAO, RoomDAO roomDAO) throws ServletException, IOException {
        String maPhimStr = request.getParameter("maPhim");
        String maPhongStr = request.getParameter("maPhong");
        String ngayChieuStr = request.getParameter("ngayChieu");
        String giaVeStr = request.getParameter("giaVeCoSo");
        String ngonNguStr = request.getParameter("ngonNguAmThanh");

        // Debug logs
        System.out.println("Debug - maPhim: '" + maPhimStr + "'");
        System.out.println("Debug - maPhong: '" + maPhongStr + "'");
        System.out.println("Debug - ngayChieu: '" + ngayChieuStr + "'");
        System.out.println("Debug - giaVeCoSo: '" + giaVeStr + "'");
        System.out.println("Debug - ngonNguAmThanh: '" + ngonNguStr + "'");

        if (maPhimStr == null || maPhongStr == null || ngayChieuStr == null) {
            request.setAttribute("error", "Thiếu thông tin để đề xuất suất chiếu");
            response.sendRedirect("admin-showtimes?action=create");
            return;
        }

        try {
            int maPhim = Integer.parseInt(maPhimStr);
            int maPhong = Integer.parseInt(maPhongStr);
            LocalDate ngayChieu = LocalDate.parse(ngayChieuStr);

            // Lấy thông tin phim và phòng
            Movie movie = movieDAO.getMovieById(maPhim);
            Room room = roomDAO.getRoomById(maPhong);

            if (movie == null || room == null) {
                request.setAttribute("error", "Không tìm thấy phim hoặc phòng chiếu");
                response.sendRedirect("admin-showtimes?action=create");
                return;
            }

            // Debug: In ra thông tin phim
            System.out.println("Debug - Phim: " + movie.getTenPhim() + ", Thời lượng: " + movie.getThoiLuong() + " phút");
            System.out.println("Debug - Phòng: " + room.getTenPhong() + ", Ngày: " + ngayChieu);

            // Sử dụng ShowtimeSuggestionService để đề xuất
            ShowtimeSuggestionService suggestionService = new ShowtimeSuggestionService();
            List<TimeSlot> suggestions = suggestionService.suggestShowtimes(maPhong, ngayChieu, movie);

            // Debug: In ra số lượng đề xuất
            System.out.println("Debug - Số lượng đề xuất: " + suggestions.size());

            // Set attributes cho JSP
            request.setAttribute("movie", movie);
            request.setAttribute("room", room);
            request.setAttribute("ngayChieu", ngayChieu);
            request.setAttribute("suggestions", suggestions);
            request.setAttribute("giaVeCoSo", giaVeStr != null ? giaVeStr : "50000");
            request.setAttribute("ngonNguAmThanh", ngonNguStr != null ? ngonNguStr : "Tiếng Việt");

            // Forward đến trang đề xuất
            request.getRequestDispatcher("Views/admin/showtime-suggestions.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Thông tin không hợp lệ");
            response.sendRedirect("admin-showtimes?action=create");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi đề xuất suất chiếu: " + e.getMessage());
            response.sendRedirect("admin-showtimes?action=create");
        }
    }

    /**
     * Tạo nhiều suất chiếu từ đề xuất
     */
    private void createBulkShowtimes(HttpServletRequest request, HttpServletResponse response,
            ShowtimeDAO showtimeDAO) throws ServletException, IOException {
        String maPhimStr = request.getParameter("maPhim");
        String maPhongStr = request.getParameter("maPhong");
        String ngayChieuStr = request.getParameter("ngayChieu");
        String giaVeStr = request.getParameter("giaVeCoSo");
        String ngonNguStr = request.getParameter("ngonNguAmThanh");
        String giaVeTreEmStr = request.getParameter("giaVeTreEm");
        String vatStr = request.getParameter("vat");
        String[] selectedSlots = request.getParameterValues("selectedSlots");

        // Debug logs
        System.out.println("Debug - createBulkShowtimes called");
        System.out.println("Debug - maPhim: " + maPhimStr);
        System.out.println("Debug - maPhong: " + maPhongStr);
        System.out.println("Debug - ngayChieu: " + ngayChieuStr);
        System.out.println("Debug - giaVeCoSo: " + giaVeStr);
        System.out.println("Debug - ngonNguAmThanh: " + ngonNguStr);
        System.out.println("Debug - giaVeTreEm: " + giaVeTreEmStr);
        System.out.println("Debug - vat: " + vatStr);
        System.out.println("Debug - selectedSlots: " + (selectedSlots != null ? java.util.Arrays.toString(selectedSlots) : "null"));

        if (maPhimStr == null || maPhongStr == null || ngayChieuStr == null || giaVeStr == null || ngonNguStr == null || selectedSlots == null || selectedSlots.length == 0) {
            System.out.println("Debug - Missing parameters, redirecting to create form");
            request.setAttribute("error", "Thiếu thông tin để tạo suất chiếu");
            response.sendRedirect("admin-showtimes?action=create");
            return;
        }

        try {
            int maPhim = Integer.parseInt(maPhimStr);
            int maPhong = Integer.parseInt(maPhongStr);
            LocalDate ngayChieu = LocalDate.parse(ngayChieuStr);
            BigDecimal giaVeCoSo = new BigDecimal(giaVeStr);
            BigDecimal giaVeTreEm = parseDecimalOrDefault(giaVeTreEmStr, null);
            BigDecimal vat = parseDecimalOrDefault(vatStr, null);

            int successCount = 0;
            int failCount = 0;
            StringBuilder errorMessages = new StringBuilder();

            for (String slot : selectedSlots) {
                try {
                    System.out.println("Debug - Processing slot: " + slot);
                    String[] timeParts = slot.split("-");
                    if (timeParts.length != 2) {
                        System.out.println("Debug - Invalid time format: " + slot);
                        failCount++;
                        errorMessages.append("Định dạng thời gian không hợp lệ: ").append(slot).append("\n");
                        continue;
                    }

                    LocalTime gioBatDau = LocalTime.parse(timeParts[0]);
                    LocalTime gioKetThuc = LocalTime.parse(timeParts[1]);
                    System.out.println("Debug - Parsed times: " + gioBatDau + " - " + gioKetThuc);

                    // Tạo LocalDateTime từ LocalDate và LocalTime
                    LocalDateTime ngayChieuDateTime = ngayChieu.atTime(gioBatDau);
                    LocalDateTime gioBatDauDateTime = ngayChieu.atTime(gioBatDau);
                    LocalDateTime gioKetThucDateTime = ngayChieu.atTime(gioKetThuc);

                    // Kiểm tra xung đột (sử dụng -1 để không loại trừ suất chiếu nào)
                    boolean hasConflict = showtimeDAO.hasConflictingShowtime(maPhong, ngayChieu, gioBatDau, gioKetThuc, -1);
                    System.out.println("Debug - Has conflict: " + hasConflict);

                    if (hasConflict) {
                        System.out.println("Debug - Conflict detected for slot: " + slot);
                        failCount++;
                        errorMessages.append("Suất chiếu ").append(slot).append(" bị xung đột\n");
                        continue;
                    }

                    // Tạo suất chiếu
                    Showtime showtime = new Showtime();
                    showtime.setMaPhim(maPhim);
                    showtime.setMaPhong(maPhong);
                    showtime.setNgayChieu(ngayChieuDateTime);
                    showtime.setGioBatDau(gioBatDauDateTime);
                    showtime.setGioKetThuc(gioKetThucDateTime);
                    showtime.setGiaVeCoSo(giaVeCoSo); // Giá từ form
                    showtime.setGiaVeTreEm(giaVeTreEm);
                    showtime.setVat(vat);
                    showtime.setNgonNguAmThanh(ngonNguStr != null ? ngonNguStr : "Tiếng Việt");

                    System.out.println("Debug - Creating showtime: " + showtime);
                    boolean addResult = showtimeDAO.addShowtime(showtime);
                    System.out.println("Debug - Add result: " + addResult);

                    if (addResult) {
                        // Tạo ticket cho tất cả ghế trong phòng
                        createTicketsForShowtime(showtime.getMaSuatChieu(), maPhong, giaVeCoSo);
                        successCount++;
                        System.out.println("Debug - Successfully created showtime for slot: " + slot);
                    } else {
                        failCount++;
                        errorMessages.append("Không thể tạo suất chiếu ").append(slot).append("\n");
                        System.out.println("Debug - Failed to create showtime for slot: " + slot);
                    }

                } catch (Exception e) {
                    failCount++;
                    errorMessages.append("Lỗi khi tạo suất chiếu ").append(slot).append(": ").append(e.getMessage()).append("\n");
                }
            }

            // Set thông báo kết quả
            System.out.println("Debug - Final results - Success: " + successCount + ", Fail: " + failCount);
            System.out.println("Debug - Error messages: " + errorMessages.toString());

            if (successCount > 0) {
                request.setAttribute("success", "Đã tạo thành công " + successCount + " suất chiếu");
            }
            if (failCount > 0) {
                request.setAttribute("error", "Không thể tạo " + failCount + " suất chiếu:\n" + errorMessages.toString());
            }

            System.out.println("Debug - Redirecting to admin-showtimes");
            response.sendRedirect("admin-showtimes");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Thông tin không hợp lệ");
            response.sendRedirect("admin-showtimes?action=create");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tạo suất chiếu: " + e.getMessage());
            response.sendRedirect("admin-showtimes?action=create");
        }
    }

    // Helper: parse BigDecimal with default
    private BigDecimal parseDecimalOrDefault(String value, BigDecimal defaultVal) {
        try {
            if (value == null || value.trim().isEmpty()) return defaultVal;
            return new BigDecimal(value.trim());
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
