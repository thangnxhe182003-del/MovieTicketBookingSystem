package dal;

import model.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO extends DBContext {

    /**
     * Lấy tất cả phòng chiếu
     */
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.*, c.TenRap FROM dbo.Room r " +
                    "JOIN dbo.Cinema c ON r.MaRap = c.MaRap";
        
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(mapRowToRoom(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Lấy phòng chiếu theo ID
     */
    public Room getRoomById(int maPhong) {
        String sql = "SELECT r.*, c.TenRap FROM dbo.Room r " +
                    "JOIN dbo.Cinema c ON r.MaRap = c.MaRap " +
                    "WHERE r.MaPhong = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToRoom(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy các phòng của rạp theo ID rạp
     */
    public List<Room> getRoomsByCinema(int maRap) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Room WHERE MaRap = ? ORDER BY TenPhong";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maRap);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRowToRoom(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Thêm phòng chiếu mới
     */
    public boolean addRoom(Room room) {
        String sql = "INSERT INTO dbo.Room (MaRap, TenPhong, SoLuongGhe) VALUES (?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, room.getMaRap());
            ps.setString(2, room.getTenPhong());
            ps.setInt(3, room.getSoLuongGhe());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật phòng chiếu
     */
    public boolean updateRoom(Room room) {
        String sql = "UPDATE dbo.Room SET MaRap = ?, TenPhong = ?, SoLuongGhe = ? WHERE MaPhong = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, room.getMaRap());
            ps.setString(2, room.getTenPhong());
            ps.setInt(3, room.getSoLuongGhe());
            ps.setInt(4, room.getMaPhong());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa phòng chiếu
     */
    public boolean deleteRoom(int maPhong) {
        String sql = "DELETE FROM dbo.Room WHERE MaPhong = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map ResultSet row to Room object
     */
    private Room mapRowToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setMaPhong(rs.getInt("MaPhong"));
        room.setMaRap(rs.getInt("MaRap"));
        room.setTenPhong(rs.getString("TenPhong"));
        room.setSoLuongGhe(rs.getInt("SoLuongGhe"));
        room.setTenRap(rs.getString("TenRap"));
        return room;
    }
}
