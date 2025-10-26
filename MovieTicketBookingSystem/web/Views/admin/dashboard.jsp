<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Dashboard"/>
    <jsp:param name="pageSubtitle" value="Trang tổng quan hệ thống MovieNow"/>
    <jsp:param name="currentPage" value="dashboard"/>
    <jsp:param name="extraStyles" value="
        <style>
            .dashboard-stats {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                gap: 20px;
                margin-bottom: 30px;
            }
            
            .stat-card {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 30px;
                border-radius: 12px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                position: relative;
                overflow: hidden;
                transition: transform 0.3s ease;
            }
            
            .stat-card:hover {
                transform: translateY(-5px);
            }
            
            .stat-card::before {
                content: '';
                position: absolute;
                top: -50%;
                right: -50%;
                width: 200%;
                height: 200%;
                background: rgba(255,255,255,0.1);
                border-radius: 50%;
                pointer-events: none;
            }
            
            .stat-card.primary {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            }
            
            .stat-card.success {
                background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            }
            
            .stat-card.warning {
                background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            }
            
            .stat-card.info {
                background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
            }
            
            .stat-icon {
                font-size: 48px;
                margin-bottom: 15px;
                opacity: 0.9;
            }
            
            .stat-number {
                font-size: 36px;
                font-weight: 700;
                margin-bottom: 5px;
            }
            
            .stat-label {
                font-size: 14px;
                opacity: 0.9;
                text-transform: uppercase;
                letter-spacing: 1px;
            }
            
            .dashboard-content {
                display: grid;
                grid-template-columns: 2fr 1fr;
                gap: 30px;
                margin-bottom: 30px;
            }
            
            .content-card {
                background: white;
                border-radius: 12px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                overflow: hidden;
            }
            
            .content-header {
                background: #f8f9fa;
                padding: 20px;
                border-bottom: 1px solid #e9ecef;
            }
            
            .content-header h3 {
                margin: 0;
                font-size: 18px;
                font-weight: 600;
                color: #333;
            }
            
            .content-body {
                padding: 20px;
            }
            
            .feature-list {
                list-style: none;
                padding: 0;
                margin: 0;
            }
            
            .feature-list li {
                padding: 12px 0;
                border-bottom: 1px solid #f0f0f0;
                display: flex;
                align-items: center;
                gap: 15px;
            }
            
            .feature-list li:last-child {
                border-bottom: none;
            }
            
            .feature-icon {
                width: 40px;
                height: 40px;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                border-radius: 8px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 18px;
            }
            
            .feature-info h4 {
                margin: 0 0 5px 0;
                font-size: 15px;
                font-weight: 600;
                color: #333;
            }
            
            .feature-info p {
                margin: 0;
                font-size: 13px;
                color: #666;
            }
            
            .quick-actions {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
                gap: 15px;
            }
            
            .quick-action-btn {
                background: white;
                border: 2px solid #e9ecef;
                padding: 20px;
                border-radius: 8px;
                text-align: center;
                text-decoration: none;
                color: #333;
                transition: all 0.3s ease;
                display: flex;
                flex-direction: column;
                align-items: center;
                gap: 10px;
            }
            
            .quick-action-btn:hover {
                border-color: #667eea;
                background: #f8f9ff;
                transform: translateY(-2px);
            }
            
            .quick-action-icon {
                font-size: 32px;
                color: #667eea;
            }
            
            .quick-action-label {
                font-size: 13px;
                font-weight: 500;
            }
            
            .welcome-section {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 40px;
                border-radius: 12px;
                margin-bottom: 30px;
            }
            
            .welcome-section h1 {
                font-size: 32px;
                margin: 0 0 10px 0;
            }
            
            .welcome-section p {
                font-size: 16px;
                opacity: 0.9;
                margin: 0;
            }
            
            @media (max-width: 1024px) {
                .dashboard-content {
                    grid-template-columns: 1fr;
                }
                
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
            }
        </style>
    "/>
</jsp:include>

<!-- WELCOME SECTION -->
<div class="welcome-section">
    <h1>🎬 Chào mừng đến với Admin Dashboard</h1>
    <p>Quản lý hệ thống rạp chiếu phim MovieNow một cách hiệu quả</p>
