<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Chọn combo đồ ăn"/>
    <jsp:param name="extraStyles" value="
        <style>
            .page-screen { min-height: 100vh; padding: 20px; background: var(--light-bg); }
            .container { max-width: 1200px; margin: 0 auto; }
            .movie-info { background: #fff; border-radius: 12px; padding: 20px; margin-bottom: 20px; box-shadow: 0 4px 12px rgba(0,0,0,.1); }
            .movie-poster { width: 80px; height: 120px; object-fit: cover; border-radius: 8px; }
            .showtime-info { background: #fff; border-radius: 12px; padding: 20px; margin-bottom: 20px; box-shadow: 0 4px 12px rgba(0,0,0,.1); }
            .product-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin-bottom: 20px; }
            .product-card { background: #fff; border: 2px solid #ddd; border-radius: 12px; padding: 20px; text-align: center; cursor: pointer; transition: all 0.3s; }
            .product-card:hover { border-color: #e50914; transform: translateY(-4px); }
            .product-card.selected { border-color: #e50914; background: #ffe6e6; }
            .product-image { width: 100px; height: 100px; object-fit: cover; border-radius: 8px; margin-bottom: 15px; }
            .product-name { font-size: 16px; font-weight: 700; color: #333; margin-bottom: 10px; }
            .product-price { font-size: 18px; font-weight: 700; color: #e50914; }
            .quantity-controls { display: flex; align-items: center; justify-content: center; gap: 10px; margin-top: 10px; }
            .quantity-btn { width: 30px; height: 30px; border: 1px solid #ddd; background: #fff; border-radius: 4px; cursor: pointer; }
            .quantity-input { width: 50px; text-align: center; border: 1px solid #ddd; border-radius: 4px; padding: 5px; }
            .btn-continue { background: #e50914; color: #fff; padding: 12px 24px; border: none; border-radius: 6px; font-weight: 600; cursor: pointer; margin-top: 20px; }
            .btn-continue:disabled { background: #ccc; cursor: not-allowed; }
            .summary { background: #fff; border-radius: 12px; padding: 20px; margin-bottom: 20px; box-shadow: 0 4px 12px rgba(0,0,0,.1); }
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
            <p style="margin: 0; color: #666;">Ghế đã chọn: ${selectedSeats}</p>
        </div>

        <!-- Product Selection -->
        <div style="display: flex; align-items: center; gap: 15px; margin-bottom: 20px;">
            <a href="seat-selection?maPhim=${movie.maPhim}&maSuatChieu=${showtime.maSuatChieu}" style="color: #e50914; text-decoration: none; font-weight: 600;">← Chọn lại ghế</a>
            <h2 style="margin: 0; color: #333;">Chọn combo đồ ăn (tùy chọn)</h2>
        </div>
        <form id="productForm" method="post" action="product-selection">
            <input type="hidden" name="maPhim" value="${movie.maPhim}">
            <input type="hidden" name="maSuatChieu" value="${showtime.maSuatChieu}">
            <input type="hidden" name="selectedSeats" value="${selectedSeats}">
            <input type="hidden" name="selectedProducts" id="selectedProducts" value="">
            
            <div class="product-grid">
                <c:forEach var="product" items="${products}">
                    <div class="product-card" data-product-id="${product.maSP}">
                        <img src="${pageContext.request.contextPath}/assets/image/${product.thumbnailUrl}" alt="${product.tenSP}" class="product-image" onerror="this.src='https://via.placeholder.com/100x100?text=No+Image'">
                        <div class="product-name">${product.tenSP}</div>
                        <div class="product-price">${product.donGia} VNĐ</div>
                        <div class="quantity-controls">
                            <button type="button" class="quantity-btn" onclick="decreaseQuantity(${product.maSP})">-</button>
                            <input type="number" class="quantity-input" id="qty-${product.maSP}" value="0" min="0" max="10" readonly>
                            <button type="button" class="quantity-btn" onclick="increaseQuantity(${product.maSP})">+</button>
                        </div>
                    </div>
                </c:forEach>
            </div>
            
            <div style="display: flex; gap: 12px; margin-top: 20px;">
                <a href="seat-selection?maPhim=${movie.maPhim}&maSuatChieu=${showtime.maSuatChieu}" class="btn btn-secondary">← Chọn lại ghế</a>
                <button type="submit" class="btn-continue" style="flex: 1;">Xác nhận đặt vé</button>
            </div>
        </form>
    </div>
</div>

<script>
    function increaseQuantity(productId) {
        const input = document.getElementById('qty-' + productId);
        const currentValue = parseInt(input.value);
        if (currentValue < 10) {
            input.value = currentValue + 1;
            updateSelectedProducts();
        }
    }
    
    function decreaseQuantity(productId) {
        const input = document.getElementById('qty-' + productId);
        const currentValue = parseInt(input.value);
        if (currentValue > 0) {
            input.value = currentValue - 1;
            updateSelectedProducts();
        }
    }
    
    function updateSelectedProducts() {
        const selectedProducts = [];
        document.querySelectorAll('.quantity-input').forEach(input => {
            const productId = input.id.replace('qty-', '');
            const quantity = parseInt(input.value);
            if (quantity > 0) {
                selectedProducts.push(productId + ':' + quantity);
            }
        });
        document.getElementById('selectedProducts').value = selectedProducts.join(',');
    }
</script>

<jsp:include page="../layout/footer.jsp" />
