<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Quản lý sản phẩm"/>
    <jsp:param name="pageSubtitle" value="Danh sách và quản lý các combo đồ ăn"/>
    <jsp:param name="currentPage" value="products"/>
    <jsp:param name="extraStyles" value="
        <style>
            .table-container {
                padding: 0;
            }
            
            .table-header {
                background: #f8f9fa;
                padding: 20px 30px;
                border-bottom: 1px solid #e9ecef;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            
            .table-title {
                font-size: 18px;
                font-weight: 600;
                color: #333;
                margin: 0;
            }
            
            .btn-add {
                background: linear-gradient(135deg, #e50914, #c90812);
                color: white;
                padding: 10px 20px;
                border: none;
                border-radius: 6px;
                text-decoration: none;
                font-weight: 600;
                display: inline-flex;
                align-items: center;
                gap: 8px;
                transition: all 0.3s;
            }
            
            .btn-add:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(229, 9, 20, 0.4);
            }
            
            .data-table {
                width: 100%;
                border-collapse: collapse;
            }
            
            .data-table th {
                background: #f8f9fa;
                padding: 15px 20px;
                text-align: left;
                font-weight: 600;
                color: #333;
                border-bottom: 2px solid #e9ecef;
            }
            
            .data-table td {
                padding: 15px 20px;
                border-bottom: 1px solid #e9ecef;
                vertical-align: middle;
            }
            
            .data-table tr:hover {
                background: #f8f9fa;
            }
            
            .product-image {
                width: 50px;
                height: 50px;
                object-fit: cover;
                border-radius: 8px;
                border: 2px solid #e9ecef;
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
                font-size: 8px;
                text-align: center;
            }
            
            .product-name {
                font-weight: 600;
                color: #333;
                margin-bottom: 4px;
            }
            
            .product-price {
                color: #e50914;
                font-weight: 700;
                font-size: 16px;
            }
            
            .status-badge {
                padding: 4px 12px;
                border-radius: 20px;
                font-size: 12px;
                font-weight: 600;
                text-transform: uppercase;
            }
            
            .status-active {
                background: #d4edda;
                color: #155724;
            }
            
            .status-inactive {
                background: #f8d7da;
                color: #721c24;
            }
            
            .action-buttons {
                display: flex;
                gap: 8px;
            }
            
            .btn-action {
                padding: 6px 12px;
                border: none;
                border-radius: 4px;
                text-decoration: none;
                font-size: 12px;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.3s;
            }
            
            .btn-edit {
                background: #17a2b8;
                color: white;
            }
            
            .btn-edit:hover {
                background: #138496;
            }
            
            .btn-delete {
                background: #dc3545;
                color: white;
            }
            
            .btn-delete:hover {
                background: #c82333;
            }
            
            .empty-state {
                text-align: center;
                padding: 60px 20px;
                color: #6c757d;
            }
            
            .empty-state i {
                font-size: 48px;
                margin-bottom: 20px;
                color: #dee2e6;
            }
            
            .empty-state h3 {
                margin-bottom: 10px;
                color: #495057;
            }
            
            .empty-state p {
                margin-bottom: 20px;
            }
            
            .search-filter {
                background: #f8f9fa;
                padding: 20px 30px;
                border-bottom: 1px solid #e9ecef;
                display: flex;
                gap: 20px;
                align-items: center;
            }
            
            .search-box {
                flex: 1;
                max-width: 300px;
            }
            
            .search-box input {
                width: 100%;
                padding: 10px 15px;
                border: 1px solid #ddd;
                border-radius: 6px;
                font-size: 14px;
            }
            
            .filter-select {
                padding: 10px 15px;
                border: 1px solid #ddd;
                border-radius: 6px;
                font-size: 14px;
                background: white;
            }
        </style>
    "/>
</jsp:include>

<div class="table-container">
    <!-- Search and Filter -->
    <div class="search-filter">
        <div class="search-box">
            <input type="text" placeholder="Tìm kiếm sản phẩm..." id="searchInput">
        </div>
        <select class="filter-select" id="statusFilter">
            <option value="">Tất cả trạng thái</option>
            <option value="Active">Đang bán</option>
            <option value="Không bán">Không bán</option>
        </select>
    </div>

    <!-- Table Header -->
    <div class="table-header">
        <h2 class="table-title">Danh sách sản phẩm</h2>
        <a href="admin-products?action=create" class="btn-add">
            <i class="fas fa-plus"></i>
            Thêm sản phẩm
        </a>
    </div>

    <!-- Data Table -->
    <c:choose>
        <c:when test="${empty products}">
            <div class="empty-state">
                <i class="fas fa-box-open"></i>
                <h3>Chưa có sản phẩm nào</h3>
                <p>Hãy thêm sản phẩm đầu tiên để bắt đầu quản lý</p>
                <a href="admin-products?action=create" class="btn-add">
                    <i class="fas fa-plus"></i>
                    Thêm sản phẩm đầu tiên
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Hình ảnh</th>
                        <th>Tên sản phẩm</th>
                        <th>Giá</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="product" items="${products}">
                        <tr>
                            <td>
                                <img src="${pageContext.request.contextPath}/assets/image/${product.thumbnailUrl}" 
                                     alt="${product.tenSP}" 
                                     class="product-image"
                                     data-fallback="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNTAiIGhlaWdodD0iNTAiIHZpZXdCb3g9IjAgMCA1MCA1MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjUwIiBoZWlnaHQ9IjUwIiBmaWxsPSIjRjhGOUZBIi8+CjxwYXRoIGQ9Ik0yNSAxMEMyNSA3LjIzOTEgMjcuMjM5MSA1IDMwIDVIMjBDMjIuNzYwOSA1IDI1IDcuMjM5MSAyNSAxMFY0MEMyNSA0Mi43NjA5IDIyLjc2MDkgNDUgMjAgNDVIMzBDMjcuMjM5MSA0NSAyNSA0Mi43NjA5IDI1IDQwVjEwWiIgZmlsbD0iI0Q5RDlEOSIvPgo8c3ZnIHg9IjE4IiB5PSIyMCIgd2lkdGg9IjE0IiBoZWlnaHQ9IjE0IiB2aWV3Qm94PSIwIDAgMjQgMjQiIGZpbGw9Im5vbmUiPgo8cGF0aCBkPSJNMTIgMkwxMy4wOSA4LjI2TDIwIDlMMTMuMDkgMTUuNzRMMTIgMjJMMTAuOTEgMTUuNzRMNCA5TDEwLjkxIDguMjZMMTIgMloiIGZpbGw9IiM5OTkiLz4KPC9zdmc+Cjwvc3ZnPgo=">
                            </td>
                            <td>
                                <div class="product-name">${product.tenSP}</div>
                            </td>
                            <td>
                                <div class="product-price">
                                    <fmt:formatNumber value="${product.donGia}" type="currency" currencySymbol="₫"/>
                                </div>
                            </td>
                            <td>
                                <span class="status-badge ${product.trangThai == 'Active' ? 'status-active' : 'status-inactive'}">
                                    ${product.trangThai}
                                </span>
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <a href="admin-products?action=edit&maSP=${product.maSP}" class="btn-action btn-edit">
                                        <i class="fas fa-edit"></i> Sửa
                                    </a>
                                    <c:choose>
                                        <c:when test="${product.trangThai == 'Active'}">
                                            <a href="admin-products?action=delete&maSP=${product.maSP}" 
                                               class="btn-action btn-delete"
                                               onclick="return confirmDelete('Bạn có chắc chắn muốn ngừng bán sản phẩm này?')">
                                                <i class="fas fa-ban"></i> Ngừng bán
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="btn-action" style="background: #6c757d; color: white; cursor: not-allowed;" title="Sản phẩm đã ngừng bán">
                                                <i class="fas fa-ban"></i> Đã ngừng
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
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
    });
    
    // Search functionality
    document.getElementById('searchInput').addEventListener('input', function() {
        const searchTerm = this.value.toLowerCase();
        const rows = document.querySelectorAll('.data-table tbody tr');
        
        rows.forEach(row => {
            const productName = row.querySelector('.product-name').textContent.toLowerCase();
            if (productName.includes(searchTerm)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    });

    // Filter by status
    document.getElementById('statusFilter').addEventListener('change', function() {
        const filterValue = this.value;
        const rows = document.querySelectorAll('.data-table tbody tr');
        
        rows.forEach(row => {
            const statusBadge = row.querySelector('.status-badge');
            const status = statusBadge.textContent.trim();
            
            if (filterValue === '' || status === filterValue) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    });

    // Confirm delete function
    function confirmDelete(message) {
        return confirm(message);
    }
</script>

<jsp:include page="../layout/admin-footer.jsp" />
