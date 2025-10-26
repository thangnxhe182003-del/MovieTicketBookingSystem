<%-- 
    Document   : reset-password
    Created on : Oct 15, 2025
    Author     : thang
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Đặt lại mật khẩu"/>
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
                background-color: #4361ee;
                color: white;
                text-align: center;
                padding: 25px 0;
            }
            
            .form-header .logo img {
                width: 70px;
                height: 70px;
                border-radius: 50%;
                margin-bottom: 10px;
            }
            
            .form-header h1 {
                font-size: 22px;
                margin-bottom: 5px;
            }
            
            .form-header p {
                font-size: 14px;
                opacity: 0.9;
            }
            
            .form-content {
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
                color: #f72585;
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
                border-color: #4361ee;
                outline: none;
                box-shadow: 0 0 4px rgba(67, 97, 238, 0.3);
            }
            
            .error-message {
                color: #f72585;
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
            
            .submit-button {
                width: 100%;
                background-color: #4361ee;
                border: none;
                padding: 12px;
                border-radius: 6px;
                color: white;
                font-size: 16px;
                font-weight: 600;
                cursor: pointer;
                transition: 0.3s;
            }
            
            .submit-button:hover {
                background-color: #3f37c9;
            }
            
            .form-footer {
                background-color: #f8f9fa;
                text-align: center;
                padding: 15px;
                font-size: 14px;
            }
            
            .form-footer a {
                color: #4361ee;
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
        <div class="logo">
            <img src="https://png.pngtree.com/element_origin_min_pic/16/12/04/906aae676011fbcc70e96932704830e3.jpg" alt="Logo" />
        </div>
        <h1>Đặt lại mật khẩu</h1>
        <p>Tạo mật khẩu mới cho tài khoản của bạn</p>
    </div>
    
    <form class="form-content" id="resetForm" action="reset-password" method="POST">
        <input type="hidden" name="maKH" value="${maKH}">
        
        <div class="form-group">
            <label for="password">Mật khẩu mới <span>*</span></label>
            <input type="password" id="password" name="password" placeholder="Nhập mật khẩu mới (ít nhất 6 ký tự)" minlength="6" required>
            <span class="password-toggle" onclick="togglePassword('password')">👁</span>
            <p class="error-message" id="passwordError"></p>
        </div>
        
        <div class="form-group">
            <label for="confirmPassword">Xác nhận mật khẩu <span>*</span></label>
            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Nhập lại mật khẩu mới" required>
            <span class="password-toggle" onclick="togglePassword('confirmPassword')">👁</span>
            <p class="error-message" id="confirmError"></p>
        </div>
        
        <button type="submit" class="submit-button">Đặt lại mật khẩu</button>
    </form>
    
    <div class="form-footer">
        <p>Đã nhớ mật khẩu? <a href="login">Đăng nhập ngay</a></p>
    </div>
</div>

<script>
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

    // Form validation
    const form = document.getElementById('resetForm');
    const passwordInput = document.getElementById('password');
    const confirmInput = document.getElementById('confirmPassword');
    const passwordError = document.getElementById('passwordError');
    const confirmError = document.getElementById('confirmError');

    function validatePassword() {
        const value = passwordInput.value.trim();
        passwordError.textContent = "";
        passwordInput.style.borderColor = "#ccc";
        if (value === "") {
            passwordError.textContent = "Vui lòng nhập mật khẩu";
            passwordInput.style.borderColor = "#f72585";
            return false;
        }
        if (value.length < 6) {
            passwordError.textContent = "Mật khẩu phải ít nhất 6 ký tự";
            passwordInput.style.borderColor = "#f72585";
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
            confirmInput.style.borderColor = "#f72585";
            return false;
        }
        if (password !== confirm) {
            confirmError.textContent = "Mật khẩu nhập lại không khớp";
            confirmInput.style.borderColor = "#f72585";
            return false;
        }
        return true;
    }

    // Event listeners
    passwordInput.addEventListener("blur", validatePassword);
    passwordInput.addEventListener("input", () => {
        passwordError.textContent = "";
        passwordInput.style.borderColor = "#ccc";
    });

    confirmInput.addEventListener("input", validateConfirmPassword);
    confirmInput.addEventListener("blur", validateConfirmPassword);

    // Form submit validation
    form.addEventListener("submit", (e) => {
        const validPassword = validatePassword();
        const validConfirm = validateConfirmPassword();

        if (!validPassword || !validConfirm) {
            e.preventDefault();
        }
    });
</script>

</div>

<jsp:include page="../layout/footer.jsp" />
