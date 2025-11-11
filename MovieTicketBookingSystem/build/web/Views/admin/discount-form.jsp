<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="${discount != null ? 'Chỉnh sửa mã giảm giá' : 'Thêm mã giảm giá mới'}"/>
    <jsp:param name="pageSubtitle" value="${discount != null ? 'Cập nhật thông tin mã giảm giá' : 'Tạo mã giảm giá mới'}"/>
    <jsp:param name="currentPage" value="discounts"/>
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
            
            .conditional-field {
                display: none;
            }
            
            .conditional-field.show {
                display: block;
            }
        </style>
    "/>
</jsp:include>

<div class="form-container">
    <!-- Form Header -->
    <div class="form-header">
        <h2 class="form-title">${discount != null ? 'Chỉnh sửa mã giảm giá' : 'Thêm mã giảm giá mới'}</h2>
        <a href="admin-discounts" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i>
            Quay lại
        </a>
    </div>

    <!-- Form Content -->
    <div class="form-content">
        <form id="discountForm" method="POST" action="admin-discounts">
            <input type="hidden" name="action" value="${discount != null ? 'update' : 'create'}">
            <c:if test="${discount != null}">
                <input type="hidden" name="maGiamGia" value="${discount.maGiamGia}">
            </c:if>
            
            <div class="form-grid">
                <!-- Left Column -->
                <div>
                    <div class="form-group">
                        <label for="maCode">Mã code <span class="required">*</span></label>
                        <input type="text" 
                               id="maCode" 
                               name="maCode" 
                               value="${discount != null ? discount.maCode : ''}"
                               placeholder="VD: GIAM50K"
                               required
                               style="text-transform: uppercase;">
                        <div class="help-text">Mã code sẽ được tự động chuyển thành chữ hoa</div>
                    </div>

                    <div class="form-group">
                        <label for="tenGiamGia">Tên chương trình <span class="required">*</span></label>
                        <input type="text" 
                               id="tenGiamGia" 
                               name="tenGiamGia" 
                               value="${discount != null ? discount.tenGiamGia : ''}"
                               placeholder="Nhập tên chương trình giảm giá"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="loaiGiamGia">Loại giảm giá <span class="required">*</span></label>
                        <select id="loaiGiamGia" name="loaiGiamGia" required>
                            <option value="">Chọn loại giảm giá</option>
                            <option value="Vé" ${discount != null && discount.loaiGiamGia == 'Vé' ? 'selected' : ''}>Vé</option>
                            <option value="Đồ ăn" ${discount != null && discount.loaiGiamGia == 'Đồ ăn' ? 'selected' : ''}>Đồ ăn</option>
                            <option value="Toàn đơn" ${discount != null && discount.loaiGiamGia == 'Toàn đơn' ? 'selected' : ''}>Toàn đơn</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="hinhThucGiam">Hình thức giảm <span class="required">*</span></label>
                        <select id="hinhThucGiam" name="hinhThucGiam" required>
                            <option value="">Chọn hình thức giảm</option>
                            <option value="PhanTram" ${discount != null && discount.hinhThucGiam == 'PhanTram' ? 'selected' : ''}>Phần trăm</option>
                            <option value="TienMat" ${discount != null && discount.hinhThucGiam == 'TienMat' ? 'selected' : ''}>Tiền mặt</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="giaTriGiam">Giá trị giảm <span class="required">*</span></label>
                        <input type="text" 
                               id="giaTriGiam" 
                               name="giaTriGiam" 
                               value="${giaTriGiamFormatted != null ? giaTriGiamFormatted : ''}"
                               placeholder="${discount != null && discount.hinhThucGiam == 'PhanTram' ? 'VD: 10 (10%)' : 'VD: 50000 (₫50,000)'}"
                               required>
                        <div class="help-text" id="giaTriGiamHelp">
                            <c:choose>
                                <c:when test="${discount != null && discount.hinhThucGiam == 'PhanTram'}">
                                    Nhập phần trăm giảm giá (ví dụ: 10 cho 10%)
                                </c:when>
                                <c:otherwise>
                                    Nhập số tiền giảm giá (ví dụ: 50000)
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="form-group conditional-field" id="giaTriToiDaGroup">
                        <label for="giaTriToiDa">Giá trị tối đa</label>
                        <input type="text" 
                               id="giaTriToiDa" 
                               name="giaTriToiDa" 
                               value="${giaTriToiDaFormatted != null ? giaTriToiDaFormatted : ''}"
                               placeholder="VD: 100000">
                        <div class="help-text">Chỉ áp dụng khi hình thức là Phần trăm. Để trống nếu không giới hạn.</div>
                    </div>
                </div>

                <!-- Right Column -->
                <div>
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
                            <option value="Hoạt Động" ${discount != null && discount.trangThai == 'Hoạt Động' ? 'selected' : ''}>Hoạt Động</option>
                            <option value="Hết hạn" ${discount != null && discount.trangThai == 'Hết hạn' ? 'selected' : ''}>Hết hạn</option>
                            <option value="Không hoạt động" ${discount != null && discount.trangThai == 'Không hoạt động' ? 'selected' : ''}>Không hoạt động</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="soLanSuDung">Số lần sử dụng tối đa</label>
                        <input type="number" 
                               id="soLanSuDung" 
                               name="soLanSuDung" 
                               value="${discount != null && discount.soLanSuDung != null ? discount.soLanSuDung : ''}"
                               placeholder="Để trống nếu không giới hạn"
                               min="1">
                        <div class="help-text">Để trống nếu không giới hạn số lần sử dụng</div>
                    </div>

                    <div class="form-group full-width">
                        <label for="moTa">Mô tả</label>
                        <textarea id="moTa" 
                                  name="moTa" 
                                  placeholder="Nhập mô tả về chương trình giảm giá (tùy chọn)">${discount != null ? discount.moTa : ''}</textarea>
                    </div>
                </div>
            </div>

            <!-- Form Actions -->
            <div class="form-actions">
                <a href="admin-discounts" class="btn btn-secondary">
                    <i class="fas fa-times"></i>
                    Hủy
                </a>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i>
                    ${discount != null ? 'Cập nhật' : 'Tạo mới'}
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    // Auto-uppercase mã code
    document.getElementById('maCode').addEventListener('input', function() {
        this.value = this.value.toUpperCase();
    });

    // Show/hide giá trị tối đa based on hình thức giảm
    const hinhThucGiam = document.getElementById('hinhThucGiam');
    const giaTriToiDaGroup = document.getElementById('giaTriToiDaGroup');
    const giaTriGiamInput = document.getElementById('giaTriGiam');
    const giaTriGiamHelp = document.getElementById('giaTriGiamHelp');

    function updateFormBasedOnHinhThuc() {
        const isPhanTram = hinhThucGiam.value === 'PhanTram';
        
        if (isPhanTram) {
            giaTriToiDaGroup.classList.add('show');
            giaTriGiamInput.placeholder = 'VD: 10 (10%)';
            giaTriGiamHelp.textContent = 'Nhập phần trăm giảm giá (ví dụ: 10 cho 10%)';
        } else {
            giaTriToiDaGroup.classList.remove('show');
            giaTriGiamInput.placeholder = 'VD: 50000 (₫50,000)';
            giaTriGiamHelp.textContent = 'Nhập số tiền giảm giá (ví dụ: 50000)';
        }
    }

    hinhThucGiam.addEventListener('change', updateFormBasedOnHinhThuc);
    
    // Initialize on page load
    updateFormBasedOnHinhThuc();

    // Form validation
    document.getElementById('discountForm').addEventListener('submit', function(e) {
        const maCode = document.getElementById('maCode').value.trim();
        const tenGiamGia = document.getElementById('tenGiamGia').value.trim();
        const loaiGiamGia = document.getElementById('loaiGiamGia').value;
        const hinhThucGiam = document.getElementById('hinhThucGiam').value;
        const giaTriGiam = document.getElementById('giaTriGiam').value.trim();
        const ngayBatDau = document.getElementById('ngayBatDau').value;
        const trangThai = document.getElementById('trangThai').value;

        if (!maCode || !tenGiamGia || !loaiGiamGia || !hinhThucGiam || !giaTriGiam || !ngayBatDau || !trangThai) {
            e.preventDefault();
            alert('Vui lòng điền đầy đủ thông tin bắt buộc');
            return;
        }

        // Validate giá trị giảm
        const giaTriGiamNum = parseFloat(giaTriGiam.replace(/,/g, '').replace(/\./g, ''));
        if (isNaN(giaTriGiamNum) || giaTriGiamNum <= 0) {
            e.preventDefault();
            alert('Giá trị giảm phải là số dương');
            return;
        }

        if (hinhThucGiam === 'PhanTram' && (giaTriGiamNum < 0 || giaTriGiamNum > 100)) {
            e.preventDefault();
            alert('Phần trăm giảm giá phải từ 0 đến 100');
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

        // Validate số lần sử dụng
        const soLanSuDung = document.getElementById('soLanSuDung').value;
        if (soLanSuDung && parseInt(soLanSuDung) < 1) {
            e.preventDefault();
            alert('Số lần sử dụng phải lớn hơn 0');
            return;
        }
    });

    // Format price input
    document.getElementById('giaTriGiam').addEventListener('input', function() {
        if (hinhThucGiam.value === 'TienMat') {
            let value = this.value.replace(/[^0-9]/g, '');
            if (value) {
                this.value = parseInt(value).toLocaleString('vi-VN');
            }
        }
    });

    document.getElementById('giaTriToiDa').addEventListener('input', function() {
        let value = this.value.replace(/[^0-9]/g, '');
        if (value) {
            this.value = parseInt(value).toLocaleString('vi-VN');
        }
    });
</script>

<jsp:include page="../layout/admin-footer.jsp" />

