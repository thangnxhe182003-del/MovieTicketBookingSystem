<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Cập nhật thông tin"/>
    <jsp:param name="extraStyles" value="
        <style>
            .page-screen { min-height: 100vh; display:flex; align-items:center; justify-content:center; padding:20px; background: var(--light-bg); }
            .form-container { background:#fff; width:100%; max-width:600px; border-radius:12px; box-shadow:0 10px 30px rgba(0,0,0,.08); overflow:hidden; }
            .form-header { background:#e50914; color:#fff; padding:18px 24px; font-weight:700; font-size:18px; }
            .form-content { padding: 24px; }
            .form-row { display:grid; grid-template-columns:1fr 1fr; gap:16px; }
            .form-row.full { grid-template-columns:1fr; }
            .form-group { margin-bottom:14px; }
            label { display:block; font-size:13px; color:#555; margin-bottom:6px; }
            input, select { width:100%; padding:10px 12px; border:1px solid #ddd; border-radius:6px; font-size:14px; }
            .actions { display:flex; gap:12px; margin-top:8px; }
            .btn { padding:12px 16px; border:none; border-radius:6px; font-weight:700; cursor:pointer; }
            .btn-primary { background:#e50914; color:#fff; }
            .btn-secondary { background:#f0f0f0; color:#333; }
            .error-message { color: #e50914; font-size: 12px; margin-top: 4px; display: none; }
            .input-error { border-color: #e50914; }
        </style>
    "/>
</jsp:include>

<div class="page-screen">
    <div class="form-container">
        <div class="form-header">Cập nhật thông tin cá nhân</div>
        <form class="form-content" method="post" action="profile" id="profileForm">
            <div class="form-row full">
                <div class="form-group">
                    <label>Họ và tên</label>
                    <input type="text" name="hoten" value="${sessionScope.loggedInUser.hoTen}" required>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label>Ngày sinh</label>
                    <input type="date" name="dob" id="dob" value="${sessionScope.loggedInUser.ngaySinh}">
                    <div id="dobError" class="error-message">Ngày sinh không được lớn hơn ngày hiện tại</div>
                </div>
                <div class="form-group">
                    <label>Giới tính</label>
                    <select name="gender">
                        <option value="Nam" ${sessionScope.loggedInUser.gioiTinh == 'Nam' ? 'selected' : ''}>Nam</option>
                        <option value="Nu" ${sessionScope.loggedInUser.gioiTinh == 'Nu' ? 'selected' : ''}>Nữ</option>
                        <option value="Khac" ${sessionScope.loggedInUser.gioiTinh == 'Khac' ? 'selected' : ''}>Khác</option>
                    </select>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label>Số điện thoại</label>
                    <input type="tel" name="phone" id="phone" value="${sessionScope.loggedInUser.soDienThoai}" required>
                    <div id="phoneError" class="error-message">Số điện thoại phải có 10 chữ số và bắt đầu bằng 0</div>
                </div>
                <div class="form-group">
                    <label>Email</label>
                    <input type="email" name="email" value="${sessionScope.loggedInUser.email}" readonly>
                </div>
            </div>
            <div class="actions">
                <button class="btn btn-primary" type="submit">Lưu thay đổi</button>
                <a class="btn btn-secondary" href="change-password">Đổi mật khẩu</a>
            </div>
        </form>
    </div>
</div>

<script>
    // Thiết lập ngày tối đa cho input date là ngày hiện tại
    document.addEventListener('DOMContentLoaded', function() {
        const dobInput = document.getElementById('dob');
        const today = new Date().toISOString().split('T')[0];
        dobInput.setAttribute('max', today);

        const form = document.getElementById('profileForm');
        const phoneInput = document.getElementById('phone');
        const phoneError = document.getElementById('phoneError');
        const dobError = document.getElementById('dobError');

        // Hàm kiểm tra số điện thoại
        function validatePhone(phone) {
            const phoneRegex = /^0\d{9}$/;
            return phoneRegex.test(phone);
        }

        // Hàm kiểm tra ngày sinh
        function validateDob(dob) {
            const selectedDate = new Date(dob);
            const currentDate = new Date();
            return selectedDate <= currentDate;
        }

        form.addEventListener('submit', function(event) {
            let isValid = true;

            // Validate số điện thoại
            if (!validatePhone(phoneInput.value)) {
                phoneError.style.display = 'block';
                phoneInput.classList.add('input-error');
                isValid = false;
            } else {
                phoneError.style.display = 'none';
                phoneInput.classList.remove('input-error');
            }

            // Validate ngày sinh
            if (dobInput.value && !validateDob(dobInput.value)) {
                dobError.style.display = 'block';
                dobInput.classList.add('input-error');
                isValid = false;
            } else {
                dobError.style.display = 'none';
                dobInput.classList.remove('input-error');
            }

            if (!isValid) {
                event.preventDefault();
            }
        });

        // Xóa thông báo lỗi khi người dùng nhập lại
        phoneInput.addEventListener('input', function() {
            phoneError.style.display = 'none';
            phoneInput.classList.remove('input-error');
        });

        dobInput.addEventListener('input', function() {
            dobError.style.display = 'none';
            dobInput.classList.remove('input-error');
        });
    });
</script>

<jsp:include page="../layout/footer.jsp" />