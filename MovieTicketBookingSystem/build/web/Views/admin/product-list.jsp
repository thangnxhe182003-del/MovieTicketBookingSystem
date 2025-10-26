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
                                     onerror="this.src='https://via.placeholder.com/50x50?text=No+Image'">
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
                                    <a href="admin-products?action=delete&maSP=${product.maSP}" 
                                       class="btn-action btn-delete"
                                       onclick="return confirmDelete('Bạn có chắc chắn muốn xóa sản phẩm này?')">
                                        <i class="fas fa-trash"></i> Xóa
                                    </a>
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
</script>

<jsp:include page="../layout/admin-footer.jsp" />
