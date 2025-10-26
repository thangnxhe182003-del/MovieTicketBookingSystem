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
                background: #e50914 !important;
                border-color: #c90812 !important;
                color: white !important;
                transform: scale(1.05);
                box-shadow: 0 0 10px rgba(229, 9, 20, 0.5);
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

            .total-summary-section {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 25px;
                border-radius: 16px;
                margin-top: 25px;
                box-shadow: 0 10px 30px rgba(102, 126, 234, 0.3);
                position: relative;
                overflow: hidden;
            }

            .total-summary-section::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%);
                pointer-events: none;
            }

            .total-summary-title {
                font-size: 20px;
                font-weight: 700;
                margin-bottom: 20px;
                display: flex;
                align-items: center;
                gap: 12px;
                position: relative;
                z-index: 1;
            }

            .total-summary-title i {
                color: #ffd700;
                font-size: 22px;
                text-shadow: 0 2px 4px rgba(0,0,0,0.3);
            }

            .total-summary-content {
                background: rgba(255, 255, 255, 0.15);
                padding: 20px;
                border-radius: 12px;
                backdrop-filter: blur(20px);
                border: 1px solid rgba(255, 255, 255, 0.2);
                position: relative;
                z-index: 1;
            }

            .total-breakdown {
                display: flex;
                flex-direction: column;
                gap: 12px;
            }

            .total-breakdown .price-row {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 12px 0;
                border-bottom: 1px solid rgba(255, 255, 255, 0.15);
                font-size: 15px;
                transition: all 0.3s ease;
            }

            .total-breakdown .price-row:hover {
                background: rgba(255, 255, 255, 0.1);
                border-radius: 8px;
                padding-left: 10px;
                padding-right: 10px;
            }

            .total-breakdown .price-row:last-child {
                border-bottom: none;
            }

            .total-breakdown .price-row.total {
                border-top: 2px solid rgba(255, 255, 255, 0.4);
                margin-top: 15px;
                padding-top: 20px;
                font-weight: 800;
                font-size: 18px;
                color: #ffd700;
                text-shadow: 0 2px 4px rgba(0,0,0,0.3);
                background: rgba(255, 215, 0, 0.1);
                border-radius: 10px;
                padding-left: 15px;
                padding-right: 15px;
            }

            .total-breakdown .price-row.total:hover {
                background: rgba(255, 215, 0, 0.2);
                transform: translateY(-2px);
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

            /* Combo Products Section */
            .combo-section {
                background: white;
                padding: 20px;
                border-radius: 8px;
                margin-bottom: 20px;
                box-shadow: 0 3px 10px rgba(0,0,0,0.1);
            }

            .combo-title {
                font-size: 18px;
                font-weight: 600;
                color: #222;
                margin-bottom: 15px;
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .combo-title i {
                color: #e50914;
            }

            .combo-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
                gap: 10px;
                margin-bottom: 20px;
            }

            .combo-item {
                border: 2px solid #e9ecef;
                border-radius: 8px;
                padding: 10px;
                cursor: pointer;
                transition: all 0.3s ease;
                background: white;
            }

            .combo-item:hover {
                border-color: #e50914;
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(229, 9, 20, 0.1);
            }

            .combo-item.selected {
                border-color: #e50914;
                background: #fff5f5;
            }

            .combo-image {
                width: 100%;
                height: 80px;
                object-fit: cover;
                border-radius: 6px;
                margin-bottom: 8px;
            }

            .combo-name {
                font-weight: 600;
                color: #222;
                margin-bottom: 5px;
                font-size: 12px;
                line-height: 1.3;
            }

            .combo-price {
                color: #e50914;
                font-weight: 700;
                font-size: 14px;
            }

            .combo-quantity {
                display: flex;
                align-items: center;
                gap: 10px;
                margin-top: 10px;
            }

            .quantity-btn {
                width: 30px;
                height: 30px;
                border: 1px solid #ddd;
                background: white;
                border-radius: 4px;
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: 600;
            }

            .quantity-btn:hover {
                background: #f8f9fa;
            }

            .quantity-input {
                width: 50px;
                text-align: center;
                border: 1px solid #ddd;
                border-radius: 4px;
                padding: 5px;
            }

            .combo-summary {
                background: #f8f9fa;
                padding: 15px;
                border-radius: 6px;
                margin-top: 15px;
            }

            .combo-summary-title {
                font-weight: 600;
                color: #222;
                margin-bottom: 10px;
            }

            .combo-total {
                display: flex;
                justify-content: space-between;
                align-items: center;
                font-weight: 600;
                color: #e50914;
                font-size: 16px;
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
                <p>🏢 ${room.tenRap}</p>
                <p>📅 ${formattedDate} ⏰ ${formattedStartTime}</p>
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
                                     data-seat-type="${seat.loaiGhe}"
                                     <c:if test="${isBooked}">style="cursor: not-allowed;"</c:if>>
                                    ${seat.hangGhe}${seat.soGhe}
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- CHECKOUT SECTION -->
            <div class="checkout-section">
                <div class="selected-seats-info">
                    <div class="info-box">
                        <div class="info-label">Ghế được chọn</div>
                        <div class="info-value" id="selectedSeatsDisplay">
                            <span id="seatCount">0</span> ghế
                        </div>
                    </div>
                    <div class="info-box">
                        <div class="info-label">Tổng tiền ghế</div>
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
            </div>

            <!-- Combo Products Section -->
            <div class="combo-section">
                <div class="combo-title">
                    <i class="fas fa-utensils"></i>
                    Chọn combo đồ ăn & thức uống
                </div>
                
                <div class="combo-grid">
                    <c:forEach var="product" items="${products}">
                        <c:if test="${'Active'.equals(product.trangThai)}">
                            <div class="combo-item" data-product-id="${product.maSP}" data-product-price="${product.donGia}">
                                <img src="${pageContext.request.contextPath}/assets/image/${product.thumbnailUrl}" alt="${product.tenSP}" class="combo-image" 
                                     onerror="this.src='https://via.placeholder.com/200x120?text=Combo'">
                                <div class="combo-name">${product.tenSP}</div>
                                <div class="combo-price">₫<fmt:formatNumber value="${product.donGia}" pattern="#,###"/></div>
                                <div class="combo-quantity" style="display: none;">
                                    <button type="button" class="quantity-btn minus" onclick="event.stopPropagation(); changeQuantity(${product.maSP}, -1)">-</button>
                                    <input type="number" class="quantity-input" id="qty_${product.maSP}" value="1" min="1" max="10" onclick="event.stopPropagation();">
                                    <button type="button" class="quantity-btn plus" onclick="event.stopPropagation(); changeQuantity(${product.maSP}, 1)">+</button>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
                
                <div class="combo-summary" id="comboSummary" style="display: none;">
                    <div class="combo-summary-title">Tóm tắt combo đã chọn:</div>
                    <div id="comboDetails"></div>
                    <div class="combo-total">
                        <span>Tổng combo:</span>
                        <span id="comboTotal">₫0</span>
                    </div>
                </div>
            </div>

            <!-- TOTAL SUMMARY SECTION -->
            <div class="total-summary-section">
                <div class="total-summary-title">
                    <i class="fas fa-calculator"></i>
                    Tóm tắt đơn hàng
                </div>
                <div class="total-summary-content">
                    <div id="totalBreakdown" class="total-breakdown">
                        <div class="price-row"><span>Ghế đã chọn:</span><span>₫0</span></div>
                        <div class="price-row"><span>Combo đã chọn:</span><span>₫0</span></div>
                        <div class="price-row total"><span>TỔNG CỘNG:</span><span id="grandTotalDisplay">₫0</span></div>
                    </div>
                </div>
            </div>
            <br>
            <div class="action-buttons">
                <a href="showtime-selection?maPhim=${movie.maPhim}" class="btn btn-secondary">
                    ← Chọn lại suất chiếu
                </a>
                <form id="bookingForm" method="POST" action="seat-selection" style="flex: 1;">
                    <input type="hidden" name="maPhim" value="${movie.maPhim}">
                    <input type="hidden" name="maSuatChieu" value="${showtime.maSuatChieu}">
                    <input type="hidden" name="selectedSeats" id="selectedSeatsInput" value="">
                    <input type="hidden" name="selectedProducts" id="selectedProductsInput" value="">
                    <button type="submit" class="btn btn-primary" id="confirmBtn" disabled style="width: 100%;">
                        ✓ Thanh Toán
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
    
    // Map seat prices by seat ID
    const seatPrices = new Map();
    <c:forEach var="priceEntry" items="${seatPrices}">
        seatPrices.set(${priceEntry.key}, ${priceEntry.value});
    </c:forEach>
    
    console.log('Base price:', basePrice);
    console.log('Seat prices map:', seatPrices);
    
    function getSeatType(element) {
        if (element.classList.contains('vip')) return 'VIP';
        if (element.classList.contains('couple')) return 'Couple';
        return 'Thường';
    }

    function getPrice(seatId) {
        const price = seatPrices.get(seatId) || basePrice;
        console.log('getPrice for seatId:', seatId, 'returned:', price);
        return price;
    }

    function selectSeat(element) {
        console.log('selectSeat called for:', element);
        
        if (element.classList.contains('booked') || element.classList.contains('maintenance')) {
            console.log('Seat is booked or maintenance, cannot select');
            return;
        }

        const seatId = element.getAttribute('data-seat-id');
        const seatName = element.getAttribute('data-seat-name');
        
        console.log('Seat ID:', seatId, 'Seat Name:', seatName);
        
        if (element.classList.contains('selected')) {
            // Bỏ chọn ghế
            console.log('Deselecting seat:', seatName);
            element.classList.remove('selected');
            selectedSeats.delete(seatId);
        } else {
            // Chọn ghế
            console.log('Selecting seat:', seatName);
            element.classList.add('selected');
            const price = getPrice(parseInt(seatId));
            console.log('Seat price:', price);
            selectedSeats.set(seatId, {
                name: seatName,
                type: getSeatType(element),
                price: price
            });
        }

        console.log('Current selected seats:', selectedSeats.size);
        updateSummary();
    }

    // Attach click handlers to all seat elements
    document.addEventListener('DOMContentLoaded', function() {
        // Attach click handlers to all available seats (not booked or maintenance)
        document.querySelectorAll('.seat').forEach(seat => {
            if (!seat.classList.contains('booked') && !seat.classList.contains('maintenance')) {
                seat.addEventListener('click', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    selectSeat(this);
                });
            }
        });
        
        console.log('Seat selection initialized. Available seats:', 
            document.querySelectorAll('.seat:not(.booked):not(.maintenance)').length);
        
        // Test: Log all seat elements
        document.querySelectorAll('.seat').forEach((seat, index) => {
            console.log(`Seat ${index}:`, seat.className, seat.getAttribute('data-seat-id'), seat.getAttribute('data-seat-name'));
        });
        
        // Test: Check if DOM elements exist
        console.log('DOM elements check:');
        console.log('- seatCount:', document.getElementById('seatCount'));
        console.log('- totalPriceDisplay:', document.getElementById('totalPriceDisplay'));
        console.log('- finalTotal:', document.getElementById('finalTotal'));
        console.log('- priceDetails:', document.getElementById('priceDetails'));
        console.log('- priceBreakdown:', document.getElementById('priceBreakdown'));
        console.log('- confirmBtn:', document.getElementById('confirmBtn'));
    });

    function updateSummary() {
        console.log('updateSummary called');
        console.log('selectedSeats Map:', selectedSeats);
        
        const count = selectedSeats.size;
        console.log('Seat count:', count);
        
        // Update seat count display
        const seatCountElement = document.getElementById('seatCount');
        if (seatCountElement) {
            seatCountElement.textContent = count;
        }

        let total = 0;
        let details = '';
        
        selectedSeats.forEach((seat, seatId) => {
            console.log('Processing seat:', seatId, seat);
            const price = parseFloat(seat.price);
            console.log('Seat price:', price);
            total += price;
            const seatInfo = '<div class="price-row"><span>' + seat.name + ' (' + seat.type + ')</span><span>₫' + price.toLocaleString('vi-VN') + '</span></div>';
            details += seatInfo;
        });

        console.log('Total calculated:', total);

        // Update displays
        const selectedSeatsDisplay = document.getElementById('selectedSeatsDisplay');
        const totalPriceDisplay = document.getElementById('totalPriceDisplay');
        const finalTotal = document.getElementById('finalTotal');
        const priceDetails = document.getElementById('priceDetails');
        const priceBreakdown = document.getElementById('priceBreakdown');
        const confirmBtn = document.getElementById('confirmBtn');
        
        if (selectedSeatsDisplay) {
            selectedSeatsDisplay.innerHTML = count + ' ghế';
        }
        
        if (totalPriceDisplay) {
            totalPriceDisplay.textContent = '₫' + total.toLocaleString('vi-VN');
        }
        
        if (finalTotal) {
            finalTotal.textContent = '₫' + total.toLocaleString('vi-VN');
        }
        
        if (count > 0) {
            if (priceDetails) {
                priceDetails.innerHTML = details;
            }
            if (priceBreakdown) {
                priceBreakdown.style.display = 'block';
            }
            if (confirmBtn) {
                confirmBtn.disabled = false;
            }
        } else {
            if (priceBreakdown) {
                priceBreakdown.style.display = 'none';
            }
            if (confirmBtn) {
                confirmBtn.disabled = true;
            }
        }

        // Update hidden input with selected seat IDs
        const seatIds = Array.from(selectedSeats.keys()).join(',');
        const selectedSeatsInput = document.getElementById('selectedSeatsInput');
        if (selectedSeatsInput) {
            selectedSeatsInput.value = seatIds;
        }
        
        console.log('Final total:', total);
        console.log('Selected seat IDs:', seatIds);
        
        // Cập nhật tổng kết
        updateTotalSummary();
    }
    
    // Combo Products Management
    const selectedProducts = new Map(); // Map to store {productId: {name, price, quantity}}
    
    // Initialize combo selection
    document.addEventListener('DOMContentLoaded', function() {
        // Add click handlers to combo items
        document.querySelectorAll('.combo-item').forEach(item => {
            item.addEventListener('click', function(e) {
                e.preventDefault();
                e.stopPropagation();
                selectCombo(this);
            });
        });
    });
    
    function selectCombo(element) {
        const productId = element.getAttribute('data-product-id');
        const productPrice = parseFloat(element.getAttribute('data-product-price'));
        const productName = element.querySelector('.combo-name').textContent;
        const quantityElement = element.querySelector('.combo-quantity');
        const quantityInput = element.querySelector('.quantity-input');
        
        console.log('selectCombo - productId:', productId, 'type:', typeof productId);
        
        if (element.classList.contains('selected')) {
            // Deselect combo
            element.classList.remove('selected');
            quantityElement.style.display = 'none';
            selectedProducts.delete(productId);
            console.log('Deselected product:', productId);
        } else {
            // Select combo
            element.classList.add('selected');
            quantityElement.style.display = 'flex';
            selectedProducts.set(productId, {
                name: productName,
                price: productPrice,
                quantity: 1
            });
            quantityInput.value = 1;
            console.log('Selected product:', productId, 'data:', selectedProducts.get(productId));
        }
        
        updateComboSummary();
        updateTotalSummary();
    }
    
    function changeQuantity(productId, change) {
        const quantityInput = document.getElementById('qty_' + productId);
        const currentQuantity = parseInt(quantityInput.value);
        const newQuantity = Math.max(1, Math.min(10, currentQuantity + change));
        
        quantityInput.value = newQuantity;
        
        // Debug log
        console.log('changeQuantity - productId:', productId, 'newQuantity:', newQuantity);
        console.log('selectedProducts before:', selectedProducts);
        
        // Tìm product trong selectedProducts (kiểm tra cả string và number)
        let found = false;
        for (let [key, value] of selectedProducts.entries()) {
            if (key == productId || key == productId.toString()) {
                value.quantity = newQuantity;
                found = true;
                console.log('Updated quantity for product:', productId, 'new quantity:', newQuantity);
                break;
            }
        }
        
        if (!found) {
            console.log('Product not found in selectedProducts:', productId);
            console.log('Available keys:', Array.from(selectedProducts.keys()));
        }
        
        updateComboSummary();
        updateTotalSummary();
    }
    
    function updateComboSummary() {
        const comboSummary = document.getElementById('comboSummary');
        const comboDetails = document.getElementById('comboDetails');
        const comboTotal = document.getElementById('comboTotal');
        const selectedProductsInput = document.getElementById('selectedProductsInput');
        
        if (selectedProducts.size === 0) {
            comboSummary.style.display = 'none';
            if (selectedProductsInput) {
                selectedProductsInput.value = '';
            }
            return;
        }
        
        comboSummary.style.display = 'block';
        
        let total = 0;
        let details = '';
        
        selectedProducts.forEach((product, productId) => {
            const subtotal = product.price * product.quantity;
            total += subtotal;
            details += '<div class="price-row"><span>' + product.name + ' x' + product.quantity + '</span><span>₫' + subtotal.toLocaleString('vi-VN') + '</span></div>';
        });
        
        if (comboDetails) {
            comboDetails.innerHTML = details;
        }
        
        if (comboTotal) {
            comboTotal.textContent = '₫' + total.toLocaleString('vi-VN');
        }
        
        // Update hidden input with selected products
        if (selectedProductsInput) {
            const productData = Array.from(selectedProducts.entries()).map(([id, product]) => 
                id + ':' + product.quantity
            ).join(',');
            selectedProductsInput.value = productData;
        }
    }
    
    function updateTotalSummary() {
        // Tính tổng giá ghế
        let seatTotal = 0;
        selectedSeats.forEach((seat) => {
            const price = parseFloat(seat.price);
            seatTotal += price;
        });
        
        // Tính tổng giá combo
        let comboTotal = 0;
        selectedProducts.forEach((product) => {
            comboTotal += product.price * product.quantity;
        });
        
        // Tổng cộng
        const grandTotal = seatTotal + comboTotal;
        
        // Cập nhật hiển thị
        const totalPriceDisplay = document.getElementById('totalPriceDisplay');
        if (totalPriceDisplay) {
            totalPriceDisplay.textContent = '₫' + seatTotal.toLocaleString('vi-VN');
        }
        
        // Cập nhật phần tóm tắt tổng (nếu có)
        const grandTotalDisplay = document.getElementById('grandTotalDisplay');
        if (grandTotalDisplay) {
            grandTotalDisplay.textContent = '₫' + grandTotal.toLocaleString('vi-VN');
        }
        
        // Cập nhật chi tiết tổng
        const totalBreakdown = document.getElementById('totalBreakdown');
        if (totalBreakdown) {
            let breakdown = '';
            if (seatTotal > 0) {
                breakdown += '<div class="price-row"><span>Ghế đã chọn:</span><span>₫' + seatTotal.toLocaleString('vi-VN') + '</span></div>';
            }
            if (comboTotal > 0) {
                breakdown += '<div class="price-row"><span>Combo đã chọn:</span><span>₫' + comboTotal.toLocaleString('vi-VN') + '</span></div>';
            }
            breakdown += '<div class="price-row total"><span>TỔNG CỘNG:</span><span>₫' + grandTotal.toLocaleString('vi-VN') + '</span></div>';
            totalBreakdown.innerHTML = breakdown;
        }
        
        console.log('Total Summary - Seats:', seatTotal, 'Combo:', comboTotal, 'Grand Total:', grandTotal);
    }
    
    // Test function to manually update price display
    function testUpdatePrice() {
        console.log('Testing manual price update...');
        const testTotal = 150000;
        const totalPriceDisplay = document.getElementById('totalPriceDisplay');
        if (totalPriceDisplay) {
            totalPriceDisplay.textContent = '₫' + testTotal.toLocaleString('vi-VN');
            console.log('Manual price update successful');
        } else {
            console.log('totalPriceDisplay element not found');
        }
    }
    
    // Make test function available globally
    window.testUpdatePrice = testUpdatePrice;
</script>

<jsp:include page="../layout/footer.jsp" />
