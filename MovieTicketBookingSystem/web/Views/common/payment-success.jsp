<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Thanh toán thành công"/>
</jsp:include>
<div style="min-height:60vh;display:flex;align-items:center;justify-content:center;flex-direction:column;gap:12px;">
    <div style="font-size:56px;">✅</div>
    <h2>Thanh toán thành công</h2>
    <p>Cảm ơn bạn đã đặt vé. Thông tin vé đã được ghi nhận.</p>
    <a class="btn" href="order-history" style="background:#e50914;color:#fff;padding:10px 16px;border-radius:6px;text-decoration:none;">Xem lịch sử đặt vé</a>
</div>
<jsp:include page="../layout/footer.jsp"/>
