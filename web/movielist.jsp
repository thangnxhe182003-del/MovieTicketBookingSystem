<%-- 
    Document   : movielist
    Created on : Oct 19, 2025, 4:21:46 PM
    Author     : thang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.*, model.Movie" %>
<%
    List<Movie> ds = (List<Movie>) request.getAttribute("dsMovie");
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>Danh sách Phim</title>
        <style>
            table {
                border-collapse: collapse;
                width: 90%;
                margin: auto;
            }
            th, td {
                border: 1px solid #999;
                padding: 8px;
                text-align: center;
            }
            th {
                background-color: #eee;
            }
            h2 {
                text-align: center;
            }
        </style>
    </head>
    <body>
        <h2>Danh sách Phim</h2>
        <div style="text-align: center; margin-top: 20px;">
            <a href="addmovie.jsp">Thêm Phim</a>
        </div>
        <br>

        <table>
            <tr>
                <th>ID</th>
                <th>Tên Phim</th>
                <th>Thể Loại</th>
                <th>Loại Phim</th>
                <th>Đạo Diễn</th>
                <th>Diễn Viên</th>
                <th>Độ Tuổi Giới Hạn</th>
                <th>Thời Lượng</th>
                <th>Nội Dung</th>
                <th>Ngày Khởi Chiếu</th>
                <th>Ngôn Ngữ Phụ Đề</th>
                <th>Poster</th>
                <th>Edit</th>
                <th>Delete</th>
            </tr>
            <%
                if (ds != null) {
                    for (Movie movie : ds) {
            %>
            <tr>
                <td><%= movie.getMaPhim() %></td>
                <td><%= movie.getTenPhim() %></td>
                <td><%= movie.getTheLoai() %></td>
                <td><%= movie.getLoaiPhim() %></td>
                <td><%= movie.getDaoDien() %></td>
                <td><%= movie.getDienVien() %></td>
                <td><%= movie.getDoTuoiGioiHan() %></td>
                <td><%= movie.getThoiLuong() %></td>
                <td><%= movie.getNoiDung() %></td>
                <td><%= movie.getNgayKhoiChieu() %></td>
                <td><%= movie.getNgonNguPhuDe() %></td>
                <td><img src="${pageContext.request.contextPath}<%= movie.getPoster() %>" alt="Poster" width="50"></td>
                <td>
                    <a href="updatemovie?ma=<%= movie.getMaPhim() %>">Sửa</a>
                </td>
                <td>
                    <a href="deletemovie?ma=<%= movie.getMaPhim() %>" onclick="return confirm('Bạn có chắc muốn xóa phim này?');">Xóa</a>
                </td>

            </tr>
            <%
                    }
                } else {
            %>
            <tr><td colspan="6">Không có dữ liệu</td></tr>
            <% } %>
        </table>
    </body>
</html>
