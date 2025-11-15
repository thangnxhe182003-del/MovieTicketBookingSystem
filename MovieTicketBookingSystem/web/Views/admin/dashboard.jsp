<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Dashboard"/>
    <jsp:param name="pageSubtitle" value="Trang tổng quan hệ thống MovieNow"/>
    <jsp:param name="currentPage" value="dashboard"/>
    <jsp:param name="extraStyles" value="
        <style>
            .dashboard-container {
                max-width: 1400px;
                margin: 0 auto;
                padding: 20px;
            }
            
            .welcome-section {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 30px 40px;
                border-radius: 12px;
                margin-bottom: 30px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }
            
            .welcome-section h1 {
                font-size: 28px;
                margin: 0 0 8px 0;
                font-weight: 600;
            }
            
            .welcome-section p {
                font-size: 15px;
                opacity: 0.95;
                margin: 0;
            }
            
            .dashboard-stats {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
                gap: 20px;
                margin-bottom: 30px;
            }
            
            .stat-card {
                background: white;
                padding: 25px;
                border-radius: 12px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.08);
                border-left: 4px solid;
                transition: transform 0.2s ease, box-shadow 0.2s ease;
            }
            
            .stat-card:hover {
                transform: translateY(-3px);
                box-shadow: 0 4px 15px rgba(0,0,0,0.12);
            }
            
            .stat-card.primary {
                border-left-color: #667eea;
            }
            
            .stat-card.success {
                border-left-color: #48bb78;
            }
            
            .stat-card.warning {
                border-left-color: #ed8936;
            }
            
            .stat-card.info {
                border-left-color: #4299e1;
            }
            
            .stat-header {
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 15px;
            }
            
            .stat-icon {
                font-size: 36px;
                opacity: 0.8;
            }
            
            .stat-number {
                font-size: 32px;
                font-weight: 700;
                color: #2d3748;
                margin-bottom: 5px;
            }
            
            .stat-label {
                font-size: 14px;
                color: #718096;
                font-weight: 500;
            }
            
            .statistics-section {
                background: white;
                border-radius: 12px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.08);
                overflow: hidden;
                margin-bottom: 30px;
            }
            
            .section-header {
                background: #f7fafc;
                padding: 20px 25px;
                border-bottom: 1px solid #e2e8f0;
            }
            
            .section-header h3 {
                margin: 0;
                font-size: 18px;
                font-weight: 600;
                color: #2d3748;
            }
            
            .section-body {
                padding: 25px;
            }
            
            .filter-controls {
                display: flex;
                gap: 15px;
                margin-bottom: 25px;
                flex-wrap: wrap;
                align-items: center;
                padding-bottom: 20px;
                border-bottom: 2px solid #e2e8f0;
            }
            
            .filter-group {
                display: flex;
                gap: 10px;
                align-items: center;
            }
            
            .filter-btn {
                padding: 10px 20px;
                border: 2px solid #cbd5e0;
                background: white;
                color: #4a5568;
                border-radius: 8px;
                cursor: pointer;
                font-weight: 500;
                font-size: 14px;
                transition: all 0.2s ease;
            }
            
            .filter-btn:hover {
                border-color: #667eea;
                color: #667eea;
                background: #f7f9ff;
            }
            
            .filter-btn.active {
                background: #667eea;
                color: white;
                border-color: #667eea;
            }
            
            .filter-input {
                padding: 10px 15px;
                border: 2px solid #e2e8f0;
                border-radius: 8px;
                font-size: 14px;
                width: 80px;
            }
            
            .filter-input:focus {
                outline: none;
                border-color: #667eea;
            }
            
            .stats-filter-section {
                background: white;
                padding: 20px 25px;
                border-radius: 12px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.08);
                margin-bottom: 30px;
            }
            
            .stats-filter-controls {
                display: flex;
                align-items: center;
                flex-wrap: wrap;
                gap: 10px;
            }
            
            .chart-container {
                position: relative;
                height: 400px;
                margin-top: 20px;
                padding: 20px;
                background: #f7fafc;
                border-radius: 8px;
            }
            
            .summary-stats {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 20px;
                margin-top: 25px;
                padding-top: 25px;
                border-top: 2px solid #e2e8f0;
            }
            
            .summary-card {
                background: #f7fafc;
                padding: 20px;
                border-radius: 8px;
                border-left: 4px solid #667eea;
            }
            
            .summary-label {
                font-size: 13px;
                color: #718096;
                margin-bottom: 8px;
                font-weight: 500;
            }
            
            .summary-value {
                font-size: 24px;
                font-weight: 700;
                color: #2d3748;
            }
            
            @media (max-width: 1024px) {
                .dashboard-stats {
                    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                }
            }
            
            @media (max-width: 768px) {
                .stat-number {
                    font-size: 28px;
                }
                
                .welcome-section h1 {
                    font-size: 24px;
                }
                
                .filter-controls {
                    flex-direction: column;
                    align-items: stretch;
                }
                
                .filter-group {
                    width: 100%;
                    justify-content: space-between;
                }
            }
        </style>
    "/>
