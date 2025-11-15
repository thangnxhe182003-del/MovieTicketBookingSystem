package controller;

import dal.CinemaDAO;
import dal.RoomDAO;
import dal.SeatDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Cinema;
import model.Room;
import model.Seat;
import util.RoleChecker;

public class AdminRoomServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chỉ Admin mới được truy cập
        if (!RoleChecker.requireAdmin(request, response)) return;
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
        // Chỉ Admin mới được truy cập
        if (!RoleChecker.requireAdmin(request, response)) return;
        
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("admin-rooms");
            return;
        }

        RoomDAO roomDAO = new RoomDAO();
        SeatDAO seatDAO = new SeatDAO();

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
    }

    private void createRoom(HttpServletRequest request, HttpServletResponse response, RoomDAO roomDAO)
            throws ServletException, IOException {
        String maRapStr = request.getParameter("maRap");
        String tenPhong = request.getParameter("tenPhong");
        String soLuongGheStr = request.getParameter("soLuongGhe");

        if (maRapStr == null || tenPhong == null || soLuongGheStr == null) {
            response.sendRedirect("admin-rooms?action=create");
            return;
        }
        try {
            int maRap = Integer.parseInt(maRapStr);
            int soLuongGhe = Integer.parseInt(soLuongGheStr);
            Room room = new Room();
            room.setMaRap(maRap);
            room.setTenPhong(tenPhong);
            room.setSoLuongGhe(soLuongGhe);
            roomDAO.addRoom(room);
            response.sendRedirect("admin-rooms");
        } catch (NumberFormatException e) {
            response.sendRedirect("admin-rooms?action=create");
        }
    }

    private void updateRoom(HttpServletRequest request, HttpServletResponse response, RoomDAO roomDAO)
            throws ServletException, IOException {
        String maPhongStr = request.getParameter("maPhong");
        String maRapStr = request.getParameter("maRap");
        String tenPhong = request.getParameter("tenPhong");
        String soLuongGheStr = request.getParameter("soLuongGhe");

        if (maPhongStr == null || maRapStr == null || tenPhong == null || soLuongGheStr == null) {
            response.sendRedirect("admin-rooms");
            return;
        }
        try {
            int maPhong = Integer.parseInt(maPhongStr);
            int maRap = Integer.parseInt(maRapStr);
            int soLuongGhe = Integer.parseInt(soLuongGheStr);
            Room room = new Room();
            room.setMaPhong(maPhong);
            room.setMaRap(maRap);
            room.setTenPhong(tenPhong);
            room.setSoLuongGhe(soLuongGhe);
            roomDAO.updateRoom(room);
            response.sendRedirect("admin-rooms");
        } catch (NumberFormatException e) {
            response.sendRedirect("admin-rooms");
        }
    }

    private void deleteRoom(HttpServletRequest request, HttpServletResponse response, RoomDAO roomDAO)
            throws ServletException, IOException {
        String maPhongStr = request.getParameter("maPhong");
        if (maPhongStr == null) {
            response.sendRedirect("admin-rooms");
            return;
        }
        try {
            int maPhong = Integer.parseInt(maPhongStr);
            roomDAO.deleteRoom(maPhong);
            response.sendRedirect("admin-rooms");
        } catch (NumberFormatException e) {
            response.sendRedirect("admin-rooms");
        }
    }

    private void addSeat(HttpServletRequest request, HttpServletResponse response, SeatDAO seatDAO)
            throws ServletException, IOException {
        String maPhongStr = request.getParameter("maPhong");
        String hangGhe = request.getParameter("hangGhe");
        String soGheStr = request.getParameter("soGhe");
        String loaiGhe = request.getParameter("loaiGhe");
        String ghiChu = request.getParameter("ghiChu");
        String trangThai = request.getParameter("trangThai");
        if (trangThai == null || trangThai.isBlank()) trangThai = "Có sẵn";
        if (loaiGhe == null || loaiGhe.isBlank()) loaiGhe = "Thuong";

        try {
            int maPhong = Integer.parseInt(maPhongStr);
            int soGhe = Integer.parseInt(soGheStr);
            Seat seat = new Seat();
            seat.setMaPhong(maPhong);
            seat.setHangGhe(hangGhe);
            seat.setSoGhe(soGhe);
            seat.setLoaiGhe(loaiGhe);
            seat.setGhiChu(ghiChu);
            seat.setTrangThai(trangThai);
            seatDAO.addSeat(seat);
            response.sendRedirect("admin-rooms?action=seats&maPhong=" + maPhong);
        } catch (NumberFormatException e) {
            response.sendRedirect("admin-rooms");
        }
    }

    private void updateSeat(HttpServletRequest request, HttpServletResponse response, SeatDAO seatDAO)
            throws ServletException, IOException {
        String maGheStr = request.getParameter("maGhe");
        String maPhongStr = request.getParameter("maPhong");
        String hangGhe = request.getParameter("hangGhe");
        String soGheStr = request.getParameter("soGhe");
        String loaiGhe = request.getParameter("loaiGhe");
        String ghiChu = request.getParameter("ghiChu");
        String trangThai = request.getParameter("trangThai");

        try {
            int maGhe = Integer.parseInt(maGheStr);
            int maPhong = Integer.parseInt(maPhongStr);
            int soGhe = Integer.parseInt(soGheStr);
            Seat seat = new Seat();
            seat.setMaGhe(maGhe);
            seat.setMaPhong(maPhong);
            seat.setHangGhe(hangGhe);
            seat.setSoGhe(soGhe);
            seat.setLoaiGhe(loaiGhe);
            seat.setGhiChu(ghiChu);
            seat.setTrangThai(trangThai);
            seatDAO.updateSeat(seat);
            response.sendRedirect("admin-rooms?action=seats&maPhong=" + maPhong);
        } catch (NumberFormatException e) {
            response.sendRedirect("admin-rooms");
        }
    }

    private void deleteSeat(HttpServletRequest request, HttpServletResponse response, SeatDAO seatDAO)
            throws ServletException, IOException {
        String maGheStr = request.getParameter("maGhe");
        String maPhongStr = request.getParameter("maPhong");
        try {
            int maGhe = Integer.parseInt(maGheStr);
            int maPhong = Integer.parseInt(maPhongStr);
            seatDAO.deleteSeat(maGhe);
            response.sendRedirect("admin-rooms?action=seats&maPhong=" + maPhong);
        } catch (NumberFormatException e) {
            response.sendRedirect("admin-rooms");
        }
    }

    private void bulkGenerateSeats(HttpServletRequest request, HttpServletResponse response, SeatDAO seatDAO)
            throws ServletException, IOException {
        String maPhongStr = request.getParameter("maPhong");
        String fromRow = request.getParameter("fromRow"); // e.g. A
        String toRow = request.getParameter("toRow");     // e.g. K
        String fromNumStr = request.getParameter("fromNum"); // e.g. 1
        String toNumStr = request.getParameter("toNum");     // e.g. 12
        String loaiGhe = request.getParameter("loaiGhe");
        if (loaiGhe == null || loaiGhe.isBlank()) loaiGhe = "Thuong";

        try {
            int maPhong = Integer.parseInt(maPhongStr);
            int fromNum = Integer.parseInt(fromNumStr);
            int toNum = Integer.parseInt(toNumStr);

            char start = fromRow.toUpperCase().charAt(0);
            char end = toRow.toUpperCase().charAt(0);
            if (start > end) {
                char tmp = start; start = end; end = tmp;
            }

            // Lấy danh sách ghế hiện có để kiểm tra
            List<Seat> existingSeats = seatDAO.getSeatsByRoom(maPhong);
            java.util.Set<String> existingKeys = new java.util.HashSet<>();
            for (Seat existing : existingSeats) {
                // Sử dụng key bao gồm MaPhong, HangGhe và SoGhe để tránh trùng lặp chính xác
                existingKeys.add(maPhong + "-" + existing.getHangGhe() + "-" + existing.getSoGhe());
            }
            
            System.out.println("Debug - Số ghế hiện có: " + existingSeats.size());
            System.out.println("Debug - Các ghế hiện có: " + existingKeys);

            List<Seat> toInsert = new ArrayList<>();
            int createdCount = 0;
            int skippedCount = 0;
            
            System.out.println("Debug - Bắt đầu tạo ghế từ hàng " + start + " đến " + end + ", số " + fromNum + " đến " + toNum);
            
            for (char row = start; row <= end; row++) {
                for (int num = fromNum; num <= toNum; num++) {
                    String key = maPhong + "-" + String.valueOf(row) + "-" + num;
                    System.out.println("Debug - Kiểm tra ghế: " + key);
                    
                    // Chỉ tạo ghế nếu chưa tồn tại trong phòng này
                    if (!existingKeys.contains(key)) {
                        Seat s = new Seat();
                        s.setMaPhong(maPhong);
                        s.setHangGhe(String.valueOf(row));
                        s.setSoGhe(num);
                        s.setLoaiGhe(loaiGhe);
                        s.setTrangThai("Có sẵn");
                        toInsert.add(s);
                        existingKeys.add(key); // Đánh dấu đã tạo để tránh trùng trong lần chạy tiếp theo
                        createdCount++;
                        System.out.println("Debug - Thêm ghế vào list: " + key);
                    } else {
                        skippedCount++;
                        System.out.println("Debug - Bỏ qua ghế đã tồn tại: " + key);
                    }
                }
            }
            
            System.out.println("Debug - Tổng số ghế tạo được: " + createdCount);
            System.out.println("Debug - Tổng số ghế bỏ qua: " + skippedCount);
            
            // Insert các ghế mới với transaction để đảm bảo hoặc rollback toàn bộ nếu lỗi
            int insertedCount = 0;
            int failedCount = 0;
            
            for (Seat s : toInsert) {
                try {
                    boolean success = seatDAO.addSeat(s);
                    if (success) {
                        insertedCount++;
                        System.out.println("Debug - Đã insert ghế: " + s.getHangGhe() + s.getSoGhe());
                    } else {
                        failedCount++;
                        System.err.println("Không thể insert ghế: " + s.getHangGhe() + s.getSoGhe());
                    }
                } catch (Exception e) {
                    failedCount++;
                    System.err.println("Lỗi khi tạo ghế " + s.getHangGhe() + s.getSoGhe() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            System.out.println("Debug - Tổng số ghế đã insert thành công: " + insertedCount);
            System.out.println("Debug - Tổng số ghế insert thất bại: " + failedCount);

            // Thêm thông báo kết quả chi tiết
            String message;
            if (failedCount > 0) {
                message = String.format("Đã tạo %d ghế mới thành công. %d ghế đã tồn tại. %d ghế thất bại do trùng lặp.", 
                    insertedCount, skippedCount, failedCount);
            } else if (createdCount > 0 && skippedCount > 0) {
                message = String.format("Đã tạo %d ghế mới thành công. %d ghế đã tồn tại và được bỏ qua.", 
                    insertedCount, skippedCount);
            } else if (createdCount > 0) {
                message = String.format("Đã tạo thành công %d ghế mới.", insertedCount);
            } else if (skippedCount > 0) {
                message = String.format("Tất cả %d ghế đã tồn tại và được bỏ qua.", skippedCount);
            } else {
                message = "Không có ghế nào được tạo.";
            }
            
            response.sendRedirect("admin-rooms?action=seats&maPhong=" + maPhong + "&message=" + 
                                java.net.URLEncoder.encode(message, "UTF-8"));
        } catch (Exception e) {
            response.sendRedirect("admin-rooms");
        }
    }
}


