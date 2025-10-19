package dal;

import model.Seat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO extends DBContext {

    /**
     * Lấy tất cả ghế
     */
    public List<Seat> getAllSeats() {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Ghe ORDER BY MaPhong, HangGhe, SoGhe";
        
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                seats.add(mapRowToSeat(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    /**
     * Lấy ghế theo ID
     */
    public Seat getSeatById(int maGhe) {
        String sql = "SELECT * FROM dbo.Ghe WHERE MaGhe = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maGhe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToSeat(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy tất cả ghế trong phòng
     */
    public List<Seat> getSeatsByRoom(int maPhong) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Ghe WHERE MaPhong = ? ORDER BY HangGhe, SoGhe";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapRowToSeat(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    /**
     * Lấy ghế trống trong phòng
     */
    public List<Seat> getAvailableSeatsByRoom(int maPhong) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Ghe WHERE MaPhong = ? AND TrangThai = 'Có sẵn' ORDER BY HangGhe, SoGhe";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapRowToSeat(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    /**
     * Lấy ghế trống trong phòng (chỉ lấy mã ghế)
     */
    public List<Integer> getBookedSeatIdsByRoom(int maPhong) {
        List<Integer> bookedSeats = new ArrayList<>();
        String sql = "SELECT MaGhe FROM dbo.Ghe WHERE MaPhong = ? AND TrangThai != 'Có sẵn'";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookedSeats.add(rs.getInt("MaGhe"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookedSeats;
    }

    /**
     * Lấy ghế theo hàng
     */
    public List<Seat> getSeatsByRoomAndRow(int maPhong, String hangGhe) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Ghe WHERE MaPhong = ? AND HangGhe = ? ORDER BY SoGhe";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            ps.setString(2, hangGhe);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapRowToSeat(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    /**
     * Lấy số lượng ghế theo trạng thái
     */
    public int countSeatsByStatus(int maPhong, String trangThai) {
        String sql = "SELECT COUNT(*) as SoLuong FROM dbo.Ghe WHERE MaPhong = ? AND TrangThai = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            ps.setString(2, trangThai);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("SoLuong");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Thêm ghế mới
     */
    public boolean addSeat(Seat seat) {
        String sql = "INSERT INTO dbo.Ghe (MaPhong, HangGhe, SoGhe, LoaiGhe, GhiChu, TrangThai) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, seat.getMaPhong());
            ps.setString(2, seat.getHangGhe());
            ps.setInt(3, seat.getSoGhe());
            ps.setString(4, seat.getLoaiGhe());
            ps.setString(5, seat.getGhiChu());
            ps.setString(6, seat.getTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật trạng thái ghế
     */
    public boolean updateSeatStatus(int maGhe, String trangThai) {
        String sql = "UPDATE dbo.Ghe SET TrangThai = ? WHERE MaGhe = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, maGhe);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật ghế
     */
    public boolean updateSeat(Seat seat) {
        String sql = "UPDATE dbo.Ghe SET MaPhong = ?, HangGhe = ?, SoGhe = ?, LoaiGhe = ?, GhiChu = ?, TrangThai = ? " +
                    "WHERE MaGhe = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, seat.getMaPhong());
            ps.setString(2, seat.getHangGhe());
            ps.setInt(3, seat.getSoGhe());
            ps.setString(4, seat.getLoaiGhe());
            ps.setString(5, seat.getGhiChu());
            ps.setString(6, seat.getTrangThai());
            ps.setInt(7, seat.getMaGhe());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa ghế
     */
    public boolean deleteSeat(int maGhe) {
        String sql = "DELETE FROM dbo.Ghe WHERE MaGhe = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maGhe);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map ResultSet row to Seat object
     */
    private Seat mapRowToSeat(ResultSet rs) throws SQLException {
        Seat seat = new Seat();
        seat.setMaGhe(rs.getInt("MaGhe"));
        seat.setMaPhong(rs.getInt("MaPhong"));
        seat.setHangGhe(rs.getString("HangGhe"));
        seat.setSoGhe(rs.getInt("SoGhe"));
        seat.setLoaiGhe(rs.getString("LoaiGhe"));
        seat.setGhiChu(rs.getString("GhiChu"));
        seat.setTrangThai(rs.getString("TrangThai"));
        return seat;
    }
}
