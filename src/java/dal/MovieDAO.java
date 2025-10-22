/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.Movie;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thang
 */
public class MovieDAO extends DBContext {

    public List<Movie> getAllMovies() {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT * FROM Movie";

        try {
            DBContext db = new DBContext(); // dùng kết nối sẵn
            PreparedStatement ps = db.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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
                movie.setNgayKhoiChieu(rs.getString("NgayKhoiChieu"));
                movie.setPoster(rs.getString("Poster"));
                movie.setNgonNguPhuDe(rs.getString("NgonNguPhuDe"));
                list.add(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void addMovie(Movie movie) {
        String sql = "INSERT INTO Movie (TenPhim, TheLoai, LoaiPhim, DaoDien, DienVien, DoTuoiGioiHan, ThoiLuong, NoiDung, NgayKhoiChieu, Poster, NgonNguPhuDe) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            DBContext db = new DBContext();
            PreparedStatement ps = db.connection.prepareStatement(sql);
            ps.setString(1, movie.getTenPhim());
            ps.setString(2, movie.getTheLoai());
            ps.setString(3, movie.getLoaiPhim());
            ps.setString(4, movie.getDaoDien());
            ps.setString(5, movie.getDienVien());
            ps.setInt(6, movie.getDoTuoiGioiHan());
            ps.setInt(7, movie.getThoiLuong());
            ps.setString(8, movie.getNoiDung());
            ps.setString(9, movie.getNgayKhoiChieu());
            ps.setString(10, movie.getPoster());
            ps.setString(11, movie.getNgonNguPhuDe());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Movie getMovieById(int id) {
        String sql = "SELECT * FROM Movie WHERE MaPhim = ?";
        try {
            DBContext db = new DBContext();
            PreparedStatement ps = db.connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
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
                movie.setNgayKhoiChieu(rs.getString("NgayKhoiChieu"));
                movie.setPoster(rs.getString("Poster"));
                movie.setNgonNguPhuDe(rs.getString("NgonNguPhuDe"));
                return movie;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateMovie(Movie movie) {
        String sql = "UPDATE Movie SET TenPhim=?, TheLoai=?, LoaiPhim=?, DaoDien=?, DienVien=?, DoTuoiGioiHan=?, ThoiLuong=?, NoiDung=?, NgayKhoiChieu=?, Poster=?, NgonNguPhuDe=? WHERE MaPhim=?";
        try {
            DBContext db = new DBContext();
            PreparedStatement ps = db.connection.prepareStatement(sql);
            ps.setString(1, movie.getTenPhim());
            ps.setString(2, movie.getTheLoai());
            ps.setString(3, movie.getLoaiPhim());
            ps.setString(4, movie.getDaoDien());
            ps.setString(5, movie.getDienVien());
            ps.setInt(6, movie.getDoTuoiGioiHan());
            ps.setInt(7, movie.getThoiLuong());
            ps.setString(8, movie.getNoiDung());
            ps.setString(9, movie.getNgayKhoiChieu());
            ps.setString(10, movie.getPoster());
            ps.setString(11, movie.getNgonNguPhuDe());
            ps.setInt(12, movie.getMaPhim());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMovie(int maPhim) {
        String sql = "DELETE FROM Movie WHERE MaPhim = ?";
        try {
            DBContext db = new DBContext();
            PreparedStatement ps = db.connection.prepareStatement(sql);
            ps.setInt(1, maPhim);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}