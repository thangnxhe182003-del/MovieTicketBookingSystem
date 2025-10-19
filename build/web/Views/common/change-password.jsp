<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Đổi mật khẩu"/>
    <jsp:param name="extraStyles" value="
        <style>
            .page-screen { min-height: 100vh; display:flex; align-items:center; justify-content:center; padding:20px; background: var(--light-bg); }
            .form-container { background:#fff; width:100%; max-width:480px; border-radius:12px; box-shadow:0 10px 30px rgba(0,0,0,.08); overflow:hidden; }
            .form-header { background:#e50914; color:#fff; padding:18px 24px; font-weight:700; font-size:18px; }
            .form-content { padding: 24px; }
            .form-group { margin-bottom:14px; position:relative; }
            label { display:block; font-size:13px; color:#555; margin-bottom:6px; }
            input { width:100%; padding:10px 12px; border:1px solid #ddd; border-radius:6px; font-size:14px; }
            .actions { display:flex; gap:12px; margin-top:8px; }
            .btn { padding:12px 16px; border:none; border-radius:6px; font-weight:700; cursor:pointer; }
            .btn-primary { background:#e50914; color:#fff; }
            .btn-secondary { background:#f0f0f0; color:#333; }
        </style>
    "/>
</jsp:include>

<div class="page-screen">
    <div class="form-container">
        <div class="form-header">Đổi mật khẩu</div>
        <form class="form-content" method="post" action="change-password">
            <div class="form-group">
                <label>Mật khẩu hiện tại</label>
                <input type="password" name="oldPassword" required>
            </div>
            <div class="form-group">
                <label>Mật khẩu mới</label>
                <input type="password" name="newPassword" minlength="6" required>
            </div>
            <div class="form-group">
                <label>Nhập lại mật khẩu mới</label>
                <input type="password" name="confirmPassword" minlength="6" required>
            </div>
            <div class="actions">
                <button class="btn btn-primary" type="submit">Cập nhật</button>
                <a class="btn btn-secondary" href="profile">Về hồ sơ</a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../layout/footer.jsp" />


