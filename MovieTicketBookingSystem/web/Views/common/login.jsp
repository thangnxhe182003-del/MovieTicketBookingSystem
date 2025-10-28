<%-- 
    Document   : Login
    Created on : Oct 5, 2025, 12:35:35 PM
    Author     : thang
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Đăng nhập"/>
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
                max-width: 450px;
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
            }

            .form-group {
                margin-bottom: 20px;
                position: relative;
            }

            label {
                display: block;
                font-weight: 600;
                margin-bottom: 8px;
                color: #333;
                font-size: 14px;
            }

            input {
                width: 100%;
                padding: 12px 15px;
                border: 1px solid #ddd;
                border-radius: 6px;
                font-size: 14px;
                transition: all 0.3s;
            }

            input:focus {
                border-color: #e50914;
                box-shadow: 0 0 0 3px rgba(229, 9, 20, 0.1);
                outline: none;
            }

            input.error {
                border-color: #dc3545;
                background-color: #f8f9fa;
            }

            .error-message {
                color: #dc3545;
                font-size: 12px;
                margin-top: 5px;
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
                top: 38px;
                cursor: pointer;
                color: #999;
                font-size: 18px;
                user-select: none;
            }

            .password-toggle:hover {
                color: #333;
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
                gap: 6px;
            }

            .remember-me input {
                width: auto;
                cursor: pointer;
            }

            .forgot-password a {
                color: #e50914;
                text-decoration: none;
                transition: color 0.3s;
            }

            .forgot-password a:hover {
                text-decoration: underline;
            }

            .submit-button {
                width: 100%;
                padding: 12px;
                background-color: #e50914;
                color: white;
                border: none;
                border-radius: 6px;
                font-size: 16px;
                font-weight: 600;
                cursor: pointer;
                transition: 0.3s;
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
        <h1>🎬 Đăng nhập</h1>
        <p>Truy cập tài khoản MovieNow của bạn</p>
    </div>

    <form class="form-content" id="loginForm" action="login" method="post">
        <input type="hidden" name="next" value="${param.next}"/>
        <div class="form-group">
            <label for="username">Tên đăng nhập / Email / SĐT</label>
            <input type="text" id="username" name="username"
                   placeholder="Nhập tên đăng nhập hoặc email"
                   value="${param.username != null ? param.username : ''}"
                   required>
            <span class="error-message" id="usernameError">${requestScope.usernameError}</span>
        </div>

        <div class="form-group">
            <label for="password">Mật khẩu</label>
            <div class="password-wrapper">
                <input type="password" id="password" name="password"
                       placeholder="Nhập mật khẩu"
                       required>
                <span class="password-toggle" id="togglePassword">👁</span>
            </div>
            <span class="error-message" id="passwordError">${requestScope.passwordError}</span>
        </div>

        <div class="remember-forgot">
            <div class="remember-me">
                <input type="checkbox" id="remember" name="remember">
                <label for="remember">Ghi nhớ đăng nhập</label>
            </div>
            <div class="forgot-password">
                <a href="forgot-password">Quên mật khẩu?</a>
            </div>
        </div>

        <button type="submit" class="submit-button">Đăng nhập</button>
    </form>

    <div class="form-footer">
        <p>Bạn chưa có tài khoản? <a href="register">Đăng ký ngay</a></p>
    </div>
</div>

<%--<jsp:include page="../layout/footer.jsp" />--%>

</div>

<script>
    const passwordInput = document.getElementById('password');
    const togglePassword = document.getElementById('togglePassword');
    let passwordVisible = false;

    togglePassword.addEventListener('click', function() {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            passwordInput.type = 'text';
            togglePassword.textContent = '🙈';
        } else {
            passwordInput.type = 'password';
            togglePassword.textContent = '👁';
        }
    });

    // Show error messages if they exist
    const usernameError = document.getElementById('usernameError');
    const passwordError = document.getElementById('passwordError');

    if (usernameError && usernameError.textContent.trim()) {
        usernameError.classList.add('show');
        document.getElementById('username').classList.add('error');
    }

    if (passwordError && passwordError.textContent.trim()) {
        passwordError.classList.add('show');
        passwordInput.classList.add('error');
    }
</script>

<jsp:include page="../layout/footer.jsp" />