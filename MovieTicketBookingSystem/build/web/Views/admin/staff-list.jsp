<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Quản lý nhân viên"/>
    <jsp:param name="pageSubtitle" value="Quản lý thông tin nhân viên và phân quyền"/>
    <jsp:param name="currentPage" value="staff"/>
    <jsp:param name="extraStyles" value="
        <style>
            .stats-section {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 20px;
                margin-bottom: 30px;
            }
            
            .stat-card {
                background: white;
                padding: 20px;
                border-radius: 12px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                text-align: center;
                border-left: 4px solid #e50914;
            }
            
            .stat-number {
                font-size: 28px;
                font-weight: 700;
                color: #e50914;
                margin-bottom: 5px;
            }
            
            .stat-label {
                color: #666;
                font-size: 14px;
                font-weight: 500;
            }
            
            .search-section {
                background: white;
                padding: 20px;
                border-radius: 12px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                margin-bottom: 20px;
            }
            
            .search-form {
                display: flex;
                gap: 10px;
                align-items: center;
            }
            
            .search-input {
                flex: 1;
                padding: 10px 15px;
                border: 1px solid #ddd;
                border-radius: 6px;
                font-size: 14px;
            }
            
            .btn-search {
                padding: 10px 20px;
                background: #e50914;
                color: white;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-weight: 500;
                text-decoration: none;
                display: inline-block;
            }
            
            .btn-search:hover {
                background: #cc0812;
            }
            
            .staff-table {
                background: white;
                border-radius: 12px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                overflow: hidden;
            }
            
            .table {
                width: 100%;
                border-collapse: collapse;
            }
            
            .table th {
                background: #f8f9fa;
                padding: 15px 12px;
                text-align: left;
                font-weight: 600;
                color: #333;
                border-bottom: 2px solid #e9ecef;
            }
            
            .table td {
                padding: 12px;
                border-bottom: 1px solid #e9ecef;
                vertical-align: middle;
            }
            
            .table tr:hover {
                background: #f8f9fa;
            }
            
            .position-badge {
                padding: 4px 8px;
                border-radius: 12px;
                font-size: 12px;
                font-weight: 500;
                text-transform: uppercase;
            }
            
            .position-admin {
                background: #dc3545;
                color: white;
            }
            
            .position-manager {
                background: #fd7e14;
                color: white;
            }
            
            .position-staff {
                background: #28a745;
                color: white;
            }
            
            .status-badge {
                padding: 4px 8px;
                border-radius: 12px;
                font-size: 12px;
                font-weight: 500;
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
            
            .status-suspended {
                background: #fff3cd;
                color: #856404;
            }
            
            .action-buttons {
                display: flex;
                gap: 5px;
            }
            
            .btn-edit, .btn-delete, .btn-status {
                padding: 6px 12px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 12px;
                font-weight: 500;
                text-decoration: none;
                display: inline-block;
            }
            
            .btn-edit {
                background: #007bff;
                color: white;
            }
            
            .btn-edit:hover {
                background: #0056b3;
            }
            
            .btn-delete {
                background: #dc3545;
                color: white;
            }
            
            .btn-delete:hover {
                background: #c82333;
            }
            
            .btn-status {
                background: #28a745;
                color: white;
            }
            
            .btn-status:hover {
                background: #218838;
            }
            
            .btn-add {
                background: #e50914;
                color: white;
                padding: 10px 20px;
                border-radius: 6px;
                text-decoration: none;
                font-weight: 500;
                display: inline-block;
            }
            
            .btn-add:hover {
                background: #cc0812;
            }
            
            .empty-state {
                text-align: center;
                padding: 40px;
                color: #666;
            }
            
            .empty-state i {
                font-size: 48px;
                color: #ddd;
                margin-bottom: 15px;
            }
            
            @media (max-width: 768px) {
                .search-form {
                    flex-direction: column;
                }
                
                .search-input {
                    width: 100%;
                }
                
                .table {
                    font-size: 12px;
                }
                
                .table th,
                .table td {
                    padding: 8px 6px;
                }
                
                .action-buttons {
                    flex-direction: column;
                }
            }
        </style>
    "/>
</jsp:include>

<!-- STATS -->
<div class="stats-section">
    <div class="stat-card">
        <div class="stat-number">${totalStaff}</div>
        <div class="stat-label">Tổng số nhân viên</div>
    </div>
    <div class="stat-card">
        <div class="stat-number">${activeStaff}</div>
        <div class="stat-label">Đang hoạt động</div>
    </div>
    <div class="stat-card">
        <div class="stat-number">${adminCount}</div>
        <div class="stat-label">Admin</div>
    </div>
    <div class="stat-card">
        <div class="stat-number">${managerCount}</div>
        <div class="stat-label">Manager</div>
    </div>
    <div class="stat-card">
        <div class="stat-number">${staffCount}</div>
        <div class="stat-label">Staff</div>
    </div>
</div>

<!-- SEARCH SECTION -->
<div class="search-section">
    <form class="search-form" method="GET" action="admin-staff">
        <input type="hidden" name="action" value="search">
        <input type="text" 
               name="keyword" 
               class="search-input" 
               placeholder="Tìm kiếm theo tên, username, email, số điện thoại hoặc chức vụ..."
               value="${searchKeyword}">
        <button type="submit" class="btn-search">🔍 Tìm kiếm</button>
        <c:if test="${not empty searchKeyword}">
            <a href="admin-staff" class="btn-search" style="background: #6c757d; text-decoration: none;">
                ✕ Xóa bộ lọc
            </a>
        </c:if>
    </form>
</div>

<!-- ADD BUTTON -->
<div style="text-align: right; margin-bottom: 20px;">
    <a href="admin-staff?action=add" class="btn-add">
        ➕ Thêm nhân viên mới
    </a>
</div>

<!-- STAFF TABLE -->
<div class="staff-table">
    <c:choose>
        <c:when test="${not empty staffList}">
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Rạp phim</th>
                        <th>Họ tên</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Số điện thoại</th>
                        <th>Chức vụ</th>
                        <th>Trạng thái</th>
                        <th>Ngày tạo</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="staff" items="${staffList}">
                        <tr>
                            <td>${staff.maNhanVien}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty staff.maRap}">
                                        🏢 Rạp #${staff.maRap}
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <strong>${staff.hoTen}</strong>
                            </td>
                            <td>${staff.tenDangNhap}</td>
                            <td>${staff.email}</td>
                            <td>${staff.soDienThoai}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${staff.chucVu == 'Admin'}">
                                        <span class="position-badge position-admin">👑 Admin</span>
                                    </c:when>
                                    <c:when test="${staff.chucVu == 'Manager'}">
                                        <span class="position-badge position-manager">👔 Manager</span>
                                    </c:when>
                                    <c:when test="${staff.chucVu == 'Staff'}">
                                        <span class="position-badge position-staff">👤 Staff</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="position-badge">${staff.chucVu}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${staff.trangThai == 'Active'}">
                                        <span class="status-badge status-active">Hoạt động</span>
                                    </c:when>
                                    <c:when test="${staff.trangThai == 'Inactive'}">
                                        <span class="status-badge status-inactive">Không hoạt động</span>
                                    </c:when>
                                    <c:when test="${staff.trangThai == 'Suspended'}">
                                        <span class="status-badge status-suspended">Tạm khóa</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge">${staff.trangThai}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:set var="dateKey" value="staffDate_${staff.maNhanVien}"/>
                                ${requestScope[dateKey]}
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <a href="admin-staff?action=edit&maNhanVien=${staff.maNhanVien}" class="btn-edit">
                                        ✏️ Sửa
                                    </a>
                                    <c:if test="${staff.trangThai != 'Inactive'}">
                                        <button onclick="confirmDelete(${staff.maNhanVien}, '${staff.hoTen}')" class="btn-delete">
                                            🗑️ Xóa
                                        </button>
                                    </c:if>
                                    <c:if test="${staff.trangThai == 'Inactive'}">
                                        <a href="admin-staff?action=status&maNhanVien=${staff.maNhanVien}&trangThai=Active" class="btn-status">
                                            ✅ Kích hoạt
                                        </a>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <i class="fas fa-user-tie"></i>
                <h3>Không có nhân viên nào</h3>
                <p>Chưa có nhân viên nào trong hệ thống</p>
                <a href="admin-staff?action=add" class="btn-add">Thêm nhân viên đầu tiên</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script>
    function confirmDelete(maNhanVien, hoTen) {
        if (confirm('Bạn có chắc chắn muốn xóa nhân viên "' + hoTen + '" không?\n\nLưu ý: Hành động này sẽ đánh dấu nhân viên là không hoạt động và không thể hoàn tác!')) {
            window.location.href = 'admin-staff?action=delete&maNhanVien=' + maNhanVien;
        }
    }
</script>

</div>
</div>
