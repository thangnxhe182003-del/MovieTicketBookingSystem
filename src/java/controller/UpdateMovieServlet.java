/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.MovieDAO;
import model.Movie;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 *
 * @author thang
 */
public class UpdateMovieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String maStr = request.getParameter("ma");
        if (maStr == null || maStr.trim().isEmpty()) {
            response.sendRedirect("movie"); // Redirect if no ID
            return;
        }
        try {
            int maPhim = Integer.parseInt(maStr);
            MovieDAO dao = new MovieDAO();
            Movie movie = dao.getMovieById(maPhim);
            if (movie != null) {
                request.setAttribute("movie", movie);
                request.getRequestDispatcher("updatemovie.jsp").forward(request, response);
            } else {
                response.sendRedirect("movie"); // Movie not found
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("movie"); // Invalid ID
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String maPhimStr = request.getParameter("maPhim");
        String tenPhim = request.getParameter("tenPhim");
        String theLoai = request.getParameter("theLoai");
        String loaiPhim = request.getParameter("loaiPhim");
        String daoDien = request.getParameter("daoDien");
        String dienVien = request.getParameter("dienVien");
        String doTuoiGioiHanStr = request.getParameter("doTuoiGioiHan");
        String thoiLuongStr = request.getParameter("thoiLuong");
        String noiDung = request.getParameter("noiDung");
        String ngayKhoiChieu = request.getParameter("ngayKhoiChieu");
        String ngonNguPhuDe = request.getParameter("ngonNguPhuDe");

        String error = "";

        // Handle null parameters
        if (tenPhim == null) {
            tenPhim = "";
        }
        if (theLoai == null) {
            theLoai = "";
        }
        if (loaiPhim == null) {
            loaiPhim = "";
        }
        if (daoDien == null) {
            daoDien = "";
        }
        if (dienVien == null) {
            dienVien = "";
        }
        if (doTuoiGioiHanStr == null) {
            doTuoiGioiHanStr = "";
        }
        if (thoiLuongStr == null) {
            thoiLuongStr = "";
        }
        if (noiDung == null) {
            noiDung = "";
        }
        if (ngayKhoiChieu == null) {
            ngayKhoiChieu = "";
        }
        if (ngonNguPhuDe == null) {
            ngonNguPhuDe = "";
        }

        // Regex kiểm tra hợp lệ
        String regexText = "^[\\p{L}0-9\\s,.:;()–-]+$"; // Cho phép dấu phẩy, dấu chấm, ngoặc, gạch ngang cho thể loại/nội dung
        String regexDate = "^\\d{4}-\\d{2}-\\d{2}$"; // YYYY-MM-DD

        if (tenPhim.trim().isEmpty() || !tenPhim.matches(regexText)) {
            error = "Tên phim không được chứa ký tự đặc biệt và không được rỗng.";
        } else if (theLoai.trim().isEmpty() || !theLoai.matches(regexText)) {
            error = "Thể loại không được chứa ký tự đặc biệt và không được rỗng.";
        } else if (!daoDien.matches(regexText)) {
            error = "Đạo diễn không được chứa ký tự đặc biệt.";
        } else if (!dienVien.matches(regexText)) {
            error = "Diễn viên không được chứa ký tự đặc biệt.";
        } else if (!noiDung.matches(regexText)) {
            error = "Nội dung không được chứa ký tự đặc biệt.";
        } else if (ngayKhoiChieu.trim().isEmpty() || !ngayKhoiChieu.matches(regexDate)) {
            error = "Ngày khởi chiếu phải theo định dạng YYYY-MM-DD và không được rỗng.";
        } else if (doTuoiGioiHanStr.isEmpty() || !doTuoiGioiHanStr.matches("^[0-9]+$") || Integer.parseInt(doTuoiGioiHanStr) < 0) {
            error = "Độ tuổi giới hạn phải là số nguyên không âm.";
        } else if (thoiLuongStr.isEmpty() || !thoiLuongStr.matches("^[0-9]+$") || Integer.parseInt(thoiLuongStr) <= 0) {
            error = "Thời lượng phải là số nguyên dương.";
        }

        // Nếu có lỗi → trả lại trang JSP
        if (!error.isEmpty()) {
            try {
                Movie movie = new Movie();
                movie.setMaPhim(Integer.parseInt(maPhimStr));
                movie.setTenPhim(tenPhim);
                movie.setTheLoai(theLoai);
                movie.setLoaiPhim(loaiPhim);
                movie.setDaoDien(daoDien);
                movie.setDienVien(dienVien);
                movie.setDoTuoiGioiHan(Integer.parseInt(doTuoiGioiHanStr));
                movie.setThoiLuong(Integer.parseInt(thoiLuongStr));
                movie.setNoiDung(noiDung);
                movie.setNgayKhoiChieu(ngayKhoiChieu);
                movie.setNgonNguPhuDe(ngonNguPhuDe);
                // Giữ poster hiện tại
                MovieDAO daoTemp = new MovieDAO();
                Movie existing = daoTemp.getMovieById(movie.getMaPhim());
                if (existing != null) {
                    movie.setPoster(existing.getPoster());
                }
                request.setAttribute("error", error);
                request.setAttribute("movie", movie);
                request.getRequestDispatcher("updatemovie.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                error = "Dữ liệu không hợp lệ.";
                request.setAttribute("error", error);
                response.sendRedirect("movie");
            }
            return;
        }

        // Hợp lệ → cập nhật DB
        try {
            Movie movie = new Movie();
            movie.setMaPhim(Integer.parseInt(maPhimStr));
            movie.setTenPhim(tenPhim);
            movie.setTheLoai(theLoai);
            movie.setLoaiPhim(loaiPhim);
            movie.setDaoDien(daoDien);
            movie.setDienVien(dienVien);
            movie.setDoTuoiGioiHan(Integer.parseInt(doTuoiGioiHanStr));
            movie.setThoiLuong(Integer.parseInt(thoiLuongStr));
            movie.setNoiDung(noiDung);
            movie.setNgayKhoiChieu(ngayKhoiChieu);
            movie.setNgonNguPhuDe(ngonNguPhuDe);
            // Giữ poster hiện tại
            MovieDAO daoTemp = new MovieDAO();
            Movie existing = daoTemp.getMovieById(movie.getMaPhim());
            if (existing != null) {
                movie.setPoster(existing.getPoster());
            }
            MovieDAO dao = new MovieDAO();
            dao.updateMovie(movie);

            response.sendRedirect("movie");
        } catch (NumberFormatException e) {
            error = "Dữ liệu không hợp lệ.";
            request.setAttribute("error", error);
            response.sendRedirect("movie");
        }
    }
}
