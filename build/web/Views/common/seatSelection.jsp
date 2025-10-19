<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Chọn ghế"/>
    <jsp:param name="extraStyles" value="
        <style>
            .seat-selection-page {
                background: #f5f5f5;
                padding: 30px 0;
                min-height: 100vh;
            }

            .seat-container {
                max-width: 1000px;
                margin: 0 auto;
                padding: 0 20px;
            }

            .seat-header {
                background: white;
                padding: 20px;
                border-radius: 8px;
                margin-bottom: 20px;
                box-shadow: 0 3px 10px rgba(0,0,0,0.1);
                display: grid;
                grid-template-columns: 120px 1fr auto;
                gap: 20px;
                align-items: center;
            }

            .seat-poster img {
                width: 100%;
                border-radius: 6px;
                height: auto;
            }

            .seat-info h1 {
                font-size: 24px;
                margin: 0 0 10px 0;
                color: #222;
            }

            .seat-info p {
                color: #666;
                margin: 5px 0;
                font-size: 14px;
            }

            .seat-main {
                background: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 3px 10px rgba(0,0,0,0.1);
            }

            .section-title {
                font-size: 20px;
                font-weight: bold;
                color: #222;
                margin-bottom: 20px;
                text-align: center;
                border-bottom: 2px solid #e0e0e0;
                padding-bottom: 10px;
            }

            .screen {
                text-align: center;
                margin-bottom: 30px;
                font-size: 16px;
                color: #999;
                font-weight: 600;
                text-transform: uppercase;
                letter-spacing: 2px;
            }

            .seat-layout {
                display: flex;
                flex-direction: column;
                gap: 10px;
                margin-bottom: 30px;
            }

            .seat-row {
                display: flex;
                justify-content: center;
                gap: 8px;
                align-items: center;
            }

            .row-label {
                width: 30px;
                text-align: center;
                font-weight: 600;
                color: #333;
                font-size: 14px;
            }

            .seats-group {
                display: flex;
                gap: 8px;
            }

            .seat {
                width: 40px;
                height: 40px;
                border: 2px solid #ddd;
                border-radius: 6px;
                background: #f0f0f0;
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 11px;
                font-weight: 600;
                color: #666;
                transition: all 0.3s;
                position: relative;
            }

            .seat:hover:not(.booked):not(.maintenance) {
                border-color: #e50914;
                transform: scale(1.1);
                background: #ffe0e0;
            }

            .seat.available {
                background: #e8f5e9;
                border-color: #4caf50;
                color: #2e7d32;
            }

            .seat.selected {
                background: #e50914;
                border-color: #c90812;
                color: white;
            }

            .seat.booked {
                background: #ccc;
                border-color: #999;
                color: #666;
                cursor: not-allowed;
            }

            .seat.maintenance {
                background: #ff9800;
                border-color: #f57c00;
                color: white;
                cursor: not-allowed;
            }

            .seat.vip {
                background: #fff3e0;
                border-color: #ff9800;
                color: #e65100;
            }

            .seat.couple {
                background: #fce4ec;
                border-color: #e91e63;
                color: #c2185b;
            }

            .seat.selected.vip,
            .seat.selected.couple {
                color: white;
            }

            .legend {
                display: flex;
                justify-content: center;
                flex-wrap: wrap;
                gap: 20px;
                padding: 20px;
                background: #f9f9f9;
                border-radius: 6px;
                margin-bottom: 20px;
            }

            .legend-item {
                display: flex;
                align-items: center;
                gap: 8px;
                font-size: 13px;
            }

            .legend-seat {
                width: 28px;
                height: 28px;
                border: 2px solid;
                border-radius: 4px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 10px;
                font-weight: 600;
            }

            .checkout-section {
                background: #f9f9f9;
                padding: 20px;
                border-radius: 8px;
                margin-top: 20px;
                border-top: 2px solid #e0e0e0;
            }

            .selected-seats-info {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 20px;
                margin-bottom: 20px;
            }

            .info-box {
                background: white;
                padding: 15px;
                border-radius: 6px;
                border-left: 4px solid #e50914;
            }

            .info-label {
                font-size: 12px;
                color: #999;
                text-transform: uppercase;
                margin-bottom: 5px;
            }

            .info-value {
                font-size: 18px;
                font-weight: bold;
                color: #333;
            }

            .price-breakdown {
                background: white;
                padding: 15px;
                border-radius: 6px;
                margin-bottom: 15px;
            }

            .price-row {
                display: flex;
                justify-content: space-between;
                margin: 8px 0;
                font-size: 14px;
            }

            .price-row.total {
                border-top: 2px solid #e0e0e0;
                padding-top: 10px;
                font-weight: bold;
                font-size: 18px;
                color: #e50914;
            }

            .action-buttons {
                display: flex;
                gap: 10px;
                justify-content: flex-end;
            }

            .btn {
                padding: 12px 24px;
                border: none;
                border-radius: 6px;
                font-size: 15px;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.3s;
            }

            .btn-primary {
                background: linear-gradient(135deg, #e50914, #c90812);
                color: white;
            }

            .btn-primary:hover:not(:disabled) {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(229, 9, 20, 0.4);
            }

            .btn-primary:disabled {
                opacity: 0.5;
                cursor: not-allowed;
            }

            .btn-secondary {
                background: #f0f0f0;
                color: #333;
            }

            .btn-secondary:hover {
                background: #e0e0e0;
            }

            @media (max-width: 768px) {
                .seat-header {
                    grid-template-columns: 1fr;
                    gap: 15px;
                }

                .seat {
                    width: 32px;
                    height: 32px;
                    font-size: 10px;
                }

                .selected-seats-info {
                    grid-template-columns: 1fr;
                }

                .action-buttons {
                    flex-direction: column;
                }

                .btn {
                    width: 100%;
                }
            }
        </style>
    "/>
</jsp:include>

<div class="seat-selection-page">
    <div class="seat-container">
        <!-- HEADER -->
        <div class="seat-header">
            <div class="seat-poster">
                <img src="${pageContext.request.contextPath}/assets/image/${movie.poster}" alt="${movie.tenPhim}" onerror="this.src='https://via.placeholder.com/120x160?text=No+Image'">
            </div>
            <div class="seat-info">
                <h1>${movie.tenPhim}</h1>
                <p>🎪 ${showtime.tenPhong}</p>
                <p>🏢 ${showtime.tenRap}</p>
                <p>📅 <fmt:formatDate value="${showtime.ngayChieu}" pattern="dd/MM/yyyy"/> 
                   ⏰ <fmt:formatDate value="${showtime.gioBatDau}" pattern="HH:mm"/></p>
            </div>
            <div style="text-align: right;">
                <div style="font-size: 12px; color: #999; margin-bottom: 10px;">Mã suất chiếu</div>
                <div style="font-size: 24px; font-weight: bold; color: #e50914;">${showtime.maSuatChieu}</div>
            </div>
        </div>

        <!-- SEAT LAYOUT -->
        <div class="seat-main">
            <h2 class="section-title">🎬 Chọn ghế</h2>

            <!-- LEGEND -->
            <div class="legend">
                <div class="legend-item">
                    <div class="legend-seat" style="background: #e8f5e9; border-color: #4caf50;"></div>
                    <span>Ghế trống</span>
                </div>
                <div class="legend-item">
                    <div class="legend-seat" style="background: #ccc; border-color: #999;"></div>
                    <span>Ghế đã đặt</span>
                </div>
                <div class="legend-item">
                    <div class="legend-seat" style="background: #fff3e0; border-color: #ff9800;"></div>
                    <span>Ghế VIP</span>
                </div>
                <div class="legend-item">
                    <div class="legend-seat" style="background: #fce4ec; border-color: #e91e63;"></div>
                    <span>Ghế Couple</span>
                </div>
            </div>

            <!-- SCREEN -->
            <div class="screen">📺 Màn hình</div>

            <!-- SEAT LAYOUT (A-K rows, 12 seats per row) -->
            <div class="seat-layout">
                <%
                    java.util.Map<String, java.util.List<Object>> seatsByRow = new java.util.LinkedHashMap<>();
                    for (Object obj : (java.util.List<?>) request.getAttribute("seats")) {
                        model.Seat s = (model.Seat) obj;
                        seatsByRow.computeIfAbsent(s.getHangGhe(), k -> new java.util.ArrayList<>()).add(s);
                    }
                    request.setAttribute("seatsByRow", seatsByRow);
                    java.util.List<Integer> bookedIds = (java.util.List<Integer>) request.getAttribute("bookedSeatIds");
                %>
                <c:forEach var="rowEntry" items="${seatsByRow}">
                    <div class="seat-row">
                        <div class="row-label">${rowEntry.key}</div>
                        <div class="seats-group">
                            <c:forEach var="seat" items="${rowEntry.value}">
                                <c:set var="isBooked" value="${bookedSeatIds.contains(seat.maGhe)}"/>
                                <c:set var="seatClass" value="seat available"/>
                                <c:if test="${isBooked}">
                                    <c:set var="seatClass" value="seat booked"/>
                                </c:if>
                                <c:if test="${not isBooked && 'VIP'.equalsIgnoreCase(seat.loaiGhe)}">
                                    <c:set var="seatClass" value="seat available vip"/>
                                </c:if>
                                <c:if test="${not isBooked && 'Couple'.equalsIgnoreCase(seat.loaiGhe)}">
                                    <c:set var="seatClass" value="seat available couple"/>
                                </c:if>

                                <c:set var="isAvailable" value="${not isBooked}"/>
                                <div class="${seatClass}" 
                                     data-seat-id="${seat.maGhe}" 
                                     data-seat-name="${seat.hangGhe}${seat.soGhe}"
                                     <c:if test="${isBooked}">style="cursor: not-allowed;"</c:if>>
                                    ${seat.hangGhe}${seat.soGhe}
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- CHECKOUT -->
            <div class="checkout-section">
                <div class="selected-seats-info">
                    <div class="info-box">
                        <div class="info-label">Ghế được chọn</div>
                        <div class="info-value" id="selectedSeatsDisplay">
                            <span id="seatCount">0</span> ghế
                        </div>
                    </div>
                    <div class="info-box">
                        <div class="info-label">Tổng tiền</div>
                        <div class="info-value" id="totalPriceDisplay" style="color: #e50914;">
                            ₫0
                        </div>
                    </div>
                </div>

                <div class="price-breakdown" id="priceBreakdown" style="display: none;">
                    <div id="priceDetails"></div>
                    <div class="price-row total">
                        <span>TỔNG CỘNG:</span>
                        <span id="finalTotal">₫0</span>
                    </div>
                </div>

                <div class="action-buttons">
                    <a href="booking?action=selectShowtime&maPhim=${movie.maPhim}" class="btn btn-secondary">
                        ← Chọn lại suất chiếu
                    </a>
                    <form id="bookingForm" method="POST" action="booking?action=confirmBooking&maSuatChieu=${showtime.maSuatChieu}" style="flex: 1;">
                        <input type="hidden" name="selectedSeats" id="selectedSeatsInput" value="">
                        <button type="submit" class="btn btn-primary" id="confirmBtn" disabled style="width: 100%;">
                            ✓ Xác nhận đặt vé
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    const selectedSeats = new Map(); // Map to store {seatId: {name, price}}
    const basePrice = parseFloat('<fmt:formatNumber value="${showtime.giaVeCoSo}" type="number"/>');
    
    function getSeatType(element) {
        if (element.classList.contains('vip')) return 'VIP';
        if (element.classList.contains('couple')) return 'Couple';
        return 'Thường';
    }

    function getPrice(seatClass) {
        if (seatClass.includes('vip')) {
            return basePrice * 1.2; // VIP +20%
        } else if (seatClass.includes('couple')) {
            return basePrice * 1.5; // Couple +50%
        }
        return basePrice;
    }

    function selectSeat(element) {
        if (element.classList.contains('booked')) return;

        if (element.classList.contains('selected')) {
            element.classList.remove('selected');
            const seatId = element.getAttribute('data-seat-id');
            selectedSeats.delete(seatId);
        } else {
            element.classList.add('selected');
            const seatId = element.getAttribute('data-seat-id');
            const seatName = element.getAttribute('data-seat-name');
            const price = getPrice(element.className);
            selectedSeats.set(seatId, {
                name: seatName,
                type: getSeatType(element),
                price: price
            });
        }

        updateSummary();
    }

    // Attach click handlers to all seat elements
    document.addEventListener('DOMContentLoaded', function() {
        document.querySelectorAll('.seat.available, .seat.vip, .seat.couple').forEach(seat => {
            seat.addEventListener('click', function() {
                selectSeat(this);
            });
        });
    });

    function updateSummary() {
        const count = selectedSeats.size;
        document.getElementById('seatCount').textContent = count;

        let total = 0;
        let details = '';
        selectedSeats.forEach((seat, seatId) => {
            total += seat.price;
            const seatInfo = '<div class="price-row"><span>' + seat.name + ' (' + seat.type + ')</span><span>₫' + seat.price.toLocaleString('vi-VN') + '</span></div>';
            details += seatInfo;
        });

        document.getElementById('selectedSeatsDisplay').innerHTML = count + ' ghế';
        document.getElementById('totalPriceDisplay').textContent = '₫' + total.toLocaleString('vi-VN');
        document.getElementById('finalTotal').textContent = '₫' + total.toLocaleString('vi-VN');
        
        if (count > 0) {
            document.getElementById('priceDetails').innerHTML = details;
            document.getElementById('priceBreakdown').style.display = 'block';
            document.getElementById('confirmBtn').disabled = false;
        } else {
            document.getElementById('priceBreakdown').style.display = 'none';
            document.getElementById('confirmBtn').disabled = true;
        }

        // Update hidden input with selected seat IDs
        const seatIds = Array.from(selectedSeats.keys()).join(',');
        document.getElementById('selectedSeatsInput').value = seatIds;
    }
</script>

<jsp:include page="../layout/footer.jsp" />
