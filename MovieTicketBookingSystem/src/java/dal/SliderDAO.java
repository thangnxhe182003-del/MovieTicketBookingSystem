package dal;

import model.Slider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SliderDAO extends DBContext {

    /**
     * Lấy danh sách tất cả slider
     */
    public List<Slider> getAllSliders() {
        List<Slider> sliders = new ArrayList<>();
        String sql = "SELECT MaSlider, TieuDe, MoTa, AnhSlide, ThuTuHienThi, TrangThai, " +
                     "CONVERT(VARCHAR(23), NgayBatDau, 120) as NgayBatDau, " +
                     "CONVERT(VARCHAR(23), NgayKetThuc, 120) as NgayKetThuc, " +
                     "CONVERT(VARCHAR(23), CreatedAt, 120) as CreatedAt, " +
                     "CONVERT(VARCHAR(23), UpdatedAt, 120) as UpdatedAt " +
                     "FROM dbo.Slider ORDER BY ThuTuHienThi, MaSlider";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                sliders.add(mapResultSetToSlider(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sliders;
    }

    /**
     * Lấy thông tin slider theo ID
     */
    public Slider getSliderById(int maSlider) {
        String sql = "SELECT MaSlider, TieuDe, MoTa, AnhSlide, ThuTuHienThi, TrangThai, " +
                     "CONVERT(VARCHAR(23), NgayBatDau, 120) as NgayBatDau, " +
                     "CONVERT(VARCHAR(23), NgayKetThuc, 120) as NgayKetThuc, " +
                     "CONVERT(VARCHAR(23), CreatedAt, 120) as CreatedAt, " +
                     "CONVERT(VARCHAR(23), UpdatedAt, 120) as UpdatedAt " +
                     "FROM dbo.Slider WHERE MaSlider = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSlider);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSlider(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tạo slider mới
     */
    public boolean createSlider(Slider slider) {
        String sql = "INSERT INTO dbo.Slider (TieuDe, MoTa, AnhSlide, ThuTuHienThi, TrangThai, NgayBatDau, NgayKetThuc, CreatedAt, UpdatedAt) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, SYSUTCDATETIME(), SYSUTCDATETIME())";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, slider.getTieuDe());
            ps.setString(2, slider.getMoTa());
            ps.setString(3, slider.getAnhSlide());
            ps.setInt(4, slider.getThuTuHienThi());
            ps.setString(5, slider.getTrangThai());
            
            if (slider.getNgayBatDau() != null) {
                ps.setTimestamp(6, java.sql.Timestamp.valueOf(slider.getNgayBatDau()));
            } else {
                ps.setTimestamp(6, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            
            if (slider.getNgayKetThuc() != null) {
                ps.setTimestamp(7, java.sql.Timestamp.valueOf(slider.getNgayKetThuc()));
            } else {
                ps.setNull(7, java.sql.Types.TIMESTAMP);
            }
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật slider
     */
    public boolean updateSlider(Slider slider) {
        String sql = "UPDATE dbo.Slider SET TieuDe = ?, MoTa = ?, AnhSlide = ?, ThuTuHienThi = ?, " +
                     "TrangThai = ?, NgayBatDau = ?, NgayKetThuc = ?, UpdatedAt = SYSUTCDATETIME() " +
                     "WHERE MaSlider = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, slider.getTieuDe());
            ps.setString(2, slider.getMoTa());
            ps.setString(3, slider.getAnhSlide());
            ps.setInt(4, slider.getThuTuHienThi());
            ps.setString(5, slider.getTrangThai());
            
            if (slider.getNgayBatDau() != null) {
                ps.setTimestamp(6, java.sql.Timestamp.valueOf(slider.getNgayBatDau()));
            } else {
                ps.setTimestamp(6, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            
            if (slider.getNgayKetThuc() != null) {
                ps.setTimestamp(7, java.sql.Timestamp.valueOf(slider.getNgayKetThuc()));
            } else {
                ps.setNull(7, java.sql.Types.TIMESTAMP);
            }
            
            ps.setInt(8, slider.getMaSlider());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa slider (cập nhật trạng thái thành "Ẩn")
     */
    public boolean deleteSlider(int maSlider) {
        String sql = "UPDATE dbo.Slider SET TrangThai = 'Ẩn', UpdatedAt = SYSUTCDATETIME() WHERE MaSlider = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSlider);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy danh sách slider đang hiển thị (trạng thái = "Hiển thị" và trong khoảng ngày)
     */
    public List<Slider> getActiveSliders() {
        List<Slider> sliders = new ArrayList<>();
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        System.out.println("[SliderDAO] getActiveSliders() - Current time: " + now);
        
        String sql = "SELECT MaSlider, TieuDe, MoTa, AnhSlide, ThuTuHienThi, TrangThai, " +
                     "CONVERT(VARCHAR(23), NgayBatDau, 120) as NgayBatDau, " +
                     "CONVERT(VARCHAR(23), NgayKetThuc, 120) as NgayKetThuc, " +
                     "CONVERT(VARCHAR(23), CreatedAt, 120) as CreatedAt, " +
                     "CONVERT(VARCHAR(23), UpdatedAt, 120) as UpdatedAt " +
                     "FROM dbo.Slider " +
                     "WHERE TrangThai = ? " +
                     "AND NgayBatDau <= ? " +
                     "AND (NgayKetThuc IS NULL OR NgayKetThuc >= ?) " +
                     "ORDER BY ThuTuHienThi, MaSlider";
        System.out.println("[SliderDAO] SQL Query: " + sql);
        System.out.println("[SliderDAO] Parameters: trangThai='Hiển thị', now=" + now);
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Hiển thị");
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(now));
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(now));
            try (ResultSet rs = ps.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    // Log raw values from database
                    String ngayBatDauStr = rs.getString("NgayBatDau");
                    String ngayKetThucStr = rs.getString("NgayKetThuc");
                    String trangThaiStr = rs.getString("TrangThai");
                    System.out.println("[SliderDAO] Raw from DB - MaSlider=" + rs.getInt("MaSlider") + 
                                     ", TrangThai='" + trangThaiStr + "'" +
                                     ", NgayBatDau (string)='" + ngayBatDauStr + "'" +
                                     ", NgayKetThuc (string)='" + ngayKetThucStr + "'");
                    
                    Slider slider = mapResultSetToSlider(rs);
                    sliders.add(slider);
                    
                    // Check if dates are valid
                    boolean ngayBatDauValid = slider.getNgayBatDau() != null && (slider.getNgayBatDau().isBefore(now) || slider.getNgayBatDau().isEqual(now));
                    boolean ngayKetThucValid = slider.getNgayKetThuc() == null || slider.getNgayKetThuc().isAfter(now) || slider.getNgayKetThuc().isEqual(now);
                    
                    System.out.println("[SliderDAO] Found slider #" + count + ": MaSlider=" + slider.getMaSlider() + 
                                     ", TieuDe=" + slider.getTieuDe() + 
                                     ", AnhSlide=" + slider.getAnhSlide() + 
                                     ", ThuTuHienThi=" + slider.getThuTuHienThi() +
                                     ", NgayBatDau=" + slider.getNgayBatDau() + " (valid: " + ngayBatDauValid + ")" +
                                     ", NgayKetThuc=" + slider.getNgayKetThuc() + " (valid: " + ngayKetThucValid + ")");
                }
                System.out.println("[SliderDAO] Total active sliders found: " + count);
            }
        } catch (Exception e) {
            System.out.println("[SliderDAO] Error getting active sliders: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("[SliderDAO] Returning " + sliders.size() + " sliders");
        return sliders;
    }

    /**
     * Map ResultSet sang Slider object
     */
    private Slider mapResultSetToSlider(ResultSet rs) throws Exception {
        Slider slider = new Slider();
        slider.setMaSlider(rs.getInt("MaSlider"));
        slider.setTieuDe(rs.getString("TieuDe"));
        slider.setMoTa(rs.getString("MoTa"));
        slider.setAnhSlide(rs.getString("AnhSlide"));
        slider.setThuTuHienThi(rs.getInt("ThuTuHienThi"));
        slider.setTrangThai(rs.getString("TrangThai"));
        
        // Parse datetime từ string
        String ngayBatDauStr = rs.getString("NgayBatDau");
        if (ngayBatDauStr != null && !ngayBatDauStr.trim().isEmpty()) {
            // Format: yyyy-MM-dd HH:mm:ss
            String cleaned = ngayBatDauStr.trim();
            if (cleaned.contains(".")) {
                cleaned = cleaned.substring(0, cleaned.indexOf("."));
            }
            cleaned = cleaned.replace(" ", "T");
            slider.setNgayBatDau(LocalDateTime.parse(cleaned));
        }
        
        String ngayKetThucStr = rs.getString("NgayKetThuc");
        if (ngayKetThucStr != null && !ngayKetThucStr.trim().isEmpty()) {
            String cleaned = ngayKetThucStr.trim();
            if (cleaned.contains(".")) {
                cleaned = cleaned.substring(0, cleaned.indexOf("."));
            }
            cleaned = cleaned.replace(" ", "T");
            slider.setNgayKetThuc(LocalDateTime.parse(cleaned));
        }
        
        String createdAtStr = rs.getString("CreatedAt");
        if (createdAtStr != null && !createdAtStr.trim().isEmpty()) {
            String cleaned = createdAtStr.trim();
            if (cleaned.contains(".")) {
                cleaned = cleaned.substring(0, cleaned.indexOf("."));
            }
            cleaned = cleaned.replace(" ", "T");
            slider.setCreatedAt(LocalDateTime.parse(cleaned));
        }
        
        String updatedAtStr = rs.getString("UpdatedAt");
        if (updatedAtStr != null && !updatedAtStr.trim().isEmpty()) {
            String cleaned = updatedAtStr.trim();
            if (cleaned.contains(".")) {
                cleaned = cleaned.substring(0, cleaned.indexOf("."));
            }
            cleaned = cleaned.replace(" ", "T");
            slider.setUpdatedAt(LocalDateTime.parse(cleaned));
        }
        
        return slider;
    }
}

