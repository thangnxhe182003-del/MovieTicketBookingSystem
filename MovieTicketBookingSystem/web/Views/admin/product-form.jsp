<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="${product != null ? 'Chỉnh sửa sản phẩm' : 'Thêm sản phẩm mới'}"/>
    <jsp:param name="pageSubtitle" value="${product != null ? 'Cập nhật thông tin sản phẩm' : 'Tạo sản phẩm combo đồ ăn mới'}"/>
    <jsp:param name="currentPage" value="products"/>
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
                width: 100px;
                height: 100px;
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
        <h2 class="form-title">${product != null ? 'Chỉnh sửa sản phẩm' : 'Thêm sản phẩm mới'}</h2>
        <a href="admin-products" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i>
            Quay lại
        </a>
    </div>

    <!-- Form Content -->
    <div class="form-content">
        <form id="productForm" method="POST" action="admin-products" enctype="multipart/form-data">
            <input type="hidden" name="action" value="${product != null ? 'update' : 'create'}">
            <c:if test="${product != null}">
                <input type="hidden" name="maSP" value="${product.maSP}">
            </c:if>
            
            <div class="form-grid">
                <!-- Left Column -->
                <div>
                    <div class="form-group">
                        <label for="tenSP">Tên sản phẩm <span class="required">*</span></label>
                        <input type="text" 
                               id="tenSP" 
                               name="tenSP" 
                               value="${product != null ? product.tenSP : ''}"
                               placeholder="Nhập tên sản phẩm"
                               required>
                        <div class="help-text">Tên combo đồ ăn, ví dụ: "Combo Bắp Nước Lớn"</div>
                    </div>

                    <div class="form-group">
                        <label for="donGia">Giá sản phẩm <span class="required">*</span></label>
                        <input type="number" 
                               id="donGia" 
                               name="donGia" 
                               value="${product != null ? product.donGia : ''}"
                               placeholder="Nhập giá sản phẩm"
                               min="0"
                               step="any"
                               required>
                        <div class="help-text">Giá tính bằng VNĐ (có thể nhập bất kỳ giá trị nào > 0)</div>
                    </div>

                    <div class="form-group">
                        <label for="trangThai">Trạng thái <span class="required">*</span></label>
                        <select id="trangThai" name="trangThai" required>
                            <option value="">Chọn trạng thái</option>
                            <option value="Active" ${product != null && product.trangThai == 'Active' ? 'selected' : ''}>Đang bán</option>
                            <option value="Không bán" ${product != null && product.trangThai == 'Không bán' ? 'selected' : ''}>Không bán</option>
                        </select>
                    </div>
                </div>

                <!-- Right Column -->
                <div>
                    <div class="form-group">
                        <label for="thumbnailFile">Hình ảnh sản phẩm <span class="required">*</span></label>
                        <input type="file" 
                               id="thumbnailFile" 
                               name="thumbnailFile" 
                               accept="image/*"
                               onchange="previewImage(this)">
                        <div class="help-text">Chọn file ảnh (JPG, PNG, GIF) - Tối đa 5MB</div>
                        <c:if test="${product != null}">
                            <div class="help-text" style="color: #17a2b8; margin-top: 5px;">
                                <i class="fas fa-info-circle"></i> Hình ảnh hiện tại: ${product.thumbnailUrl}
                            </div>
                        </c:if>
                    </div>

                    <div class="form-group">
                        <label>Xem trước hình ảnh</label>
                        <img id="imagePreview" 
                             class="image-preview" 
                             src="${product != null ? pageContext.request.contextPath.concat('/assets/image/').concat(product.thumbnailUrl) : 'https://via.placeholder.com/100x100?text=No+Image'}"
                             alt="Preview"
                             onerror="this.src='https://via.placeholder.com/100x100?text=No+Image'">
                    </div>
                </div>
            </div>

            <!-- Form Actions -->
            <div class="form-actions">
                <a href="admin-products" class="btn btn-secondary">
                    <i class="fas fa-times"></i>
                    Hủy
                </a>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i>
                    ${product != null ? 'Cập nhật' : 'Tạo mới'}
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
    document.getElementById('productForm').addEventListener('submit', function(e) {
        const tenSP = document.getElementById('tenSP').value.trim();
        const donGia = document.getElementById('donGia').value;
        const thumbnailFile = document.getElementById('thumbnailFile').files[0];
        const trangThai = document.getElementById('trangThai').value;
        <c:choose>
            <c:when test="${product != null}">
                const isEdit = true;
            </c:when>
            <c:otherwise>
                const isEdit = false;
            </c:otherwise>
        </c:choose>

        if (!tenSP || !donGia || !trangThai) {
            e.preventDefault();
            alert('Vui lòng điền đầy đủ thông tin bắt buộc');
            return;
        }

        if (!isEdit && !thumbnailFile) {
            e.preventDefault();
            alert('Vui lòng chọn hình ảnh sản phẩm');
            return;
        }

        if (parseFloat(donGia) <= 0) {
            e.preventDefault();
            alert('Giá sản phẩm phải lớn hơn 0');
            return;
        }

        if (thumbnailFile && thumbnailFile.size > 5 * 1024 * 1024) {
            e.preventDefault();
            alert('Kích thước file không được vượt quá 5MB');
            return;
        }
    });
</script>

<jsp:include page="../layout/admin-footer.jsp" />
