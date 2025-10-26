package dal;

import model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends DBContext {

    /**
     * Lấy danh sách tất cả sản phẩm (combo đồ ăn)
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT MaSP, TenSP, DonGia, ThumbnailUrl, TrangThai FROM dbo.Product ORDER BY TenSP";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setMaSP(rs.getInt("MaSP"));
                product.setTenSP(rs.getString("TenSP"));
                product.setDonGia(rs.getDouble("DonGia"));
                product.setThumbnailUrl(rs.getString("ThumbnailUrl"));
                product.setTrangThai(rs.getString("TrangThai"));
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Lấy thông tin sản phẩm theo ID
     */
    public Product getProductById(int maSP) {
        String sql = "SELECT MaSP, TenSP, DonGia, ThumbnailUrl, TrangThai FROM dbo.Product WHERE MaSP = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setMaSP(rs.getInt("MaSP"));
                    product.setTenSP(rs.getString("TenSP"));
                    product.setDonGia(rs.getDouble("DonGia"));
                    product.setThumbnailUrl(rs.getString("ThumbnailUrl"));
                    product.setTrangThai(rs.getString("TrangThai"));
                    return product;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tạo sản phẩm mới
     */
    public boolean createProduct(Product product) {
        String sql = "INSERT INTO dbo.Product (TenSP, DonGia, ThumbnailUrl, TrangThai) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, product.getTenSP());
            ps.setDouble(2, product.getDonGia());
            ps.setString(3, product.getThumbnailUrl());
            ps.setString(4, product.getTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật sản phẩm
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE dbo.Product SET TenSP = ?, DonGia = ?, ThumbnailUrl = ?, TrangThai = ? WHERE MaSP = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, product.getTenSP());
            ps.setDouble(2, product.getDonGia());
            ps.setString(3, product.getThumbnailUrl());
            ps.setString(4, product.getTrangThai());
            ps.setInt(5, product.getMaSP());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa sản phẩm (cập nhật trạng thái thành "Không bán")
     */
    public boolean deleteProduct(int maSP) {
        String sql = "UPDATE dbo.Product SET TrangThai = 'Không bán' WHERE MaSP = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSP);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
