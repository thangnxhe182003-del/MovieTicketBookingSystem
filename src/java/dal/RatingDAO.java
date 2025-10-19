package dal;

import model.Rating;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingDAO extends DBContext {

    public Double getAverageRatingByMovie(int maPhim) {
        String sql = "SELECT AVG(CAST(Rating AS FLOAT)) AS avgRating FROM dbo.DanhGia WHERE MaPhim = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhim);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double avg = rs.getDouble("avgRating");
                    return rs.wasNull() ? null : avg;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getRatingCountByMovie(int maPhim) {
        String sql = "SELECT COUNT(*) AS cnt FROM dbo.DanhGia WHERE MaPhim = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhim);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Rating> getRatingsByMovie(int maPhim, int limit) {
        List<Rating> ratings = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM dbo.DanhGia WHERE MaPhim = ? ORDER BY DateDanhGia DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, maPhim);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ratings.add(mapRowToRating(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratings;
    }

    public Rating getUserRatingForMovie(int maKH, int maPhim) {
        String sql = "SELECT * FROM dbo.DanhGia WHERE MaKH = ? AND MaPhim = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maKH);
            ps.setInt(2, maPhim);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToRating(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean upsertRating(Rating rating) {
        // Try update first (unique constraint MaKH+MaPhim)
        String update = "UPDATE dbo.DanhGia SET Rating = ?, Comment = ?, DateDanhGia = SYSUTCDATETIME() WHERE MaKH = ? AND MaPhim = ?";
        try (PreparedStatement ps = connection.prepareStatement(update)) {
            ps.setInt(1, rating.getRating());
            ps.setString(2, rating.getComment());
            ps.setInt(3, rating.getMaKH());
            ps.setInt(4, rating.getMaPhim());
            int rows = ps.executeUpdate();
            if (rows > 0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String insert = "INSERT INTO dbo.DanhGia (MaKH, MaPhim, Rating, Comment) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            ps.setInt(1, rating.getMaKH());
            ps.setInt(2, rating.getMaPhim());
            ps.setInt(3, rating.getRating());
            ps.setString(4, rating.getComment());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Rating mapRowToRating(ResultSet rs) throws SQLException {
        Rating r = new Rating();
        r.setMaDanhGia(rs.getInt("MaDanhGia"));
        r.setMaKH(rs.getInt("MaKH"));
        r.setMaPhim(rs.getInt("MaPhim"));
        r.setRating(rs.getInt("Rating"));
        r.setComment(rs.getString("Comment"));
        Timestamp ts = rs.getTimestamp("DateDanhGia");
        if (ts != null) r.setDateDanhGia(ts.toLocalDateTime());
        return r;
    }
}


