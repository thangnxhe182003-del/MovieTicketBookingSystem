package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Genre;

public class GenreDAO extends DBContext {
	public Genre getById(int id) {
		String sql = "SELECT MaTheLoai, TenTheLoai FROM dbo.Genre WHERE MaTheLoai = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Genre g = new Genre();
					g.setMaTheLoai(rs.getInt("MaTheLoai"));
					g.setTenTheLoai(rs.getNString("TenTheLoai"));
					return g;
				}
			}
		} catch (Exception ignored) {}
		return null;
	}
	public List<Genre> getAll() {
		List<Genre> list = new ArrayList<>();
		String sql = "SELECT MaTheLoai, TenTheLoai FROM dbo.Genre ORDER BY TenTheLoai";
		try (PreparedStatement ps = connection.prepareStatement(sql);
		     ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Genre g = new Genre();
				g.setMaTheLoai(rs.getInt("MaTheLoai"));
				g.setTenTheLoai(rs.getNString("TenTheLoai"));
				list.add(g);
			}
		} catch (Exception ignored) {}
		return list;
	}

	public List<Integer> getGenreIdsByMovie(int maPhim) {
		List<Integer> ids = new ArrayList<>();
		String sql = "SELECT MaTheLoai FROM dbo.MovieGenre WHERE MaPhim = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, maPhim);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) ids.add(rs.getInt(1));
			}
		} catch (Exception ignored) {}
		return ids;
	}

	public List<String> getGenreNamesByMovie(int maPhim) {
		List<String> names = new ArrayList<>();
		String sql = "SELECT g.TenTheLoai FROM dbo.MovieGenre mg JOIN dbo.Genre g ON g.MaTheLoai = mg.MaTheLoai WHERE mg.MaPhim = ? ORDER BY g.TenTheLoai";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, maPhim);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) names.add(rs.getNString(1));
			}
		} catch (Exception ignored) {}
		return names;
	}

	public void setGenresForMovie(int maPhim, List<Integer> genreIds) throws Exception {
		connection.setAutoCommit(false);
		try {
			try (PreparedStatement del = connection.prepareStatement("DELETE FROM dbo.MovieGenre WHERE MaPhim=?")) {
				del.setInt(1, maPhim);
				del.executeUpdate();
			}
			if (genreIds != null && !genreIds.isEmpty()) {
				try (PreparedStatement ins = connection.prepareStatement("INSERT INTO dbo.MovieGenre(MaPhim, MaTheLoai) VALUES(?,?)")) {
					for (Integer id : genreIds) {
						ins.setInt(1, maPhim);
						ins.setInt(2, id);
						ins.addBatch();
					}
					ins.executeBatch();
				}
			}
			connection.commit();
		} catch (Exception ex) {
			connection.rollback();
			throw ex;
		} finally {
			connection.setAutoCommit(true);
		}
	}

	public boolean create(String tenTheLoai) {
		String sql = "INSERT INTO dbo.Genre(TenTheLoai) VALUES (?)";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setNString(1, tenTheLoai);
			return ps.executeUpdate() > 0;
		} catch (Exception ignored) {}
		return false;
	}

	public boolean update(int maTheLoai, String tenTheLoai) {
		String sql = "UPDATE dbo.Genre SET TenTheLoai = ? WHERE MaTheLoai = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setNString(1, tenTheLoai);
			ps.setInt(2, maTheLoai);
			return ps.executeUpdate() > 0;
		} catch (Exception ignored) {}
		return false;
	}

	public boolean delete(int maTheLoai) {
		// Không cho phép xóa nếu còn liên kết ở MovieGenre
		String checkSql = "SELECT TOP 1 1 FROM dbo.MovieGenre WHERE MaTheLoai = ?";
		String delSql = "DELETE FROM dbo.Genre WHERE MaTheLoai = ?";
		try (PreparedStatement check = connection.prepareStatement(checkSql)) {
			check.setInt(1, maTheLoai);
			try (ResultSet rs = check.executeQuery()) {
				if (rs.next()) {
					// Đã có FK tham chiếu -> không xóa
					return false;
				}
			}
		} catch (Exception ignored) {}
		try (PreparedStatement ps = connection.prepareStatement(delSql)) {
			ps.setInt(1, maTheLoai);
			return ps.executeUpdate() > 0;
		} catch (Exception ignored) {}
		return false;
	}
}


