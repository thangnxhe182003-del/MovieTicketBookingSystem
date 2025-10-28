<%-- 
    Document   : Login
    Created on : Oct 5, 2025, 12:35:35 PM
    Author     : thang
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng nhập</title>
        <style>
            :root {
                --primary-color: #4361ee;
                --secondary-color: #3f37c9;
                --accent-color: #4895ef;
                --error-color: #f72585;
                --light-color: #f8f9fa;
                --dark-color: #212529;
                --success-color: #4cc9f0;
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


            .login-container {
                background-color: white;
                width: 100%;
                max-width: 450px;
                border-radius: 12px;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
                overflow: hidden;
                animation: fadeIn 0.5s ease-out;
            }

            .login-header {
                background-color: var(--primary-color);
                color: white;
                padding: 25px;
                text-align: center;
            }

            .login-header h1 {
                font-size: 24px;
                font-weight: 600;
                margin-bottom: 5px;
            }

            .login-header p {
                opacity: 0.9;
                font-size: 14px;
            }

            .login-form {
                padding: 30px;
            }

            .form-group {
                margin-bottom: 20px;
                position: relative;
            }

            .form-group label {
                display: block;
                margin-bottom: 8px;
                font-weight: 500;
                color: var(--dark-color);
                font-size: 14px;
            }

            .form-group input {
                width: 100%;
                padding: 12px 15px;
                border: 1px solid #ddd;
                border-radius: 6px;
                font-size: 14px;
                transition: all 0.3s;
            }

            .form-group input:focus {
                border-color: var(--accent-color);
                box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.1);
                outline: none;
            }

            .password-toggle {
                position: absolute;
                right: 12px;
                top: 38px;
                cursor: pointer;
                color: #777;
            }

            .password-toggle:hover {
                color: var(--dark-color);
            }

            .remember-forgot {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
                font-size: 13px;
            }

            .remember-me {
                display: flex;
                align-items: center;
            }

            .remember-me input {
                margin-right: 8px;
            }

            .forgot-password a {
                color: var(--accent-color);
                text-decoration: none;
            }

            .forgot-password a:hover {
                text-decoration: underline;
            }

            .login-button {
                width: 100%;
                padding: 13px;
                background-color: var(--primary-color);
                color: white;
                border: none;
                border-radius: 6px;
                font-size: 16px;
                font-weight: 500;
                cursor: pointer;
                transition: all 0.3s;
            }

            .login-button:hover {
                background-color: var(--secondary-color);
            }

            .login-footer {
                text-align: center;
                padding: 20px;
                font-size: 13px;
                color: #666;
                border-top: 1px solid #eee;
            }

            .login-footer a {
                color: var(--primary-color);
                text-decoration: none;
            }

            .login-footer a:hover {
                text-decoration: underline;
            }

            .error-message {
                color: var(--error-color);
                font-size: 12px;
                margin-top: 5px;
                display: none;
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
                    transform: translateY(20px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            @media (max-width: 480px) {
                .login-container {
                    border-radius: 0;
                }

                .login-header {
                    padding: 20px;
                }

                .login-form {
                    padding: 20px;
                }
            }

            /* Logo styling */
            .logo {
                margin-bottom: 10px;
            }

            .logo img {
                width: 70px;
                height: 70px;
                border-radius: 50%;
                background-color: white;
            }
        </style>
    </head>
    <body>
        <div class="login-container">
            <div class="login-header">
                <div class="logo">
                    <img src="https://png.pngtree.com/element_origin_min_pic/16/12/04/906aae676011fbcc70e96932704830e3.jpg" alt="Logo" />
                </div>
                <h1>Đăng nhập</h1>
                <p>Hãy nhập thông tin đăng nhập của bạn</p>
            </div>

            <form class="login-form" id="loginForm" action="Trangchudadangnhap.jsp" method="post">
                <div class="form-group">
                    <label for="username">Tên đăng nhập</label>
                    <input type="text" id="username" name="username"
                           placeholder="Tên, email hoặc sdt"
                           value="${username != null ? username : ''}" required>
                    <span class="error-message" id="username-error"
                          style="${usernameError != null ? 'display:block;' : ''}">${usernameError}</span>

                </div>

                <div class="form-group">
                    <label for="password">Mật khẩu</label>
                    <input type="password" id="password" name="password"
                           placeholder="Nhập mật khẩu" required>
                    <span class="password-toggle" id="togglePassword">👁</span>
                    <span class="error-message" id="password-error"
                          style="${passwordError != null ? 'display:block;' : ''}">${passwordError}</span>

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

                <div class="remember-forgot">
                    <div class="remember-me">
                        <input type="checkbox" id="remember">
                        <label for="remember">Ghi nhớ đăng nhập</label>
                    </div>
                    <div class="forgot-password">
                        <a href="#">Quên mật khẩu?</a>
                    </div>
                </div>

                <button type="submit" class="login-button">ĐĂNG NHẬP</button>
            </form>

            <div class="login-footer">
                Bạn chưa có tài khoản <a href="register.jsp">Đăng ký ngay</a>
            </div>
        </div>
        <script>
            const passwordInput = document.getElementById('password');
            const togglePassword = document.getElementById('togglePassword');
            let passwordVisible = false;

            togglePassword.addEventListener('click', function () {
                passwordVisible = !passwordVisible;
                if (passwordVisible) {
                    passwordInput.type = 'text';
                    togglePassword.textContent = '🙈'; // đổi biểu tượng khi đang hiển thị
                } else {
                    passwordInput.type = 'password';
                    togglePassword.textContent = '👁'; // đổi lại biểu tượng ban đầu
                }
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
