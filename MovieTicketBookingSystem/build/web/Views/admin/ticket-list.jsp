<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Quản lý vé"/>
    <jsp:param name="pageSubtitle" value="Theo ngày · phim · suất chiếu"/>
    <jsp:param name="currentPage" value="tickets"/>
    <jsp:param name="extraStyles" value="
        <style>
            .table-container { padding: 0; }
            .table-header { background:#f8f9fa; padding:20px 30px; border-bottom:1px solid #e9ecef; display:flex; justify-content:space-between; align-items:center; }
            .table-title { font-size:18px; font-weight:600; color:#333; margin:0; }
            .data-table { width:100%; border-collapse: collapse; }
            .data-table th { background:#f8f9fa; padding:12px 16px; text-align:left; font-weight:600; color:#333; border-bottom:2px solid #e9ecef; }
            .data-table td { padding:12px 16px; border-bottom:1px solid #e9ecef; vertical-align: middle; }
            .search-filter { background:#f8f9fa; padding:15px 30px; border-bottom:1px solid #e9ecef; display:flex; gap:12px; align-items:center; flex-wrap:wrap; }
            .search-box { flex:1; max-width: 300px; }
            .search-box input, .filter-select { width:100%; padding:8px 12px; border:1px solid #ddd; border-radius:6px; font-size:14px; background:#fff; }
            .filter-group { display:flex; gap:12px; align-items:center; flex:1; }
            .filter-item { min-width:180px; }
            .status-badge { padding:4px 12px; border-radius:20px; font-size:12px; font-weight:600; }
            .status-paid { background:#d4edda; color:#155724; }
            .status-used { background:#e2e3e5; color:#5a5c69; }
            .status-failed { background:#f8d7da; color:#721c24; }
            .action-buttons { display:flex; gap:8px; }
            .btn-action { padding:6px 12px; border:none; border-radius:4px; text-decoration:none; font-size:12px; font-weight:600; cursor:pointer; transition:.3s; }
            .btn-checkin { background:#17a2b8; color:#fff; }
            .btn-checkin:hover { background:#138496; }
            .btn-view { background:#6c757d; color:#fff; }
            .btn-view:hover { background:#5a6268; }
            .btn-reset { background:#adb5bd; color:#fff; }
            .btn-reset:hover { background:#9aa1a8; }

            /* Modal */
            .modal-backdrop { position: fixed; inset: 0; background: rgba(0,0,0,.5); display:none; align-items:center; justify-content:center; z-index:9999; }
            .modal { width: 90%; max-width: 920px; background:#fff; border-radius:12px; overflow:hidden; box-shadow:0 15px 40px rgba(0,0,0,.2); }
            .modal-header { padding:14px 16px; border-bottom:1px solid #eee; display:flex; justify-content:space-between; align-items:center; }
            .modal-body { padding:16px; }
            .modal-close { background:none; border:none; font-size:20px; cursor:pointer; }
            .grid-2 { display:grid; grid-template-columns: 280px 1fr; gap:16px; }
            .poster { width:100%; height: 380px; object-fit: cover; border-radius:8px; background:#f0f0f0; }
            .kv { color:#555; font-size:14px; }
            .kv strong { color:#222; }
            .list { margin-top:12px; }
            .list h4 { margin: 12px 0 8px 0; }
            .combo-item { display:flex; align-items:center; gap:10px; padding:6px 0; border-bottom:1px dashed #eee; }
            .combo-thumb { width:48px; height:48px; object-fit:cover; border-radius:6px; background:#f0f0f0; }
        </style>
    "/>
</jsp:include>

<div class="table-container">
    <form class="search-filter" method="get" action="admin-tickets">
        <div class="filter-group">
            <div class="filter-item">
                <select class="filter-select" name="status">
                    <option value="" ${empty filterStatus ? 'selected' : ''}>Trạng thái: Tất cả</option>
                    <option value="Đã thanh toán" ${filterStatus == 'Đã thanh toán' ? 'selected' : ''}>Đã thanh toán</option>
                    <option value="Thanh toan tien mat" ${filterStatus == 'Thanh toan tien mat' ? 'selected' : ''}>Thanh toán tiền mặt</option>
                    <option value="Đã sử dụng" ${filterStatus == 'Đã sử dụng' ? 'selected' : ''}>Đã sử dụng</option>
                    <option value="Thanh toán thất bại" ${filterStatus == 'Thanh toán thất bại' ? 'selected' : ''}>Thanh toán thất bại</option>
                </select>
            </div>
            <div class="filter-item">
                <select class="filter-select" name="maPhim">
                    <option value="">Phim: Tất cả</option>
                    <c:forEach var="movie" items="${movies}">
                        <option value="${movie.maPhim}" ${selectedMaPhim == movie.maPhim ? 'selected' : ''}>${movie.tenPhim}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="filter-item">
                <select class="filter-select" name="maRap">
                    <option value="">Rạp: Tất cả</option>
                    <c:forEach var="cinema" items="${cinemas}">
                        <option value="${cinema.maRap}" ${selectedMaRap == cinema.maRap ? 'selected' : ''}>${cinema.tenRap}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="filter-item">
                <input type="date" class="filter-select" name="ngay" value="${selectedNgay}" placeholder="Ngày chiếu">
            </div>
            <div class="filter-item">
                <select class="filter-select" name="from">
                    <option value="today" ${from == 'today' ? 'selected' : ''}>Từ hôm nay</option>
                    <option value="all" ${from == 'all' ? 'selected' : ''}>Tất cả ngày</option>
                </select>
            </div>
        </div>
        <button type="submit" class="btn-action" style="background:#e50914; color:#fff; padding:8px 20px;">Lọc</button>
        <a href="admin-tickets" class="btn-action btn-reset" style="padding:8px 20px;">Reset</a>
    </form>

    <div class="table-header">
        <h2 class="table-title">Danh sách vé theo ngày · phim · suất</h2>
    </div>

    <c:choose>
        <c:when test="${empty groups}">
            <div style="text-align:center; padding:60px 20px; color:#6c757d;">Không có đơn vé nào.</div>
        </c:when>
        <c:otherwise>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Ngày</th>
                        <th>Phim</th>
                        <th>Suất</th>
                        <th>Số vé</th>
                        <th>Trạng thái</th>
                        <th>MaOrder</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="g" items="${groups}">
                        <c:set var="resolvedStatus" value="${empty orderStatusMap[g.maOrder] ? g.trangThai : orderStatusMap[g.maOrder]}"/>
                        <tr>
                            <td>${g.ngayChieuStr}</td>
                            <td>${g.tenPhim}</td>
                            <td>${g.gioBatDau} - ${g.gioKetThuc}</td>
                            <td>${g.soVe}</td>
                            <td>
                                <c:set var="displayStatus" value="${resolvedStatus == 'Thanh toan tien mat' ? 'Thanh toán tiền mặt' : resolvedStatus}"/>
                                <span class="status-badge 
                                    ${('Đã thanh toán' == resolvedStatus || 'Thanh toan tien mat' == resolvedStatus) ? 'status-paid' : ('Đã sử dụng' == resolvedStatus ? 'status-used' : 'status-failed')}">${displayStatus}</span>
                            </td>
                            <td>#${g.maOrder}</td>
                            <td>
                                <div class="action-buttons">
                                    <button type="button" class="btn-action btn-view" onclick="openDetails(${g.maOrder})"><i class="fas fa-eye"></i> View</button>
                                    <%
                                        dal.TicketDAO.GroupedTicketsRow gRow = (dal.TicketDAO.GroupedTicketsRow) pageContext.getAttribute("g");
                                        java.time.LocalDateTime currentTime = (java.time.LocalDateTime) pageContext.getAttribute("currentTime");
                                        boolean isPastShowtime = false;
                                        if (gRow != null && gRow.getGioBatDauDateTime() != null && currentTime != null) {
                                            isPastShowtime = gRow.getGioBatDauDateTime().isBefore(currentTime);
                                        }
                                        pageContext.setAttribute("isPastShowtime", isPastShowtime);
                                    %>
                                    <c:if test="${resolvedStatus == 'Đã thanh toán' || resolvedStatus == 'Thanh toan tien mat'}">
                                        <c:choose>
                                            <c:when test="${isPastShowtime}">
                                                <span class="btn-action" style="background:#6c757d; color:#fff; cursor:default; opacity:0.7;" title="Suất chiếu đã qua - không thể check-in">
                                                    <i class="fas fa-ban"></i> Đã qua
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <a class="btn-action btn-checkin" href="admin-tickets?action=checkin&maOrder=${g.maOrder}" onclick="return confirm('Xác nhận check-in đơn #${g.maOrder}? Tất cả vé sẽ chuyển sang Đã sử dụng.');">Check-in</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<!-- Modal chi tiết -->
<div id="detailBackdrop" class="modal-backdrop">
  <div class="modal">
    <div class="modal-header">
      <div id="detailTitle" style="font-weight:700;">Chi tiết đơn</div>
      <button class="modal-close" onclick="closeDetails()">×</button>
    </div>
    <div class="modal-body">
      <div class="grid-2">
        <div>
          <img id="detailPoster" class="poster" src="" alt="poster">
        </div>
        <div>
          <div class="kv">Mã đơn: <strong id="dMaOrder"></strong></div>
          <div class="kv">OrderCode: <strong id="dOrderCode"></strong></div>
          <div class="kv">Trạng thái: <strong id="dStatus"></strong></div>
          <div class="kv">Thanh toán: <strong id="dPaidAt"></strong></div>
          <div class="kv">Tổng tiền: <strong id="dTotal"></strong></div>
          <div class="kv">Phim: <strong id="dMovie"></strong></div>
          <div class="kv">Rạp/Phòng: <strong id="dCinemaRoom"></strong></div>
          <div class="kv">Suất/Ngày: <strong id="dShow"></strong></div>

          <div class="list">
            <h4>Vé</h4>
            <div id="dTickets"></div>
          </div>

          <div class="list">
            <h4>Combo</h4>
            <div id="dCombos"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script>
  function openDetails(maOrder) {
    fetch('admin-tickets?action=details&maOrder=' + maOrder)
      .then(r => r.json())
      .then(data => {
        document.getElementById('detailBackdrop').style.display = 'flex';
        document.getElementById('detailTitle').textContent = 'Chi tiết đơn #' + data.maOrder;
        document.getElementById('dMaOrder').textContent = '#' + data.maOrder;
        document.getElementById('dOrderCode').textContent = data.orderCode || '';
        document.getElementById('dStatus').textContent = data.trangThai || '';
        document.getElementById('dPaidAt').textContent = data.paidAt || '';
        document.getElementById('dTotal').textContent = '₫' + (data.tongTien || 0).toLocaleString('vi-VN');

        const show = data.showtime || {};
        document.getElementById('dMovie').textContent = show.movieTitle || '';
        document.getElementById('detailPoster').src = (show.poster ? ('${pageContext.request.contextPath}/assets/image/' + show.poster) : '');
        document.getElementById('dCinemaRoom').textContent = (show.cinema || '') + (show.room ? (' · Phòng ' + show.room) : '');
        document.getElementById('dShow').textContent = (show.start || '') + (show.date ? (' · ' + show.date) : '');

        const tWrap = document.getElementById('dTickets');
        tWrap.innerHTML = '';
        (data.tickets || []).forEach(t => {
          const div = document.createElement('div');
          div.textContent = 'Ghế ' + (t.soGhe || '') + ' · ₫' + (t.donGia || 0).toLocaleString('vi-VN');
          tWrap.appendChild(div);
        });

        const cWrap = document.getElementById('dCombos');
        cWrap.innerHTML = '';
        (data.items || []).forEach(i => {
          const row = document.createElement('div');
          row.className = 'combo-item';
          const img = document.createElement('img');
          img.className = 'combo-thumb';
          if (i.thumb) img.src = '${pageContext.request.contextPath}/assets/image/' + i.thumb; else img.style.display = 'none';
          const text = document.createElement('div');
          text.textContent = i.name + ' x' + i.qty + ' — ₫' + (i.subtotal || 0).toLocaleString('vi-VN');
          row.appendChild(img); row.appendChild(text);
          cWrap.appendChild(row);
        });
      })
      .catch(() => alert('Không tải được chi tiết đơn.'));
  }
  function closeDetails(){ document.getElementById('detailBackdrop').style.display = 'none'; }
</script>

<jsp:include page="../layout/admin-footer.jsp" />
