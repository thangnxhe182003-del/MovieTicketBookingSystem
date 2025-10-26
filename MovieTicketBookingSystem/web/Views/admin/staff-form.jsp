<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="${not empty staff ? 'Sửa nhân viên' : 'Thêm nhân viên mới'}"/>
    <jsp:param name="pageSubtitle" value="${not empty staff ? 'Cập nhật thông tin nhân viên' : 'Thêm nhân viên mới vào hệ thống'}"/>
    <jsp:param name="currentPage" value="staff"/>
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
            
            .position-info {
                background: #f8f9fa;
                padding: 15px;
                border-radius: 6px;
                margin-bottom: 20px;
                border-left: 4px solid #e50914;
            }
            
            .position-info h5 {
                margin: 0 0 10px 0;
                color: #e50914;
            }
            
            .position-info ul {
                margin: 0;
                padding-left: 20px;
                font-size: 13px;
                color: #666;
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
    <a href="admin-staff" class="back-btn">← Quay lại danh sách</a>
</div>

<!-- FORM CONTAINER -->
<div class="form-container">
    <!-- FORM INFO -->
    <div class="form-info">
        <h4>👔 Thông tin nhân viên</h4>
        <p>
            <c:choose>
                <c:when test="${not empty staff}">
                    Cập nhật thông tin nhân viên "${staff.hoTen}"
                </c:when>
                <c:otherwise>
                    Nhập đầy đủ thông tin để tạo nhân viên mới
                </c:otherwise>
            </c:choose>
        </p>
    </div>

    <!-- FORM -->
    <form method="POST" action="admin-staff" id="staffForm" class="form-content">
        <input type="hidden" name="action" value="${not empty staff ? 'update' : 'add'}">
        <c:if test="${not empty staff}">
            <input type="hidden" name="maNhanVien" value="${staff.maNhanVien}">
        </c:if>
        
        <!-- Thông tin cơ bản -->
        <div class="form-row">
            <div class="form-group">
                <label for="maRap">Rạp phim <span class="required">*</span></label>
                <select id="maRap" name="maRap" required>
                    <option value="">-- Chọn rạp phim --</option>
                    <c:forEach var="cinema" items="${cinemas}">
                        <option value="${cinema.maRap}" ${staff.maRap == cinema.maRap ? 'selected' : ''}>
                            ${cinema.tenRap}
                        </option>
                    </c:forEach>
                </select>
                <div class="help-text">Chọn rạp phim làm việc</div>
            </div>
            
            <div class="form-group">
                <label for="hoTen">Họ và tên <span class="required">*</span></label>
                <input type="text" 
                       id="hoTen" 
                       name="hoTen" 
                       value="${staff.hoTen}"
                       placeholder="Nhập họ và tên đầy đủ"
                       required>
                <div class="help-text">Tên hiển thị của nhân viên</div>
            </div>
        </div>
        
        <div class="form-row">
            <div class="form-group">
                <label for="tenDangNhap">Tên đăng nhập <span class="required">*</span></label>
                <input type="text" 
                       id="tenDangNhap" 
                       name="tenDangNhap" 
                       value="${staff.tenDangNhap}"
                       placeholder="Nhập tên đăng nhập"
                       required>
                <div class="help-text">Tên đăng nhập duy nhất</div>
            </div>
            
            <div class="form-group">
                <label for="chucVu">Chức vụ <span class="required">*</span></label>
                <select id="chucVu" name="chucVu" required>
                    <option value="">-- Chọn chức vụ --</option>
                    <option value="Staff" ${staff.chucVu == 'Staff' ? 'selected' : ''}>👤 Staff - Nhân viên</option>
                    <option value="Manager" ${staff.chucVu == 'Manager' ? 'selected' : ''}>👔 Manager - Quản lý</option>
                    <option value="Admin" ${staff.chucVu == 'Admin' ? 'selected' : ''}>👑 Admin - Quản trị viên</option>
                </select>
                <div class="help-text">Phân quyền và chức vụ trong hệ thống</div>
            </div>
        </div>
        
        <div class="form-row">
            <div class="form-group">
                <label for="matKhau">Mật khẩu <span class="required">*</span></label>
                <input type="password" 
                       id="matKhau" 
                       name="matKhau" 
                       value="${staff.matKhau}"
                       placeholder="Nhập mật khẩu"
                       required>
                <div class="help-text">Mật khẩu tối thiểu 6 ký tự</div>
            </div>
            
            <div class="form-group">
                <label for="email">Email <span class="required">*</span></label>
                <input type="email" 
                       id="email" 
                       name="email" 
                       value="${staff.email}"
                       placeholder="Nhập địa chỉ email"
                       required>
                <div class="help-text">Email để liên hệ</div>
            </div>
        </div>
        
        <div class="form-row">
            <div class="form-group">
                <label for="soDienThoai">Số điện thoại</label>
                <input type="tel" 
                       id="soDienThoai" 
                       name="soDienThoai" 
                       value="${staff.soDienThoai}"
                       placeholder="Nhập số điện thoại">
                <div class="help-text">Số điện thoại liên hệ</div>
            </div>
            
            <div class="form-group">
                <label for="trangThai">Trạng thái</label>
                <select id="trangThai" name="trangThai">
                    <option value="Active" ${staff.trangThai == 'Active' ? 'selected' : ''}>Hoạt động</option>
                    <option value="Inactive" ${staff.trangThai == 'Inactive' ? 'selected' : ''}>Không hoạt động</option>
                    <option value="Suspended" ${staff.trangThai == 'Suspended' ? 'selected' : ''}>Tạm khóa</option>
                </select>
                <div class="help-text">Trạng thái tài khoản</div>
            </div>
        </div>
        
        <!-- Thông tin chức vụ -->
        <div class="position-info">
            <h5>📋 Thông tin phân quyền:</h5>
            <ul>
                <li><strong>Staff:</strong> Quản lý phim, suất chiếu, đặt vé</li>
                <li><strong>Manager:</strong> Quản lý nhân viên, báo cáo, cấu hình hệ thống</li>
                <li><strong>Admin:</strong> Toàn quyền quản trị hệ thống</li>
            </ul>
        </div>
        
        <!-- FORM ACTIONS -->
        <div class="form-actions">
            <a href="admin-staff" class="btn btn-secondary">Hủy bỏ</a>
            <button type="submit" class="btn btn-primary">
                ${not empty staff ? 'Cập nhật' : 'Thêm mới'}
            </button>
        </div>
    </form>
</div>

<script>
    // Form validation
    document.getElementById('staffForm').addEventListener('submit', function(e) {
        const maRap = document.getElementById('maRap').value.trim();
        const hoTen = document.getElementById('hoTen').value.trim();
        const tenDangNhap = document.getElementById('tenDangNhap').value.trim();
        const matKhau = document.getElementById('matKhau').value.trim();
        const email = document.getElementById('email').value.trim();
        const chucVu = document.getElementById('chucVu').value.trim();

        // Clear previous error messages
        document.querySelectorAll('.error-message').forEach(msg => msg.remove());

        let hasError = false;

        // Validate rạp phim
        if (maRap === '') {
            showError('maRap', 'Vui lòng chọn rạp phim');
            hasError = true;
        }

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

        // Validate mật khẩu
        if (matKhau === '') {
            showError('matKhau', 'Mật khẩu không được để trống');
            hasError = true;
        } else if (matKhau.length < 6) {
            showError('matKhau', 'Mật khẩu phải có ít nhất 6 ký tự');
            hasError = true;
        }

        // Validate email
        if (email === '') {
            showError('email', 'Email không được để trống');
            hasError = true;
        } else if (!isValidEmail(email)) {
            showError('email', 'Email không hợp lệ');
            hasError = true;
        }

        // Validate chức vụ
        if (chucVu === '') {
            showError('chucVu', 'Vui lòng chọn chức vụ');
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
