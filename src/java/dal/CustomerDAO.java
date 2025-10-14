package dal;

import Model.Customer;
import java.sql.*;

public class CustomerDAO extends DBContext {
    
    /**
     * Xác thực đăng nhập khách hàng
     * Có thể đăng nhập bằng: Tên đăng nhập, Email hoặc Số điện thoại
     */
    public Customer authenticate(String username, String password) {
        String sql = "SELECT * FROM Customer WHERE (TenDangNhap = ? OR Email = ? OR SoDienThoai = ?) AND MatKhau = ?";
        System.out.println("Authenticating: username='" + username + "', password='" + password + "'");
        try (Connection conn = getConnection()) {
            if (conn == null) {
                System.out.println("Connection failed - check DBContext configuration.");
                return null;
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, username);
                ps.setString(3, username);
                ps.setString(4, password);
                System.out.println("Executing query with params: username=" + username + ", password=" + password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Customer customer = new Customer();
                        customer.setMaKH(rs.getInt("MaKH"));
                        customer.setTenDangNhap(rs.getString("TenDangNhap"));
                        customer.setMatKhau(rs.getString("MatKhau"));
                        customer.setHoTen(rs.getString("HoTen"));
                        customer.setNgaySinh(rs.getDate("NgaySinh"));
                        customer.setGioiTinh(rs.getString("GioiTinh"));
                        customer.setSoDienThoai(rs.getString("SoDienThoai"));
                        customer.setEmail(rs.getString("Email"));
                        customer.setDiemHienCo(rs.getInt("DiemHienCo"));
                        System.out.println("Success: Found user " + customer.getHoTen() + ", MatKhau from DB: '" + customer.getMatKhau() + "'");
                        return customer;
                    } else {
                        System.out.println("No match found for username: '" + username + "', password: '" + password + "'");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy thông tin khách hàng theo ID
     */
    public Customer getCustomerById(int maKH) {
        String sql = "SELECT * FROM Customer WHERE MaKH = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maKH);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setMaKH(rs.getInt("MaKH"));
                customer.setTenDangNhap(rs.getString("TenDangNhap"));
                customer.setMatKhau(rs.getString("MatKhau"));
                customer.setHoTen(rs.getString("HoTen"));
                customer.setNgaySinh(rs.getDate("NgaySinh"));
                customer.setGioiTinh(rs.getString("GioiTinh"));
                customer.setSoDienThoai(rs.getString("SoDienThoai"));
                customer.setEmail(rs.getString("Email"));
                customer.setDiemHienCo(rs.getInt("DiemHienCo"));
                return customer;
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi lấy thông tin khách hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Kiểm tra tên đăng nhập đã tồn tại chưa
     */
    public boolean isUsernameExist(String username) {
        String sql = "SELECT COUNT(*) FROM Customer WHERE TenDangNhap = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi kiểm tra username: " + e.getMessage());
        }
        return false;
    }

    /**
     * Kiểm tra email đã tồn tại chưa
     */
    public boolean isEmailExist(String email) {
        String sql = "SELECT COUNT(*) FROM Customer WHERE Email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi kiểm tra email: " + e.getMessage());
        }
        return false;
    }

    /**
     * Đăng ký khách hàng mới
     */
    public boolean registerCustomer(Customer customer) {
        String sql = "INSERT INTO Customer (TenDangNhap, MatKhau, HoTen, NgaySinh, " +
                    "GioiTinh, SoDienThoai, Email, DiemHienCo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, 0)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getTenDangNhap());
            ps.setString(2, customer.getMatKhau());
            ps.setString(3, customer.getHoTen());
            ps.setDate(4, customer.getNgaySinh());
            ps.setString(5, customer.getGioiTinh());
            ps.setString(6, customer.getSoDienThoai());
            ps.setString(7, customer.getEmail());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đăng ký khách hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật thông tin khách hàng
     */
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE Customer SET HoTen = ?, NgaySinh = ?, GioiTinh = ?, " +
                    "SoDienThoai = ?, Email = ? WHERE MaKH = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getHoTen());
            ps.setDate(2, customer.getNgaySinh());
            ps.setString(3, customer.getGioiTinh());
            ps.setString(4, customer.getSoDienThoai());
            ps.setString(5, customer.getEmail());
            ps.setInt(6, customer.getMaKH());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi cập nhật thông tin: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Test DAO
     */
    public static void main(String[] args) {
        CustomerDAO dao = new CustomerDAO();
        System.out.println("=== Test đăng nhập ===");
        Customer customer = dao.authenticate("user1", "hashed_pass1");
        if (customer != null) {
            System.out.println("Đăng nhập thành công!");
            System.out.println(customer);
        } else {
            System.out.println("Đăng nhập thất bại!");
        }
    }
}