<%-- 
    Document   : Home
    Created on : Oct 4, 2025, 1:27:33 PM
    Author     : thang
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Trang chủ - Đặt vé phim</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <style>
            body {
                font-family: "Segoe UI", Arial, sans-serif;
                margin: 0;
                background-color: #f8f9fa;
            }

            /* ==== HEADER ==== */
            header {
                display: flex;
                align-items: center;
                justify-content: space-between;
                background: #fff;
                padding: 8px 40px;
                border-bottom: 1px solid #ccc;
                position: sticky;
                top: 0;
                z-index: 1000;
            }

            .logo {
                display: flex;
                align-items: center;
                gap: 0.5rem;
            }

            .logo img {
                width: 50px;
                height: auto;
                border-radius: 10px;
            }

            .logo span {
                font-weight: 700;
                font-size: 1.25rem;
                color: #075985;
                letter-spacing: 0.04em;
            }

            /* ==== SEARCH BAR ==== */
            .search-bar {
                display: flex;
                align-items: center;
                flex: 1;
                max-width: 250px;
                margin-left: 20px;
                background-color: #f4f4f4;
                border-radius: 8px;
                padding: 3px 10px;
            }

            .search-bar input {
                border: none;
                outline: none;
                background: none;
                padding: 4px 5px;
                width: 100%;
                font-size: 14px;
            }

            /* ==== MENU ==== */
            nav {
                display: flex;
                align-items: center;
                gap: 35px;
                flex: 1;
                justify-content: center;
            }

            nav a {
                text-decoration: none;
                font-weight: bold;
                font-size: 16px;
                color: #000;
                position: relative;
            }

            nav a:hover {
                color: #1a3a7d;
            }

            nav a::after {
                content: "";
                position: absolute;
                left: 0;
                bottom: -5px;
                width: 0;
                height: 2px;
                background-color: #1a3a7d;
                transition: width 0.3s;
            }

            nav a:hover::after {
                width: 100%;
            }

            /* ==== LOGIN ICON ==== */
            .login {
                text-align: center;
                font-size: 12px;
            }

            .login a {
                text-decoration: none;
                color: #000;
                display: block;
            }

            .login img {
                width: 40px;
                height: 40px;
                border-radius: 5px;
                display: block;
                margin: auto;
            }

            .login a:hover {
                color: #1a3a7d;
            }

            /* ==== SLIDER ==== */
            .slider {
                position: relative;
                width: 100%;
                height: 380px;
                overflow: hidden;
            }

            .slides {
                display: flex;
            }

            .slide {
                min-width: 100%;
                height: 380px;
            }

            .slide img {
                width: 100%;
                height: 380px;
                object-fit: cover;
            }

            .slider-nav {
                position: absolute;
                bottom: 15px;
                left: 50%;
                transform: translateX(-50%);
                display: flex;
                gap: 10px;
            }

            .dot {
                height: 10px;
                width: 10px;
                background-color: #ccc;
                border-radius: 50%;
            }

            .dot.active {
                background-color: #333;
            }

            /* ==== MOVIE SECTIONS ==== */
            .movie-section {
                width: 90%;
                margin: 40px auto;
            }

            .movie-section h2 {
                font-size: 22px;
                font-weight: bold;
                margin-bottom: 20px;
                color: #1a3a7d;
                border-left: 5px solid #1a3a7d;
                padding-left: 10px;
            }

            .movie-list {
                display: flex;
                justify-content: space-between;
                flex-wrap: wrap;
                gap: 25px;
            }

            .movie-card {
                width: 200px;
                background: white;
                border-radius: 8px;
                box-shadow: 0 3px 10px rgba(0,0,0,0.1);
                overflow: hidden;
                text-align: center;
                transition: transform 0.3s;
            }

            .movie-card:hover {
                transform: translateY(-5px);
            }

            .movie-card img {
                width: 100%;
                height: 280px;
                object-fit: cover;
            }

            .movie-info {
                padding: 10px;
            }

            .movie-info h3 {
                font-size: 16px;
                margin: 5px 0;
                color: #222;
            }

            .movie-info p {
                font-size: 13px;
                color: gray;
                margin-bottom: 10px;
            }

            .movie-info button {
                background: linear-gradient(#c94444, #9b1d1d);
                border: none;
                color: #fff;
                padding: 6px 16px;
                border-radius: 12px;
                cursor: pointer;
                font-weight: bold;
            }

            .movie-info button:hover {
                background: linear-gradient(#b33333, #7a0e0e);
            }

            /* ==== PROMOTION SECTION ==== */
            .promo-section {
                background: #fff;
                padding: 40px 0;
            }

            .promo-section h2 {
                text-align: center;
                font-size: 22px;
                color: #1a3a7d;
                margin-bottom: 20px;
            }

            .promo-list {
                display: flex;
                justify-content: center;
                gap: 30px;
                flex-wrap: wrap;
            }

            .promo-card {
                width: 300px;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 3px 10px rgba(0,0,0,0.1);
            }

            .promo-card img {
                width: 100%;
                height: 180px;
                object-fit: cover;
            }

            .promo-card p {
                padding: 10px;
                text-align: center;
                font-size: 15px;
            }

            /* ==== FOOTER ==== */
            footer {
                background: #0f172a;
                color: white;
                padding: 40px 0;
                text-align: center;
            }

            footer p {
                font-size: 14px;
                color: #cbd5e1;
            }
        </style>
    </head>
    <body>

        <!-- ==== HEADER ==== -->
        <header>
            <div class="logo">
                <img src="https://png.pngtree.com/element_origin_min_pic/16/12/04/906aae676011fbcc70e96932704830e3.jpg" alt="Logo">
                <span>MovieNow</span>
            </div>

            <div class="search-bar">
                <i class="fa fa-search" style="color: gray;"></i>
                <input type="text" placeholder="Tìm phim, rạp, ưu đãi...">
                <i class="fa fa-times" style="color: gray;"></i>
            </div>

            <nav>
                <a href="#">Phim</a>
                <a href="#">Thể loại</a>
                <a href="#">Mua vé</a>
                <a href="#">Tin tức & Ưu đãi</a>
                <a href="#">Hỗ trợ</a>
            </nav>

            <div class="login">
                <a href="login.jsp">
                    <img src="https://cdn-icons-png.flaticon.com/512/847/847969.png" alt="user">
                    <span>Đăng nhập</span>
                </a>
            </div>
        </header>

        <!-- ==== SLIDER (tĩnh, không chạy tự động) ==== -->
        <div class="slider">
            <div class="slides">
                <div class="slide"><img src="https://media.lottecinemavn.com/Media/WebAdmin/7ce48d93c8074902b0b5a6ba82e85351.jpg" alt="Banner 1"></div>
            </div>
            <div class="slider-nav">
                <span class="dot active"></span>
                <span class="dot"></span>
                <span class="dot"></span>
            </div>
        </div>

        <!-- ==== MOVIE SECTIONS ==== -->
        <div class="movie-section">
            <h2>🎬 Phim đang chiếu</h2>
            <div class="movie-list">
                <div class="movie-card">
                    <img src="https://img.cgv.co.kr/Movie/Thumbnail/Poster/000087/87211/87211_320.jpg" alt="Phim 1">
                    <div class="movie-info">
                        <h3>Venom 3</h3>
                        <p>Hành động | 128 phút</p>
                        <button>Đặt vé</button>
                    </div>
                </div>
                <div class="movie-card">
                    <img src="https://img.cgv.co.kr/Movie/Thumbnail/Poster/000087/87172/87172_320.jpg" alt="Phim 2">
                    <div class="movie-info">
                        <h3>Inside Out 2</h3>
                        <p>Hoạt hình | 105 phút</p>
                        <button>Đặt vé</button>
                    </div>
                </div>
                <div class="movie-card">
                    <img src="https://img.cgv.co.kr/Movie/Thumbnail/Poster/000087/87237/87237_320.jpg" alt="Phim 3">
                    <div class="movie-info">
                        <h3>Deadpool & Wolverine</h3>
                        <p>Siêu anh hùng | 132 phút</p>
                        <button>Đặt vé</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="movie-section">
            <h2>🍿 Phim sắp chiếu</h2>
            <div class="movie-list">
                <div class="movie-card">
                    <img src="https://img.cgv.co.kr/Movie/Thumbnail/Poster/000087/87239/87239_320.jpg" alt="Phim 4">
                    <div class="movie-info">
                        <h3>Gladiator II</h3>
                        <p>Lịch sử | 155 phút</p>
                        <button>Xem chi tiết</button>
                    </div>
                </div>
                <div class="movie-card">
                    <img src="https://img.cgv.co.kr/Movie/Thumbnail/Poster/000087/87230/87230_320.jpg" alt="Phim 5">
                    <div class="movie-info">
                        <h3>Wicked</h3>
                        <p>Âm nhạc | 130 phút</p>
                        <button>Xem chi tiết</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- ==== PROMOTION ==== -->
        <div class="promo-section">
            <h2>🎁 Tin tức & Ưu đãi</h2>
            <div class="promo-list">
                <div class="promo-card">
                    <img src="https://iguov8nhvyobj.vcdn.cloud/media/wysiwyg/2025/052025/240x201_2_.jpg" alt="Promo 1">
                    <p>Nhận ngay combo bắp nước miễn phí khi đặt vé online 🎉</p>
                </div>
                <div class="promo-card">
                    <img src="https://iguov8nhvyobj.vcdn.cloud/media/wysiwyg/2024/122024/240X201_14_.png" alt="Promo 2">
                    <p>Tham gia Movie Club - tích điểm đổi vé miễn phí!</p>
                </div>
            </div>
        </div>

        <!-- ==== FOOTER ==== -->
        <footer>
            <p>© 2025 MovieNow - Nền tảng đặt vé phim hàng đầu Việt Nam</p>
            <p>Hotline: 1900 1234 | Email: support@movienow.vn</p>
        </footer>

    </body>
</html>
