<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Xác nhận đặt vé"/>
    <jsp:param name="extraStyles" value="
        <style>
            .page-screen { min-height: 100vh; padding: 20px; background: var(--light-bg); }
            .container { max-width: 900px; margin: 0 auto; }
            .confirm-card { background: #fff; border-radius: 12px; box-shadow: 0 10px 30px rgba(0,0,0,.1); overflow: hidden; }
            .confirm-header { background: linear-gradient(135deg, #e50914, #c90812); color: #fff; padding: 30px; text-align: center; }
            .confirm-title { font-size: 28px; font-weight: 700; margin-bottom: 10px; }
            .confirm-subtitle { font-size: 16px; opacity: 0.9; }
            .confirm-content { padding: 30px; }
            .section { margin-bottom: 25px; padding-bottom: 20px; border-bottom: 1px solid #eee; }
            .section:last-child { border-bottom: none; margin-bottom: 0; }
            .section-title { font-size: 18px; font-weight: 700; color: #333; margin-bottom: 15px; display: flex; align-items: center; gap: 8px; }
            .info-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px; }
            .info-item { background: #f9f9f9; padding: 15px; border-radius: 8px; border-left: 4px solid #e50914; }
            .info-label { font-size: 12px; color: #999; text-transform: uppercase; margin-bottom: 5px; }
            .info-value { font-size: 16px; color: #333; font-weight: 600; }
            .seats-display { display: flex; flex-wrap: wrap; gap: 8px; }
            .seat-badge { background: #e50914; color: #fff; padding: 6px 12px; border-radius: 20px; font-size: 14px; font-weight: 600; }
            .products-display { display: flex; flex-wrap: wrap; gap: 8px; }
            .product-badge { background: #28a745; color: #fff; padding: 6px 12px; border-radius: 20px; font-size: 14px; font-weight: 600; }
            .price-summary { background: #f8f9fa; padding: 20px; border-radius: 8px; margin-top: 20px; }
            .price-row { display: flex; justify-content: space-between; margin: 8px 0; font-size: 15px; }
            .price-row.total { border-top: 2px solid #ddd; padding-top: 15px; margin-top: 15px; font-size: 20px; font-weight: 700; color: #e50914; }
            .action-buttons { display: flex; gap: 15px; padding: 20px; border-top: 2px solid #eee; background: #f9f9f9; }
            .btn { padding: 12px 24px; border: none; border-radius: 6px; font-size: 16px; font-weight: 600; cursor: pointer; transition: all 0.3s; text-decoration: none; display: inline-block; text-align: center; }
            .btn-primary { background: linear-gradient(135deg, #e50914, #c90812); color: #fff; }
            .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(229, 9, 20, 0.4); }
            .btn-secondary { background: #f0f0f0; color: #333; }
            .btn-secondary:hover { background: #e0e0e0; }
            .warning-box { background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; border-radius: 6px; margin-bottom: 20px; color: #856404; }
        </style>
    "/>
</jsp:include>

<div class="page-screen">
    <div class="container">
        <div class="confirm-card">
            <!-- Header -->
            <div class="confirm-header">
                <div style="font-size: 24px; margin-bottom: 10px;">✓</div>
                <div class="confirm-title">Xác nhận đặt vé</div>
                <div class="confirm-subtitle">Vui lòng kiểm tra thông tin và hoàn tất thanh toán</div>
            </div>

            <!-- Content -->
            <div class="confirm-content">
                <!-- Movie Info -->
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

                <!-- Showtime Info -->
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

                <!-- Seats -->
                <div class="section">
                    <div class="section-title">🎫 Ghế đã chọn</div>
                    <div class="seats-display">
                        <c:forTokens var="seat" items="${selectedSeats}" delims=",">
                            <div class="seat-badge">${seat}</div>
                        </c:forTokens>
                    </div>
                </div>

                <!-- Products -->
                <c:if test="${not empty selectedProducts}">
                    <div class="section">
                        <div class="section-title">🍿 Combo đã chọn</div>
                        <div class="products-display">
                            <c:forEach var="product" items="${selectedProducts}">
                                <div class="product-badge">${product.tenSP}</div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>

                <!-- Price Summary -->
                <div class="section">
                    <div class="section-title">💰 Chi phí</div>
                    
                    <div class="warning-box">
                        ⚠️ Vui lòng thanh toán trong vòng 15 phút để giữ chỗ. Hết hạn sẽ mất vé!
                    </div>

                    <div class="price-summary">
                        <div class="price-row">
                            <span>Giá vé cơ bản × <c:forTokens var="seat" items="${selectedSeats}" delims="," varStatus="status"><c:if test="${status.last}">${status.count}</c:if></c:forTokens> ghế</span>
                            <span><fmt:formatNumber value="${showtime.giaVeCoSo}" type="currency" currencySymbol="₫"/></span>
                        </div>
                        <c:if test="${not empty selectedProducts}">
                            <c:forEach var="product" items="${selectedProducts}">
                                <div class="price-row">
                                    <span>${product.tenSP}</span>
                                    <span><fmt:formatNumber value="${product.donGia}" type="currency" currencySymbol="₫"/></span>
                                </div>
                            </c:forEach>
                        </c:if>
                        <div class="price-row total">
                            <span>TỔNG THANH TOÁN:</span>
                            <span><fmt:formatNumber value="${showtime.giaVeCoSo * fn:length(fn:split(selectedSeats, ','))}" type="currency" currencySymbol="₫"/></span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Actions -->
            <div class="action-buttons">
                <a href="product-selection?maPhim=${movie.maPhim}&maSuatChieu=${showtime.maSuatChieu}&selectedSeats=${selectedSeats}" class="btn btn-secondary">
                    ← Chọn lại combo
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
        // Redirect to homepage
        window.location.href = 'home';
    }
</script>

<jsp:include page="../layout/footer.jsp" />
