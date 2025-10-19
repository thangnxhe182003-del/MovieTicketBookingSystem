<%-- 
    Document   : register
    Created on : Oct 5, 2025, 1:13:04 PM
    Author     : thang
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Đăng ký tài khoản"/>
    <jsp:param name="extraStyles" value="
        <style>
            .page-screen {
                background:
                    linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)),
                    url('https://png.pngtree.com/thumb_back/fh260/background/20210902/pngtree-movie-festival-cinema-blockbuster-watching-background-image-image_785372.jpg');
                background-size: cover;
                background-position: center;
                background-attachment: fixed;
                min-height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
                padding: 20px;
            }
            
            .form-container {
                background-color: white;
                width: 100%;
                max-width: 550px;
                border-radius: 12px;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
                overflow: hidden;
                animation: fadeIn 0.5s ease-out;
                margin: 0 auto;
            }

            .form-header {
                background-color: #e50914;
                color: white;
                text-align: center;
                padding: 25px 0;
            }

            .form-header h1 {
                font-size: 28px;
                margin-bottom: 8px;
            }

            .form-header p {
                font-size: 14px;
                opacity: 0.9;
            }

            .form-content {
                padding: 30px;
                max-height: 600px;
                overflow-y: auto;
            }

            .form-row {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 15px;
            }

            .form-row.full {
                grid-template-columns: 1fr;
            }

            .form-group {
                margin-bottom: 18px;
                position: relative;
            }

            label {
                display: block;
                font-weight: 600;
                margin-bottom: 6px;
                color: #333;
                font-size: 13px;
            }

            label span {
                color: #e50914;
                margin-left: 2px;
                font-weight: bold;
            }

            input {
                width: 100%;
                padding: 10px 12px;
                border: 1px solid #ccc;
                border-radius: 6px;
                font-size: 14px;
                transition: border-color 0.3s;
            }

            input:focus {
                border-color: #e50914;
                outline: none;
                box-shadow: 0 0 4px rgba(229, 9, 20, 0.3);
            }

            .gender-group {
                display: flex;
                gap: 15px;
                margin-top: 8px;
            }

            .gender-option {
                display: flex;
                align-items: center;
                gap: 6px;
            }

            .gender-option input {
                width: auto;
                cursor: pointer;
            }

            .gender-option label {
                display: inline;
                margin: 0;
                font-weight: 400;
                cursor: pointer;
            }

            .error-message {
                color: #e50914;
                font-size: 12px;
                margin-top: 4px;
                display: none;
            }

            .error-message.show {
                display: block;
            }

            .password-wrapper {
                position: relative;
            }

            .password-toggle {
                position: absolute;
                right: 12px;
                top: 32px;
                cursor: pointer;
                font-size: 18px;
                color: #777;
                user-select: none;
            }

            .password-toggle:hover {
                color: #333;
            }

            .submit-button {
                width: 100%;
                background-color: #e50914;
                border: none;
                padding: 12px;
                border-radius: 6px;
                color: white;
                font-size: 16px;
                font-weight: 600;
                cursor: pointer;
                transition: 0.3s;
                margin-top: 10px;
            }

            .submit-button:hover {
                background-color: #cc0812;
            }

            .form-footer {
                background-color: #f8f9fa;
                text-align: center;
                padding: 15px;
                font-size: 14px;
            }

            .form-footer a {
                color: #e50914;
                text-decoration: none;
                font-weight: 500;
            }

            .form-footer a:hover {
                text-decoration: underline;
            }

            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: translateY(15px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }
        </style>
    "/>
</jsp:include>

