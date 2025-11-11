<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Quản lý phòng"/>
    <jsp:param name="pageSubtitle" value="Danh sách phòng chiếu và thao tác quản lý"/>
    <jsp:param name="currentPage" value="rooms"/>
    <jsp:param name="extraStyles" value="
        <style>
            .content-container { padding: 30px; }
            .page-header { background:#f8f9fa; padding:20px 30px; border-bottom:1px solid #e9ecef; display:flex; justify-content:space-between; align-items:center; margin-bottom:30px; }
            .page-title { font-size:24px; font-weight:700; color:#333; margin:0; }
            .btn { padding:12px 24px; border:none; border-radius:6px; font-weight:600; cursor:pointer; text-decoration:none; display:inline-flex; align-items:center; gap:8px; transition:all .3s; }
            .btn-primary { background:linear-gradient(135deg,#e50914,#c90812); color:#fff; }
            .btn-warning { background:#ffc107; color:#212529; }
            .btn-danger { background:#dc3545; color:#fff; }
            .btn-secondary { background:#6c757d; color:#fff; }
            .btn-sm { padding:8px 12px; font-size:12px; }
            .data-table { background:#fff; border-radius:8px; box-shadow:0 2px 4px rgba(0,0,0,0.1); overflow:hidden; }
            table { width:100%; border-collapse:collapse; }
            th { background:#f8f9fa; padding:12px; text-align:left; border-bottom:2px solid #e9ecef; font-size:14px; }
            td { padding:12px; border-bottom:1px solid #e9ecef; font-size:14px; }
            .actions { display:flex; gap:8px; }
        </style>
    "/>
</jsp:include>

<div class="content-container">
    <div class="page-header">
        <div>
            <h1 class="page-title">Quản lý phòng</h1>
            <p>Danh sách phòng chiếu</p>
        </div>
        <a href="admin-rooms?action=create" class="btn btn-primary"><i class="fas fa-plus"></i> Thêm phòng</a>
    </div>
    <c:if test="${not empty param.message}">
        <div class="alert ${param.type == 'error' ? 'alert-danger' : 'alert-success'}" style="margin-bottom: 20px; padding: 12px 16px; border: 1px solid; border-radius: 6px;">
            <i class="fas fa-${param.type == 'error' ? 'exclamation-circle' : 'check-circle'}"></i> ${param.message}
        </div>
    </c:if>

    <div class="data-table">
        <table>
            <thead>
                <tr>
                    <th>Mã phòng</th>
                    <th>Mã rạp</th>
                    <th>Tên phòng</th>
                    <th>Số lượng ghế</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="room" items="${rooms}">
                    <tr>
                        <td>${room.maPhong}</td>
                        <td>${room.maRap}</td>
                        <td>${room.tenPhong}</td>
                        <td>${room.soLuongGhe}</td>
                        <td>
                            <div class="actions">
                                <a class="btn btn-warning btn-sm" href="admin-rooms?action=edit&maPhong=${room.maPhong}"><i class="fas fa-edit"></i> Sửa</a>
                                <a class="btn btn-secondary btn-sm" href="admin-rooms?action=seats&maPhong=${room.maPhong}"><i class="fas fa-chair"></i> Ghế</a>
                                <button class="btn btn-danger btn-sm" onclick="deleteRoom(${room.maPhong})"><i class="fas fa-trash"></i> Xóa</button>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script>
function deleteRoom(maPhong) {
    if (confirm('Xóa phòng này?')) {
        const f = document.createElement('form');
        f.method = 'POST';
        f.action = 'admin-rooms';
        f.innerHTML = '<input type="hidden" name="action" value="delete" />' +
                      '<input type="hidden" name="maPhong" value="' + maPhong + '" />';
        document.body.appendChild(f);
        f.submit();
    }
}
</script>

<jsp:include page="../layout/admin-footer.jsp" />


