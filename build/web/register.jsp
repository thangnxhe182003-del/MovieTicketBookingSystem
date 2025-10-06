<%-- 
    Document   : Register
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
                background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
                min-height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
                padding: 20px;
            }

            .register-container {
                background-color: white;
                width: 400px;
                border-radius: 12px;
                box-shadow: 0 5px 25px rgba(0, 0, 0, 0.1);
                overflow: hidden;
                animation: fadeIn 0.5s ease;
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
        </style>
    </head>
    <body>
        <div class="register-container">
            <div class="register-header">
                <div class="logo">
                    <img src="https://png.pngtree.com/element_origin_min_pic/16/12/04/906aae676011fbcc70e96932704830e3.jpg" alt="Logo">
                </div>
                <h1>Đăng ký tài khoản</h1>
                <p>Vui lòng điền thông tin để tạo tài khoản mới</p>
            </div>

            <form class="register-form" action="login.jsp" method="post">
                <div class="form-group">
                    <label for="email">Email hoặc Số điện thoại</label>
                    <input type="text" id="email" name="email" placeholder="Nhập email hoặc SĐT" required>
                </div>

                <div class="form-group">
                    <label for="username">Tên đăng nhập</label>
                    <input type="text" id="username" name="username" placeholder="Nhập tên đăng nhập" required>
                </div>

                <div class="form-group">
                    <label for="password">Mật khẩu</label>
                    <input type="password" id="password" name="password" placeholder="Nhập mật khẩu" required>
                    <span class="password-toggle" id="togglePassword">👁</span>
                </div>

                <div class="form-group">
                    <label for="confirm">Nhập lại mật khẩu</label>
                    <input type="password" id="confirm" name="confirm" placeholder="Xác nhận mật khẩu" required>
                    <span class="password-toggle" id="toggleConfirm">👁</span>
                </div>

                <!-- MÃ BẢO VỆ -->
                <div class="form-group">
                    <label for="captchaInput">Mã bảo vệ</label>
                    <div style="display: flex; align-items: center; gap: 10px;">
                        <div class="captcha-box" id="captchaCode">AB12X</div>
                        <button type="button" class="refresh-btn" id="refreshCaptcha">🔄</button>
                    </div>
                </div>

                <div class="form-group">
                    <input type="text" id="captchaInput" name="captchaInput" placeholder="Nhập mã bảo vệ ở trên" required>
                </div>

                <button type="submit" class="register-button">ĐĂNG KÝ</button>
            </form>

            <div class="register-footer">
                Đã có tài khoản? <a href="login.jsp">Đăng nhập ngay</a>
            </div>
        </div>

        <script>
            // Hiển thị / ẩn mật khẩu
            const togglePassword = document.getElementById('togglePassword');
            const toggleConfirm = document.getElementById('toggleConfirm');
            const passwordInput = document.getElementById('password');
            const confirmInput = document.getElementById('confirm');

            function toggleVisibility(input, toggleIcon) {
                const isVisible = input.type === 'text';
                input.type = isVisible ? 'password' : 'text';
                toggleIcon.textContent = isVisible ? '👁' : '🙈';
            }

            togglePassword.addEventListener('click', () => {
                toggleVisibility(passwordInput, togglePassword);
            });

            toggleConfirm.addEventListener('click', () => {
                toggleVisibility(confirmInput, toggleConfirm);
            });

            // Tạo mã bảo vệ ngẫu nhiên
            function generateCaptcha() {
                const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
                let captcha = '';
                for (let i = 0; i < 5; i++) {
                    captcha += chars.charAt(Math.floor(Math.random() * chars.length));
                }
                document.getElementById('captchaCode').textContent = captcha;
            }

            document.getElementById('refreshCaptcha').addEventListener('click', generateCaptcha);
            window.onload = generateCaptcha;
        </script>
    </body>
</html>
