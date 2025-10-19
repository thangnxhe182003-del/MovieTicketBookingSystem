/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Random;
import model.Customer;
import model.CustomerToken;

/**
 *
 * @author thang
 */
public class CustomerDAO extends DBContext {

    /**
     * Check if an account with the given username, email, or phone already exists
     * 
     * @param username Username to check
     * @param email Email to check
     * @param phone Phone number to check
     * @return true if any of these already exist
     */
    public boolean checkAccountExists(String username, String email, String phone) {
        String sql = "SELECT * FROM Customer WHERE TenDangNhap = ? OR Email = ? OR SoDienThoai = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, phone);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking account: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy khách hàng theo tên đăng nhập hoặc email
     */
    public Customer getByUsernameOrEmail(String username, String email) {
        StringBuilder sql = new StringBuilder("SELECT * FROM dbo.Customer WHERE 1=1 ");
        boolean hasUsername = username != null && !username.trim().isEmpty();
        boolean hasEmail = email != null && !email.trim().isEmpty();
        if (hasUsername) {
            sql.append("AND TenDangNhap = ? ");
        }
        if (hasEmail) {
            sql.append(hasUsername ? "OR Email = ? " : "AND Email = ? ");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int idx = 1;
            if (hasUsername) {
                ps.setString(idx++, username);
            }
            if (hasEmail) {
                ps.setString(idx++, email);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Customer c = new Customer();
                    c.setMaKH(rs.getInt("MaKH"));
                    c.setTenDangNhap(rs.getString("TenDangNhap"));
                    c.setMatKhau(rs.getString("MatKhau"));
                    c.setHoTen(rs.getString("HoTen"));
                    c.setNgaySinh(rs.getString("NgaySinh"));
                    c.setGioiTinh(rs.getString("GioiTinh"));
                    c.setSoDienThoai(rs.getString("SoDienThoai"));
                    c.setEmail(rs.getString("Email"));
                    c.setIsEmailVerified(rs.getBoolean("IsEmailVerified"));
                    c.setTrangThai(rs.getString("TrangThai"));
                    java.sql.Timestamp created = rs.getTimestamp("NgayTao");
                    if (created != null) c.setNgayTao(created.toLocalDateTime());
                    java.sql.Timestamp updated = rs.getTimestamp("NgayCapNhat");
                    if (updated != null) c.setNgayCapNhat(updated.toLocalDateTime());
                    return c;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật thông tin hồ sơ người dùng (không bao gồm mật khẩu, trạng thái, verified)
     */
    public boolean updateProfile(Customer customer) {
        String sql = "UPDATE dbo.Customer SET HoTen = ?, NgaySinh = ?, GioiTinh = ?, SoDienThoai = ?, Email = ?, NgayCapNhat = SYSUTCDATETIME() WHERE MaKH = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customer.getHoTen());
            ps.setString(2, customer.getNgaySinh());
            ps.setString(3, customer.getGioiTinh());
            ps.setString(4, customer.getSoDienThoai());
            ps.setString(5, customer.getEmail());
            ps.setInt(6, customer.getMaKH());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Đổi mật khẩu an toàn, xác nhận mật khẩu cũ
     */
    public boolean changePassword(int maKH, String oldPassword, String newPassword) {
        String sql = "UPDATE dbo.Customer SET MatKhau = ?, NgayCapNhat = SYSUTCDATETIME() WHERE MaKH = ? AND MatKhau = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, maKH);
            ps.setString(3, oldPassword);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Check login credentials
     * 
     * @param usernameOrEmail Username or email
     * @param password Password
     * @return Customer object if login successful, null otherwise
     */
    public Customer checkLogin(String usernameOrEmail, String password) {
        String sql = "SELECT * FROM Customer WHERE (TenDangNhap = ? OR Email = ?) AND MatKhau = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, usernameOrEmail);
            ps.setString(2, usernameOrEmail);
            ps.setString(3, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Customer c = new Customer();
                c.setMaKH(rs.getInt("MaKH"));
                c.setTenDangNhap(rs.getString("TenDangNhap"));
                c.setMatKhau(rs.getString("MatKhau"));
                c.setHoTen(rs.getString("HoTen"));
                
                Date ngaySinh = rs.getDate("NgaySinh");
                c.setNgaySinh(ngaySinh != null ? ngaySinh.toString() : null);
                
                c.setGioiTinh(rs.getString("GioiTinh"));
                c.setSoDienThoai(rs.getString("SoDienThoai"));
                c.setEmail(rs.getString("Email"));
                c.setIsEmailVerified(rs.getBoolean("IsEmailVerified"));
                
                Timestamp ngayTao = rs.getTimestamp("NgayTao");
                c.setNgayTao(ngayTao != null ? ngayTao.toLocalDateTime() : null);
                
                Timestamp ngayCapNhat = rs.getTimestamp("NgayCapNhat");
                c.setNgayCapNhat(ngayCapNhat != null ? ngayCapNhat.toLocalDateTime() : null);
                
                c.setTrangThai(rs.getString("TrangThai"));
                
                return c;
            }
        } catch (SQLException e) {
            System.out.println("Error checking login: " + e.getMessage());
        }
        return null;
    }

    /**
     * Insert a new customer into the database
     * 
     * @param c Customer object with data to insert
     * @return true if insert was successful
     */
    public boolean insertCustomer(Customer c) {
        String sql = "INSERT INTO Customer (TenDangNhap, MatKhau, HoTen, NgaySinh, GioiTinh, "
                + "SoDienThoai, Email, IsEmailVerified, TrangThai) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, c.getTenDangNhap());
            ps.setString(2, c.getMatKhau());
            ps.setString(3, c.getHoTen());

            if (c.getNgaySinh() == null || c.getNgaySinh().isEmpty()) {
                ps.setNull(4, java.sql.Types.DATE);
            } else {
                ps.setDate(4, java.sql.Date.valueOf(c.getNgaySinh()));
            }

            String gioiTinh = c.getGioiTinh();
            if (gioiTinh == null || gioiTinh.isEmpty()) {
                gioiTinh = "Khac";
            }
            ps.setString(5, gioiTinh);

            ps.setString(6, c.getSoDienThoai());
            ps.setString(7, c.getEmail());
            ps.setString(8, c.getTrangThai());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error inserting customer: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get the last inserted customer ID
     * 
     * @return The MaKH of the last inserted customer or -1 if error
     */
    public int getLastInsertedMaKH() {
        String sql = "SELECT MAX(MaKH) as LastID FROM Customer";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("LastID");
            }
        } catch (SQLException e) {
            System.out.println("Error getting last ID: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Generate a random 6-digit OTP code
     * 
     * @return 6-digit OTP as string
     */
    public String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));  // 6 chữ số
    }

    /**
     * Create a new token for the given customer and purpose
     * 
     * @param maKH Customer ID
     * @param purpose Purpose of the token (use constants from CustomerToken class)
     * @param expiryMinutes Minutes until token expires
     * @return The generated OTP code or null if error
     */
    public String createToken(int maKH, String purpose, int expiryMinutes) {
        String otp = generateOTP();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(expiryMinutes);
        
        String sql = "INSERT INTO CustomerToken (MaKH, Purpose, OtpCode, ExpiresAt, IsUsed) "
                + "VALUES (?, ?, ?, ?, 0)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, maKH);
            ps.setString(2, purpose);
            ps.setString(3, otp);
            ps.setTimestamp(4, Timestamp.valueOf(expiresAt));
            
            if (ps.executeUpdate() > 0) {
                return otp;
            }
        } catch (SQLException e) {
            System.out.println("Error creating token: " + e.getMessage());
        }
        return null;
    }

    /**
     * Verify an OTP token for a specific purpose and mark it as used if valid
     * 
     * @param maKH Customer ID
     * @param otp OTP code to verify
     * @param purpose Purpose of the token
     * @return true if token is valid and was marked as used
     */
    public boolean verifyToken(int maKH, String otp, String purpose) {
        String sql = "SELECT * FROM CustomerToken WHERE MaKH = ? AND OtpCode = ? "
                + "AND Purpose = ? AND IsUsed = 0 AND ExpiresAt > GETUTCDATE()";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, maKH);
            ps.setString(2, otp);
            ps.setString(3, purpose);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                // Token is valid, mark it as used
                Long tokenId = rs.getLong("TokenId");
                markTokenAsUsed(tokenId);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error verifying token: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Verify an OTP token by email and OTP code for a specific purpose
     * 
     * @param email Customer email
     * @param otp OTP code to verify
     * @param purpose Purpose of the token
     * @return The customer ID if token is valid, or -1 if invalid
     */
    public int verifyTokenByEmail(String email, String otp, String purpose) {
        String sql = "SELECT c.MaKH, t.TokenId FROM Customer c "
                + "JOIN CustomerToken t ON c.MaKH = t.MaKH "
                + "WHERE c.Email = ? AND t.OtpCode = ? AND t.Purpose = ? "
                + "AND t.IsUsed = 0 AND t.ExpiresAt > GETUTCDATE()";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, otp);
            ps.setString(3, purpose);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int maKH = rs.getInt("MaKH");
                Long tokenId = rs.getLong("TokenId");
                markTokenAsUsed(tokenId);
                return maKH;
            }
        } catch (SQLException e) {
            System.out.println("Error verifying token by email: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Mark a token as used
     * 
     * @param tokenId ID of the token to mark
     * @return true if successful
     */
    private boolean markTokenAsUsed(Long tokenId) {
        String sql = "UPDATE CustomerToken SET IsUsed = 1, UsedAt = GETUTCDATE() WHERE TokenId = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, tokenId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error marking token as used: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Activate a customer account after email verification
     * 
     * @param maKH Customer ID to activate
     * @return true if successful
     */
    public boolean activateCustomer(int maKH) {
        String sql = "UPDATE Customer SET IsEmailVerified = 1, TrangThai = 'Active', "
                + "NgayCapNhat = GETUTCDATE() WHERE MaKH = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error activating customer: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get customer by email
     * 
     * @param email Email to search for
     * @return Customer object or null if not found
     */
    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT * FROM Customer WHERE Email = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Customer c = new Customer();
                c.setMaKH(rs.getInt("MaKH"));
                c.setTenDangNhap(rs.getString("TenDangNhap"));
                c.setMatKhau(rs.getString("MatKhau"));
                c.setHoTen(rs.getString("HoTen"));
                
                Date ngaySinh = rs.getDate("NgaySinh");
                c.setNgaySinh(ngaySinh != null ? ngaySinh.toString() : null);
                
                c.setGioiTinh(rs.getString("GioiTinh"));
                c.setSoDienThoai(rs.getString("SoDienThoai"));
                c.setEmail(rs.getString("Email"));
                c.setIsEmailVerified(rs.getBoolean("IsEmailVerified"));
                
                Timestamp ngayTao = rs.getTimestamp("NgayTao");
                c.setNgayTao(ngayTao != null ? ngayTao.toLocalDateTime() : null);
                
                Timestamp ngayCapNhat = rs.getTimestamp("NgayCapNhat");
                c.setNgayCapNhat(ngayCapNhat != null ? ngayCapNhat.toLocalDateTime() : null);
                
                c.setTrangThai(rs.getString("TrangThai"));
                
                return c;
            }
        } catch (SQLException e) {
            System.out.println("Error getting customer by email: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Update customer password
     * 
     * @param maKH Customer ID
     * @param newPassword New password
     * @return true if successful
     */
    public boolean updatePassword(int maKH, String newPassword) {
        String sql = "UPDATE Customer SET MatKhau = ?, NgayCapNhat = GETUTCDATE() WHERE MaKH = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setInt(2, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
        }
        return false;
    }
}