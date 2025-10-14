<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Trang chủ - Đặt vé phim</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <link rel="stylesheet" href="css/styles.css">
    </head>
    <body>
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
                <c:choose>
                    <c:when test="${not empty sessionScope.userType}">
                        <div class="profile-dropdown">
                            <div class="dropdown-toggle" onclick="toggleDropdown()">
                                <img src="https://cdn-icons-png.flaticon.com/512/847/847969.png" alt="user">
                                <span>${sessionScope.fullName} <i class="fa fa-chevron-down"></i></span>
                            </div>
                            <div class="dropdown-menu" id="profileDropdown">
                                <a href="#" class="dropdown-item" onclick="showProfileModal(event)">Thông tin tài khoản</a>
                                <a href="#" class="dropdown-item" onclick="showChangePwModal(event)">Đổi mật khẩu</a>
                                <a href="Points.jsp" class="dropdown-item">Điểm thưởng</a>
                                <a href="LogoutServlet" class="dropdown-item">Đăng xuất</a>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="Login.jsp">
                            <img src="https://cdn-icons-png.flaticon.com/512/847/847969.png" alt="user">
                            <span>Đăng nhập</span>
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </header>

        <div class="slider">
            <div class="slides">
                <div class="slide"><img src="https://media.lottecinemavn.com/Media/WebAdmin/7ce48d93c8074902b0b5a6ba82e85351.jpg" alt="Banner 1"></div>
                <div class="slide"><img src="https://media.lottecinemavn.com/Media/WebAdmin/0908df98502240e8991ef02760dea525.jpg" alt="Banner 2"></div>
                <div class="slide"><img src="https://img.cgv.co.kr/WebApp/contents/banner/2023/1015/CGV_MovieClub_960x448.jpg" alt="Banner 3"></div>
            </div>
            <div class="slider-nav">
                <span class="dot active"></span>
                <span class="dot"></span>
                <span class="dot"></span>
            </div>
        </div>

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

        <div class="promo-section">
            <h2>🎁 Tin tức & Ưu đãi</h2>
            <div class="promo-list">
                <div class="promo-card">
                    <img src="https://img.cgv.co.kr/WebApp/contents/banner/2023/1025/Promotion_960x448.jpg" alt="Promo 1">
                    <p>Nhận ngay combo bắp nước miễn phí khi đặt vé online 🎉</p>
                </div>
                <div class="promo-card">
                    <img src="https://img.cgv.co.kr/WebApp/contents/banner/2023/1017/Movie_Club_960x448.jpg" alt="Promo 2">
                    <p>Tham gia Movie Club - tích điểm đổi vé miễn phí!</p>
                </div>
            </div>
        </div>

        <footer>
            <p>© 2025 MovieNow - Nền tảng đặt vé phim hàng đầu Việt Nam</p>
            <p>Hotline: 1900 1234 | Email: support@movienow.vn</p>
        </footer>

        <script src="js/scripts.js"></script>
        <%@ include file="Profile.jsp" %>
        <%@ include file="ChangePass.jsp" %>
    </body>
</html>