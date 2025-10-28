package dal;

import model.OrderFoodDetail;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderFoodDetailDAO extends DBContext {
    
    /**
     * Thêm combo vào order
     */
    public boolean createOrderFoodDetail(OrderFoodDetail orderFoodDetail) {
        String sql = "INSERT INTO dbo.OrderFoodDetail (MaOrder, MaSP, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderFoodDetail.getMaOrder());
            ps.setInt(2, orderFoodDetail.getMaSP());
            ps.setInt(3, orderFoodDetail.getSoLuong());
            ps.setBigDecimal(4, orderFoodDetail.getDonGia());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<OrderFoodDetail> getByOrder(int maOrder) {
        List<OrderFoodDetail> items = new ArrayList<>();
        String sql = "SELECT * FROM dbo.OrderFoodDetail WHERE MaOrder = ? ORDER BY MaSP";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maOrder);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderFoodDetail it = new OrderFoodDetail();
                    it.setMaOrder(rs.getInt("MaOrder"));
                    it.setMaSP(rs.getInt("MaSP"));
                    it.setSoLuong(rs.getInt("SoLuong"));
                    it.setDonGia(rs.getBigDecimal("DonGia"));
                    // ThanhTien là computed column; tính lại hoặc bỏ qua nếu đã có
                    items.add(it);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}

