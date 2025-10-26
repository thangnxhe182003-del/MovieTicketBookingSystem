package dal;

import model.Trailer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrailerDAO extends DBContext {

    /**
     * Lấy tất cả trailer
     */
    public List<Trailer> getAllTrailers() {
        List<Trailer> trailers = new ArrayList<>();
        String sql = "SELECT * FROM dbo.TrailerPhim";
        
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                trailers.add(mapRowToTrailer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trailers;
    }

    /**
     * Lấy trailer của phim theo ID phim
     */
    public List<Trailer> getTrailersByMovie(int maPhim) {
        List<Trailer> trailers = new ArrayList<>();
        String sql = "SELECT * FROM dbo.TrailerPhim WHERE MaPhim = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maPhim);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    trailers.add(mapRowToTrailer(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trailers;
    }

    /**
     * Lấy trailer theo ID
     */
    public Trailer getTrailerById(int maTrailer) {
        String sql = "SELECT * FROM dbo.TrailerPhim WHERE MaTrailer = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maTrailer);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToTrailer(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Thêm trailer mới
     */
    public boolean addTrailer(Trailer trailer) {
        String sql = "INSERT INTO dbo.TrailerPhim (MaPhim, LinkTrailer, MoTa) VALUES (?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, trailer.getMaPhim());
            ps.setString(2, trailer.getLinkTrailer());
            ps.setString(3, trailer.getMoTa());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật trailer
     */
    public boolean updateTrailer(Trailer trailer) {
        String sql = "UPDATE dbo.TrailerPhim SET MaPhim = ?, LinkTrailer = ?, MoTa = ? WHERE MaTrailer = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, trailer.getMaPhim());
            ps.setString(2, trailer.getLinkTrailer());
            ps.setString(3, trailer.getMoTa());
            ps.setInt(4, trailer.getMaTrailer());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa trailer
     */
    public boolean deleteTrailer(int maTrailer) {
        String sql = "DELETE FROM dbo.TrailerPhim WHERE MaTrailer = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maTrailer);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map ResultSet row to Trailer object
     */
    private Trailer mapRowToTrailer(ResultSet rs) throws SQLException {
        Trailer trailer = new Trailer();
        trailer.setMaTrailer(rs.getInt("MaTrailer"));
        trailer.setMaPhim(rs.getInt("MaPhim"));
        trailer.setLinkTrailer(rs.getString("LinkTrailer"));
        trailer.setMoTa(rs.getString("MoTa"));
        return trailer;
    }
}
