<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="${sessionScope.lang == 'en' ? 'Change Password' : 'Đổi mật khẩu'}"/>
    <jsp:param name="extraStyles" value="
        <style>
            .page-screen { min-height: 100vh; display:flex; align-items:center; justify-content:center; padding:20px; background: var(--light-bg); }
            .form-container { background:#fff; width:100%; max-width:480px; border-radius:12px; box-shadow:0 10px 30px rgba(0,0,0,.08); overflow:hidden; }
            .form-header { background:#e50914; color:#fff; padding:18px 24px; font-weight:700; font-size:18px; }
            .form-content { padding: 24px; }
            .form-group { margin-bottom:14px; position:relative; }
            label { display:block; font-size:13px; color:#555; margin-bottom:6px; }
            input { width:100%; padding:10px 40px 10px 12px; border:1px solid #ddd; border-radius:6px; font-size:14px; }
            .toggle-password { position:absolute; right:12px; top:50%; transform:translateY(-50%); cursor:pointer; color:#555; font-size:16px; }
            .toggle-password:hover { color:#e50914; }
            .actions { display:flex; gap:12px; margin-top:8px; }
            .btn { padding:12px 16px; border:none; border-radius:6px; font-weight:700; cursor:pointer; }
            .btn-primary { background:#e50914; color:#fff; }
            .btn-secondary { background:#f0f0f0; color:#333; }
            .error-message { color: #e50914; font-size: 12px; margin-top: 4px; display: none; }
            .input-error { border-color: #e50914; }
            .server-message { padding: 10px; text-align: center; }
            .error { color: #e50914; }
            .success { color: #28a745; }
        </style>
    "/>
</jsp:include>

<div class="page-screen">
    <div class="form-container">
        <div class="form-header">${sessionScope.lang == 'en' ? 'Change Password' : 'Đổi mật khẩu'}</div>
        <c:if test="${not empty error}">
            <div class="server-message error">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="server-message success">${success}</div>
        </c:if>
        <form class="form-content" method="post" action="change-password" id="changePasswordForm">
            <div class="form-group">
                <label>${sessionScope.lang == 'en' ? 'Current Password' : 'Mật khẩu hiện tại'}</label>
                <input type="password" name="oldPassword" id="oldPassword" required>
                <span class="toggle-password" onclick="togglePassword('oldPassword')">&#128065;</span>
            </div>
            <div class="form-group">
                <label>${sessionScope.lang == 'en' ? 'New Password' : 'Mật khẩu mới'}</label>
                <input type="password" name="newPassword" id="newPassword" minlength="6" required>
                <span class="toggle-password" onclick="togglePassword('newPassword')">&#128065;</span>
                <div id="newPasswordError" class="error-message">
                    ${sessionScope.lang == 'en' ? 'New password must be different from the current password' : 'Mật khẩu mới phải khác mật khẩu hiện tại'}
                </div>
            </div>
            <div class="form-group">
                <label>${sessionScope.lang == 'en' ? 'Confirm New Password' : 'Nhập lại mật khẩu mới'}</label>
                <input type="password" name="confirmPassword" id="confirmPassword" minlength="6" required>
                <span class="toggle-password" onclick="togglePassword('confirmPassword')">&#128065;</span>
                <div id="confirmPasswordError" class="error-message">
                    ${sessionScope.lang == 'en' ? 'Confirm password does not match' : 'Mật khẩu nhập lại không khớp'}
                </div>
            </div>
            <div class="actions">
                <button class="btn btn-primary" type="submit">${sessionScope.lang == 'en' ? 'Update' : 'Cập nhật'}</button>
                <a class="btn btn-secondary" href="profile">${sessionScope.lang == 'en' ? 'Back to Profile' : 'Về hồ sơ'}</a>
            </div>
        </form>
    </div>
</div>

<script>
    function togglePassword(inputId) {
        const input = document.getElementById(inputId);
        const icon = input.nextElementSibling;
        if (input.type === 'password') {
            input.type = 'text';
            icon.textContent = '🙈';
        } else {
            input.type = 'password';
            icon.textContent = '👁️';
        }
    }

    document.addEventListener('DOMContentLoaded', function() {
        const form = document.getElementById('changePasswordForm');
        const oldPasswordInput = document.getElementById('oldPassword');
        const newPasswordInput = document.getElementById('newPassword');
        const confirmPasswordInput = document.getElementById('confirmPassword');
        const newPasswordError = document.getElementById('newPasswordError');
        const confirmPasswordError = document.getElementById('confirmPasswordError');

        form.addEventListener('submit', function(event) {
            let isValid = true;

            // Kiểm tra mật khẩu mới khác mật khẩu hiện tại
            if (newPasswordInput.value === oldPasswordInput.value) {
                newPasswordError.style.display = 'block';
                newPasswordInput.classList.add('input-error');
                isValid = false;
            } else {
                newPasswordError.style.display = 'none';
                newPasswordInput.classList.remove('input-error');
            }

            // Kiểm tra mật khẩu nhập lại khớp với mật khẩu mới
            if (newPasswordInput.value !== confirmPasswordInput.value) {
                confirmPasswordError.style.display = 'block';
                confirmPasswordInput.classList.add('input-error');
                isValid = false;
            } else {
                confirmPasswordError.style.display = 'none';
                confirmPasswordInput.classList.remove('input-error');
            }

            // Kiểm tra mật khẩu mạnh
            const strongPasswordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;
            if (!strongPasswordRegex.test(newPasswordInput.value)) {
                newPasswordError.textContent = '${sessionScope.lang == "en" ? "New password must contain uppercase, lowercase, number, and special character" : "Mật khẩu mới phải chứa chữ hoa, chữ thường, số và ký tự đặc biệt"}';
                newPasswordError.style.display = 'block';
                newPasswordInput.classList.add('input-error');
                isValid = false;
            }

            if (!isValid) {
                event.preventDefault();
            }
        });

        // Xóa thông báo lỗi khi người dùng nhập lại
        newPasswordInput.addEventListener('input', function() {
            newPasswordError.style.display = 'none';
            newPasswordInput.classList.remove('input-error');
        });

        confirmPasswordInput.addEventListener('input', function() {
            confirmPasswordError.style.display = 'none';
            confirmPasswordInput.classList.remove('input-error');
        });
    });
</script>

<jsp:include page="../layout/footer.jsp" />