<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Quản lý ghế - Phòng ${room.tenPhong}"/>
    <jsp:param name="pageSubtitle" value="Thêm/sửa/xóa ghế và sinh ghế hàng loạt"/>
    <jsp:param name="currentPage" value="rooms"/>
    <jsp:param name="extraStyles" value="
        <style>
            .container { padding:30px; }
            .grid { display:grid; grid-template-columns: 1fr 2fr; gap:24px; }
            .card { background:#fff; border-radius:8px; box-shadow:0 2px 6px rgba(0,0,0,0.1); }
            .card-header { padding:16px 24px; border-bottom:1px solid #e9ecef; background:#f8f9fa; font-weight:700; }
            .card-body { padding:24px; }
            .form-group { margin-bottom:12px; }
            label { display:block; font-weight:600; margin-bottom:6px; }
            input, select { width:100%; padding:10px 12px; border:1px solid #ddd; border-radius:6px; }
            table { width:100%; border-collapse:collapse; }
            th { background:#f8f9fa; padding:10px; text-align:left; border-bottom:2px solid #e9ecef; }
            td { padding:10px; border-bottom:1px solid #e9ecef; }
            .actions { display:flex; gap:8px; }
            .btn { padding:10px 16px; border:none; border-radius:6px; font-weight:600; cursor:pointer; }
            .btn-primary { background:linear-gradient(135deg,#e50914,#c90812); color:#fff; }
            .btn-secondary { background:#6c757d; color:#fff; }
            .btn-danger { background:#dc3545; color:#fff; }
            .btn-sm { padding:6px 10px; font-size:12px; }
        </style>
    "/>
</jsp:include>

<div class="container">
    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 24px;">
        <div style="display: flex; align-items: center; gap: 16px;">
            <a href="admin-rooms" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Quay lại
            </a>
            <h2 style="margin: 0;">Phòng: ${room.tenPhong} (Mã: ${room.maPhong})</h2>
        </div>
    </div>
    
    <c:if test="${not empty param.message}">
        <div class="alert alert-success" style="margin-bottom: 20px; padding: 12px 16px; background: #d4edda; color: #155724; border: 1px solid #c3e6cb; border-radius: 6px;">
            <i class="fas fa-check-circle"></i> ${param.message}
        </div>
    </c:if>

    <div class="grid">
        <div class="card">
            <div class="card-header">Thêm ghế</div>
            <div class="card-body">
                <form action="admin-rooms" method="POST">
                    <input type="hidden" name="action" value="addSeat" />
                    <input type="hidden" name="maPhong" value="${room.maPhong}" />
                    <div class="form-group">
                        <label for="hangGhe">Hàng ghế (A-Z)</label>
                        <input id="hangGhe" name="hangGhe" maxlength="1" required />
                    </div>
                    <div class="form-group">
                        <label for="soGhe">Số ghế</label>
                        <input id="soGhe" name="soGhe" type="number" min="1" required />
                    </div>
                    <div class="form-group">
                        <label for="loaiGhe">Loại ghế</label>
                        <select id="loaiGhe" name="loaiGhe">
                            <option value="Thuong">Thường</option>
                            <option value="VIP">VIP</option>
                            <option value="Couple">Couple</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="trangThai">Trạng thái</label>
                        <select id="trangThai" name="trangThai">
                            <option value="Có sẵn">Có sẵn</option>
                            <option value="Đã đặt">Đã đặt</option>
                            <option value="Bảo trì">Bảo trì</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="ghiChu">Ghi chú</label>
                        <input id="ghiChu" name="ghiChu" />
                    </div>
                    <button class="btn btn-primary" type="submit">Thêm ghế</button>
                </form>
            </div>
        </div>

        <div class="card">
            <div class="card-header">Sinh ghế hàng loạt</div>
            <div class="card-body">
                <form action="admin-rooms" method="POST">
                    <input type="hidden" name="action" value="bulkGenerate" />
                    <input type="hidden" name="maPhong" value="${room.maPhong}" />
                    <div class="form-group">
                        <label>Hàng từ (A) đến (K)</label>
                        <div style="display:flex; gap:8px;">
                            <input name="fromRow" maxlength="1" placeholder="A" required />
                            <input name="toRow" maxlength="1" placeholder="K" required />
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Số ghế từ (1) đến (12)</label>
                        <div style="display:flex; gap:8px;">
                            <input name="fromNum" type="number" min="1" placeholder="1" required />
                            <input name="toNum" type="number" min="1" placeholder="12" required />
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Loại ghế</label>
                        <select name="loaiGhe">
                            <option value="Thuong">Thường</option>
                            <option value="VIP">VIP</option>
                            <option value="Couple">Couple</option>
                        </select>
                    </div>
                    <button class="btn btn-primary" type="submit">Sinh ghế</button>
                </form>
            </div>
        </div>
    </div>

    <!-- So do ghe kieu CGV -->
    <div class="card" style="margin-top:24px;">
        <div class="card-header">Sơ đồ mô phỏng</div>
        <div class="card-body" style="display:flex; justify-content:center;">
            <style>
                .screen-bar { width: 100%; height: 14px; background: linear-gradient(180deg,#ddd,#bbb); border-radius: 8px; margin: 0 auto 18px; box-shadow: inset 0 -2px 6px rgba(0,0,0,0.15); }
                .seat-grid { display: inline-block; padding: 16px; background: #f8f9fa; border-radius: 12px; box-shadow: inset 0 0 0 1px #e9ecef; }
                .seat-layout { display:flex; flex-direction:column; align-items:center; justify-content:center; width: 100%; }
                .seat-layout .screen-bar { max-width: 720px; }
                .row-wrap { display: flex; align-items: center; gap: 10px; margin: 6px 0; }
                .row-label { width: 26px; text-align: center; font-weight: 700; color: #7f8c8d; }
                .seat { position: relative; width: 36px; height: 36px; border-radius: 6px; background: #e0f2f1; color: #00695c; border: 1px solid #b2dfdb; display: flex; align-items: center; justify-content: center; font-size: 12px; font-weight: bold; cursor: pointer; transition: transform .1s ease, box-shadow .2s ease; }
                .seat:hover { transform: translateY(-2px); box-shadow: 0 4px 10px rgba(0,0,0,0.15); }
                .seat.vip { background: #ffe8cc; color: #8a4b08; border-color: #ffd8a8; }
                .seat.couple { background: #e7e1ff; color: #5f3dc4; border-color: #dcd2ff; width: 74px; }
                .seat.blocked, .seat.booked { background: #ececec; color: #9e9e9e; border-color: #e0e0e0; cursor: not-allowed; }
                .corner { position: absolute; top: -6px; right: -6px; }
                .corner button { width: 18px; height: 18px; border: none; border-radius: 50%; background: #fff; color: #555; box-shadow: 0 1px 3px rgba(0,0,0,0.2); cursor: pointer; font-size: 10px; }
                .legend { display:flex; gap:14px; align-items:center; margin-top: 16px; color:#666; }
                .legend .badge { display:inline-block; width:16px; height:16px; border-radius:4px; vertical-align:middle; margin-right:6px; }
                .badge-normal { background:#e0f2f1; border:1px solid #b2dfdb; }
                .badge-vip { background:#ffe8cc; border:1px solid #ffd8a8; }
                .badge-couple { background:#e7e1ff; border:1px solid #dcd2ff; }
                .badge-booked { background:#ececec; border:1px solid #e0e0e0; }
            </style>
            <div class="seat-layout">
                <div class="screen-bar" title="Màn hình"></div>
                <div id="seatGrid" class="seat-grid"></div>
            </div>
            <div class="legend" style="justify-content:center;">
                <span><span class="badge badge-normal"></span>Thường</span>
                <span><span class="badge badge-vip"></span>VIP</span>
                <span><span class="badge badge-couple"></span>Couple</span>
                <span><span class="badge badge-booked"></span>Đã đặt/Bảo trì</span>
            </div>
            <script>
                (function(){
                    const seats = [
                        <c:forEach var="s" items="${seats}" varStatus="st">
                            { maGhe: ${s.maGhe}, row: '${s.hangGhe}', col: ${s.soGhe}, loai: '${s.loaiGhe}', trangThai: '${s.trangThai}' }${st.last ? '' : ','}
                        </c:forEach>
                    ];
                    // Chỉ lấy các hàng và cột thực tế từ dữ liệu trong DB
                    const rowSet = new Set(seats.map(s => (s.row||'').toUpperCase()));
                    const rows = Array.from(rowSet).sort();
                    
                    const colSet = new Set(seats.map(s => s.col||0));
                    const cols = Array.from(colSet).sort((a,b) => a-b);
                    const maxCol = Math.max(...cols);
                    const gridEl = document.getElementById('seatGrid');
                    gridEl.innerHTML = '';
                    const byKey = new Map(seats.map(s => [s.row + '-' + s.col, s]));

                    rows.forEach(r => {
                        const wrap = document.createElement('div');
                        wrap.className = 'row-wrap';
                        const label = document.createElement('div');
                        label.className = 'row-label';
                        label.textContent = r;
                        wrap.appendChild(label);
                        const rowFrag = document.createDocumentFragment();
                        for (let c = 1; c <= maxCol; c++) {
                            const data = byKey.get(r + '-' + c);
                            const btn = document.createElement('div');
                            const code = r + (c < 10 ? ('0' + c) : c);
                            btn.className = 'seat';
                            if (data) {
                                if ((data.loai||'').toLowerCase() === 'vip') btn.classList.add('vip');
                                if ((data.loai||'').toLowerCase() === 'couple') btn.classList.add('couple');
                                const st = (data.trangThai||'').toLowerCase();
                                if (st === 'đã đặt' || st === 'da dat' || st === 'bao tri' || st === 'bảo trì') btn.classList.add('booked');
                                btn.title = `#${data.maGhe} • ${data.loai||'Thuong'} • ${data.trangThai||'Có sẵn'}`;
                            } else {
                                btn.title = 'Trống (chưa tồn tại)';
                                // Không thay đổi opacity để giữ style đồng nhất
                            }
                            btn.textContent = code;

                            // Nút góc để action nhanh - TẤT CẢ GHẾ ĐỀU CÓ ICON
                            const corner = document.createElement('div');
                            corner.className = 'corner';
                            const more = document.createElement('button');
                            more.type = 'button';
                            more.innerHTML = '⋮';
                            more.title = 'Tùy chọn';
                            corner.appendChild(more);
                            btn.appendChild(corner);
                            
                            if (data) {
                                // Ghế đã tồn tại - sửa/xóa
                                more.onclick = function(){ openSeatModal(data, code); };
                                btn.onclick = function(){ openSeatModal(data, code); };
                            } else {
                                // Ghế chưa tồn tại - thêm mới
                                more.onclick = function(){ openSeatModal({ maGhe: '', row: r, col: c, loai: 'Thuong', trangThai: 'Có sẵn' }, code, true); };
                                btn.onclick = function(){ openSeatModal({ maGhe: '', row: r, col: c, loai: 'Thuong', trangThai: 'Có sẵn' }, code, true); };
                            }

                            rowFrag.appendChild(btn);
                        }
                        wrap.appendChild(rowFrag);
                        gridEl.appendChild(wrap);
                    });

                    function nextStatus(cur){
                        const order = ['Có sẵn','Đã đặt','Bảo trì'];
                        const i = order.findIndex(x => x.toLowerCase() === (cur||'').toLowerCase());
                        return order[(i+1) % order.length];
                    }

                    let modal, modalClose, modalTitle, f_maGhe, f_hangGhe, f_soGhe, f_loaiGhe, f_trangThai, f_ghiChu, btnDelete;
                    function ensureModalRefs(){
                        if (modal) return;
                        modal = document.getElementById('seatModal');
                        modalClose = document.getElementById('modalClose');
                        modalTitle = document.getElementById('modalTitle');
                        f_maGhe = document.getElementById('f_maGhe');
                        f_hangGhe = document.getElementById('f_hangGhe');
                        f_soGhe = document.getElementById('f_soGhe');
                        f_loaiGhe = document.getElementById('f_loaiGhe');
                        f_trangThai = document.getElementById('f_trangThai');
                        f_ghiChu = document.getElementById('f_ghiChu');
                        btnDelete = document.getElementById('btnDelete');
                        if (!modal) return;
                        modalClose.onclick = function(){ modal.style.display = 'none'; };
                        modal.onclick = function(e){ if (e.target === modal) modal.style.display = 'none'; };
                        // Nút "Xóa mềm" - Chỉ cập nhật trạng thái thành "Bảo trì"
                        btnDelete.onclick = function(){
                            if (confirm('Đặt ghế này vào trạng thái bảo trì (xóa mềm)?')) {
                                f_trangThai.value = 'Bảo trì';
                                document.getElementById('seatForm').submit();
                            }
                        };
                    }

                    function openSeatModal(data, code, isCreate){
                        ensureModalRefs();
                        if (!modal) return;
                        modal.style.display = 'flex';
                        modalTitle.textContent = isCreate ? ('Thêm ghế ' + code) : ('Ghế ' + code + ' (#' + data.maGhe + ')');
                        f_maGhe.value = data.maGhe || '';
                        f_hangGhe.value = data.row;
                        f_soGhe.value = data.col;
                        // Set giá trị cho hidden input để submit
                        document.getElementById('f_hangGhe_hidden').value = data.row;
                        document.getElementById('f_soGhe_hidden').value = data.col;
                        f_loaiGhe.value = data.loai || 'Thuong';
                        f_trangThai.value = data.trangThai || 'Có sẵn';
                        f_ghiChu.value = '';
                        // Đổi action form giữa updateSeat và addSeat
                        var form = document.getElementById('seatForm');
                        if (isCreate) {
                            form.action = 'admin-rooms';
                            form.querySelector('input[name="action"]').value = 'addSeat';
                            btnDelete.style.display = 'none';
                        } else {
                            form.action = 'admin-rooms';
                            form.querySelector('input[name="action"]').value = 'updateSeat';
                            btnDelete.style.display = '';
                        }
                    }
                })();
            </script>
        </div>
    </div>

    <!-- Modal chỉnh sửa/Xóa ghế -->
    <div id="seatModal" style="position:fixed; inset:0; background:rgba(0,0,0,.45); display:none; align-items:center; justify-content:center; z-index:2000;">
        <div style="background:#fff; width:540px; max-width:90vw; border-radius:12px; box-shadow:0 12px 40px rgba(0,0,0,.25); overflow:hidden;">
            <div style="padding:14px 18px; background:#f8f9fa; border-bottom:1px solid #e9ecef; display:flex; align-items:center; justify-content:space-between;">
                <strong id="modalTitle">Ghế</strong>
                <button id="modalClose" style="border:none; background:transparent; font-size:18px; cursor:pointer;">✕</button>
            </div>
            <div style="padding:18px;">
                <form id="seatForm" action="admin-rooms" method="POST" style="display:grid; grid-template-columns:1fr 1fr; gap:14px;">
                    <input type="hidden" name="action" value="updateSeat" />
                    <input type="hidden" name="maGhe" id="f_maGhe" />
                    <input type="hidden" name="maPhong" value="${room.maPhong}" />
                    <input type="hidden" name="hangGhe" id="f_hangGhe_hidden" />
                    <input type="hidden" name="soGhe" id="f_soGhe_hidden" />
                    <div>
                        <label>Hàng ghế</label>
                        <input id="f_hangGhe" readonly disabled style="background:#f8f9fa; color:#6c757d;" />
                    </div>
                    <div>
                        <label>Số ghế</label>
                        <input id="f_soGhe" readonly disabled style="background:#f8f9fa; color:#6c757d;" />
                    </div>
                    <div>
                        <label>Loại ghế</label>
                        <select id="f_loaiGhe" name="loaiGhe">
                            <option value="Thuong">Thường</option>
                            <option value="VIP">VIP</option>
                            <option value="Couple">Couple</option>
                        </select>
                    </div>
                    <div>
                        <label>Trạng thái</label>
                        <select id="f_trangThai" name="trangThai">
                            <option value="Có sẵn">Có sẵn</option>
                            <option value="Đã đặt">Đã đặt</option>
                            <option value="Bảo trì">Bảo trì</option>
                        </select>
                    </div>
                    <div class="full" style="grid-column:1/-1;">
                        <label>Ghi chú</label>
                        <input id="f_ghiChu" name="ghiChu" />
                    </div>
                    <div class="full" style="grid-column:1/-1; display:flex; gap:10px; justify-content:flex-end; margin-top:16px;">
                        <button type="button" id="btnDelete" class="btn btn-danger btn-sm">
                            <i class="fas fa-ban"></i> Xóa mềm (Bảo trì)
                        </button>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> Lưu
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../layout/admin-footer.jsp" />


