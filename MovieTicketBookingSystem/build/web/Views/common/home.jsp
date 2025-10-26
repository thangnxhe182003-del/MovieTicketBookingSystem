<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%-- JSTL Core: UPDATED URI --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- JSTL Formatting: UPDATED URI --%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Trang chủ - MovieNow</title>
        <style>
            /* ==== SLIDER ==== */
            .slider {
                position: relative;
                width: 100%;
                height: 380px;
                overflow: hidden;
            }
            .slides { display: flex; transition: transform .4s ease; }
            .slide { min-width: 100%; height: 380px; }
            .slide img { width: 100%; height: 380px; object-fit: cover; }
            .slider-nav { position: absolute; bottom: 15px; left: 50%; transform: translateX(-50%); display: flex; gap: 10px; }
            .dot { height: 10px; width: 10px; background-color: #ccc; border-radius: 50%; cursor: pointer; }
            .dot.active { background-color: #333; }

            /* ==== MOVIE SECTIONS ==== */
            .home-body { max-width: 1200px; margin: 30px auto; padding: 0 20px; }
            .tabs { display: flex; gap: 10px; border-bottom: 2px solid #eee; margin-bottom: 20px; }
            .tab-btn { padding: 10px 18px; border: 1px solid #eee; border-bottom: 2px solid transparent; background: #fafafa; color: #333; cursor: pointer; font-weight: 600; border-top-left-radius: 6px; border-top-right-radius: 6px; }
            .tab-btn.active { background: #fff; border-color: #e50914; color: #e50914; border-bottom-color: #fff; }
            .tab-content { display: none; }
            .tab-content.active { display: block; }
            .movie-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(180px, 1fr)); gap: 20px; }
            .movie-card { background: #fff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 14px rgba(0,0,0,0.08); transition: transform .2s ease, box-shadow .2s ease; display: flex; flex-direction: column; height: 100%; }
            .movie-card:hover { transform: translateY(-4px); box-shadow: 0 8px 24px rgba(0,0,0,0.12); }
            .movie-thumb { width: 100%; aspect-ratio: 2 / 3; background: #f1f1f1; }
            .movie-thumb img { width: 100%; height: 100%; object-fit: cover; display: block; }
            .movie-meta { padding: 10px 12px; display: flex; flex-direction: column; gap: 6px; flex: 1; }
            .movie-title { font-size: 15px; font-weight: 700; color: #222; line-height: 1.3; min-height: 40px; }
            .movie-subinfo { 
                font-size: 12px; 
                color: #666; 
                display: flex;
                flex-direction: column;
                gap: 4px;
            }
            .movie-subinfo .age-rating {
                align-self: flex-start;
                margin-bottom: 2px;
            }
            .movie-subinfo .badge,
            .movie-subinfo span:not(.age-rating) {
                margin-left: 0;
            }
            .badge { font-size: 11px; background:#f4f6ff; color:#1a3a7d; padding:2px 8px; border-radius:12px; display:inline-block; }
            .age-rating { 
                font-size: 11px; 
                color:#fff; 
                padding:3px 6px; 
                border-radius:4px; 
                display:inline-block; 
                font-weight:700; 
                min-width: 24px;
                text-align: center;
                box-shadow: 0 2px 4px rgba(0,0,0,0.2);
            }
            .age-rating[data-age="0"] { background: #4CAF50; } /* Xanh lá - G (0+) */
            .age-rating[data-age="6"] { background: #FF9800; } /* Cam - PG (6+) */
            .age-rating[data-age="13"] { background: #FF5722; } /* Đỏ cam - PG-13 (13+) */
            .age-rating[data-age="16"] { background: #E91E63; } /* Hồng - R (16+) */
            .age-rating[data-age="18"] { background: #9C27B0; } /* Tím - NC-17 (18+) */
            .rating { color:#ff9800; font-weight:700; font-size:12px; }
            .card-actions { padding: 10px 12px 14px; }
            .btn-primary { display:block; width:100%; text-align:center; text-decoration:none; background:#e50914; color:#fff; font-weight:700; padding:10px 12px; border-radius:6px; }
            .btn-primary:hover { background:#c90812; }

            /* ==== PROMOTION SECTION ==== */
            .promo-section { background: #fff; padding: 40px 0; margin: 40px 0; }
            .promo-section h2 { text-align: center; font-size: 22px; color: #1a3a7d; margin-bottom: 20px; }
            .promo-list { display: flex; justify-content: center; gap: 30px; flex-wrap: wrap; }
            .promo-card { width: 300px; border-radius: 8px; overflow: hidden; box-shadow: 0 3px 10px rgba(0,0,0,0.1); }
            .promo-card img { width: 100%; height: 180px; object-fit: cover; }
            .promo-card p { padding: 10px; text-align: center; font-size: 15px; }
        </style>
    </head>
    <body>
        <jsp:include page="../layout/header.jsp" />

        <div class="slider">
            <div class="slides">
                <div class="slide"><img src="https://media.lottecinemavn.com/Media/WebAdmin/7ce48d93c8074902b0b5a6ba82e85351.jpg" alt="Banner 1"></div>
                <div class="slide"><img src="https://via.placeholder.com/1200x380?text=Slide+2" alt="Banner 2"></div>
                <div class="slide"><img src="https://via.placeholder.com/1200x380?text=Slide+3" alt="Banner 3"></div>
            </div>
            <div class="slider-nav">
                <span class="dot active"></span>
                <span class="dot"></span>
                <span class="dot"></span>
            </div>
        </div>

        <div class="home-body">
            <div class="tabs">
                <button class="tab-btn active" data-tab="now">Đang chiếu</button>
                <button class="tab-btn" data-tab="upcoming">Sắp chiếu</button>
            </div>

            <div id="tab-now" class="tab-content active">
                <c:choose>
                    <c:when test="${empty nowShowingMovies}">
                        <p style="text-align:center;color:#666;padding:20px 0;">Không có phim nào đang chiếu</p>
                    </c:when>
                    <c:otherwise>
                        <div class="movie-grid">
                            <c:forEach var="movie" items="${nowShowingMovies}">
                                <div class="movie-card">
                                    <c:url var="movieDetailUrl" value="movie?action=detail&maPhim=${movie.maPhim}"/>
                                    <c:url var="moviePosterUrl" value="/assets/image/${movie.poster}"/>
                                    <a class="movie-thumb" href="${movieDetailUrl}">
                                        <img src="${moviePosterUrl}" alt="<c:out value="${movie.tenPhim}"/>" onerror="this.src='https://via.placeholder.com/300x450?text=No+Image'">
                                    </a>
                                    <div class="movie-meta">
                                        <div class="movie-title"><c:out value="${movie.tenPhim}"/></div>
                                        <div class="movie-subinfo">
                                            <span class="age-rating" data-age="${movie.doTuoiGioiHan}">${movie.doTuoiGioiHan}+</span>
                                            <span class="badge"><c:out value="${movie.theLoai}"/></span>
                                            <span><c:out value="${movie.thoiLuong}"/> phút</span>
                                        </div>
                                        <c:if test="${not empty avgRatingsMap[movie.maPhim]}">
                                            <div class="rating">⭐ <fmt:formatNumber value="${avgRatingsMap[movie.maPhim]}" maxFractionDigits="1"/> / 10</div>
                                        </c:if>
                                    </div>
                                    <div class="card-actions">
                                        <a class="btn-primary" href="showtime-selection?maPhim=${movie.maPhim}">Đặt vé ngay</a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div id="tab-upcoming" class="tab-content">
                <c:choose>
                    <c:when test="${empty upcomingMovies}">
                        <p style="text-align:center;color:#666;padding:20px 0;">Không có phim nào sắp chiếu</p>
                    </c:when>
                    <c:otherwise>
                        <div class="movie-grid">
                            <c:forEach var="movie" items="${upcomingMovies}">
                                <div class="movie-card">
                                    <c:url var="movieDetailUrl" value="movie?action=detail&maPhim=${movie.maPhim}"/>
                                    <c:url var="moviePosterUrl" value="/assets/image/${movie.poster}"/>
                                    <a class="movie-thumb" href="${movieDetailUrl}">
                                        <img src="${moviePosterUrl}" alt="<c:out value="${movie.tenPhim}"/>" onerror="this.src='https://via.placeholder.com/300x450?text=No+Image'">
                                    </a>
                                    <div class="movie-meta">
                                        <div class="movie-title"><c:out value="${movie.tenPhim}"/></div>
                                        <div class="movie-subinfo">
                                            <span class="age-rating" data-age="${movie.doTuoiGioiHan}">${movie.doTuoiGioiHan}+</span>
                                            <span class="badge"><c:out value="${movie.theLoai}"/></span>
                                            <c:if test="${movie.ngayKhoiChieu != null}">
                                                <span style="color:#e67e22;">
                                                    <c:out value="${dateLabelMap[movie.maPhim]}"/>
                                                </span>
                                            </c:if>
                                        </div>
                                        <c:if test="${not empty avgRatingsMap[movie.maPhim]}">
                                            <div class="rating">⭐ <fmt:formatNumber value="${avgRatingsMap[movie.maPhim]}" maxFractionDigits="1"/> / 10</div>
                                        </c:if>
                                    </div>
                                    <div class="card-actions">
                                        <a class="btn-primary" href="${movieDetailUrl}">Xem chi tiết</a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

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

        <jsp:include page="../layout/footer.jsp" />

        <script>
            // Tabs switching
            document.addEventListener('DOMContentLoaded', function() {
                const tabs = document.querySelectorAll('.tab-btn');
                const nowTab = document.getElementById('tab-now');
                const upTab = document.getElementById('tab-upcoming');
                tabs.forEach(btn => {
                    btn.addEventListener('click', function() {
                        tabs.forEach(b => b.classList.remove('active'));
                        this.classList.add('active');
                        const tab = this.getAttribute('data-tab');
                        if (tab === 'now') { nowTab.classList.add('active'); upTab.classList.remove('active'); }
                        else { upTab.classList.add('active'); nowTab.classList.remove('active'); }
                    });
                });

                // Simple slider
                const slides = document.querySelector('.slides');
                const dots = document.querySelectorAll('.dot');
                let current = 0;
                function setSlide(i) {
                    slides.style.transform = 'translateX(' + (-i * 100) + '%)';
                    dots.forEach((d, idx) => d.classList.toggle('active', idx === i));
                    current = i;
                }
                dots.forEach((d, idx) => d.addEventListener('click', () => setSlide(idx)));
                setInterval(() => setSlide((current + 1) % dots.length), 5000);
            });
        </script>
    </body>
</html>