
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!-- ==== FOOTER ==== -->
<footer class="footer">
    <div class="footer-content">
        <div class="footer-section">
            <h3>Về MovieNow</h3>
            <ul>
                <li><a href="#">Giới thiệu</a></li>
                <li><a href="#">Blog</a></li>
                <li><a href="#">Tuyển dụng</a></li>
                <li><a href="#">Liên hệ</a></li>
            </ul>
        </div>

        <div class="footer-section">
            <h3>Hỗ trợ khách hàng</h3>
            <ul>
                <li><a href="#">Câu hỏi thường gặp</a></li>
                <li><a href="#">Chính sách hoàn vé</a></li>
                <li><a href="#">Điều khoản dịch vụ</a></li>
                <li><a href="#">Chính sách bảo mật</a></li>
            </ul>
        </div>

        <div class="footer-section">
            <h3>Liên hệ</h3>
            <p><i class="fas fa-phone"></i> Hotline: 1900 1234</p>
            <p><i class="fas fa-envelope"></i> Email: support@movienow.vn</p>
            <p><i class="fas fa-clock"></i> Giờ hỗ trợ: 8:00 - 22:00 hàng ngày</p>
        </div>

        <div class="footer-section">
            <h3>Kết nối với chúng tôi</h3>
            <div class="social-links">
                <a href="#" title="Facebook"><i class="fab fa-facebook"></i></a>
                <a href="#" title="Twitter"><i class="fab fa-twitter"></i></a>
                <a href="#" title="Instagram"><i class="fab fa-instagram"></i></a>
                <a href="#" title="YouTube"><i class="fab fa-youtube"></i></a>
            </div>
        </div>
    </div>

    <div class="footer-bottom">
        <p>&copy; 2025 MovieNow - Nền tảng đặt vé phim hàng đầu Việt Nam. All rights reserved.</p>
    </div>
</footer>

<style>
    /* ==== FOOTER ==== */
    .footer {
        background-color: var(--secondary-color, #221f1f);
        color: var(--light-text, #ffffff);
        padding: 40px 20px 20px;
        margin-top: 60px;
    }

    .footer-content {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 30px;
        width: 90%;
        margin: 0 auto 30px;
        max-width: 1200px;
    }

    .footer-section h3 {
        font-size: 16px;
        font-weight: 600;
        color: var(--primary-color, #e50914);
        margin-bottom: 15px;
    }

    .footer-section ul {
        list-style: none;
    }

    .footer-section ul li {
        margin-bottom: 10px;
    }

    .footer-section a {
        color: #ccc;
        text-decoration: none;
        font-size: 14px;
        transition: color 0.3s ease;
    }

    .footer-section a:hover {
        color: var(--primary-color, #e50914);
    }

    .footer-section p {
        font-size: 14px;
        color: #ccc;
        margin-bottom: 8px;
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .social-links {
        display: flex;
        gap: 15px;
        margin-top: 10px;
    }

    .social-links a {
        width: 36px;
        height: 36px;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: var(--primary-color, #e50914);
        border-radius: 50%;
        color: white;
        font-size: 16px;
        transition: all 0.3s ease;
    }

    .social-links a:hover {
        transform: translateY(-3px);
        background-color: #cc0812;
    }

    .footer-bottom {
        text-align: center;
        padding-top: 20px;
        border-top: 1px solid rgba(255,255,255,0.1);
        font-size: 13px;
        color: #999;
    }

    @media (max-width: 768px) {
        .footer-content {
            grid-template-columns: 1fr;
            gap: 20px;
        }

        .footer {
            padding: 30px 15px 15px;
        }
    }
 </style>
</body>
</html>
