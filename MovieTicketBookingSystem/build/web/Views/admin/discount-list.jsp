<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Quản lý mã giảm giá"/>
    <jsp:param name="pageSubtitle" value="Danh sách và quản lý các mã giảm giá"/>
    <jsp:param name="currentPage" value="discounts"/>
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
            
            .discount-code {
                font-weight: 600;
                color: #e50914;
                font-size: 14px;
                font-family: 'Courier New', monospace;
            }
            
            .discount-name {
                font-weight: 600;
                color: #333;
                margin-bottom: 4px;
            }
            
            .discount-type {
                color: #6c757d;
                font-size: 12px;
            }
            
            .discount-value {
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
            
            .status-expired {
                background: #fff3cd;
                color: #856404;
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
            <input type="text" placeholder="Tìm kiếm mã giảm giá..." id="searchInput">
        </div>
        <select class="filter-select" id="statusFilter">
            <option value="">Tất cả trạng thái</option>
            <option value="Hoạt Động">Hoạt Động</option>
            <option value="Hết hạn">Hết hạn</option>
            <option value="Không hoạt động">Không hoạt động</option>
        </select>
    </div>

    <!-- Table Header -->
    <div class="table-header">
        <h2 class="table-title">Danh sách mã giảm giá</h2>
        <a href="admin-discounts?action=create" class="btn-add">
            <i class="fas fa-plus"></i>
            Thêm mã giảm giá
        </a>
    </div>

    <!-- Data Table -->
    <c:choose>
        <c:when test="${empty discounts}">
            <div class="empty-state">
                <i class="fas fa-tag"></i>
                <h3>Chưa có mã giảm giá nào</h3>
                <p>Hãy thêm mã giảm giá đầu tiên để bắt đầu quản lý</p>
                <a href="admin-discounts?action=create" class="btn-add">
                    <i class="fas fa-plus"></i>
                    Thêm mã giảm giá đầu tiên
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Mã code</th>
                        <th>Tên chương trình</th>
                        <th>Loại giảm giá</th>
                        <th>Hình thức</th>
                        <th>Giá trị giảm</th>
                        <th>Ngày bắt đầu</th>
                        <th>Ngày kết thúc</th>
                        <th>Trạng thái</th>
                        <th>Đã sử dụng</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="discount" items="${discounts}">
                        <tr>
                            <td>
                                <div class="discount-code">${discount.maCode}</div>
                            </td>
                            <td>
                                <div class="discount-name">${discount.tenGiamGia}</div>
                                <c:if test="${not empty discount.moTa}">
                                    <div class="discount-type">${discount.moTa}</div>
                                </c:if>
                            </td>
                            <td>
                                <div class="discount-type">${discount.loaiGiamGia}</div>
                            </td>
                            <td>
                                <div class="discount-type">${discount.hinhThucGiam == 'PhanTram' ? 'Phần trăm' : 'Tiền mặt'}</div>
                            </td>
                            <td>
                                <div class="discount-value">
                                    <c:choose>
                                        <c:when test="${discount.hinhThucGiam == 'PhanTram'}">
                                            ${discount.giaTriGiam}%
                                            <c:if test="${not empty discount.giaTriToiDa}">
                                                <br><small style="color: #6c757d; font-size: 12px;">Tối đa: <fmt:formatNumber value="${discount.giaTriToiDa}" type="currency" currencySymbol="₫"/></small>
                                            </c:if>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatNumber value="${discount.giaTriGiam}" type="currency" currencySymbol="₫"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                            <td>
                                ${ngayBatDauFormatted[discount.maGiamGia]}
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${ngayKetThucFormatted[discount.maGiamGia] != null}">
                                        ${ngayKetThucFormatted[discount.maGiamGia]}
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: #6c757d;">Không giới hạn</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${discount.trangThai == 'Hoạt Động'}">
                                        <span class="status-badge status-active">${discount.trangThai}</span>
                                    </c:when>
                                    <c:when test="${discount.trangThai == 'Hết hạn'}">
                                        <span class="status-badge status-expired">${discount.trangThai}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge status-inactive">${discount.trangThai}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty discount.soLanSuDung}">
                                        ${discount.daSuDung} / ${discount.soLanSuDung}
                                    </c:when>
                                    <c:otherwise>
                                        ${discount.daSuDung} / ∞
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <a href="admin-discounts?action=edit&maGiamGia=${discount.maGiamGia}" class="btn-action btn-edit">
                                        <i class="fas fa-edit"></i> Sửa
                                    </a>
                                    <a href="admin-discounts?action=delete&maGiamGia=${discount.maGiamGia}" 
                                       class="btn-action btn-delete"
                                       onclick="return confirmDelete('Bạn có chắc chắn muốn vô hiệu hóa mã giảm giá này?')">
                                        <i class="fas fa-trash"></i> Vô hiệu hóa
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
    function confirmDelete(message) {
        return confirm(message);
    }
    
    // Search functionality
    document.getElementById('searchInput').addEventListener('input', function() {
        const searchTerm = this.value.toLowerCase();
        const rows = document.querySelectorAll('.data-table tbody tr');
        
        rows.forEach(row => {
            const code = row.querySelector('.discount-code').textContent.toLowerCase();
            const name = row.querySelector('.discount-name').textContent.toLowerCase();
            if (code.includes(searchTerm) || name.includes(searchTerm)) {
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

