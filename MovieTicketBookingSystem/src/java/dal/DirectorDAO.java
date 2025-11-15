package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Director;

public class DirectorDAO extends DBContext {
	public Director getById(int id) {
		String sql = "SELECT MaDaoDien, TenDaoDien FROM dbo.Director WHERE MaDaoDien = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Director d = new Director();
					d.setMaDaoDien(rs.getInt("MaDaoDien"));
					d.setTenDaoDien(rs.getNString("TenDaoDien"));
					return d;
				}
			}
		} catch (Exception ignored) {}
		return null;
	}
	public List<Director> getAll() {
		List<Director> list = new ArrayList<>();
		String sql = "SELECT MaDaoDien, TenDaoDien FROM dbo.Director ORDER BY TenDaoDien";
		try (PreparedStatement ps = connection.prepareStatement(sql);
		     ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Director d = new Director();
				d.setMaDaoDien(rs.getInt("MaDaoDien"));
				d.setTenDaoDien(rs.getNString("TenDaoDien"));
				list.add(d);
			}
		} catch (Exception ignored) {}
		return list;
	}

	public List<Integer> getDirectorIdsByMovie(int maPhim) {
		List<Integer> ids = new ArrayList<>();
		String sql = "SELECT MaDaoDien FROM dbo.MovieDirector WHERE MaPhim = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, maPhim);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) ids.add(rs.getInt(1));
			}
		} catch (Exception ignored) {}
		return ids;
	}

	public List<String> getDirectorNamesByMovie(int maPhim) {
		List<String> names = new ArrayList<>();
		String sql = "SELECT d.TenDaoDien FROM dbo.MovieDirector md JOIN dbo.Director d ON d.MaDaoDien = md.MaDaoDien WHERE md.MaPhim = ? ORDER BY d.TenDaoDien";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, maPhim);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) names.add(rs.getNString(1));
			}
		} catch (Exception ignored) {}
		return names;
	}

	public void setDirectorsForMovie(int maPhim, List<Integer> directorIds) throws Exception {
		connection.setAutoCommit(false);
		try {
			try (PreparedStatement del = connection.prepareStatement("DELETE FROM dbo.MovieDirector WHERE MaPhim=?")) {
				del.setInt(1, maPhim);
				del.executeUpdate();
			}
			if (directorIds != null && !directorIds.isEmpty()) {
				try (PreparedStatement ins = connection.prepareStatement("INSERT INTO dbo.MovieDirector(MaPhim, MaDaoDien) VALUES(?,?)")) {
					for (Integer id : directorIds) {
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

	public boolean create(String tenDaoDien) {
		String sql = "INSERT INTO dbo.Director(TenDaoDien) VALUES (?)";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setNString(1, tenDaoDien);
			return ps.executeUpdate() > 0;
		} catch (Exception ignored) {}
		return false;
	}

	public boolean update(int maDaoDien, String tenDaoDien) {
		String sql = "UPDATE dbo.Director SET TenDaoDien = ? WHERE MaDaoDien = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setNString(1, tenDaoDien);
			ps.setInt(2, maDaoDien);
			return ps.executeUpdate() > 0;
		} catch (Exception ignored) {}
		return false;
	}

	public boolean delete(int maDaoDien) {
		// Không cho phép xóa nếu còn liên kết ở MovieDirector
		String checkSql = "SELECT TOP 1 1 FROM dbo.MovieDirector WHERE MaDaoDien = ?";
		String delSql = "DELETE FROM dbo.Director WHERE MaDaoDien = ?";
		try (PreparedStatement check = connection.prepareStatement(checkSql)) {
			check.setInt(1, maDaoDien);
			try (ResultSet rs = check.executeQuery()) {
				if (rs.next()) {
					// Đã có FK tham chiếu -> không xóa
					return false;
				}
			}
		} catch (Exception ignored) {}
		try (PreparedStatement ps = connection.prepareStatement(delSql)) {
			ps.setInt(1, maDaoDien);
			return ps.executeUpdate() > 0;
		} catch (Exception ignored) {}
		return false;
	}
}


