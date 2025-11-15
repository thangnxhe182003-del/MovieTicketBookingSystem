package dal;

import model.Order;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO extends DBContext {
    
    /**
     * Tạo order mới
     */
    public int createOrder(Order order) {
        String sql = "INSERT INTO dbo.[Order] (MaKH, OrderCode, KenhDat, TrangThai, TongTien, GhiChu, CreatedAt, PaidAt, MaGiamGia) " +
                     "OUTPUT INSERTED.MaOrder VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, order.getMaKH());
            ps.setString(2, order.getOrderCode());
            ps.setString(3, order.getKenhDat());
            ps.setString(4, order.getTrangThai());
            ps.setBigDecimal(5, order.getTongTien());
            ps.setString(6, order.getGhiChu());
            ps.setTimestamp(7, java.sql.Timestamp.valueOf(order.getCreatedAt()));
            
            if (order.getPaidAt() != null) {
                ps.setTimestamp(8, java.sql.Timestamp.valueOf(order.getPaidAt()));
            } else {
                ps.setNull(8, java.sql.Types.TIMESTAMP);
            }
            
            if (order.getMaGiamGia() != null) {
                ps.setInt(9, order.getMaGiamGia());
            } else {
                ps.setNull(9, java.sql.Types.INTEGER);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("MaOrder");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Generate order code: OR20251028-00001
     */
    public String generateOrderCode() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String datePrefix = LocalDateTime.now().format(dateFormatter);
        
        String sql = "SELECT TOP 1 OrderCode FROM dbo.[Order] WHERE OrderCode LIKE ? ORDER BY OrderCode DESC";
        String pattern = "OR" + datePrefix + "-%";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String lastCode = rs.getString("OrderCode");
                    int lastNum = Integer.parseInt(lastCode.substring(11));
                    return "OR" + datePrefix + "-" + String.format("%05d", lastNum + 1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "OR" + datePrefix + "-00001";
    }
    
    public Order getOrderByCode(String orderCode) {
        String sql = "SELECT * FROM dbo.[Order] WHERE OrderCode = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, orderCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order o = new Order();
                    o.setMaOrder(rs.getInt("MaOrder"));
                    o.setMaKH(rs.getInt("MaKH"));
                    o.setOrderCode(rs.getString("OrderCode"));
                    o.setKenhDat(rs.getString("KenhDat"));
                    o.setTrangThai(rs.getString("TrangThai"));
                    o.setTongTien(rs.getBigDecimal("TongTien"));
                    java.sql.Timestamp paidAt = rs.getTimestamp("PaidAt");
                    if (paidAt != null) o.setPaidAt(paidAt.toLocalDateTime());
                    java.sql.Timestamp createdAt = rs.getTimestamp("CreatedAt");
                    if (createdAt != null) o.setCreatedAt(createdAt.toLocalDateTime());
                    int maGiamGia = rs.getInt("MaGiamGia");
                    if (!rs.wasNull()) o.setMaGiamGia(maGiamGia);
                    return o;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateOrderStatus(int maOrder, String trangThai) {
        String sql = "UPDATE dbo.[Order] SET TrangThai = ? WHERE MaOrder = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, maOrder);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean markOrderAsPaid(int maOrder) {
        String sql = "UPDATE dbo.[Order] SET TrangThai = N'Đã thanh toán', PaidAt = SYSUTCDATETIME() WHERE MaOrder = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maOrder);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> getPaidOrdersByCustomer(int maKH) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM dbo.[Order] WHERE MaKH = ? AND (TrangThai = N'Đã thanh toán' OR TrangThai = 'Thanh toan tien mat') ORDER BY CreatedAt DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = new Order();
                    o.setMaOrder(rs.getInt("MaOrder"));
                    o.setMaKH(rs.getInt("MaKH"));
                    o.setOrderCode(rs.getString("OrderCode"));
                    o.setKenhDat(rs.getString("KenhDat"));
                    o.setTrangThai(rs.getString("TrangThai"));
                    o.setTongTien(rs.getBigDecimal("TongTien"));
                    java.sql.Timestamp createdAt = rs.getTimestamp("CreatedAt");
                    if (createdAt != null) o.setCreatedAt(createdAt.toLocalDateTime());
                    java.sql.Timestamp paidAt = rs.getTimestamp("PaidAt");
                    if (paidAt != null) o.setPaidAt(paidAt.toLocalDateTime());
                    orders.add(o);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public Order getOrderById(int maOrder) {
        String sql = "SELECT * FROM dbo.[Order] WHERE MaOrder = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maOrder);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order o = new Order();
                    o.setMaOrder(rs.getInt("MaOrder"));
                    o.setMaKH(rs.getInt("MaKH"));
                    o.setOrderCode(rs.getString("OrderCode"));
                    o.setKenhDat(rs.getString("KenhDat"));
                    o.setTrangThai(rs.getString("TrangThai"));
                    o.setTongTien(rs.getBigDecimal("TongTien"));
                    java.sql.Timestamp createdAt = rs.getTimestamp("CreatedAt");
                    if (createdAt != null) o.setCreatedAt(createdAt.toLocalDateTime());
                    java.sql.Timestamp paidAt = rs.getTimestamp("PaidAt");
                    if (paidAt != null) o.setPaidAt(paidAt.toLocalDateTime());
                    int maGiamGia = rs.getInt("MaGiamGia");
                    if (!rs.wasNull()) o.setMaGiamGia(maGiamGia);
                    return o;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

