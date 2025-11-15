<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="${movie != null ? 'Chỉnh sửa phim' : 'Thêm phim mới'}"/>
    <jsp:param name="pageSubtitle" value="${movie != null ? 'Cập nhật thông tin phim' : 'Tạo phim mới cho hệ thống'}"/>
    <jsp:param name="currentPage" value="movies"/>
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
            
            .poster-preview {
                width: 150px;
                height: 200px;
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
            
            .full-width {
                grid-column: 1 / -1;
            }

            /* Checkbox options layout */
            .options-grid {
                display: grid;
                grid-template-columns: repeat(2, minmax(0, 1fr));
                gap: 10px;
            }
            .option-item {
                display: flex;
                align-items: center;
                gap: 10px;
                padding: 10px 12px;
                border: 1px solid #e9ecef;
                border-radius: 6px;
                background: #fff;
            }
            .option-item:hover {
                border-color: #e50914;
                box-shadow: 0 0 0 3px rgba(229, 9, 20, 0.08);
            }
            .option-item input[type='checkbox'] {
                width: 16px;
                height: 16px;
                accent-color: #e50914;
            }
            .option-item span {
                font-size: 14px;
                color: #333;
            }
            @media (max-width: 768px) {
                .options-grid {
                    grid-template-columns: 1fr;
                }
            }
        </style>
    "/>
</jsp:include>

<div class="form-container">
    <!-- Form Header -->
    <div class="form-header">
        <h2 class="form-title">${movie != null ? 'Chỉnh sửa phim' : 'Thêm phim mới'}</h2>
        <a href="admin-movies" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i>
            Quay lại
        </a>
    </div>

    <!-- Form Content -->
    <div class="form-content">
        <form id="movieForm" method="POST" action="admin-movies" enctype="multipart/form-data">
            <input type="hidden" name="action" value="${movie != null ? 'update' : 'create'}">
            <c:if test="${movie != null}">
                <input type="hidden" name="maPhim" value="${movie.maPhim}">
            </c:if>
            
            <div class="form-grid">
                <!-- Left Column -->
                <div>
                    <div class="form-group">
                        <label for="tenPhim">Tên phim <span class="required">*</span></label>
                        <input type="text" 
                               id="tenPhim" 
                               name="tenPhim" 
                               value="${movie != null ? movie.tenPhim : ''}"
                               placeholder="Nhập tên phim"
                               required>
                    </div>

                    <div class="form-group">
                        <label>Thể loại <span class="required">*</span></label>
                        <div class="options-grid">
                            <c:forEach var="g" items="${allGenres}">
                                <label class="option-item">
                                    <input type="checkbox" name="genreIds" value="${g.maTheLoai}"
                                           ${selectedGenreIds != null && selectedGenreIds.contains(g.maTheLoai) ? 'checked' : ''}>
                                    <span>${g.tenTheLoai}</span>
                                </label>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="loaiPhim">Loại phim <span class="required">*</span></label>
                        <select id="loaiPhim" name="loaiPhim" required>
                            <option value="">Chọn loại phim</option>
                            <option value="2D" ${movie != null && movie.loaiPhim == '2D' ? 'selected' : ''}>2D</option>
                            <option value="3D" ${movie != null && movie.loaiPhim == '3D' ? 'selected' : ''}>3D</option>
                            <option value="IMAX" ${movie != null && movie.loaiPhim == 'IMAX' ? 'selected' : ''}>IMAX</option>
                            <option value="4DX" ${movie != null && movie.loaiPhim == '4DX' ? 'selected' : ''}>4DX</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Đạo diễn <span class="required">*</span></label>
                        <div class="options-grid">
                            <c:forEach var="d" items="${allDirectors}">
                                <label class="option-item">
                                    <input type="checkbox" name="directorIds" value="${d.maDaoDien}"
                                           ${selectedDirectorIds != null && selectedDirectorIds.contains(d.maDaoDien) ? 'checked' : ''}>
                                    <span>${d.tenDaoDien}</span>
                                </label>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="dienVien">Diễn viên <span class="required">*</span></label>
                        <input type="text" 
                               id="dienVien" 
                               name="dienVien" 
                               value="${movie != null ? movie.dienVien : ''}"
                               placeholder="Nhập tên diễn viên chính"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="doTuoiGioiHan">Độ tuổi giới hạn <span class="required">*</span></label>
                        <select id="doTuoiGioiHan" name="doTuoiGioiHan" required>
                            <option value="">Chọn độ tuổi</option>
                            <option value="0" ${movie != null && movie.doTuoiGioiHan == 0 ? 'selected' : ''}>P (Mọi lứa tuổi)</option>
                            <option value="8" ${movie != null && movie.doTuoiGioiHan == 8 ? 'selected' : ''}>8+</option>
                            <option value="13" ${movie != null && movie.doTuoiGioiHan == 13 ? 'selected' : ''}>13+</option>
                            <option value="16" ${movie != null && movie.doTuoiGioiHan == 16 ? 'selected' : ''}>16+</option>
                            <option value="18" ${movie != null && movie.doTuoiGioiHan == 18 ? 'selected' : ''}>18+</option>
                        </select>
                    </div>
                </div>

                <!-- Right Column -->
                <div>
                    <div class="form-group">
                        <label for="thoiLuong">Thời lượng (phút) <span class="required">*</span></label>
                        <input type="number" 
                               id="thoiLuong" 
                               name="thoiLuong" 
                               value="${movie != null ? movie.thoiLuong : ''}"
                               placeholder="Nhập thời lượng phim"
                               min="1"
                               max="300"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="ngayKhoiChieu">Ngày khởi chiếu <span class="required">*</span></label>
                        <input type="date" 
                               id="ngayKhoiChieu" 
                               name="ngayKhoiChieu" 
                               value="${ngayKhoiChieuFormatted != null ? ngayKhoiChieuFormatted : ''}"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="ngonNguPhuDe">Ngôn ngữ phụ đề <span class="required">*</span></label>
                        <select id="ngonNguPhuDe" name="ngonNguPhuDe" required>
                            <option value="">Chọn ngôn ngữ</option>
                            <option value="Tiếng Việt" ${movie != null && movie.ngonNguPhuDe == 'Tiếng Việt' ? 'selected' : ''}>Tiếng Việt</option>
                            <option value="Tiếng Anh" ${movie != null && movie.ngonNguPhuDe == 'Tiếng Anh' ? 'selected' : ''}>Tiếng Anh</option>
                            <option value="Song ngữ" ${movie != null && movie.ngonNguPhuDe == 'Song ngữ' ? 'selected' : ''}>Song ngữ</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="trangThai">Trạng thái <span class="required">*</span></label>
                        <select id="trangThai" name="trangThai" required>
                            <option value="">Chọn trạng thái</option>
                            <option value="Sắp chiếu" ${movie != null && movie.trangThai == 'Sắp chiếu' ? 'selected' : ''}>Sắp chiếu</option>
                            <option value="Đang chiếu" ${movie != null && movie.trangThai == 'Đang chiếu' ? 'selected' : ''}>Đang chiếu</option>
                            <option value="Ngừng chiếu" ${movie != null && movie.trangThai == 'Ngừng chiếu' ? 'selected' : ''}>Ngừng chiếu</option>
                        </select>
                        <div class="help-text">Trạng thái hiện tại của phim</div>
                    </div>

                    <div class="form-group">
                        <label for="posterFile">Poster phim <span class="required">*</span></label>
                        <input type="file" 
                               id="posterFile" 
                               name="posterFile" 
                               accept="image/*"
                               onchange="previewPoster(this)">
                        <div class="help-text">Chọn file ảnh (JPG, PNG, GIF) - Tối đa 5MB</div>
                        <c:if test="${movie != null}">
                            <div class="help-text" style="color: #17a2b8; margin-top: 5px;">
                                <i class="fas fa-info-circle"></i> Poster hiện tại: ${movie.poster}
                            </div>
                        </c:if>
                    </div>

                    <div class="form-group">
                        <label>Xem trước poster</label>
                        <img id="posterPreview" 
                             class="poster-preview" 
                             src="${movie != null && movie.poster != null && !movie.poster.isEmpty() ? pageContext.request.contextPath.concat('/assets/image/').concat(movie.poster) : ''}"
                             alt="Preview"
                             style="${movie == null || movie.poster == null || movie.poster.isEmpty() ? 'display: none;' : ''}"
                             onerror="this.style.display='none';">
                    </div>
                    
                    <div class="form-group">
                        <label for="linkTrailer">Link Trailer</label>
                        <input type="url" 
                               id="linkTrailer" 
                               name="linkTrailer" 
                               value="${trailer != null ? trailer.linkTrailer : ''}"
                               placeholder="Nhập link YouTube hoặc video (ví dụ: https://www.youtube.com/watch?v=...)">
                        <div class="help-text">Link trailer từ YouTube hoặc video khác</div>
                    </div>
                    
                    <div class="form-group">
                        <label for="moTaTrailer">Mô tả trailer</label>
                        <textarea id="moTaTrailer" 
                                  name="moTaTrailer" 
                                  placeholder="Nhập mô tả ngắn về trailer (tùy chọn)"
                                  rows="3">${trailer != null ? trailer.moTa : ''}</textarea>
                    </div>
                </div>
            </div>

            <!-- Full Width Row -->
            <div class="form-group full-width">
                <label for="noiDung">Nội dung phim <span class="required">*</span></label>
                <textarea id="noiDung" 
                          name="noiDung" 
                          placeholder="Nhập nội dung tóm tắt của phim"
                          required>${movie != null ? movie.noiDung : ''}</textarea>
                <div class="help-text">Mô tả ngắn gọn về nội dung phim</div>
            </div>

            <!-- Form Actions -->
            <div class="form-actions">
                <a href="admin-movies" class="btn btn-secondary">
                    <i class="fas fa-times"></i>
                    Hủy
                </a>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i>
                    ${movie != null ? 'Cập nhật' : 'Tạo mới'}
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    // Poster preview functionality
    function previewPoster(input) {
        const preview = document.getElementById('posterPreview');
        
        if (input.files && input.files[0]) {
            const reader = new FileReader();
            
            reader.onload = function(e) {
                preview.src = e.target.result;
                preview.style.display = 'block';
            };
            
            reader.readAsDataURL(input.files[0]);
        } else {
            // Nếu không có file, ẩn preview
            preview.style.display = 'none';
        }
    }
    

    // Form validation
    document.getElementById('movieForm').addEventListener('submit', function(e) {
        const tenPhim = document.getElementById('tenPhim').value.trim();
        const genreChecked = Array.from(document.querySelectorAll('input[name=\"genreIds\"]:checked')).map(i=>i.value);
        const loaiPhim = document.getElementById('loaiPhim').value;
        const directorChecked = Array.from(document.querySelectorAll('input[name=\"directorIds\"]:checked')).map(i=>i.value);
        const dienVien = document.getElementById('dienVien').value.trim();
        const doTuoiGioiHan = document.getElementById('doTuoiGioiHan').value;
        const thoiLuong = document.getElementById('thoiLuong').value;
        const noiDung = document.getElementById('noiDung').value.trim();
        const ngayKhoiChieu = document.getElementById('ngayKhoiChieu').value;
        const ngonNguPhuDe = document.getElementById('ngonNguPhuDe').value;
        const posterFile = document.getElementById('posterFile').files[0];
        const isEdit = ${movie != null ? 'true' : 'false'};

        if (!tenPhim || genreChecked.length === 0 || !loaiPhim || directorChecked.length === 0 || !dienVien || 
            !doTuoiGioiHan || !thoiLuong || !noiDung || !ngayKhoiChieu || !ngonNguPhuDe) {
            e.preventDefault();
            alert('Vui lòng điền đầy đủ thông tin bắt buộc');
            return;
        }

        if (!isEdit && !posterFile) {
            e.preventDefault();
            alert('Vui lòng chọn poster phim');
            return;
        }

        if (parseInt(thoiLuong) < 1 || parseInt(thoiLuong) > 300) {
            e.preventDefault();
            alert('Thời lượng phim phải từ 1 đến 300 phút');
            return;
        }

        if (posterFile && posterFile.size > 5 * 1024 * 1024) {
            e.preventDefault();
            alert('Kích thước file không được vượt quá 5MB');
            return;
        }

        // Không giới hạn ngày khởi chiếu, có thể chọn trong quá khứ
    });
</script>

<jsp:include page="../layout/admin-footer.jsp" />
