<%-- 
    Document   : otppage
    Created on : Oct 13, 2025, 8:55:47 PM
    Author     : thang
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Xác thực OTP</title>
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

            .otp-container {
                background-color: white;
                width: 100%;
                max-width: 450px;
                border-radius: 12px;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
                overflow: hidden;
                animation: fadeIn 0.5s ease-out;
            }

            .otp-header {
                background-color: var(--primary-color);
                color: white;
                text-align: center;
                padding: 25px 0;
            }

            .otp-header h1 {
                font-size: 22px;
                margin-bottom: 5px;
            }

            .otp-header p {
                font-size: 14px;
                opacity: 0.9;
            }

            .otp-form {
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
                padding: 12px;
                border: 1px solid #ccc;
                border-radius: 6px;
                font-size: 16px;
                text-align: center;
                letter-spacing: 10px;
                font-family: monospace;
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

            .success-message {
                color: green;
                font-size: 14px;
                margin-bottom: 10px;
            }

            .warning-message {
                color: orange;
                font-size: 14px;
                margin-bottom: 10px;
            }

            .verify-button {
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

            .verify-button:hover {
                background-color: var(--secondary-color);
            }

            .otp-footer {
                background-color: var(--light-color);
                text-align: center;
                padding: 15px;
                font-size: 14px;
            }

            .otp-footer a {
                color: var(--primary-color);
                text-decoration: none;
                font-weight: 500;
            }

            .otp-footer a:hover {
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
    </head>
    <body>
        <div class="otp-container">
            <div class="otp-header">
                <h1>Xác thực OTP</h1>
                <p>Nhập mã OTP được gửi đến email của bạn</p>
            </div>
            <form class="otp-form" action="otpverify" method="POST">
                <% 
                    String success = (String) request.getAttribute("success");
                    String warning = (String) request.getAttribute("warning");
                    String error = (String) request.getAttribute("error");
                    String email = (String) request.getAttribute("email");
                %>
                <% if (success != null) { %>
                <p class="success-message"><%= success %></p>
                <% } %>
                <% if (warning != null) { %>
                <p class="warning-message"><%= warning %></p>
                <% } %>
                <% if (error != null) { %>
                <p class="error-message"><%= error %></p>
                <% } %>

                <input type="hidden" name="email" value="<%= email != null ? email : "" %>">

                <div class="form-group">
                    <label for="otp">Mã OTP (6 chữ số):</label>
                    <input type="text" id="otp" name="otp" maxlength="6" placeholder="000000" required>
                    <p class="error-message" id="otpError"></p>
                </div>

                <button type="submit" class="verify-button">Xác nhận</button>
            </form>
            <div class="otp-footer">
                <p>Không nhận được OTP? <a href="register">Gửi lại</a> hoặc <a href="register">Đăng ký lại</a></p>
            </div>
        </div>

        <script>
            const otpInput = document.getElementById('otp');
            const otpError = document.getElementById('otpError');

            otpInput.addEventListener('input', (e) => {
                // Chỉ cho phép số
                e.target.value = e.target.value.replace(/[^0-9]/g, '');
                if (e.target.value.length === 6) {
                    otpError.textContent = '';
                }
            });

            // Validate on blur
            otpInput.addEventListener('blur', () => {
                if (otpInput.value.length !== 6) {
                    otpError.textContent = 'Mã OTP phải là 6 chữ số';
                } else {
                    otpError.textContent = '';
                }
            });

            // Form submit validation
            document.querySelector('.otp-form').addEventListener('submit', (e) => {
                if (otpInput.value.length !== 6) {
                    e.preventDefault();
                    otpError.textContent = 'Vui lòng nhập đúng 6 chữ số OTP';
                }
            });
        </script>
    </body>
</html>
