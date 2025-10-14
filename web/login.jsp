<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng nhập - MovieNow</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <link rel="stylesheet" href="css/styles.css">
        <style>
            body {
                font-family: "Segoe UI", Arial, sans-serif;
                margin: 0;
                height: 100vh;
                overflow: auto;
                display: flex;
                justify-content: center;
                align-items: center;
                background: url('https://media.lottecinemavn.com/Media/WebAdmin/7ce48d93c8074902b0b5a6ba82e85351.jpg') no-repeat center center fixed;
                background-size: cover;
                backdrop-filter: blur(5px);
                -webkit-backdrop-filter: blur(5px);
            }
            .login-container {
                position: relative;
                z-index: 1;
                background: rgba(255, 255, 255, 0.9);
                padding: 20px;
                border-radius: 15px;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
                width: 100%;
                max-width: 400px;
                text-align: center;
            }
            .login-logo {
                display: flex;
                align-items: center;
                justify-content: center;
                margin-bottom: 20px;
            }
            .login-logo img {
                width: 50px;
                height: auto;
                border-radius: 10px;
                margin-right: 10px;
            }
            .login-logo span {
                font-weight: 700;
                font-size: 1.5rem;
                color: #075985;
                letter-spacing: 0.04em;
            }
            .login-card {
                padding: 20px;
            }
            .login-card h3 {
                color: #075985;
                margin-bottom: 20px;
                font-size: 24px;
            }
            .login-card .error-message {
                color: #dc3545;
                font-size: 14px;
                margin-bottom: 15px;
                padding: 10px;
                background: rgba(220, 53, 69, 0.1);
                border-radius: 5px;
                border-left: 4px solid #dc3545;
                display: ${not empty error ? 'block' : 'none'};
            }
            .login-card .form-group {
                margin-bottom: 15px;
                text-align: left;
                position: relative;
            }
            .login-card label {
                display: block;
                font-weight: 500;
                color: #333;
                margin-bottom: 5px;
                font-size: 14px;
            }
            .login-card input {
                width: 100%;
                padding: 10px 12px;
                border: 1px solid #ccc;
                border-radius: 5px;
                font-size: 14px;
                box-sizing: border-box;
            }
            .login-card input:focus {
                border-color: #075985;
                outline: none;
                box-shadow: 0 0 5px rgba(7, 89, 133, 0.3);
            }
            .login-card .password-toggle {
                position: absolute;
                right: 10px;
                top: 38px;
                cursor: pointer;
                color: #666;
                font-size: 16px;
            }
            .login-card .password-toggle:hover {
                color: #075985;
            }
            .login-card .remember-forgot {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
                font-size: 13px;
            }
            /* CHỈNH SỬA: Đảm bảo label "Ghi nhớ tài khoản" luôn hiển thị trên 1 dòng */
            .login-card .remember-me {
                display: flex;
                align-items: center;
                margin: 0;
            }
            .login-card .remember-me input {
                margin-right: 8px;
                margin-top: 0;
                vertical-align: middle;
            }
            .login-card .remember-me label {
                white-space: nowrap;
                margin: 0;
                font-size: 13px;
            }
            .login-card .forgot-password {
                color: #075985;
                text-decoration: none;
            }
            .login-card .forgot-password:hover {
                text-decoration: underline;
            }
            .login-card .buttons {
                margin-top: 10px;
            }
            .login-card button {
                width: 100%;
                padding: 12px;
                border: none;
                border-radius: 5px;
                font-weight: bold;
                cursor: pointer;
                transition: all 0.3s ease;
            }
            .login-card button[type="submit"] {
                background: linear-gradient(90deg, #075985, #1a3a7d);
                color: #fff;
            }
            .login-card button[type="submit"]:hover {
                background: linear-gradient(90deg, #1a3a7d, #0f2c5e);
            }
            .login-card button[type="button"] {
                background: #ccc;
                color: #333;
                margin-top: 10px;
            }
            .login-card button[type="button"]:hover {
                background: #bbb;
            }
            .login-card .register-link {
                font-size: 13px;
                color: #075985;
                text-decoration: none;
                display: block;
                margin-top: 15px;
                padding: 8px 0;
                background: rgba(7, 89, 133, 0.1);
                border-radius: 5px;
            }
            .login-card .register-link:hover {
                background: rgba(7, 89, 133, 0.2);
                text-decoration: underline;
            }
            @media (max-width: 480px) {
                .login-container {
                    padding: 10px;
                    margin: 10px;
                }
                .login-logo img {
                    width: 40px;
                }
                .login-logo span {
                    font-size: 1.2rem;
                }
            }
        </style>
    </head>
    <body>
        <div class="login-container">
            <div class="login-logo">
                <img src="https://png.pngtree.com/element_origin_min_pic/16/12/04/906aae676011fbcc70e96932704830e3.jpg" alt="Logo">
                <span>MovieNow</span>
            </div>
            <div class="login-card">
                <h3><i class="fas fa-sign-in-alt"></i> Đăng nhập</h3>
                <c:if test="${not empty error}">
                    <div class="error-message">${error}</div>
                </c:if>
                <form action="LoginServlet" method="post">
                    <div class="form-group">
                        <label for="username">Tên đăng nhập:</label>
                        <input type="text" id="username" name="username" value="${param.username != null ? param.username : ''}" required placeholder="Tên, email hoặc sdt">
                    </div>
                    <div class="form-group">
                        <label for="password">Mật khẩu:</label>
                        <input type="password" id="password" name="password" required placeholder="Nhập mật khẩu">
                        <span class="password-toggle" id="togglePassword">👁</span>
                    </div>
                    <div class="remember-forgot">
                        <div class="remember-me">
                            <input type="checkbox" id="rememberMe" ${not empty cookie.username ? 'checked' : ''}>
                            <label for="rememberMe">Ghi nhớ tài khoản</label>
                        </div>
                        <a href="ForgotPassword.jsp" class="forgot-password">Quên mật khẩu?</a>
                    </div>
                    <div class="buttons">
                        <button type="submit">Đăng nhập</button>
                        <button type="button" onclick="window.location.href='home.jsp'">Hủy</button>
                    </div>
                    <a href="Register.jsp" class="register-link">Đăng ký ngay</a>
                </form>
            </div>
        </div>
        <script>
            // Load saved credentials if "Remember Me" was checked
            document.addEventListener('DOMContentLoaded', () => {
                const username = localStorage.getItem('rememberedUsername');
                const password = localStorage.getItem('rememberedPassword');
                if (username && password) {
                    document.getElementById('username').value = username;
                    document.getElementById('password').value = password;
                    document.getElementById('rememberMe').checked = true;
                }

                const passwordInput = document.getElementById('password');
                const togglePassword = document.getElementById('togglePassword');
                togglePassword.addEventListener('click', () => {
                    if (passwordInput.type === 'password') {
                        passwordInput.type = 'text';
                        togglePassword.textContent = '🙈';
                    } else {
                        passwordInput.type = 'password';
                        togglePassword.textContent = '👁';
                    }
                });

                const form = document.querySelector('form');
                const loginBtn = document.querySelector('button[type="submit"]');
                form.addEventListener('submit', (e) => {
                    const username = form.querySelector('input[name="username"]').value.trim();
                    const password = form.querySelector('input[name="password"]').value.trim();
                    const rememberMe = document.getElementById('rememberMe').checked;
                    if (!username || !password) {
                        e.preventDefault();
                        alert('Vui lòng nhập đầy đủ thông tin!');
                        return false;
                    }
                    if (rememberMe) {
                        localStorage.setItem('rememberedUsername', username);
                        localStorage.setItem('rememberedPassword', password);
                    } else {
                        localStorage.removeItem('rememberedUsername');
                        localStorage.removeItem('rememberedPassword');
                    }
                    loginBtn.disabled = true;
                    loginBtn.textContent = 'ĐANG ĐĂNG NHẬP...';
                });
            });
        </script>
    </body>
</html>