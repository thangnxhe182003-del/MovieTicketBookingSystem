package dal;

import model.Staff;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO extends DBContext {
    
    /**
     * Lấy tất cả nhân viên
     */
    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Staff ORDER BY NgayTao DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                staffList.add(mapRowToStaff(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }
    
    /**
     * Lấy nhân viên theo ID
     */
    public Staff getStaffById(int maNhanVien) {
        String sql = "SELECT * FROM dbo.Staff WHERE MaNhanVien = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maNhanVien);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToStaff(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Thêm nhân viên mới
     */
    public boolean addStaff(Staff staff) {
        String sql = "INSERT INTO dbo.Staff (MaRap, TenDangNhap, MatKhau, HoTen, ChucVu, SoDienThoai, Email, NgayTao, NgayCapNhat, TrangThai) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, staff.getMaRap());
            ps.setString(2, staff.getTenDangNhap());
            ps.setString(3, staff.getMatKhau());
            ps.setString(4, staff.getHoTen());
            ps.setString(5, staff.getChucVu());
            ps.setString(6, staff.getSoDienThoai());
            ps.setString(7, staff.getEmail());
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(10, staff.getTrangThai());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        staff.setMaNhanVien(generatedKeys.getInt(1));
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
     * Cập nhật nhân viên
     */
    public boolean updateStaff(Staff staff) {
        String sql = "UPDATE dbo.Staff SET MaRap = ?, TenDangNhap = ?, MatKhau = ?, HoTen = ?, " +
                    "ChucVu = ?, SoDienThoai = ?, Email = ?, NgayCapNhat = ?, TrangThai = ? " +
                    "WHERE MaNhanVien = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, staff.getMaRap());
            ps.setString(2, staff.getTenDangNhap());
            ps.setString(3, staff.getMatKhau());
            ps.setString(4, staff.getHoTen());
            ps.setString(5, staff.getChucVu());
            ps.setString(6, staff.getSoDienThoai());
            ps.setString(7, staff.getEmail());
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(9, staff.getTrangThai());
            ps.setInt(10, staff.getMaNhanVien());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa nhân viên (soft delete)
     */
    public boolean deleteStaff(int maNhanVien) {
        String sql = "UPDATE dbo.Staff SET TrangThai = 'Inactive', NgayCapNhat = ? WHERE MaNhanVien = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, maNhanVien);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Kiểm tra tên đăng nhập có tồn tại không
     */
    public boolean usernameExists(String tenDangNhap, int excludeId) {
        String sql = "SELECT COUNT(*) FROM dbo.Staff WHERE TenDangNhap = ? AND MaNhanVien != ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, tenDangNhap);
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
     * Kiểm tra email có tồn tại không
     */
    public boolean emailExists(String email, int excludeId) {
        String sql = "SELECT COUNT(*) FROM dbo.Staff WHERE Email = ? AND MaNhanVien != ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
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
     * Tìm kiếm nhân viên
     */
    public List<Staff> searchStaff(String keyword) {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Staff WHERE HoTen LIKE ? OR TenDangNhap LIKE ? OR Email LIKE ? OR SoDienThoai LIKE ? OR ChucVu LIKE ? ORDER BY NgayTao DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            ps.setString(5, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    staffList.add(mapRowToStaff(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }
    
    /**
     * Lấy nhân viên theo chức vụ
     */
    public List<Staff> getStaffByPosition(String chucVu) {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Staff WHERE ChucVu = ? ORDER BY NgayTao DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, chucVu);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    staffList.add(mapRowToStaff(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }
    
    /**
     * Lấy nhân viên theo trạng thái
     */
    public List<Staff> getStaffByStatus(String trangThai) {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Staff WHERE TrangThai = ? ORDER BY NgayTao DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    staffList.add(mapRowToStaff(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }
    
    /**
     * Cập nhật trạng thái nhân viên
     */
    public boolean updateStaffStatus(int maNhanVien, String trangThai) {
        String sql = "UPDATE dbo.Staff SET TrangThai = ?, NgayCapNhat = ? WHERE MaNhanVien = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, maNhanVien);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Lấy danh sách chức vụ
     */
    public List<String> getDistinctPositions() {
        List<String> positions = new ArrayList<>();
        String sql = "SELECT DISTINCT ChucVu FROM dbo.Staff WHERE ChucVu IS NOT NULL ORDER BY ChucVu";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String position = rs.getString("ChucVu");
                if (position != null && !position.trim().isEmpty()) {
                    positions.add(position);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return positions;
    }
    
    /**
     * Map ResultSet to Staff object
     */
    private Staff mapRowToStaff(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setMaNhanVien(rs.getInt("MaNhanVien"));
        staff.setMaRap(rs.getInt("MaRap"));
        staff.setTenDangNhap(rs.getString("TenDangNhap"));
        staff.setMatKhau(rs.getString("MatKhau"));
        staff.setHoTen(rs.getString("HoTen"));
        staff.setChucVu(rs.getString("ChucVu"));
        staff.setSoDienThoai(rs.getString("SoDienThoai"));
        staff.setEmail(rs.getString("Email"));
        
        Timestamp ngayTao = rs.getTimestamp("NgayTao");
        if (ngayTao != null) {
            staff.setNgayTao(ngayTao.toLocalDateTime());
        }
        
        Timestamp ngayCapNhat = rs.getTimestamp("NgayCapNhat");
        if (ngayCapNhat != null) {
            staff.setNgayCapNhat(ngayCapNhat.toLocalDateTime());
        }
        
        staff.setTrangThai(rs.getString("TrangThai"));
        return staff;
    }
    
    /**
     * Kiểm tra đăng nhập cho staff
     */
    public Staff checkLogin(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM dbo.Staff WHERE TenDangNhap = ? AND MatKhau = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, tenDangNhap);
            ps.setString(2, matKhau);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToStaff(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
