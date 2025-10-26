package dal;

import model.Ticket;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO extends DBContext {
    
    /**
     * Tạo ticket mới
     */
    public boolean addTicket(Ticket ticket) {
        String sql = "INSERT INTO dbo.Ticket (MaSuatChieu, MaGhe, LoaiGhe, GiaVe, TrangThai, ThoiGianTao, ThoiGianCapNhat, GhiChu) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ticket.getMaSuatChieu());
            ps.setInt(2, ticket.getMaGhe());
            ps.setString(3, ticket.getLoaiGhe());
            ps.setBigDecimal(4, ticket.getGiaVe());
            ps.setString(5, ticket.getTrangThai());
            ps.setTimestamp(6, Timestamp.valueOf(ticket.getThoiGianTao()));
            ps.setTimestamp(7, Timestamp.valueOf(ticket.getThoiGianCapNhat()));
            ps.setString(8, ticket.getGhiChu());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Tạo ticket hàng loạt cho một suất chiếu
     */
    public boolean bulkCreateTicketsForShowtime(int maSuatChieu, List<Ticket> tickets) {
        String sql = "INSERT INTO dbo.Ticket (MaSuatChieu, MaGhe, LoaiGhe, GiaVe, TrangThai, ThoiGianTao, ThoiGianCapNhat, GhiChu) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Ticket ticket : tickets) {
                ps.setInt(1, ticket.getMaSuatChieu());
                ps.setInt(2, ticket.getMaGhe());
                ps.setString(3, ticket.getLoaiGhe());
                ps.setBigDecimal(4, ticket.getGiaVe());
                ps.setString(5, ticket.getTrangThai());
                ps.setTimestamp(6, Timestamp.valueOf(ticket.getThoiGianTao()));
                ps.setTimestamp(7, Timestamp.valueOf(ticket.getThoiGianCapNhat()));
                ps.setString(8, ticket.getGhiChu());
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Lấy danh sách ticket theo suất chiếu
     */
    public List<Ticket> getTicketsByShowtime(int maSuatChieu) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Ticket WHERE MaSuatChieu = ? ORDER BY MaGhe";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRowToTicket(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
    
    /**
     * Lấy danh sách ticket đã đặt theo suất chiếu
     */
    public List<Ticket> getBookedTicketsByShowtime(int maSuatChieu) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Ticket WHERE MaSuatChieu = ? AND TrangThai IN ('Đã đặt', 'Hold') ORDER BY MaGhe";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRowToTicket(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
    
    /**
     * Cập nhật trạng thái ticket
     */
    public boolean updateTicketStatus(int maVe, String trangThai) {
        String sql = "UPDATE dbo.Ticket SET TrangThai = ?, ThoiGianCapNhat = ? WHERE MaVe = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, maVe);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa tất cả ticket của một suất chiếu
     */
    public boolean deleteTicketsByShowtime(int maSuatChieu) {
        String sql = "DELETE FROM dbo.Ticket WHERE MaSuatChieu = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Kiểm tra xem ticket đã tồn tại chưa
     */
    public boolean ticketExists(int maSuatChieu, int maGhe) {
        String sql = "SELECT COUNT(*) FROM dbo.Ticket WHERE MaSuatChieu = ? AND MaGhe = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            ps.setInt(2, maGhe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Map ResultSet to Ticket object
     */
    private Ticket mapRowToTicket(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setMaVe(rs.getInt("MaVe"));
        ticket.setMaSuatChieu(rs.getInt("MaSuatChieu"));
        ticket.setMaGhe(rs.getInt("MaGhe"));
        ticket.setLoaiGhe(rs.getString("LoaiGhe"));
        ticket.setGiaVe(rs.getBigDecimal("GiaVe"));
        ticket.setTrangThai(rs.getString("TrangThai"));
        
        Timestamp thoiGianTao = rs.getTimestamp("ThoiGianTao");
        if (thoiGianTao != null) {
            ticket.setThoiGianTao(thoiGianTao.toLocalDateTime());
        }
        
        Timestamp thoiGianCapNhat = rs.getTimestamp("ThoiGianCapNhat");
        if (thoiGianCapNhat != null) {
            ticket.setThoiGianCapNhat(thoiGianCapNhat.toLocalDateTime());
        }
        
        ticket.setGhiChu(rs.getString("GhiChu"));
        return ticket;
    }
    
    /**
     * Kiểm tra xem có ticket nào đã được đặt cho suất chiếu không
     * @param maSuatChieu Mã suất chiếu
     * @return true nếu có ticket đã đặt, false nếu không có
     */
    public boolean hasBookedTickets(int maSuatChieu) {
        String sql = "SELECT COUNT(*) FROM dbo.Ticket WHERE MaSuatChieu = ? AND TrangThai = 'Đã đặt'";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
