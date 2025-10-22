<%-- 
    Document   : updatemovie
    Created on : Oct 19, 2025, 4:28:48 PM
    Author     : thang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Movie" %>
<%
    Movie movie = (Movie) request.getAttribute("movie");
    String error = (String) request.getAttribute("error");
%>
<html lang="vi">
    <head>
        <title>Sửa Phim</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
            }
            .container {
                max-width: 600px;
                margin: 0 auto;
            }
            h2 {
                color: #333;
            }
            .error {
                color: red;
                margin-bottom: 10px;
            }
            .form-group {
                margin-bottom: 15px;
            }
            .form-group label {
                display: block;
                margin-bottom: 5px;
            }
            .form-group input, .form-group textarea, .form-group select {
                width: 100%;
                padding: 8px;
                font-size: 14px;
            }
            .form-group textarea {
                height: 100px;
            }
            .form-group img {
                margin-top: 10px;
            }
            .form-group button {
                padding: 10px 20px;
                background: #007bff;
                color: white;
                border: none;
                cursor: pointer;
            }
            .form-group button:hover {
                background: #0056b3;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Sửa Phim</h2>
            <% if (error != null && !error.isEmpty()) { %>
            <div class="error"><%= error %></div>
            <% } %>
            <form action="updatemovie" method="post">
                <input type="hidden" name="maPhim" value="<%= movie.getMaPhim() %>">
                <div class="form-group">
                    <label>Tên Phim</label>
                    <input type="text" name="tenPhim" value="<%= movie.getTenPhim() %>" required>
                </div>
                <div class="form-group">
                    <label>Thể Loại (cách nhau bằng dấu phẩy)</label>
                    <input type="text" name="theLoai" value="<%= movie.getTheLoai() %>" required>
                </div>
                <div class="form-group">
                    <label>Loại Phim</label>
                    <select name="loaiPhim">
                        <option value="2D" <%= "2D".equals(movie.getLoaiPhim()) ? "selected" : "" %>>2D</option>
                        <option value="3D" <%= "3D".equals(movie.getLoaiPhim()) ? "selected" : "" %>>3D</option>
                        <option value="IMAX" <%= "IMAX".equals(movie.getLoaiPhim()) ? "selected" : "" %>>IMAX</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Đạo Diễn</label>
                    <input type="text" name="daoDien" value="<%= movie.getDaoDien() %>">
                </div>
                <div class="form-group">
                    <label>Diễn Viên</label>
                    <input type="text" name="dienVien" value="<%= movie.getDienVien() %>">
                </div>
                <div class="form-group">
                    <label>Độ Tuổi Giới Hạn</label>
                    <input type="number" name="doTuoiGioiHan" value="<%= movie.getDoTuoiGioiHan() %>" required>
                </div>
                <div class="form-group">
                    <label>Thời Lượng (phút)</label>
                    <input type="number" name="thoiLuong" value="<%= movie.getThoiLuong() %>" required>
                </div>
                <div class="form-group">
                    <label>Nội Dung</label>
                    <textarea name="noiDung"><%= movie.getNoiDung() %></textarea>
                </div>
                <div class="form-group">
                    <label>Ngày Khởi Chiếu (YYYY-MM-DD)</label>
                    <input type="text" name="ngayKhoiChieu" value="<%= movie.getNgayKhoiChieu() %>" required>
                </div>
                <div class="form-group">
                    <label>Ngôn Ngữ Phụ Đề</label>
                    <input type="text" name="ngonNguPhuDe" value="<%= movie.getNgonNguPhuDe() %>">
                </div>
                <div class="form-group">
                    <label>Poster hiện tại</label>
                    <% if (movie.getPoster() != null && !movie.getPoster().isEmpty()) { %>
                    <img src="${pageContext.request.contextPath}<%= movie.getPoster() %>" alt="Poster hiện tại" width="100">
                    <% } else { %>
                    <p>Không có poster</p>
                    <% } %>
                </div>
                <div class="form-group">
                    <button type="submit">Lưu</button>
                </div>
            </form>
        </div>
    </body>
</html>
