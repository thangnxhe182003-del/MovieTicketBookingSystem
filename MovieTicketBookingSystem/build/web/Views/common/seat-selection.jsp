<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Chọn ghế"/>
    <jsp:param name="extraStyles" value="
        <style>
            .page-screen { min-height: 100vh; padding: 20px; background: var(--light-bg); }
            .container { max-width: 1200px; margin: 0 auto; }
            .movie-info { background: #fff; border-radius: 12px; padding: 20px; margin-bottom: 20px; box-shadow: 0 4px 12px rgba(0,0,0,.1); }
            .movie-poster { width: 80px; height: 120px; object-fit: cover; border-radius: 8px; }
            .showtime-info { background: #fff; border-radius: 12px; padding: 20px; margin-bottom: 20px; box-shadow: 0 4px 12px rgba(0,0,0,.1); }
            .seat-container { background: #fff; border-radius: 12px; padding: 20px; margin-bottom: 20px; box-shadow: 0 4px 12px rgba(0,0,0,.1); }
            .screen { background: #333; color: #fff; text-align: center; padding: 10px; margin-bottom: 20px; border-radius: 4px; }
            .seat-grid { display: grid; grid-template-columns: repeat(10, 1fr); gap: 8px; max-width: 500px; margin: 0 auto; }
            .seat { width: 40px; height: 40px; border: 2px solid #ddd; border-radius: 4px; display: flex; align-items: center; justify-content: center; cursor: pointer; font-size: 12px; font-weight: 600; }
            .seat.available { background: #fff; color: #333; }
            .seat.available:hover { background: #e6f3ff; border-color: #e50914; }
            .seat.selected { background: #e50914; color: #fff; border-color: #e50914; }
            .seat.occupied { background: #ccc; color: #666; cursor: not-allowed; }
            .seat.vip { border-color: #ffd700; }
            .seat-legend { display: flex; justify-content: center; gap: 20px; margin-bottom: 20px; }
            .legend-item { display: flex; align-items: center; gap: 8px; }
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

        <!-- Showtime Info -->
        <div class="showtime-info">
            <h2 style="margin: 0 0 8px 0; color: #e50914;">${showtime.tenRap}</h2>
            <p style="margin: 0; color: #666;">${showtime.ngayChieu} - ${showtime.gioBatDau} - Phòng ${showtime.tenPhong}</p>
        </div>

        <!-- Seat Selection -->
        <div class="seat-container">
            <div style="display: flex; align-items: center; gap: 15px; margin-bottom: 20px;">
                <a href="showtime-selection?maPhim=${movie.maPhim}" style="color: #e50914; text-decoration: none; font-weight: 600;">← Chọn lại suất chiếu</a>
                <h2 style="margin: 0; color: #333;">Chọn ghế</h2>
            </div>
            <div class="screen">MÀN HÌNH</div>
            
            <div class="seat-legend">
                <div class="legend-item">
                    <div class="seat available" style="pointer-events: none;"></div>
                    <span>Ghế trống</span>
                </div>
                <div class="legend-item">
                    <div class="seat selected" style="pointer-events: none;"></div>
                    <span>Đã chọn</span>
                </div>
                <div class="legend-item">
                    <div class="seat occupied" style="pointer-events: none;"></div>
                    <span>Đã bán</span>
                </div>
                <div class="legend-item">
                    <div class="seat vip" style="pointer-events: none;"></div>
                    <span>VIP</span>
                </div>
            </div>
            
            <form id="seatForm" method="post" action="seat-selection">
                <input type="hidden" name="maPhim" value="${movie.maPhim}">
                <input type="hidden" name="maSuatChieu" value="${showtime.maSuatChieu}">
                <input type="hidden" name="selectedSeats" id="selectedSeats" value="">
                
                <div class="seat-grid">
                    <c:forEach var="seat" items="${seats}">
                        <div class="seat ${seat.trangThai == 'Available' ? 'available' : 'occupied'} ${seat.loaiGhe == 'VIP' ? 'vip' : ''}" 
                             data-seat-id="${seat.maGhe}" 
                             data-seat-number="${seat.soGhe}"
                             onclick="toggleSeat(this)">
                            ${seat.soGhe}
                        </div>
                    </c:forEach>
                </div>
                
                <button type="submit" class="btn-continue" id="continueBtn" disabled>Tiếp tục</button>
            </form>
        </div>
    </div>
</div>

<script>
    let selectedSeats = [];
    
    function toggleSeat(seatElement) {
        if (seatElement.classList.contains('occupied')) {
            return; // Can't select occupied seats
        }
        
        const seatId = seatElement.dataset.seatId;
        const seatNumber = seatElement.dataset.seatNumber;
        
        if (seatElement.classList.contains('selected')) {
            // Deselect seat
            seatElement.classList.remove('selected');
            selectedSeats = selectedSeats.filter(id => id !== seatId);
        } else {
            // Select seat
            seatElement.classList.add('selected');
            selectedSeats.push(seatId);
        }
        
        // Update hidden input
        document.getElementById('selectedSeats').value = selectedSeats.join(',');
        
        // Enable/disable continue button
        document.getElementById('continueBtn').disabled = selectedSeats.length === 0;
    }
</script>

<jsp:include page="../layout/footer.jsp" />
