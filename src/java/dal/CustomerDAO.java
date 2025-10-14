/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import java.util.Random;
import java.util.UUID;
import model.Customer;

/**
 *
 * @author thang
 */
public class CustomerDAO extends DBContext {

    public boolean checkAccountExists(String username, String email, String phone) {
        String sql = "SELECT * FROM Customer WHERE (TenDangNhap = ? OR Email = ? OR SoDienThoai = ?) AND IsActivated = 0";  // Chỉ check inactive để tránh duplicate
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, phone);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
        }
        return false;
    }

    public boolean insertCustomer(Customer c) {
        String sql = "INSERT INTO Customer (TenDangNhap, MatKhau, HoTen, NgaySinh, GioiTinh, SoDienThoai, Email, ChucVu, DiemHienCo, IsActivated, OTP) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, 'Inactive', 0, 0, NULL)";
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

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
        }
        return false;
    }

    // Generate và save OTP cho customer mới (6 chữ số ngẫu nhiên)
    public String generateAndSaveOTP(int maKH) {
        Random random = new Random();
        String otp = String.format("%06d", random.nextInt(1000000));  // 6 chữ số
        String sql = "UPDATE Customer SET OTP = ? WHERE MaKH = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, otp);
            ps.setInt(2, maKH);
            ps.executeUpdate();
            return otp;
        } catch (SQLException e) {
        }
        return null;
    }

    // Verify OTP và activate customer
    public boolean verifyAndActivateOTP(String email, String inputOtp) {
        String sql = "UPDATE Customer SET IsActivated = 1, ChucVu = 'Active', OTP = NULL WHERE Email = ? AND IsActivated = 0 AND OTP = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, inputOtp);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
        }
        return false;
    }

    // Lấy MaKH cuối cùng insert
    public int getLastInsertedMaKH() {
        String sql = "SELECT MAX(MaKH) as LastID FROM Customer";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("LastID");
            }
        } catch (SQLException e) {
        }
        return -1;
    }
}