<div class="page-screen">
<div class="form-container">
    <div class="form-header">
        <h1>🎫 Đăng ký tài khoản</h1>
        <p>Tạo tài khoản để đặt vé xem phim dễ dàng hơn</p>
    </div>

    <form class="form-content" id="registerForm" action="register" method="POST">
            <div class="form-row full">
                <div class="form-group">
                    <label for="hoten">Họ và tên <span class="required">*</span></label>
                    <input type="text" id="hoten" name="hoten" placeholder="Ví dụ: Nguyễn Văn A" required>
                    <span class="error-message" id="hotenError"></span>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="email">Email <span class="required">*</span></label>
                    <input type="email" id="email" name="email" placeholder="example@email.com" required>
                    <span class="error-message" id="emailError"></span>
                </div>
                <div class="form-group">
                    <label for="phone">Số điện thoại <span class="required">*</span></label>
                    <input type="tel" id="phone" name="phone" placeholder="0xxxxxxxxx" maxlength="10" required>
                    <span class="error-message" id="phoneError"></span>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="dob">Ngày sinh</label>
                    <input type="date" id="dob" name="dob">
                    <span class="error-message" id="dobError"></span>
                </div>
                <div class="form-group">
                    <label>Giới tính</label>
                    <div class="gender-group">
                        <div class="gender-option">
                            <input type="radio" id="male" name="gender" value="Nam">
                            <label for="male">Nam</label>
                        </div>
                        <div class="gender-option">
                            <input type="radio" id="female" name="gender" value="Nu">
                            <label for="female">Nữ</label>
                        </div>
                        <div class="gender-option">
                            <input type="radio" id="other" name="gender" value="Khac" checked>
                            <label for="other">Khác</label>
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-row full">
                <div class="form-group">
                    <label for="username">Tên đăng nhập <span class="required">*</span></label>
                    <input type="text" id="username" name="username" placeholder="Tên đăng nhập" required>
                    <span class="error-message" id="usernameError"></span>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="password">Mật khẩu <span class="required">*</span></label>
                    <div class="password-wrapper">
                        <input type="password" id="password" name="password" placeholder="Min 6 ký tự" minlength="6" required>
                        <span class="password-toggle" onclick="togglePassword('password')">👁</span>
                    </div>
                    <span class="error-message" id="passwordError"></span>
                </div>
                <div class="form-group">
                    <label for="confirm">Xác nhận mật khẩu <span class="required">*</span></label>
                    <div class="password-wrapper">
                        <input type="password" id="confirm" name="confirm" placeholder="Nhập lại mật khẩu" required>
                        <span class="password-toggle" onclick="togglePassword('confirm')">👁</span>
                    </div>
                    <span class="error-message" id="confirmError"></span>
                </div>
            </div>


        <button type="submit" class="submit-button">Đăng ký</button>
    </form>

    <div class="form-footer">
        <p>Đã có tài khoản? <a href="login">Đăng nhập ngay</a></p>
    </div>
</div>

<%--<jsp:include page="../layout/footer.jsp" />--%>

</div><script>
    function togglePassword(fieldId) {
        const field = document.getElementById(fieldId);
        const toggle = field.nextElementSibling;
        if (field.type === 'password') {
            field.type = 'text';
            toggle.textContent = '🙈';
        } else {
            field.type = 'password';
            toggle.textContent = '👁';
        }
    }

    // Validation
    const form = document.getElementById('registerForm');
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phoneRegex = /^0\d{9}$/;

    function validateForm() {
        let isValid = true;

        const hoten = document.getElementById('hoten').value.trim();
        const email = document.getElementById('email').value.trim();
        const phone = document.getElementById('phone').value.trim();
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value.trim();
        const confirm = document.getElementById('confirm').value.trim();

        if (!hoten) {
            document.getElementById('hotenError').textContent = 'Vui lòng nhập họ tên';
            document.getElementById('hotenError').classList.add('show');
            isValid = false;
        }

        if (!email || !emailRegex.test(email)) {
            document.getElementById('emailError').textContent = 'Email không hợp lệ';
            document.getElementById('emailError').classList.add('show');
            isValid = false;
        }

        if (!phone || !phoneRegex.test(phone)) {
            document.getElementById('phoneError').textContent = 'SĐT phải bắt đầu 0 và có 10 số';
            document.getElementById('phoneError').classList.add('show');
            isValid = false;
        }

        if (!username) {
            document.getElementById('usernameError').textContent = 'Vui lòng nhập tên đăng nhập';
            document.getElementById('usernameError').classList.add('show');
            isValid = false;
        }

        if (password.length < 6) {
            document.getElementById('passwordError').textContent = 'Mật khẩu phải ít nhất 6 ký tự';
            document.getElementById('passwordError').classList.add('show');
            isValid = false;
        }

        if (password !== confirm) {
            document.getElementById('confirmError').textContent = 'Mật khẩu không khớp';
            document.getElementById('confirmError').classList.add('show');
            isValid = false;
        }

        return isValid;
    }

    form.addEventListener('submit', (e) => {
        if (!validateForm()) {
            e.preventDefault();
        }
    });
</script>

<jsp:include page="../layout/footer.jsp" />