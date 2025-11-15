<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Thanh toán thất bại"/>
</jsp:include>
<div style="min-height:60vh;display:flex;align-items:center;justify-content:center;flex-direction:column;gap:12px;">
    <div style="font-size:56px;">❌</div>
    <h2>Thanh toán thất bại</h2>
    <p>Giao dịch không thành công hoặc đã bị hủy. Vui lòng thử lại.</p>
    <a class="btn" href="home" style="background:#666;color:#fff;padding:10px 16px;border-radius:6px;text-decoration:none;">Về trang chủ</a>
</div>
<jsp:include page="../layout/footer.jsp"/>
