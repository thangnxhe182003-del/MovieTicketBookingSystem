<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

        <!-- Showtime Selection -->
        <div style="display: flex; align-items: center; gap: 15px; margin-bottom: 20px;">
            <a href="movie?action=detail&maPhim=${movie.maPhim}" style="color: #e50914; text-decoration: none; font-weight: 600;">← Quay lại</a>
            <h2 style="margin: 0; color: #333;">Chọn suất chiếu</h2>
        </div>
        <form id="showtimeForm" method="post" action="showtime-selection">
            <input type="hidden" name="maPhim" value="${movie.maPhim}">
            <input type="hidden" name="maSuatChieu" id="selectedShowtime" value="">
            
            <c:forEach var="dateEntry" items="${showtimesByDate}">
                <div style="margin-bottom: 30px;">
                    <h3 style="color: #333; margin-bottom: 15px; padding: 10px; background: #f8f9fa; border-radius: 6px;">
                        ${dateEntry.key}
                    </h3>
                    <div class="showtime-grid">
                        <c:forEach var="showtime" items="${dateEntry.value}">
                            <div class="showtime-card" onclick="selectShowtime(${showtime.maSuatChieu})">
                                <div class="showtime-time">
                                    ${showtime.gioBatDau.format(timeFormatter)} - ${showtime.gioKetThuc.format(timeFormatter)}
                                </div>
                                <div class="showtime-room">${showtime.tenRap} - Phòng ${showtime.tenPhong}</div>
                                <div class="showtime-language">${showtime.ngonNguAmThanh}</div>
<!--                                <div class="showtime-price">${showtime.giaVeCoSo} VNĐ</div>-->
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:forEach>
            
            <button type="submit" class="btn-continue" id="continueBtn" disabled>Tiếp tục</button>
        </form>
    </div>
</div>

<script>
    function selectShowtime(maSuatChieu) {
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
