<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/admin-header.jsp">
	<jsp:param name="pageTitle" value="Quản lý đạo diễn"/>
	<jsp:param name="pageSubtitle" value="Danh sách đạo diễn"/>
	<jsp:param name="currentPage" value="directors"/>
	<jsp:param name="extraStyles" value="
        <style>
            .content-container { padding: 30px; }
            .data-table { background: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); overflow: hidden; }
            .table { width: 100%; border-collapse: collapse; margin: 0; }
            .table th { background: #f8f9fa; padding: 15px 12px; text-align: left; font-weight: 600; color: #333; border-bottom: 2px solid #e9ecef; font-size: 14px; }
            .table td { padding: 15px 12px; border-bottom: 1px solid #e9ecef; vertical-align: middle; font-size: 14px; }
            .btn { padding: 8px 16px; border: none; border-radius: 6px; font-weight: 600; cursor: pointer; text-decoration: none; display: inline-flex; align-items: center; gap: 8px; transition: all 0.3s; }
            .btn-primary { background: linear-gradient(135deg, #e50914, #c90812); color: #fff; }
            .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(229, 9, 20, 0.4); }
            .btn-warning { background: #ffc107; color: #212529; }
            .btn-danger { background: #dc3545; color: #fff; }
            .btn-sm { padding: 6px 12px; font-size: 12px; }
        </style>
    "/>
</jsp:include>
<div class="content-container">
	<div class="page-header" style="display:flex; justify-content:space-between; align-items:center; margin-bottom:16px;">
		<h2 class="page-title" style="margin:0;">Danh sách đạo diễn</h2>
		<button class="btn btn-primary" onclick="openDirectorModal()">Thêm đạo diễn</button>
	</div>
	<div class="data-table">
	<table class="table">
		<thead>
		<tr>
			<th style="text-align:left;">Mã</th>
			<th style="text-align:left;">Tên đạo diễn</th>
			<th style="text-align:left;">Thao tác</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="d" items="${directors}">
			<tr>
				<td>${d.maDaoDien}</td>
				<td>${d.tenDaoDien}</td>
				<td>
					<button class="btn btn-warning btn-sm" onclick="openDirectorModal(${d.maDaoDien}, '${d.tenDaoDien}')">Sửa</button>
					<form method="post" action="admin-directors" style="display:inline;">
						<input type="hidden" name="action" value="delete">
						<input type="hidden" name="id" value="${d.maDaoDien}">
						<button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Xóa đạo diễn này?')">Xóa</button>
					</form>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</div>
</div>

<!-- Modal Director -->
<div id="directorModal" style="display:none; position:fixed; inset:0; background:rgba(0,0,0,0.4); align-items:center; justify-content:center;">
	<div style="background:#fff; padding:20px; border-radius:8px; width:400px;">
		<h3 id="directorModalTitle">Thêm đạo diễn</h3>
		<form method="post" action="admin-directors">
			<input type="hidden" name="action" value="create" id="directorAction">
			<input type="hidden" name="id" id="directorId">
			<div class="form-group" style="margin-top:10px;">
				<label>Tên đạo diễn</label>
				<input type="text" name="name" id="directorName" required style="width:100%; padding:8px;">
			</div>
			<div style="display:flex; gap:8px; justify-content:flex-end; margin-top:12px;">
				<button type="button" class="btn btn-secondary" onclick="closeDirectorModal()">Hủy</button>
				<button type="submit" class="btn btn-primary">Lưu</button>
			</div>
		</form>
	</div>
</div>
<script>
	function openDirectorModal(id, name) {
		document.getElementById('directorModal').style.display = 'flex';
		if (id) {
			document.getElementById('directorModalTitle').textContent = 'Sửa đạo diễn';
			document.getElementById('directorAction').value = 'update';
			document.getElementById('directorId').value = id;
			document.getElementById('directorName').value = name || '';
		} else {
			document.getElementById('directorModalTitle').textContent = 'Thêm đạo diễn';
			document.getElementById('directorAction').value = 'create';
			document.getElementById('directorId').value = '';
			document.getElementById('directorName').value = '';
		}
	}
	function closeDirectorModal() {
		document.getElementById('directorModal').style.display = 'none';
	}
</script>
<jsp:include page="../layout/admin-footer.jsp" />


