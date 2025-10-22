<%-- 
    Document   : addmovie
    Created on : Oct 19, 2025, 4:25:53 PM
    Author     : thang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="vi">
    <head>
        <title>Thêm Phim</title>
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
            <h2>Thêm Phim</h2>
            <form action="addmovie" method="post">
                <div class="form-group">
                    <label>Tên Phim</label>
                    <input type="text" name="tenPhim" required>
                </div>
                <div class="form-group">
                    <label>Thể Loại (cách nhau bằng dấu phẩy)</label>
                    <input type="text" name="theLoai" placeholder="Ví dụ: Hành động, Kinh dị" required>
                </div>
                <div class="form-group">
                    <label>Loại Phim</label>
                    <select name="loaiPhim">
                        <option value="2D">2D</option>
                        <option value="3D">3D</option>
                        <option value="IMAX">IMAX</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Đạo Diễn</label>
                    <input type="text" name="daoDien">
                </div>
                <div class="form-group">
                    <label>Diễn Viên</label>
                    <input type="text" name="dienVien">
                </div>
                <div class="form-group">
                    <label>Độ Tuổi Giới Hạn</label>
                    <input type="number" name="doTuoiGioiHan" required>
                </div>
                <div class="form-group">
                    <label>Thời Lượng (phút)</label>
                    <input type="number" name="thoiLuong" required>
                </div>
                <div class="form-group">
                    <label>Nội Dung</label>
                    <textarea name="noiDung"></textarea>
                </div>
                <div class="form-group">
                    <label>Ngày Khởi Chiếu (YYYY-MM-DD)</label>
                    <input type="text" name="ngayKhoiChieu" required>
                </div>
                <div class="form-group">
                    <label>Ngôn Ngữ Phụ Đề</label>
                    <input type="text" name="ngonNguPhuDe">
                </div>
                <div class="form-group">
                    <button type="submit">Lưu</button>
                </div>
            </form>
        </div>
    </body>
</html>
