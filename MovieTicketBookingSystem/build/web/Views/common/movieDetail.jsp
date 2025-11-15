<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chi tiết phim - MovieNow</title>
        <style>
            .movie-detail-page {
                background: #f5f5f5;
                padding: 30px 0;
                min-height: 100vh;
            }
            .detail-container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 0 20px;
                background: white;
                border-radius: 8px;
                box-shadow: 0 3px 15px rgba(0,0,0,0.1);
                overflow: hidden;
            }
            .detail-header {
                display: grid;
                grid-template-columns: 250px 1fr;
                gap: 30px;
                padding: 30px;
                align-items: start;
            }
            .movie-poster {
                width: 100%;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 5px 15px rgba(0,0,0,0.2);
            }
            .movie-poster img {
                width: 100%;
                height: auto;
                display: block;
                opacity: 0;
                transition: opacity 0.3s ease;
                background: #f0f0f0;
            }

            .movie-poster img.loaded {
                opacity: 1;
            }

            .movie-poster img.error {
                opacity: 1;
                background: linear-gradient(135deg, #f8f9fa, #e9ecef);
                display: flex;
                align-items: center;
                justify-content: center;
                color: #666;
                font-size: 14px;
                text-align: center;
            }
            .movie-info-section {
                padding: 10px 0;
            }
            .movie-title {
                font-size: 32px;
                font-weight: bold;
                color: #222;
                margin-bottom: 10px;
            }
            .movie-meta {
                display: grid;
                grid-template-columns: repeat(2, 1fr);
                gap: 15px;
                margin: 20px 0;
            }
            .meta-item {
                background: #f9f9f9;
                padding: 12px;
                border-left: 4px solid #e50914;
                border-radius: 4px;
            }
            .meta-label {
                font-size: 12px;
                color: #999;
                text-transform: uppercase;
                margin-bottom: 5px;
            }
            .meta-value {
                font-size: 16px;
                color: #333;
                font-weight: 500;
            }
            .rating-box {
                background: #fff7f7;
                border: 1px solid #ffd6d6;
                border-radius: 6px;
                padding: 12px 14px;
                display: inline-flex;
                align-items: baseline;
                gap: 8px;
                color: #e50914;
                font-weight: 700;
                margin: 10px 0 0 0;
            }
            .movie-description {
                margin: 20px 0;
                line-height: 1.6;
                color: #555;
                font-size: 15px;
            }
            .action-buttons {
                display: flex;
                gap: 15px;
                margin: 20px 0;
            }
            .btn {
                padding: 12px 24px;
                border: none;
                border-radius: 6px;
                font-size: 16px;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.3s;
            }
            .btn-primary {
                background: linear-gradient(135deg, #e50914, #c90812);
                color: white;
                flex: 1;
            }
            .btn-primary:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(229, 9, 20, 0.4);
            }
            .btn-secondary {
                background: #f0f0f0;
                color: #333;
                flex: 0.5;
            }
            .btn-secondary:hover {
                background: #e0e0e0;
            }
            .trailer-section, .showtime-section {
                padding: 30px;
                border-top: 1px solid #e0e0e0;
            }
            .section-title {
                font-size: 24px;
                font-weight: bold;
                color: #222;
                margin-bottom: 20px;
                border-left: 4px solid #e50914;
                padding-left: 10px;
            }
            .trailer-container {
                background: #000;
                aspect-ratio: 16 / 9;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 5px 15px rgba(0,0,0,0.2);
                margin-bottom: 20px;
            }
            .trailer-container iframe {
                width: 100%;
                height: 100%;
                border: none;
            }
            .trailer-list {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
                gap: 15px;
            }
            .trailer-item {
                background: #f9f9f9;
                border-radius: 6px;
                overflow: hidden;
                cursor: pointer;
                transition: transform 0.3s, box-shadow 0.3s;
                border: 2px solid transparent;
            }
            .trailer-item:hover {
                transform: translateY(-3px);
                box-shadow: 0 5px 15px rgba(229, 9, 20, 0.3);
                border-color: #e50914;
            }
            .trailer-item-thumb {
                width: 100%;
                aspect-ratio: 16 / 9;
                background: #000;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 40px;
                color: #e50914;
                overflow: hidden;
                border-radius: 8px;
            }

            .trailer-item-thumb img {
                width: 100%;
                height: 100%;
                object-fit: cover;
                opacity: 0;
                transition: opacity 0.3s ease;
                background: #f0f0f0;
            }

            .trailer-item-thumb img.loaded {
                opacity: 1;
            }

            .trailer-item-thumb img.error {
                opacity: 1;
                background: linear-gradient(135deg, #f8f9fa, #e9ecef);
                display: flex;
                align-items: center;
                justify-content: center;
                color: #666;
                font-size: 12px;
                text-align: center;
            }
            .trailer-item-info {
                padding: 10px;
                font-size: 13px;
                color: #555;
            }
            .showtime-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
                gap: 15px;
            }
            .showtime-card {
                background: #f9f9f9;
                border: 2px solid #e0e0e0;
                border-radius: 8px;
                padding: 15px;
                text-align: center;
                transition: all 0.3s;
                cursor: pointer;
            }
            .showtime-card:hover {
                border-color: #e50914;
                background: #fff;
                box-shadow: 0 5px 15px rgba(229, 9, 20, 0.2);
            }
            .showtime-date {
                font-size: 14px;
                color: #999;
                margin-bottom: 10px;
            }
            .showtime-time {
                font-size: 28px;
                font-weight: bold;
                color: #e50914;
                margin-bottom: 10px;
            }
            .showtime-room {
                font-size: 12px;
                color: #555;
                margin-bottom: 10px;
            }
            .showtime-price {
                font-size: 14px;
                color: #333;
                font-weight: 600;
                margin-bottom: 10px;
            }
            .showtime-card a {
                display: inline-block;
                background: #e50914;
                color: white;
                padding: 8px 16px;
                border-radius: 6px;
                text-decoration: none;
                font-size: 13px;
                font-weight: 600;
                transition: background 0.3s;
            }
            .showtime-card a:hover {
                background: #c90812;
            }
            .no-content {
                text-align: center;
                color: #999;
                padding: 40px 20px;
                font-size: 16px;
            }
            @media (max-width: 768px) {
                .detail-header {
                    grid-template-columns: 1fr;
                }
                .movie-meta {
                    grid-template-columns: 1fr;
                }
                .action-buttons {
                    flex-direction: column;
                }
                .btn-secondary {
                    flex: 1;
                }
                .showtime-grid {
                    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
                }
            }
        </style>
    </head>
    <body>
        <jsp:include page="../layout/header.jsp" />

        <div class="movie-detail-page">
            <div class="detail-container">
                <div class="detail-header">
                    <div class="movie-poster">
                        <img src="${pageContext.request.contextPath}/assets/image/${movie.poster}" 
                             alt="${movie.tenPhim}" 
                             data-fallback="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjUwIiBoZWlnaHQ9IjM1MCIgdmlld0JveD0iMCAwIDI1MCAzNTAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIyNTAiIGhlaWdodD0iMzUwIiBmaWxsPSIjRjhGOUZBIi8+CjxwYXRoIGQ9Ik0xMjUgNzBDMTI1IDU0LjM2MTcgMTM4LjM2MiA0MSAxNTUgNDFIMTU1QzE3MS42MzggNDEgMTg1IDU0LjM2MTcgMTg1IDcwVjI4MEMxODUgMjk2LjYzOCAxNzEuNjM4IDMxMCAxNTUgMzEwSDE1NUMxMzguMzYyIDMxMCAxMjUgMjk2LjYzOCAxMjUgMjgwVjcwWiIgZmlsbD0iI0Q5RDlEOSIvPgo8c3ZnIHg9IjExMCIgeT0iMTUwIiB3aWR0aD0iMzAiIGhlaWdodD0iMzAiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSI+CjxwYXRoIGQ9Ik0xMiAyTDEzLjA5IDguMjZMMjAgOUwxMy4wOSAxNS43NEwxMiAyMkwxMC45MSAxNS43NEw0IDlMMTAuOTEgOC4yNkwxMiAyWiIgZmlsbD0iIzk5OSIvPgo8L3N2Zz4KPC9zdmc+Cg==">
                    </div>
                    <div class="movie-info-section">
                        <h1 class="movie-title">${movie.tenPhim}</h1>
                        <c:if test="${not empty avgRating}">
                            <div class="rating-box">⭐ <fmt:formatNumber value="${avgRating}" maxFractionDigits="1"/> / 10
                                <span style="color:#999; font-weight:500; font-size:13px;">(${ratingCount} đánh giá)</span>
                            </div>
                        </c:if>
                        <div class="movie-meta">
                            <div class="meta-item"><div class="meta-label">Thể loại</div><div class="meta-value">
                                <c:forEach var="g" items="${genres}">
                                    <a href="${pageContext.request.contextPath}/home?genreId=${g.maTheLoai}" class="badge" style="text-decoration:none; margin-right:6px;">${g.tenTheLoai}</a>
                                </c:forEach>
                            </div></div>
                            <div class="meta-item"><div class="meta-label">Loại phim</div><div class="meta-value">${movie.loaiPhim}</div></div>
                            <div class="meta-item"><div class="meta-label">Thời lượng</div><div class="meta-value">${movie.thoiLuong} phút</div></div>
                            <div class="meta-item"><div class="meta-label">Giới hạn tuổi</div><div class="meta-value"><c:choose><c:when test="${movie.doTuoiGioiHan == 0}">Mọi lứa tuổi</c:when><c:otherwise>K${movie.doTuoiGioiHan}+</c:otherwise></c:choose></div></div>
                            <div class="meta-item"><div class="meta-label">Đạo diễn</div><div class="meta-value">
                                <c:forEach var="d" items="${directors}">
                                    <a href="${pageContext.request.contextPath}/home?directorId=${d.maDaoDien}" class="badge" style="text-decoration:none; background:#fff3cd; color:#856404; margin-right:6px;">${d.tenDaoDien}</a>
                                </c:forEach>
                            </div></div>
                            <div class="meta-item"><div class="meta-label">Ngôn ngữ phụ đề</div><div class="meta-value">${movie.ngonNguPhuDe}</div></div>
                        </div>
                        <div class="action-buttons">
                            <c:if test="${not empty showtimes}"><a href="showtime-selection?maPhim=${movie.maPhim}" class="btn btn-primary">🎫 Đặt vé ngay</a></c:if>
                            <c:if test="${empty showtimes}"><button class="btn btn-primary" disabled style="opacity: 0.6; cursor: not-allowed;">Sắp có suất chiếu</button></c:if>
                                <button class="btn btn-secondary" onclick="window.location.href = '${pageContext.request.contextPath}/home'">← Quay lại</button>

                            </div>
                            <div class="movie-description"><strong>Nội dung:</strong><br>${movie.noiDung}</div>
                        <div style="background: #f9f9f9; padding: 15px; border-radius: 6px; margin-top: 15px;"><strong>Diễn viên:</strong> ${movie.dienVien}</div>
                    </div>
                </div>
                <c:if test="${not empty showtimesByDate}">
                    <div class="showtime-section">
                        <h2 class="section-title">📅 Lịch chiếu</h2>
                        <c:forEach var="dateEntry" items="${showtimesByDate}">
                            <div style="margin-bottom: 25px;">
                                <h3 style="color: #333; margin-bottom: 15px; padding: 10px; background: #f8f9fa; border-radius: 6px;">
                                    📅 ${dateEntry.key.format(java.time.format.DateTimeFormatter.ofPattern('EEEE, dd/MM/yyyy', java.util.Locale.forLanguageTag('vi')))}
                                </h3>
                                <div class="showtime-grid">
                                    <c:forEach var="showtime" items="${dateEntry.value}">
                                        <div class="showtime-card">
                                            <div class="showtime-time">
                                                ${showtime.gioBatDau.format(java.time.format.DateTimeFormatter.ofPattern('HH:mm'))} - ${showtime.gioKetThuc.format(java.time.format.DateTimeFormatter.ofPattern('HH:mm'))}
                                            </div>
                                            <div class="showtime-room">🎪 ${showtime.tenPhong} - ${showtime.tenRap}</div>
                                            <div class="showtime-price">💰 <fmt:formatNumber value="${showtime.giaVeCoSo}" type="currency" currencySymbol="₫"/></div>
                                            <a href="booking?action=selectSeats&maSuatChieu=${showtime.maSuatChieu}">Chọn ghế</a>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>

                <c:if test="${empty showtimes}">
                    <div class="showtime-section">
                        <h2 class="section-title">📅 Lịch chiếu</h2>
                        <div class="no-content">Hiện tại phim chưa có suất chiếu nào. Vui lòng quay lại sau!</div>
                    </div>
                </c:if>

                <c:if test="${not empty trailers}">
                    <div class="trailer-section">
                        <h2 class="section-title">🎬 Trailer</h2>
                        <c:set var="mainTrailer" value="${trailers[0]}"/>
                        <c:set var="videoId" value="${fn:substringAfter(mainTrailer.linkTrailer, 'v=')}"/>
                        <div class="trailer-container">
                            <iframe src="https://www.youtube.com/embed/${videoId}?modestbranding=1&controls=1" allowfullscreen allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"></iframe>
                        </div>
                        <c:if test="${trailers.size() > 1}">
                            <div class="trailer-list">
                                <c:forEach var="trailer" items="${trailers}">
                                    <c:set var="vidId" value="${fn:substringAfter(trailer.linkTrailer, 'v=')}"/>
                                    <div class="trailer-item" onclick="document.querySelector('.trailer-container iframe').src = 'https://www.youtube.com/embed/${vidId}?modestbranding=1&controls=1'">
                                        <div class="trailer-item-thumb">▶</div>
                                        <div class="trailer-item-info"><strong>Trailer</strong><br><c:if test="${not empty trailer.moTa}">${fn:substring(trailer.moTa, 0, 50)}...</c:if></div>
                                        </div>
                                </c:forEach>
                            </div>
                        </c:if>
                    </div>
                </c:if>

                <c:if test="${not empty products}">
                    <div class="trailer-section">
                        <h2 class="section-title">🍿 Combo đồ ăn</h2>
                        <div class="trailer-list">
                            <c:forEach var="product" items="${products}">
                                <div class="trailer-item" style="cursor: default;">
                                    <div class="trailer-item-thumb">
                                        <img src="${pageContext.request.contextPath}/assets/image/${product.thumbnailUrl}" 
                                             alt="${product.tenSP}" 
                                             data-fallback="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjExMiIgdmlld0JveD0iMCAwIDIwMCAxMTIiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIyMDAiIGhlaWdodD0iMTEyIiBmaWxsPSIjRjhGOUZBIi8+CjxwYXRoIGQ9Ik0xMDAgMjhDMTAwIDI0LjY4NjMgMTAyLjY4NiAyMiAxMDYgMjJIMTA2QzEwOS4zMTQgMjIgMTEyIDI0LjY4NjMgMTEyIDI4Vjg0QzExMiA4Ny4zMTM3IDEwOS4zMTQgOTAgMTA2IDkwSDEwNkMxMDIuNjg2IDkwIDEwMCA4Ny4zMTM3IDEwMCA4NFYyOFoiIGZpbGw9IiNEOUQ5RDkiLz4KPHN2ZyB4PSI5MCIgeT0iNTAiIHdpZHRoPSIyMCIgaGVpZ2h0PSIyMCIgdmlld0JveD0iMCAwIDI0IDI0IiBmaWxsPSJub25lIj4KPHBhdGggZD0iTTEyIDJMMTMuMDkgOC4yNkwyMCA5TDEzLjA5IDE1Ljc0TDEyIDIyTDEwLjkxIDE1Ljc0TDQgOUwxMC45MSA4LjI2TDEyIDJaIiBmaWxsPSIjOTk5Ii8+Cjwvc3ZnPgo8L3N2Zz4K">
                                    </div>
                                    <div class="trailer-item-info">
                                        <strong>${product.tenSP}</strong><br>
                                        <span style="color: #e50914; font-weight: 600;"><fmt:formatNumber value="${product.donGia}" type="currency" currencySymbol="₫"/></span>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>


            </div>
        </div>

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

            document.addEventListener('DOMContentLoaded', function () {
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
            });
        </script>

        <jsp:include page="../layout/footer.jsp" />
    </body>
</html>
