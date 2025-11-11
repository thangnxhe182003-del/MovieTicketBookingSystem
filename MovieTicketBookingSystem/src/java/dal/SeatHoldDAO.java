package dal;

import model.SeatHold;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SeatHoldDAO extends DBContext {
    
    /**
     * Tạo seat hold mới (giữ chỗ trong 15 phút)
     */
    public Long createSeatHold(SeatHold seatHold) {
        String sql = "INSERT INTO dbo.SeatHold (MaSuatChieu, MaGhe, MaKH, CreatedAt, ExpiresAt, Note) " +
                     "OUTPUT INSERTED.HoldId VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, seatHold.getMaSuatChieu());
            ps.setInt(2, seatHold.getMaGhe());
            
            if (seatHold.getMaKH() != null) {
                ps.setInt(3, seatHold.getMaKH());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            
            // Map heldAt -> CreatedAt, ghiChu -> Note
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(seatHold.getHeldAt()));
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(seatHold.getExpiresAt()));
            ps.setString(6, seatHold.getGhiChu());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long holdId = rs.getLong("HoldId");
                    System.out.println("[SeatHoldDAO] CREATED holdId=" + holdId + ", maSuatChieu=" + seatHold.getMaSuatChieu() + ", maGhe=" + seatHold.getMaGhe() + ", expiresAt=" + seatHold.getExpiresAt());
                    return holdId;
                }
            }
        } catch (SQLException e) {
            System.err.println("[SeatHoldDAO] ERROR createSeatHold: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Xóa seat hold
     */
    public boolean deleteSeatHold(Long holdId) {
        String sql = "DELETE FROM dbo.SeatHold WHERE HoldId = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, holdId);
            int rows = ps.executeUpdate();
            System.out.println("[SeatHoldDAO] DELETED holdId=" + holdId + ", rows=" + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[SeatHoldDAO] ERROR deleteSeatHold: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa tất cả seat holds theo MaSuatChieu và MaKH
     */
    public boolean deleteSeatHoldsByCustomer(int maSuatChieu, Integer maKH) {
        String sql = "DELETE FROM dbo.SeatHold WHERE MaSuatChieu = ? AND MaKH = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            ps.setInt(2, maKH);
            int rows = ps.executeUpdate();
            System.out.println("[SeatHoldDAO] DELETED by customer maSuatChieu=" + maSuatChieu + ", maKH=" + maKH + ", rows=" + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[SeatHoldDAO] ERROR deleteSeatHoldsByCustomer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Lấy danh sách ghế đã hold theo suất chiếu
     */
    public List<Integer> getHeldSeatIds(int maSuatChieu) {
        List<Integer> heldSeatIds = new ArrayList<>();
        String sql = "SELECT MaGhe FROM dbo.SeatHold WHERE MaSuatChieu = ? AND ExpiresAt > SYSUTCDATETIME()";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    heldSeatIds.add(rs.getInt(1));
                }
            }
            System.out.println("[SeatHoldDAO] VALID HOLDS maSuatChieu=" + maSuatChieu + ": " + heldSeatIds);
        } catch (SQLException e) {
            System.err.println("[SeatHoldDAO] ERROR getHeldSeatIds: " + e.getMessage());
            e.printStackTrace();
        }
        return heldSeatIds;
    }
    
    /**
     * Cleanup expired holds
     */
    public int cleanupExpiredHolds() {
        String sql = "DELETE FROM dbo.SeatHold WHERE ExpiresAt < SYSUTCDATETIME()";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("[SeatHoldDAO] CLEANUP expired holds, rows=" + rows);
            return rows;
        } catch (SQLException e) {
            System.err.println("[SeatHoldDAO] ERROR cleanupExpiredHolds: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}

