<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8"/>
        <title>Admin - Dashboard</title>
        <style>
            /* Reset nhẹ */
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
                font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            }
            body {
                background: #f5f7fb;
                color: #222;
            }
            a {
                color: #2b7be4;
                text-decoration: none;
            }

            /* Layout */
            .container {
                display: flex;
                min-height: 100vh;
            }
            .sidebar {
                width: 260px;
                background: linear-gradient(180deg,#0f2540, #072033);
                color: #fff;
                padding: 24px 16px;
            }
            .sidebar .brand {
                font-weight: 700;
                font-size: 20px;
                margin-bottom: 18px;
                letter-spacing: 1px;
            }
            .sidebar .user {
                background: rgba(255,255,255,0.04);
                padding: 10px;
                border-radius: 8px;
                margin-bottom: 12px;
                display:flex;
                align-items:center;
                gap:10px;
            }
            .avatar {
                width:36px;
                height:36px;
                border-radius:50%;
                background:#fff3;
                display:inline-block;
                text-align:center;
                line-height:36px;
                font-weight:700;
                color:#fff;
            }

            .nav-section {
                margin-top: 12px;
            }
            .nav-section h4 {
                font-size:13px;
                color:#cfe6ff;
                margin:8px 6px;
                font-weight:600;
            }
            .nav-item {
                display:block;
                padding: 10px 10px;
                border-radius: 6px;
                color:#eaf6ff;
                margin:6px 2px;
                cursor:pointer;
            }
            .nav-item:hover {
                background: rgba(255,255,255,0.04);
            }

            /* Main content */
            .main {
                flex:1;
                padding: 20px 28px;
            }
            .topbar {
                display:flex;
                justify-content:space-between;
                align-items:center;
                margin-bottom: 18px;
            }
            .topbar h2 {
                color:#1f2937;
                font-size:20px;
            }
            .card-row {
                display:flex;
                gap:14px;
                margin-bottom: 18px;
                flex-wrap:wrap;
            }
            .stat-card {
                flex: 1 1 220px;
                background: #fff;
                border-radius:8px;
                padding: 14px;
                box-shadow: 0 1px 4px rgba(16,24,40,0.06);
                border-left: 4px solid #cbd5e1;
            }
            .stat-card.blue {
                border-left-color: #2b8cf7;
            }
            .stat-card.green {
                border-left-color: #16a34a;
            }
            .stat-card.orange {
                border-left-color: #f59e0b;
            }
            .stat-card.red {
                border-left-color: #ef4444;
            }
            .stat-card .label {
                font-size:13px;
                color:#6b7280;
            }
            .stat-card .value {
                font-size:20px;
                font-weight:700;
                margin-top:6px;
                color:#111827;
            }

            /* dashboard content area */
            .dashboard-grid {
                display:grid;
                grid-template-columns: 1fr 1fr;
                gap: 16px;
                margin-bottom: 18px;
            }
            .panel {
                background:#fff;
                border-radius:8px;
                padding:14px;
                box-shadow: 0 1px 6px rgba(16,24,40,0.06);
            }
            .panel h3 {
                font-size:15px;
                color:#111827;
                margin-bottom:10px;
            }
            .chart {
                height:220px;
                display:flex;
                align-items:center;
                justify-content:center;
            }

            /* tables */
            .tables {
                display:flex;
                gap:16px;
                margin-bottom: 40px;
            }
            .table {
                background:#fff;
                border-radius:8px;
                padding:14px;
                flex:1;
                box-shadow: 0 1px 6px rgba(16,24,40,0.06);
            }
            table {
                width:100%;
                border-collapse:collapse;
            }
            th, td {
                text-align:left;
                padding:10px 8px;
                border-bottom:1px solid #f1f5f9;
                font-size:13px;
                color:#374151;
            }
            th {
                color:#6b7280;
                font-weight:600;
                font-size:13px;
            }

            /* footer small */
            .footer-note {
                color:#6b7280;
                font-size:13px;
                margin-top:6px;
            }

            /* responsive */
            @media (max-width: 980px){
                .dashboard-grid {
                    grid-template-columns: 1fr;
                }
                .tables {
                    flex-direction:column;
                }
                .sidebar {
                    display:none;
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <aside class="sidebar" id="sidebar">
                <div class="brand">ADMIN</div>
                <div class="user">
                    <div class="avatar">A</div>
                    <div>
                        <div style="font-weight:700">Xin chào, Admin</div>
                        <div style="font-size:12px;color:#cfe6ff">admin@example.com</div>
                    </div>
                </div>

                <nav>
                    <div class="nav-section">
                        <h4>Tổng quan</h4>
                        <div class="nav-item" data-view="dashboard">Dashboard</div>
                    </div>

                    <div class="nav-section">
                        <h4>Quản lý</h4>
                        <div class="nav-item" data-view="film">Quản lý phim</div>
                        <div class="nav-item" data-view="theater">Quản lý rạp</div>
                        <div class="nav-item" data-view="room">Quản lý phòng chiếu</div>
                        <div class="nav-item" data-view="schedule">Quản lý lịch chiếu / suất chiếu</div>
                        <div class="nav-item" data-view="combo">Quản lý combo bắp nước</div>
                        <div class="nav-item" data-view="accounts">Quản lý tài khoản</div>
                        <div class="nav-item" data-view="pricing">Quản lý giá vé</div>
                        <div class="nav-item" data-view="reports">Quản lí báo cáo</div>
                        <div class="nav-item" data-view="promos">Quản lí khuyến mãi, voucher</div>
                    </div>
                </nav>
            </aside>

            <main class="main">
                <div class="topbar">
                    <h2>Dashboard - Tổng quan</h2>
                    <div style="font-size:13px;color:#6b7280">Hôm nay: <strong id="today">15/5/2024</strong></div>
                </div>

                <!-- statistic cards -->
                <div class="card-row">
                    <div class="stat-card blue">
                        <div class="label">Doanh thu trong ngày (15/5/2024)</div>
                        <div class="value" id="revToday">760,000</div>
                    </div>
                    <div class="stat-card green">
                        <div class="label">Khách hàng mới (T5/2024)</div>
                        <div class="value" id="newCust">0</div>
                    </div>
                    <div class="stat-card orange">
                        <div class="label">Tổng vé bán ra (T5/2024)</div>
                        <div class="value" id="tickets">9</div>
                    </div>
                    <div class="stat-card red">
                        <div class="label">Tổng doanh thu (T5/2024)</div>
                        <div class="value" id="revMonth">1,826,000</div>
                    </div>
                </div>

                <!-- charts -->
                <div class="dashboard-grid">
                    <div class="panel">
                        <h3>Top bài viết được xem nhiều nhất</h3>
                        <div class="chart">
                            <canvas id="barChart" width="500" height="220"></canvas>
                        </div>
                    </div>

                    <div class="panel">
                        <h3>Doanh thu theo tháng</h3>
                        <div class="chart">
                            <canvas id="lineChart" width="500" height="220"></canvas>
                        </div>
                    </div>
                </div>

                <!-- tables -->
                <div class="tables">
                    <div class="table">
                        <h3>Doanh thu theo phim</h3>
                        <table>
                            <thead>
                                <tr><th>Tên phim</th><th>Tổng vé bán ra</th><th>Tổng doanh thu</th></tr>
                            </thead>
                            <tbody id="movieTableBody">
                                <tr>
                                    <td>Monkey Man Bảo Thù</td>
                                    <td>5</td>
                                    <td>1,066,000</td>
                                </tr>
                                <tr>
                                    <td>Cái Giá Của Hạnh Phúc</td>
                                    <td>4</td>
                                    <td>760,000</td>
                                </tr>
                            </tbody>
                        </table>
                        <div class="footer-note">Xem tất cả | Trang 1</div>
                    </div>

                    <div class="table">
                        <h3>Doanh thu theo rạp</h3>
                        <table>
                            <thead><tr><th>Rạp chiếu</th><th>Tổng vé bán ra</th><th>Tổng doanh thu</th></tr></thead>
                            <tbody id="theaterTableBody">
                                <tr>
                                    <td>HCinema Aeon Hà Đông</td>
                                    <td>9</td>
                                    <td>1,826,000</td>
                                </tr>
                            </tbody>
                        </table>
                        <div class="footer-note">Xem tất cả | Trang 1</div>
                    </div>
                </div>

                <!-- placeholder for different management views -->
                <div id="viewContainer"></div>

            </main>
        </div>

        <script>
            // -------------------------
            // Simple chart drawing (no libs)
            // -------------------------
            function drawBarChart(canvas, labels, values) {
            const ctx = canvas.getContext('2d');
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            const padding = 30;
            const chartW = canvas.width - padding * 2;
            const chartH = canvas.height - padding * 2;
            const maxV = Math.max(...values, 1);
            const barW = chartW / values.length * 0.6;
            values.forEach((v, i) => {
            const x = padding + i * (chartW / values.length) + (chartW / values.length - barW) / 2;
            const h = (v / maxV) * (chartH - 20);
            const y = padding + (chartH - h);
            // bar
            ctx.fillStyle = '#93c5fd';
            roundRect(ctx, x, y, barW, h, 6, true, false);
            // label
            ctx.fillStyle = '#1f2937';
            ctx.font = '12px Arial';
            ctx.textAlign = 'center';
            ctx.fillText(labels[i], x + barW / 2, canvas.height - 8);
            });
            // y-axis guide
            ctx.strokeStyle = '#e6eefc';
            ctx.beginPath();
            ctx.moveTo(padding, padding);
            ctx.lineTo(padding, canvas.height - padding);
            ctx.stroke();
            function roundRect(ctx, x, y, w, h, r, fill, stroke) {
            if (w < 2 * r) r = w / 2;
            if (h < 2 * r) r = h / 2;
            ctx.beginPath();
            ctx.moveTo(x + r, y);
            ctx.arcTo(x + w, y, x + w, y + h, r);
            ctx.arcTo(x + w, y + h, x, y + h, r);
            ctx.arcTo(x, y + h, x, y, r);
            ctx.arcTo(x, y, x + w, y, r);
            ctx.closePath();
            if (fill) ctx.fill();
            if (stroke) ctx.stroke();
            }
            }

            function drawLineChart(canvas, labels, values) {
            const ctx = canvas.getContext('2d');
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            const padding  =  30;
            const chartW  =  canvas.width -  padding * 2;
            const chartH  =  canvas.height -  padding * 2;
            const maxV  =  Math.max(...values,  1);

            // grid
            ctx.strokeStyle  =  '#f3f4f6';
            for  (let i = 0; i <= 4; i++){
            const y  =  padding +  i * (chartH / 4);
            ctx.beginPath();
            ctx.moveTo(padding,  y);
            ctx.lineTo(canvas.width - padding,  y);
            ctx.stroke();
            }

            ctx.beginPath();
            for  (let i = 0; i < values.length; i++){
            const x  =  padding +  (i / (values.length - 1 ||  1)) * chartW;
            const y  =  padding +  (1 -  values[i] / maxV) *  chartH;
            if  (i === 0) ctx.moveTo(x, y);   else ctx.lineTo(x, y);
            }
            ctx.strokeStyle  =  '#fb7185';
            ctx.lineWidth  =  2;
            ctx.stroke();

            // points
            ctx.fillStyle  =  '#fff';
            ctx.strokeStyle  =  '#fb7185';
            for  (let i = 0; i < values.length; i++){
            const x  =  padding +  (i / (values.length - 1 ||  1)) * chartW;
            const y  =  padding +  (1 -  values[i] / maxV) *  chartH;
            ctx.beginPath();
            ctx.arc(x, y, 4, 0, Math.PI * 2);
            ctx.fill();
            ctx.stroke();
            }
            }

            // sample data
            const articleLabels  =  ['Điểm mặt l...',  'Chị Cứ Em...',  '15 phim bớ...',  'Top 10 phìm', '13 phim l...'];
            const articleViews  =  [24, 15, 12, 11, 10];

            const months  =  ['1/2024', '2/2024', '3/2024', '4/2024', '5/2024'];
            const monthRevenue  =  [0, 200000, 300000, 80000000, 0];  // mẫu

            // draw initial charts
            window.addEventListener('load',  function(){
            const bar  =  document.getElementById('barChart');
            drawBarChart(bar,  articleLabels,  articleViews);

            const line  =  document.getElementById('lineChart');
            drawLineChart(line,  months,  monthRevenue);
            });

            // -------------------------
            // Simple view switcher for sidebar
            // -------------------------
            const viewContainer  =  document.getElementById('viewContainer');
            document.querySelectorAll('.nav-item').forEach(it => {
            it.addEventListener('click',  ()  =>  {
            const view  =  it.getAttribute('data-view');
            if (view ===  'dashboard') {
            // scroll to top of main or clear view
            viewContainer.innerHTML  =  '';
            window.scrollTo({top:0, behavior:'smooth'});
            return;
            }
            // load a simple management form template
            viewContainer.innerHTML  =  renderManagementView(view);
            window.scrollTo({top: document.body.scrollHeight,  behavior:'smooth'});
            });
            });

            function renderManagementView(view) {
            const titleMap  =  {
            film:'Quản lý phim (Thêm / Sửa / Xóa / Tra cứu)',
                    theater:'Quản lý rạp (Thêm / Cập nhật / Xóa)',
                    room:'Quản lý phòng chiếu (Sơ đồ ghế)',
                    schedule:'Quản lý lịch chiếu / suất chiếu',
                    combo:'Quản lý combo bắp nước (CRUD)',
                    accounts:'Quản lý tài khoản (khách hàng, nhân viên, quản lý)',
                    pricing:'Quản lý giá vé',
                    reports:'Quản lý báo cáo',
                    promos:'Quản lý khuyến mãi / voucher'
            };
            const title  =  titleMap[view] ||  'Quản lý';
            return `
                    <div class="panel" style="margin-top:18px;">
                        <h3>${title}</h3>
                        <div style="display:flex; gap:12px; margin-bottom:10px;">
                            <button onclick="openCreate('${view}')" style="padding:8px 12px; border-radius:6px; border:1px solid #d1d5db; background:#fff; cursor:pointer;">Tạo mới</button>
                            <input id="search_${view}" placeholder="Tìm kiếm..." style="flex:1;padding:8px;border-radius:6px;border:1px solid #e6eefc;" />
                            <button onclick="doSearch('${view}')" style="padding:8px 12px;border-radius:6px;background:#2b8cf7;color:#fff;border:none;cursor:pointer;">Tìm</button>
                        </div>
                        <div id="manage_${view}">
                            <!-- danh sách mẫu -->
                            <table>
                                <thead><tr><th>Tên</th><th>Thông tin</th><th>Hành động</th></tr></thead>
                                <tbody>
                                    <tr><td>Mẫu A</td><td>Thông tin mẫu</td><td><button onclick="editItem(this)" style="margin-right:6px;">Sửa</button><button onclick="deleteItem(this)">Xóa</button></td></tr>
                                    <tr><td>Mẫu B</td><td>Thông tin mẫu</td><td><button onclick="editItem(this)" style="margin-right:6px;">Sửa</button><button onclick="deleteItem(this)">Xóa</button></td></tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                `;
            }

            function openCreate(view) {
                const cont = document.getElementById('manage_' + view);
                const formHtml = `
                    <div style="margin-top:12px;background:#fbfbfd;padding:12px;border-radius:8px;border:1px solid #eef2ff;">
                        <div style="display:flex;gap:8px;">
                            <input placeholder="Tên" id="new_name" style="flex:1;padding:8px;border-radius:6px;border:1px solid #e5e7eb;" />
                            <input placeholder="Thông tin" id="new_info" style="flex:2;padding:8px;border-radius:6px;border:1px solid #e5e7eb;" />
                            <button onclick="createItem()" style="padding:8px 12px;border-radius:6px;background:#10b981;color:#fff;border:none;cursor:pointer;">Lưu</button>
                            <button onclick="cancelCreate()" style="padding:8px 12px;border-radius:6px;background:#ef4444;color:#fff;border:none;cursor:pointer;">Hủy</button>
                        </div>
                    </div>
                `;
                cont.insertAdjacentHTML('afterbegin', formHtml);
            }

            function cancelCreate() {
                const forms = document.querySelectorAll('#manage_'+Array.from(document.querySelectorAll('.nav-item')).find(n=>n.classList.contains('active'))?.getAttribute('data-view')+' .panel');
                // simple remove first form if exists
                const firstForm = document.querySelector('#manage_film > div') || document.querySelector('#manage_theater > div') || document.querySelector('#manage_room > div');
                if(firstForm) firstForm.remove();
                // fallback: remove any inserted create box
                document.querySelectorAll('#manage_film > div, #manage_theater > div, #manage_room > div').forEach(d=>{
                    if(d.style && d.style.background === 'rgb(251, 251, 253)') d.remove();
                });
            }

            function createItem() {
                alert('Chức năng tạo mẫu (kết nối backend để lưu thực tế).');
            }

            function editItem(btn) {
                const tr = btn.closest('tr');
                const name = tr.children[0].innerText;
                const info = tr.children[1].innerText;
                const newName = prompt('Sửa tên', name);
                if(newName !== null) {
                    tr.children[0].innerText = newName;
                    tr.children[1].innerText = info + ' (đã sửa)';
                }
            }
            function deleteItem(btn) {
                if(confirm('Bạn có chắc muốn xóa mục này?')) {
                    const tr = btn.closest('tr');
                    tr.parentNode.removeChild(tr);
                }
            }
            function doSearch(view) {
                const q = document.getElementById('search_' + view).value.trim().toLowerCase();
                const tbody = document.querySelector('#manage_' + view + ' tbody');
                if(!tbody) return;
                Array.from(tbody.children).forEach(tr=>{
                    const text = tr.innerText.toLowerCase();
                    tr.style.display = text.indexOf(q) !== -1 ? '' : 'none';
                });
            }

        </script>
    </body>
</html>