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
        String sql = "SELECT MaPhim, TenPhim, LoaiPhim, DienVien, DoTuoiGioiHan, ThoiLuong, NoiDung, " +
                    "CONVERT(VARCHAR(23), NgayKhoiChieu, 120) as NgayKhoiChieu, Poster, NgonNguPhuDe, TrangThai, " +
                    "CONVERT(VARCHAR(23), NgayTao, 120) as NgayTao, CONVERT(VARCHAR(23), NgayCapNhat, 120) as NgayCapNhat " +
                    "FROM dbo.Movie ORDER BY NgayKhoiChieu DESC";
        
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
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
        String sql = "SELECT MaPhim, TenPhim, LoaiPhim, DienVien, DoTuoiGioiHan, ThoiLuong, NoiDung, " +
                    "CONVERT(VARCHAR(23), NgayKhoiChieu, 120) as NgayKhoiChieu, Poster, NgonNguPhuDe, TrangThai, " +
                    "CONVERT(VARCHAR(23), NgayTao, 120) as NgayTao, CONVERT(VARCHAR(23), NgayCapNhat, 120) as NgayCapNhat " +
                    "FROM dbo.Movie WHERE MaPhim = ?";
        
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
     * Lấy phim đang chiếu (sắp tới hoặc đang chiếu) - Dựa trên ngày khởi chiếu
     * Dùng cho logic cũ, không dùng cho home page
     */
    public List<Movie> getUpcomingMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT MaPhim, TenPhim, LoaiPhim, DienVien, DoTuoiGioiHan, ThoiLuong, NoiDung, " +
                    "CONVERT(VARCHAR(23), NgayKhoiChieu, 120) as NgayKhoiChieu, Poster, NgonNguPhuDe, TrangThai, " +
                    "CONVERT(VARCHAR(23), NgayTao, 120) as NgayTao, CONVERT(VARCHAR(23), NgayCapNhat, 120) as NgayCapNhat " +
                    "FROM dbo.Movie WHERE NgayKhoiChieu IS NOT NULL " +
                    "AND NgayKhoiChieu >= CAST(GETDATE() AS DATE) " +
                    "ORDER BY NgayKhoiChieu ASC";
        
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                movies.add(mapRowToMovie(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
    
    /**
     * Lấy phim đang chiếu (dựa trên TrangThai = 'Đang chiếu')
     * Dùng cho home page
     */
    public List<Movie> getNowShowingMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT MaPhim, TenPhim, LoaiPhim, DienVien, DoTuoiGioiHan, ThoiLuong, NoiDung, " +
                    "CONVERT(VARCHAR(23), NgayKhoiChieu, 120) as NgayKhoiChieu, Poster, NgonNguPhuDe, TrangThai, " +
                    "CONVERT(VARCHAR(23), NgayTao, 120) as NgayTao, CONVERT(VARCHAR(23), NgayCapNhat, 120) as NgayCapNhat " +
                    "FROM dbo.Movie WHERE TrangThai = ? " +
                    "ORDER BY NgayKhoiChieu DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Đang chiếu");
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
     * Lấy phim sắp chiếu (dựa trên TrangThai = 'Sắp chiếu')
     * Dùng cho home page
     */
    public List<Movie> getComingSoonMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT MaPhim, TenPhim, LoaiPhim, DienVien, DoTuoiGioiHan, ThoiLuong, NoiDung, " +
                    "CONVERT(VARCHAR(23), NgayKhoiChieu, 120) as NgayKhoiChieu, Poster, NgonNguPhuDe, TrangThai, " +
                    "CONVERT(VARCHAR(23), NgayTao, 120) as NgayTao, CONVERT(VARCHAR(23), NgayCapNhat, 120) as NgayCapNhat " +
                    "FROM dbo.Movie WHERE TrangThai = ? " +
                    "ORDER BY NgayKhoiChieu ASC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Sắp chiếu");
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
     * Tìm kiếm phim theo tên
     */
    public List<Movie> searchMovies(String keyword) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT MaPhim, TenPhim, LoaiPhim, DienVien, DoTuoiGioiHan, ThoiLuong, NoiDung, " +
                    "CONVERT(VARCHAR(23), NgayKhoiChieu, 120) as NgayKhoiChieu, Poster, NgonNguPhuDe, TrangThai, " +
                    "CONVERT(VARCHAR(23), NgayTao, 120) as NgayTao, CONVERT(VARCHAR(23), NgayCapNhat, 120) as NgayCapNhat " +
                    "FROM dbo.Movie WHERE TenPhim LIKE ? ORDER BY NgayKhoiChieu DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
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
     * Lấy phim theo MaTheLoai qua bảng liên kết
     */
    public List<Movie> getMoviesByGenreId(int genreId) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT m.MaPhim, m.TenPhim, m.LoaiPhim, m.DienVien, m.DoTuoiGioiHan, m.ThoiLuong, m.NoiDung, " +
                "CONVERT(VARCHAR(23), m.NgayKhoiChieu, 120) as NgayKhoiChieu, m.Poster, m.NgonNguPhuDe, m.TrangThai, " +
                "CONVERT(VARCHAR(23), m.NgayTao, 120) as NgayTao, CONVERT(VARCHAR(23), m.NgayCapNhat, 120) as NgayCapNhat " +
                "FROM dbo.Movie m JOIN dbo.MovieGenre mg ON m.MaPhim = mg.MaPhim " +
                "WHERE mg.MaTheLoai = ? " +
                "ORDER BY m.NgayKhoiChieu DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, genreId);
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
     * Lấy phim theo MaDaoDien qua bảng liên kết
     */
    public List<Movie> getMoviesByDirectorId(int directorId) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT m.MaPhim, m.TenPhim, m.LoaiPhim, m.DienVien, m.DoTuoiGioiHan, m.ThoiLuong, m.NoiDung, " +
                "CONVERT(VARCHAR(23), m.NgayKhoiChieu, 120) as NgayKhoiChieu, m.Poster, m.NgonNguPhuDe, m.TrangThai, " +
                "CONVERT(VARCHAR(23), m.NgayTao, 120) as NgayTao, CONVERT(VARCHAR(23), m.NgayCapNhat, 120) as NgayCapNhat " +
                "FROM dbo.Movie m JOIN dbo.MovieDirector md ON m.MaPhim = md.MaPhim " +
                "WHERE md.MaDaoDien = ? " +
                "ORDER BY m.NgayKhoiChieu DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, directorId);
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
     * Lấy phim theo thể loại
     */
    public List<Movie> getMoviesByCategory(String theLoai) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT MaPhim, TenPhim, LoaiPhim, DienVien, DoTuoiGioiHan, ThoiLuong, NoiDung, " +
                    "CONVERT(VARCHAR(23), NgayKhoiChieu, 120) as NgayKhoiChieu, Poster, NgonNguPhuDe, TrangThai, " +
                    "CONVERT(VARCHAR(23), NgayTao, 120) as NgayTao, CONVERT(VARCHAR(23), NgayCapNhat, 120) as NgayCapNhat " +
                    "FROM dbo.Movie WHERE 1=0 ORDER BY NgayKhoiChieu DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // TheLoai đã tách sang bảng Genre - phương thức cũ không còn dùng
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
        String sql = "INSERT INTO dbo.Movie (TenPhim, LoaiPhim, DienVien, " +
                    "DoTuoiGioiHan, ThoiLuong, NoiDung, NgayKhoiChieu, Poster, NgonNguPhuDe, TrangThai) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, movie.getTenPhim());
            ps.setString(2, movie.getLoaiPhim());
            ps.setString(3, movie.getDienVien());
            ps.setInt(4, movie.getDoTuoiGioiHan());
            ps.setInt(5, movie.getThoiLuong());
            ps.setString(6, movie.getNoiDung());
            ps.setTimestamp(7, movie.getNgayKhoiChieu() != null ? 
                java.sql.Timestamp.valueOf(movie.getNgayKhoiChieu()) : null);
            ps.setString(8, movie.getPoster());
            ps.setString(9, movie.getNgonNguPhuDe());
            ps.setString(10, movie.getTrangThai() != null ? movie.getTrangThai() : "Sắp chiếu");
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Lấy MaPhim được tạo tự động
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        movie.setMaPhim(generatedKeys.getInt(1));
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
     * Cập nhật phim
     */
    public boolean updateMovie(Movie movie) {
        String sql = "UPDATE dbo.Movie SET TenPhim = ?, LoaiPhim = ?, " +
                    "DienVien = ?, DoTuoiGioiHan = ?, ThoiLuong = ?, " +
                    "NoiDung = ?, NgayKhoiChieu = ?, Poster = ?, NgonNguPhuDe = ?, TrangThai = ? " +
                    "WHERE MaPhim = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, movie.getTenPhim());
            ps.setString(2, movie.getLoaiPhim());
            ps.setString(3, movie.getDienVien());
            ps.setInt(4, movie.getDoTuoiGioiHan());
            ps.setInt(5, movie.getThoiLuong());
            ps.setString(6, movie.getNoiDung());
            ps.setTimestamp(7, movie.getNgayKhoiChieu() != null ? 
                java.sql.Timestamp.valueOf(movie.getNgayKhoiChieu()) : null);
            ps.setString(8, movie.getPoster());
            ps.setString(9, movie.getNgonNguPhuDe());
            ps.setString(10, movie.getTrangThai() != null ? movie.getTrangThai() : "Sắp chiếu");
            ps.setInt(11, movie.getMaPhim());
            
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
        String sql = "UPDATE dbo.Movie SET TrangThai = N'Ngừng chiếu' WHERE MaPhim = ?";
        
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
        movie.setLoaiPhim(rs.getString("LoaiPhim"));
        movie.setDienVien(rs.getString("DienVien"));
        movie.setDoTuoiGioiHan(rs.getInt("DoTuoiGioiHan"));
        movie.setThoiLuong(rs.getInt("ThoiLuong"));
        movie.setNoiDung(rs.getString("NoiDung"));
        
        // Parse NgayKhoiChieu from string
        String ngayKhoiChieuStr = rs.getString("NgayKhoiChieu");
        if (ngayKhoiChieuStr != null && !ngayKhoiChieuStr.trim().isEmpty()) {
            try {
                // Format: yyyy-MM-dd HH:mm:ss
                String cleaned = ngayKhoiChieuStr.trim().replace(" ", "T");
                if (cleaned.length() > 19) {
                    cleaned = cleaned.substring(0, 19);
                }
                movie.setNgayKhoiChieu(LocalDateTime.parse(cleaned));
            } catch (Exception e) {
                System.out.println("[MovieDAO] Error parsing NgayKhoiChieu: " + ngayKhoiChieuStr + " - " + e.getMessage());
            }
        }
        
        movie.setPoster(rs.getString("Poster"));
        movie.setNgonNguPhuDe(rs.getString("NgonNguPhuDe"));
        movie.setTrangThai(rs.getString("TrangThai"));
        
        // Parse NgayTao from string
        String ngayTaoStr = rs.getString("NgayTao");
        if (ngayTaoStr != null && !ngayTaoStr.trim().isEmpty()) {
            try {
                String cleaned = ngayTaoStr.trim().replace(" ", "T");
                if (cleaned.length() > 19) {
                    cleaned = cleaned.substring(0, 19);
                }
                movie.setNgayTao(LocalDateTime.parse(cleaned));
            } catch (Exception e) {
                System.out.println("[MovieDAO] Error parsing NgayTao: " + ngayTaoStr + " - " + e.getMessage());
            }
        }
        
        // Parse NgayCapNhat from string
        String ngayCapNhatStr = rs.getString("NgayCapNhat");
        if (ngayCapNhatStr != null && !ngayCapNhatStr.trim().isEmpty()) {
            try {
                String cleaned = ngayCapNhatStr.trim().replace(" ", "T");
                if (cleaned.length() > 19) {
                    cleaned = cleaned.substring(0, 19);
                }
                movie.setNgayCapNhat(LocalDateTime.parse(cleaned));
            } catch (Exception e) {
                System.out.println("[MovieDAO] Error parsing NgayCapNhat: " + ngayCapNhatStr + " - " + e.getMessage());
            }
        }
        
        return movie;
    }
}
