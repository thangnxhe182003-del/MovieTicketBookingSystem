<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="${room != null ? 'Chỉnh sửa phòng' : 'Thêm phòng mới'}"/>
    <jsp:param name="pageSubtitle" value="${room != null ? 'Cập nhật thông tin phòng' : 'Tạo phòng chiếu mới'}"/>
    <jsp:param name="currentPage" value="rooms"/>
    <jsp:param name="extraStyles" value="
        <style>
            .container { padding:30px; }
            .card { background:#fff; border-radius:8px; box-shadow:0 2px 6px rgba(0,0,0,0.1); }
            .card-header { padding:16px 24px; border-bottom:1px solid #e9ecef; background:#f8f9fa; }
            .card-body { padding:24px; }
            .form-group { margin-bottom:16px; }
            label { font-weight:600; margin-bottom:6px; display:block; }
            input, select { width:100%; padding:10px 12px; border:1px solid #ddd; border-radius:6px; }
            .actions { display:flex; gap:12px; justify-content:flex-end; margin-top:16px; }
            .btn { padding:12px 24px; border:none; border-radius:6px; font-weight:600; cursor:pointer; }
            .btn-primary { background:linear-gradient(135deg,#e50914,#c90812); color:#fff; }
            .btn-secondary { background:#6c757d; color:#fff; }
        </style>
    "/>
</jsp:include>

<div class="container">
    <div class="card">
        <div class="card-header">
            <h3>${room != null ? 'Chỉnh sửa phòng' : 'Thêm phòng mới'}</h3>
        </div>
        <div class="card-body">
            <form action="admin-rooms" method="POST">
                <input type="hidden" name="action" value="${room != null ? 'update' : 'create'}" />
                <c:if test="${room != null}">
                    <input type="hidden" name="maPhong" value="${room.maPhong}" />
                </c:if>

                <div class="form-group">
                    <label for="maRap">Rạp</label>
                    <select id="maRap" name="maRap" required>
                        <option value="">Chọn rạp</option>
                        <c:forEach var="c" items="${cinemas}">
                            <option value="${c.maRap}" ${room != null && room.maRap == c.maRap ? 'selected' : ''}>${c.tenRap}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="tenPhong">Tên phòng</label>
                    <input type="text" id="tenPhong" name="tenPhong" value="${room != null ? room.tenPhong : ''}" required />
                </div>

                <div class="form-group">
                    <label for="soLuongGhe">Số lượng ghế</label>
                    <input type="number" id="soLuongGhe" name="soLuongGhe" min="1" value="${room != null ? room.soLuongGhe : ''}" required />
                </div>

                <div class="actions">
                    <a class="btn btn-secondary" href="admin-rooms">Hủy</a>
                    <button class="btn btn-primary" type="submit">${room != null ? 'Lưu' : 'Tạo mới'}</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="../layout/admin-footer.jsp" />


