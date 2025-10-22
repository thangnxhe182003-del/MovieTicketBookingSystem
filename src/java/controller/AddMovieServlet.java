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
public class AddMovieServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

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

        // Regex kiểm tra không có ký tự đặc biệt cho text fields
        String regexText = "^[\\p{L}0-9\\s,.:;()–-]+$"; // Cho phép dấu phẩy, dấu chấm, ngoặc, gạch ngang cho thể loại/nội dung
        String regexDate = "^\\d{4}-\\d{2}-\\d{2}$"; // YYYY-MM-DD

        if (!tenPhim.matches(regexText)) {
            error = "Tên phim không được chứa ký tự đặc biệt.";
        } else if (!theLoai.matches(regexText)) {
            error = "Thể loại không được chứa ký tự đặc biệt.";
        } else if (!daoDien.matches(regexText)) {
            error = "Đạo diễn không được chứa ký tự đặc biệt.";
        } else if (!dienVien.matches(regexText)) {
            error = "Diễn viên không được chứa ký tự đặc biệt.";
        } else if (!noiDung.matches(regexText)) {
            error = "Nội dung không được chứa ký tự đặc biệt.";
        } else if (!ngayKhoiChieu.matches(regexDate)) {
            error = "Ngày khởi chiếu phải theo định dạng YYYY-MM-DD.";
        } else if (doTuoiGioiHanStr == null || !doTuoiGioiHanStr.matches("^[0-9]+$") || Integer.parseInt(doTuoiGioiHanStr) < 0) {
            error = "Độ tuổi giới hạn phải là số nguyên không âm.";
        } else if (thoiLuongStr == null || !thoiLuongStr.matches("^[0-9]+$") || Integer.parseInt(thoiLuongStr) <= 0) {
            error = "Thời lượng phải là số nguyên dương.";
        }

        if (!error.isEmpty()) {
            // Trả lại form cùng dữ liệu và thông báo lỗi
            request.setAttribute("error", error);

            // Giữ lại dữ liệu đã nhập để hiển thị lại
            Movie movie = new Movie();
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
            request.setAttribute("movie", movie);

            request.getRequestDispatcher("addmovie.jsp").forward(request, response);
            return;
        }

        // Nếu hợp lệ thì thêm vào database
        Movie movie = new Movie();
        movie.setTenPhim(tenPhim);
        movie.setTheLoai(theLoai);
        movie.setLoaiPhim(loaiPhim);
        movie.setDaoDien(daoDien);
        movie.setDienVien(dienVien);
        movie.setDoTuoiGioiHan(Integer.parseInt(doTuoiGioiHanStr));
        movie.setThoiLuong(Integer.parseInt(thoiLuongStr));
        movie.setNoiDung(noiDung);
        movie.setNgayKhoiChieu(ngayKhoiChieu);
        movie.setPoster(""); // Default empty poster
        movie.setNgonNguPhuDe(ngonNguPhuDe);
        MovieDAO dao = new MovieDAO();
        dao.addMovie(movie);

        response.sendRedirect("movie");
    }
}
