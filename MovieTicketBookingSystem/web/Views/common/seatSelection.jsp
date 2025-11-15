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
                opacity: 0;
                transition: opacity 0.3s ease;
                background: #f0f0f0;
                min-height: 160px;
                object-fit: cover;
            }
            
            .seat-poster img.loaded {
                opacity: 1;
            }
            
            .seat-poster img.error {
                opacity: 1;
                background: linear-gradient(135deg, #f8f9fa, #e9ecef);
                display: flex;
                align-items: center;
                justify-content: center;
                color: #666;
                font-size: 12px;
                text-align: center;
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
                background: #ffffff;
                color: #333;
                padding: 24px;
                border-radius: 16px;
                margin-top: 25px;
                box-shadow: 0 8px 24px rgba(0,0,0,0.08);
                position: relative;
                overflow: hidden;
                border: 1px solid #eee;
            }

            .total-summary-section::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                height: 6px;
                background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
                pointer-events: none;
            }

            .total-summary-title {
                font-size: 20px;
                font-weight: 800;
                margin-bottom: 20px;
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 10px;
                position: relative;
                z-index: 1;
                padding-bottom: 12px;
                border-bottom: 1px solid #eee;
                color: #222;
            }

            .total-summary-title i {
                color: #764ba2;
                font-size: 22px;
                text-shadow: none;
            }

            .total-summary-content {
                background: transparent;
                padding: 0;
                border-radius: 12px;
                backdrop-filter: none;
                border: none;
                position: relative;
                z-index: 1;
            }

            .total-breakdown {
                display: flex;
                flex-direction: column;
                gap: 10px;
            }

            .total-breakdown .price-row {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 12px 14px;
                background: #fff;
                border-radius: 10px;
                font-size: 15px;
                transition: background 0.15s ease;
                border: 1px solid #eee;
            }

            .total-breakdown .price-row:hover {
                background: #fafafa;
                transform: none;
                box-shadow: none;
            }

            .total-breakdown .price-row span:first-child {
                font-weight: 600;
                color: #444;
            }

            .total-breakdown .price-row span:last-child {
                font-weight: 700;
                color: #111;
                font-size: 16px;
            }

            .total-breakdown .price-row.total {
                border-top: 2px solid #eee;
                border-bottom: 2px solid #eee;
                margin-top: 6px;
                padding: 16px 16px;
                font-weight: 900;
                font-size: 20px;
                color: #e50914;
                background: #fff5f5;
                border-radius: 12px;
                letter-spacing: 0.2px;
            }

            .total-breakdown .price-row.total span:last-child {
                font-size: 22px;
                color: #e50914;
                font-weight: 900;
            }

            .total-breakdown .price-row#discountRow {
                background: #f0fff3;
                border-color: #d7f5df;
            }

            .total-breakdown .price-row#discountRow span:last-child {
                color: #2e7d32;
                font-weight: 800;
            }

            /* Danh sách ghế và combo chi tiết */
            #summarySeatList, #summaryComboList {
                padding: 8px 12px;
                margin: 0 0 6px 0;
                background: #fff;
                border-radius: 8px;
                border: 1px solid #eee;
            }

            #summarySeatList div, #summaryComboList div {
                padding: 8px 0;
                font-size: 14px;
                color: #333;
                line-height: 1.5;
                border-bottom: 1px dashed #eee;
                display: flex;
                justify-content: space-between;
                gap: 12px;
            }

            #summarySeatList div:last-child, #summaryComboList div:last-child {
                border-bottom: none;
            }

            /* Phần mã giảm giá */
            .discount-section {
                margin-top: 16px;
                background: rgba(255, 255, 255, 0.12);
                border: 1px solid rgba(255, 255, 255, 0.2);
                border-radius: 12px;
                padding: 16px;
            }

            .discount-section-title {
                font-weight: 700;
                font-size: 16px;
                color: #fff;
                margin-bottom: 14px;
                display: flex;
                align-items: center;
                gap: 8px;
            }

            .discount-section-title i {
                color: #ffd700;
                font-size: 18px;
            }

            .discount-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
                gap: 12px;
                max-height: 250px;
                overflow-y: auto;
                padding: 4px;
            }

            .discount-grid::-webkit-scrollbar {
                width: 6px;
            }

            .discount-grid::-webkit-scrollbar-track {
                background: rgba(255, 255, 255, 0.1);
                border-radius: 3px;
            }

            .discount-grid::-webkit-scrollbar-thumb {
                background: rgba(255, 255, 255, 0.3);
                border-radius: 3px;
            }

            .discount-grid::-webkit-scrollbar-thumb:hover {
                background: rgba(255, 255, 255, 0.5);
            }

            .discount-card {
                background: linear-gradient(135deg, rgba(255, 255, 255, 0.15) 0%, rgba(255, 255, 255, 0.1) 100%);
                border: 1.5px solid rgba(255, 255, 255, 0.25);
                border-radius: 10px;
                padding: 12px;
                cursor: pointer;
                transition: all 0.3s ease;
                display: flex;
                flex-direction: column;
                gap: 10px;
            }

            .discount-card:hover {
                background: linear-gradient(135deg, rgba(255, 255, 255, 0.25) 0%, rgba(255, 255, 255, 0.15) 100%);
                border-color: rgba(255, 215, 0, 0.6);
                transform: translateY(-2px);
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
            }

            .discount-card.applied {
                background: linear-gradient(135deg, rgba(76, 175, 80, 0.3) 0%, rgba(76, 175, 80, 0.2) 100%);
                border-color: #4caf50;
                box-shadow: 0 0 15px rgba(76, 175, 80, 0.4);
            }

            .discount-card-header {
                text-align: center;
            }

            .discount-code {
                font-weight: 700;
                font-size: 14px;
                color: #ffd700;
                margin-bottom: 4px;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .discount-name {
                font-size: 12px;
                color: #111;
                font-weight: 700;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
            }

            .discount-card-body {
                text-align: center;
                flex: 1;
            }

            .discount-value {
                font-size: 20px;
                font-weight: 800;
                margin-bottom: 6px;
            }
            .discount-value.percent { color: #e50914; }
            .discount-value.amount { color: #e50914; }

            .discount-type {
                font-size: 11px;
                color: rgba(255, 255, 255, 0.85);
                font-weight: 500;
            }

            .discount-card .btn-apply-discount {
                width: 100%;
                padding: 8px 12px;
                background: #4caf50;
                color: #fff;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-size: 12px;
                font-weight: 600;
                transition: all 0.2s ease;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .discount-card .btn-apply-discount:hover {
                background: #45a049;
                transform: scale(1.05);
                box-shadow: 0 2px 8px rgba(76, 175, 80, 0.4);
            }

            .discount-card.applied .btn-apply-discount {
                background: #ff9800;
            }

            .discount-card.applied .btn-apply-discount:hover {
                background: #f57c00;
            }

            .discount-empty {
                grid-column: 1 / -1;
                text-align: center;
                padding: 20px;
                color: rgba(255, 255, 255, 0.7);
                font-size: 13px;
            }

            .applied-discount-info {
                margin-top: 12px;
                padding: 12px;
                background: rgba(76, 175, 80, 0.25);
                border: 1.5px solid rgba(76, 175, 80, 0.5);
                border-radius: 8px;
            }

            .applied-discount-content {
                display: flex;
                justify-content: space-between;
                align-items: center;
                gap: 12px;
            }

            .applied-discount-name {
                font-weight: 600;
                color: #4caf50;
                font-size: 14px;
                margin-bottom: 4px;
            }

            .applied-discount-detail {
                font-size: 12px;
                color: rgba(255, 255, 255, 0.9);
            }

            .btn-remove-discount {
                padding: 6px 14px;
                background: #f44336;
                color: #fff;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-size: 12px;
                font-weight: 600;
                transition: all 0.2s ease;
                white-space: nowrap;
            }

            .btn-remove-discount:hover {
                background: #d32f2f;
                transform: scale(1.05);
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
                opacity: 0;
                transition: opacity 0.3s ease;
                background: #f0f0f0;
            }
            
            .combo-image.loaded {
                opacity: 1;
            }
            
            .combo-image.error {
                opacity: 1;
                background: linear-gradient(135deg, #f8f9fa, #e9ecef);
                display: flex;
                align-items: center;
                justify-content: center;
                color: #666;
                font-size: 10px;
                text-align: center;
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
                <img src="${pageContext.request.contextPath}/assets/image/${movie.poster}" 
                     alt="${movie.tenPhim}" 
                     data-fallback="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTIwIiBoZWlnaHQ9IjE2MCIgdmlld0JveD0iMCAwIDEyMCAxNjAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIxMjAiIGhlaWdodD0iMTYwIiBmaWxsPSIjRjhGOUZBIi8+CjxwYXRoIGQ9Ik02MCA0MEM2MCAzNS41ODE3IDYzLjU4MTcgMzIgNjggMzJINzJDNzYuNDE4MyAzMiA4MCAzNS41ODE3IDgwIDQwVjEyMEM4MCAxMjQuNDE4IDc2LjQxODMgMTI4IDcyIDEyOEg2OEM2My41ODE3IDEyOCA2MCAxMjQuNDE4IDYwIDEyMFY0MFoiIGZpbGw9IiNEOUQ5RDkiLz4KPHN2ZyB4PSI0NSIgeT0iNjAiIHdpZHRoPSIzMCIgaGVpZ2h0PSIzMCIgdmlld0JveD0iMCAwIDI0IDI0IiBmaWxsPSJub25lIj4KPHBhdGggZD0iTTEyIDJMMTMuMDkgOC4yNkwyMCA5TDEzLjA5IDE1Ljc0TDEyIDIyTDEwLjkxIDE1Ljc0TDQgOUwxMC45MSA4LjI2TDEyIDJaIiBmaWxsPSIjOTk5Ii8+Cjwvc3ZnPgo8L3N2Zz4K">
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
                <!-- Ẩn tóm tắt ghế riêng lẻ -->
                <div class="selected-seats-info" style="display: none;">
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

                <!-- Ẩn chi tiết giá ghế riêng lẻ -->
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
                                <img src="${pageContext.request.contextPath}/assets/image/${product.thumbnailUrl}" 
                                     alt="${product.tenSP}" 
                                     class="combo-image"
                                     data-fallback="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjEyMCIgdmlld0JveD0iMCAwIDIwMCAxMjAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIyMDAiIGhlaWdodD0iMTIwIiBmaWxsPSIjRjhGOUZBIi8+CjxwYXRoIGQ9Ik0xMDAgNDBDMTAwIDM1LjU4MTcgMTAzLjU4MiAzMiAxMDggMzJIMTEyQzExNi40MTggMzIgMTIwIDM1LjU4MTcgMTIwIDQwVjgwQzEyMCA4NC40MTgzIDExNi40MTggODggMTEyIDg4SDEwOEMxMDMuNTgyIDg4IDEwMCA4NC40MTgzIDEwMCA4MFY0MFoiIGZpbGw9IiNEOUQ5RDkiLz4KPHN2ZyB4PSI4NSIgeT0iNDUiIHdpZHRoPSIzMCIgaGVpZ2h0PSIzMCIgdmlld0JveD0iMCAwIDI0IDI0IiBmaWxsPSJub25lIj4KPHBhdGggZD0iTTEyIDJMMTMuMDkgOC4yNkwyMCA5TDEzLjA5IDE1Ljc0TDEyIDIyTDEwLjkxIDE1Ljc0TDQgOUwxMC45MSA4LjI2TDEyIDJaIiBmaWxsPSIjOTk5Ii8+Cjwvc3ZnPgo8L3N2Zz4K">
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
                
                <!-- Ẩn tóm tắt combo riêng lẻ -->
                <div class="combo-summary" id="comboSummary" style="display: none;"></div>
            </div>

            <!-- TOTAL SUMMARY SECTION -->
            <div class="total-summary-section">
                <div class="total-summary-title">
                    <i class="fas fa-calculator"></i>
                    Tóm tắt đơn hàng
                </div>
                <div class="total-summary-content">
                    <div id="totalBreakdown" class="total-breakdown">
                        <div class="price-row"><span>Ghế đã chọn:</span><span id="seatTotalDisplay">₫0</span></div>
                        <div id="summarySeatList" style="font-size: 13px; color: #fff; opacity: .95;"></div>

                        <div class="price-row"><span>Combo đã chọn:</span><span id="comboTotalDisplay">₫0</span></div>
                        <div id="summaryComboList" style="font-size: 13px; color: #fff; opacity: .95;"></div>

                        <!-- Mã giảm giá -->
                        <div class="price-row" id="discountRow" style="display: none;">
                            <span>Giảm giá:</span>
                            <span id="discountAmountDisplay" style="color: #4caf50;">-₫0</span>
                        </div>

                        <div class="price-row total"><span>TỔNG CỘNG:</span><span id="grandTotalDisplay">₫0</span></div>
                    </div>

                    <!-- Mã giảm giá -->
                    <div class="discount-section">
                        <div class="discount-section-title">
                            <i class="fas fa-tag"></i>
                            Mã giảm giá
                        </div>
                        <div id="discountList" class="discount-grid">
                            <c:choose>
                                <c:when test="${empty availableDiscounts}">
                                    <div class="discount-empty">
                                        Hiện không có mã giảm giá nào
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="discount" items="${availableDiscounts}">
                                        <div class="discount-card" data-ma-giam-gia="${discount.maGiamGia}">
                                            <div class="discount-card-header">
                                                <div class="discount-code">${discount.maCode}</div>
                                                <div class="discount-name">${discount.tenGiamGia}</div>
                                            </div>
                                            <div class="discount-card-body">
                                                <c:choose>
                                                    <c:when test="${discount.hinhThucGiam == 'Phần trăm' || discount.hinhThucGiam == 'PhanTram'}">
                                                        <div class="discount-value percent">-${discount.giaTriGiam}%</div>
                                                        <div class="discount-type">
                                                            <c:choose>
                                                                <c:when test="${discount.loaiGiamGia == 'Vé'}">Giảm cho vé</c:when>
                                                                <c:when test="${discount.loaiGiamGia == 'Đồ ăn'}">Giảm cho đồ ăn</c:when>
                                                                <c:when test="${discount.loaiGiamGia == 'Toàn đơn'}">Giảm toàn đơn</c:when>
                                                            </c:choose>
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="discount-value amount">-₫<fmt:formatNumber value="${discount.giaTriGiam}" pattern="#,###"/></div>
                                                        <div class="discount-type">
                                                            <c:choose>
                                                                <c:when test="${discount.loaiGiamGia == 'Vé'}">Giảm cho vé</c:when>
                                                                <c:when test="${discount.loaiGiamGia == 'Đồ ăn'}">Giảm cho đồ ăn</c:when>
                                                                <c:when test="${discount.loaiGiamGia == 'Toàn đơn'}">Giảm toàn đơn</c:when>
                                                            </c:choose>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <button type="button" class="btn-apply-discount" data-ma-giam-gia="${discount.maGiamGia}">
                                                Áp dụng
                                            </button>
                                        </div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div id="appliedDiscountInfo" class="applied-discount-info" style="display:none;">
                            <div class="applied-discount-content">
                                <div>
                                    <div class="applied-discount-name" id="appliedDiscountName"></div>
                                    <div class="applied-discount-detail" id="appliedDiscountDetail"></div>
                                </div>
                                <button type="button" id="removeDiscountBtn" class="btn-remove-discount">
                                    Hủy
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- Phương thức thanh toán nằm NGOÀI form -->
                    <div style="margin-top:14px; background: rgba(255,255,255,.15); border:1px solid rgba(255,255,255,.2); border-radius:10px; padding:12px;">
                        <div style="font-weight:700; margin-bottom:6px;">Phương thức thanh toán</div>
                        <label style="display:flex; align-items:center; gap:8px; cursor:pointer;">
                            <input type="radio" name="paymentMethodView" value="VNPAY" checked>
                            VNPAY
                        </label>
                        <label style="display:flex; align-items:center; gap:8px; cursor:pointer; margin-top:6px;">
                            <input type="radio" name="paymentMethodView" value="CASH">
                            Tiền mặt tại quầy
                        </label>
                    </div>

                    <!-- Form xác nhận: KHÔNG chứa radio payment -->
                    <form id="bookingForm" method="POST" action="booking?action=confirmBooking" style="margin-top: 16px;">
                        <input type="hidden" name="maPhim" value="${movie.maPhim}">
                        <input type="hidden" name="maSuatChieu" value="${showtime.maSuatChieu}">
                        <input type="hidden" name="selectedSeats" id="selectedSeatsInput" value="">
                        <input type="hidden" name="seatTypes" id="seatTypesInput" value="">
                        <input type="hidden" name="selectedProducts" id="selectedProductsInput" value="">
                        <input type="hidden" name="maGiamGia" id="maGiamGiaInput" value="">
                        <input type="hidden" name="paymentMethod" id="paymentMethodInput" value="VNPAY">
                        <div class="action-buttons" style="margin-top: 0;">
                            <a href="booking?action=selectShowtime&maPhim=${movie.maPhim}" class="btn btn-secondary">
                                ← Chọn lại suất chiếu
                            </a>
                            <button type="submit" class="btn btn-primary" id="confirmBtn" disabled style="min-width: 200px;">
                                ✓ Xác Nhận
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- BỎ khối chi tiết dưới, vì đã gộp vào tóm tắt -->
            </div>
        </div>
    </div>
</div>

<!-- MODAL QUY ĐỊNH ĐẶT VÉ -->
<div id="termsModal" style="display:none; position: fixed; inset: 0; background: rgba(0,0,0,.5); z-index: 9999; align-items: center; justify-content: center;">
    <div style="background: #fff; width: 90%; max-width: 520px; border-radius: 12px; box-shadow: 0 10px 30px rgba(0,0,0,.2); overflow: hidden;">
        <div style="padding: 16px 20px; border-bottom: 1px solid #eee; background: #f8f9fa;">
            <h3 style="margin:0; color:#e50914;">Quy định về đặt vé</h3>
        </div>
        <div style="padding: 16px 20px; max-height: 60vh; overflow:auto; color:#333; line-height:1.6;">
            <ul style="padding-left: 18px; margin: 0;">
                <li>Vé đã đặt không hoàn, không hủy, không đổi suất chiếu.</li>
                <li>Vui lòng kiểm tra kỹ phim, rạp, suất chiếu và vị trí ghế trước khi xác nhận.</li>
                <li>Vui lòng thanh toán trong vòng 15 phút để giữ chỗ; quá thời hạn hệ thống sẽ tự động hủy giữ chỗ.</li>
                <li>Trong trường hợp giao dịch thất bại, ghế đã giữ sẽ được giải phóng và bạn có thể đặt lại nếu còn trống.</li>
            </ul>
            <div style="margin-top: 14px;">
                <label style="display:flex; gap:8px; align-items:center; cursor:pointer;">
                    <input type="checkbox" id="termsCheckbox"/>
                    Tôi đã đọc và đồng ý với các quy định đặt vé.
                </label>
            </div>
        </div>
        <div style="padding: 14px 20px; border-top: 1px solid #eee; display:flex; gap:10px; justify-content:flex-end; background:#fff;">
            <button type="button" id="termsCancelBtn" style="padding:10px 16px; border:1px solid #ddd; background:#f5f5f5; border-radius:8px; cursor:pointer;">Hủy</button>
            <button type="button" id="termsAgreeBtn" disabled style="padding:10px 16px; border:none; background:#e50914; color:#fff; border-radius:8px; cursor:not-allowed;">Đồng ý & Tiếp tục</button>
        </div>
    </div>
</div>

<script>
    const selectedSeats = new Map(); // { seatId: {name, type, ageType, price} }
    const basePrice = ${showtime.giaVeCoSo};
    const childBasePrice = ${showtime.giaVeTreEm != null ? showtime.giaVeTreEm : showtime.giaVeCoSo};
    const movieAgeLimit = ${movie.doTuoiGioiHan};
    const vatPercent = ${showtime.vat != null ? showtime.vat : 0}; // %
    
    // Map seat prices by seat ID
    const seatPrices = new Map();
    <c:forEach var="priceEntry" items="${seatPrices}">
        seatPrices.set(${priceEntry.key}, ${priceEntry.value});
    </c:forEach>

    function getSeatType(element) {
        if (element.classList.contains('vip')) return 'VIP';
        if (element.classList.contains('couple')) return 'Couple';
        return 'Thường';
    }
    function getMultiplierBySeatType(seatType) {
        if (seatType === 'VIP') return 1.2;
        if (seatType === 'Couple') return 1.5;
        return 1.0;
    }
    function getSeatPrice(seatId, seatType, ageType) {
        const p = seatPrices.get(seatId);
        if (typeof p === 'number' && !isNaN(p)) return p;
        const base = (ageType === 'child') ? Number(childBasePrice) : Number(basePrice);
        const mul = getMultiplierBySeatType(seatType);
        return Math.round(base * mul);
    }

    let agePickerEl = null;
    function ensureAgePicker() {
        if (agePickerEl) return agePickerEl;
        agePickerEl = document.createElement('div');
        agePickerEl.id = 'agePickerPopover';
        agePickerEl.style.position = 'absolute';
        agePickerEl.style.zIndex = '9999';
        agePickerEl.style.background = '#fff';
        agePickerEl.style.border = '1px solid #e0e0e0';
        agePickerEl.style.borderRadius = '8px';
        agePickerEl.style.boxShadow = '0 6px 18px rgba(0,0,0,0.15)';
        agePickerEl.style.padding = '10px';
        agePickerEl.style.display = 'none';
        agePickerEl.style.minWidth = '200px';
        agePickerEl.innerHTML = '<div style="font-weight:700; margin-bottom:8px; color:#333;">Chọn loại vé</div>'
            + '<div style="display:flex; gap:8px; align-items:center;">'
            +   '<button type="button" id="agePickAdult" style="flex:1; padding:8px 10px; border:1px solid #ddd; background:#f8f8f8; border-radius:6px; cursor:pointer;">Vé thường</button>'
            +   '<button type="button" id="agePickChild" style="flex:1; padding:8px 10px; border:1px solid #ddd; background:#f8f8f8; border-radius:6px; cursor:pointer;">Vé trẻ em</button>'
            + '</div>';
        document.body.appendChild(agePickerEl);
        return agePickerEl;
    }
    function hideAgePicker() { if (agePickerEl) agePickerEl.style.display = 'none'; }
    function showAgePickerForSeat(seatElement, seatId) {
        if (!(movieAgeLimit < 16)) return;
        const picker = ensureAgePicker();
        const rect = seatElement.getBoundingClientRect();
        const scrollX = window.pageXOffset || document.documentElement.scrollLeft;
        const scrollY = window.pageYOffset || document.documentElement.scrollTop;
        picker.style.left = (rect.left + scrollX + rect.width + 8) + 'px';
        picker.style.top = (rect.top + scrollY - 4) + 'px';
        picker.style.display = 'block';
        const onAdult = () => { changeSeatAgeType(seatId, 'adult'); hideAgePicker(); };
        const onChild = () => { changeSeatAgeType(seatId, 'child'); hideAgePicker(); };
        const adultBtn = picker.querySelector('#agePickAdult');
        const childBtn = picker.querySelector('#agePickChild');
        const adultClone = adultBtn.cloneNode(true);
        const childClone = childBtn.cloneNode(true);
        adultBtn.parentNode.replaceChild(adultClone, adultBtn);
        childBtn.parentNode.replaceChild(childClone, childBtn);
        adultClone.addEventListener('click', onAdult);
        childClone.addEventListener('click', onChild);
    }

    function selectSeat(element) {
        if (element.classList.contains('booked') || element.classList.contains('maintenance')) return;
        const seatId = element.getAttribute('data-seat-id');
        const seatName = element.getAttribute('data-seat-name');
        if (element.classList.contains('selected')) {
            element.classList.remove('selected');
            selectedSeats.delete(seatId);
            hideAgePicker();
        } else {
            element.classList.add('selected');
            const seatType = getSeatType(element);
            const ageType = (movieAgeLimit < 16) ? 'adult' : 'adult';
            const price = getSeatPrice(parseInt(seatId), seatType, ageType);
            selectedSeats.set(seatId, { name: seatName, type: seatType, ageType: ageType, price: price });
            showAgePickerForSeat(element, seatId);
        }
        updateSummary();
    }

    function renderSeatTypeControls() {
        const list = document.getElementById('summarySeatList');
        if (!list) return;
        let html = '';
        // Header amount first
        const seatTotal = Array.from(selectedSeats.values()).reduce((acc, s) => acc + (Number(s.price)||0), 0);
        html += '<div style="font-weight:700; color:#fff; margin-bottom:8px;">₫' + seatTotal.toLocaleString('vi-VN') + '</div>';
        selectedSeats.forEach((seat, seatId) => {
            const isChildAllowed = movieAgeLimit < 16;
            const controls = isChildAllowed ? (
                '<div style="display:flex; gap:8px; align-items:center; margin-top:4px;">'
                + '<label style="display:flex; gap:4px; align-items:center; cursor:pointer;">'
                + '<input type="radio" name="age_' + seatId + '" value="adult" '
                + (seat.ageType === 'adult' ? 'checked' : '')
                + ' onchange="changeSeatAgeType(\'' + seatId + '\',\'adult\')"> Vé thường'
                + '</label>'
                + '<label style="display:flex; gap:4px; align-items:center; cursor:pointer;">'
                + '<input type="radio" name="age_' + seatId + '" value="child" '
                + (seat.ageType === 'child' ? 'checked' : '')
                + ' onchange="changeSeatAgeType(\'' + seatId + '\',\'child\')"> Vé trẻ em'
                + '</label>'
                + '</div>'
            ) : '';
            html += '<div style="margin-bottom:6px;">• ' + seat.name + ' (' + seat.type + (seat.ageType==='child'?' - Trẻ em':'') + ') - ₫' + (Number(seat.price)||0).toLocaleString('vi-VN') + controls + '</div>';
        });
        list.innerHTML = html;
    }
    function changeSeatAgeType(seatId, ageType) {
        const seat = selectedSeats.get(seatId);
        if (!seat) return;
        seat.ageType = ageType;
        seat.price = getSeatPrice(parseInt(seatId), seat.type, seat.ageType);
        selectedSeats.set(seatId, seat);
        updateSummary();
    }

    // Update Total Summary with VAT and discount
    function updateTotalSummary() {
        // Seat total
        let seatTotal = 0;
        selectedSeats.forEach(s => seatTotal += (Number(s.price)||0));
        // Combo total
        let comboTotal = 0;
        selectedProducts.forEach(p => comboTotal += (p.price * p.quantity));
        // Base grand
        let grandTotal = seatTotal + comboTotal;
        let discountAmount = 0;
        let finalAfterDiscount = grandTotal;
        if (appliedDiscount) {
            discountAmount = parseFloat(appliedDiscount.discountAmount) || 0;
            finalAfterDiscount = Math.max(0, grandTotal - discountAmount);
        }
        // VAT
        const vatAmount = Math.round(finalAfterDiscount * (Number(vatPercent) / 100));
        const finalWithVat = finalAfterDiscount + vatAmount;

        // Update UI breakdown
        const totalBreakdown = document.getElementById('totalBreakdown');
        if (totalBreakdown) {
            let breakdown = '';
            breakdown += '<div class="price-row"><span>Ghế đã chọn:</span><span>₫' + seatTotal.toLocaleString('vi-VN') + '</span></div>';
            breakdown += '<div id="summarySeatList"></div>';
            breakdown += '<div class="price-row"><span>Combo đã chọn:</span><span>₫' + comboTotal.toLocaleString('vi-VN') + '</span></div>';
            breakdown += '<div id="summaryComboList"></div>';
            if (discountAmount > 0) breakdown += '<div class="price-row" id="discountRow2" style="color:#4caf50;"><span>Giảm giá:</span><span>-₫' + discountAmount.toLocaleString('vi-VN') + '</span></div>';
            breakdown += '<div class="price-row"><span>VAT (' + Number(vatPercent).toLocaleString('vi-VN') + '%):</span><span>₫' + vatAmount.toLocaleString('vi-VN') + '</span></div>';
            breakdown += '<div class="price-row total"><span>TỔNG CỘNG:</span><span>₫' + finalWithVat.toLocaleString('vi-VN') + '</span></div>';
            totalBreakdown.innerHTML = breakdown;
        }
        // Update top displays
        const seatTotalDisplay = document.getElementById('seatTotalDisplay');
        if (seatTotalDisplay) seatTotalDisplay.textContent = '₫' + seatTotal.toLocaleString('vi-VN');
        const comboTotalDisplay = document.getElementById('comboTotalDisplay');
        if (comboTotalDisplay) comboTotalDisplay.textContent = '₫' + comboTotal.toLocaleString('vi-VN');
        const discountRow = document.getElementById('discountRow');
        const discountAmountDisplay = document.getElementById('discountAmountDisplay');
        if (discountAmount > 0) {
            if (discountRow) discountRow.style.display = 'flex';
            if (discountAmountDisplay) discountAmountDisplay.textContent = '-₫' + discountAmount.toLocaleString('vi-VN');
        } else {
            if (discountRow) discountRow.style.display = 'none';
        }
        const grandTotalDisplay = document.getElementById('grandTotalDisplay');
        if (grandTotalDisplay) grandTotalDisplay.textContent = '₫' + finalWithVat.toLocaleString('vi-VN');

        // Render lists and controls
        updateSeatAndComboLists();
        renderSeatTypeControls();

        // Hidden inputs
        const selectedSeatsInput = document.getElementById('selectedSeatsInput');
        if (selectedSeatsInput) selectedSeatsInput.value = Array.from(selectedSeats.keys()).join(',');
        const seatTypesInput = document.getElementById('seatTypesInput');
        if (seatTypesInput) {
            const pairs = [];
            selectedSeats.forEach((seat, seatId) => pairs.push(seatId + ':' + (seat.ageType||'adult')));
            seatTypesInput.value = pairs.join(',');
        }
    }

    function updateTotalSummaryWithDiscount(discountResponse) {
        // Use server numbers if provided, else fallback to local calc
        const seatTotal = parseFloat(discountResponse.seatTotal) || 0;
        const productTotal = parseFloat(discountResponse.productTotal) || 0;
        const discountAmount = parseFloat(discountResponse.discountAmount) || 0;
        // Server may return VAT fields
        const respVatPercent = (typeof discountResponse.vatPercent !== 'undefined') ? parseFloat(discountResponse.vatPercent) : Number(vatPercent);
        const finalAfterDiscount = Math.max(0, (seatTotal + productTotal) - discountAmount);
        const vatAmount = (typeof discountResponse.vatAmount !== 'undefined') ? parseFloat(discountResponse.vatAmount) : Math.round(finalAfterDiscount * (respVatPercent/100));
        const finalWithVat = (typeof discountResponse.finalWithVat !== 'undefined') ? parseFloat(discountResponse.finalWithVat) : (finalAfterDiscount + vatAmount);

        // Update UI sections similar to updateTotalSummary
        const seatTotalDisplay = document.getElementById('seatTotalDisplay');
        if (seatTotalDisplay) seatTotalDisplay.textContent = '₫' + seatTotal.toLocaleString('vi-VN');
        const comboTotalDisplay = document.getElementById('comboTotalDisplay');
        if (comboTotalDisplay) comboTotalDisplay.textContent = '₫' + productTotal.toLocaleString('vi-VN');
        const discountRow = document.getElementById('discountRow');
        const discountAmountDisplay = document.getElementById('discountAmountDisplay');
        if (discountAmount > 0) {
            if (discountRow) discountRow.style.display = 'flex';
            if (discountAmountDisplay) discountAmountDisplay.textContent = '-₫' + discountAmount.toLocaleString('vi-VN');
        } else {
            if (discountRow) discountRow.style.display = 'none';
        }
        const totalBreakdown = document.getElementById('totalBreakdown');
        if (totalBreakdown) {
            let breakdown = '';
            breakdown += '<div class="price-row"><span>Ghế đã chọn:</span><span>₫' + seatTotal.toLocaleString('vi-VN') + '</span></div>';
            breakdown += '<div id="summarySeatList"></div>';
            breakdown += '<div class="price-row"><span>Combo đã chọn:</span><span>₫' + productTotal.toLocaleString('vi-VN') + '</span></div>';
            breakdown += '<div id="summaryComboList"></div>';
            if (discountAmount > 0) breakdown += '<div class="price-row" id="discountRow2" style="color:#4caf50;"><span>Giảm giá:</span><span>-₫' + discountAmount.toLocaleString('vi-VN') + '</span></div>';
            breakdown += '<div class="price-row"><span>VAT (' + respVatPercent.toLocaleString('vi-VN') + '%):</span><span>₫' + vatAmount.toLocaleString('vi-VN') + '</span></div>';
            breakdown += '<div class="price-row total"><span>TỔNG CỘNG:</span><span>₫' + finalWithVat.toLocaleString('vi-VN') + '</span></div>';
            totalBreakdown.innerHTML = breakdown;
        }
        const grandTotalDisplay = document.getElementById('grandTotalDisplay');
        if (grandTotalDisplay) grandTotalDisplay.textContent = '₫' + finalWithVat.toLocaleString('vi-VN');
        updateSeatAndComboLists();
        renderSeatTypeControls();
    }

    // Apply discount: gửi thêm seatTypes
    function applyDiscountCode(maGiamGia) {
        const selectedSeatsInput = document.getElementById('selectedSeatsInput');
        const selectedProductsInput = document.getElementById('selectedProductsInput');
        const maSuatChieuInput = document.querySelector('input[name="maSuatChieu"]');
        const seatTypesInput = document.getElementById('seatTypesInput');
        if (!selectedSeatsInput || !selectedSeatsInput.value || selectedSeatsInput.value.trim() === '') { alert('Vui lòng chọn ít nhất một ghế trước khi áp dụng mã giảm giá'); return; }
        const selectedSeatsVal = selectedSeatsInput.value;
        const selectedProductsVal = selectedProductsInput ? selectedProductsInput.value : '';
        const seatTypesVal = seatTypesInput ? seatTypesInput.value : '';
        const maSuatChieu = maSuatChieuInput ? maSuatChieuInput.value : '';
        const applyButtons = document.querySelectorAll('.btn-apply-discount');
        applyButtons.forEach(btn => { btn.disabled = true; btn.style.opacity = '0.6'; btn.style.cursor = 'wait'; });
        const xhr = new XMLHttpRequest();
        xhr.open('POST', 'booking?action=applyDiscount', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onload = function() {
            applyButtons.forEach(btn => { btn.disabled = false; btn.style.opacity = '1'; btn.style.cursor = 'pointer'; });
            if (xhr.status === 200) {
                try {
                    const response = JSON.parse(xhr.responseText);
                    if (response.success) {
                        appliedDiscount = response;
                        const maGiamGiaInput = document.getElementById('maGiamGiaInput');
                        if (maGiamGiaInput) maGiamGiaInput.value = response.maGiamGia;
                        const appliedDiscountInfo = document.getElementById('appliedDiscountInfo');
                        const appliedDiscountName = document.getElementById('appliedDiscountName');
                        const appliedDiscountDetail = document.getElementById('appliedDiscountDetail');
                        if (appliedDiscountInfo && appliedDiscountName && appliedDiscountDetail) {
                            appliedDiscountName.textContent = response.maCode + ' - ' + response.tenGiamGia;
                            const discountAmt = parseFloat(response.discountAmount) || 0;
                            const finalWithVat = (typeof response.finalWithVat !== 'undefined') ? parseFloat(response.finalWithVat) : (parseFloat(response.finalTotal)||0);
                            let detailText = 'Giảm ₫' + discountAmt.toLocaleString('vi-VN') + ' (Tổng: ₫' + finalWithVat.toLocaleString('vi-VN') + ')';
                            appliedDiscountDetail.textContent = detailText;
                            appliedDiscountInfo.style.display = 'block';
                        }
                        updateTotalSummaryWithDiscount(response);
                    } else {
                        alert(response.message || 'Không thể áp dụng mã giảm giá');
                    }
                } catch (e) {
                    alert('Lỗi khi xử lý phản hồi từ server: ' + e.message);
                }
            } else {
                alert('Lỗi kết nối đến server (Status: ' + xhr.status + ')');
            }
        };
        xhr.onerror = function() { applyButtons.forEach(btn => { btn.disabled = false; btn.style.opacity = '1'; btn.style.cursor = 'pointer'; }); alert('Lỗi kết nối đến server'); };
        const params = 'maGiamGia=' + encodeURIComponent(maGiamGia)
                     + '&selectedSeats=' + encodeURIComponent(selectedSeatsVal)
                     + '&selectedProducts=' + encodeURIComponent(selectedProductsVal)
                     + '&maSuatChieu=' + encodeURIComponent(maSuatChieu)
                     + '&seatTypes=' + encodeURIComponent(seatTypesVal);
        xhr.send(params);
    }

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
    
    // Attach click handlers to all seat elements
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
        
        // Modal quy định đặt vé - chặn submit nếu chưa đồng ý
        const form = document.getElementById('bookingForm');
        const termsModal = document.getElementById('termsModal');
        const termsCheckbox = document.getElementById('termsCheckbox');
        const termsAgreeBtn = document.getElementById('termsAgreeBtn');
        const termsCancelBtn = document.getElementById('termsCancelBtn');

        // Hidden input để gửi kèm nếu cần phía server
        let termsInput = document.querySelector('input[name="termsAccepted"]');
        if (!termsInput) {
            termsInput = document.createElement('input');
            termsInput.type = 'hidden';
            termsInput.name = 'termsAccepted';
            termsInput.value = '';
            form.appendChild(termsInput);
        }

        if (form) {
            form.addEventListener('submit', function(e) {
                // Chỉ hiện modal nếu có ghế được chọn
                const hasSeats = selectedSeats.size > 0;
                if (!hasSeats) return; // để mặc định chặn bởi nút disabled

                // Nếu chưa đồng ý, mở modal và chặn submit
                if (termsInput.value !== 'true') {
                    e.preventDefault();
                    termsModal.style.display = 'flex';
                }
            });
        }

        termsCheckbox.addEventListener('change', function() {
            if (this.checked) {
                termsAgreeBtn.disabled = false;
                termsAgreeBtn.style.cursor = 'pointer';
                termsAgreeBtn.style.opacity = '1';
            } else {
                termsAgreeBtn.disabled = true;
                termsAgreeBtn.style.cursor = 'not-allowed';
                termsAgreeBtn.style.opacity = '.8';
            }
        });

        termsCancelBtn.addEventListener('click', function() {
            termsModal.style.display = 'none';
        });

        termsAgreeBtn.addEventListener('click', function() {
            if (termsCheckbox.checked) {
                termsInput.value = 'true';
                termsModal.style.display = 'none';
                form.submit();
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
        const count = selectedSeats.size;
        // Update totals
        let total = 0;
        let details = '';
        selectedSeats.forEach((seat, seatId) => {
            const price = Number(seat.price) || 0;
            total += price;
            details += '<div class="price-row"><span>' + seat.name + ' (' + seat.type + (seat.ageType==='child'?' - Trẻ em':'') + ')</span><span>₫' + price.toLocaleString('vi-VN') + '</span></div>';
        });
        const finalTotal = document.getElementById('finalTotal');
        const priceDetails = document.getElementById('priceDetails');
        const priceBreakdown = document.getElementById('priceBreakdown');
        const confirmBtn = document.getElementById('confirmBtn');
        if (priceDetails) priceDetails.innerHTML = details;
        if (count > 0) {
            if (priceBreakdown) priceBreakdown.style.display = 'block';
            if (confirmBtn) confirmBtn.disabled = false;
        } else {
            if (priceBreakdown) priceBreakdown.style.display = 'none';
            if (confirmBtn) confirmBtn.disabled = true;
        }
        // Hidden inputs
        const seatIds = Array.from(selectedSeats.keys()).join(',');
        const selectedSeatsInput = document.getElementById('selectedSeatsInput');
        if (selectedSeatsInput) selectedSeatsInput.value = seatIds;
        const seatTypesInput = document.getElementById('seatTypesInput');
        if (seatTypesInput) {
            const pairs = [];
            selectedSeats.forEach((seat, seatId) => {
                pairs.push(seatId + ':' + (seat.ageType || 'adult'));
            });
            seatTypesInput.value = pairs.join(',');
        }
        updateTotalSummary();
        renderSeatTypeControls();
    }

    // Sync payment method
    document.addEventListener('DOMContentLoaded', function() {
        const radios = document.querySelectorAll('input[name="paymentMethodView"]');
        const hidden = document.getElementById('paymentMethodInput');
        radios.forEach(r => r.addEventListener('change', () => { if (hidden) hidden.value = r.value; }));
    });

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
        const productThumb = element.querySelector('.combo-image') ? element.querySelector('.combo-image').getAttribute('src') : '';
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
                quantity: 1,
                thumb: productThumb
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
    
    // Biến lưu thông tin mã giảm giá được áp dụng
    let appliedDiscount = null;
    
    // Event listener cho nút áp dụng mã giảm giá
    document.addEventListener('DOMContentLoaded', function() {
        const applyButtons = document.querySelectorAll('.btn-apply-discount');
        const removeDiscountBtn = document.getElementById('removeDiscountBtn');
        const appliedDiscountInfo = document.getElementById('appliedDiscountInfo');
        const maGiamGiaInput = document.getElementById('maGiamGiaInput');
        
        applyButtons.forEach(btn => {
            btn.addEventListener('click', function() {
                const maGiamGia = this.getAttribute('data-ma-giam-gia');
                applyDiscountCode(maGiamGia);
            });
        });
        
        if (removeDiscountBtn) {
            removeDiscountBtn.addEventListener('click', function() {
                removeDiscount();
            });
        }
    });
    
    function applyDiscountCode(maGiamGia) {
        // Lấy dữ liệu ghế và combo đã chọn
        const selectedSeatsInput = document.getElementById('selectedSeatsInput');
        const selectedProductsInput = document.getElementById('selectedProductsInput');
        const maSuatChieuInput = document.querySelector('input[name="maSuatChieu"]');
        const seatTypesInput = document.getElementById('seatTypesInput');
        
        if (!selectedSeatsInput || !selectedSeatsInput.value || selectedSeatsInput.value.trim() === '') {
            alert('Vui lòng chọn ít nhất một ghế trước khi áp dụng mã giảm giá');
            return;
        }
        
        const selectedSeatsVal = selectedSeatsInput.value;
        const selectedProductsVal = selectedProductsInput ? selectedProductsInput.value : '';
        const seatTypesVal = seatTypesInput ? seatTypesInput.value : '';
        const maSuatChieu = maSuatChieuInput ? maSuatChieuInput.value : '';
        
        // Disable tất cả nút áp dụng
        const applyButtons = document.querySelectorAll('.btn-apply-discount');
        applyButtons.forEach(btn => {
            btn.disabled = true;
            btn.style.opacity = '0.6';
            btn.style.cursor = 'wait';
        });
        
        // Gọi AJAX để áp dụng mã giảm giá
        const xhr = new XMLHttpRequest();
        xhr.open('POST', 'booking?action=applyDiscount', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        
        xhr.onload = function() {
            // Enable lại tất cả nút
            applyButtons.forEach(btn => {
                btn.disabled = false;
                btn.style.opacity = '1';
                btn.style.cursor = 'pointer';
            });
            
            if (xhr.status === 200) {
                try {
                    const response = JSON.parse(xhr.responseText);
                    if (response.success) {
                        // Lưu thông tin mã giảm giá đã áp dụng
                        appliedDiscount = response;
                        
                        // Cập nhật input hidden
                        const maGiamGiaInput = document.getElementById('maGiamGiaInput');
                        if (maGiamGiaInput) maGiamGiaInput.value = response.maGiamGia;
                        
                        // Hiển thị thông tin mã đã áp dụng
                        const appliedDiscountInfo = document.getElementById('appliedDiscountInfo');
                        const appliedDiscountName = document.getElementById('appliedDiscountName');
                        const appliedDiscountDetail = document.getElementById('appliedDiscountDetail');
                        if (appliedDiscountInfo && appliedDiscountName && appliedDiscountDetail) {
                            appliedDiscountName.textContent = response.maCode + ' - ' + response.tenGiamGia;
                            const discountAmt = parseFloat(response.discountAmount) || 0;
                            const finalWithVat = (typeof response.finalWithVat !== 'undefined') ? parseFloat(response.finalWithVat) : (parseFloat(response.finalTotal)||0);
                            let detailText = 'Giảm ₫' + discountAmt.toLocaleString('vi-VN') + ' (Tổng: ₫' + finalWithVat.toLocaleString('vi-VN') + ')';
                            appliedDiscountDetail.textContent = detailText;
                            appliedDiscountInfo.style.display = 'block';
                        }
                        
                        // Cập nhật giá hiển thị (seat giữ nguyên, cộng discount + VAT)
                        updateTotalSummaryWithDiscount(response);
                        
                        // Highlight mã đã áp dụng
                        highlightAppliedDiscount(response.maGiamGia);
                    } else {
                        alert(response.message || 'Không thể áp dụng mã giảm giá');
                    }
                } catch (e) {
                    alert('Lỗi khi xử lý phản hồi từ server: ' + e.message);
                }
            } else {
                alert('Lỗi kết nối đến server (Status: ' + xhr.status + ')');
            }
        };
        
        xhr.onerror = function() {
            applyButtons.forEach(btn => {
                btn.disabled = false;
                btn.style.opacity = '1';
                btn.style.cursor = 'pointer';
            });
            alert('Lỗi kết nối đến server');
        };
        
        // Gửi request
        const params = 'maGiamGia=' + encodeURIComponent(maGiamGia)
                     + '&selectedSeats=' + encodeURIComponent(selectedSeatsVal)
                     + '&selectedProducts=' + encodeURIComponent(selectedProductsVal)
                     + '&maSuatChieu=' + encodeURIComponent(maSuatChieu)
                     + '&seatTypes=' + encodeURIComponent(seatTypesVal);
        xhr.send(params);
    }
    
    function removeDiscount() {
        appliedDiscount = null;
        const maGiamGiaInput = document.getElementById('maGiamGiaInput');
        if (maGiamGiaInput) maGiamGiaInput.value = '';
        
        const appliedDiscountInfo = document.getElementById('appliedDiscountInfo');
        if (appliedDiscountInfo) appliedDiscountInfo.style.display = 'none';
        
        // Bỏ highlight
        const discountItems = document.querySelectorAll('.discount-item');
        discountItems.forEach(item => {
            item.style.background = 'rgba(255,255,255,.1)';
            item.style.border = '1px solid rgba(255,255,255,.2)';
        });
        
        // Cập nhật lại giá không có giảm giá
        updateTotalSummary();
    }
    
    function highlightAppliedDiscount(maGiamGia) {
        const discountItems = document.querySelectorAll('.discount-item');
        discountItems.forEach(item => {
            if (item.getAttribute('data-ma-giam-gia') == maGiamGia) {
                item.style.background = 'rgba(76,175,80,.3)';
                item.style.border = '1px solid rgba(76,175,80,.6)';
            } else {
                item.style.background = 'rgba(255,255,255,.1)';
                item.style.border = '1px solid rgba(255,255,255,.2)';
            }
        });
    }
    
    function updateSeatAndComboLists() {
        // Danh sách ghế trong tóm tắt
        const summarySeatList = document.getElementById('summarySeatList');
        if (summarySeatList) {
            let seatTotal = 0;
            let seatList = '';
            selectedSeats.forEach((seat) => {
                const price = Number(seat.price) || 0;
                seatTotal += price;
                const ageNote = seat.ageType === 'child' ? ' - Trẻ em' : '';
                seatList += '<div>• ' + seat.name + ' (' + seat.type + ageNote + ') - ₫' + price.toLocaleString('vi-VN') + '</div>';
            });
            if (seatTotal > 0) {
                summarySeatList.innerHTML = seatList;
            } else {
                summarySeatList.innerHTML = '';
            }
        }
        
        // Danh sách combo với thumbnail nhỏ trong tóm tắt
        const summaryComboList = document.getElementById('summaryComboList');
        if (summaryComboList) {
            let comboTotal = 0;
            let comboList = '';
            selectedProducts.forEach((p) => {
                const subtotal = (p.price * p.quantity) || 0;
                comboTotal += subtotal;
                const thumb = p.thumb ? p.thumb : '';
                comboList += '<div style="display:flex; align-items:center; gap:8px; margin:4px 0;">'
                    + (thumb ? ('<img src="' + thumb + '" alt="thumb" style="width:28px;height:28px;object-fit:cover;border-radius:4px;">') : '')
                    + '<span>' + p.name + ' x' + p.quantity + '</span>'
                    + '<span style="margin-left:auto;">₫' + subtotal.toLocaleString('vi-VN') + '</span>'
                    + '</div>';
            });
            if (comboTotal > 0) {
                summaryComboList.innerHTML = comboList;
            } else {
                summaryComboList.innerHTML = '';
            }
        }
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
