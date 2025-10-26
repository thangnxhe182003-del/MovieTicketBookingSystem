<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Quản lý khách hàng"/>
    <jsp:param name="pageSubtitle" value="Quản lý thông tin khách hàng trong hệ thống"/>
    <jsp:param name="currentPage" value="customers"/>
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
            
            .customer-table {
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
            
            .status-pending {
                background: #fff3cd;
                color: #856404;
            }
            
            .status-deleted {
                background: #f8d7da;
                color: #721c24;
            }
            
            .verified-badge {
                color: #28a745;
                font-weight: 600;
            }
            
            .not-verified-badge {
                color: #dc3545;
                font-weight: 600;
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
        <div class="stat-number">${totalCustomers}</div>
        <div class="stat-label">Tổng số khách hàng</div>
    </div>
    <div class="stat-card">
        <div class="stat-number">${activeCustomers}</div>
        <div class="stat-label">Đang hoạt động</div>
    </div>
    <div class="stat-card">
        <div class="stat-number">${pendingCustomers}</div>
        <div class="stat-label">Chờ xác thực</div>
    </div>
    <div class="stat-card">
        <div class="stat-number">${verifiedCustomers}</div>
        <div class="stat-label">Đã xác thực email</div>
    </div>
</div>

<!-- SEARCH SECTION -->
<div class="search-section">
    <form class="search-form" method="GET" action="admin-customers">
        <input type="hidden" name="action" value="search">
        <input type="text" 
               name="keyword" 
               class="search-input" 
               placeholder="Tìm kiếm theo tên, username, email hoặc số điện thoại..."
               value="${searchKeyword}">
        <button type="submit" class="btn-search">🔍 Tìm kiếm</button>
        <c:if test="${not empty searchKeyword}">
            <a href="admin-customers" class="btn-search" style="background: #6c757d; text-decoration: none;">
                ✕ Xóa bộ lọc
            </a>
        </c:if>
    </form>
</div>

<!-- ADD BUTTON -->
<div style="text-align: right; margin-bottom: 20px;">
    <a href="admin-customers?action=add" class="btn-add">
        ➕ Thêm khách hàng mới
    </a>
</div>

<!-- CUSTOMER TABLE -->
<div class="customer-table">
    <c:choose>
        <c:when test="${not empty customers}">
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Họ tên</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Số điện thoại</th>
                        <th>Giới tính</th>
                        <th>Ngày sinh</th>
                        <th>Xác thực</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="customer" items="${customers}">
                        <tr>
                            <td>${customer.maKH}</td>
                            <td>
                                <strong>${customer.hoTen}</strong>
                            </td>
                            <td>${customer.tenDangNhap}</td>
                            <td>${customer.email}</td>
                            <td>${customer.soDienThoai}</td>
                            <td>
                                ${customer.gioiTinh}
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty customer.ngaySinh}">
                                        ${customer.ngaySinh}
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${customer.isEmailVerified}">
                                        <span class="verified-badge">✅ Đã xác thực</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="not-verified-badge">❌ Chưa xác thực</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${customer.trangThai == 'Active'}">
                                        <span class="status-badge status-active">Hoạt động</span>
                                    </c:when>
                                    <c:when test="${customer.trangThai == 'Pending'}">
                                        <span class="status-badge status-pending">Chờ xác thực</span>
                                    </c:when>
                                    <c:when test="${customer.trangThai == 'Deleted'}">
                                        <span class="status-badge status-deleted">Đã xóa</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge">${customer.trangThai}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            
                            <td>
                                <div class="action-buttons">
                                    <a href="admin-customers?action=edit&maKH=${customer.maKH}" class="btn-edit">
                                        ✏️ Sửa
                                    </a>
                                    <c:if test="${customer.trangThai != 'Deleted'}">
                                        <button onclick="confirmSuspend(${customer.maKH}, '${customer.hoTen}')" class="btn-delete">
                                            ⏸️ Tạm dừng
                                        </button>
                                    </c:if>
                                    <c:if test="${customer.trangThai == 'Pending'}">
                                        <a href="admin-customers?action=status&maKH=${customer.maKH}&trangThai=Active" class="btn-status">
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
                <i class="fas fa-users"></i>
                <h3>Không có khách hàng nào</h3>
                <p>Chưa có khách hàng nào trong hệ thống</p>
                <a href="admin-customers?action=add" class="btn-add">Thêm khách hàng đầu tiên</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script>
    function confirmSuspend(maKH, hoTen) {
        if (confirm('Bạn có chắc chắn muốn tạm dừng tài khoản khách hàng "' + hoTen + '" không?\n\nLưu ý: Khách hàng sẽ không thể đăng nhập cho đến khi được kích hoạt lại!')) {
            window.location.href = 'admin-customers?action=delete&maKH=' + maKH;
        }
    }
</script>

</div>
</div>