</jsp:include>

<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>

<div class="dashboard-container">
    <!-- WELCOME SECTION -->
    <div class="welcome-section">
        <h1>🎬 Chào mừng đến với Admin Dashboard</h1>
        <p>Quản lý hệ thống rạp chiếu phim MovieNow một cách hiệu quả</p>
    </div>

    <!-- STATS -->
    <div class="dashboard-stats">
        <div class="stat-card primary">
            <div class="stat-header">
                <div>
                    <div class="stat-number" id="statOrderCount">
                        <c:choose>
                            <c:when test="${not empty todayStats}">
                                <fmt:formatNumber value="${todayStats.orderCount}" pattern="#,###"/>
                            </c:when>
                            <c:otherwise>0</c:otherwise>
                        </c:choose>
                    </div>
                    <div class="stat-label">Đơn đặt vé</div>
                </div>
                <div class="stat-icon">🎫</div>
            </div>
        </div>
        
        <div class="stat-card success">
            <div class="stat-header">
                <div>
                    <div class="stat-number" id="statTicketRevenue">
                        <c:choose>
                            <c:when test="${not empty todayStats}">
                                ₫<fmt:formatNumber value="${todayStats.ticketRevenue}" pattern="#,###"/>
                            </c:when>
                            <c:otherwise>₫0</c:otherwise>
                        </c:choose>
                    </div>
                    <div class="stat-label">Doanh thu vé</div>
                </div>
                <div class="stat-icon">💰</div>
            </div>
        </div>
        
        <div class="stat-card warning">
            <div class="stat-header">
                <div>
                    <div class="stat-number" id="statComboRevenue">
                        <c:choose>
                            <c:when test="${not empty todayStats}">
                                ₫<fmt:formatNumber value="${todayStats.comboRevenue}" pattern="#,###"/>
                            </c:when>
                            <c:otherwise>₫0</c:otherwise>
                        </c:choose>
                    </div>
                    <div class="stat-label">Doanh thu combo</div>
                </div>
                <div class="stat-icon">🍿</div>
            </div>
        </div>
        
        <div class="stat-card info">
            <div class="stat-header">
                <div>
                    <div class="stat-number" id="statTotalRevenue">
                        <c:choose>
                            <c:when test="${not empty todayStats}">
                                ₫<fmt:formatNumber value="${todayStats.totalRevenue}" pattern="#,###"/>
                            </c:when>
                            <c:otherwise>₫0</c:otherwise>
                        </c:choose>
                    </div>
                    <div class="stat-label">Tổng doanh thu</div>
                </div>
                <div class="stat-icon">💵</div>
            </div>
        </div>
    </div>
    
    <!-- Date Filter for Stats -->
    <div class="stats-filter-section">
        <div class="stats-filter-controls">
            <label style="color: #4a5568; font-weight: 500; margin-right: 10px;">Lọc theo ngày:</label>
            <input type="date" id="statsStartDate" class="filter-input" style="width: 150px;">
            <span style="margin: 0 10px; color: #718096;">đến</span>
            <input type="date" id="statsEndDate" class="filter-input" style="width: 150px;">
            <button class="filter-btn" onclick="loadStatsByDateRange()" style="margin-left: 10px;">Áp dụng</button>
            <button class="filter-btn" onclick="resetStatsToToday()" style="background: #e2e8f0; border-color: #e2e8f0; color: #4a5568;">Hôm nay</button>
        </div>
    </div>

    <!-- STATISTICS SECTION -->
    <div class="statistics-section">
        <div class="section-header">
            <h3>📊 Thống kê doanh thu</h3>
        </div>
        <div class="section-body">
            <!-- Filter Controls -->
            <div class="filter-controls">
                <div class="filter-group">
                    <button class="filter-btn active" data-period="daily" onclick="loadStatistics('daily')">Theo ngày</button>
                    <button class="filter-btn" data-period="weekly" onclick="loadStatistics('weekly')">Theo tuần</button>
                    <button class="filter-btn" data-period="monthly" onclick="loadStatistics('monthly')">Theo tháng</button>
                </div>
            </div>
            
            <!-- Chart Container -->
            <div class="chart-container">
                <canvas id="revenueChart"></canvas>
            </div>
            
            <!-- Summary Stats -->
            <div class="summary-stats" id="summaryStats">
                <div class="summary-card">
                    <div class="summary-label">Tổng doanh thu vé</div>
                    <div class="summary-value" id="totalTicketRevenue">₫0</div>
                </div>
                <div class="summary-card">
                    <div class="summary-label">Tổng doanh thu combo</div>
                    <div class="summary-value" id="totalComboRevenue">₫0</div>
                </div>
                <div class="summary-card">
                    <div class="summary-label">Tổng doanh thu</div>
                    <div class="summary-value" id="totalRevenue">₫0</div>
                </div>
                <div class="summary-card">
                    <div class="summary-label">Số đơn hàng</div>
                    <div class="summary-value" id="totalOrders">0</div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    let revenueChart = null;
    let currentPeriod = 'daily';
    let currentDays = 7;
    
    // Initialize chart with default data
    document.addEventListener('DOMContentLoaded', function() {
        // Set default date filter to today
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('statsStartDate').value = today;
        document.getElementById('statsEndDate').value = today;
        
        const chartData = [
            <c:choose>
            <c:when test="${not empty chartData}">
                <c:forEach var="data" items="${chartData}" varStatus="status">
                {
                    label: '<c:choose><c:when test="${chartType == 'daily'}">${data.date}</c:when><c:when test="${chartType == 'weekly'}">${data.week}</c:when><c:otherwise>${data.date}</c:otherwise></c:choose>',
                    orderCount: ${data.orderCount != null ? data.orderCount : 0}
                }<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            </c:when>
            <c:otherwise>
                { label: 'Không có dữ liệu', orderCount: 0 }
            </c:otherwise>
            </c:choose>
        ];
        
        if (chartData.length > 0 && chartData[0].label !== 'Không có dữ liệu') {
            initializeChart(chartData);
        } else {
            document.querySelector('.chart-container').innerHTML = 
                '<div style="text-align: center; padding: 50px; color: #718096;">Chưa có dữ liệu thống kê</div>';
        }
        
        // Set initial summary stats
        <c:if test="${not empty todayStats}">
        updateSummaryStats({
            ticketRevenue: ${todayStats.ticketRevenue != null ? todayStats.ticketRevenue : 0},
            comboRevenue: ${todayStats.comboRevenue != null ? todayStats.comboRevenue : 0},
            totalRevenue: ${todayStats.totalRevenue != null ? todayStats.totalRevenue : 0},
            orderCount: ${todayStats.orderCount != null ? todayStats.orderCount : 0}
        });
        </c:if>
    });
    
    function initializeChart(data) {
        const ctx = document.getElementById('revenueChart').getContext('2d');
        
        if (revenueChart) {
            revenueChart.destroy();
        }
        
        const labels = data.map(item => item.label);
        const orderCountData = data.map(item => parseInt(item.orderCount || 0));
        
        revenueChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: 'Số đơn hàng',
                        data: orderCountData,
                        backgroundColor: 'rgba(102, 126, 234, 0.8)',
                        borderColor: 'rgb(102, 126, 234)',
                        borderWidth: 1,
                        borderRadius: 4
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return 'Số đơn hàng: ' + new Intl.NumberFormat('vi-VN').format(context.parsed.y);
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1,
                            callback: function(value) {
                                return new Intl.NumberFormat('vi-VN').format(value);
                            }
                        }
                    }
                }
            }
        });
    }
    
    function loadStatistics(period) {
        currentPeriod = period;
        
        // Fixed: always show 7 items
        let days = 7;
        if (period === 'weekly') {
            days = 49; // 7 weeks = 49 days
        } else if (period === 'monthly') {
            days = 210; // 7 months = ~210 days
        }
        
        // Update active button
        document.querySelectorAll('.filter-btn[data-period]').forEach(btn => {
            btn.classList.remove('active');
            if (btn.dataset.period === period) {
                btn.classList.add('active');
            }
        });
        
        // Show loading
        const chartContainer = document.querySelector('.chart-container');
        chartContainer.innerHTML = '<div style="text-align: center; padding: 50px; color: #718096;">Đang tải dữ liệu...</div>';
        
        // Fetch data
        let url = 'admin-dashboard?action=getStatistics&period=' + period;
        if (period === 'daily' || period === 'weekly') {
            url += '&days=' + days;
        } else if (period === 'monthly') {
            url += '&months=' + 7; // 7 months
        }
        
        fetch(url)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Update chart
                    if (data.chartData && data.chartData.length > 0) {
                        let chartData = data.chartData.map(item => {
                            let label = '';
                            if (period === 'daily') {
                                label = new Date(item.date).toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit' });
                            } else if (period === 'weekly') {
                                label = item.week || '';
                            } else if (period === 'monthly') {
                                label = item.month || '';
                            }
                            return {
                                label: label,
                                orderCount: parseInt(item.orderCount || 0)
                            };
                        });
                        
                        // Data is already limited to 7 items from backend
                        chartContainer.innerHTML = '<canvas id="revenueChart"></canvas>';
                        initializeChart(chartData);
                    } else {
                        chartContainer.innerHTML = '<div style="text-align: center; padding: 50px; color: #718096;">Chưa có dữ liệu thống kê</div>';
                    }
                    
                    // Update summary stats
                    if (data.summaryStats) {
                        updateSummaryStats(data.summaryStats);
                    }
                } else {
                    chartContainer.innerHTML = '<div style="text-align: center; padding: 50px; color: #e53e3e;">Lỗi: ' + (data.error || 'Không thể tải dữ liệu') + '</div>';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                chartContainer.innerHTML = '<div style="text-align: center; padding: 50px; color: #e53e3e;">Lỗi khi tải dữ liệu</div>';
            });
    }
    
    function loadStatsByDateRange() {
        const startDate = document.getElementById('statsStartDate').value;
        const endDate = document.getElementById('statsEndDate').value;
        
        if (!startDate || !endDate) {
            alert('Vui lòng chọn cả ngày bắt đầu và ngày kết thúc');
            return;
        }
        
        if (new Date(startDate) > new Date(endDate)) {
            alert('Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc');
            return;
        }
        
        // Show loading
        document.getElementById('statOrderCount').textContent = '...';
        document.getElementById('statTicketRevenue').textContent = '₫...';
        document.getElementById('statComboRevenue').textContent = '₫...';
        document.getElementById('statTotalRevenue').textContent = '₫...';
        
        // Fetch stats
        fetch('admin-dashboard?action=getStatistics&period=dateRange&startDate=' + startDate + '&endDate=' + endDate)
            .then(response => response.json())
            .then(data => {
                if (data.success && data.summaryStats) {
                    updateStatsCards(data.summaryStats);
                } else {
                    alert('Không thể tải dữ liệu');
                    resetStatsToToday();
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Lỗi khi tải dữ liệu');
                resetStatsToToday();
            });
    }
    
    function resetStatsToToday() {
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('statsStartDate').value = today;
        document.getElementById('statsEndDate').value = today;
        
        // Reload today's stats
        <c:if test="${not empty todayStats}">
        updateStatsCards({
            ticketRevenue: ${todayStats.ticketRevenue != null ? todayStats.ticketRevenue : 0},
            comboRevenue: ${todayStats.comboRevenue != null ? todayStats.comboRevenue : 0},
            totalRevenue: ${todayStats.totalRevenue != null ? todayStats.totalRevenue : 0},
            orderCount: ${todayStats.orderCount != null ? todayStats.orderCount : 0}
        });
        </c:if>
    }
    
    function updateStatsCards(stats) {
        document.getElementById('statOrderCount').textContent = 
            new Intl.NumberFormat('vi-VN').format(parseInt(stats.orderCount || 0));
        document.getElementById('statTicketRevenue').textContent = 
            '₫' + new Intl.NumberFormat('vi-VN').format(parseFloat(stats.ticketRevenue || 0));
        document.getElementById('statComboRevenue').textContent = 
            '₫' + new Intl.NumberFormat('vi-VN').format(parseFloat(stats.comboRevenue || 0));
        document.getElementById('statTotalRevenue').textContent = 
            '₫' + new Intl.NumberFormat('vi-VN').format(parseFloat(stats.totalRevenue || 0));
    }
    
    function updateSummaryStats(stats) {
        document.getElementById('totalTicketRevenue').textContent = 
            '₫' + new Intl.NumberFormat('vi-VN').format(parseFloat(stats.ticketRevenue || 0));
        document.getElementById('totalComboRevenue').textContent = 
            '₫' + new Intl.NumberFormat('vi-VN').format(parseFloat(stats.comboRevenue || 0));
        document.getElementById('totalRevenue').textContent = 
            '₫' + new Intl.NumberFormat('vi-VN').format(parseFloat(stats.totalRevenue || 0));
        document.getElementById('totalOrders').textContent = 
            new Intl.NumberFormat('vi-VN').format(parseInt(stats.orderCount || 0));
    }
</script>
