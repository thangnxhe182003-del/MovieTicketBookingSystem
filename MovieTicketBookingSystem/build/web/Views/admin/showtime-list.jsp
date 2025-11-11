<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.time.LocalDateTime" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Quản lý suất chiếu"/>
    <jsp:param name="pageSubtitle" value="Danh sách và quản lý các suất chiếu phim"/>
    <jsp:param name="currentPage" value="showtimes"/>
    <jsp:param name="extraStyles" value="
        <style>
            .container { padding: 0; max-width: 100%; }
            
            /* Filter Bar */
            .filter-section { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 20px 30px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
            .filter-container { display: flex; align-items: flex-end; gap: 15px; flex-wrap: nowrap; }
            .filter-group { display: flex; flex-direction: column; gap: 6px; flex: 1; min-width: 0; }
            .filter-group label { font-size: 13px; font-weight: 600; color: #fff; text-transform: uppercase; letter-spacing: 0.5px; white-space: nowrap; }
            .filter-group select, .filter-group input { padding: 10px 14px; border: 2px solid rgba(255,255,255,0.3); border-radius: 8px; font-size: 14px; background: rgba(255,255,255,0.95); transition: all 0.3s; width: 100%; }
            .filter-group select:focus, .filter-group input:focus { border-color: #fff; outline: none; box-shadow: 0 0 0 3px rgba(255,255,255,0.2); background: #fff; }
            .filter-actions { display: flex; gap: 10px; flex-shrink: 0; }
            .btn-filter { background: #fff; color: #667eea; padding: 10px 24px; border: none; border-radius: 8px; font-weight: 600; cursor: pointer; transition: all 0.3s; white-space: nowrap; }
            .btn-filter:hover { background: #f0f0f0; transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.15); }
            .btn-reset { background: rgba(255,255,255,0.2); color: #fff; padding: 10px 20px; border: 2px solid #fff; border-radius: 8px; font-weight: 600; cursor: pointer; transition: all 0.3s; white-space: nowrap; }
            .btn-reset:hover { background: rgba(255,255,255,0.3); }
            
            /* Stats Bar */
            .stats-bar { background: #f8f9fa; padding: 20px 30px; border-bottom: 1px solid #e9ecef; display: flex; justify-content: space-between; align-items: center; }
            .stats-info { display: flex; gap: 30px; }
            .stat-item { display: flex; align-items: center; gap: 10px; }
            .stat-icon { width: 40px; height: 40px; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 18px; }
            .stat-icon.primary { background: linear-gradient(135deg, #667eea, #764ba2); color: #fff; }
            .stat-icon.success { background: linear-gradient(135deg, #11998e, #38ef7d); color: #fff; }
            .stat-icon.warning { background: linear-gradient(135deg, #f093fb, #f5576c); color: #fff; }
            .stat-content h4 { margin: 0; font-size: 24px; font-weight: 700; color: #333; }
            .stat-content p { margin: 0; font-size: 12px; color: #666; text-transform: uppercase; letter-spacing: 0.5px; }
            .btn-add { background: linear-gradient(135deg, #e50914, #c90812); color: white; padding: 12px 24px; border: none; border-radius: 8px; text-decoration: none; font-weight: 600; display: inline-flex; align-items: center; gap: 8px; transition: all 0.3s; box-shadow: 0 4px 12px rgba(229, 9, 20, 0.3); }
            .btn-add:hover { transform: translateY(-2px); box-shadow: 0 6px 20px rgba(229, 9, 20, 0.4); }
            
            /* Cinema Sections */
            .cinema-section { margin-bottom: 40px; }
            .cinema-header { background: linear-gradient(135deg, #2c3e50, #34495e); color: #fff; padding: 20px 30px; border-radius: 12px 12px 0 0; display: flex; align-items: center; gap: 15px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
            .cinema-icon { width: 50px; height: 50px; background: rgba(255,255,255,0.2); border-radius: 10px; display: flex; align-items: center; justify-content: center; font-size: 24px; }
            .cinema-info h2 { margin: 0 0 5px 0; font-size: 24px; font-weight: 700; }
            .cinema-info p { margin: 0; opacity: 0.9; font-size: 14px; }
            
            /* Date Sections */
            .date-section { margin-bottom: 25px; }
            .date-header { background: linear-gradient(to right, #e3f2fd, #bbdefb); padding: 12px 30px; display: flex; align-items: center; gap: 12px; border-left: 5px solid #2196f3; }
            .date-icon { width: 40px; height: 40px; background: #2196f3; color: #fff; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 18px; }
            .date-header h3 { margin: 0; font-size: 18px; font-weight: 600; color: #1976d2; }
            
            /* Room Sections */
            .room-section { background: #fff; margin-bottom: 15px; border-radius: 0 0 8px 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.08); }
            .room-header { background: #f8f9fa; padding: 15px 30px; border-bottom: 2px solid #e9ecef; display: flex; justify-content: space-between; align-items: center; }
            .room-title { display: flex; align-items: center; gap: 12px; }
            .room-icon { width: 35px; height: 35px; background: linear-gradient(135deg, #ffa726, #fb8c00); color: #fff; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-size: 16px; }
            .room-title h4 { margin: 0; font-size: 16px; font-weight: 600; color: #333; }
            .room-stats { display: flex; gap: 20px; font-size: 13px; color: #666; }
            .room-stats span { display: flex; align-items: center; gap: 5px; }
            
            /* Showtime Grid */
            .showtime-grid { padding: 20px 30px; display: grid; gap: 15px; }
            .showtime-card { display: grid; grid-template-columns: 100px 200px 1fr auto auto; gap: 20px; align-items: center; padding: 20px; background: #fff; border: 2px solid #f0f0f0; border-radius: 10px; transition: all 0.3s; }
            .showtime-card:hover { border-color: #e50914; box-shadow: 0 4px 12px rgba(0,0,0,0.1); transform: translateY(-2px); }
            
            /* Time Badge */
            .time-badge { text-align: center; }
            .time-start { font-size: 24px; font-weight: 700; color: #e50914; line-height: 1; }
            .time-end { font-size: 14px; color: #666; margin-top: 5px; }
            .time-duration { font-size: 11px; color: #999; margin-top: 3px; }
            
            /* Movie Info */
            .movie-info { display: flex; gap: 15px; align-items: center; }
            .movie-poster { width: 70px; height: 100px; object-fit: cover; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.2); }
            .movie-details h5 { margin: 0 0 8px 0; font-size: 16px; font-weight: 600; color: #333; }
            .movie-meta { display: flex; gap: 10px; flex-wrap: wrap; }
            .meta-badge { padding: 4px 10px; background: #f0f0f0; border-radius: 12px; font-size: 12px; font-weight: 500; color: #666; display: inline-flex; align-items: center; gap: 5px; }
            .meta-badge.language { background: #e3f2fd; color: #1976d2; }
            .meta-badge.duration { background: #fff3e0; color: #f57c00; }
            
            /* Price Section */
            .price-section { text-align: center; }
            .price-label { font-size: 12px; color: #666; margin-bottom: 5px; }
            .price-value { font-size: 20px; font-weight: 700; color: #e50914; }
            .price-breakdown { font-size: 11px; color: #999; margin-top: 5px; }
            
            /* Status Badge */
            .status-badge { padding: 8px 16px; border-radius: 20px; font-size: 12px; font-weight: 600; text-transform: uppercase; display: inline-flex; align-items: center; gap: 6px; }
            .status-badge.active { background: #d4edda; color: #155724; }
            .status-badge.upcoming { background: #fff3cd; color: #856404; }
            .status-badge.past { background: #f8d7da; color: #721c24; }
            
            /* Action Buttons */
            .action-buttons { display: flex; gap: 8px; }
            .btn-action { width: 36px; height: 36px; border: none; border-radius: 8px; display: flex; align-items: center; justify-content: center; cursor: pointer; transition: all 0.3s; font-size: 14px; text-decoration: none; }
            .btn-edit { background: #17a2b8; color: white; }
            .btn-edit:hover { background: #138496; transform: scale(1.1); }
            .btn-delete { background: #dc3545; color: white; }
            .btn-delete:hover { background: #c82333; transform: scale(1.1); }
            .btn-view { background: #6c757d; color: white; }
            .btn-view:hover { background: #5a6268; transform: scale(1.1); }
            
            /* Empty State */
            .empty-state { text-align: center; padding: 80px 20px; }
            .empty-icon { font-size: 64px; color: #dee2e6; margin-bottom: 20px; }
            .empty-state h3 { color: #495057; margin-bottom: 10px; font-size: 24px; }
            .empty-state p { color: #6c757d; margin-bottom: 30px; font-size: 16px; }
            
            /* Messages */
            .alert { padding: 15px 20px; margin: 20px 30px; border-radius: 8px; display: flex; align-items: center; gap: 12px; font-weight: 500; animation: slideIn 0.3s ease; }
            .alert-success { background: #d4edda; color: #155724; border-left: 4px solid #28a745; }
            .alert-error { background: #f8d7da; color: #721c24; border-left: 4px solid #dc3545; }
            @keyframes slideIn { from { opacity: 0; transform: translateY(-10px); } to { opacity: 1; transform: translateY(0); } }
            
            /* Responsive */
            @media (max-width: 1200px) {
                .showtime-card { grid-template-columns: 80px 180px 1fr auto; }
            }
            @media (max-width: 992px) {
                .showtime-card { grid-template-columns: 1fr; text-align: center; }
                .movie-info { flex-direction: column; }
                .filter-container { flex-wrap: wrap; }
                .filter-group { min-width: calc(50% - 8px); }
                .filter-actions { width: 100%; justify-content: flex-end; }
            }
            @media (max-width: 768px) {
                .filter-container { flex-direction: column; align-items: stretch; }
                .filter-group { min-width: 100%; }
                .filter-actions { width: 100%; }
            }
        </style>
    "/>
</jsp:include>

<div class="container">
    <!-- Filter Section -->
    <div class="filter-section">
        <form action="admin-showtimes" method="GET" class="filter-container">
            <div class="filter-group">
                <label><i class="fas fa-building"></i> Rạp chiếu</label>
                <select name="cinema" id="cinemaFilter">
                    <option value="">Tất cả rạp</option>
                    <c:forEach var="cinema" items="${cinemaNames}">
                        <option value="${cinema}" ${cinema == selectedCinema ? 'selected' : ''}>${cinema}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="filter-group">
                <label><i class="fas fa-door-open"></i> Phòng chiếu</label>
                <select name="room" id="roomFilter">
                    <option value="">Tất cả phòng</option>
                    <c:forEach var="room" items="${allRooms}">
                        <option value="${room.tenPhong}" ${room.tenPhong == selectedRoom ? 'selected' : ''}>${room.tenPhong}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="filter-group">
                <label><i class="fas fa-calendar-alt"></i> Ngày chiếu</label>
                <input type="date" name="date" id="dateFilter" value="${selectedDate}" />
            </div>
            <div class="filter-actions">
                <button type="submit" class="btn-filter">
                    <i class="fas fa-search"></i> Lọc
                </button>
                <button type="button" class="btn-reset" onclick="window.location.href='admin-showtimes'">
                    <i class="fas fa-redo"></i> Reset
                </button>
            </div>
        </form>
    </div>

    <!-- Stats Bar -->
    <div class="stats-bar">
        <div class="stats-info">
            <div class="stat-item">
                <div class="stat-icon primary">
                    <i class="fas fa-film"></i>
                </div>
                <div class="stat-content">
                    <h4>${totalShowtimes}</h4>
                    <p>Tổng suất chiếu</p>
                </div>
            </div>
            <div class="stat-item">
                <div class="stat-icon success">
                    <i class="fas fa-building"></i>
                </div>
                <div class="stat-content">
                    <h4>${organizedShowtimes.size()}</h4>
                    <p>Rạp chiếu</p>
                </div>
            </div>
        </div>
        <a href="admin-showtimes?action=create" class="btn-add">
            <i class="fas fa-plus-circle"></i>
            Thêm suất chiếu
        </a>
    </div>

    <!-- Success/Error Messages -->
    <c:if test="${not empty requestScope.success}">
        <div class="alert alert-success">
            <i class="fas fa-check-circle"></i>
            ${requestScope.success}
        </div>
    </c:if>
    
    <c:if test="${not empty requestScope.error}">
        <div class="alert alert-error">
            <i class="fas fa-exclamation-circle"></i>
            ${requestScope.error}
        </div>
    </c:if>

    <!-- Content -->
    <div style="padding: 30px;">
        <c:choose>
            <c:when test="${empty organizedShowtimes}">
                <div class="empty-state">
                    <div class="empty-icon">
                        <i class="fas fa-calendar-times"></i>
                    </div>
                    <h3>Không tìm thấy suất chiếu nào</h3>
                    <p>Hãy thử điều chỉnh bộ lọc hoặc tạo suất chiếu mới</p>
                    <a href="admin-showtimes?action=create" class="btn-add">
                        <i class="fas fa-plus-circle"></i>
                        Tạo suất chiếu đầu tiên
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <!-- Loop through cinemas -->
                <c:forEach var="cinemaEntry" items="${organizedShowtimes}">
                    <div class="cinema-section">
                        <div class="cinema-header">
                            <div class="cinema-icon">
                                <i class="fas fa-film"></i>
                            </div>
                            <div class="cinema-info">
                                <h2>${cinemaEntry.key}</h2>
                                <p>
                                    <c:set var="cinemaShowtimeCount" value="0" />
                                    <c:forEach var="dateEntry" items="${cinemaEntry.value}">
                                        <c:forEach var="roomEntry" items="${dateEntry.value}">
                                            <c:set var="cinemaShowtimeCount" value="${cinemaShowtimeCount + roomEntry.value.size()}" />
                                        </c:forEach>
                                    </c:forEach>
                                    ${cinemaShowtimeCount} suất chiếu
                                </p>
                            </div>
                        </div>
                        
                        <!-- Loop through dates -->
                        <c:forEach var="dateEntry" items="${cinemaEntry.value}">
                            <div class="date-section">
                                <div class="date-header">
                                    <div class="date-icon">
                                        <i class="fas fa-calendar-day"></i>
                                    </div>
                                    <h3>${dateEntry.key}</h3>
                                </div>
                                
                                <!-- Loop through rooms -->
                                <c:forEach var="roomEntry" items="${dateEntry.value}">
                                    <div class="room-section">
                                        <div class="room-header">
                                            <div class="room-title">
                                                <div class="room-icon">
                                                    <i class="fas fa-door-open"></i>
                                                </div>
                                                <h4>${roomEntry.key}</h4>
                                            </div>
                                            <div class="room-stats">
                                                <span><i class="fas fa-film"></i> ${roomEntry.value.size()} suất</span>
                                            </div>
                                        </div>
                                        
                                        <!-- Showtime Grid -->
                                        <div class="showtime-grid">
                                            <c:forEach var="showtime" items="${roomEntry.value}">
                                                <div class="showtime-card">
                                                    <!-- Time -->
                                                    <div class="time-badge">
                                                        <div class="time-start">${showtime.gioBatDau.format(timeFormatter)}</div>
                                                        <div class="time-end">đến ${showtime.gioKetThuc.format(timeFormatter)}</div>
                                                    </div>
                                                    
                                                    <!-- Movie Info -->
                                                    <div class="movie-info">
                                                        <img src="${pageContext.request.contextPath}/assets/image/${showtime.poster}" 
                                                             alt="${showtime.tenPhim}" 
                                                             class="movie-poster"
                                                             onerror="this.src='https://via.placeholder.com/70x100?text=No+Image'">
                                                        <div class="movie-details">
                                                            <h5>${showtime.tenPhim != null ? showtime.tenPhim : 'Chưa có phim'}</h5>
                                                            <div class="movie-meta">
                                                                <span class="meta-badge language">
                                                                    <i class="fas fa-language"></i>
                                                                    ${showtime.ngonNguAmThanh}
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    
                                                    <!-- Spacer -->
                                                    <div></div>
                                                    
                                                    <!-- Price -->
                                                    <div class="price-section">
                                                        <div class="price-label">Giá vé cơ sở</div>
                                                        <div class="price-value">
                                                            <fmt:formatNumber value="${showtime.giaVeCoSo}" pattern="#,###"/>đ
                                                        </div>
                                                    </div>
                                                    
                                                    <!-- Actions -->
                                                    <div class="action-buttons">
                                                        <%
                                                            // Kiểm tra nếu suất chiếu đã qua
                                                            model.Showtime st = (model.Showtime) pageContext.getAttribute("showtime");
                                                            java.time.LocalDateTime currentTime = (java.time.LocalDateTime) pageContext.getAttribute("currentTime");
                                                            boolean isPast = false;
                                                            if (st != null && st.getGioBatDau() != null && currentTime != null) {
                                                                isPast = st.getGioBatDau().isBefore(currentTime);
                                                            }
                                                            pageContext.setAttribute("isPastShowtime", isPast);
                                                        %>
                                                        <c:choose>
                                                            <c:when test="${isPastShowtime}">
                                                                <!-- Suất chiếu đã qua - chỉ cho xem -->
                                                                <span class="btn-action btn-view" 
                                                                      style="background: #6c757d; color: white; cursor: default; opacity: 0.7;" 
                                                                      title="Suất chiếu đã qua - chỉ xem">
                                                                    <i class="fas fa-eye"></i>
                                                                </span>
                                                                <span style="font-size: 10px; color: #999; display: block; margin-top: 4px; text-align: center;">Đã qua</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <!-- Suất chiếu chưa qua - cho sửa/xóa -->
                                                                <a href="admin-showtimes?action=edit&maSuatChieu=${showtime.maSuatChieu}" 
                                                                   class="btn-action btn-edit" 
                                                                   title="Chỉnh sửa">
                                                                    <i class="fas fa-edit"></i>
                                                                </a>
                                                                <a href="admin-showtimes?action=delete&maSuatChieu=${showtime.maSuatChieu}" 
                                                                   class="btn-action btn-delete" 
                                                                   onclick="return confirm('Bạn có chắc chắn muốn xóa suất chiếu này?')" 
                                                                   title="Xóa">
                                                                    <i class="fas fa-trash"></i>
                                                                </a>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:forEach>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    // Auto-hide messages after 5 seconds
    document.addEventListener('DOMContentLoaded', function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(alert => {
            setTimeout(() => {
                alert.style.transition = 'opacity 0.5s, transform 0.5s';
                alert.style.opacity = '0';
                alert.style.transform = 'translateY(-10px)';
                setTimeout(() => alert.remove(), 500);
            }, 5000);
        });
    });

    // Dynamic room filter based on cinema selection
    document.getElementById('cinemaFilter').addEventListener('change', function() {
        const selectedCinema = this.value;
        const roomFilter = document.getElementById('roomFilter');
        const allRooms = JSON.parse('${allRoomsJson}');
        
        // Clear current options except "Tất cả phòng"
        roomFilter.innerHTML = '<option value="">Tất cả phòng</option>';
        
        // Add filtered rooms
        if (selectedCinema) {
            allRooms.forEach(room => {
                if (room.tenRap === selectedCinema) {
                    const option = document.createElement('option');
                    option.value = room.tenPhong;
                    option.textContent = room.tenPhong;
                    roomFilter.appendChild(option);
                }
            });
        } else {
            allRooms.forEach(room => {
                const option = document.createElement('option');
                option.value = room.tenPhong;
                option.textContent = room.tenPhong;
                roomFilter.appendChild(option);
            });
        }
    });
</script>

<jsp:include page="../layout/admin-footer.jsp" />
