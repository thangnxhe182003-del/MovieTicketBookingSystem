<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Quản lý rạp phim"/>
    <jsp:param name="pageSubtitle" value="Quản lý thông tin các rạp phim trong hệ thống"/>
    <jsp:param name="currentPage" value="cinema"/>
    <jsp:param name="extraStyles" value="
        <style>
            .admin-page {
                background: #f5f5f5;
                min-height: 100vh;
                padding: 20px 0;
            }
            
            .admin-container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 0 20px;
            }
            
            .page-header {
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                margin-bottom: 20px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            
            .page-title {
                font-size: 24px;
                font-weight: bold;
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
                transition: all 0.3s;
            }
            
            .btn-add:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(229, 9, 20, 0.4);
            }
            
            .search-section {
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                margin-bottom: 20px;
            }
            
            .search-form {
                display: flex;
                gap: 15px;
                align-items: center;
            }
            
            .search-input {
                flex: 1;
                padding: 10px 15px;
                border: 1px solid #ddd;
                border-radius: 6px;
                font-size: 14px;
            }
            
            .search-input:focus {
                outline: none;
                border-color: #e50914;
                box-shadow: 0 0 4px rgba(229, 9, 20, 0.3);
            }
            
            .btn-search {
                background: #007bff;
                color: white;
                padding: 10px 20px;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-weight: 600;
            }
            
            .btn-search:hover {
                background: #0056b3;
            }
            
            .cinema-table {
                background: white;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                overflow: hidden;
            }
            
            .table {
                width: 100%;
                border-collapse: collapse;
                margin: 0;
            }
            
            .table th {
                background: #f8f9fa;
                padding: 15px;
                text-align: left;
                font-weight: 600;
                color: #333;
                border-bottom: 2px solid #e0e0e0;
            }
            
            .table td {
                padding: 15px;
                border-bottom: 1px solid #e0e0e0;
                vertical-align: middle;
            }
            
            .table tbody tr:hover {
                background: #f8f9fa;
            }
            
            .cinema-info {
                display: flex;
                flex-direction: column;
                gap: 5px;
            }
            
            .cinema-name {
                font-weight: 600;
                color: #333;
                font-size: 16px;
            }
            
            .cinema-area {
                color: #666;
                font-size: 14px;
                background: #e3f2fd;
                padding: 2px 8px;
                border-radius: 12px;
                display: inline-block;
                width: fit-content;
            }
            
            .cinema-address {
                color: #666;
                font-size: 14px;
            }
            
            .action-buttons {
                display: flex;
                gap: 8px;
            }
            
            .btn-edit {
                background: #28a745;
                color: white;
                padding: 6px 12px;
                border: none;
                border-radius: 4px;
                text-decoration: none;
                font-size: 12px;
                font-weight: 600;
                transition: all 0.3s;
            }
            
            .btn-edit:hover {
                background: #218838;
                transform: translateY(-1px);
            }
            
            .btn-delete {
                background: #dc3545;
                color: white;
                padding: 6px 12px;
                border: none;
                border-radius: 4px;
                text-decoration: none;
                font-size: 12px;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.3s;
            }
            
            .btn-delete:hover {
                background: #c82333;
                transform: translateY(-1px);
            }
            
            .empty-state {
                text-align: center;
                padding: 40px;
                color: #666;
            }
            
            .empty-state i {
                font-size: 48px;
                color: #ccc;
                margin-bottom: 15px;
            }
            
            .stats-section {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 15px;
                margin-bottom: 20px;
            }
            
            .stat-card {
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                text-align: center;
            }
            
            .stat-number {
                font-size: 24px;
                font-weight: bold;
                color: #e50914;
                margin-bottom: 5px;
            }
            
            .stat-label {
                color: #666;
                font-size: 14px;
            }
            
            @media (max-width: 768px) {
                .page-header {
                    flex-direction: column;
                    gap: 15px;
                    text-align: center;
                }
                
                .search-form {
                    flex-direction: column;
                }
                
                .action-buttons {
                    flex-direction: column;
                }
                
                .table {
                    font-size: 14px;
                }
                
                .table th,
                .table td {
                    padding: 10px 8px;
                }
            }
        </style>
    "/>
