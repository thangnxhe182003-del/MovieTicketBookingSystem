<%-- 
    Document   : register
    Created on : Oct 5, 2025, 1:13:04 PM
    Author     : thang
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng ký tài khoản</title>
        <style>
            :root {
                --primary-color: #4361ee;
                --secondary-color: #3f37c9;
                --accent-color: #4895ef;
                --light-color: #f8f9fa;
                --error-color: #f72585;
            }

            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }

            body {
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

            .register-container {
                background-color: white;
                width: 100%;
                max-width: 450px;
                border-radius: 12px;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
                overflow: hidden;
                animation: fadeIn 0.5s ease-out;
            }

            .register-header {
                background-color: var(--primary-color);
                color: white;
                text-align: center;
                padding: 25px 0;
            }

            .register-header .logo img {
                width: 70px;
                height: 70px;
                border-radius: 50%;
                margin-bottom: 10px;
            }

            .register-header h1 {
                font-size: 22px;
                margin-bottom: 5px;
            }

            .register-header p {
                font-size: 14px;
                opacity: 0.9;
            }

            .register-form {
                padding: 30px;
            }

            .form-group {
                margin-bottom: 18px;
                position: relative;
            }

            label {
                display: block;
                font-weight: 500;
                margin-bottom: 6px;
                color: #333;
            }

            label span {
                color: var(--error-color);
                margin-left: 2px;
                font-weight: bold;
            }

            input {
                width: 100%;
                padding: 10px 40px 10px 12px;
                border: 1px solid #ccc;
                border-radius: 6px;
                font-size: 14px;
                transition: border-color 0.3s;
            }

            input:focus {
                border-color: var(--primary-color);
                outline: none;
                box-shadow: 0 0 4px rgba(67, 97, 238, 0.3);
            }

            .error-message {
                color: var(--error-color);
                font-size: 13px;
                margin-top: 4px;
            }

            .password-toggle {
                position: absolute;
                right: 12px;
                top: 36px;
                cursor: pointer;
                font-size: 18px;
                color: #777;
                user-select: none;
            }

            .register-button {
                width: 100%;
                background-color: var(--primary-color);
                border: none;
                padding: 12px;
                border-radius: 6px;
                color: white;
                font-size: 16px;
                font-weight: 600;
                cursor: pointer;
                transition: 0.3s;
            }

            .register-button:hover {
                background-color: var(--secondary-color);
            }

            .register-footer {
                background-color: var(--light-color);
                text-align: center;
                padding: 15px;
                font-size: 14px;
            }

            .register-footer a {
                color: var(--primary-color);
                text-decoration: none;
                font-weight: 500;
            }

            .register-footer a:hover {
                text-decoration: underline;
            }

            .captcha-box {
                display: flex;
                align-items: center;
                justify-content: space-between;
                background: #f2f2f2;
                border: 1px solid #ccc;
                border-radius: 6px;
                padding: 8px 12px;
                font-family: monospace;
                letter-spacing: 2px;
                font-size: 18px;
                font-weight: bold;
                user-select: none;
                margin-bottom: 8px;
            }

            .refresh-btn {
                background: none;
                border: none;
                font-size: 20px;
                cursor: pointer;
                color: var(--primary-color);
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

            .success-message, .error-message-global {
                padding: 10px;
                border-radius: 6px;
                margin-bottom: 15px;
                font-weight: 500;
            }

            .success-message {
                background-color: #d4edda;
                color: #155724;
                border: 1px solid #c3e6cb;
            }

            .error-message-global {
                background-color: #f8d7da;
                color: #721c24;
                border: 1px solid #f5c6cb;
            }
        </style>
    </head>
    <body>
        <div class="register-container">
            <div class="register-header">
                <div class="logo">
                    <img src="https://png.pngtree.com/element_origin_min_pic/16/12/04/906aae676011fbcc70e96932704830e3.jpg" alt="Logo" />
                </div>
                <h1>Đăng ký tài khoản</h1>
                <p>Tạo tài khoản để đặt vé xem phim dễ dàng</p>
            </div>
            <form class="register-form" id="registerForm" action="register" method="POST">
                <% 
                    String success = (String) request.getAttribute("success");
                    String error = (String) request.getAttribute("error");
                %>
                <% if (success != null) { %>
                <div class="success-message"><%= success %></div>
                <% } %>
                <% if (error != null) { %>
                <div class="error-message-global"><%= error %></div>
                <% } %>

                <div class="form-group">
                    <label for="hoten">Họ và tên : <span>*</span></label>
                    <input type="text" id="hoten" name="hoten" placeholder="Nhập họ và tên" required>
                    <p class="error-message" id="hotenError"></p>
                </div>

                <div class="form-group">
                    <label for="dob">Ngày sinh:</label>
                    <input type="date" id="dob" name="dob">
                    <p class="error-message" id="dobError"></p>
                </div>

                <div class="form-group">
                    <label>Giới tính:</label>
                    <input type="radio" id="male" name="gender" value="Nam">
                    <label for="male" style="display: inline; margin-right: 20px;">Nam</label>
                    <input type="radio" id="female" name="gender" value="Nu">
                    <label for="female" style="display: inline;">Nữ</label>
                    <input type="radio" id="other" name="gender" value="Khac" checked>
                    <label for="other" style="display: inline;">Khác</label>
                </div>

                <div class="form-group">
                    <label for="phone">Số điện thoại : <span>*</span></label>
                    <input type="tel" id="phone" name="phone" placeholder="0xxxxxxxxx" maxlength="10" required>
                    <p class="error-message" id="phoneError"></p>
                </div>

                <div class="form-group">
                    <label for="email">Email : <span>*</span></label>
                    <input type="email" id="email" name="email" placeholder="example@email.com" required>
                    <p class="error-message" id="emailError"></p>
                </div>

                <div class="form-group">
                    <label for="username">Tên đăng nhập : <span>*</span></label>
                    <input type="text" id="username" name="username" placeholder="Tên đăng nhập" required>
                    <p class="error-message" id="usernameError"></p>
                </div>

                <div class="form-group">
                    <label for="password">Mật khẩu : <span>*</span></label>
                    <input type="password" id="password" name="password" placeholder="Mật khẩu (ít nhất 6 ký tự)" minlength="6" required>
                    <span class="password-toggle" onclick="togglePassword('password')">👁</span>
                    <p class="error-message" id="passwordError"></p>
                </div>

                <div class="form-group">
                    <label for="confirm">Xác nhận mật khẩu : <span>*</span></label>
                    <input type="password" id="confirm" name="confirm" placeholder="Nhập lại mật khẩu" required>
                    <span class="password-toggle" onclick="togglePassword('confirm')">👁</span>
                    <p class="error-message" id="confirmError"></p>
                </div>

                <div class="form-group">
                    <label for="captcha">Mã bảo vệ : <span>*</span></label>
                    <div class="captcha-box">
                        <span id="captchaDisplay"></span>
                        <button type="button" class="refresh-btn" onclick="generateCaptcha()">↻</button>
                    </div>
                    <input type="text" id="captcha" name="captchaInput" placeholder="Nhập mã hiển thị" maxlength="6" required>
                    <input type="hidden" id="captchaHidden" name="captchaCode">
                    <p class="error-message" id="captchaError"></p>
                </div>

                <button type="submit" class="register-button">Đăng ký</button>
            </form>
            <div class="register-footer">
                <p>Đã có tài khoản? <a href="login.jsp">Đăng nhập ngay</a></p>
            </div>
        </div>

        <script>
            // Captcha logic
            let captchaCode = '';
            function generateCaptcha() {
                const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
                captchaCode = '';
                for (let i = 0; i < 6; i++) {
                    captchaCode += chars[Math.floor(Math.random() * chars.length)];
                }
                document.getElementById('captchaDisplay').textContent = captchaCode;
                document.getElementById('captchaHidden').value = captchaCode;
            }
            // Auto generate on load
            window.onload = function () {
                generateCaptcha();
            };

            // Password toggle
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

            // Validation functions (giữ nguyên và mở rộng)
            const form = document.getElementById('registerForm');
            const hotenInput = document.getElementById('hoten');
            const dobInput = document.getElementById('dob');
            const phoneInput = document.getElementById('phone');
            const emailInput = document.getElementById('email');
            const usernameInput = document.getElementById('username');
            const passwordInput = document.getElementById('password');
            const confirmInput = document.getElementById('confirm');
            const captchaInput = document.getElementById('captcha');
            const hotenError = document.getElementById('hotenError');
            const dobError = document.getElementById('dobError');
            const phoneError = document.getElementById('phoneError');
            const emailError = document.getElementById('emailError');
            const usernameError = document.getElementById('usernameError');
            const passwordError = document.getElementById('passwordError');
            const confirmError = document.getElementById('confirmError');
            const captchaError = document.getElementById('captchaError');

            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            const phoneRegex = /^0\d{9}$/;

            function validateHoten() {
                const value = hotenInput.value.trim();
                hotenError.textContent = "";
                hotenInput.style.borderColor = "#ccc";
                if (value === "") {
                    hotenError.textContent = "Vui lòng nhập họ và tên";
                    hotenInput.style.borderColor = "var(--error-color)";
                    return false;
                }
                return true;
            }

            function validateDob() {
                const value = dobInput.value;
                dobError.textContent = "";
                dobInput.style.borderColor = "#ccc";
                // Optional, nhưng nếu có thì check future date
                if (value && new Date(value) > new Date()) {
                    dobError.textContent = "Ngày sinh không được trong tương lai";
                    dobInput.style.borderColor = "var(--error-color)";
                    return false;
                }
                return true;
            }

            function validateEmail() {
                const value = emailInput.value.trim();
                emailError.textContent = "";
                emailInput.style.borderColor = "#ccc";
                if (value === "") {
                    emailError.textContent = "Vui lòng nhập email";
                    emailInput.style.borderColor = "var(--error-color)";
                    return false;
                }
                if (!emailRegex.test(value)) {
                    emailError.textContent = "Sai định dạng email";
                    emailInput.style.borderColor = "var(--error-color)";
                    return false;
                }
                return true;
            }

            function validatePhone() {
                const value = phoneInput.value.trim();
                phoneError.textContent = "";
                phoneInput.style.borderColor = "#ccc";
                if (value === "") {
                    phoneError.textContent = "Vui lòng nhập số điện thoại";
                    phoneInput.style.borderColor = "var(--error-color)";
                    return false;
                }
                if (!phoneRegex.test(value)) {
                    phoneError.textContent = "Sai định dạng số điện thoại (10 số bắt đầu bằng 0)";
                    phoneInput.style.borderColor = "var(--error-color)";
                    return false;
                }
                return true;
            }

            function validateUsername() {
                const value = usernameInput.value.trim();
                usernameError.textContent = "";
                usernameInput.style.borderColor = "#ccc";
                if (value === "") {
                    usernameError.textContent = "Vui lòng nhập tên đăng nhập";
                    usernameInput.style.borderColor = "var(--error-color)";
                    return false;
                }
                return true;
            }

            function validatePassword() {
                const value = passwordInput.value.trim();
                passwordError.textContent = "";
                passwordInput.style.borderColor = "#ccc";
                if (value === "") {
                    passwordError.textContent = "Vui lòng nhập mật khẩu";
                    passwordInput.style.borderColor = "var(--error-color)";
                    return false;
                }
                if (value.length < 6) {
                    passwordError.textContent = "Mật khẩu phải ít nhất 6 ký tự";
                    passwordInput.style.borderColor = "var(--error-color)";
                    return false;
                }
                return true;
            }

            function validateConfirmPassword() {
                const password = passwordInput.value.trim();
                const confirm = confirmInput.value.trim();
                confirmError.textContent = "";
                confirmInput.style.borderColor = "#ccc";
                if (confirm === "") {
                    confirmError.textContent = "Vui lòng nhập lại mật khẩu";
                    confirmInput.style.borderColor = "var(--error-color)";
                    return false;
                }
                if (password !== confirm) {
                    confirmError.textContent = "Mật khẩu nhập lại không khớp";
                    confirmInput.style.borderColor = "var(--error-color)";
                    return false;
                }
                return true;
            }

            function validateCaptcha() {
                const value = captchaInput.value.trim();
                captchaError.textContent = "";
                captchaInput.style.borderColor = "#ccc";
                if (value === "") {
                    captchaError.textContent = "Vui lòng nhập mã bảo vệ";
                    captchaInput.style.borderColor = "var(--error-color)";
                    return false;
                }
                if (value.toUpperCase() !== captchaCode) {
                    captchaError.textContent = "Mã bảo vệ không đúng";
                    captchaInput.style.borderColor = "var(--error-color)";
                    return false;
                }
                return true;
            }

            // Event listeners (thêm cho dob và captcha)
            hotenInput.addEventListener("blur", validateHoten);
            hotenInput.addEventListener("input", () => {
                hotenError.textContent = "";
                hotenInput.style.borderColor = "#ccc";
            });

            dobInput.addEventListener("blur", validateDob);
            dobInput.addEventListener("input", () => {
                dobError.textContent = "";
                dobInput.style.borderColor = "#ccc";
            });

            emailInput.addEventListener("blur", validateEmail);
            emailInput.addEventListener("input", () => {
                emailError.textContent = "";
                emailInput.style.borderColor = "#ccc";
            });

            phoneInput.addEventListener("blur", validatePhone);
            phoneInput.addEventListener("input", () => {
                phoneError.textContent = "";
                phoneInput.style.borderColor = "#ccc";
            });

            usernameInput.addEventListener("blur", validateUsername);
            usernameInput.addEventListener("input", () => {
                usernameError.textContent = "";
                usernameInput.style.borderColor = "#ccc";
            });

            passwordInput.addEventListener("blur", validatePassword);
            passwordInput.addEventListener("input", () => {
                passwordError.textContent = "";
                passwordInput.style.borderColor = "#ccc";
            });

            confirmInput.addEventListener("input", validateConfirmPassword);
            confirmInput.addEventListener("blur", validateConfirmPassword);

            captchaInput.addEventListener("blur", validateCaptcha);
            captchaInput.addEventListener("input", () => {
                captchaError.textContent = "";
                captchaInput.style.borderColor = "#ccc";
            });

            // Form submit validation
            form.addEventListener("submit", (e) => {
                const validHoten = validateHoten();
                const validDob = validateDob();
                const validEmail = validateEmail();
                const validPhone = validatePhone();
                const validUsername = validateUsername();
                const validPassword = validatePassword();
                const validConfirm = validateConfirmPassword();
                const validCaptcha = validateCaptcha();

                if (!validHoten || !validDob || !validEmail || !validPhone || !validUsername || !validPassword || !validConfirm || !validCaptcha) {
                    e.preventDefault();
                    alert("Vui lòng kiểm tra lại thông tin!");
                }
            });
        </script>
    </body>
</html>
