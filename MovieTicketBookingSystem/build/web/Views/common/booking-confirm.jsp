<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Xác nhận đặt vé"/>
    <jsp:param name="extraStyles" value="
        <style>
            .page-screen { 
                min-height: 100vh; 
                padding: 20px; 
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                position: relative;
            }
            
            .page-screen::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background: radial-gradient(circle at 20% 20%, rgba(255,255,255,0.1) 0%, transparent 50%),
                            radial-gradient(circle at 80% 80%, rgba(255,255,255,0.1) 0%, transparent 50%),
                            radial-gradient(circle at 40% 40%, rgba(255,255,255,0.05) 0%, transparent 50%);
                pointer-events: none;
            }
            
            .container { 
                max-width: 1000px; 
                margin: 0 auto; 
                position: relative;
                z-index: 1;
            }
            
            .confirm-card { 
                background: rgba(255, 255, 255, 0.95); 
                border-radius: 20px; 
                box-shadow: 0 20px 60px rgba(0,0,0,0.2); 
                overflow: hidden;
                backdrop-filter: blur(20px);
                border: 1px solid rgba(255, 255, 255, 0.3);
            }
            
            .confirm-header { 
                background: linear-gradient(135deg, #e50914, #c90812); 
                color: #fff; 
                padding: 40px 30px; 
                text-align: center;
                position: relative;
                overflow: hidden;
            }
            
            .confirm-header::before {
                content: '';
                position: absolute;
                top: -50%;
                left: -50%;
                width: 200%;
                height: 200%;
                background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
                animation: float 6s ease-in-out infinite;
            }
            
            @keyframes float {
                0%, 100% { transform: translateY(0px) rotate(0deg); }
                50% { transform: translateY(-20px) rotate(180deg); }
            }
            
            .confirm-title { 
                font-size: 32px; 
                font-weight: 800; 
                margin-bottom: 15px;
                text-shadow: 0 2px 4px rgba(0,0,0,0.3);
                position: relative;
                z-index: 1;
            }
            
            .confirm-subtitle { 
                font-size: 18px; 
                opacity: 0.9;
                position: relative;
                z-index: 1;
            }
            
            .confirm-content { 
                padding: 40px; 
                background: rgba(255, 255, 255, 0.8);
            }
            
            .section { 
                margin-bottom: 30px; 
                padding-bottom: 25px; 
                border-bottom: 2px solid rgba(229, 9, 20, 0.1);
                position: relative;
            }
            
            .section:last-child { 
                border-bottom: none; 
                margin-bottom: 0; 
            }
            
            .section-title { 
                font-size: 22px; 
                font-weight: 700; 
                color: #333; 
                margin-bottom: 20px; 
                display: flex; 
                align-items: center; 
                gap: 12px;
                position: relative;
            }
            
            .section-title::after {
                content: '';
                flex: 1;
                height: 2px;
                background: linear-gradient(90deg, #e50914, transparent);
                margin-left: 10px;
            }
            
            .info-grid { 
                display: grid; 
                grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); 
                gap: 20px; 
            }
            
            .info-item { 
                background: linear-gradient(135deg, #f8f9fa, #e9ecef); 
                padding: 20px; 
                border-radius: 15px; 
                border-left: 5px solid #e50914;
                box-shadow: 0 5px 15px rgba(0,0,0,0.1);
                transition: all 0.3s ease;
                position: relative;
                overflow: hidden;
            }
            
            .info-item::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background: linear-gradient(135deg, rgba(229, 9, 20, 0.05), transparent);
                opacity: 0;
                transition: opacity 0.3s ease;
            }
            
            .info-item:hover::before {
                opacity: 1;
            }
            
            .info-item:hover {
                transform: translateY(-5px);
                box-shadow: 0 10px 25px rgba(0,0,0,0.15);
            }
            
            .info-label { 
                font-size: 13px; 
                color: #666; 
                text-transform: uppercase; 
                margin-bottom: 8px;
                font-weight: 600;
                letter-spacing: 1px;
            }
            
            .info-value { 
                font-size: 18px; 
                color: #333; 
                font-weight: 700;
                position: relative;
                z-index: 1;
            }
            
            .seats-display { 
                display: flex; 
                flex-wrap: wrap; 
                gap: 12px;
                margin-top: 15px;
            }
            
            .seat-badge { 
                background: linear-gradient(135deg, #e50914, #c90812); 
                color: #fff; 
                padding: 10px 16px; 
                border-radius: 25px; 
                font-size: 15px; 
                font-weight: 700;
                box-shadow: 0 4px 12px rgba(229, 9, 20, 0.3);
                transition: all 0.3s ease;
                position: relative;
                overflow: hidden;
            }
            
            .seat-badge::before {
                content: '';
                position: absolute;
                top: 0;
                left: -100%;
                width: 100%;
                height: 100%;
                background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent);
                transition: left 0.5s ease;
            }
            
            .seat-badge:hover::before {
                left: 100%;
            }
            
            .seat-badge:hover {
                transform: translateY(-2px);
                box-shadow: 0 6px 20px rgba(229, 9, 20, 0.4);
            }
            
            .products-display { 
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 20px;
                margin-top: 15px;
            }
            
            .product-item { 
                background: linear-gradient(135deg, #f8f9fa, #e9ecef); 
                border-radius: 15px; 
                padding: 15px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.1);
                transition: all 0.3s ease;
                position: relative;
                overflow: hidden;
                border: 2px solid rgba(40, 167, 69, 0.1);
            }
            
            .product-item:hover {
                transform: translateY(-5px);
                box-shadow: 0 10px 25px rgba(0,0,0,0.15);
                border-color: #28a745;
            }
            
            .product-image {
                width: 100%;
                height: 120px;
                object-fit: cover;
                border-radius: 10px;
                margin-bottom: 10px;
                opacity: 0;
                transition: opacity 0.3s ease;
                background: #f0f0f0;
            }
            
            .product-image.loaded {
                opacity: 1;
            }
            
            .product-image.error {
                opacity: 1;
                background: linear-gradient(135deg, #f8f9fa, #e9ecef);
                display: flex;
                align-items: center;
                justify-content: center;
                color: #666;
                font-size: 12px;
                text-align: center;
            }
            
            .product-name {
                font-size: 16px;
                font-weight: 700;
                color: #333;
                margin-bottom: 5px;
                text-align: center;
            }
            
            .product-quantity {
                background: linear-gradient(135deg, #28a745, #20c997); 
                color: #fff; 
                padding: 8px 12px; 
                border-radius: 20px; 
                font-size: 14px; 
                font-weight: 700;
                text-align: center;
                box-shadow: 0 4px 12px rgba(40, 167, 69, 0.3);
            }
            
            .price-summary { 
                background: linear-gradient(135deg, #f8f9fa, #e9ecef); 
                padding: 25px; 
                border-radius: 15px; 
                margin-top: 25px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.1);
                border: 1px solid rgba(229, 9, 20, 0.1);
            }
            
            .price-row { 
                display: flex; 
                justify-content: space-between; 
                margin: 12px 0; 
                font-size: 16px;
                padding: 8px 0;
                border-bottom: 1px solid rgba(0,0,0,0.05);
                transition: all 0.3s ease;
            }
            
            .price-row:hover {
                background: rgba(229, 9, 20, 0.05);
                border-radius: 8px;
                padding-left: 10px;
                padding-right: 10px;
            }
            
            .price-row.total { 
                border-top: 3px solid #e50914; 
                padding-top: 20px; 
                margin-top: 20px; 
                font-size: 24px; 
                font-weight: 800; 
                color: #e50914;
                background: linear-gradient(135deg, rgba(229, 9, 20, 0.1), rgba(201, 8, 18, 0.05));
                border-radius: 12px;
                padding-left: 15px;
                padding-right: 15px;
                text-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            
            .action-buttons { 
                display: flex; 
                gap: 20px; 
                padding: 30px; 
                border-top: 2px solid rgba(229, 9, 20, 0.1); 
                background: linear-gradient(135deg, #f8f9fa, #e9ecef);
            }
            
            .btn { 
                padding: 15px 30px; 
                border: none; 
                border-radius: 12px; 
                font-size: 18px; 
                font-weight: 700; 
                cursor: pointer; 
                transition: all 0.3s ease; 
                text-decoration: none; 
                display: inline-block; 
                text-align: center;
                position: relative;
                overflow: hidden;
                flex: 1;
            }
            
            .btn-primary { 
                background: linear-gradient(135deg, #e50914, #c90812); 
                color: #fff;
                box-shadow: 0 8px 25px rgba(229, 9, 20, 0.3);
            }
            
            .btn-primary:hover { 
                transform: translateY(-3px); 
                box-shadow: 0 12px 35px rgba(229, 9, 20, 0.4);
            }
            
            .btn-secondary { 
                background: linear-gradient(135deg, #6c757d, #495057); 
                color: #fff;
                box-shadow: 0 8px 25px rgba(108, 117, 125, 0.3);
            }
            
            .btn-secondary:hover { 
                transform: translateY(-3px);
                box-shadow: 0 12px 35px rgba(108, 117, 125, 0.4);
            }
            
            .warning-box { 
                background: linear-gradient(135deg, #fff3cd, #ffeaa7); 
                border-left: 5px solid #ffc107; 
                padding: 20px; 
                border-radius: 12px; 
                margin-bottom: 25px; 
                color: #856404;
                box-shadow: 0 5px 15px rgba(255, 193, 7, 0.2);
                font-weight: 600;
            }
            
            .summary-stats {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
                gap: 15px;
                margin-top: 15px;
            }
            
            .stat-item {
                background: rgba(255, 255, 255, 0.7);
                padding: 15px;
                border-radius: 10px;
                text-align: center;
                border: 2px solid rgba(229, 9, 20, 0.1);
                transition: all 0.3s ease;
            }
            
            .stat-item:hover {
                transform: translateY(-3px);
                border-color: #e50914;
                box-shadow: 0 8px 20px rgba(229, 9, 20, 0.2);
            }
            
            .stat-number {
                font-size: 24px;
                font-weight: 800;
                color: #e50914;
                display: block;
            }
            
            .stat-label {
                font-size: 12px;
                color: #666;
                text-transform: uppercase;
                font-weight: 600;
                letter-spacing: 1px;
            }
            
            @media (max-width: 768px) {
                .page-screen { padding: 10px; }
                .confirm-content { padding: 20px; }
                .confirm-title { font-size: 24px; }
                .info-grid { grid-template-columns: 1fr; }
                .action-buttons { flex-direction: column; }
                .btn { width: 100%; }
            }
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
                            <div class="info-value">${formattedDate}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Giờ chiếu</div>
                            <div class="info-value">${formattedStartTime} - ${formattedEndTime}</div>
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
                        <c:forEach var="seat" items="${selectedSeatsList}">
                            <div class="seat-badge">${seat.hangGhe}${seat.soGhe}</div>
                        </c:forEach>
                    </div>
                </div>

                <!-- Products -->
                <c:if test="${not empty selectedProducts}">
                    <div class="section">
                        <div class="section-title">🍿 Combo đã chọn</div>
                        <div class="products-display">
                            <c:forEach var="productInfo" items="${selectedProducts}">
                                <div class="product-item">
                                    <img src="${pageContext.request.contextPath}/assets/image/${productInfo.product.thumbnailUrl}" 
                                         alt="${productInfo.product.tenSP}" 
                                         class="product-image"
                                         data-fallback="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjEyMCIgdmlld0JveD0iMCAwIDIwMCAxMjAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIyMDAiIGhlaWdodD0iMTIwIiBmaWxsPSIjRjhGOUZBIi8+CjxwYXRoIGQ9Ik0xMDAgNDBDMTAwIDM1LjU4MTcgMTAzLjU4MiAzMiAxMDggMzJIMTEyQzExNi40MTggMzIgMTIwIDM1LjU4MTcgMTIwIDQwVjgwQzEyMCA4NC40MTgzIDExNi40MTggODggMTEyIDg4SDEwOEMxMDMuNTgyIDg4IDEwMCA4NC40MTgzIDEwMCA4MFY0MFoiIGZpbGw9IiNEOUQ5RDkiLz4KPHN2ZyB4PSI4NSIgeT0iNDUiIHdpZHRoPSIzMCIgaGVpZ2h0PSIzMCIgdmlld0JveD0iMCAwIDI0IDI0IiBmaWxsPSJub25lIj4KPHBhdGggZD0iTTEyIDJMMTMuMDkgOC4yNkwyMCA5TDEzLjA5IDE1Ljc0TDEyIDIyTDEwLjkxIDE1Ljc0TDQgOUwxMC45MSA4LjI2TDEyIDJaIiBmaWxsPSIjOTk5Ii8+Cjwvc3ZnPgo8L3N2Zz4K">
                                    <div class="product-name">${productInfo.product.tenSP}</div>
                                    <div class="product-quantity">Số lượng: ${productInfo.quantity}</div>
                                </div>
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
                        <!-- Seat prices -->
                        <c:set var="seatCount" value="${fn:length(selectedSeatsList)}"/>
                        <div class="price-row">
                            <span>🎫 Giá vé × ${seatCount} ghế</span>
                            <span><fmt:formatNumber value="${seatTotalPrice}" type="currency" currencySymbol="₫"/></span>
                        </div>
                        
                        <!-- Product prices -->
                        <c:if test="${not empty selectedProducts}">
                            <c:forEach var="productInfo" items="${selectedProducts}">
                                <div class="price-row">
                                    <span>🍿 ${productInfo.product.tenSP} x${productInfo.quantity}</span>
                                    <span><fmt:formatNumber value="${productInfo.subtotal}" type="currency" currencySymbol="₫"/></span>
                                </div>
                            </c:forEach>
                        </c:if>
                        
                        <!-- Total -->
                        <div class="price-row total">
                            <span>💳 TỔNG THANH TOÁN:</span>
                            <span><fmt:formatNumber value="${grandTotal}" type="currency" currencySymbol="₫"/></span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Actions -->
            <div class="action-buttons">
                <a href="booking?action=selectSeats&maSuatChieu=${showtime.maSuatChieu}" class="btn btn-secondary">
                    ← Chọn lại ghế & combo
                </a>
                <form id="paymentForm" method="POST" action="booking?action=processPayment" style="flex: 1;">
                    <input type="hidden" name="maSuatChieu" value="${showtime.maSuatChieu}">
                    <input type="hidden" name="selectedSeats" value="${selectedSeats}">
                    <input type="hidden" name="selectedProducts">
                    <input type="hidden" name="holdIds" value="${holdIds}">
                    <button type="submit" class="btn btn-primary">
                    💳 Thanh toán ngay
                </button>
                </form>
            </div>
        </div>
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
        
        // Handle payment form submission
        const paymentForm = document.getElementById('paymentForm');
        if (paymentForm) {
            paymentForm.addEventListener('submit', function(e) {
                const selectedProducts = [];
                <c:forEach var="productInfo" items="${selectedProducts}">
                    selectedProducts.push('${productInfo.product.maSP}:${productInfo.quantity}');
                </c:forEach>
                const selectedProductsInput = paymentForm.querySelector('input[name="selectedProducts"]');
                selectedProductsInput.value = selectedProducts.join(',');
            });
        }
    });
    
    function confirmPayment() {
        // In a real application, this would redirect to a payment gateway
        alert('Tính năng thanh toán đang được phát triển.\n\nThông tin vé sẽ được gửi qua email của bạn.');
        // Redirect to homepage
        window.location.href = 'home';
    }
</script>

<jsp:include page="../layout/footer.jsp" />
