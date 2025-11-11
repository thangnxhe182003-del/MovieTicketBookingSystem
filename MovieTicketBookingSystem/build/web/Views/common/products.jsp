<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Sản phẩm"/>
    <jsp:param name="extraStyles" value="
        <style>
            .products-container {
                max-width: 1200px;
                margin: 40px auto;
                padding: 0 20px;
            }
            
            .page-title {
                text-align: center;
                margin-bottom: 40px;
            }
            
            .page-title h1 {
                font-size: 36px;
                color: #e50914;
                margin-bottom: 10px;
            }
            
            .page-title p {
                font-size: 16px;
                color: #666;
            }
            
            .products-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
                gap: 30px;
                margin-bottom: 40px;
            }
            
            .product-card {
                background: white;
                border-radius: 12px;
                overflow: hidden;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                transition: transform 0.3s ease, box-shadow 0.3s ease;
                cursor: pointer;
            }
            
            .product-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
            }
            
            .product-image {
                width: 100%;
                height: 200px;
                object-fit: cover;
                background: #f5f5f5;
                display: block;
            }
            
            .product-image.hidden {
                display: none !important;
            }
            
            .product-info {
                padding: 20px;
            }
            
            .product-name {
                font-size: 18px;
                font-weight: 600;
                color: #333;
                margin-bottom: 10px;
                min-height: 54px;
            }
            
            .product-price {
                font-size: 24px;
                font-weight: 700;
                color: #e50914;
                margin-top: 15px;
            }
            
            .product-status {
                display: inline-block;
                padding: 4px 12px;
                border-radius: 12px;
                font-size: 12px;
                font-weight: 600;
                margin-top: 10px;
            }
            
            .status-active {
                background: #d4edda;
                color: #155724;
            }
            
            .no-products {
                text-align: center;
                padding: 60px 20px;
                color: #666;
            }
            
            .no-products i {
                font-size: 64px;
                margin-bottom: 20px;
                opacity: 0.5;
            }
            
            .no-products h3 {
                font-size: 24px;
                margin-bottom: 10px;
            }
            
            .no-products p {
                font-size: 16px;
            }
        </style>
    "/>
</jsp:include>

<div class="products-container">
    <div class="page-title">
        <h1>🍿 Sản phẩm</h1>
        <p>Combo đồ ăn và thức uống tại rạp</p>
    </div>

    <c:choose>
        <c:when test="${not empty products}">
            <div class="products-grid">
                <c:forEach var="product" items="${products}">
                    <div class="product-card">
                        <c:if test="${not empty product.thumbnailUrl}">
                            <img src="${pageContext.request.contextPath}/assets/image/${product.thumbnailUrl}" 
                                 alt="${product.tenSP}" 
                                 class="product-image"
                                 onerror="this.onerror=null; this.classList.add('hidden'); this.parentElement.querySelector('.product-info').style.paddingTop='20px';"
                                 onload="if(this.naturalWidth === 0 || this.naturalHeight === 0) { this.classList.add('hidden'); this.parentElement.querySelector('.product-info').style.paddingTop='20px'; }">
                        </c:if>
                        <div class="product-info">
                            <div class="product-name">${product.tenSP}</div>
                            <div class="product-price">
                                <fmt:formatNumber value="${product.donGia}" type="number" maxFractionDigits="0"/>₫
                            </div>
                            <span class="product-status status-active">Đang bán</span>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="no-products">
                <i class="fas fa-shopping-bag"></i>
                <h3>Chưa có sản phẩm nào</h3>
                <p>Hiện tại chưa có sản phẩm nào đang bán</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script>
    // Kiểm tra và ẩn ảnh nếu không hợp lệ hoặc lỗi
    document.addEventListener('DOMContentLoaded', function() {
        const productImages = document.querySelectorAll('.product-image');
        productImages.forEach(function(img) {
            // Kiểm tra khi ảnh load xong
            img.addEventListener('load', function() {
                // Nếu ảnh quá nhỏ (có thể là placeholder hoặc ảnh lỗi) hoặc không có kích thước
                if (this.naturalWidth === 0 || this.naturalHeight === 0 || 
                    this.naturalWidth < 50 || this.naturalHeight < 50) {
                    this.classList.add('hidden');
                    const productInfo = this.parentElement.querySelector('.product-info');
                    if (productInfo) {
                        productInfo.style.paddingTop = '20px';
                    }
                }
            });
            
            // Kiểm tra nếu ảnh không load được
            img.addEventListener('error', function() {
                this.classList.add('hidden');
                this.onerror = null; // Tránh lặp vô hạn
                const productInfo = this.parentElement.querySelector('.product-info');
                if (productInfo) {
                    productInfo.style.paddingTop = '20px';
                }
            });
            
            // Kiểm tra ngay lập tức nếu ảnh đã load trước khi script chạy
            if (img.complete) {
                if (img.naturalWidth === 0 || img.naturalHeight === 0 || 
                    img.naturalWidth < 50 || img.naturalHeight < 50) {
                    img.classList.add('hidden');
                    const productInfo = img.parentElement.querySelector('.product-info');
                    if (productInfo) {
                        productInfo.style.paddingTop = '20px';
                    }
                }
            }
        });
    });
</script>

<jsp:include page="../layout/footer.jsp" />

