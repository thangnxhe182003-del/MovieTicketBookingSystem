<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Xác nhận đặt vé"/>
    <jsp:param name="extraStyles" value="
        <style>
            .confirm-page {
                background: #f5f5f5;
                padding: 30px 0;
                min-height: 100vh;
            }

            .confirm-container {
                max-width: 900px;
                margin: 0 auto;
                padding: 0 20px;
            }

            .confirm-card {
                background: white;
                border-radius: 8px;
                box-shadow: 0 3px 15px rgba(0,0,0,0.1);
                overflow: hidden;
            }

            .confirm-header {
                background: linear-gradient(135deg, #e50914, #c90812);
                color: white;
                padding: 30px;
                text-align: center;
            }

            .confirm-title {
                font-size: 32px;
                font-weight: bold;
                margin-bottom: 10px;
            }

            .confirm-subtitle {
                font-size: 16px;
                opacity: 0.9;
            }

            .confirm-content {
                padding: 40px 30px;
            }

            .section {
                margin-bottom: 30px;
                padding-bottom: 30px;
                border-bottom: 1px solid #e0e0e0;
            }

            .section:last-child {
                border-bottom: none;
                margin-bottom: 0;
                padding-bottom: 0;
            }

            .section-title {
                font-size: 18px;
                font-weight: bold;
                color: #333;
                margin-bottom: 15px;
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .info-grid {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 20px;
            }

            .info-item {
                background: #f9f9f9;
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
                font-size: 16px;
                color: #333;
                font-weight: 600;
            }

            .seats-list {
                display: flex;
                flex-wrap: wrap;
                gap: 10px;
            }

            .seat-badge {
                background: #e50914;
                color: white;
                padding: 8px 16px;
                border-radius: 20px;
                font-size: 14px;
                font-weight: 600;
            }

            .price-summary {
                background: linear-gradient(135deg, #f5f5f5, #f9f9f9);
                padding: 20px;
                border-radius: 8px;
                margin-top: 20px;
            }

            .price-row {
                display: flex;
                justify-content: space-between;
                margin: 10px 0;
                font-size: 15px;
            }

            .price-row.total {
                border-top: 2px solid #ddd;
                padding-top: 15px;
                margin-top: 15px;
                font-size: 20px;
                font-weight: bold;
                color: #e50914;
            }

            .action-section {
                display: flex;
                gap: 15px;
                padding: 30px;
                border-top: 2px solid #e0e0e0;
                background: #f9f9f9;
                justify-content: flex-end;
                align-items: center;
            }

            .btn {
                padding: 12px 32px;
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
            }

            .btn-primary:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(229, 9, 20, 0.4);
            }

            .btn-secondary {
                background: #f0f0f0;
                color: #333;
            }

            .btn-secondary:hover {
                background: #e0e0e0;
            }

            .warning-box {
                background: #fff3cd;
                border-left: 4px solid #ffc107;
                padding: 15px;
                border-radius: 6px;
                margin-bottom: 20px;
                color: #856404;
            }

            .success-icon {
                font-size: 24px;
                margin-bottom: 10px;
            }

            @media (max-width: 768px) {
                .info-grid {
                    grid-template-columns: 1fr;
                }

                .action-section {
                    flex-direction: column-reverse;
                }

                .btn {
                    width: 100%;
                }

                .confirm-title {
                    font-size: 24px;
                }
            }
        </style>
    "/>
</jsp:include>

<div class="confirm-page">
    <div class="confirm-container">
        <div class="confirm-card">
            <!-- HEADER -->
            <div class="confirm-header">
                <div class="success-icon">✓</div>
                <div class="confirm-title">Xác nhận đặt vé</div>
                <div class="confirm-subtitle">Vui lòng kiểm tra thông tin và hoàn tất thanh toán</div>
            </div>

            <!-- CONTENT -->
            <div class="confirm-content">
                <!-- MOVIE INFO SECTION -->
                <div class="section">
                    <div class="section-title">🎬 Thông tin phim</div>
                    <div class="info-grid">
                        <div class="info-item">
                            <div class="info-label">Tên phim</div>
                            <div class="info-value">${movie.tenPhim}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Thể loại</div>
                            <div class="info-value">${movie.theLoai}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Thời lượng</div>
                            <div class="info-value">${movie.thoiLuong} phút</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Loại phim</div>
                            <div class="info-value">${movie.loaiPhim}</div>
                        </div>
                    </div>
                </div>

                <!-- SHOWTIME SECTION -->
                <div class="section">
                    <div class="section-title">📅 Lịch chiếu</div>
                    <div class="info-grid">
                        <div class="info-item">
                            <div class="info-label">Ngày chiếu</div>
                            <div class="info-value">
                                <fmt:formatDate value="${showtime.ngayChieu}" pattern="dd/MM/yyyy"/>
                            </div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Giờ chiếu</div>
                            <div class="info-value">
                                <fmt:formatDate value="${showtime.gioBatDau}" pattern="HH:mm"/> - 
                                <fmt:formatDate value="${showtime.gioKetThuc}" pattern="HH:mm"/>
                            </div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Phòng chiếu</div>
                            <div class="info-value">${showtime.tenPhong}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Rạp phim</div>
                            <div class="info-value">${showtime.tenRap}</div>
                        </div>
                    </div>
                </div>

                <!-- SEATS SECTION -->
                <div class="section">
                    <div class="section-title">🎫 Ghế đã chọn</div>
                    <div class="seats-list">
                        <c:forEach var="seat" items="${selectedSeats}">
                            <div class="seat-badge">
                                ${seat.hangGhe}${seat.soGhe}
                                <c:if test="${not empty seat.loaiGhe && seat.loaiGhe != 'Thường'}">
                                    (${seat.loaiGhe})
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <!-- PRICE SECTION -->
                <div class="section">
                    <div class="section-title">💰 Chi phí</div>
                    
                    <div class="warning-box">
                        ⚠️ Vui lòng thanh toán trong vòng 15 phút để giữ chỗ. Hết hạn sẽ mất vé!
                    </div>

                    <div class="price-summary">
                        <div class="price-row">
                            <span>Giá vé cơ bản × ${selectedSeats.size()} ghế</span>
                            <span><fmt:formatNumber value="${showtime.giaVeCoSo}" type="currency" currencySymbol="₫"/></span>
                        </div>
                        <div class="price-row">
                            <span>Loại ghế: 
                                <c:forEach var="seat" items="${selectedSeats}" varStatus="status">
                                    ${seat.loaiGhe}<c:if test="${not status.last}">, </c:if>
                                </c:forEach>
                            </span>
                        </div>
                        <div class="price-row total">
                            <span>TỔNG THANH TOÁN:</span>
                            <span><fmt:formatNumber value="${totalPrice}" type="currency" currencySymbol="₫"/></span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- ACTIONS -->
            <div class="action-section">
                <a href="booking?action=selectSeats&maSuatChieu=${showtime.maSuatChieu}" class="btn btn-secondary">
                    ← Chọn lại ghế
                </a>
                <button class="btn btn-primary" onclick="confirmPayment()">
                    💳 Thanh toán ngay
                </button>
            </div>
        </div>
    </div>
</div>

<script>
    function confirmPayment() {
        // In a real application, this would redirect to a payment gateway
        alert('Tính năng thanh toán đang được phát triển.\n\nThông tin vé sẽ được gửi qua email của bạn.');
        // Redirect to homepage or booking history
        window.location.href = 'movie?action=list';
    }
</script>

<jsp:include page="../layout/footer.jsp" />
