package dal;

import model.Discount;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DiscountDAO extends DBContext {

    /**
     * Lấy danh sách tất cả mã giảm giá
     */
    public List<Discount> getAllDiscounts() {
        List<Discount> discounts = new ArrayList<>();
        // Dùng CONVERT để lấy datetime dưới dạng string trực tiếp, tránh timezone conversion
        String sql = "SELECT MaGiamGia, MaCode, TenGiamGia, MoTa, LoaiGiamGia, HinhThucGiam, " +
                     "GiaTriGiam, GiaTriToiDa, " +
                     "CONVERT(VARCHAR(23), NgayBatDau, 120) as NgayBatDau, " +
                     "CONVERT(VARCHAR(23), NgayKetThuc, 120) as NgayKetThuc, " +
                     "TrangThai, SoLanSuDung, DaSuDung, " +
                     "CONVERT(VARCHAR(23), CreatedAt, 120) as CreatedAt " +
                     "FROM dbo.Discount ORDER BY CreatedAt DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Discount discount = mapResultSetToDiscount(rs);
                discounts.add(discount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return discounts;
    }

    /**
     * Lấy thông tin mã giảm giá theo ID
     */
    public Discount getDiscountById(int maGiamGia) {
        String sql = "SELECT MaGiamGia, MaCode, TenGiamGia, MoTa, LoaiGiamGia, HinhThucGiam, " +
                     "GiaTriGiam, GiaTriToiDa, " +
                     "CONVERT(VARCHAR(23), NgayBatDau, 120) as NgayBatDau, " +
                     "CONVERT(VARCHAR(23), NgayKetThuc, 120) as NgayKetThuc, " +
                     "TrangThai, SoLanSuDung, DaSuDung, " +
                     "CONVERT(VARCHAR(23), CreatedAt, 120) as CreatedAt " +
                     "FROM dbo.Discount WHERE MaGiamGia = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maGiamGia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDiscount(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy thông tin mã giảm giá theo mã code
     */
    public Discount getDiscountByCode(String maCode) {
        String sql = "SELECT MaGiamGia, MaCode, TenGiamGia, MoTa, LoaiGiamGia, HinhThucGiam, " +
                     "GiaTriGiam, GiaTriToiDa, " +
                     "CONVERT(VARCHAR(23), NgayBatDau, 120) as NgayBatDau, " +
                     "CONVERT(VARCHAR(23), NgayKetThuc, 120) as NgayKetThuc, " +
                     "TrangThai, SoLanSuDung, DaSuDung, " +
                     "CONVERT(VARCHAR(23), CreatedAt, 120) as CreatedAt " +
                     "FROM dbo.Discount WHERE MaCode = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, maCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDiscount(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tạo mã giảm giá mới
     */
    public boolean createDiscount(Discount discount) {
        String sql = "INSERT INTO dbo.Discount (MaCode, TenGiamGia, MoTa, LoaiGiamGia, HinhThucGiam, " +
                     "GiaTriGiam, GiaTriToiDa, NgayBatDau, NgayKetThuc, TrangThai, SoLanSuDung, DaSuDung) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, discount.getMaCode());
            ps.setString(2, discount.getTenGiamGia());
            ps.setString(3, discount.getMoTa());
            ps.setString(4, discount.getLoaiGiamGia());
            ps.setString(5, discount.getHinhThucGiam());
            ps.setBigDecimal(6, discount.getGiaTriGiam());
            
            if (discount.getGiaTriToiDa() != null) {
                ps.setBigDecimal(7, discount.getGiaTriToiDa());
            } else {
                ps.setNull(7, java.sql.Types.DECIMAL);
            }
            
            ps.setTimestamp(8, Timestamp.valueOf(discount.getNgayBatDau()));
            
            if (discount.getNgayKetThuc() != null) {
                ps.setTimestamp(9, Timestamp.valueOf(discount.getNgayKetThuc()));
            } else {
                ps.setNull(9, java.sql.Types.TIMESTAMP);
            }
            
            ps.setString(10, discount.getTrangThai());
            
            if (discount.getSoLanSuDung() != null) {
                ps.setInt(11, discount.getSoLanSuDung());
            } else {
                ps.setNull(11, java.sql.Types.INTEGER);
            }
            
            ps.setInt(12, discount.getDaSuDung());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật mã giảm giá
     */
    public boolean updateDiscount(Discount discount) {
        String sql = "UPDATE dbo.Discount SET MaCode = ?, TenGiamGia = ?, MoTa = ?, LoaiGiamGia = ?, " +
                     "HinhThucGiam = ?, GiaTriGiam = ?, GiaTriToiDa = ?, NgayBatDau = ?, " +
                     "NgayKetThuc = ?, TrangThai = ?, SoLanSuDung = ? WHERE MaGiamGia = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, discount.getMaCode());
            ps.setString(2, discount.getTenGiamGia());
            ps.setString(3, discount.getMoTa());
            ps.setString(4, discount.getLoaiGiamGia());
            ps.setString(5, discount.getHinhThucGiam());
            ps.setBigDecimal(6, discount.getGiaTriGiam());
            
            if (discount.getGiaTriToiDa() != null) {
                ps.setBigDecimal(7, discount.getGiaTriToiDa());
            } else {
                ps.setNull(7, java.sql.Types.DECIMAL);
            }
            
            ps.setTimestamp(8, Timestamp.valueOf(discount.getNgayBatDau()));
            
            if (discount.getNgayKetThuc() != null) {
                ps.setTimestamp(9, Timestamp.valueOf(discount.getNgayKetThuc()));
            } else {
                ps.setNull(9, java.sql.Types.TIMESTAMP);
            }
            
            ps.setString(10, discount.getTrangThai());
            
            if (discount.getSoLanSuDung() != null) {
                ps.setInt(11, discount.getSoLanSuDung());
            } else {
                ps.setNull(11, java.sql.Types.INTEGER);
            }
            
            ps.setInt(12, discount.getMaGiamGia());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa mã giảm giá (update status thành "Không hoạt động")
     */
    public boolean deleteDiscount(int maGiamGia) {
        String sql = "UPDATE dbo.Discount SET TrangThai = 'Không hoạt động' WHERE MaGiamGia = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maGiamGia);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Kiểm tra mã code đã tồn tại chưa (trừ mã hiện tại khi update)
     */
    public boolean isCodeExists(String maCode, int excludeMaGiamGia) {
        String sql = "SELECT COUNT(*) FROM dbo.Discount WHERE MaCode = ? AND MaGiamGia != ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, maCode);
            ps.setInt(2, excludeMaGiamGia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra mã code đã tồn tại chưa (khi tạo mới)
     */
    public boolean isCodeExists(String maCode) {
        String sql = "SELECT COUNT(*) FROM dbo.Discount WHERE MaCode = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, maCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Helper method để map ResultSet to Discount
     */
    private Discount mapResultSetToDiscount(ResultSet rs) throws Exception {
        Discount discount = new Discount();
        discount.setMaGiamGia(rs.getInt("MaGiamGia"));
        discount.setMaCode(rs.getString("MaCode"));
        discount.setTenGiamGia(rs.getString("TenGiamGia"));
        discount.setMoTa(rs.getString("MoTa"));
        discount.setLoaiGiamGia(rs.getString("LoaiGiamGia"));
        discount.setHinhThucGiam(rs.getString("HinhThucGiam"));
        discount.setGiaTriGiam(rs.getBigDecimal("GiaTriGiam"));
        
        BigDecimal giaTriToiDa = rs.getBigDecimal("GiaTriToiDa");
        if (!rs.wasNull()) {
            discount.setGiaTriToiDa(giaTriToiDa);
        }
        
        // Lấy datetime dưới dạng string rồi parse để tránh timezone conversion
        String ngayBatDauStr = rs.getString("NgayBatDau");
        if (ngayBatDauStr != null && !ngayBatDauStr.trim().isEmpty()) {
            try {
                // Parse string từ SQL Server DATETIME2 format: "2025-10-27 15:22:00.0000000"
                // Loại bỏ phần milliseconds nếu có
                String cleaned = ngayBatDauStr.trim();
                System.out.println("[DiscountDAO] NgayBatDau từ database (string): " + ngayBatDauStr);
                
                if (cleaned.contains(".")) {
                    cleaned = cleaned.substring(0, cleaned.indexOf("."));
                }
                // Parse: "2025-10-27 15:22:00" -> LocalDateTime
                String parseString = cleaned.replace(" ", "T");
                System.out.println("[DiscountDAO] NgayBatDau sau khi clean và replace: " + parseString);
                
                LocalDateTime parsed = LocalDateTime.parse(parseString);
                System.out.println("[DiscountDAO] NgayBatDau sau khi parse thành LocalDateTime: " + parsed);
                
                discount.setNgayBatDau(parsed);
            } catch (Exception e) {
                System.out.println("[DiscountDAO] Lỗi khi parse NgayBatDau: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        String ngayKetThucStr = rs.getString("NgayKetThuc");
        if (ngayKetThucStr != null && !ngayKetThucStr.trim().isEmpty()) {
            try {
                String cleaned = ngayKetThucStr.trim();
                System.out.println("[DiscountDAO] NgayKetThuc từ database (string): " + ngayKetThucStr);
                
                if (cleaned.contains(".")) {
                    cleaned = cleaned.substring(0, cleaned.indexOf("."));
                }
                String parseString = cleaned.replace(" ", "T");
                System.out.println("[DiscountDAO] NgayKetThuc sau khi clean và replace: " + parseString);
                
                LocalDateTime parsed = LocalDateTime.parse(parseString);
                System.out.println("[DiscountDAO] NgayKetThuc sau khi parse thành LocalDateTime: " + parsed);
                
                discount.setNgayKetThuc(parsed);
            } catch (Exception e) {
                System.out.println("[DiscountDAO] Lỗi khi parse NgayKetThuc: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        discount.setTrangThai(rs.getString("TrangThai"));
        
        Integer soLanSuDung = rs.getInt("SoLanSuDung");
        if (!rs.wasNull()) {
            discount.setSoLanSuDung(soLanSuDung);
        }
        
        discount.setDaSuDung(rs.getInt("DaSuDung"));
        
        String createdAtStr = rs.getString("CreatedAt");
        if (createdAtStr != null && !createdAtStr.trim().isEmpty()) {
            try {
                String cleaned = createdAtStr.trim();
                System.out.println("[DiscountDAO] CreatedAt từ database (string): " + createdAtStr);
                
                if (cleaned.contains(".")) {
                    cleaned = cleaned.substring(0, cleaned.indexOf("."));
                }
                String parseString = cleaned.replace(" ", "T");
                System.out.println("[DiscountDAO] CreatedAt sau khi clean và replace: " + parseString);
                
                LocalDateTime parsed = LocalDateTime.parse(parseString);
                System.out.println("[DiscountDAO] CreatedAt sau khi parse thành LocalDateTime: " + parsed);
                
                discount.setCreatedAt(parsed);
            } catch (Exception e) {
                System.out.println("[DiscountDAO] Lỗi khi parse CreatedAt: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        return discount;
    }
    
    /**
     * Lấy danh sách mã giảm giá phù hợp để hiển thị cho khách hàng
     * Điều kiện: Trạng thái = "Hoạt Động", còn trong thời hạn, còn số lần sử dụng
     */
    public List<Discount> getAvailableDiscounts() {
        List<Discount> discounts = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        String sql = "SELECT MaGiamGia, MaCode, TenGiamGia, MoTa, LoaiGiamGia, HinhThucGiam, " +
                     "GiaTriGiam, GiaTriToiDa, " +
                     "CONVERT(VARCHAR(23), NgayBatDau, 120) as NgayBatDau, " +
                     "CONVERT(VARCHAR(23), NgayKetThuc, 120) as NgayKetThuc, " +
                     "TrangThai, SoLanSuDung, DaSuDung, " +
                     "CONVERT(VARCHAR(23), CreatedAt, 120) as CreatedAt " +
                     "FROM dbo.Discount " +
                     "WHERE TrangThai = N'Hoạt Động' " +
                     "AND NgayBatDau <= ? " +
                     "AND (NgayKetThuc IS NULL OR NgayKetThuc >= ?) " +
                     "AND (SoLanSuDung IS NULL OR DaSuDung < SoLanSuDung) " +
                     "ORDER BY CreatedAt DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, java.sql.Timestamp.valueOf(now));
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(now));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Discount discount = mapResultSetToDiscount(rs);
                    discounts.add(discount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return discounts;
    }
    
    /**
     * Tăng số lần đã sử dụng của mã giảm giá
     */
    public boolean incrementDiscountUsage(int maGiamGia) {
        String sql = "UPDATE dbo.Discount SET DaSuDung = DaSuDung + 1 WHERE MaGiamGia = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maGiamGia);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

