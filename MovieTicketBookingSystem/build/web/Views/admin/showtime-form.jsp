<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="${showtime != null ? 'Sửa suất chiếu' : 'Thêm suất chiếu mới'}"/>
    <jsp:param name="pageSubtitle" value="${showtime != null ? 'Chỉnh sửa thông tin suất chiếu' : 'Tạo suất chiếu mới cho phim'}"/>
    <jsp:param name="currentPage" value="showtimes"/>
    <jsp:param name="extraStyles" value="
        <style>
            .container { padding: 30px; }
            .form-container { max-width: 800px; margin: 0 auto; }
            .card { background: #fff; border-radius: 8px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); }
            .card-header { padding: 20px 30px; border-bottom: 1px solid #e9ecef; background: #f8f9fa; }
            .card-header h2 { margin: 0; font-size: 20px; font-weight: 600; color: #333; }
            .card-body { padding: 30px; }
            .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 20px; }
            .form-group { margin-bottom: 20px; }
            .form-group label { display: block; font-weight: 600; margin-bottom: 8px; color: #333; }
            .form-group label span { color: #e50914; margin-left: 2px; }
            .form-group input, .form-group select, .form-group textarea { 
                width: 100%; padding: 12px 15px; border: 1px solid #ddd; border-radius: 6px; 
                font-size: 14px; transition: border-color 0.3s; 
            }
            .form-group input:focus, .form-group select:focus, .form-group textarea:focus { 
                border-color: #e50914; outline: none; box-shadow: 0 0 0 3px rgba(229, 9, 20, 0.1); 
            }
            .form-group textarea { height: 80px; resize: vertical; }
            .btn-group { display: flex; gap: 15px; justify-content: flex-end; margin-top: 30px; }
            .btn { padding: 12px 24px; border: none; border-radius: 6px; font-weight: 600; cursor: pointer; 
                   text-decoration: none; display: inline-flex; align-items: center; gap: 8px; transition: all 0.3s; }
            .btn-primary { background: linear-gradient(135deg, #e50914, #c90812); color: white; }
            .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(229, 9, 20, 0.4); }
            .btn-secondary { background: #6c757d; color: white; }
            .btn-secondary:hover { background: #5a6268; }
            .alert { padding: 12px 16px; border-radius: 6px; margin-bottom: 20px; font-weight: 500; }
            .alert-error { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
            .alert-success { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
            .movie-preview { display: flex; align-items: center; gap: 15px; padding: 15px; background: #f8f9fa; 
                            border-radius: 6px; margin-top: 10px; }
            .movie-poster { width: 60px; height: 80px; object-fit: cover; border-radius: 6px; border: 2px solid #e9ecef; }
            .movie-details h4 { margin: 0 0 5px 0; font-size: 16px; font-weight: 600; color: #333; }
            .movie-details p { margin: 0; font-size: 14px; color: #666; }
            .time-slots { display: grid; grid-template-columns: repeat(auto-fit, minmax(120px, 1fr)); gap: 10px; margin-top: 10px; }
            .time-slot { padding: 10px; border: 1px solid #ddd; border-radius: 6px; text-align: center; cursor: pointer; 
                        transition: all 0.3s; background: #fff; }
            .time-slot:hover { border-color: #e50914; background: #fff5f5; }
            .time-slot.selected { border-color: #e50914; background: #e50914; color: white; }
            .time-slot input { display: none; }
        </style>
    "/>
</jsp:include>

<div class="container">
    <div class="form-container">
        <div class="card">
            <div class="card-header">
                <h2>${showtime != null ? 'Sửa suất chiếu' : 'Thêm suất chiếu mới'}</h2>
            </div>
            <div class="card-body">
                <c:if test="${not empty param.error}">
                    <div class="alert alert-error">
                        <i class="fas fa-exclamation-circle"></i> ${param.error}
                    </div>
                </c:if>

                <form action="admin-showtimes" method="POST">
                    <input type="hidden" name="action" value="${showtime != null ? 'update' : 'create'}" />
                    <c:if test="${showtime != null}">
                        <input type="hidden" name="maSuatChieu" value="${showtime.maSuatChieu}" />
                    </c:if>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="maPhim">Phim <span>*</span></label>
                            <select id="maPhim" name="maPhim" required>
                                <option value="">-- Chọn phim --</option>
                                <c:forEach var="movie" items="${movies}">
                                    <option value="${movie.maPhim}" 
                                            ${showtime != null && showtime.maPhim == movie.maPhim ? 'selected' : ''}>
                                        ${movie.tenPhim}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="maPhong">Phòng chiếu <span>*</span></label>
                            <select id="maPhong" name="maPhong" required>
                                <option value="">-- Chọn phòng --</option>
                                <c:forEach var="room" items="${rooms}">
                                    <option value="${room.maPhong}" 
                                            ${showtime != null && showtime.maPhong == room.maPhong ? 'selected' : ''}>
                                        ${room.tenPhong} - ${room.tenRap}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="ngayChieu">Ngày chiếu <span>*</span></label>
                            <input type="date" id="ngayChieu" name="ngayChieu" 
                                   value="${showtime != null ? formattedDate : ''}" 
                                   min="<%=java.time.LocalDate.now().toString()%>" required />
                        </div>

                        <div class="form-group">
                            <label for="giaVeCoSo">Giá vé cơ sở <span>*</span></label>
                            <input type="number" id="giaVeCoSo" name="giaVeCoSo" 
                                   value="${showtime != null ? showtime.giaVeCoSo : '50000'}" 
                                   min="0" step="1000" required />
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="gioBatDau">Giờ bắt đầu <span>*</span></label>
                            <input type="time" id="gioBatDau" name="gioBatDau" 
                                   value="${showtime != null ? formattedStartTime : ''}" required />
                        </div>

                        <div class="form-group">
                            <label for="gioKetThuc">Giờ kết thúc <span>*</span></label>
                            <input type="time" id="gioKetThuc" name="gioKetThuc" 
                                   value="${showtime != null ? formattedEndTime : ''}" required />
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="ngonNguAmThanh">Ngôn ngữ/Âm thanh <span>*</span></label>
                        <select id="ngonNguAmThanh" name="ngonNguAmThanh" required>
                            <option value="">-- Chọn ngôn ngữ --</option>
                            <option value="Tiếng Việt" ${showtime != null && showtime.ngonNguAmThanh == 'Tiếng Việt' ? 'selected' : ''}>Tiếng Việt</option>
                            <option value="Tiếng Anh" ${showtime != null && showtime.ngonNguAmThanh == 'Tiếng Anh' ? 'selected' : ''}>Tiếng Anh</option>
                            <option value="Lồng tiếng" ${showtime != null && showtime.ngonNguAmThanh == 'Lồng tiếng' ? 'selected' : ''}>Lồng tiếng</option>
                            <option value="Phụ đề" ${showtime != null && showtime.ngonNguAmThanh == 'Phụ đề' ? 'selected' : ''}>Phụ đề</option>
                        </select>
                    </div>


                    <div class="btn-group">
                        <a href="admin-showtimes" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i>
                            Quay lại
                        </a>
                        <button type="button" class="btn btn-info" onclick="suggestShowtimes()" id="suggestBtn">
                            <i class="fas fa-lightbulb"></i>
                            Đề xuất suất chiếu
                        </button>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i>
                            ${showtime != null ? 'Cập nhật' : 'Tạo'} suất chiếu
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    // Auto-calculate end time when start time changes
    document.getElementById('gioBatDau').addEventListener('change', function() {
        const startTime = this.value;
        if (startTime) {
            const [hours, minutes] = startTime.split(':').map(Number);
            const endTime = new Date();
            endTime.setHours(hours + 2, minutes); // Assume 2 hours duration
            const endTimeString = endTime.toTimeString().slice(0, 5);
            document.getElementById('gioKetThuc').value = endTimeString;
        }
    });

    // Set minimum date to today (only if not already set)
    const ngayChieuField = document.getElementById('ngayChieu');
    if (!ngayChieuField.min) {
        ngayChieuField.min = new Date().toISOString().split('T')[0];
    }
    
    
    // Suggest showtimes function
    function suggestShowtimes() {
        const maPhim = document.getElementById('maPhim').value;
        const maPhong = document.getElementById('maPhong').value;
        const ngayChieu = document.getElementById('ngayChieu').value;
        const giaVeCoSo = document.getElementById('giaVeCoSo').value;
        const ngonNguAmThanh = document.getElementById('ngonNguAmThanh').value;
        
        // Debug: In ra các giá trị
        console.log('Debug - maPhim:', maPhim);
        console.log('Debug - maPhong:', maPhong);
        console.log('Debug - ngayChieu:', ngayChieu);
        console.log('Debug - giaVeCoSo:', giaVeCoSo);
        console.log('Debug - ngonNguAmThanh:', ngonNguAmThanh);
        
        if (!maPhim || !maPhong || !ngayChieu) {
            alert('Vui lòng chọn phim, phòng và ngày chiếu trước khi đề xuất');
            return;
        }
        
        // Redirect to suggestion page
        window.location.href = 'admin-showtimes?action=suggest&maPhim=' + maPhim + '&maPhong=' + maPhong + '&ngayChieu=' + ngayChieu + '&giaVeCoSo=' + giaVeCoSo + '&ngonNguAmThanh=' + encodeURIComponent(ngonNguAmThanh);
    }
</script>


<jsp:include page="../layout/admin-footer.jsp" />
