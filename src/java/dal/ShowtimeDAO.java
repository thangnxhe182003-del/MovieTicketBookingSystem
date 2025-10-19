package dal;

import model.Showtime;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeDAO extends DBContext {

    /**
     * Lấy tất cả suất chiếu
     */
    public List<Showtime> getAllShowtimes() {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT s.*, m.TenPhim, rm.TenPhong, c.TenRap " +
                    "FROM dbo.Showtime s " +
                    "JOIN dbo.Movie m ON s.MaPhim = m.MaPhim " +
                    "JOIN dbo.Room rm ON s.MaPhong = rm.MaPhong " +
                    "JOIN dbo.Cinema c ON rm.MaRap = c.MaRap " +
                    "ORDER BY s.NgayChieu DESC, s.GioBatDau ASC";
        
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                showtimes.add(mapRowToShowtime(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    /**
     * Lấy suất chiếu theo ID
     */
    public Showtime getShowtimeById(int maSuatChieu) {
        String sql = "SELECT s.*, m.TenPhim, rm.TenPhong, c.TenRap " +
                    "FROM dbo.Showtime s " +
                    "JOIN dbo.Movie m ON s.MaPhim = m.MaPhim " +
                    "JOIN dbo.Room rm ON s.MaPhong = rm.MaPhong " +
                    "JOIN dbo.Cinema c ON rm.MaRap = c.MaRap " +
                    "WHERE s.MaSuatChieu = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToShowtime(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy suất chiếu của phim theo ID phim
     */
    public List<Showtime> getShowtimesByMovie(int maPhim) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT s.*, m.TenPhim, rm.TenPhong, c.TenRap " +
                    "FROM dbo.Showtime s " +
                    "JOIN dbo.Movie m ON s.MaPhim = m.MaPhim " +
                    "JOIN dbo.Room rm ON s.MaPhong = rm.MaPhong " +
                    "JOIN dbo.Cinema c ON rm.MaRap = c.MaRap " +
                    "WHERE s.MaPhim = ? AND s.NgayChieu >= CAST(GETDATE() AS DATE) " +
                    "ORDER BY s.NgayChieu ASC, s.GioBatDau ASC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhim);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRowToShowtime(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    /**
     * Lấy suất chiếu theo ngày
     */
    public List<Showtime> getShowtimesByDate(LocalDate date) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT s.*, m.TenPhim, rm.TenPhong, c.TenRap " +
                    "FROM dbo.Showtime s " +
                    "JOIN dbo.Movie m ON s.MaPhim = m.MaPhim " +
                    "JOIN dbo.Room rm ON s.MaPhong = rm.MaPhong " +
                    "JOIN dbo.Cinema c ON rm.MaRap = c.MaRap " +
                    "WHERE CAST(s.NgayChieu AS DATE) = ? " +
                    "ORDER BY s.GioBatDau ASC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRowToShowtime(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    /**
     * Lấy suất chiếu theo phòng chiếu
     */
    public List<Showtime> getShowtimesByRoom(int maPhong) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT s.*, m.TenPhim, rm.TenPhong, c.TenRap " +
                    "FROM dbo.Showtime s " +
                    "JOIN dbo.Movie m ON s.MaPhim = m.MaPhim " +
                    "JOIN dbo.Room rm ON s.MaPhong = rm.MaPhong " +
                    "JOIN dbo.Cinema c ON rm.MaRap = c.MaRap " +
                    "WHERE s.MaPhong = ? AND s.NgayChieu >= CAST(GETDATE() AS DATE) " +
                    "ORDER BY s.NgayChieu ASC, s.GioBatDau ASC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRowToShowtime(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    /**
     * Lấy suất chiếu theo phim và ngày
     */
    public List<Showtime> getShowtimesByMovieAndDate(int maPhim, LocalDate date) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT s.*, m.TenPhim, rm.TenPhong, c.TenRap " +
                    "FROM dbo.Showtime s " +
                    "JOIN dbo.Movie m ON s.MaPhim = m.MaPhim " +
                    "JOIN dbo.Room rm ON s.MaPhong = rm.MaPhong " +
                    "JOIN dbo.Cinema c ON rm.MaRap = c.MaRap " +
                    "WHERE s.MaPhim = ? AND CAST(s.NgayChieu AS DATE) = ? " +
                    "ORDER BY s.GioBatDau ASC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhim);
            ps.setDate(2, java.sql.Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRowToShowtime(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    /**
     * Thêm suất chiếu mới
     */
    public boolean addShowtime(Showtime showtime) {
        String sql = "INSERT INTO dbo.Showtime (MaPhim, MaPhong, NgayChieu, GioBatDau, GioKetThuc, GiaVeCoSo, NgonNguAmThanh) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, showtime.getMaPhim());
            ps.setInt(2, showtime.getMaPhong());
            ps.setDate(3, java.sql.Date.valueOf(showtime.getNgayChieu()));
            ps.setTime(4, java.sql.Time.valueOf(showtime.getGioBatDau()));
            ps.setTime(5, java.sql.Time.valueOf(showtime.getGioKetThuc()));
            ps.setBigDecimal(6, showtime.getGiaVeCoSo());
            ps.setString(7, showtime.getNgonNguAmThanh());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật suất chiếu
     */
    public boolean updateShowtime(Showtime showtime) {
        String sql = "UPDATE dbo.Showtime SET MaPhim = ?, MaPhong = ?, NgayChieu = ?, " +
                    "GioBatDau = ?, GioKetThuc = ?, GiaVeCoSo = ?, NgonNguAmThanh = ? " +
                    "WHERE MaSuatChieu = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, showtime.getMaPhim());
            ps.setInt(2, showtime.getMaPhong());
            ps.setDate(3, java.sql.Date.valueOf(showtime.getNgayChieu()));
            ps.setTime(4, java.sql.Time.valueOf(showtime.getGioBatDau()));
            ps.setTime(5, java.sql.Time.valueOf(showtime.getGioKetThuc()));
            ps.setBigDecimal(6, showtime.getGiaVeCoSo());
            ps.setString(7, showtime.getNgonNguAmThanh());
            ps.setInt(8, showtime.getMaSuatChieu());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa suất chiếu
     */
    public boolean deleteShowtime(int maSuatChieu) {
        String sql = "DELETE FROM dbo.Showtime WHERE MaSuatChieu = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map ResultSet row to Showtime object
     */
    private Showtime mapRowToShowtime(ResultSet rs) throws SQLException {
        Showtime showtime = new Showtime();
        showtime.setMaSuatChieu(rs.getInt("MaSuatChieu"));
        showtime.setMaPhim(rs.getInt("MaPhim"));
        showtime.setMaPhong(rs.getInt("MaPhong"));
        
        java.sql.Date sqlDate = rs.getDate("NgayChieu");
        if (sqlDate != null) {
            showtime.setNgayChieu(sqlDate.toLocalDate());
        }
        
        java.sql.Time sqlTime = rs.getTime("GioBatDau");
        if (sqlTime != null) {
            showtime.setGioBatDau(sqlTime.toLocalTime());
        }
        
        sqlTime = rs.getTime("GioKetThuc");
        if (sqlTime != null) {
            showtime.setGioKetThuc(sqlTime.toLocalTime());
        }
        
        showtime.setGiaVeCoSo(rs.getBigDecimal("GiaVeCoSo"));
        showtime.setNgonNguAmThanh(rs.getString("NgonNguAmThanh"));
        
        // Display fields from join
        showtime.setTenPhim(rs.getString("TenPhim"));
        showtime.setTenPhong(rs.getString("TenPhong"));
        showtime.setTenRap(rs.getString("TenRap"));
        
        return showtime;
    }
}
