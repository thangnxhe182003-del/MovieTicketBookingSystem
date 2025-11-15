<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Quản lý phim"/>
    <jsp:param name="pageSubtitle" value="Danh sách tất cả phim trong hệ thống"/>
    <jsp:param name="currentPage" value="movies"/>
    <jsp:param name="extraStyles" value="
        <style>
            .content-container {
                padding: 30px;
            }
            
            .page-header {
                background: #f8f9fa;
                padding: 20px 30px;
                border-bottom: 1px solid #e9ecef;
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 30px;
            }
            
            .page-title {
                font-size: 24px;
                font-weight: 700;
                color: #333;
                margin: 0;
            }
            
            .page-subtitle {
                color: #6c757d;
                margin: 5px 0 0 0;
                font-size: 14px;
            }
            
            .btn {
                padding: 12px 24px;
                border: none;
                border-radius: 6px;
                font-weight: 600;
                cursor: pointer;
                text-decoration: none;
                display: inline-flex;
                align-items: center;
                gap: 8px;
                transition: all 0.3s;
            }
            
            .btn-primary {
                background: linear-gradient(135deg, #e50914, #c90812);
                color: white;
            }
            
            .btn-primary:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(229, 9, 20, 0.4);
            }
            
            .btn-secondary {
                background: #6c757d;
                color: white;
            }
            
            .btn-secondary:hover {
                background: #5a6268;
            }
            
            .btn-sm {
                padding: 8px 16px;
                font-size: 12px;
            }
            
            .btn-danger {
                background: #dc3545;
                color: white;
            }
            
            .btn-danger:hover {
                background: #c82333;
            }
            
            .btn-warning {
                background: #ffc107;
                color: #212529;
            }
            
            .btn-warning:hover {
                background: #e0a800;
            }
            
            .filters {
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                margin-bottom: 20px;
                display: flex;
                gap: 20px;
                align-items: center;
                flex-wrap: wrap;
            }
            
            .filter-group {
                display: flex;
                flex-direction: column;
                gap: 5px;
            }
            
            .filter-group label {
                font-size: 12px;
                font-weight: 600;
                color: #333;
            }
            
            .filter-group input,
            .filter-group select {
                padding: 8px 12px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 14px;
            }
            
            .data-table {
                background: white;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                overflow: hidden;
            }
            
            .table {
                width: 100%;
                border-collapse: collapse;
                margin: 0;
            }
            
            .table th {
                background: #f8f9fa;
                padding: 15px 12px;
                text-align: left;
                font-weight: 600;
                color: #333;
                border-bottom: 2px solid #e9ecef;
                font-size: 14px;
            }
            
            .table td {
                padding: 15px 12px;
                border-bottom: 1px solid #e9ecef;
                vertical-align: middle;
                font-size: 14px;
            }
            
            .table tbody tr:hover {
                background-color: #f8f9fa;
            }
            
            .movie-poster {
                width: 60px;
                height: 80px;
                object-fit: cover;
                border-radius: 4px;
                border: 1px solid #e9ecef;
                opacity: 0;
                transition: opacity 0.3s ease;
                background: #f0f0f0;
            }
            
            .movie-poster.loaded {
                opacity: 1;
            }
            
            .movie-poster.error {
                opacity: 1;
                background: linear-gradient(135deg, #f8f9fa, #e9ecef);
                display: flex;
                align-items: center;
                justify-content: center;
                color: #666;
                font-size: 10px;
                text-align: center;
            }
            
            .movie-info {
                display: flex;
                flex-direction: column;
                gap: 4px;
            }
            
            .movie-title {
                font-weight: 600;
                color: #333;
                font-size: 14px;
            }
            
            .movie-genre {
                color: #6c757d;
                font-size: 12px;
            }
            
            .movie-duration {
                color: #6c757d;
                font-size: 12px;
            }
            
            .status-badge {
                padding: 4px 8px;
                border-radius: 12px;
                font-size: 11px;
                font-weight: 600;
                text-transform: uppercase;
            }
            
            .status-upcoming {
                background: #fff3cd;
                color: #856404;
            }
            
            .status-current {
                background: #d4edda;
                color: #155724;
            }
            
            .status-past {
                background: #f8d7da;
                color: #721c24;
            }
            
            .actions {
                display: flex;
                gap: 8px;
            }
            
            .no-data {
                text-align: center;
                padding: 40px;
                color: #6c757d;
            }
            
            .no-data i {
                font-size: 48px;
                margin-bottom: 16px;
                opacity: 0.5;
            }
        </style>
    "/>
</jsp:include>

<div class="content-container">
    <!-- Page Header -->
    <div class="page-header">
        <div>
            <h1 class="page-title">Quản lý phim</h1>
            <p class="page-subtitle">Danh sách tất cả phim trong hệ thống</p>
        </div>
        <a href="admin-movies?action=create" class="btn btn-primary">
            <i class="fas fa-plus"></i>
            Thêm phim mới
        </a>
    </div>

    <!-- Filters -->
    <div class="filters">
        <div class="filter-group">
            <label>Tìm kiếm</label>
            <input type="text" id="searchInput" placeholder="Tên phim, đạo diễn...">
        </div>
        <div class="filter-group">
            <label>Thể loại</label>
            <select id="genreFilter">
                <option value="">Tất cả thể loại</option>
                <c:forEach var="genre" items="${genres}">
                    <option value="${genre}">${genre}</option>
                </c:forEach>
            </select>
        </div>
        <div class="filter-group">
            <label>Trạng thái</label>
            <select id="statusFilter">
                <option value="">Tất cả</option>
                <option value="Sắp chiếu">Sắp chiếu</option>
                <option value="Đang chiếu">Đang chiếu</option>
                <option value="Ngừng chiếu">Ngừng chiếu</option>
            </select>
        </div>
    </div>

    <!-- Data Table -->
    <div class="data-table">
        <c:choose>
            <c:when test="${not empty movies}">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Poster</th>
                            <th>Thông tin phim</th>
                            <th>Thể loại</th>
                            <th>Loại phim</th>
                            <th>Độ tuổi</th>
                            <th>Thời lượng</th>
                            <th>Ngày khởi chiếu</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="movie" items="${movies}">
                            <tr>
                                <td>
                                    <img src="${pageContext.request.contextPath}/assets/image/${movie.poster}" 
                                         alt="${movie.tenPhim}" 
                                         class="movie-poster"
                                         data-fallback="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iODAiIHZpZXdCb3g9IjAgMCA2MCA4MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjYwIiBoZWlnaHQ9IjgwIiBmaWxsPSIjRjhGOUZBIi8+CjxwYXRoIGQ9Ik0zMCAyMEMzMCAxNi42ODYzIDMyLjY4NjMgMTQgMzYgMTRIMjRDMjcuMzEzNyAxNCAzMCAxNi42ODYzIDMwIDIwVjYwQzMwIDYzLjMxMzcgMjcuMzEzNyA2NiAyNCA2NkgzNkMzMi42ODYzIDY2IDMwIDYzLjMxMzcgMzAgNjBWMjBaIiBmaWxsPSIjRDlEOUQ5Ii8+CjxzdmcgeD0iMjIiIHk9IjM1IiB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSI+CjxwYXRoIGQ9Ik0xMiAyTDEzLjA5IDguMjZMMjAgOUwxMy4wOSAxNS43NEwxMiAyMkwxMC45MSAxNS43NEw0IDlMMTAuOTEgOC4yNkwxMiAyWiIgZmlsbD0iIzk5OSIvPgo8L3N2Zz4KPC9zdmc+Cg==">
                                </td>
                                <td>
                                    <div class="movie-info">
                                        <div class="movie-title">${movie.tenPhim}</div>
                                        <div class="movie-genre">${movieIdToDirectors[movie.maPhim]}</div>
                                        <div class="movie-duration">${movie.dienVien}</div>
                                    </div>
                                </td>
                                <td>${movieIdToGenres[movie.maPhim]}</td>
                                <td>${movie.loaiPhim}</td>
                                <td>${movie.doTuoiGioiHan}+</td>
                                <td>${movie.thoiLuong} phút</td>
                                <td>
                                    ${dateLabelMap[movie.maPhim]}
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${movie.trangThai == 'Sắp chiếu'}">
                                            <span class="status-badge status-upcoming">Sắp chiếu</span>
                                        </c:when>
                                        <c:when test="${movie.trangThai == 'Đang chiếu'}">
                                            <span class="status-badge status-current">Đang chiếu</span>
                                        </c:when>
                                        <c:when test="${movie.trangThai == 'Ngừng chiếu'}">
                                            <span class="status-badge status-past">Ngừng chiếu</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status-badge status-past">${movie.trangThai != null ? movie.trangThai : 'N/A'}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div class="actions">
                                        <a href="admin-movies?action=edit&maPhim=${movie.maPhim}" 
                                           class="btn btn-warning btn-sm">
                                            <i class="fas fa-edit"></i>
                                            Sửa
                                        </a>
                                        <button onclick="deleteMovie(${movie.maPhim}, '${movie.tenPhim}')" 
                                                class="btn btn-danger btn-sm">
                                            <i class="fas fa-trash"></i>
                                            Xóa
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="no-data">
                    <i class="fas fa-film"></i>
                    <h3>Chưa có phim nào</h3>
                    <p>Hãy thêm phim đầu tiên để bắt đầu quản lý</p>
                    <a href="admin-movies?action=create" class="btn btn-primary">
                        <i class="fas fa-plus"></i>
                        Thêm phim mới
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
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
    });
    
    // Search functionality
    document.getElementById('searchInput').addEventListener('input', function() {
        const searchTerm = this.value.toLowerCase();
        const rows = document.querySelectorAll('.table tbody tr');
        
        rows.forEach(row => {
            const movieTitle = row.querySelector('.movie-title').textContent.toLowerCase();
            const director = row.querySelector('.movie-genre').textContent.toLowerCase();
            const actors = row.querySelector('.movie-duration').textContent.toLowerCase();
            
            if (movieTitle.includes(searchTerm) || director.includes(searchTerm) || actors.includes(searchTerm)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    });

    // Genre filter
    document.getElementById('genreFilter').addEventListener('change', function() {
        const selectedGenre = this.value;
        const rows = document.querySelectorAll('.table tbody tr');
        
        rows.forEach(row => {
            const genreText = row.cells[2].textContent.trim();
            // Check if the selected genre is contained in the movie's genres
            if (selectedGenre === '' || genreText.includes(selectedGenre)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    });

    // Status filter
    document.getElementById('statusFilter').addEventListener('change', function() {
        const selectedStatus = this.value;
        const rows = document.querySelectorAll('.table tbody tr');
        
        rows.forEach(row => {
            const statusBadge = row.querySelector('.status-badge');
            const status = statusBadge ? statusBadge.textContent.trim() : '';
            
            if (selectedStatus === '' || status === selectedStatus) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    });

    // Delete movie function
    function deleteMovie(maPhim, tenPhim) {
        if (confirm('Bạn có chắc chắn muốn xóa phim "' + tenPhim + '"?')) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = 'admin-movies';
            
            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'delete';
            
            const maPhimInput = document.createElement('input');
            maPhimInput.type = 'hidden';
            maPhimInput.name = 'maPhim';
            maPhimInput.value = maPhim;
            
            form.appendChild(actionInput);
            form.appendChild(maPhimInput);
            document.body.appendChild(form);
            form.submit();
        }
    }
</script>

<jsp:include page="../layout/admin-footer.jsp" />