</div>

<!-- STATS -->
<div class="dashboard-stats">
    <div class="stat-card primary">
        <div class="stat-icon">🎫</div>
        <div class="stat-number">1,234</div>
        <div class="stat-label">Đơn đặt vé hôm nay</div>
    </div>
    
    <div class="stat-card success">
        <div class="stat-icon">👥</div>
        <div class="stat-number">5,678</div>
        <div class="stat-label">Tổng số khách hàng</div>
    </div>
    
    <div class="stat-card warning">
        <div class="stat-icon">🎬</div>
        <div class="stat-number">45</div>
        <div class="stat-label">Phim đang chiếu</div>
    </div>
    
    <div class="stat-card info">
        <div class="stat-icon">💰</div>
        <div class="stat-number">₫15,234,567</div>
        <div class="stat-label">Doanh thu hôm nay</div>
    </div>
</div>

<!-- DASHBOARD CONTENT -->
<div class="dashboard-content">
    <!-- FEATURES -->
    <div class="content-card">
        <div class="content-header">
            <h3>📋 Tính năng hệ thống</h3>
        </div>
        <div class="content-body">
            <ul class="feature-list">
                <li>
                    <div class="feature-icon">🎬</div>
                    <div class="feature-info">
                        <h4>Quản lý phim</h4>
                        <p>Thêm, sửa, xóa phim và quản lý thông tin chi tiết</p>
                    </div>
                </li>
                <li>
                    <div class="feature-icon">🪑</div>
                    <div class="feature-info">
                        <h4>Quản lý rạp phim</h4>
                        <p>Thiết lập rạp phim, phòng chiếu và ghế ngồi</p>
                    </div>
                </li>
                <li>
                    <div class="feature-icon">⏰</div>
                    <div class="feature-info">
                        <h4>Quản lý suất chiếu</h4>
                        <p>Lên lịch và điều phối các suất chiếu</p>
                    </div>
                </li>
                <li>
                    <div class="feature-icon">🎫</div>
                    <div class="feature-info">
                        <h4>Quản lý đặt vé</h4>
                        <p>Theo dõi và xử lý đơn đặt vé của khách hàng</p>
                    </div>
                </li>
                <li>
                    <div class="feature-icon">👥</div>
                    <div class="feature-info">
                        <h4>Quản lý người dùng</h4>
                        <p>Quản lý khách hàng và nhân viên trong hệ thống</p>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    
    <!-- QUICK ACTIONS -->
    <div class="content-card">
        <div class="content-header">
            <h3>⚡ Thao tác nhanh</h3>
        </div>
        <div class="content-body">
            <div class="quick-actions">
                <a href="admin-movies" class="quick-action-btn">
                    <div class="quick-action-icon">🎬</div>
                    <div class="quick-action-label">Quản lý phim</div>
                </a>
                
                <a href="admin-showtimes" class="quick-action-btn">
                    <div class="quick-action-icon">⏰</div>
                    <div class="quick-action-label">Suất chiếu</div>
                </a>
                
                <a href="admin-customers" class="quick-action-btn">
                    <div class="quick-action-icon">👥</div>
                    <div class="quick-action-label">Khách hàng</div>
                </a>
                
                <a href="admin-staff" class="quick-action-btn">
                    <div class="quick-action-icon">👔</div>
                    <div class="quick-action-label">Nhân viên</div>
                </a>
                
                <a href="admin-rooms" class="quick-action-btn">
                    <div class="quick-action-icon">🪑</div>
                    <div class="quick-action-label">Phòng ghế</div>
                </a>
                
                <a href="admin-cinema" class="quick-action-btn">
                    <div class="quick-action-icon">🏢</div>
                    <div class="quick-action-label">Rạp phim</div>
                </a>
            </div>
        </div>
    </div>
</div>

<!-- RECENT ACTIVITY -->
<div class="content-card">
    <div class="content-header">
        <h3>📊 Hoạt động gần đây</h3>
    </div>
    <div class="content-body">
        <p style="text-align: center; color: #666; padding: 40px 0;">
            Tính năng này sẽ hiển thị các hoạt động gần đây trong hệ thống
        </p>
    </div>
</div>

</div>
</div>
