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
        String sql = "SELECT * FROM dbo.Seat ORDER BY MaPhong, HangGhe, SoGhe";
        
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
        String sql = "SELECT * FROM dbo.Seat WHERE MaGhe = ?";
        
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
        String sql = "SELECT * FROM dbo.Seat WHERE MaPhong = ? ORDER BY HangGhe, SoGhe";
        
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
        String sql = "SELECT * FROM dbo.Seat WHERE MaPhong = ? AND TrangThai = 'Có sẵn' ORDER BY HangGhe, SoGhe";
        
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
        String sql = "SELECT MaGhe FROM dbo.Seat WHERE MaPhong = ? AND TrangThai != 'Có sẵn'";
        
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
        String sql = "SELECT * FROM dbo.Seat WHERE MaPhong = ? AND HangGhe = ? ORDER BY SoGhe";
        
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
        String sql = "SELECT COUNT(*) as SoLuong FROM dbo.Seat WHERE MaPhong = ? AND TrangThai = ?";
        
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
     * Get seat by row and column to check duplicate (using full code like "A01")
     */
    public Seat getSeatByFullCode(int maPhong, String fullSoGhe) {
        String sql = "SELECT * FROM dbo.Seat WHERE MaPhong = ? AND SoGhe = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            ps.setString(2, fullSoGhe.toUpperCase());
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
     * Check if seat has dependencies (e.g., tickets) - For BR-04, MSG-E11
     */
    public boolean hasDependencies(int maGhe) {
        String sql = "SELECT TOP 1 1 FROM dbo.Ticket WHERE MaGhe = ?";  // Assume Ticket table exists
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maGhe);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Thêm ghế mới
     * SoGhe được lưu dưới dạng "A1", "B2", v.v.
     */
    public boolean addSeat(Seat seat) {
        String sql = "INSERT INTO dbo.Seat (MaPhong, HangGhe, SoGhe, LoaiGhe, GhiChu, TrangThai) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, seat.getMaPhong());
            ps.setString(2, seat.getHangGhe().toUpperCase());
            
            // Lưu SoGhe dưới dạng VARCHAR "A01"
            String fullSoGhe = seat.getHangGhe().toUpperCase() + String.format("%02d", seat.getSoGhe());
            ps.setString(3, fullSoGhe);
            
            ps.setString(4, seat.getLoaiGhe());
            ps.setString(5, seat.getGhiChu());
            ps.setString(6, seat.getTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm ghế: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật trạng thái ghế
     */
    public boolean updateSeatStatus(int maGhe, String trangThai) {
        String sql = "UPDATE dbo.Seat SET TrangThai = ? WHERE MaGhe = ?";
        
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
     * Cập nhật ghế - chỉ cập nhật LoaiGhe, TrangThai, GhiChu
     * Không cho phép thay đổi MaPhong, HangGhe, SoGhe
     */
    public boolean updateSeat(Seat seat) {
        String sql = "UPDATE dbo.Seat SET LoaiGhe = ?, TrangThai = ?, GhiChu = ? WHERE MaGhe = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, seat.getLoaiGhe());
            ps.setString(2, seat.getTrangThai());
            ps.setString(3, seat.getGhiChu());
            ps.setInt(4, seat.getMaGhe());
            
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
        String sql = "DELETE FROM dbo.Seat WHERE MaGhe = ?";
        
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
        
        String soGheStr = rs.getString("SoGhe");  // "A01"
        if (soGheStr != null && soGheStr.length() >= 3) {
            seat.setHangGhe(soGheStr.substring(0, 1).toUpperCase());  // "A"
            try {
                seat.setSoGhe(Integer.parseInt(soGheStr.substring(1)));  // "01" → 1
            } catch (NumberFormatException e) {
                seat.setSoGhe(0);
            }
        } else {
            seat.setHangGhe(rs.getString("HangGhe"));  // Fallback
            seat.setSoGhe(0);
        }
        
        seat.setLoaiGhe(rs.getString("LoaiGhe"));
        seat.setGhiChu(rs.getString("GhiChu"));
        seat.setTrangThai(rs.getString("TrangThai"));
        return seat;
    }
}
