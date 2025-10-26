<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="${not empty cinema ? 'Sửa rạp phim' : 'Thêm rạp phim mới'}"/>
    <jsp:param name="pageSubtitle" value="${not empty cinema ? 'Cập nhật thông tin rạp phim' : 'Thêm rạp phim mới vào hệ thống'}"/>
    <jsp:param name="currentPage" value="cinema"/>
    <jsp:param name="extraStyles" value="
        <style>
            .admin-page {
                background: #f5f5f5;
                min-height: 100vh;
                padding: 20px 0;
            }
            
            .admin-container {
                max-width: 800px;
                margin: 0 auto;
                padding: 0 20px;
            }
            
            .page-header {
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                margin-bottom: 20px;
                display: flex;
                align-items: center;
                gap: 15px;
            }
            
            .back-btn {
                background: #6c757d;
                color: white;
                padding: 8px 16px;
                border: none;
                border-radius: 6px;
                text-decoration: none;
                font-weight: 600;
                transition: all 0.3s;
            }
            
            .back-btn:hover {
                background: #5a6268;
                transform: translateY(-1px);
            }
            
            .page-title {
                font-size: 24px;
                font-weight: bold;
                color: #333;
                margin: 0;
            }
            
            .form-container {
                background: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            }
            
            .form-group {
                margin-bottom: 20px;
            }
            
            .form-group label {
                display: block;
                font-weight: 600;
                color: #333;
                margin-bottom: 8px;
                font-size: 14px;
            }
            
            .form-group label .required {
                color: #e50914;
                margin-left: 3px;
            }
            
            .form-group input,
            .form-group select,
            .form-group textarea {
                width: 100%;
                padding: 12px 15px;
                border: 1px solid #ddd;
                border-radius: 6px;
                font-size: 14px;
                transition: border-color 0.3s;
                box-sizing: border-box;
            }
            
            .form-group input:focus,
            .form-group select:focus,
            .form-group textarea:focus {
                outline: none;
                border-color: #e50914;
                box-shadow: 0 0 4px rgba(229, 9, 20, 0.3);
            }
            
            .form-group textarea {
                resize: vertical;
                min-height: 80px;
            }
            
            .form-group .help-text {
                font-size: 12px;
                color: #666;
                margin-top: 5px;
            }
            
            .form-row {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 20px;
            }
            
            .form-actions {
                display: flex;
                gap: 15px;
                justify-content: flex-end;
                margin-top: 30px;
                padding-top: 20px;
                border-top: 1px solid #e0e0e0;
            }
            
            .btn {
                padding: 12px 24px;
                border: none;
                border-radius: 6px;
                font-size: 14px;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.3s;
                text-decoration: none;
                display: inline-block;
                text-align: center;
            }
            
            .btn-primary {
                background: linear-gradient(135deg, #e50914, #c90812);
                color: white;
            }
            
            .btn-primary:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(229, 9, 20, 0.4);
            }
            
            .btn-secondary {
                background: #6c757d;
                color: white;
            }
            
            .btn-secondary:hover {
                background: #5a6268;
                transform: translateY(-1px);
            }
            
            .error-message {
                color: #dc3545;
                font-size: 12px;
                margin-top: 5px;
                display: flex;
                align-items: center;
                gap: 5px;
            }
            
            .success-message {
                color: #28a745;
                font-size: 12px;
                margin-top: 5px;
                display: flex;
                align-items: center;
                gap: 5px;
            }
            
            .form-info {
                background: #e3f2fd;
                padding: 15px;
                border-radius: 6px;
                margin-bottom: 20px;
                border-left: 4px solid #2196f3;
            }
            
            .form-info h4 {
                margin: 0 0 8px 0;
                color: #1976d2;
                font-size: 14px;
            }
            
            .form-info p {
                margin: 0;
                color: #666;
                font-size: 13px;
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
    <a href="admin-cinema" class="back-btn">← Quay lại danh sách</a>
</div>

        <!-- FORM CONTAINER -->
        <div class="form-container">
            <!-- FORM INFO -->
            <div class="form-info">
                <h4>📋 Thông tin rạp phim</h4>
                <p>
                    <c:choose>
                        <c:when test="${not empty cinema}">
                            Cập nhật thông tin rạp phim "${cinema.tenRap}"
                        </c:when>
                        <c:otherwise>
                            Nhập đầy đủ thông tin để tạo rạp phim mới
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>

            <!-- FORM -->
            <form method="POST" action="admin-cinema" id="cinemaForm">
                <input type="hidden" name="action" value="${not empty cinema ? 'update' : 'add'}">
                <c:if test="${not empty cinema}">
                    <input type="hidden" name="maRap" value="${cinema.maRap}">
                </c:if>

                <!-- Tên rạp phim -->
                <div class="form-group">
                    <label for="tenRap">
                        Tên rạp phim <span class="required">*</span>
                    </label>
                    <input type="text" 
                           id="tenRap" 
                           name="tenRap" 
                           value="${cinema.tenRap}"
                           placeholder="Ví dụ: CGV Vincom Q1, Lotte Cinema Landmark..."
                           required>
                    <div class="help-text">Tên rạp phim phải là duy nhất trong hệ thống</div>
                </div>

                <!-- Địa chỉ -->
                <div class="form-group">
                    <label for="diaChi">
                        Địa chỉ <span class="required">*</span>
                    </label>
                    <textarea id="diaChi" 
                              name="diaChi" 
                              placeholder="Nhập địa chỉ chi tiết của rạp phim..."
                              required>${cinema.diaChi}</textarea>
                    <div class="help-text">Địa chỉ chi tiết giúp khách hàng dễ dàng tìm kiếm</div>
                </div>

                <!-- Khu vực -->
                <div class="form-group">
                    <label for="khuVuc">Khu vực</label>
                    <input type="text" 
                           id="khuVuc" 
                           name="khuVuc" 
                           value="${cinema.khuVuc}"
                           placeholder="Ví dụ: Quận 1, Quận 2, Thủ Đức, Bình Thạnh...">
                    <div class="help-text">Nhập tên khu vực hoặc để trống nếu chưa xác định</div>
                </div>

                <!-- FORM ACTIONS -->
                <div class="form-actions">
                    <a href="admin-cinema" class="btn btn-secondary">Hủy bỏ</a>
                    <button type="submit" class="btn btn-primary">
                        ${not empty cinema ? '💾 Cập nhật' : '➕ Thêm mới'}
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    // Form validation
    document.getElementById('cinemaForm').addEventListener('submit', function(e) {
        const tenRap = document.getElementById('tenRap').value.trim();
        const diaChi = document.getElementById('diaChi').value.trim();

        // Clear previous error messages
        document.querySelectorAll('.error-message').forEach(msg => msg.remove());

        let hasError = false;

        // Validate tên rạp phim
        if (tenRap === '') {
            showError('tenRap', 'Tên rạp phim không được để trống');
            hasError = true;
        } else if (tenRap.length < 3) {
            showError('tenRap', 'Tên rạp phim phải có ít nhất 3 ký tự');
            hasError = true;
        }

        // Validate địa chỉ
        if (diaChi === '') {
            showError('diaChi', 'Địa chỉ không được để trống');
            hasError = true;
        } else if (diaChi.length < 10) {
            showError('diaChi', 'Địa chỉ phải có ít nhất 10 ký tự');
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

    // Auto-resize textarea
    document.getElementById('diaChi').addEventListener('input', function() {
        this.style.height = 'auto';
        this.style.height = this.scrollHeight + 'px';
    });
</script>

</div>
</div>
