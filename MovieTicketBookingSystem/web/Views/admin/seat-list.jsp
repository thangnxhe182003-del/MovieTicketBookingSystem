<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Quản lý ghế - Phòng ${room.tenPhong}"/>
    <jsp:param name="pageSubtitle" value="Sơ đồ ghế & quản lý chi tiết"/>
    <jsp:param name="currentPage" value="rooms"/>
    <jsp:param name="extraStyles" value="
        <style>
            /* ==== COMMON ==== */
            .container { padding:30px; font-family:'Segoe UI',sans-serif; }
            .back-btn { display:inline-block; margin-bottom:16px; padding:8px 16px;
                        background:#6c757d; color:#fff; text-decoration:none; border-radius:6px; }
            h2 { margin:0 0 20px; color:#212529; }

            .grid { display:grid; grid-template-columns:1fr 2.5fr; gap:24px; }
            .card { background:#fff; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.1); overflow:hidden; }
            .card-header { padding:16px 24px; background:linear-gradient(135deg,#e50914,#c90812);
                           color:#fff; font-weight:700; font-size:18px; }
            .card-body { padding:24px; }

            /* ==== FORM ==== */
            .form-group { margin-bottom:16px; }
            label { display:block; font-weight:600; margin-bottom:6px; color:#333; }
            input, select { width:100%; padding:10px 12px; border:1px solid #ddd; border-radius:6px; }
            .btn { padding:10px 20px; border:none; border-radius:6px; font-weight:600; cursor:pointer; }
            .btn-primary { background:linear-gradient(135deg,#e50914,#c90812); color:#fff; }
            .btn-primary:hover { background:linear-gradient(135deg,#c90812,#a0070f); }
            .btn-sm { padding:6px 12px; font-size:12px; }

            /* ==== SEAT DIAGRAM ==== */
            .seat-diagram { margin-top:20px; }
            .seat-screen { background:#333; color:#fff; text-align:center; padding:12px;
                           border-radius:8px; font-weight:bold; margin-bottom:20px; }
            .seat-row { display:flex; justify-content:center; gap:6px; margin-bottom:8px; align-items:center; }
            .seat-label { width:30px; font-weight:bold; color:#495057; text-align:right; }
            .seat { width:40px; height:40px; border-radius:8px; display:flex;
                    align-items:center; justify-content:center; font-size:12px; font-weight:600;
                    cursor:pointer; transition:0.2s; box-shadow:0 1px 3px rgba(0,0,0,0.2); }
            .seat.available   { background:#28a745; color:#fff; }
            .seat.booked      { background:#dc3545; color:#fff; }
            .seat.maintenance { background:#6c757d; color:#fff; }
            .seat:hover { transform:scale(1.1); }

            .legend { display:flex; justify-content:center; gap:20px; margin-top:20px; font-size:14px; }
            .legend-item { display:flex; align-items:center; gap:6px; }
            .legend-color { width:16px; height:16px; border-radius:4px; }

            /* ==== TABLE ==== */
            table { width:100%; border-collapse:collapse; margin-top:20px; }
            th { background:#f8f9fa; padding:12px; text-align:left; border-bottom:2px solid #e9ecef; font-weight:600; }
            td { padding:10px 12px; border-bottom:1px solid #e9ecef; vertical-align:middle; }
            .actions { display:flex; gap:6px; }

            /* ==== MODAL ==== */
            .modal { display:none; position:fixed; inset:0; background:rgba(0,0,0,0.5);
                     align-items:center; justify-content:center; z-index:2000; }
            .modal-content { background:#fff; width:500px; max-width:90vw; border-radius:12px;
                             box-shadow:0 10px 30px rgba(0,0,0,0.3); z-index:2001; }
            .modal-header { padding:16px 20px; background:#f8f9fa; border-bottom:1px solid #eee;
                            display:flex; justify-content:space-between; align-items:center; }
            .modal-title { font-weight:700; font-size:18px; }
            .modal-close { background:none; border:none; font-size:20px; cursor:pointer; color:#999; }
            .modal-body { padding:20px; }
            .modal-footer { padding:16px 20px; background:#f8f9fa; display:flex;
                            justify-content:flex-end; gap:10px; border-top:1px solid #eee; }

            /* ==== RESPONSIVE ==== */
            @media (max-width:992px) {
                .grid { grid-template-columns:1fr; }
                .grid > :nth-child(2) { order:-1; }   /* form lên trên */
            }
        </style>
    "/>
</jsp:include>

<div class="container">
    <a href="admin-rooms" class="back-btn">Quay lại danh sách phòng</a>
    <h2>Phòng: ${room.tenPhong} (Mã: ${room.maPhong})</h2>

    <c:if test="${not empty param.message}">
        <div class="alert ${param.type == 'error' ? 'alert-danger' : 'alert-success'}"
             style="padding:12px 16px; border-radius:6px; margin-bottom:20px; border:1px solid; display:flex; align-items:center; gap:8px;">
            <i class="fas fa-${param.type == 'error' ? 'exclamation-triangle' : 'check-circle'}"></i>
            ${param.message}
        </div>
    </c:if>

    <div class="grid">
        <!-- ==== CỘT 1: FORM ==== -->
        <div>
            <!-- Thêm ghế -->
            <div class="card">
                <div class="card-header">Thêm ghế mới</div>
                <div class="card-body">
                    <form action="admin-rooms" method="POST">
                        <input type="hidden" name="action" value="addSeat"/>
                        <input type="hidden" name="maPhong" value="${room.maPhong}"/>
                        <div class="form-group"><label>Hàng ghế (A-Z)</label><input name="hangGhe" maxlength="1" required placeholder="A"/></div>
                        <div class="form-group"><label>Số ghế</label><input name="soGhe" type="number" min="1" required placeholder="1"/></div>
                        <div class="form-group"><label>Loại ghế</label>
                            <select name="loaiGhe">
                                <option value="Thuong">Thường</option>
                                <option value="VIP">VIP</option>
                                <option value="Couple">Couple</option>
                            </select>
                        </div>
                        <div class="form-group"><label>Trạng thái</label>
                            <select name="trangThai">
                                <option value="Có sẵn">Có sẵn</option>
                                <option value="Đã đặt">Đã đặt</option>
                                <option value="Bảo trì">Bảo trì</option>
                            </select>
                        </div>
                        <div class="form-group"><label>Ghi chú</label><input name="ghiChu" placeholder="VD: Gần lối đi"/></div>
                        <button type="submit" class="btn btn-primary">Thêm ghế</button>
                    </form>
                </div>
            </div>

            <!-- Sinh ghế hàng loạt -->
            <div class="card" style="margin-top:20px;">
                <div class="card-header">Sinh ghế hàng loạt</div>
                <div class="card-body">
                    <form action="admin-rooms" method="POST">
                        <input type="hidden" name="action" value="bulkGenerate"/>
                        <input type="hidden" name="maPhong" value="${room.maPhong}"/>
                        <div class="form-group"><label>Từ hàng</label><input name="fromRow" maxlength="1" required placeholder="A"/></div>
                        <div class="form-group"><label>Đến hàng</label><input name="toRow" maxlength="1" required placeholder="C"/></div>
                        <div class="form-group"><label>Từ số</label><input name="fromNum" type="number" min="1" value="1" required/></div>
                        <div class="form-group"><label>Đến số</label><input name="toNum" type="number" min="1" value="10" required/></div>
                        <div class="form-group"><label>Loại ghế</label>
                            <select name="loaiGhe">
                                <option value="Thuong">Thường</option>
                                <option value="VIP">VIP</option>
                                <option value="Couple">Couple</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary">Sinh ghế</button>
                    </form>
                </div>
            </div>
        </div>

        <!-- ==== CỘT 2: SƠ ĐỒ + BẢNG ==== -->
        <div style="display:flex; flex-direction:column; gap:24px;">
            <!-- SƠ ĐỒ -->
            <div class="card">
                <div class="card-header">Sơ đồ ghế ngồi</div>
                <div class="card-body" style="max-height:520px; overflow:auto;">
                    <div class="seat-screen">MÀN HÌNH</div>
                    <div class="seat-diagram">
                        <c:set var="rows" value="${fn:split('A,B,C,D,E,F,G,H,I,J,K', ',')}"/>
                        <c:set var="maxCol" value="12"/>
                        <c:forEach var="row" items="${rows}">
                            <div class="seat-row">
                                <div class="seat-label">${row}</div>
                                <c:forEach var="col" begin="1" end="${maxCol}">
                                    <c:set var="seatCode" value="${row}${String.format('%02d', col)}"/>
                                    <c:set var="seat" value="${null}"/>
                                    <c:forEach var="s" items="${seats}">
                                        <c:if test="${s.fullSeatCode == seatCode}">
                                            <c:set var="seat" value="${s}"/>
                                        </c:if>
                                    </c:forEach>
                                    <c:choose>
                                        <c:when test="${seat != null}">
                                            <div class="seat ${seat.trangThai == 'Có sẵn' ? 'available' : (seat.trangThai == 'Đã đặt' ? 'booked' : 'maintenance')}"
                                                 onclick="openEditModal(${seat.maGhe}, '${seat.fullSeatCode}', '${seat.loaiGhe}', '${seat.trangThai}', '${seat.ghiChu}')">
                                                ${seatCode}
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="seat" style="background:#f8f9fa; color:#ccc; border:1px dashed #ddd; cursor:default;">
                                                ${seatCode}
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="legend">
                        <div class="legend-item"><div class="legend-color" style="background:#28a745;"></div><span>Có sẵn</span></div>
                        <div class="legend-item"><div class="legend-color" style="background:#dc3545;"></div><span>Đã đặt</span></div>
                        <div class="legend-item"><div class="legend-color" style="background:#6c757d;"></div><span>Bảo trì</span></div>
                    </div>
                </div>
            </div>

            <!-- BẢNG DANH SÁCH -->
            <div class="card" style="flex:1; overflow:auto;">
                <div class="card-header">Danh sách ghế</div>
                <div class="card-body">
                    <table>
                        <thead>
                            <tr>
                                <th>Mã</th><th>Ghế</th><th>Loại</th><th>Trạng thái</th><th>Ghi chú</th><th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="seat" items="${seats}">
                                <tr>
                                    <td>${seat.maGhe}</td>
                                    <td><strong>${seat.fullSeatCode}</strong></td>
                                    <td>${seat.loaiGhe}</td>
                                    <td>
                                        <span style="padding:4px 8px; border-radius:4px; font-size:12px;
                                            background:${seat.trangThai == 'Có sẵn' ? '#d4edda' : (seat.trangThai == 'Đã đặt' ? '#f8d7da' : '#e2e3e5')};
                                            color:${seat.trangThai == 'Có sẵn' ? '#155724' : (seat.trangThai == 'Đã đặt' ? '#721c24' : '#383d41')}">
                                            ${seat.trangThai}
                                        </span>
                                    </td>
                                    <td>${seat.ghiChu}</td>
                                    <td class="actions">
                                        <button class="btn btn-primary btn-sm"
                                                onclick="openEditModal(${seat.maGhe}, '${seat.fullSeatCode}', '${seat.loaiGhe}', '${seat.trangThai}', '${seat.ghiChu}')">Sửa</button>
                                        <form action="admin-rooms" method="POST" style="display:inline;"
                                              onsubmit="return confirm('Xóa ghế ${seat.fullSeatCode}?')">
                                            <input type="hidden" name="action" value="deleteSeat"/>
                                            <input type="hidden" name="maGhe" value="${seat.maGhe}"/>
                                            <input type="hidden" name="maPhong" value="${room.maPhong}"/>
                                            <button type="submit" class="btn btn-danger btn-sm">Xóa</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- ==== MODAL ==== -->
<div id="editModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <div class="modal-title">Chỉnh sửa ghế</div>
            <button class="modal-close" onclick="closeModal()">×</button>
        </div>
        <form action="admin-rooms" method="POST">
            <div class="modal-body">
                <input type="hidden" name="action" value="updateSeat"/>
                <input type="hidden" name="maGhe" id="edit_maGhe"/>
                <input type="hidden" name="maPhong" value="${room.maPhong}"/>
                <div class="form-group"><label>Ghế</label><input type="text" id="edit_seatCode" disabled style="background:#f8f9fa;"/></div>
                <div class="form-group"><label>Loại ghế</label>
                    <select name="loaiGhe" id="edit_loaiGhe">
                        <option value="Thuong">Thường</option>
                        <option value="VIP">VIP</option>
                        <option value="Couple">Couple</option>
                    </select>
                </div>
                <div class="form-group"><label>Trạng thái</label>
                    <select name="trangThai" id="edit_trangThai">
                        <option value="Có sẵn">Có sẵn</option>
                        <option value="Đã đặt">Đã đặt</option>
                        <option value="Bảo trì">Bảo trì</option>
                    </select>
                </div>
                <div class="form-group"><label>Ghi chú</label><input name="ghiChu" id="edit_ghiChu"/></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeModal()">Hủy</button>
                <button type="submit" class="btn btn-primary">Lưu</button>
            </div>
        </form>
    </div>
</div>

<script>
    function openEditModal(maGhe, seatCode, loaiGhe, trangThai, ghiChu) {
        document.getElementById('edit_maGhe').value = maGhe;
        document.getElementById('edit_seatCode').value = seatCode;
        document.getElementById('edit_loaiGhe').value = loaiGhe;
        document.getElementById('edit_trangThai').value = trangThai;
        document.getElementById('edit_ghiChu').value = ghiChu || '';
        document.getElementById('editModal').style.display = 'flex';
    }
    function closeModal() { document.getElementById('editModal').style.display = 'none'; }
    window.onclick = e => { if (e.target === document.getElementById('editModal')) closeModal(); };
</script>

<jsp:include page="../layout/admin-footer.jsp" />