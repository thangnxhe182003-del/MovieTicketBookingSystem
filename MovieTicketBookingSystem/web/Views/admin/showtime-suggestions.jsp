<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Đề xuất suất chiếu"/>
</jsp:include>

<div class="container-fluid">
    <div class="row">
        <div class="col-12">
            <div class="page-header">
                <h1 class="page-title">
                    <i class="fas fa-lightbulb"></i>
                    Đề xuất suất chiếu thông minh
                </h1>
                <nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="admin-showtimes">Suất chiếu</a></li>
                        <li class="breadcrumb-item active">Đề xuất</li>
                    </ol>
                </nav>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-12">
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">Thông tin đề xuất</h3>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-4">
                            <div class="info-item">
                                <label class="info-label">Phim:</label>
                                <span class="info-value">${movie.tenPhim}</span>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="info-item">
                                <label class="info-label">Phòng:</label>
                                <span class="info-value">${room.tenPhong} - ${room.tenRap}</span>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="info-item">
                                <label class="info-label">Ngày chiếu:</label>
                                <span class="info-value">${ngayChieu}</span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4">
                            <div class="info-item">
                                <label class="info-label">Thời lượng:</label>
                                <span class="info-value">${movie.thoiLuong} phút</span>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="info-item">
                                <label class="info-label">Loại phim:</label>
                                <span class="info-value">${movie.loaiPhim}</span>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="info-item">
                                <label class="info-label">Độ tuổi:</label>
                                <span class="info-value">${movie.doTuoiGioiHan}+</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-12">
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">Khung giờ đề xuất</h3>
                    <div class="card-tools">
                        <button type="button" class="btn btn-sm btn-primary" onclick="selectAllAvailable()">
                            <i class="fas fa-check-double"></i> Chọn tất cả khả dụng
                        </button>
                        <button type="button" class="btn btn-sm btn-secondary" onclick="clearAll()">
                            <i class="fas fa-times"></i> Bỏ chọn tất cả
                        </button>
                    </div>
                </div>
                <div class="card-body">
                    <form id="suggestionForm" method="POST" action="admin-showtimes">
                        <input type="hidden" name="action" value="create-bulk">
                        <input type="hidden" name="maPhim" value="${movie.maPhim}">
                        <input type="hidden" name="maPhong" value="${room.maPhong}">
                        <input type="hidden" name="ngayChieu" value="${ngayChieu}">
                        <input type="hidden" name="giaVeCoSo" value="${giaVeCoSo}">
                        <input type="hidden" name="ngonNguAmThanh" value="${ngonNguAmThanh}">
                        
                        <div class="suggestions-grid">
                            <c:if test="${empty suggestions}">
                                <div class="alert alert-warning">
                                    <i class="fas fa-exclamation-triangle"></i>
                                    Không có khung giờ đề xuất nào. Vui lòng kiểm tra lại thông tin phim và phòng.
                                </div>
                            </c:if>
                            <c:forEach var="suggestion" items="${suggestions}" varStatus="status">
                                <div class="suggestion-item ${suggestion.available ? 'available' : 'unavailable'}">
                                    <div class="suggestion-header">
                                        <div class="time-display">
                                            <i class="fas fa-clock"></i>
                                            <span class="time-start">${suggestion.startTime}</span>
                                            <span class="time-separator">→</span>
                                            <span class="time-end">${suggestion.endTime}</span>
                                        </div>
                                        <div class="status-badge">
                                            <c:choose>
                                                <c:when test="${suggestion.available}">
                                                    <span class="badge badge-success">
                                                        <i class="fas fa-check"></i> Có thể
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-danger">
                                                        <i class="fas fa-times"></i> Không thể
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                    
                                    <c:if test="${suggestion.available}">
                                        <div class="suggestion-actions">
                                            <div class="form-check">
                                                <input type="checkbox" 
                                                       class="form-check-input suggestion-checkbox" 
                                                       id="suggestion_${status.index}"
                                                       name="selectedSlots" 
                                                       value="${suggestion.startTime}-${suggestion.endTime}">
                                                <label class="form-check-label" for="suggestion_${status.index}">
                                                    Chọn suất chiếu này
                                                </label>
                                            </div>
                                        </div>
                                    </c:if>
                                    
                                    <c:if test="${not suggestion.available}">
                                        <div class="unavailable-reason">
                                            <i class="fas fa-exclamation-triangle"></i>
                                            ${suggestion.reason}
                                        </div>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </div>
                        
                        <div class="selected-summary" id="selectedSummary" style="display: none;">
                            <h4>Đã chọn <span id="selectedCount">0</span> suất chiếu</h4>
                            <div id="selectedList"></div>
                        </div>
                        
                        <div class="form-actions">
                            <a href="admin-showtimes" class="btn btn-secondary">
                                <i class="fas fa-arrow-left"></i> Quay lại
                            </a>
                            <button type="submit" class="btn btn-primary" id="createButton" disabled>
                                <i class="fas fa-plus"></i> Tạo suất chiếu đã chọn
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    .suggestions-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
        gap: 20px;
        margin-bottom: 20px;
    }
    
    .suggestion-item {
        border: 2px solid #dee2e6;
        border-radius: 12px;
        padding: 20px;
        background: white;
        transition: all 0.3s ease;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    
    .suggestion-item:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(0,0,0,0.15);
    }
    
    .suggestion-item.available {
        border-color: #28a745;
        background: linear-gradient(135deg, #f8fff9 0%, #e8f5e8 100%);
    }
    
    .suggestion-item.available:hover {
        border-color: #1e7e34;
        box-shadow: 0 4px 12px rgba(40, 167, 69, 0.2);
    }
    
    .suggestion-item.unavailable {
        border-color: #dc3545;
        background: linear-gradient(135deg, #fff8f8 0%, #ffeaea 100%);
        opacity: 0.8;
    }
    
    .suggestion-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10px;
    }
    
    .time-display {
        font-size: 18px;
        font-weight: 700;
        color: #495057;
        display: flex;
        align-items: center;
        gap: 8px;
    }
    
    .time-display i {
        color: #007bff;
        font-size: 16px;
    }
    
    .time-start, .time-end {
        background: #f8f9fa;
        padding: 4px 8px;
        border-radius: 4px;
        border: 1px solid #e9ecef;
    }
    
    .time-separator {
        color: #6c757d;
        font-weight: 500;
        font-size: 16px;
    }
    
    .status-badge .badge {
        font-size: 12px;
    }
    
    .suggestion-actions {
        margin-top: 15px;
        padding-top: 15px;
        border-top: 1px solid #e9ecef;
    }
    
    .form-check {
        margin: 0;
        display: flex;
        align-items: center;
        gap: 10px;
    }
    
    .form-check-input {
        margin: 0;
        width: 18px;
        height: 18px;
        cursor: pointer;
    }
    
    .form-check-label {
        font-weight: 600;
        cursor: pointer;
        color: #495057;
        font-size: 14px;
    }
    
    .form-check-input:checked + .form-check-label {
        color: #28a745;
    }
    
    .unavailable-reason {
        color: #dc3545;
        font-size: 14px;
        margin-top: 10px;
    }
    
    .unavailable-reason i {
        margin-right: 5px;
    }
    
    .selected-summary {
        background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
        border: 2px solid #2196f3;
        border-radius: 12px;
        padding: 20px;
        margin: 20px 0;
        box-shadow: 0 2px 8px rgba(33, 150, 243, 0.2);
    }
    
    .selected-summary h4 {
        margin: 0 0 15px 0;
        color: #1976d2;
        font-weight: 700;
        display: flex;
        align-items: center;
        gap: 8px;
    }
    
    .selected-summary h4::before {
        content: "📋";
        font-size: 20px;
    }
    
    .form-actions {
        display: flex;
        gap: 15px;
        justify-content: flex-end;
        margin-top: 30px;
        padding-top: 25px;
        border-top: 2px solid #dee2e6;
    }
    
    .form-actions .btn {
        padding: 12px 24px;
        font-weight: 600;
        border-radius: 8px;
        transition: all 0.3s ease;
    }
    
    .form-actions .btn:hover {
        transform: translateY(-1px);
        box-shadow: 0 4px 8px rgba(0,0,0,0.15);
    }
    
    .info-item {
        margin-bottom: 15px;
        padding: 10px;
        background: #f8f9fa;
        border-radius: 6px;
        border-left: 4px solid #007bff;
    }
    
    .info-label {
        font-weight: 700;
        color: #495057;
        margin-right: 8px;
        font-size: 14px;
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }
    
    .info-value {
        color: #6c757d;
        font-weight: 500;
        font-size: 16px;
    }
</style>

<script>
    // Update selected count and list
    function updateSelectedSummary() {
        const checkboxes = document.querySelectorAll('.suggestion-checkbox:checked');
        const count = checkboxes.length;
        const summary = document.getElementById('selectedSummary');
        const countSpan = document.getElementById('selectedCount');
        const listDiv = document.getElementById('selectedList');
        const createButton = document.getElementById('createButton');
        
        countSpan.textContent = count;
        
        if (count > 0) {
            summary.style.display = 'block';
            createButton.disabled = false;
            
            let listHtml = '<ul class="list-unstyled">';
            checkboxes.forEach(checkbox => {
                const timeDisplay = checkbox.closest('.suggestion-item').querySelector('.time-display').textContent;
                listHtml += `<li><i class="fas fa-check text-success"></i> ${timeDisplay}</li>`;
            });
            listHtml += '</ul>';
            listDiv.innerHTML = listHtml;
        } else {
            summary.style.display = 'none';
            createButton.disabled = true;
        }
    }
    
    // Select all available slots
    function selectAllAvailable() {
        const checkboxes = document.querySelectorAll('.suggestion-checkbox');
        checkboxes.forEach(checkbox => {
            checkbox.checked = true;
        });
        updateSelectedSummary();
    }
    
    // Clear all selections
    function clearAll() {
        const checkboxes = document.querySelectorAll('.suggestion-checkbox');
        checkboxes.forEach(checkbox => {
            checkbox.checked = false;
        });
        updateSelectedSummary();
    }
    
    // Add event listeners
    document.addEventListener('DOMContentLoaded', function() {
        const checkboxes = document.querySelectorAll('.suggestion-checkbox');
        checkboxes.forEach(checkbox => {
            checkbox.addEventListener('change', updateSelectedSummary);
        });
    });
</script>

<jsp:include page="../layout/admin-footer.jsp" />
