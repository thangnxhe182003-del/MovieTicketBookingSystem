<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<style>
    :root {
        --primary-color: #e50914;
        --secondary-color: #221f1f;
        --accent-color: #564d4d;
        --light-bg: #f5f5f1;
        --dark-text: #221f1f;
        --light-text: #ffffff;
    }

    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
    }

    body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background-color: var(--light-bg);
        color: var(--dark-text);
    }

    /* ==== HEADER ==== */
    header {
        background-color: var(--secondary-color);
        padding: 12px 40px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        position: sticky;
        top: 0;
        z-index: 1000;
    }

    .header-left {
        display: flex;
        align-items: center;
        gap: 30px;
        flex: 1;
    }

    .logo {
        display: flex;
        align-items: center;
        gap: 10px;
        text-decoration: none;
        color: var(--light-text);
    }

    .logo img {
        width: 50px;
        height: 50px;
        border-radius: 8px;
        object-fit: cover;
    }

    .logo span {
        font-size: 24px;
        font-weight: 700;
        color: var(--primary-color);
        letter-spacing: 1px;
    }

    /* ==== SEARCH BAR ==== */
    .search-bar {
        display: flex;
        align-items: center;
        background-color: white;
        border-radius: 6px;
        padding: 8px 15px;
        width: 300px;
        gap: 10px;
    }

    .search-bar i {
        color: #999;
        font-size: 16px;
    }

    .search-bar input {
        border: none;
        outline: none;
        background: none;
        width: 100%;
        font-size: 14px;
        color: var(--dark-text);
    }

    .search-bar input::placeholder {
        color: #bbb;
    }

    /* ==== NAVIGATION ==== */
    nav {
        display: flex;
        gap: 30px;
        flex: 1;
        justify-content: center;
    }

    nav a {
        color: var(--light-text);
        text-decoration: none;
        font-weight: 500;
        font-size: 15px;
        transition: color 0.3s ease;
        position: relative;
    }

    nav a:hover {
        color: var(--primary-color);
    }

    nav a::after {
        content: '';
        position: absolute;
        bottom: -5px;
        left: 0;
        width: 0;
        height: 2px;
        background-color: var(--primary-color);
        transition: width 0.3s ease;
    }

    nav a:hover::after {
        width: 100%;
    }

    /* ==== USER MENU ==== */
    .header-right {
        display: flex;
        align-items: center;
        gap: 20px;
    }

    .user-menu {
        display: flex;
        align-items: center;
        gap: 15px;
    }

    .user-menu a, .user-menu button {
        color: var(--light-text);
        text-decoration: none;
        font-size: 14px;
        background: none;
        border: none;
        cursor: pointer;
        transition: color 0.3s ease;
    }

    .user-menu a:hover, .user-menu button:hover {
        color: var(--primary-color);
    }

    .btn-login, .btn-register {
        padding: 8px 16px;
        border-radius: 4px;
        font-weight: 600;
        transition: all 0.3s ease;
    }

    .btn-login {
        border: 1px solid var(--primary-color);
        color: var(--primary-color);
    }

    .btn-login:hover {
        background-color: var(--primary-color);
        color: white;
    }

    .btn-register {
        background-color: var(--primary-color);
        color: white;
    }

    .btn-register:hover {
        background-color: #cc0812;
    }

    /* ==== ALERTS ==== */
    .alert-container {
        width: 90%;
        margin: 15px auto;
    }

    .alert {
        padding: 12px 16px;
        border-radius: 6px;
        margin-bottom: 10px;
        font-weight: 500;
        animation: slideIn 0.3s ease;
    }

    .alert-success {
        background-color: #d4edda;
        color: #155724;
        border-left: 4px solid #28a745;
    }

    .alert-error {
        background-color: #f8d7da;
        color: #721c24;
        border-left: 4px solid #dc3545;
    }

    .alert-warning {
        background-color: #fff3cd;
        color: #856404;
        border-left: 4px solid #ffc107;
    }

    @keyframes slideIn {
        from {
            opacity: 0;
            transform: translateX(-20px);
        }
        to {
            opacity: 1;
            transform: translateX(0);
        }
    }

    /* ==== RESPONSIVE ==== */
    @media (max-width: 1024px) {
        header { padding: 10px 20px; flex-wrap: wrap; }
        nav { gap: 15px; }
        nav a { font-size: 13px; }
        .search-bar { width: 250px; order: 3; flex-basis: 100%; margin-top: 10px; }
    }

    @media (max-width: 768px) {
        header { flex-direction: column; gap: 10px; }
        .header-left { width: 100%; gap: 15px; }
        .search-bar { width: 100%; order: 3; }
        nav { gap: 10px; font-size: 12px; }
        .header-right { width: 100%; }
    }
</style>
${param.extraStyles}

<!-- ==== HEADER ==== -->
<header>
    <div class="header-left">
        <a href="home" class="logo">
            <img src="https://png.pngtree.com/element_origin_min_pic/16/12/04/906aae676011fbcc70e96932704830e3.jpg" alt="MovieNow Logo">
            <span>MovieNow</span>
        </a>

        <div class="search-bar">
            <i class="fas fa-search"></i>
            <input type="text" placeholder="Tìm phim, rạp, ưu đãi...">
        </div>
    </div>

    <nav>
        <a href="home">Phim</a>
        <a href="#categories">Thể loại</a>
        <a href="#book">Mua vé</a>
        <a href="#news">Tin tức</a>
        <a href="#support">Hỗ trợ</a>
    </nav>

    <div class="header-right">
        <div class="user-menu">
            <c:choose>
                <c:when test="${not empty sessionScope.loggedInUser}">
                    <span style="color: var(--light-text); font-size: 14px;">
                        👋 ${sessionScope.loggedInUser.hoTen}
                    </span>
                    <a href="profile">Tài khoản</a>
                    <a href="logout">Đăng xuất</a>
                </c:when>
                <c:otherwise>
                    <a href="login" class="btn-login">Đăng nhập</a>
                    <a href="register" class="btn-register">Đăng ký</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</header>

<!-- ==== ALERTS ==== -->
<div class="alert-container">
    <c:if test="${not empty requestScope.success}">
        <div class="alert alert-success">
            <i class="fas fa-check-circle"></i> ${requestScope.success}
        </div>
    </c:if>
    <c:if test="${not empty requestScope.error}">
        <div class="alert alert-error">
            <i class="fas fa-exclamation-circle"></i> ${requestScope.error}
        </div>
    </c:if>
    <c:if test="${not empty requestScope.warning}">
        <div class="alert alert-warning">
            <i class="fas fa-info-circle"></i> ${requestScope.warning}
        </div>
    </c:if>
</div>