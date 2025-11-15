<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="${slider != null ? 'Chỉnh sửa slider' : 'Thêm slider mới'}"/>
    <jsp:param name="pageSubtitle" value="${slider != null ? 'Cập nhật thông tin slider' : 'Tạo slider mới'}"/>
    <jsp:param name="currentPage" value="sliders"/>
    <jsp:param name="extraStyles" value="
        <style>
            .form-container {
                padding: 30px;
            }
            
            .form-header {
                background: #f8f9fa;
                padding: 20px 30px;
                border-bottom: 1px solid #e9ecef;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            
            .form-title {
                font-size: 18px;
                font-weight: 600;
                color: #333;
                margin: 0;
            }
            
            .form-content {
                padding: 30px;
            }
            
            .form-grid {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 30px;
                margin-bottom: 30px;
            }
            
            .form-group {
                margin-bottom: 20px;
            }
            
            .form-group.full-width {
                grid-column: 1 / -1;
            }
            
            .form-group label {
                display: block;
                font-weight: 600;
                color: #333;
                margin-bottom: 8px;
            }
            
            .form-group label .required {
                color: #e50914;
                margin-left: 4px;
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
            }
            
            .form-group input:focus,
            .form-group select:focus,
            .form-group textarea:focus {
                outline: none;
                border-color: #e50914;
                box-shadow: 0 0 0 3px rgba(229, 9, 20, 0.1);
            }
            
            .form-group textarea {
                resize: vertical;
                min-height: 100px;
            }
            
            .form-actions {
                display: flex;
                gap: 15px;
                justify-content: flex-end;
                padding-top: 20px;
                border-top: 1px solid #e9ecef;
            }
            
            .btn {
                padding: 12px 24px;
                border: none;
                border-radius: 6px;
                font-weight: 600;
                cursor: pointer;
                text-decoration: none;
                display: inline-flex;
                align-items: center;
                gap: 8px;
                transition: all 0.3s;
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
            }
            
            .image-preview {
                width: 300px;
                height: 150px;
                object-fit: cover;
                border-radius: 8px;
                border: 2px solid #e9ecef;
                margin-top: 10px;
            }
            
            .error-message {
                color: #dc3545;
                font-size: 12px;
                margin-top: 5px;
            }
            
            .help-text {
                color: #6c757d;
                font-size: 12px;
                margin-top: 5px;
            }
        </style>
    "/>
</jsp:include>

<div class="form-container">
    <!-- Form Header -->
    <div class="form-header">
        <h2 class="form-title">${slider != null ? 'Chỉnh sửa slider' : 'Thêm slider mới'}</h2>
        <a href="admin-sliders" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i>
            Quay lại
        </a>
    </div>

    <!-- Form Content -->
    <div class="form-content">
        <form id="sliderForm" method="POST" action="admin-sliders" enctype="multipart/form-data">
            <input type="hidden" name="action" value="${slider != null ? 'update' : 'create'}">
            <c:if test="${slider != null}">
                <input type="hidden" name="maSlider" value="${slider.maSlider}">
            </c:if>
            
            <div class="form-grid">
                <!-- Left Column -->
                <div>
                    <div class="form-group">
                        <label for="tieuDe">Tiêu đề <span class="required">*</span></label>
                        <input type="text" 
                               id="tieuDe" 
                               name="tieuDe" 
                               value="${slider != null ? slider.tieuDe : ''}"
                               placeholder="Nhập tiêu đề slider"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="thuTuHienThi">Thứ tự hiển thị <span class="required">*</span></label>
                        <input type="number" 
                               id="thuTuHienThi" 
                               name="thuTuHienThi" 
                               value="${slider != null ? slider.thuTuHienThi : '1'}"
                               placeholder="Nhập thứ tự hiển thị"
                               min="1"
                               required>
                        <div class="help-text">Số nhỏ hơn sẽ hiển thị trước</div>
                    </div>

                    <div class="form-group">
                        <label for="ngayBatDau">Ngày bắt đầu <span class="required">*</span></label>
                        <input type="datetime-local" 
                               id="ngayBatDau" 
                               name="ngayBatDau" 
                               value="${ngayBatDauFormatted != null ? ngayBatDauFormatted : ''}"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="ngayKetThuc">Ngày kết thúc</label>
                        <input type="datetime-local" 
                               id="ngayKetThuc" 
                               name="ngayKetThuc" 
                               value="${ngayKetThucFormatted != null ? ngayKetThucFormatted : ''}">
                        <div class="help-text">Để trống nếu không giới hạn thời gian</div>
                    </div>

                    <div class="form-group">
                        <label for="trangThai">Trạng thái <span class="required">*</span></label>
                        <select id="trangThai" name="trangThai" required>
                            <option value="">Chọn trạng thái</option>
                            <option value="Hiển thị" ${slider != null && slider.trangThai == 'Hiển thị' ? 'selected' : ''}>Hiển thị</option>
                            <option value="Ẩn" ${slider != null && slider.trangThai == 'Ẩn' ? 'selected' : ''}>Ẩn</option>
                        </select>
                    </div>
                </div>

                <!-- Right Column -->
                <div>
                    <div class="form-group">
                        <label for="anhSlideFile">Hình ảnh slider <span class="required">*</span></label>
                        <input type="file" 
                               id="anhSlideFile" 
                               name="anhSlideFile" 
                               accept="image/*"
                               onchange="previewImage(this)">
                        <div class="help-text">Chọn file ảnh (JPG, PNG, GIF) - Tối đa 5MB</div>
                        <c:if test="${slider != null}">
                            <div class="help-text" style="color: #17a2b8; margin-top: 5px;">
                                <i class="fas fa-info-circle"></i> Hình ảnh hiện tại: ${slider.anhSlide}
                            </div>
                        </c:if>
                    </div>

                    <div class="form-group">
                        <label>Xem trước hình ảnh</label>
                        <img id="imagePreview" 
                             class="image-preview" 
                             src="${slider != null ? pageContext.request.contextPath.concat('/assets/image/').concat(slider.anhSlide) : 'https://via.placeholder.com/300x150?text=No+Image'}"
                             alt="Preview"
                             onerror="this.src='https://via.placeholder.com/300x150?text=No+Image'">
                    </div>
                </div>

                <!-- Full Width -->
                <div class="form-group full-width">
                    <label for="moTa">Mô tả</label>
                    <textarea id="moTa" 
                              name="moTa" 
                              placeholder="Nhập mô tả về slider (tùy chọn)">${slider != null ? slider.moTa : ''}</textarea>
                </div>
            </div>

            <!-- Form Actions -->
            <div class="form-actions">
                <a href="admin-sliders" class="btn btn-secondary">
                    <i class="fas fa-times"></i>
                    Hủy
                </a>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i>
                    ${slider != null ? 'Cập nhật' : 'Tạo mới'}
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    // Image preview functionality
    function previewImage(input) {
        const preview = document.getElementById('imagePreview');
        
        if (input.files && input.files[0]) {
            const reader = new FileReader();
            
            reader.onload = function(e) {
                preview.src = e.target.result;
            };
            
            reader.readAsDataURL(input.files[0]);
        }
    }

    // Form validation
    document.getElementById('sliderForm').addEventListener('submit', function(e) {
        const tieuDe = document.getElementById('tieuDe').value.trim();
        const thuTuHienThi = document.getElementById('thuTuHienThi').value;
        const ngayBatDau = document.getElementById('ngayBatDau').value;
        const trangThai = document.getElementById('trangThai').value;
        const anhSlideFile = document.getElementById('anhSlideFile').files[0];
        const isEdit = ${slider != null ? 'true' : 'false'};

        if (!tieuDe || !thuTuHienThi || !ngayBatDau || !trangThai) {
            e.preventDefault();
            alert('Vui lòng điền đầy đủ thông tin bắt buộc');
            return;
        }

        if (!isEdit && !anhSlideFile) {
            e.preventDefault();
            alert('Vui lòng chọn hình ảnh slider');
            return;
        }

        if (parseInt(thuTuHienThi) < 1) {
            e.preventDefault();
            alert('Thứ tự hiển thị phải lớn hơn 0');
            return;
        }

        // Validate ngày
        const ngayBatDauDate = new Date(ngayBatDau);
        const ngayKetThuc = document.getElementById('ngayKetThuc').value;
        if (ngayKetThuc) {
            const ngayKetThucDate = new Date(ngayKetThuc);
            if (ngayKetThucDate <= ngayBatDauDate) {
                e.preventDefault();
                alert('Ngày kết thúc phải sau ngày bắt đầu');
                return;
            }
        }

        if (anhSlideFile && anhSlideFile.size > 5 * 1024 * 1024) {
            e.preventDefault();
            alert('Kích thước file không được vượt quá 5MB');
            return;
        }
    });
</script>

<jsp:include page="../layout/admin-footer.jsp" />

