<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Quản lý slider"/>
    <jsp:param name="pageSubtitle" value="Danh sách và quản lý các slider"/>
    <jsp:param name="currentPage" value="sliders"/>
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
            
            .slider-image {
                width: 120px;
                height: 60px;
                object-fit: cover;
                border-radius: 8px;
                border: 2px solid #e9ecef;
                opacity: 0;
                transition: opacity 0.3s ease;
                background: #f0f0f0;
            }
            
            .slider-image.loaded {
                opacity: 1;
            }
            
            .slider-image.error {
                opacity: 1;
                background: linear-gradient(135deg, #f8f9fa, #e9ecef);
                display: flex;
                align-items: center;
                justify-content: center;
                color: #666;
                font-size: 8px;
                text-align: center;
            }
            
            .slider-title {
                font-weight: 600;
                color: #333;
                margin-bottom: 4px;
            }
            
            .slider-description {
                color: #6c757d;
                font-size: 12px;
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
            <input type="text" placeholder="Tìm kiếm slider..." id="searchInput">
        </div>
        <select class="filter-select" id="statusFilter">
            <option value="">Tất cả trạng thái</option>
            <option value="Hiển thị">Hiển thị</option>
            <option value="Ẩn">Ẩn</option>
        </select>
    </div>

    <!-- Table Header -->
    <div class="table-header">
        <h2 class="table-title">Danh sách slider</h2>
        <a href="admin-sliders?action=create" class="btn-add">
            <i class="fas fa-plus"></i>
            Thêm slider
        </a>
    </div>

    <!-- Data Table -->
    <c:choose>
        <c:when test="${empty sliders}">
            <div class="empty-state">
                <i class="fas fa-images"></i>
                <h3>Chưa có slider nào</h3>
                <p>Hãy thêm slider đầu tiên để bắt đầu quản lý</p>
                <a href="admin-sliders?action=create" class="btn-add">
                    <i class="fas fa-plus"></i>
                    Thêm slider đầu tiên
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Hình ảnh</th>
                        <th>Tiêu đề</th>
                        <th>Mô tả</th>
                        <th>Thứ tự</th>
                        <th>Ngày bắt đầu</th>
                        <th>Ngày kết thúc</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="slider" items="${sliders}">
                        <tr>
                            <td>
                                <img src="${pageContext.request.contextPath}/assets/image/${slider.anhSlide}" 
                                     alt="${slider.tieuDe}" 
                                     class="slider-image"
                                     data-fallback="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTIwIiBoZWlnaHQ9IjYwIiB2aWV3Qm94PSIwIDAgMTIwIDYwIiBmaWxsPSJub25lIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciPgo8cmVjdCB3aWR0aD0iMTIwIiBoZWlnaHQ9IjYwIiBmaWxsPSIjRjhGOUZBIi8+CjxwYXRoIGQ9Ik02MCAyMEM2MCAxNy43OTEgNjEuNzkxIDE2IDY0IDE2SDcyQzc0LjIwOSAxNiA3NiAxNy43OTEgNzYgMjBWNDBDNzYgNDIuMjA5IDc0LjIwOSA0NCA3MiA0NEg2NEM2MS43OTEgNDQgNjAgNDIuMjA5IDYwIDQwVjIwWiIgZmlsbD0iI0Q5RDlEOSIvPgo8c3ZnIHg9IjUwIiB5PSIyMCIgd2lkdGg9IjIwIiBoZWlnaHQ9IjIwIiB2aWV3Qm94PSIwIDAgMjQgMjQiIGZpbGw9Im5vbmUiPgo8cGF0aCBkPSJNMTIgMkwxMy4wOSA4LjI2TDIwIDlMMTMuMDkgMTUuNzRMMTIgMjJMMTAuOTEgMTUuNzRMNCA5TDEwLjkxIDguMjZMMTIgMloiIGZpbGw9IiM5OTkiLz4KPC9zdmc+Cjwvc3ZnPgo=">
                            </td>
                            <td>
                                <div class="slider-title">${slider.tieuDe}</div>
                            </td>
                            <td>
                                <div class="slider-description">${slider.moTa != null ? slider.moTa : '-'}</div>
                            </td>
                            <td>
                                <div class="slider-title">${slider.thuTuHienThi}</div>
                            </td>
                            <td>
                                ${ngayBatDauFormatted[slider.maSlider]}
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${ngayKetThucFormatted[slider.maSlider] != null}">
                                        ${ngayKetThucFormatted[slider.maSlider]}
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: #6c757d;">Không giới hạn</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${slider.trangThai == 'Hiển thị'}">
                                        <span class="status-badge status-active">${slider.trangThai}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge status-inactive">${slider.trangThai}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <a href="admin-sliders?action=edit&maSlider=${slider.maSlider}" class="btn-action btn-edit">
                                        <i class="fas fa-edit"></i> Sửa
                                    </a>
                                    <a href="admin-sliders?action=delete&maSlider=${slider.maSlider}" 
                                       class="btn-action btn-delete"
                                       onclick="return confirmDelete('Bạn có chắc chắn muốn vô hiệu hóa slider này?')">
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
            const title = row.querySelector('.slider-title').textContent.toLowerCase();
            const description = row.querySelector('.slider-description').textContent.toLowerCase();
            if (title.includes(searchTerm) || description.includes(searchTerm)) {
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

    // Image loading
    document.querySelectorAll('.slider-image').forEach(img => {
        img.onload = function() {
            this.classList.add('loaded');
        };
        img.onerror = function() {
            this.classList.add('error');
            this.src = this.getAttribute('data-fallback');
        };
        if (img.complete) {
            img.onload();
        }
    });
</script>

<jsp:include page="../layout/admin-footer.jsp" />

