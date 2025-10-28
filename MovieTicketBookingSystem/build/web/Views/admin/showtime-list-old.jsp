<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Quản lý suất chiếu"/>
    <jsp:param name="pageSubtitle" value="Danh sách và quản lý các suất chiếu phim"/>
    <jsp:param name="currentPage" value="showtimes"/>
    <jsp:param name="extraStyles" value="
        <style>
            .table-container { padding: 0; }
            .table-header { background: #f8f9fa; padding: 20px 30px; border-bottom: 1px solid #e9ecef; display: flex; justify-content: space-between; align-items: center; }
            .table-title { font-size: 18px; font-weight: 600; color: #333; margin: 0; }
            .btn-add { background: linear-gradient(135deg, #e50914, #c90812); color: white; padding: 10px 20px; border: none; border-radius: 6px; text-decoration: none; font-weight: 600; display: inline-flex; align-items: center; gap: 8px; transition: all 0.3s; }
            .btn-add:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(229, 9, 20, 0.4); }
            .data-table { width: 100%; border-collapse: collapse; }
            .data-table th { background: #f8f9fa; padding: 15px 20px; text-align: left; font-weight: 600; color: #333; border-bottom: 2px solid #e9ecef; }
            .data-table td { padding: 15px 20px; border-bottom: 1px solid #e9ecef; vertical-align: middle; }
            .data-table tr:hover { background: #f8f9fa; }
            .movie-info { display: flex; align-items: center; gap: 12px; }
            .movie-poster { width: 50px; height: 70px; object-fit: cover; border-radius: 6px; border: 2px solid #e9ecef; }
            .movie-details h4 { margin: 0 0 4px 0; font-size: 16px; font-weight: 600; color: #333; }
            .movie-details p { margin: 0; font-size: 14px; color: #666; }
            .showtime-info { text-align: center; }
            .showtime-date { font-weight: 600; color: #e50914; font-size: 16px; margin-bottom: 4px; }
            .showtime-time { font-size: 14px; color: #333; }
            .room-info { text-align: center; }
            .room-name { font-weight: 600; color: #333; margin-bottom: 2px; }
            .cinema-name { font-size: 12px; color: #666; }
            .price { font-weight: 700; color: #e50914; font-size: 16px; text-align: right; }
            .language { background: #e3f2fd; color: #1976d2; padding: 4px 8px; border-radius: 12px; font-size: 12px; font-weight: 600; }
            .action-buttons { display: flex; gap: 8px; }
            .btn-action { padding: 6px 12px; border: none; border-radius: 4px; text-decoration: none; font-size: 12px; font-weight: 600; cursor: pointer; transition: all 0.3s; }
            .btn-edit { background: #17a2b8; color: white; }
            .btn-edit:hover { background: #138496; }
            .btn-delete { background: #dc3545; color: white; }
            .btn-delete:hover { background: #c82333; }
            .btn-assign { background: #28a745; color: white; }
            .btn-assign:hover { background: #218838; }
            .empty-state { text-align: center; padding: 60px 20px; color: #6c757d; }
            .empty-state i { font-size: 48px; margin-bottom: 20px; color: #dee2e6; }
            .empty-state h3 { margin-bottom: 10px; color: #495057; }
            .empty-state p { margin-bottom: 20px; }
            .filter-bar { background: #f8f9fa; padding: 20px 30px; border-bottom: 1px solid #e9ecef; display: flex; gap: 20px; align-items: center; }
            .filter-group { display: flex; flex-direction: column; gap: 5px; }
            .filter-group label { font-size: 12px; font-weight: 600; color: #666; }
            .filter-group select, .filter-group input { padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px; }
            .btn-filter { background: #6c757d; color: white; padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; }
            .btn-filter:hover { background: #5a6268; }
            .status-badge { padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; text-transform: uppercase; }
            .status-assigned { background: #d4edda; color: #155724; }
            .status-unassigned { background: #fff3cd; color: #856404; }
        </style>
    "/>
</jsp:include>

<div class="table-container">
    <!-- Filter Bar -->
    <div class="filter-bar">
        <div class="filter-group">
            <label>Rạp</label>
            <select id="cinemaFilter">
                <option value="">Tất cả rạp</option>
                <option value="CGV">CGV</option>
                <option value="Lotte">Lotte</option>
                <option value="Galaxy">Galaxy</option>
            </select>
        </div>
        <div class="filter-group">
            <label>Phòng</label>
            <select id="roomFilter">
                <option value="">Tất cả phòng</option>
            </select>
        </div>
        <div class="filter-group">
            <label>Ngày</label>
            <input type="date" id="dateFilter" />
        </div>
        <div class="filter-group">
            <label>Trạng thái</label>
            <select id="statusFilter">
                <option value="">Tất cả</option>
                <option value="assigned">Đã gán phim</option>
                <option value="unassigned">Chưa gán phim</option>
            </select>
        </div>
        <button class="btn-filter" onclick="applyFilters()">Lọc</button>
    </div>

    <!-- Success/Error Messages -->
    <c:if test="${not empty requestScope.success}">
        <div style="background: #d4edda; color: #155724; padding: 12px 20px; margin: 20px 30px; border-radius: 6px; border: 1px solid #c3e6cb; display: flex; align-items: center; gap: 10px;">
            <i class="fas fa-check-circle"></i>
            ${requestScope.success}
        </div>
    </c:if>
    
    <c:if test="${not empty requestScope.error}">
        <div style="background: #f8d7da; color: #721c24; padding: 12px 20px; margin: 20px 30px; border-radius: 6px; border: 1px solid #f5c6cb; display: flex; align-items: center; gap: 10px;">
            <i class="fas fa-exclamation-circle"></i>
            ${requestScope.error}
        </div>
    </c:if>

    <!-- Table Header -->
    <div class="table-header">
        <h2 class="table-title">Danh sách suất chiếu (7 ngày tới)</h2>
        <div style="display: flex; gap: 10px;">
            <a href="admin-showtimes?action=create" class="btn-add">
                <i class="fas fa-plus"></i>
                Thêm suất chiếu
            </a>
        </div>
    </div>

    <!-- Data Table -->
    <c:choose>
        <c:when test="${empty showtimesByDate}">
            <div class="empty-state">
                <i class="fas fa-calendar-times"></i>
                <h3>Chưa có suất chiếu nào</h3>
                <p>Hãy tạo suất chiếu đầu tiên để bắt đầu quản lý</p>
                <a href="admin-showtimes?action=create" class="btn-add">
                    <i class="fas fa-plus"></i>
                    Tạo suất chiếu đầu tiên
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <c:forEach var="dateEntry" items="${showtimesByDate}">
                <div style="margin-bottom: 30px;">
                    <h3 style="color: #333; margin-bottom: 15px; padding: 10px; background: #f8f9fa; border-radius: 6px;">
                        ${dateEntry.key}
                    </h3>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Phim</th>
                                <th>Giờ chiếu</th>
                                <th>Phòng</th>
                                <th>Giá vé</th>
                                <th>Ngôn ngữ</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="showtime" items="${dateEntry.value}">
                                <tr>
                                    <td>
                                        <div class="movie-info">
                                            <img src="${pageContext.request.contextPath}/assets/image/${showtime.poster}" 
                                                 alt="${showtime.tenPhim}" 
                                                 class="movie-poster"
                                                 onerror="this.src='https://via.placeholder.com/50x70?text=No+Image'">
                                            <div class="movie-details">
                                                <h4>${showtime.tenPhim != null ? showtime.tenPhim : 'Phim mặc định'}</h4>
                                                <p>${showtime.tenRap}</p>
                                            </div>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="showtime-info">
                                            <div class="showtime-time">
                                                ${showtime.gioBatDau.format(timeFormatter)} - ${showtime.gioKetThuc.format(timeFormatter)}
                                            </div>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="room-info">
                                            <div class="room-name">${showtime.tenPhong}</div>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="price">
                                            <fmt:formatNumber value="${showtime.giaVeCoSo}" type="currency" currencySymbol="₫"/>
                                        </div>
                                    </td>
                                    <td>
                                        <span class="language">${showtime.ngonNguAmThanh}</span>
                                    </td>
                                    <td>
                                        <span class="status-badge status-assigned">
                                            Đã gán phim
                                        </span>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="admin-showtimes?action=assign&maSuatChieu=${showtime.maSuatChieu}" 
                                               class="btn-action btn-assign" title="Đổi phim">
                                                <i class="fas fa-film"></i>
                                            </a>
                                            <a href="admin-showtimes?action=edit&maSuatChieu=${showtime.maSuatChieu}" 
                                               class="btn-action btn-edit" title="Sửa">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <a href="admin-showtimes?action=delete&maSuatChieu=${showtime.maSuatChieu}" 
                                               class="btn-action btn-delete" 
                                               onclick="return confirmDelete('Bạn có chắc chắn muốn xóa suất chiếu này?')" title="Xóa">
                                                <i class="fas fa-trash"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>

<script>
    function applyFilters() {
        const cinemaFilter = document.getElementById('cinemaFilter').value;
        const roomFilter = document.getElementById('roomFilter').value;
        const dateFilter = document.getElementById('dateFilter').value;
        const statusFilter = document.getElementById('statusFilter').value;
        
        const rows = document.querySelectorAll('.data-table tbody tr');
        
        rows.forEach(row => {
            let show = true;
            
            // Filter by cinema
            if (cinemaFilter && !row.querySelector('.movie-details p').textContent.includes(cinemaFilter)) {
                show = false;
            }
            
            // Filter by room
            if (roomFilter && !row.querySelector('.room-name').textContent.includes(roomFilter)) {
                show = false;
            }
            
            // Filter by date
            if (dateFilter) {
                const showDate = row.querySelector('.showtime-date').textContent;
                const filterDate = new Date(dateFilter).toLocaleDateString('vi-VN');
                if (showDate !== filterDate) {
                    show = false;
                }
            }
            
            // Filter by status
            if (statusFilter) {
                const statusBadge = row.querySelector('.status-badge');
                const isAssigned = statusBadge.classList.contains('status-assigned');
                if ((statusFilter === 'assigned' && !isAssigned) || 
                    (statusFilter === 'unassigned' && isAssigned)) {
                    show = false;
                }
            }
            
            row.style.display = show ? '' : 'none';
        });
    }

    function confirmDelete(message) {
        return confirm(message);
    }
    
    // Auto-hide success/error messages after 5 seconds
    document.addEventListener('DOMContentLoaded', function() {
        const successMessage = document.querySelector('[style*="background: #d4edda"]');
        const errorMessage = document.querySelector('[style*="background: #f8d7da"]');
        
        if (successMessage) {
            setTimeout(function() {
                successMessage.style.transition = 'opacity 0.5s';
                successMessage.style.opacity = '0';
                setTimeout(function() {
                    successMessage.remove();
                }, 500);
            }, 5000);
        }
        
        if (errorMessage) {
            setTimeout(function() {
                errorMessage.style.transition = 'opacity 0.5s';
                errorMessage.style.opacity = '0';
                setTimeout(function() {
                    errorMessage.remove();
                }, 500);
            }, 5000);
        }
    });
</script>

<jsp:include page="../layout/admin-footer.jsp" />
