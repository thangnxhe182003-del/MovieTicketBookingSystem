<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.pageTitle != null ? param.pageTitle : 'Admin Dashboard'} - MovieNow</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        :root {
            --primary-color: #e50914;
            --secondary-color: #221f1f;
            --accent-color: #564d4d;
            --light-bg: #f5f5f1;
            --dark-text: #221f1f;
            --light-text: #ffffff;
            --admin-sidebar: #2c3e50;
            --admin-content: #ecf0f1;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: var(--admin-content);
            color: var(--dark-text);
        }

        /* ==== ADMIN HEADER ==== */
        .admin-header {
            background: linear-gradient(135deg, var(--admin-sidebar), #34495e);
            color: var(--light-text);
            padding: 15px 30px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            z-index: 1000;
        }

        .admin-header-left {
            display: flex;
            align-items: center;
            gap: 20px;
        }

        .admin-logo {
            display: flex;
            align-items: center;
            gap: 10px;
            text-decoration: none;
            color: var(--light-text);
        }

        .admin-logo img {
            width: 40px;
            height: 40px;
            border-radius: 8px;
        }

        .admin-logo span {
            font-size: 20px;
            font-weight: 700;
            color: var(--primary-color);
        }

        .admin-breadcrumb {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 14px;
            color: #bdc3c7;
        }

        .admin-breadcrumb a {
            color: #bdc3c7;
            text-decoration: none;
        }

        .admin-breadcrumb a:hover {
            color: var(--light-text);
        }

        .admin-header-right {
            display: flex;
            align-items: center;
            gap: 20px;
        }

        .admin-user-info {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .admin-user-avatar {
            width: 35px;
            height: 35px;
            border-radius: 50%;
            background: var(--primary-color);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: 600;
        }

        .admin-logout {
            color: var(--light-text);
            text-decoration: none;
            padding: 8px 16px;
            border: 1px solid rgba(255,255,255,0.3);
            border-radius: 6px;
            transition: all 0.3s;
        }

        .admin-logout:hover {
            background: rgba(255,255,255,0.1);
            border-color: var(--primary-color);
        }

        /* ==== SIDEBAR ==== */
        .admin-sidebar {
            position: fixed;
            top: 70px;
            left: 0;
            width: 250px;
            height: calc(100vh - 70px);
            background: var(--admin-sidebar);
            color: var(--light-text);
            overflow-y: auto;
            z-index: 999;
        }

        .sidebar-menu {
            padding: 20px 0;
        }

        .menu-section {
            margin-bottom: 30px;
        }

        .menu-section-title {
            padding: 0 20px 10px;
            font-size: 12px;
            text-transform: uppercase;
            color: #7f8c8d;
            font-weight: 600;
            letter-spacing: 1px;
        }

        .menu-item {
            display: block;
            padding: 12px 20px;
            color: #bdc3c7;
            text-decoration: none;
            transition: all 0.3s;
            border-left: 3px solid transparent;
        }

        .menu-item:hover {
            background: rgba(255,255,255,0.1);
            color: var(--light-text);
            border-left-color: var(--primary-color);
        }

        .menu-item.active {
            background: rgba(229, 9, 20, 0.2);
            color: var(--light-text);
            border-left-color: var(--primary-color);
        }

        .menu-item i {
            width: 20px;
            margin-right: 10px;
        }

        /* ==== MAIN CONTENT ==== */
        .admin-main {
            margin-left: 250px;
            margin-top: 70px;
            min-height: calc(100vh - 70px);
            padding: 30px;
        }

        .content-header {
            background: white;
            padding: 25px 30px;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }

        .content-title {
            font-size: 28px;
            font-weight: 700;
            color: var(--dark-text);
            margin-bottom: 8px;
        }

        .content-subtitle {
            color: #7f8c8d;
            font-size: 16px;
        }

        .content-body {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            overflow: hidden;
        }

        /* ==== RESPONSIVE ==== */
        @media (max-width: 768px) {
            .admin-sidebar {
                transform: translateX(-100%);
                transition: transform 0.3s;
            }
            
            .admin-sidebar.open {
                transform: translateX(0);
            }
            
            .admin-main {
                margin-left: 0;
            }
        }
    </style>
    ${param.extraStyles}
</head>
<body>
    <!-- ==== ADMIN HEADER ==== -->
    <header class="admin-header">
        <div class="admin-header-left">
            <a href="admin-dashboard" class="admin-logo">
                <img src="https://png.pngtree.com/element_origin_min_pic/16/12/04/906aae676011fbcc70e96932704830e3.jpg" alt="MovieNow">
                <span>MovieNow</span>
            </a>
            <div class="admin-breadcrumb">
                <a href="admin-dashboard">Dashboard</a>
                <i class="fas fa-chevron-right"></i>
                <span>${param.pageTitle != null ? param.pageTitle : 'Admin'}</span>
            </div>
        </div>
        <div class="admin-header-right">
            <div class="admin-user-info">
                <div class="admin-user-avatar">
                    <i class="fas fa-user"></i>
                </div>
                <span>Admin</span>
            </div>
            <a href="logout" class="admin-logout">
                <i class="fas fa-sign-out-alt"></i> Đăng xuất
            </a>
            <a href="home" class="admin-logout">
                <i class="fas fa-sign-out-alt"></i> Home
            </a>
        </div>
    </header>

    <!-- ==== SIDEBAR ==== -->
    <nav class="admin-sidebar">
        <div class="sidebar-menu">
            <div class="menu-section">
                <div class="menu-section-title">Tổng quan</div>
                <a href="admin-dashboard" class="menu-item">
                    <i class="fas fa-tachometer-alt"></i>
                    Dashboard
                </a>
            </div>
            
            <div class="menu-section">
                <div class="menu-section-title">Quản lý</div>
                <a href="admin-cinema" class="menu-item ${param.currentPage == 'cinema' ? 'active' : ''}">
                    <i class="fas fa-building"></i>
                    Rạp phim
                </a>
                <a href="admin-products" class="menu-item ${param.currentPage == 'products' ? 'active' : ''}">
                    <i class="fas fa-box"></i>
                    Sản phẩm
                </a>
                <a href="admin-movies" class="menu-item ${param.currentPage == 'movies' ? 'active' : ''}">
                    <i class="fas fa-film"></i>
                    Phim
                </a>
                <a href="admin-rooms" class="menu-item ${param.currentPage == 'rooms' ? 'active' : ''}">
                    <i class="fas fa-chair"></i>
                    Phòng & Ghế
                </a>
                <a href="admin-showtimes" class="menu-item ${param.currentPage == 'showtimes' ? 'active' : ''}">
                    <i class="fas fa-clock"></i>
                    Suất chiếu
                </a>
                <a href="admin-bookings" class="menu-item ${param.currentPage == 'bookings' ? 'active' : ''}">
                    <i class="fas fa-ticket-alt"></i>
                    Đặt vé
                </a>
            </div>
            
            <div class="menu-section">
                <div class="menu-section-title">Người dùng</div>
                <a href="admin-customers" class="menu-item ${param.currentPage == 'customers' ? 'active' : ''}">
                    <i class="fas fa-users"></i>
                    Khách hàng
                </a>
                <a href="admin-staff" class="menu-item ${param.currentPage == 'staff' ? 'active' : ''}">
                    <i class="fas fa-user-tie"></i>
                    Nhân viên
                </a>
            </div>
            
<!--            <div class="menu-section">
                <div class="menu-section-title">Hệ thống</div>
                <a href="admin-settings" class="menu-item ${param.currentPage == 'settings' ? 'active' : ''}">
                    <i class="fas fa-cog"></i>
                    Cài đặt
                </a>
            </div>-->
        </div>
    </nav>

    <!-- ==== MAIN CONTENT ==== -->
    <main class="admin-main">
        <div class="content-header">
            <h1 class="content-title">${param.pageTitle != null ? param.pageTitle : 'Admin Dashboard'}</h1>
            <p class="content-subtitle">${param.pageSubtitle != null ? param.pageSubtitle : 'Quản lý hệ thống MovieNow'}</p>
        </div>
        
        <div class="content-body">
