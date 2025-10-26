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
        String sql = "SELECT s.*, m.TenPhim, rm.TenPhong, c.TenRap "
                + "FROM dbo.Showtime s "
                + "JOIN dbo.Movie m ON s.MaPhim = m.MaPhim "
                + "JOIN dbo.Room rm ON s.MaPhong = rm.MaPhong "
                + "JOIN dbo.Cinema c ON rm.MaRap = c.MaRap "
                + "ORDER BY s.NgayChieu DESC, s.GioBatDau ASC";

        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                showtimes.add(mapRowToShowtime2(rs));
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
        String sql = "SELECT s.*, m.TenPhim, rm.TenPhong, c.TenRap, m.Poster "
                + "FROM dbo.Showtime s "
                + "JOIN dbo.Movie m ON s.MaPhim = m.MaPhim "
                + "JOIN dbo.Room rm ON s.MaPhong = rm.MaPhong "
                + "JOIN dbo.Cinema c ON rm.MaRap = c.MaRap "
                + "WHERE s.MaSuatChieu = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToShowtime2(rs);
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
        String sql = "SELECT s.*, m.TenPhim, rm.TenPhong, c.TenRap, m.Poster\n"
                + "FROM dbo.Showtime s\n"
                + "JOIN dbo.Movie  m  ON s.MaPhim  = m.MaPhim\n"
                + "JOIN dbo.Room   rm ON s.MaPhong = rm.MaPhong\n"
                + "JOIN dbo.Cinema c  ON rm.MaRap  = c.MaRap\n"
                + "WHERE s.MaPhim = ?\n"
                + "  AND s.GioBatDau >= CAST(GETDATE() AS DATE)\n"
                + "ORDER BY CONVERT(date, s.GioBatDau) ASC, s.GioBatDau ASC;";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhim);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRowToShowtime2(rs));
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
        String sql = "SELECT s.*, m.TenPhim, rm.TenPhong, c.TenRap, m.Poster "
                + "FROM dbo.Showtime s "
                + "JOIN dbo.Movie m ON s.MaPhim = m.MaPhim "
                + "JOIN dbo.Room rm ON s.MaPhong = rm.MaPhong "
                + "JOIN dbo.Cinema c ON rm.MaRap = c.MaRap "
                + "WHERE CAST(s.NgayChieu AS DATE) = ? "
                + "ORDER BY s.GioBatDau ASC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRowToShowtime2(rs));
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
        String sql = "SELECT s.*, m.TenPhim, rm.TenPhong, c.TenRap, m.Poster "
                + "FROM dbo.Showtime s "
                + "JOIN dbo.Movie m ON s.MaPhim = m.MaPhim "
                + "JOIN dbo.Room rm ON s.MaPhong = rm.MaPhong "
                + "JOIN dbo.Cinema c ON rm.MaRap = c.MaRap "
                + "WHERE s.MaPhong = ? AND s.NgayChieu >= CAST(GETDATE() AS DATE) "
                + "ORDER BY s.NgayChieu ASC, s.GioBatDau ASC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRowToShowtime2(rs));
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
        String sql = "SELECT s.*, m.TenPhim, rm.TenPhong, c.TenRap, m.Poster "
                + "FROM dbo.Showtime s "
                + "JOIN dbo.Movie m ON s.MaPhim = m.MaPhim "
                + "JOIN dbo.Room rm ON s.MaPhong = rm.MaPhong "
                + "JOIN dbo.Cinema c ON rm.MaRap = c.MaRap "
                + "WHERE s.MaPhim = ? AND CAST(s.NgayChieu AS DATE) = ? "
                + "ORDER BY s.GioBatDau ASC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhim);
            ps.setDate(2, java.sql.Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRowToShowtime2(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    /**
     * Lấy suất chiếu theo phim và rạp
     */
    public List<Showtime> getShowtimesByMovieAndCinema(int maPhim, int maRap) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT s.*, m.TenPhim, rm.TenPhong, c.TenRap, m.Poster "
                + "FROM dbo.Showtime s "
                + "JOIN dbo.Movie m ON s.MaPhim = m.MaPhim "
                + "JOIN dbo.Room rm ON s.MaPhong = rm.MaPhong "
                + "JOIN dbo.Cinema c ON rm.MaRap = c.MaRap "
                + "WHERE s.MaPhim = ? AND c.MaRap = ? AND s.NgayChieu >= CAST(GETDATE() AS DATE) "
                + "ORDER BY s.NgayChieu ASC, s.GioBatDau ASC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhim);
            ps.setInt(2, maRap);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRowToShowtime2(rs));
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
        String sql = "INSERT INTO dbo.Showtime (MaPhim, MaPhong, NgayChieu, GioBatDau, GioKetThuc, GiaVeCoSo, NgonNguAmThanh) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // Sử dụng MaPhim = 1 làm phim mặc định nếu chưa gán phim
            ps.setInt(1, showtime.getMaPhim() > 0 ? showtime.getMaPhim() : 1);
            ps.setInt(2, showtime.getMaPhong());
            ps.setTimestamp(3, showtime.getNgayChieu() != null
                    ? java.sql.Timestamp.valueOf(showtime.getNgayChieu()) : null);
            ps.setTimestamp(4, showtime.getGioBatDau() != null
                    ? java.sql.Timestamp.valueOf(showtime.getGioBatDau()) : null);
            ps.setTimestamp(5, showtime.getGioKetThuc() != null
                    ? java.sql.Timestamp.valueOf(showtime.getGioKetThuc()) : null);
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
        String sql = "UPDATE dbo.Showtime SET MaPhim = ?, MaPhong = ?, NgayChieu = ?, "
                + "GioBatDau = ?, GioKetThuc = ?, GiaVeCoSo = ?, NgonNguAmThanh = ? "
                + "WHERE MaSuatChieu = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, showtime.getMaPhim());
            ps.setInt(2, showtime.getMaPhong());
            ps.setTimestamp(3, showtime.getNgayChieu() != null
                    ? java.sql.Timestamp.valueOf(showtime.getNgayChieu()) : null);
            ps.setTimestamp(4, showtime.getGioBatDau() != null
                    ? java.sql.Timestamp.valueOf(showtime.getGioBatDau()) : null);
            ps.setTimestamp(5, showtime.getGioKetThuc() != null
                    ? java.sql.Timestamp.valueOf(showtime.getGioKetThuc()) : null);
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
     * Lấy suất chiếu theo phòng và ngày
     */
    public List<Showtime> getShowtimesByRoomAndDate(int maPhong, LocalDate date) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT s.*, m.TenPhim, rm.TenPhong, c.TenRap "
                + "FROM dbo.Showtime s "
                + "JOIN dbo.Movie m ON s.MaPhim = m.MaPhim "
                + "JOIN dbo.Room rm ON s.MaPhong = rm.MaPhong "
                + "JOIN dbo.Cinema c ON rm.MaRap = c.MaRap "
                + "WHERE s.MaPhong = ? AND CAST(s.NgayChieu AS DATE) = ? "
                + "ORDER BY s.GioBatDau ASC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
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
     * Kiểm tra xem có suất chiếu trùng giờ không
     */
    public boolean hasConflictingShowtime(int maPhong, java.time.LocalDate ngayChieu, java.time.LocalTime gioBatDau, java.time.LocalTime gioKetThuc, int excludeMaSuatChieu) {
        String sql = "SELECT COUNT(*) FROM dbo.Showtime "
                + "WHERE MaPhong = ? AND CAST(NgayChieu AS DATE) = DATEADD(day, -2, ?) "
                + "AND MaSuatChieu != ? "
                + "AND NOT (GioKetThuc <= ? OR GioBatDau >= ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            ps.setDate(2, java.sql.Date.valueOf(ngayChieu));
            ps.setInt(3, excludeMaSuatChieu);
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(ngayChieu.atTime(gioBatDau)));   // start1
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(ngayChieu.atTime(gioKetThuc)));  // end2

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("=== DEBUG hasConflictingShowtime ===");
                    System.out.println("MaPhong: " + maPhong);
                    System.out.println("NgayChieu: " + ngayChieu);
                    System.out.println("GioBatDau: " + gioBatDau);
                    System.out.println("GioKetThuc: " + gioKetThuc);
                    System.out.println("ExcludeMaSuatChieu: " + excludeMaSuatChieu);
                    System.out.println("Conflict count: " + count);
                    System.out.println("Has conflict: " + (count > 0));
                    System.out.println("===================================");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Lấy thông tin suất chiếu xung đột
     */
    public Showtime getConflictingShowtime(int maPhong, java.time.LocalDate ngayChieu, java.time.LocalTime gioBatDau, java.time.LocalTime gioKetThuc, int excludeMaSuatChieu) {
        // Logic kiểm tra xung đột: hai khoảng thời gian xung đột nếu chúng chồng lấn nhau
        // Xung đột khi: (start1 < end2) AND (start2 < end1)
        String sql = "SELECT s.*, m.TenPhim FROM dbo.Showtime s "
                + "INNER JOIN dbo.Movie m ON s.MaPhim = m.MaPhim "
                + "WHERE s.MaPhong = ? AND CAST(s.NgayChieu AS DATE) = DATEADD(day, -2, ?) "
                + "AND s.MaSuatChieu != ? "
                + "AND NOT (s.GioKetThuc <= ? OR s.GioBatDau >= ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            ps.setDate(2, java.sql.Date.valueOf(ngayChieu));
            ps.setInt(3, excludeMaSuatChieu);
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(ngayChieu.atTime(gioBatDau)));   // start1
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(ngayChieu.atTime(gioKetThuc)));  // end2

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("=== DEBUG getConflictingShowtime ===");
                System.out.println("Searching for conflicts with: " + ngayChieu + " " + gioBatDau + "-" + gioKetThuc);
                
                while (rs.next()) {
                    // Debug: In ra dữ liệu raw từ database
                    System.out.println("Found conflict:");
                    System.out.println("Raw NgayChieu from DB: " + rs.getString("NgayChieu"));
                    System.out.println("Raw GioBatDau from DB: " + rs.getString("GioBatDau"));
                    System.out.println("Raw GioKetThuc from DB: " + rs.getString("GioKetThuc"));
                    System.out.println("TenPhim from DB: " + rs.getString("TenPhim"));
                    
                    Showtime showtime = mapRowToShowtime(rs);
                    
                    // Debug: In ra dữ liệu sau khi parse và fix timezone
                    System.out.println("After mapRowToShowtime:");
                    System.out.println("NgayChieu: " + showtime.getNgayChieu());
                    System.out.println("GioBatDau: " + showtime.getGioBatDau());
                    System.out.println("GioKetThuc: " + showtime.getGioKetThuc());
                    System.out.println("-------------------------------------");
                    
                    // Thêm tên phim vào showtime để hiển thị
                    showtime.setTenPhim(rs.getString("TenPhim"));
                    return showtime; // Trả về suất chiếu xung đột đầu tiên
                }
                System.out.println("No conflicts found");
                System.out.println("=====================================");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tạo suất chiếu hàng loạt cho nhiều ngày
     */
    public boolean bulkCreateShowtimes(List<Showtime> showtimes) {
        String sql = "INSERT INTO dbo.Showtime (MaPhim, MaPhong, NgayChieu, GioBatDau, GioKetThuc, GiaVeCoSo, NgonNguAmThanh) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Showtime showtime : showtimes) {
                ps.setInt(1, showtime.getMaPhim());
                ps.setInt(2, showtime.getMaPhong());
                ps.setTimestamp(3, showtime.getNgayChieu() != null
                        ? java.sql.Timestamp.valueOf(showtime.getNgayChieu()) : null);
                ps.setTimestamp(4, showtime.getGioBatDau() != null
                        ? java.sql.Timestamp.valueOf(showtime.getGioBatDau()) : null);
                ps.setTimestamp(5, showtime.getGioKetThuc() != null
                        ? java.sql.Timestamp.valueOf(showtime.getGioKetThuc()) : null);
                ps.setBigDecimal(6, showtime.getGiaVeCoSo());
                ps.setString(7, showtime.getNgonNguAmThanh());
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy tất cả suất chiếu trong khoảng thời gian
     */
    public List<Showtime> getShowtimesInDateRange(LocalDate startDate, LocalDate endDate) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT s.*, m.TenPhim, rm.TenPhong, c.TenRap, m.Poster "
                + "FROM dbo.Showtime s "
                + "JOIN dbo.Movie m ON s.MaPhim = m.MaPhim "
                + "JOIN dbo.Room rm ON s.MaPhong = rm.MaPhong "
                + "JOIN dbo.Cinema c ON rm.MaRap = c.MaRap "
                + "WHERE CAST(s.NgayChieu AS DATE) BETWEEN ? AND ? "
                + "ORDER BY s.NgayChieu ASC, s.GioBatDau ASC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    showtimes.add(mapRowToShowtime2(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }

    /**
     * Map ResultSet row to Showtime object
     */
    private Showtime mapRowToShowtime(ResultSet rs) throws SQLException {
        Showtime showtime = new Showtime();
        showtime.setMaSuatChieu(rs.getInt("MaSuatChieu"));
        showtime.setMaPhim(rs.getInt("MaPhim"));
        showtime.setMaPhong(rs.getInt("MaPhong"));
//        showtime.setPoster(rs.getString("Poster"));

        // Đọc trực tiếp từ database để tránh timezone issues
        try {
            // Đọc raw string từ database
            String rawNgayChieu = rs.getString("NgayChieu");
            String rawGioBatDau = rs.getString("GioBatDau");
            String rawGioKetThuc = rs.getString("GioKetThuc");
            if (rawNgayChieu != null && !rawNgayChieu.isEmpty()) {
                // Loại bỏ phần microseconds
                String cleanDate = rawNgayChieu.replaceAll("\\.\\d+", "");
                // Parse thành LocalDateTime và cộng thêm 2 ngày để sửa lỗi timezone
                java.time.LocalDateTime parsedDate = java.time.LocalDateTime.parse(cleanDate.replace(" ", "T"));
                showtime.setNgayChieu(parsedDate.plusDays(2));
            }

            // Xử lý GioBatDau và GioKetThuc - lấy ngày từ NgayChieu và giờ từ GioBatDau/GioKetThuc
            if (rawGioBatDau != null && !rawGioBatDau.isEmpty() && rawNgayChieu != null && !rawNgayChieu.isEmpty()) {
                String cleanDate = rawNgayChieu.replaceAll("\\.\\d+", "");
                String cleanTime = rawGioBatDau.replaceAll("\\.\\d+", "");

                // Lấy ngày từ NgayChieu và giờ từ GioBatDau, cộng thêm 2 ngày
                java.time.LocalDate date = java.time.LocalDate.parse(cleanDate.split(" ")[0]).plusDays(2);
                java.time.LocalTime time = java.time.LocalTime.parse(cleanTime.split(" ")[1]);

                showtime.setGioBatDau(java.time.LocalDateTime.of(date, time));
                System.out.println("DEBUG - Parsed GioBatDau: " + showtime.getGioBatDau());
            }

            if (rawGioKetThuc != null && !rawGioKetThuc.isEmpty() && rawNgayChieu != null && !rawNgayChieu.isEmpty()) {
                String cleanDate = rawNgayChieu.replaceAll("\\.\\d+", "");
                String cleanTime = rawGioKetThuc.replaceAll("\\.\\d+", "");

                // Lấy ngày từ NgayChieu và giờ từ GioKetThuc, cộng thêm 2 ngày
                java.time.LocalDate date = java.time.LocalDate.parse(cleanDate.split(" ")[0]).plusDays(2);
                java.time.LocalTime time = java.time.LocalTime.parse(cleanTime.split(" ")[1]);

                showtime.setGioKetThuc(java.time.LocalDateTime.of(date, time));
                System.out.println("DEBUG - Parsed GioKetThuc: " + showtime.getGioKetThuc());
            }
        } catch (Exception e) {
            System.err.println("Error reading timestamp: " + e.getMessage());
            // Fallback: đọc như string và parse
            try {
                String ngayChieuStr = rs.getString("NgayChieu");
                if (ngayChieuStr != null && !ngayChieuStr.isEmpty()) {
                    showtime.setNgayChieu(java.time.LocalDateTime.parse(ngayChieuStr.replace(" ", "T")));
                }

                String gioBatDauStr = rs.getString("GioBatDau");
                if (gioBatDauStr != null && !gioBatDauStr.isEmpty()) {
                    showtime.setGioBatDau(java.time.LocalDateTime.parse(gioBatDauStr.replace(" ", "T")));
                }

                String gioKetThucStr = rs.getString("GioKetThuc");
                if (gioKetThucStr != null && !gioKetThucStr.isEmpty()) {
                    showtime.setGioKetThuc(java.time.LocalDateTime.parse(gioKetThucStr.replace(" ", "T")));
                }
            } catch (Exception e2) {
                System.err.println("Error parsing datetime string: " + e2.getMessage());
            }
        }

        showtime.setGiaVeCoSo(rs.getBigDecimal("GiaVeCoSo"));
        showtime.setNgonNguAmThanh(rs.getString("NgonNguAmThanh"));

        // Display fields from join
        showtime.setTenPhim(rs.getString("TenPhim"));
        showtime.setTenPhong(rs.getString("TenPhong"));
        showtime.setTenRap(rs.getString("TenRap"));

        return showtime;
    }

    private Showtime mapRowToShowtime2(ResultSet rs) throws SQLException {
        Showtime showtime = new Showtime();
        showtime.setMaSuatChieu(rs.getInt("MaSuatChieu"));
        showtime.setMaPhim(rs.getInt("MaPhim"));
        showtime.setMaPhong(rs.getInt("MaPhong"));
        showtime.setPoster(rs.getString("Poster"));

        // Đọc trực tiếp từ database để tránh timezone issues
        try {
            // Đọc raw string từ database
            String rawNgayChieu = rs.getString("NgayChieu");
            String rawGioBatDau = rs.getString("GioBatDau");
            String rawGioKetThuc = rs.getString("GioKetThuc");
            if (rawNgayChieu != null && !rawNgayChieu.isEmpty()) {
                // Loại bỏ phần microseconds
                String cleanDate = rawNgayChieu.replaceAll("\\.\\d+", "");
                // Parse thành LocalDateTime và cộng thêm 2 ngày để sửa lỗi timezone
                java.time.LocalDateTime parsedDate = java.time.LocalDateTime.parse(cleanDate.replace(" ", "T"));
                showtime.setNgayChieu(parsedDate.plusDays(2));
            }

            // Xử lý GioBatDau và GioKetThuc - lấy ngày từ NgayChieu và giờ từ GioBatDau/GioKetThuc
            if (rawGioBatDau != null && !rawGioBatDau.isEmpty() && rawNgayChieu != null && !rawNgayChieu.isEmpty()) {
                String cleanDate = rawNgayChieu.replaceAll("\\.\\d+", "");
                String cleanTime = rawGioBatDau.replaceAll("\\.\\d+", "");

                // Lấy ngày từ NgayChieu và giờ từ GioBatDau, cộng thêm 2 ngày
                java.time.LocalDate date = java.time.LocalDate.parse(cleanDate.split(" ")[0]).plusDays(2);
                java.time.LocalTime time = java.time.LocalTime.parse(cleanTime.split(" ")[1]);

                showtime.setGioBatDau(java.time.LocalDateTime.of(date, time));
                System.out.println("DEBUG - Parsed GioBatDau: " + showtime.getGioBatDau());
            }

            if (rawGioKetThuc != null && !rawGioKetThuc.isEmpty() && rawNgayChieu != null && !rawNgayChieu.isEmpty()) {
                String cleanDate = rawNgayChieu.replaceAll("\\.\\d+", "");
                String cleanTime = rawGioKetThuc.replaceAll("\\.\\d+", "");

                // Lấy ngày từ NgayChieu và giờ từ GioKetThuc, cộng thêm 2 ngày
                java.time.LocalDate date = java.time.LocalDate.parse(cleanDate.split(" ")[0]).plusDays(2);
                java.time.LocalTime time = java.time.LocalTime.parse(cleanTime.split(" ")[1]);

                showtime.setGioKetThuc(java.time.LocalDateTime.of(date, time));
                System.out.println("DEBUG - Parsed GioKetThuc: " + showtime.getGioKetThuc());
            }
        } catch (Exception e) {
            System.err.println("Error reading timestamp: " + e.getMessage());
            // Fallback: đọc như string và parse
            try {
                String ngayChieuStr = rs.getString("NgayChieu");
                if (ngayChieuStr != null && !ngayChieuStr.isEmpty()) {
                    showtime.setNgayChieu(java.time.LocalDateTime.parse(ngayChieuStr.replace(" ", "T")));
                }

                String gioBatDauStr = rs.getString("GioBatDau");
                if (gioBatDauStr != null && !gioBatDauStr.isEmpty()) {
                    showtime.setGioBatDau(java.time.LocalDateTime.parse(gioBatDauStr.replace(" ", "T")));
                }

                String gioKetThucStr = rs.getString("GioKetThuc");
                if (gioKetThucStr != null && !gioKetThucStr.isEmpty()) {
                    showtime.setGioKetThuc(java.time.LocalDateTime.parse(gioKetThucStr.replace(" ", "T")));
                }
            } catch (Exception e2) {
                System.err.println("Error parsing datetime string: " + e2.getMessage());
            }
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
