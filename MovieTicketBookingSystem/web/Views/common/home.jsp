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
                max-width: 1200px;
                margin: 0 auto;
                background: #f5f5f5;
                overflow: hidden;
            }
            .slides { 
                display: flex; 
                transition: transform .4s ease; 
            }
            .slide { 
                min-width: 100%; 
                display: flex;
                align-items: center;
                justify-content: center;
                background: #f5f5f5;
            }
            .slide img { 
                width: 100%;
                height: auto;
                max-height: 500px;
                object-fit: contain;
                display: block;
            }
            .slider-nav { 
                position: absolute; 
                bottom: 15px; 
                left: 50%; 
                transform: translateX(-50%); 
                display: flex; 
                gap: 10px; 
                z-index: 10;
            }
            .dot { 
                height: 10px; 
                width: 10px; 
                background-color: rgba(255, 255, 255, 0.5); 
                border-radius: 50%; 
                cursor: pointer; 
                transition: all 0.3s ease;
            }
            .dot:hover {
                background-color: rgba(255, 255, 255, 0.8);
                transform: scale(1.2);
            }
            .dot.active { 
                background-color: #e50914; 
                width: 30px;
                border-radius: 5px;
            }
            
            /* Slider Navigation Arrows */
            .slider-nav-arrow {
                position: absolute;
                top: 50%;
                transform: translateY(-50%);
                background: rgba(0, 0, 0, 0.5);
                color: white;
                border: none;
                width: 50px;
                height: 50px;
                border-radius: 50%;
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 20px;
                z-index: 10;
                transition: all 0.3s ease;
            }
            
            .slider-nav-arrow:hover {
                background: rgba(0, 0, 0, 0.8);
                transform: translateY(-50%) scale(1.1);
            }
            
            .slider-nav-arrow.prev {
                left: 20px;
            }
            
            .slider-nav-arrow.next {
                right: 20px;
            }
            
            .slider-nav-arrow:disabled {
                opacity: 0.3;
                cursor: not-allowed;
            }
            
            .slider-nav-arrow:disabled:hover {
                transform: translateY(-50%);
                background: rgba(0, 0, 0, 0.5);
            }

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
            .movie-thumb img { 
                width: 100%; 
                height: 100%; 
                object-fit: cover; 
                display: block; 
                opacity: 0;
                transition: opacity 0.3s ease;
                background: #f0f0f0;
            }
            
            .movie-thumb img.loaded {
                opacity: 1;
            }
            
            .movie-thumb img.error {
                opacity: 1;
                background: linear-gradient(135deg, #f8f9fa, #e9ecef);
                display: flex;
                align-items: center;
                justify-content: center;
                color: #666;
                font-size: 12px;
                text-align: center;
            }
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
            <c:choose>
                <c:when test="${empty sliders}">
                    <!-- Default slider nếu không có slider nào -->
                    <div class="slides">
                        <div class="slide"><img src="https://via.placeholder.com/1200x380?text=No+Slider" alt="Default Banner"></div>
                    </div>
                    <div class="slider-nav">
                        <span class="dot active"></span>
                    </div>
                </c:when>
                <c:otherwise>
                    <button class="slider-nav-arrow prev" id="sliderPrev" aria-label="Previous slide">
                        <i class="fas fa-chevron-left"></i>
                    </button>
                    <button class="slider-nav-arrow next" id="sliderNext" aria-label="Next slide">
                        <i class="fas fa-chevron-right"></i>
                    </button>
                    <div class="slides" id="sliderSlides">
                        <c:forEach var="slider" items="${sliders}">
                            <div class="slide">
                                <img src="${pageContext.request.contextPath}/assets/image/${slider.anhSlide}" 
                                     alt="${slider.tieuDe}"
                                     data-fallback="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTIwMCIgaGVpZ2h0PSIzODAiIHZpZXdCb3g9IjAgMCAxMjAwIDM4MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjEyMDAiIGhlaWdodD0iMzgwIiBmaWxsPSIjRjhGOUZBIi8+CjxwYXRoIGQ9Ik02MDAgMTkwQzYwMCAxNzAuNzkxIDYxNS43OTEgMTU1IDYzNSAxNTVINjY1QzY4NC4yMDkgMTU1IDcwMCAxNzAuNzkxIDcwMCAxOTBWMjkwQzcwMCAzMDkuMjA5IDY4NC4yMDkgMzI1IDY2NSAzMjVINjM1QzYxNS43OTEgMzI1IDYwMCAzMDkuMjA5IDYwMCAyOTBWMTkwWiIgZmlsbD0iI0Q5RDlEOSIvPgo8c3ZnIHg9IjU4NSIgeT0iMTkwIiB3aWR0aD0iMzAiIGhlaWdodD0iMzAiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSI+CjxwYXRoIGQ9Ik0xMiAyTDEzLjA5IDguMjZMMjAgOUwxMy4wOSAxNS43NEwxMiAyMkwxMC45MSAxNS43NEw0IDlMMTAuOTEgOC4yNkwxMiAyWiIgZmlsbD0iIzk5OSIvPgo8L3N2Zz4KPC9zdmc+Cg==">
                            </div>
                        </c:forEach>
                    </div>
                    <div class="slider-nav" id="sliderNav">
                        <c:forEach var="slider" items="${sliders}" varStatus="status">
                            <span class="dot ${status.index == 0 ? 'active' : ''}" data-index="${status.index}"></span>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

            <div class="home-body">
                <c:if test="${not empty searchQuery}">
                    <div style="margin-bottom: 20px; padding: 15px; background: #f8f9fa; border-radius: 8px; border-left: 4px solid #e50914;">
                        <h2 style="margin: 0 0 8px 0; color: #333;">Kết quả tìm kiếm cho: "${searchQuery}"</h2>
                        <p style="margin: 0; color: #666;">Tìm thấy ${movies.size()} phim</p>
                    </div>
                </c:if>
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
                                        <img src="${moviePosterUrl}" 
                                             alt="<c:out value="${movie.tenPhim}"/>" 
                                             data-fallback="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzAwIiBoZWlnaHQ9IjQ1MCIgdmlld0JveD0iMCAwIDMwMCA0NTAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIzMDAiIGhlaWdodD0iNDUwIiBmaWxsPSIjRjhGOUZBIi8+CjxwYXRoIGQ9Ik0xNTAgODBDMTUwIDY1LjM2MTcgMTYzLjM2MiA1MiAxNzggNTJIMTIyQzEzNi42MzggNTIgMTUwIDY1LjM2MTcgMTUwIDgwVjM3MEMxNTAgMzg0LjYzOCAxMzYuNjM4IDM5OCAxMjIgMzk4SDE3OEMxNjMuMzYyIDM5OCAxNTAgMzg0LjYzOCAxNTAgMzcwVjgwWiIgZmlsbD0iI0Q5RDlEOSIvPgo8c3ZnIHg9IjEzNSIgeT0iMjAwIiB3aWR0aD0iMzAiIGhlaWdodD0iMzAiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSI+CjxwYXRoIGQ9Ik0xMiAyTDEzLjA5IDguMjZMMjAgOUwxMy4wOSAxNS43NEwxMiAyMkwxMC45MSAxNS43NEw0IDlMMTAuOTEgOC4yNkwxMiAyWiIgZmlsbD0iIzk5OSIvPgo8L3N2Zz4KPC9zdmc+Cg==">
                                    </a>
                                    <div class="movie-meta">
                                        <div class="movie-title"><c:out value="${movie.tenPhim}"/></div>
                                        <div class="movie-subinfo">
                                            <span class="age-rating" data-age="${movie.doTuoiGioiHan}">${movie.doTuoiGioiHan}+</span>
                                            <span>
                                                <c:forEach var="g" items="${movieIdToGenres[movie.maPhim]}">
                                                    <a href="home?genreId=${g.maTheLoai}" class="badge" style="text-decoration:none; margin-right:4px;">${g.tenTheLoai}</a>
                                                </c:forEach>
                                            </span>
                                            <span>
                                                <c:forEach var="d" items="${movieIdToDirectors[movie.maPhim]}">
                                                    <a href="home?directorId=${d.maDaoDien}" class="badge" style="text-decoration:none; background:#fff3cd; color:#856404; margin-right:4px;">${d.tenDaoDien}</a>
                                                </c:forEach>
                                            </span>
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
                                        <img src="${moviePosterUrl}" 
                                             alt="<c:out value="${movie.tenPhim}"/>" 
                                             data-fallback="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzAwIiBoZWlnaHQ9IjQ1MCIgdmlld0JveD0iMCAwIDMwMCA0NTAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIzMDAiIGhlaWdodD0iNDUwIiBmaWxsPSIjRjhGOUZBIi8+CjxwYXRoIGQ9Ik0xNTAgODBDMTUwIDY1LjM2MTcgMTYzLjM2MiA1MiAxNzggNTJIMTIyQzEzNi42MzggNTIgMTUwIDY1LjM2MTcgMTUwIDgwVjM3MEMxNTAgMzg0LjYzOCAxMzYuNjM4IDM5OCAxMjIgMzk4SDE3OEMxNjMuMzYyIDM5OCAxNTAgMzg0LjYzOCAxNTAgMzcwVjgwWiIgZmlsbD0iI0Q5RDlEOSIvPgo8c3ZnIHg9IjEzNSIgeT0iMjAwIiB3aWR0aD0iMzAiIGhlaWdodD0iMzAiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSI+CjxwYXRoIGQ9Ik0xMiAyTDEzLjA5IDguMjZMMjAgOUwxMy4wOSAxNS43NEwxMiAyMkwxMC45MSAxNS43NEw0IDlMMTAuOTEgOC4yNkwxMiAyWiIgZmlsbD0iIzk5OSIvPgo8L3N2Zz4KPC9zdmc+Cg==">
                                    </a>
                                    <div class="movie-meta">
                                        <div class="movie-title"><c:out value="${movie.tenPhim}"/></div>
                                        <div class="movie-subinfo">
                                            <span class="age-rating" data-age="${movie.doTuoiGioiHan}">${movie.doTuoiGioiHan}+</span>
                                            <span>
                                                <c:forEach var="g" items="${movieIdToGenres[movie.maPhim]}">
                                                    <a href="home?genreId=${g.maTheLoai}" class="badge" style="text-decoration:none; margin-right:4px;">${g.tenTheLoai}</a>
                                                </c:forEach>
                                            </span>
                                            <span>
                                                <c:forEach var="d" items="${movieIdToDirectors[movie.maPhim]}">
                                                    <a href="home?directorId=${d.maDaoDien}" class="badge" style="text-decoration:none; background:#fff3cd; color:#856404; margin-right:4px;">${d.tenDaoDien}</a>
                                                </c:forEach>
                                            </span>
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
            // Image loading handler
            function handleImageLoad(img) {
                img.classList.add('loaded');
            }
            
            function handleImageError(img) {
                img.classList.add('error');
                if (img.dataset.fallback) {
                    img.src = img.dataset.fallback;
                }
            }
            
            // Tabs switching
            document.addEventListener('DOMContentLoaded', function() {
                // Handle image loading
                document.querySelectorAll('img').forEach(img => {
                    if (img.complete) {
                        if (img.naturalHeight !== 0) {
                            handleImageLoad(img);
                        } else {
                            handleImageError(img);
                        }
                    } else {
                        img.addEventListener('load', () => handleImageLoad(img));
                        img.addEventListener('error', () => handleImageError(img));
                    }
                });
                
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
                const prevBtn = document.getElementById('sliderPrev');
                const nextBtn = document.getElementById('sliderNext');
                
                if (slides && dots.length > 0) {
                    let current = 0;
                    const totalSlides = dots.length;
                    
                    function setSlide(i) {
                        if (slides && dots.length > 0) {
                            slides.style.transform = 'translateX(' + (-i * 100) + '%)';
                            dots.forEach((d, idx) => d.classList.toggle('active', idx === i));
                            current = i;
                        }
                    }
                    
                    // Dot navigation
                    dots.forEach((d, idx) => d.addEventListener('click', () => setSlide(idx)));
                    
                    // Previous button - loop to last slide
                    if (prevBtn) {
                        prevBtn.addEventListener('click', () => {
                            if (current > 0) {
                                setSlide(current - 1);
                            } else {
                                setSlide(totalSlides - 1); // Loop to last
                            }
                        });
                    }
                    
                    // Next button - loop to first slide
                    if (nextBtn) {
                        nextBtn.addEventListener('click', () => {
                            if (current < totalSlides - 1) {
                                setSlide(current + 1);
                            } else {
                                setSlide(0); // Loop to first
                            }
                        });
                    }
                    
                    // Auto-play
                    let autoPlayInterval;
                    if (dots.length > 1) {
                        autoPlayInterval = setInterval(() => {
                            setSlide((current + 1) % dots.length);
                        }, 5000);
                        
                        // Pause on hover
                        const slider = document.querySelector('.slider');
                        if (slider) {
                            slider.addEventListener('mouseenter', () => {
                                if (autoPlayInterval) {
                                    clearInterval(autoPlayInterval);
                                }
                            });
                            slider.addEventListener('mouseleave', () => {
                                if (dots.length > 1) {
                                    autoPlayInterval = setInterval(() => {
                                        setSlide((current + 1) % dots.length);
                                    }, 5000);
                                }
                            });
                        }
                    }
                    
                    // Initialize
                    setSlide(0);
                }
                
                // Image loading for slider
                const sliderImages = document.querySelectorAll('.slider .slide img');
                sliderImages.forEach(img => {
                    img.onload = function() {
                        this.classList.add('loaded');
                    };
                    img.onerror = function() {
                        this.classList.add('error');
                        if (this.getAttribute('data-fallback')) {
                            this.src = this.getAttribute('data-fallback');
                        }
                    };
                    if (img.complete) {
                        if (img.naturalHeight !== 0) {
                            img.onload();
                        } else {
                            img.onerror();
                        }
                    }
                });
            });
        </script>
    </body>
</html>