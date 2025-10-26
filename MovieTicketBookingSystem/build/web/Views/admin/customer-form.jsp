<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="${not empty customer ? 'Sửa khách hàng' : 'Thêm khách hàng mới'}"/>
    <jsp:param name="pageSubtitle" value="${not empty customer ? 'Cập nhật thông tin khách hàng' : 'Thêm khách hàng mới vào hệ thống'}"/>
    <jsp:param name="currentPage" value="customers"/>
    <jsp:param name="extraStyles" value="
        <style>
            .form-container {
                background: white;
                border-radius: 12px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                overflow: hidden;
            }
            
            .form-info {
                background: linear-gradient(135deg, #e50914, #cc0812);
                color: white;
                padding: 20px;
                text-align: center;
            }
            
            .form-info h4 {
                margin: 0 0 10px 0;
                font-size: 18px;
            }
            
            .form-info p {
                margin: 0;
                opacity: 0.9;
                font-size: 14px;
            }
            
            .form-content {
                padding: 30px;
            }
            
            .form-row {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 20px;
                margin-bottom: 20px;
            }
            
            .form-group {
                margin-bottom: 20px;
            }
            
            .form-group.full-width {
                grid-column: 1 / -1;
            }
            
            label {
                display: block;
                margin-bottom: 5px;
                font-weight: 600;
                color: #333;
            }
            
            label .required {
                color: #e50914;
                margin-left: 2px;
            }
            
            input, select, textarea {
                width: 100%;
                padding: 10px 12px;
                border: 1px solid #ddd;
                border-radius: 6px;
                font-size: 14px;
                transition: border-color 0.3s;
            }
            
            input:focus, select:focus, textarea:focus {
                border-color: #e50914;
                outline: none;
                box-shadow: 0 0 0 2px rgba(229, 9, 20, 0.1);
            }
            
            .help-text {
                font-size: 12px;
                color: #666;
                margin-top: 5px;
            }
            
            .info-box {
                background: #e3f2fd;
                border: 1px solid #bbdefb;
                border-radius: 6px;
                padding: 10px 12px;
                color: #1976d2;
                font-size: 13px;
                display: flex;
                align-items: center;
                gap: 8px;
            }
            
            .info-box i {
                color: #1976d2;
            }
            
            .error-message {
                color: #dc3545;
                font-size: 12px;
                margin-top: 5px;
            }
            
            .form-actions {
                display: flex;
                gap: 10px;
                justify-content: flex-end;
                margin-top: 30px;
                padding-top: 20px;
                border-top: 1px solid #eee;
            }
            
            .btn {
                padding: 10px 20px;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-weight: 500;
                text-decoration: none;
                display: inline-block;
                transition: all 0.3s;
            }
            
            .btn-primary {
                background: #e50914;
                color: white;
            }
            
            .btn-primary:hover {
                background: #cc0812;
            }
            
            .btn-secondary {
                background: #6c757d;
                color: white;
            }
            
            .btn-secondary:hover {
                background: #5a6268;
            }
            
            .back-btn {
                color: #e50914;
                text-decoration: none;
                font-weight: 500;
                display: inline-flex;
                align-items: center;
                gap: 5px;
                margin-bottom: 20px;
            }
            
            .back-btn:hover {
                text-decoration: underline;
            }
            
            .checkbox-group {
                display: flex;
                align-items: center;
                gap: 10px;
            }
            
            .checkbox-group input[type='checkbox'] {
                width: auto;
                margin: 0;
            }
            
            @media (max-width: 768px) {
                .form-row {
                    grid-template-columns: 1fr;
                }
                
                .form-actions {
                    flex-direction: column;
                }
                
                .btn {
                    width: 100%;
                }
            }
        </style>
    "/>
</jsp:include>

<!-- BACK BUTTON -->
<div style="margin-bottom: 20px;">
    <a href="admin-customers" class="back-btn">← Quay lại danh sách</a>
</div>

<!-- FORM CONTAINER -->
<div class="form-container">
    <!-- FORM INFO -->
    <div class="form-info">
        <h4>👤 Thông tin khách hàng</h4>
        <p>
            <c:choose>
                <c:when test="${not empty customer}">
                    Cập nhật thông tin khách hàng "${customer.hoTen}"
                </c:when>
                <c:otherwise>
                    Nhập đầy đủ thông tin để tạo khách hàng mới
                </c:otherwise>
            </c:choose>
        </p>
    </div>

    <!-- FORM -->
    <form method="POST" action="admin-customers" id="customerForm" class="form-content">
        <input type="hidden" name="action" value="${not empty customer ? 'update' : 'add'}">
        <c:if test="${not empty customer}">
            <input type="hidden" name="maKH" value="${customer.maKH}">
        </c:if>
        
        <!-- Thông tin cơ bản -->
        <div class="form-row">
            <div class="form-group">
                <label for="hoTen">Họ và tên <span class="required">*</span></label>
                <input type="text" 
                       id="hoTen" 
                       name="hoTen" 
                       value="${customer.hoTen}"
                       placeholder="Nhập họ và tên đầy đủ"
                       required>
                <div class="help-text">Tên hiển thị của khách hàng</div>
            </div>
            
            <div class="form-group">
                <label for="tenDangNhap">Tên đăng nhập <span class="required">*</span></label>
                <input type="text" 
                       id="tenDangNhap" 
                       name="tenDangNhap" 
                       value="${customer.tenDangNhap}"
                       placeholder="Nhập tên đăng nhập"
                       required>
                <div class="help-text">Tên đăng nhập duy nhất</div>
            </div>
        </div>
        
        <div class="form-row">
            <c:if test="${empty customer}">
                <!-- Chỉ hiển thị trường mật khẩu khi thêm mới -->
                <div class="form-group">
                    <label for="matKhau">Mật khẩu <span class="required">*</span></label>
                    <input type="password" 
                           id="matKhau" 
                           name="matKhau" 
                           value="${customer.matKhau}"
                           placeholder="Nhập mật khẩu"
                           required>
                    <div class="help-text">Mật khẩu tối thiểu 6 ký tự</div>
                </div>
            </c:if>
            <c:if test="${not empty customer}">
                <!-- Khi edit, hiển thị thông báo không cho phép thay đổi mật khẩu -->
                <div class="form-group">
                    <label>Mật khẩu</label>
                    <div class="info-box">
                        <i class="fas fa-info-circle"></i>
                        Mật khẩu không thể thay đổi từ đây. Khách hàng có thể đổi mật khẩu trong trang cá nhân.
                    </div>
                </div>
            </c:if>
            
            <div class="form-group">
                <label for="email">Email <span class="required">*</span></label>
                <input type="email" 
                       id="email" 
                       name="email" 
                       value="${customer.email}"
                       placeholder="Nhập địa chỉ email"
                       required>
                <div class="help-text">Email để liên hệ và xác thực</div>
            </div>
        </div>
        
        <div class="form-row">
            <div class="form-group">
                <label for="soDienThoai">Số điện thoại</label>
                <input type="tel" 
                       id="soDienThoai" 
                       name="soDienThoai" 
                       value="${customer.soDienThoai}"
                       placeholder="Nhập số điện thoại">
                <div class="help-text">Số điện thoại liên hệ</div>
            </div>
            
            <div class="form-group">
                <label for="gioiTinh">Giới tính</label>
                <select id="gioiTinh" name="gioiTinh">
                    <option value="">-- Chọn giới tính --</option>
                    <option value="Nam" ${customer.gioiTinh == 'Nam' ? 'selected' : ''}>Nam</option>
                    <option value="Nữ" ${customer.gioiTinh == 'Nữ' ? 'selected' : ''}>Nữ</option>
                    <option value="Khác" ${customer.gioiTinh == 'Khác' ? 'selected' : ''}>Khác</option>
                </select>
            </div>
        </div>
        
        <div class="form-row">
            <div class="form-group">
                <label for="ngaySinh">Ngày sinh</label>
                <input type="date" 
                       id="ngaySinh" 
                       name="ngaySinh" 
                       value="${customer.ngaySinh}">
                <div class="help-text">Ngày sinh của khách hàng</div>
            </div>
            
            <div class="form-group">
                <label for="trangThai">Trạng thái</label>
                <select id="trangThai" name="trangThai">
                    <option value="Pending" ${customer.trangThai == 'Pending' ? 'selected' : ''}>Chờ xác thực</option>
                    <option value="Active" ${customer.trangThai == 'Active' ? 'selected' : ''}>Hoạt động</option>
                    <option value="Suspended" ${customer.trangThai == 'Suspended' ? 'selected' : ''}>Tạm khóa</option>
                </select>
                <div class="help-text">Trạng thái tài khoản</div>
            </div>
        </div>
        
        <!-- Xác thực email (chỉ hiển thị khi sửa) -->
        <c:if test="${not empty customer}">
            <div class="form-group">
                <div class="checkbox-group">
                    <input type="checkbox" 
                           id="isEmailVerified" 
                           name="isEmailVerified" 
                           value="true"
                           ${customer.isEmailVerified ? 'checked' : ''}>
                    <label for="isEmailVerified">Email đã được xác thực</label>
                </div>
                <div class="help-text">Đánh dấu nếu email đã được xác thực</div>
            </div>
        </c:if>
        
        <!-- FORM ACTIONS -->
        <div class="form-actions">
            <a href="admin-customers" class="btn btn-secondary">Hủy bỏ</a>
            <button type="submit" class="btn btn-primary">
                ${not empty customer ? 'Cập nhật' : 'Thêm mới'}
            </button>
        </div>
    </form>
</div>

<script>
    // Form validation
    document.getElementById('customerForm').addEventListener('submit', function(e) {
        const hoTen = document.getElementById('hoTen').value.trim();
        const tenDangNhap = document.getElementById('tenDangNhap').value.trim();
        const matKhau = document.getElementById('matKhau').value.trim();
        const email = document.getElementById('email').value.trim();

        // Clear previous error messages
        document.querySelectorAll('.error-message').forEach(msg => msg.remove());

        let hasError = false;

        // Validate họ tên
        if (hoTen === '') {
            showError('hoTen', 'Họ tên không được để trống');
            hasError = true;
        } else if (hoTen.length < 2) {
            showError('hoTen', 'Họ tên phải có ít nhất 2 ký tự');
            hasError = true;
        }

        // Validate tên đăng nhập
        if (tenDangNhap === '') {
            showError('tenDangNhap', 'Tên đăng nhập không được để trống');
            hasError = true;
        } else if (tenDangNhap.length < 3) {
            showError('tenDangNhap', 'Tên đăng nhập phải có ít nhất 3 ký tự');
            hasError = true;
        }

        // Validate mật khẩu (chỉ khi thêm mới)
        const isEditMode = document.getElementById('matKhau') === null;
        if (!isEditMode) {
            if (matKhau === '') {
                showError('matKhau', 'Mật khẩu không được để trống');
                hasError = true;
            } else if (matKhau.length < 6) {
                showError('matKhau', 'Mật khẩu phải có ít nhất 6 ký tự');
                hasError = true;
            }
        }

        // Validate email
        if (email === '') {
            showError('email', 'Email không được để trống');
            hasError = true;
        } else if (!isValidEmail(email)) {
            showError('email', 'Email không hợp lệ');
            hasError = true;
        }

        if (hasError) {
            e.preventDefault();
        }
    });

    function showError(fieldId, message) {
        const field = document.getElementById(fieldId);
        const errorDiv = document.createElement('div');
        errorDiv.className = 'error-message';
        errorDiv.innerHTML = '⚠️ ' + message;
        field.parentNode.appendChild(errorDiv);
    }

    function isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }
</script>

</div>
</div>
