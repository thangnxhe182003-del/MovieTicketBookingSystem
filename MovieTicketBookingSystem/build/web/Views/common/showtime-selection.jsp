<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Chọn suất chiếu"/>
    <jsp:param name="extraStyles" value="
        <style>
            .page-screen { min-height: 100vh; padding: 20px; background: var(--light-bg); }
            .container { max-width: 1200px; margin: 0 auto; }
            .movie-info { background: #fff; border-radius: 12px; padding: 20px; margin-bottom: 20px; box-shadow: 0 4px 12px rgba(0,0,0,.1); }
            .movie-poster { width: 80px; height: 120px; object-fit: cover; border-radius: 8px; }
            .cinema-info { background: #fff; border-radius: 12px; padding: 20px; margin-bottom: 20px; box-shadow: 0 4px 12px rgba(0,0,0,.1); }
            .showtime-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px; }
            .showtime-card { background: #fff; border: 2px solid #ddd; border-radius: 8px; padding: 15px; text-align: center; cursor: pointer; transition: all 0.3s; }
            .showtime-card:hover { border-color: #e50914; transform: translateY(-2px); }
            .showtime-card.selected { border-color: #e50914; background: #ffe6e6; }
            .showtime-time { font-size: 18px; font-weight: 700; color: #e50914; margin-bottom: 5px; }
            .showtime-room { color: #666; font-size: 14px; margin-bottom: 5px; }
            .showtime-language { color: #888; font-size: 12px; font-style: italic; margin-bottom: 5px; }
            .showtime-price { color: #333; font-weight: 600; }
            .btn-continue { background: #e50914; color: #fff; padding: 12px 24px; border: none; border-radius: 6px; font-weight: 600; cursor: pointer; margin-top: 20px; }
            .btn-continue:disabled { background: #ccc; cursor: not-allowed; }
        </style>
    "/>
</jsp:include>

<div class="page-screen">
    <div class="container">
        <!-- Movie Info -->
        <div class="movie-info">
            <div style="display: flex; align-items: center; gap: 20px;">
                <img src="${pageContext.request.contextPath}/assets/image/${movie.poster}" alt="${movie.tenPhim}" class="movie-poster" onerror="this.src='https://via.placeholder.com/80x120?text=No+Image'">
                <div>
                    <h1 style="margin: 0 0 8px 0; color: #e50914;">${movie.tenPhim}</h1>
                    <p style="margin: 0; color: #666;">Thời lượng: ${movie.thoiLuong} phút</p>
                    <p style="margin: 0; color: #666;">Thể loại: ${movie.theLoai}</p>
                </div>
            </div>
        </div>

        <!-- Filter Section -->
        <div style="background: #fff; border-radius: 12px; padding: 20px; margin-bottom: 20px; box-shadow: 0 4px 12px rgba(0,0,0,.1);">
            <form method="GET" action="showtime-selection" style="display: flex; gap: 15px; align-items: flex-end; flex-wrap: wrap;">
                <input type="hidden" name="maPhim" value="${movie.maPhim}">
                <div style="flex: 1; min-width: 200px;">
                    <label style="display: block; font-weight: 600; color: #333; margin-bottom: 8px; font-size: 14px;">
                        <i class="fas fa-building"></i> Rạp chiếu
                    </label>
                    <select name="cinema" style="width: 100%; padding: 10px; border: 2px solid #ddd; border-radius: 8px; font-size: 14px; background: #fff;">
                        <option value="">Tất cả rạp</option>
                        <c:forEach var="cinemaName" items="${cinemaNames}">
                            <option value="${cinemaName}" ${cinemaName == selectedCinema ? 'selected' : ''}>${cinemaName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div style="flex: 1; min-width: 200px;">
                    <label style="display: block; font-weight: 600; color: #333; margin-bottom: 8px; font-size: 14px;">
                        <i class="fas fa-calendar-alt"></i> Ngày chiếu
                    </label>
                    <input type="date" 
                           name="date" 
                           value="${selectedDate}" 
                           min="<%= java.time.LocalDate.now().toString() %>"
                           style="width: 100%; padding: 10px; border: 2px solid #ddd; border-radius: 8px; font-size: 14px; background: #fff;">
                </div>
                <div style="display: flex; gap: 10px;">
                    <button type="submit" style="background: #e50914; color: #fff; padding: 10px 24px; border: none; border-radius: 8px; font-weight: 600; cursor: pointer; white-space: nowrap;">
                        <i class="fas fa-search"></i> Lọc
                    </button>
                    <a href="showtime-selection?maPhim=${movie.maPhim}" 
                       style="background: #f0f0f0; color: #333; padding: 10px 20px; border: none; border-radius: 8px; font-weight: 600; cursor: pointer; text-decoration: none; display: inline-flex; align-items: center; white-space: nowrap;">
                        <i class="fas fa-redo"></i> Reset
                    </a>
                </div>
            </form>
        </div>

        <!-- Showtime Selection -->
        <div style="display: flex; align-items: center; gap: 15px; margin-bottom: 20px;">
            <a href="movie?action=detail&maPhim=${movie.maPhim}" style="color: #e50914; text-decoration: none; font-weight: 600;">← Quay lại</a>
            <h2 style="margin: 0; color: #333;">Chọn suất chiếu</h2>
        </div>
        <form id="showtimeForm" method="post" action="booking?action=selectSeats">
            <input type="hidden" name="maPhim" value="${movie.maPhim}">
            <input type="hidden" name="maSuatChieu" id="selectedShowtime" value="">
            
            <c:choose>
                <c:when test="${empty showtimesByDate}">
                    <div style="text-align: center; padding: 60px 20px; background: #fff; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,.1);">
                        <i class="fas fa-calendar-times" style="font-size: 48px; color: #ccc; margin-bottom: 20px;"></i>
                        <h3 style="color: #666; margin-bottom: 10px;">Không có suất chiếu nào</h3>
                        <p style="color: #999; margin-bottom: 20px;">Vui lòng thử chọn rạp hoặc ngày khác</p>
                        <a href="showtime-selection?maPhim=${movie.maPhim}" style="display: inline-block; background: #e50914; color: #fff; padding: 10px 20px; border-radius: 6px; text-decoration: none; font-weight: 600;">
                            <i class="fas fa-redo"></i> Xóa bộ lọc
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="dateEntry" items="${showtimesByDate}">
                        <div style="margin-bottom: 30px;">
                            <h3 style="color: #333; margin-bottom: 15px; padding: 10px; background: #f8f9fa; border-radius: 6px;">
                                ${dateEntry.key}
                            </h3>
                            <div class="showtime-grid">
                                <c:forEach var="showtime" items="${dateEntry.value}">
                                    <div class="showtime-card" onclick="selectShowtime(${showtime.maSuatChieu}, event)">
                                        <div class="showtime-time">
                                            <c:if test="${showtime.gioBatDau != null}">
                                                ${showtime.gioBatDau.format(timeFormatter)}
                                            </c:if>
                                            <c:if test="${showtime.gioKetThuc != null}">
                                                 - ${showtime.gioKetThuc.format(timeFormatter)}
                                            </c:if>
                                        </div>
                                        <div class="showtime-room">${showtime.tenRap} - Phòng ${showtime.tenPhong}</div>
                                        <div class="showtime-language">${showtime.ngonNguAmThanh}</div>
                                        <div class="showtime-price">
                                            <fmt:formatNumber value="${showtime.giaVeCoSo}" pattern="#,###"/>đ
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            
            <button type="submit" class="btn-continue" id="continueBtn" disabled>Tiếp tục</button>
        </form>
    </div>
</div>

<script>
    function selectShowtime(maSuatChieu, event) {
        // Remove previous selection
        document.querySelectorAll('.showtime-card').forEach(card => {
            card.classList.remove('selected');
        });
        
        // Add selection to clicked card
        event.currentTarget.classList.add('selected');
        
        // Set hidden input value
        document.getElementById('selectedShowtime').value = maSuatChieu;
        
        // Enable continue button
        document.getElementById('continueBtn').disabled = false;
    }
</script>

<jsp:include page="../layout/footer.jsp" />
