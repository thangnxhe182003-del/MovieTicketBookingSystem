 package controller;

import dal.CinemaDAO;
import dal.RoomDAO;
import dal.SeatDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cinema;
import model.Room;
import model.Seat;
import java.util.HashSet;
import java.util.Set;

public class AdminRoomServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        RoomDAO roomDAO = new RoomDAO();
        CinemaDAO cinemaDAO = new CinemaDAO();
        SeatDAO seatDAO = new SeatDAO();

        switch (action) {
            case "list":
                List<Room> rooms = roomDAO.getAllRooms();
                request.setAttribute("rooms", rooms);
                request.getRequestDispatcher("Views/admin/room-list.jsp").forward(request, response);
                break;
            case "create":
                request.setAttribute("cinemas", cinemaDAO.getAllCinemas());
                request.getRequestDispatcher("Views/admin/room-form.jsp").forward(request, response);
                break;
            case "edit":
                try {
                    int maPhong = Integer.parseInt(request.getParameter("maPhong"));
                    Room room = roomDAO.getRoomById(maPhong);
                    if (room == null) {
                        response.sendRedirect("admin-rooms");
                        return;
                    }
                    request.setAttribute("room", room);
                    request.setAttribute("cinemas", cinemaDAO.getAllCinemas());
                    request.getRequestDispatcher("Views/admin/room-form.jsp").forward(request, response);
                } catch (NumberFormatException e) {
                    response.sendRedirect("admin-rooms");
                }
                break;
            case "seats":
                try {
                    int maPhong = Integer.parseInt(request.getParameter("maPhong"));
                    Room room = roomDAO.getRoomById(maPhong);
                    if (room == null) {
                        response.sendRedirect("admin-rooms");
                        return;
                    }
                    request.setAttribute("room", room);
                    request.setAttribute("seats", seatDAO.getSeatsByRoom(maPhong));
                    request.getRequestDispatcher("Views/admin/seat-list.jsp").forward(request, response);
                } catch (NumberFormatException e) {
                    response.sendRedirect("admin-rooms");
                }
                break;
            default:
                response.sendRedirect("admin-rooms");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("admin-rooms");
            return;
        }

        RoomDAO roomDAO = new RoomDAO();
        SeatDAO seatDAO = new SeatDAO();

        try {
            switch (action) {
                case "create":
                    createRoom(request, response, roomDAO);
                    break;
                case "update":
                    updateRoom(request, response, roomDAO);
                    break;
                case "delete":
                    deleteRoom(request, response, roomDAO);
                    break;
                case "addSeat":
                    addSeat(request, response, seatDAO);
                    break;
                case "updateSeat":
                    updateSeat(request, response, seatDAO);
                    break;
                case "deleteSeat":
                    deleteSeat(request, response, seatDAO);
                    break;
                case "bulkGenerate":
                    bulkGenerateSeats(request, response, seatDAO);
                    break;
                default:
                    response.sendRedirect("admin-rooms");
            }
        } catch (SQLException e) {
            // MSG-E15: Connection error
            String maPhong = request.getParameter("maPhong");
            String redirectUrl = "admin-rooms?action=seats&maPhong=" + maPhong + "&message=System%20connection%20error.%20Please%20try%20again%20later.&type=error";
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            // General error (MSG-E01/E03)
            response.sendRedirect("admin-rooms?message=An%20error%20occurred.&type=error");
        }
    }

    private void createRoom(HttpServletRequest request, HttpServletResponse response, RoomDAO roomDAO)
            throws IOException {
        int maRap = Integer.parseInt(request.getParameter("maRap"));
        String tenPhong = request.getParameter("tenPhong");
        int soLuongGhe = Integer.parseInt(request.getParameter("soLuongGhe"));

        Room room = new Room();
        room.setMaRap(maRap);
        room.setTenPhong(tenPhong);
        room.setSoLuongGhe(soLuongGhe);

        boolean success = roomDAO.addRoom(room);
        String message = success ? "Room+added+successfully.+(MSG-S01)" : "Failed+to+add+room.+(MSG-E01)";
        String type = success ? "success" : "error";
        response.sendRedirect("admin-rooms?message=" + message + "&type=" + type);
    }

    private void updateRoom(HttpServletRequest request, HttpServletResponse response, RoomDAO roomDAO)
            throws IOException {
        int maPhong = Integer.parseInt(request.getParameter("maPhong"));
        int maRap = Integer.parseInt(request.getParameter("maRap"));
        String tenPhong = request.getParameter("tenPhong");
        int soLuongGhe = Integer.parseInt(request.getParameter("soLuongGhe"));

        Room room = new Room();
        room.setMaPhong(maPhong);
        room.setMaRap(maRap);
        room.setTenPhong(tenPhong);
        room.setSoLuongGhe(soLuongGhe);

        boolean success = roomDAO.updateRoom(room);
        String message = success ? "Room+updated+successfully.+(MSG-S01)" : "Failed+to+update+room.+(MSG-E01)";
        String type = success ? "success" : "error";
        response.sendRedirect("admin-rooms?message=" + message + "&type=" + type);
    }

    private void deleteRoom(HttpServletRequest request, HttpServletResponse response, RoomDAO roomDAO)
            throws IOException, SQLException {
        int maPhong = Integer.parseInt(request.getParameter("maPhong"));

        // Check dependency (BR-04, MSG-E11)
        if (roomDAO.hasDependencies(maPhong)) {
            String message = "Cannot+delete+room.+It+has+dependent+seats+or+showtimes.+(MSG-E11)";
            response.sendRedirect("admin-rooms?message=" + message + "&type=error");
            return;
        }

        boolean success = roomDAO.deleteRoom(maPhong);
        String message = success ? "Room+deleted+successfully.+(MSG-S01)" : "Failed+to+delete+room.+(MSG-E01)";
        String type = success ? "success" : "error";
        response.sendRedirect("admin-rooms?message=" + message + "&type=" + type);
    }

    private void addSeat(HttpServletRequest request, HttpServletResponse response, SeatDAO seatDAO)
            throws IOException, SQLException {
        String maPhongStr = request.getParameter("maPhong");
        String hangGhe = request.getParameter("hangGhe").toUpperCase();
        String soGheStr = request.getParameter("soGhe");
        String loaiGhe = request.getParameter("loaiGhe");
        String trangThai = request.getParameter("trangThai");
        String ghiChu = request.getParameter("ghiChu");

        int maPhong = Integer.parseInt(maPhongStr);
        int soGhe = Integer.parseInt(soGheStr);

        // Create fullSoGhe "A01" for check and message
        String fullSoGhe = hangGhe + String.format("%02d", soGhe);

        // Check duplicate (BR-01, MSG-E05)
        if (seatDAO.getSeatByFullCode(maPhong, fullSoGhe) != null) {
            String message = java.net.URLEncoder.encode("Seat " + fullSoGhe + " already exists in this room. Please enter a unique seat number. (MSG-E05)", "UTF-8");
            response.sendRedirect("admin-rooms?action=seats&maPhong=" + maPhong + "&message=" + message + "&type=error");
            return;
        }

        Seat seat = new Seat();
        seat.setMaPhong(maPhong);
        seat.setHangGhe(hangGhe);
        seat.setSoGhe(soGhe);
        seat.setLoaiGhe(loaiGhe != null ? loaiGhe : "Thuong");
        seat.setTrangThai(trangThai != null ? trangThai : "Có sẵn");
        seat.setGhiChu(ghiChu);

        // Validate data (BR-18)
        if (hangGhe.length() != 1 || !Character.isLetter(hangGhe.charAt(0)) || soGhe < 1) {
            String message = java.net.URLEncoder.encode("Invalid seat data. (MSG-E03)", "UTF-8");
            response.sendRedirect("admin-rooms?action=seats&maPhong=" + maPhong + "&message=" + message + "&type=error");
            return;
        }

        boolean success = seatDAO.addSeat(seat);
        String message = success ? "Seat " + fullSoGhe + " added successfully. (MSG-S01)" : "Failed to add seat. (MSG-E01)";
        String type = success ? "success" : "error";
        response.sendRedirect("admin-rooms?action=seats&maPhong=" + maPhong + "&message=" + java.net.URLEncoder.encode(message, "UTF-8") + "&type=" + type);
    }

    private void updateSeat(HttpServletRequest request, HttpServletResponse response, SeatDAO seatDAO)
            throws IOException, SQLException {
        // Parse params (maGhe, loaiGhe, trangThai, ghiChu)
        int maGhe = Integer.parseInt(request.getParameter("maGhe"));
        String loaiGhe = request.getParameter("loaiGhe");
        String trangThai = request.getParameter("trangThai");
        String ghiChu = request.getParameter("ghiChu");
        int maPhong = Integer.parseInt(request.getParameter("maPhong"));

        Seat seat = seatDAO.getSeatById(maGhe);
        if (seat == null) {
            response.sendRedirect("admin-rooms?action=seats&maPhong=" + maPhong + "&message=Seat%20not%20found.&type=error");
            return;
        }

        seat.setLoaiGhe(loaiGhe);
        seat.setTrangThai(trangThai);
        seat.setGhiChu(ghiChu);

        boolean success = seatDAO.updateSeat(seat);
        String message = success ? "Seat updated successfully. (MSG-S01)" : "Failed to update seat. (MSG-E01)";
        String type = success ? "success" : "error";
        response.sendRedirect("admin-rooms?action=seats&maPhong=" + maPhong + "&message=" + java.net.URLEncoder.encode(message, "UTF-8") + "&type=" + type);
    }

    private void deleteSeat(HttpServletRequest request, HttpServletResponse response, SeatDAO seatDAO)
            throws IOException, SQLException {
        int maGhe = Integer.parseInt(request.getParameter("maGhe"));
        int maPhong = Integer.parseInt(request.getParameter("maPhong"));

        // Check dependency (BR-04, MSG-E11)
        if (seatDAO.hasDependencies(maGhe)) {
            String message = java.net.URLEncoder.encode("Cannot delete. Dependent showtimes or rooms exist. (MSG-E11)", "UTF-8");
            response.sendRedirect("admin-rooms?action=seats&maPhong=" + maPhong + "&message=" + message + "&type=error");
            return;
        }

        boolean success = seatDAO.deleteSeat(maGhe);
        String message = success ? "Seat deleted successfully. (MSG-S01)" : "Failed to delete seat. (MSG-E01)";
        String type = success ? "success" : "error";
        response.sendRedirect("admin-rooms?action=seats&maPhong=" + maPhong + "&message=" + java.net.URLEncoder.encode(message, "UTF-8") + "&type=" + type);
    }

    private void bulkGenerateSeats(HttpServletRequest request, HttpServletResponse response, SeatDAO seatDAO)
            throws IOException, SQLException {
        String maPhongStr = request.getParameter("maPhong");
        String fromRow = request.getParameter("fromRow").toUpperCase();
        String toRow = request.getParameter("toRow").toUpperCase();
        String fromNumStr = request.getParameter("fromNum");
        String toNumStr = request.getParameter("toNum");
        String loaiGhe = request.getParameter("loaiGhe");
        if (loaiGhe == null || loaiGhe.isBlank()) loaiGhe = "Thuong";

        int maPhong = Integer.parseInt(maPhongStr);
        int fromNum = Integer.parseInt(fromNumStr);
        int toNum = Integer.parseInt(toNumStr);

        char start = fromRow.charAt(0);
        char end = toRow.charAt(0);
        if (start > end) {
            char tmp = start; start = end; end = tmp;
        }

        // Get existing to check duplicate (BR-01)
        List<Seat> existingSeats = seatDAO.getSeatsByRoom(maPhong);
        Set<String> existingKeys = new HashSet<>();
        for (Seat existing : existingSeats) {
            existingKeys.add(existing.getHangGhe() + "-" + existing.getSoGhe());
        }

        List<Seat> toInsert = new ArrayList<>();
        int createdCount = 0;
        int skippedCount = 0;

        for (char row = start; row <= end; row++) {
            for (int num = fromNum; num <= toNum; num++) {
                String key = String.valueOf(row) + "-" + num;
                if (!existingKeys.contains(key)) {
                    Seat s = new Seat();
                    s.setMaPhong(maPhong);
                    s.setHangGhe(String.valueOf(row));
                    s.setSoGhe(num);
                    s.setLoaiGhe(loaiGhe);
                    s.setTrangThai("Có sẵn");
                    toInsert.add(s);
                    existingKeys.add(key);
                    createdCount++;
                } else {
                    skippedCount++;
                }
            }
        }

        // Insert with handling
        int insertedCount = 0;
        int failedCount = 0;
        for (Seat s : toInsert) {
            if (seatDAO.addSeat(s)) {
                insertedCount++;
            } else {
                failedCount++;
            } // Log e
        }

        String message;
        String type = "success";
        if (failedCount > 0) {
            message = String.format("Created %d seats successfully. %d skipped (duplicates). %d failed. (MSG-E01)", insertedCount, skippedCount, failedCount);
            type = "error";
        } else if (createdCount > 0 && skippedCount > 0) {
            message = String.format("Created %d seats successfully. %d skipped (duplicates). (MSG-S01 / MSG-E05)", insertedCount, skippedCount);
        } else if (createdCount > 0) {
            message = String.format("Created %d seats successfully. (MSG-S01)", insertedCount);
        } else if (skippedCount > 0) {
            message = String.format("All %d seats already exist and were skipped. (MSG-E05)", skippedCount);
        } else {
            message = "No seats created.";
            type = "error";
        }

        response.sendRedirect("admin-rooms?action=seats&maPhong=" + maPhong + "&message=" + 
                              java.net.URLEncoder.encode(message, "UTF-8") + "&type=" + type);
    }
}