</jsp:include>

<!-- STATS -->
<div class="stats-section">
    <div class="stat-card">
        <div class="stat-number">${cinemas.size()}</div>
        <div class="stat-label">Tổng số rạp phim</div>
    </div>
    <div class="stat-card">
        <div class="stat-number">${areas.size()}</div>
        <div class="stat-label">Khu vực</div>
    </div>
</div>

<!-- SEARCH SECTION -->
<div class="search-section">
    <form class="search-form" method="GET" action="admin-cinema">
        <input type="hidden" name="action" value="search">
        <input type="text" 
               name="keyword" 
               class="search-input" 
               placeholder="Tìm kiếm theo tên rạp, địa chỉ hoặc khu vực..."
               value="${searchKeyword}">
        <button type="submit" class="btn-search">🔍 Tìm kiếm</button>
        <c:if test="${not empty searchKeyword}">
            <a href="admin-cinema" class="btn-search" style="background: #6c757d; text-decoration: none;">
                ✕ Xóa bộ lọc
            </a>
        </c:if>
    </form>
</div>

<!-- ADD BUTTON -->
<div style="text-align: right; margin-bottom: 20px;">
    <a href="admin-cinema?action=add" class="btn-add">
        ➕ Thêm rạp phim mới
    </a>
</div>

        <!-- CINEMA TABLE -->
        <div class="cinema-table">
            <c:choose>
                <c:when test="${not empty cinemas}">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Mã rạp</th>
                                <th>Thông tin rạp phim</th>
                                <th>Địa chỉ</th>
                                <th>Khu vực</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cinema" items="${cinemas}">
                                <tr>
                                    <td>
                                        <strong style="color: #e50914;">#${cinema.maRap}</strong>
                                    </td>
                                    <td>
                                        <div class="cinema-info">
                                            <div class="cinema-name">${cinema.tenRap}</div>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="cinema-address">
                                            📍 ${cinema.diaChi}
                                        </div>
                                    </td>
                                    <td>
                                        <c:if test="${not empty cinema.khuVuc}">
                                            <span class="cinema-area">${cinema.khuVuc}</span>
                                        </c:if>
                                        <c:if test="${empty cinema.khuVuc}">
                                            <span style="color: #999;">Chưa cập nhật</span>
                                        </c:if>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="admin-cinema?action=edit&maRap=${cinema.maRap}" 
                                               class="btn-edit">
                                                ✏️ Sửa
                                            </a>
                                            <button onclick="confirmDelete(${cinema.maRap}, '${cinema.tenRap}')" 
                                                    class="btn-delete">
                                                🗑️ Xóa
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <i class="fas fa-film"></i>
                        <h3>Không có rạp phim nào</h3>
                        <p>
                            <c:choose>
                                <c:when test="${not empty searchKeyword}">
                                    Không tìm thấy rạp phim nào với từ khóa "${searchKeyword}"
                                </c:when>
                                <c:otherwise>
                                    Chưa có rạp phim nào trong hệ thống
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <c:if test="${empty searchKeyword}">
                            <a href="admin-cinema?action=add" class="btn-add" style="margin-top: 15px;">
                                ➕ Thêm rạp phim đầu tiên
                            </a>
                        </c:if>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script>
    function confirmDelete(maRap, tenRap) {
        if (confirm('Bạn có chắc chắn muốn xóa rạp phim "' + tenRap + '" không?\n\nLưu ý: Hành động này không thể hoàn tác!')) {
            window.location.href = 'admin-cinema?action=delete&maRap=' + maRap;
        }
    }
</script>

</div>
</div>
