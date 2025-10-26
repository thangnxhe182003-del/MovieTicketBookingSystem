package dal;

import model.Cinema;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CinemaDAO extends DBContext {
    
    /**
     * Lấy tất cả rạp phim
     */
    public List<Cinema> getAllCinemas() {
        List<Cinema> cinemas = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Cinema ORDER BY TenRap";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                cinemas.add(mapRowToCinema(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cinemas;
    }
    
    /**
     * Lấy rạp phim theo ID
     */
    public Cinema getCinemaById(int maRap) {
        String sql = "SELECT * FROM dbo.Cinema WHERE MaRap = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maRap);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToCinema(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Thêm rạp phim mới
     */
    public boolean addCinema(Cinema cinema) {
        String sql = "INSERT INTO dbo.Cinema (TenRap, DiaChi, KhuVuc) VALUES (?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cinema.getTenRap());
            ps.setString(2, cinema.getDiaChi());
            ps.setString(3, cinema.getKhuVuc());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cinema.setMaRap(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Cập nhật rạp phim
     */
    public boolean updateCinema(Cinema cinema) {
        String sql = "UPDATE dbo.Cinema SET TenRap = ?, DiaChi = ?, KhuVuc = ? WHERE MaRap = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cinema.getTenRap());
            ps.setString(2, cinema.getDiaChi());
            ps.setString(3, cinema.getKhuVuc());
            ps.setInt(4, cinema.getMaRap());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa rạp phim
     */
    public boolean deleteCinema(int maRap) {
        String sql = "DELETE FROM dbo.Cinema WHERE MaRap = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maRap);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Kiểm tra rạp phim có tồn tại không
     */
    public boolean cinemaExists(int maRap) {
        String sql = "SELECT COUNT(*) FROM dbo.Cinema WHERE MaRap = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maRap);
            
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
     * Kiểm tra tên rạp phim có trùng không
     */
    public boolean cinemaNameExists(String tenRap, int excludeId) {
        String sql = "SELECT COUNT(*) FROM dbo.Cinema WHERE TenRap = ? AND MaRap != ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, tenRap);
            ps.setInt(2, excludeId);
            
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
     * Tìm kiếm rạp phim theo tên hoặc khu vực
     */
    public List<Cinema> searchCinemas(String keyword) {
        List<Cinema> cinemas = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Cinema WHERE TenRap LIKE ? OR KhuVuc LIKE ? OR DiaChi LIKE ? ORDER BY TenRap";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cinemas.add(mapRowToCinema(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cinemas;
    }
    
    /**
     * Lấy danh sách khu vực
     */
    public List<String> getDistinctAreas() {
        List<String> areas = new ArrayList<>();
        String sql = "SELECT DISTINCT KhuVuc FROM dbo.Cinema WHERE KhuVuc IS NOT NULL ORDER BY KhuVuc";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String area = rs.getString("KhuVuc");
                if (area != null && !area.trim().isEmpty()) {
                    areas.add(area);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return areas;
    }
    
    /**
     * Map ResultSet to Cinema object
     */
    private Cinema mapRowToCinema(ResultSet rs) throws SQLException {
        Cinema cinema = new Cinema();
        cinema.setMaRap(rs.getInt("MaRap"));
        cinema.setTenRap(rs.getString("TenRap"));
        cinema.setDiaChi(rs.getString("DiaChi"));
        cinema.setKhuVuc(rs.getString("KhuVuc"));
        return cinema;
    }
}