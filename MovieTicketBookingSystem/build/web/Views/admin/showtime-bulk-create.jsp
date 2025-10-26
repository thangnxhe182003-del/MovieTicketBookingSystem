<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Tạo suất chiếu hàng loạt"/>
    <jsp:param name="pageSubtitle" value="Tạo nhiều suất chiếu cho 7 ngày tiếp theo"/>
    <jsp:param name="currentPage" value="showtimes"/>
    <jsp:param name="extraStyles" value="
        <style>
            .container { padding: 30px; }
            .form-container { max-width: 1000px; margin: 0 auto; }
            .card { background: #fff; border-radius: 8px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); margin-bottom: 20px; }
            .card-header { padding: 20px 30px; border-bottom: 1px solid #e9ecef; background: #f8f9fa; }
            .card-header h2 { margin: 0; font-size: 20px; font-weight: 600; color: #333; }
            .card-body { padding: 30px; }
            .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 20px; }
            .form-group { margin-bottom: 20px; }
            .form-group label { display: block; font-weight: 600; margin-bottom: 8px; color: #333; }
            .form-group label span { color: #e50914; margin-left: 2px; }
            .form-group input, .form-group select { 
                width: 100%; padding: 12px 15px; border: 1px solid #ddd; border-radius: 6px; 
                font-size: 14px; transition: border-color 0.3s; 
            }
            .form-group input:focus, .form-group select:focus { 
                border-color: #e50914; outline: none; box-shadow: 0 0 0 3px rgba(229, 9, 20, 0.1); 
            }
            .days-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 15px; margin: 20px 0; }
            .day-card { border: 2px solid #e9ecef; border-radius: 8px; padding: 15px; text-align: center; cursor: pointer; 
                       transition: all 0.3s; background: #fff; }
            .day-card:hover { border-color: #e50914; background: #fff5f5; }
            .day-card.selected { border-color: #e50914; background: #e50914; color: white; }
            .day-card input { display: none; }
            .day-card h4 { margin: 0 0 5px 0; font-size: 16px; font-weight: 600; }
            .day-card p { margin: 0; font-size: 14px; opacity: 0.8; }
            .time-slots { display: grid; grid-template-columns: repeat(auto-fit, minmax(120px, 1fr)); gap: 10px; margin: 20px 0; }
            .time-slot { padding: 12px; border: 2px solid #e9ecef; border-radius: 6px; text-align: center; cursor: pointer; 
                        transition: all 0.3s; background: #fff; }
            .time-slot:hover { border-color: #e50914; background: #fff5f5; }
            .time-slot.selected { border-color: #e50914; background: #e50914; color: white; }
            .time-slot input { display: none; }
            .time-slot h5 { margin: 0 0 5px 0; font-size: 14px; font-weight: 600; }
            .time-slot p { margin: 0; font-size: 12px; opacity: 0.8; }
            .btn-group { display: flex; gap: 15px; justify-content: flex-end; margin-top: 30px; }
            .btn { padding: 12px 24px; border: none; border-radius: 6px; font-weight: 600; cursor: pointer; 
                   text-decoration: none; display: inline-flex; align-items: center; gap: 8px; transition: all 0.3s; }
            .btn-primary { background: linear-gradient(135deg, #e50914, #c90812); color: white; }
            .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(229, 9, 20, 0.4); }
            .btn-secondary { background: #6c757d; color: white; }
            .btn-secondary:hover { background: #5a6268; }
            .alert { padding: 12px 16px; border-radius: 6px; margin-bottom: 20px; font-weight: 500; }
            .alert-error { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
            .preview-section { background: #f8f9fa; border-radius: 8px; padding: 20px; margin-top: 20px; }
            .preview-title { font-size: 16px; font-weight: 600; margin-bottom: 15px; color: #333; }
            .preview-list { max-height: 200px; overflow-y: auto; }
            .preview-item { display: flex; justify-content: space-between; align-items: center; 
                           padding: 8px 12px; background: #fff; border-radius: 4px; margin-bottom: 8px; 
                           border: 1px solid #e9ecef; }
            .preview-item:last-child { margin-bottom: 0; }
            .preview-info { font-size: 14px; color: #333; }
            .preview-count { background: #e50914; color: white; padding: 4px 8px; border-radius: 12px; 
                            font-size: 12px; font-weight: 600; }
        </style>
    "/>
</jsp:include>

<div class="container">
    <div class="form-container">
        <c:if test="${not empty param.error}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i> ${param.error}
            </div>
        </c:if>

        <form action="admin-showtimes" method="POST" id="bulkCreateForm">
            <input type="hidden" name="action" value="bulk-create" />

            <!-- Basic Settings -->
            <div class="card">
                <div class="card-header">
                    <h2>Cài đặt cơ bản</h2>
                </div>
                <div class="card-body">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="maPhong">Phòng chiếu <span>*</span></label>
                            <select id="maPhong" name="maPhong" required>
                                <option value="">-- Chọn phòng --</option>
                                <c:forEach var="room" items="${rooms}">
                                    <option value="${room.maPhong}">${room.tenPhong} - ${room.tenRap}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="giaVeCoSo">Giá vé cơ sở <span>*</span></label>
                            <input type="number" id="giaVeCoSo" name="giaVeCoSo" value="50000" min="0" step="1000" required />
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="ngonNguAmThanh">Ngôn ngữ/Âm thanh <span>*</span></label>
                        <select id="ngonNguAmThanh" name="ngonNguAmThanh" required>
                            <option value="">-- Chọn ngôn ngữ --</option>
                            <option value="Tiếng Việt">Tiếng Việt</option>
                            <option value="Tiếng Anh">Tiếng Anh</option>
                            <option value="Lồng tiếng">Lồng tiếng</option>
                            <option value="Phụ đề">Phụ đề</option>
                        </select>
                    </div>
                </div>
            </div>

            <!-- Select Days -->
            <div class="card">
                <div class="card-header">
                    <h2>Chọn ngày (7 ngày tới)</h2>
                </div>
                <div class="card-body">
                    <div class="days-grid" id="daysGrid">
                        <!-- Days will be generated by JavaScript -->
                    </div>
                </div>
            </div>

            <!-- Select Time Slots -->
            <div class="card">
                <div class="card-header">
                    <h2>Chọn khung giờ</h2>
                </div>
                <div class="card-body">
                    <div class="time-slots" id="timeSlots">
                        <div class="time-slot" data-time="09:00-11:00">
                            <input type="checkbox" name="timeSlots" value="09:00-11:00" />
                            <h5>09:00 - 11:00</h5>
                            <p>Sáng</p>
                        </div>
                        <div class="time-slot" data-time="11:30-13:30">
                            <input type="checkbox" name="timeSlots" value="11:30-13:30" />
                            <h5>11:30 - 13:30</h5>
                            <p>Trưa</p>
                        </div>
                        <div class="time-slot" data-time="14:00-16:00">
                            <input type="checkbox" name="timeSlots" value="14:00-16:00" />
                            <h5>14:00 - 16:00</h5>
                            <p>Chiều</p>
                        </div>
                        <div class="time-slot" data-time="16:30-18:30">
                            <input type="checkbox" name="timeSlots" value="16:30-18:30" />
                            <h5>16:30 - 18:30</h5>
                            <p>Chiều tối</p>
                        </div>
                        <div class="time-slot" data-time="19:00-21:00">
                            <input type="checkbox" name="timeSlots" value="19:00-21:00" />
                            <h5>19:00 - 21:00</h5>
                            <p>Tối</p>
                        </div>
                        <div class="time-slot" data-time="21:30-23:30">
                            <input type="checkbox" name="timeSlots" value="21:30-23:30" />
                            <h5>21:30 - 23:30</h5>
                            <p>Khuya</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Preview -->
            <div class="card">
                <div class="card-header">
                    <h2>Xem trước</h2>
                </div>
                <div class="card-body">
                    <div class="preview-section">
                        <div class="preview-title">
                            <i class="fas fa-eye"></i> Sẽ tạo <span id="previewCount">0</span> suất chiếu
                        </div>
                        <div class="preview-list" id="previewList">
                            <p style="text-align: center; color: #666; margin: 20px 0;">Chọn phòng, ngày và khung giờ để xem trước</p>
                        </div>
                    </div>
                </div>
            </div>

            <div class="btn-group">
                <a href="admin-showtimes" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i>
                    Quay lại
                </a>
                <button type="submit" class="btn btn-primary" id="submitBtn" disabled>
                    <i class="fas fa-plus"></i>
                    Tạo suất chiếu
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    // Generate days for the next 7 days
    function generateDays() {
        const daysGrid = document.getElementById('daysGrid');
        const today = new Date();
        const dayNames = ['Chủ nhật', 'Thứ hai', 'Thứ ba', 'Thứ tư', 'Thứ năm', 'Thứ sáu', 'Thứ bảy'];
        
        for (let i = 0; i < 7; i++) {
            const date = new Date(today);
            date.setDate(today.getDate() + i);
            
            const dayCard = document.createElement('div');
            dayCard.className = 'day-card';
            dayCard.setAttribute('data-day', i);
            
            dayCard.innerHTML = `
                <input type="checkbox" name="selectedDays" value="${i}" />
                <h4>${dayNames[date.getDay()]}</h4>
                <p>${date.toLocaleDateString('vi-VN')}</p>
            `;
            
            dayCard.addEventListener('click', function() {
                const checkbox = this.querySelector('input[type="checkbox"]');
                checkbox.checked = !checkbox.checked;
                this.classList.toggle('selected', checkbox.checked);
                updatePreview();
            });
            
            daysGrid.appendChild(dayCard);
        }
    }

    // Time slot selection
    document.querySelectorAll('.time-slot').forEach(slot => {
        slot.addEventListener('click', function() {
            const checkbox = this.querySelector('input[type="checkbox"]');
            checkbox.checked = !checkbox.checked;
            this.classList.toggle('selected', checkbox.checked);
            updatePreview();
        });
    });

    // Update preview
    function updatePreview() {
        const selectedDays = document.querySelectorAll('input[name="selectedDays"]:checked');
        const selectedTimes = document.querySelectorAll('input[name="timeSlots"]:checked');
        const previewList = document.getElementById('previewList');
        const previewCount = document.getElementById('previewCount');
        const submitBtn = document.getElementById('submitBtn');
        
        if (selectedDays.length === 0 || selectedTimes.length === 0) {
            previewList.innerHTML = '<p style="text-align: center; color: #666; margin: 20px 0;">Chọn phòng, ngày và khung giờ để xem trước</p>';
            previewCount.textContent = '0';
            submitBtn.disabled = true;
            return;
        }
        
        const today = new Date();
        const dayNames = ['Chủ nhật', 'Thứ hai', 'Thứ ba', 'Thứ tư', 'Thứ năm', 'Thứ sáu', 'Thứ bảy'];
        let previewItems = [];
        
        selectedDays.forEach(dayCheckbox => {
            const dayOffset = parseInt(dayCheckbox.value);
            const date = new Date(today);
            date.setDate(today.getDate() + dayOffset);
            
            selectedTimes.forEach(timeCheckbox => {
                const timeSlot = timeCheckbox.value;
                const [startTime, endTime] = timeSlot.split('-');
                
                previewItems.push({
                    day: dayNames[date.getDay()],
                    date: date.toLocaleDateString('vi-VN'),
                    time: `${startTime} - ${endTime}`
                });
            });
        });
        
        previewList.innerHTML = previewItems.map(item => `
            <div class="preview-item">
                <div class="preview-info">
                    <strong>${item.day}</strong> - ${item.date} | ${item.time}
                </div>
            </div>
        `).join('');
        
        previewCount.textContent = previewItems.length;
        submitBtn.disabled = false;
    }

    // Initialize
    generateDays();
    
    // Update preview when form changes
    document.getElementById('maPhong').addEventListener('change', updatePreview);
    document.getElementById('giaVeCoSo').addEventListener('change', updatePreview);
    document.getElementById('ngonNguAmThanh').addEventListener('change', updatePreview);
</script>

<jsp:include page="../layout/admin-footer.jsp" />
