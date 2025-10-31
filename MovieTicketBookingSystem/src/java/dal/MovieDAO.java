package dal;

import model.Movie;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO extends DBContext {

    /**
     * Lấy tất cả phim
     */
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Movie ORDER BY NgayKhoiChieu DESC";

        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                movies.add(mapRowToMovie(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    /**
     * Lấy phim theo ID
     */
    public Movie getMovieById(int maPhim) {
        String sql = "SELECT * FROM dbo.Movie WHERE MaPhim = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhim);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToMovie(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy phim đang chiếu (sắp tới hoặc đang chiếu)
     */
    public List<Movie> getUpcomingMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Movie WHERE NgayKhoiChieu IS NOT NULL "
                + "AND NgayKhoiChieu >= CAST(GETDATE() AS DATE) "
                + "ORDER BY NgayKhoiChieu ASC";

        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                movies.add(mapRowToMovie(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    /**
     * Tìm kiếm phim theo tên
     */
    /**
     * Tìm kiếm phim theo tên (đơn giản)
     */
    public List<Movie> searchMovies(String keyword) {
    List<Movie> list = new ArrayList<>();
    String sql = """
        SELECT * FROM Movie WHERE 
        TenPhim LIKE ? OR TheLoai LIKE ? OR DaoDien LIKE ? OR DienVien LIKE ? 
        OR NoiDung LIKE ? OR NgonNguPhuDe LIKE ? OR LoaiPhim LIKE ?
        OR CAST(DoTuoiGioiHan AS VARCHAR) LIKE ? OR CAST(ThoiLuong AS VARCHAR) LIKE ?
        OR TrangThai LIKE ? OR CONVERT(VARCHAR, NgayKhoiChieu, 103) LIKE ?
        AND TrangThai != 'Ngừng chiếu'
        ORDER BY NgayKhoiChieu DESC
        """;

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        String like = "%" + keyword + "%";
        for (int i = 1; i <= 11; i++) ps.setString(i, like);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRowToMovie(rs));
        }
    } catch (SQLException e) { e.printStackTrace(); }
    return list;
}

    /**
     * Lấy phim theo thể loại
     */
    public List<Movie> getMoviesByCategory(String theLoai) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Movie WHERE TheLoai = ? ORDER BY NgayKhoiChieu DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, theLoai);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movies.add(mapRowToMovie(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    /**
     * Thêm phim mới
     */
    public boolean addMovie(Movie movie) {
        String sql = "INSERT INTO dbo.Movie (TenPhim, TheLoai, LoaiPhim, DaoDien, DienVien, "
                + "DoTuoiGioiHan, ThoiLuong, NoiDung, NgayKhoiChieu, Poster, NgonNguPhuDe, TrangThai) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, movie.getTenPhim());
            ps.setString(2, movie.getTheLoai());
            ps.setString(3, movie.getLoaiPhim());
            ps.setString(4, movie.getDaoDien());
            ps.setString(5, movie.getDienVien());
            ps.setInt(6, movie.getDoTuoiGioiHan());
            ps.setInt(7, movie.getThoiLuong());
            ps.setString(8, movie.getNoiDung());
            ps.setTimestamp(9, movie.getNgayKhoiChieu() != null
                    ? java.sql.Timestamp.valueOf(movie.getNgayKhoiChieu()) : null);
            ps.setString(10, movie.getPoster());
            ps.setString(11, movie.getNgonNguPhuDe());
            ps.setString(12, movie.getTrangThai() != null ? movie.getTrangThai() : "Sắp chiếu");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật phim
     */
    public boolean updateMovie(Movie movie) {
        String sql = "UPDATE dbo.Movie SET TenPhim = ?, TheLoai = ?, LoaiPhim = ?, "
                + "DaoDien = ?, DienVien = ?, DoTuoiGioiHan = ?, ThoiLuong = ?, "
                + "NoiDung = ?, NgayKhoiChieu = ?, Poster = ?, NgonNguPhuDe = ?, TrangThai = ? "
                + "WHERE MaPhim = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, movie.getTenPhim());
            ps.setString(2, movie.getTheLoai());
            ps.setString(3, movie.getLoaiPhim());
            ps.setString(4, movie.getDaoDien());
            ps.setString(5, movie.getDienVien());
            ps.setInt(6, movie.getDoTuoiGioiHan());
            ps.setInt(7, movie.getThoiLuong());
            ps.setString(8, movie.getNoiDung());
            ps.setTimestamp(9, movie.getNgayKhoiChieu() != null
                    ? java.sql.Timestamp.valueOf(movie.getNgayKhoiChieu()) : null);
            ps.setString(10, movie.getPoster());
            ps.setString(11, movie.getNgonNguPhuDe());
            ps.setString(12, movie.getTrangThai() != null ? movie.getTrangThai() : "Sắp chiếu");
            ps.setInt(13, movie.getMaPhim());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa phim (soft delete - chỉ cập nhật trạng thái)
     */
    public boolean deleteMovie(int maPhim) {
        String sql = "UPDATE dbo.Movie SET TrangThai = 'Ngừng chiếu' WHERE MaPhim = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhim);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map ResultSet row to Movie object
     */
    private Movie mapRowToMovie(ResultSet rs) throws SQLException {
        Movie movie = new Movie();
        movie.setMaPhim(rs.getInt("MaPhim"));
        movie.setTenPhim(rs.getString("TenPhim"));
        movie.setTheLoai(rs.getString("TheLoai"));
        movie.setLoaiPhim(rs.getString("LoaiPhim"));
        movie.setDaoDien(rs.getString("DaoDien"));
        movie.setDienVien(rs.getString("DienVien"));
        movie.setDoTuoiGioiHan(rs.getInt("DoTuoiGioiHan"));
        movie.setThoiLuong(rs.getInt("ThoiLuong"));
        movie.setNoiDung(rs.getString("NoiDung"));

        java.sql.Timestamp sqlNgayKhoiChieu = rs.getTimestamp("NgayKhoiChieu");
        if (sqlNgayKhoiChieu != null) {
            movie.setNgayKhoiChieu(sqlNgayKhoiChieu.toLocalDateTime());
        }

        movie.setPoster(rs.getString("Poster"));
        movie.setNgonNguPhuDe(rs.getString("NgonNguPhuDe"));
        movie.setTrangThai(rs.getString("TrangThai"));

        java.sql.Timestamp sqlTs = rs.getTimestamp("NgayTao");
        if (sqlTs != null) {
            movie.setNgayTao(sqlTs.toLocalDateTime());
        }

        sqlTs = rs.getTimestamp("NgayCapNhat");
        if (sqlTs != null) {
            movie.setNgayCapNhat(sqlTs.toLocalDateTime());
        }

        return movie;
    }
}
