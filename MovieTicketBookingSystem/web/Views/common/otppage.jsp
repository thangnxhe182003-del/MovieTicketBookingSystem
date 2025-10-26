<%-- 
    Document   : otppage
    Created on : Oct 13, 2025, 8:55:47 PM
    Author     : thang
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Xác thực OTP"/>
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
            
            .otp-container {
                background-color: white;
                width: 100%;
                max-width: 450px;
                border-radius: 12px;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
                overflow: hidden;
                animation: fadeIn 0.5s ease-out;
                margin: 0 auto;
            }
            
            .otp-header {
                background-color: #4361ee;
                color: white;
                text-align: center;
                padding: 25px 0;
            }
            
            .otp-header h1 { font-size: 22px; margin-bottom: 5px; }
            .otp-header p { font-size: 14px; opacity: 0.9; }
            .otp-form { padding: 30px; }
            .form-group { margin-bottom: 18px; position: relative; }
            label { display: block; font-weight: 500; margin-bottom: 6px; color: #333; }
            input { width: 100%; padding: 12px; border: 1px solid #ccc; border-radius: 6px; font-size: 16px; text-align: center; letter-spacing: 10px; font-family: monospace; }
            input:focus { border-color: #4361ee; outline: none; box-shadow: 0 0 4px rgba(67, 97, 238, 0.3); }
            .error-message { color: #f72585; font-size: 13px; margin-top: 4px; }
            .verify-button { width: 100%; background-color: #4361ee; border: none; padding: 12px; border-radius: 6px; color: white; font-size: 16px; font-weight: 600; cursor: pointer; transition: 0.3s; }
            .verify-button:hover { background-color: #3f37c9; }
            .otp-footer { background-color: #f8f9fa; text-align: center; padding: 15px; font-size: 14px; }
            .otp-footer a { color: #4361ee; text-decoration: none; font-weight: 500; }
            .otp-footer a:hover { text-decoration: underline; }
            @keyframes fadeIn { from { opacity: 0; transform: translateY(15px);} to { opacity: 1; transform: translateY(0);} }
        </style>
    "/>
</jsp:include>

<div class="page-screen">
<div class="otp-container">
    <div class="otp-header">
        <h1>Xác thực OTP</h1>
        <p>Nhập mã OTP được gửi đến email của bạn</p>
    </div>
    <form class="otp-form" action="otpverify" method="POST">
        <% 
            String email = (String) request.getAttribute("email");
            String purpose = (String) request.getAttribute("purpose");
            if (purpose == null) {
                purpose = "REGISTER_VERIFY";
            }
        %>
        <input type="hidden" name="email" value="<%= email != null ? email : "" %>">
        <input type="hidden" name="purpose" value="<%= purpose %>">
        <div class="form-group">
            <label for="otp">Mã OTP (6 chữ số):</label>
            <input type="text" id="otp" name="otp" maxlength="6" placeholder="000000" required>
            <p class="error-message" id="otpError"></p>
        </div>
        <button type="submit" class="verify-button">Xác nhận</button>
    </form>
    <div class="otp-footer">
        <% if ("REGISTER_VERIFY".equals(purpose)) { %>
            <p>Không nhận được OTP? <a href="register">Đăng ký lại</a></p>
        <% } else if ("PASSWORD_RESET".equals(purpose)) { %>
            <p>Không nhận được OTP? <a href="forgot-password">Gửi lại</a></p>
        <% } %>
        <p>Đã có tài khoản? <a href="login">Đăng nhập</a></p>
    </div>
</div>
</div>

<jsp:include page="../layout/footer.jsp" />

<script>
    const otpInput = document.getElementById('otp');
    const otpError = document.getElementById('otpError');
    otpInput.addEventListener('input', (e) => { e.target.value = e.target.value.replace(/[^0-9]/g, ''); if (e.target.value.length === 6) { otpError.textContent = ''; } });
    otpInput.addEventListener('blur', () => { otpError.textContent = otpInput.value.length !== 6 ? 'Mã OTP phải là 6 chữ số' : ''; });
    document.querySelector('.otp-form').addEventListener('submit', (e) => { if (otpInput.value.length !== 6) { e.preventDefault(); otpError.textContent = 'Vui lòng nhập đúng 6 chữ số OTP'; } });
</script>