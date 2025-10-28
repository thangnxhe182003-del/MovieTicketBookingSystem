<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Lịch sử đặt vé"/>
    <jsp:param name="extraStyles" value="
        <style>
            .history-page { background: #f6f7fb; min-height: 100vh; padding: 32px 0; }
            .container { max-width: 1100px; margin: 0 auto; padding: 0 16px; }

            .page-title { font-size: 28px; font-weight: 800; color: #222; margin: 0 0 18px 0; }

            .order-card {
                background: linear-gradient(180deg, #ffffff 0%, #fbfbff 100%);
                border-radius: 14px;
                box-shadow: 0 10px 25px rgba(28, 28, 70, .08);
                overflow: hidden;
                margin-bottom: 18px;
                border: 1px solid #eef0fb;
            }
            .order-head {
                display: flex; justify-content: space-between; align-items: center;
                padding: 16px 18px; border-bottom: 1px solid #eef0fb; background: #fff;
            }
            .order-title { font-weight: 800; color: #1e1e2d; font-size: 16px; }
            .order-meta { color: #667085; font-size: 13px; margin-top: 4px; }
            .order-tags { margin-top: 6px; display:flex; gap:6px; flex-wrap:wrap; }
            .tag { background:#f1f4ff; color:#3f51b5; border:1px solid #e3e7ff; padding:2px 8px; border-radius:999px; font-size:11px; font-weight:700; }
            .order-link { color: #e50914; text-decoration: none; font-weight: 600; }
            .order-link:hover { text-decoration: underline; }

            .order-body { padding: 18px; background: radial-gradient(1200px 300px at 0% 0%, #ffffff 0%, #fafaff 100%); }

            .grid { display: grid; grid-template-columns: 1fr 1fr; gap: 18px; }
            .section { background: #fff; border: 1px solid #eef0fb; border-radius: 12px; padding: 14px; }
            .section-title { font-weight: 800; color: #2e2e3a; margin-bottom: 10px; font-size: 14px; letter-spacing: .3px; }

            .ticket-list, .item-list { list-style: none; padding-left: 0; margin: 0; }
            .ticket-item, .item-row { display: flex; align-items: center; gap: 10px; padding: 8px 0; border-bottom: 1px dashed #eef0fb; font-size: 14px; }
            .ticket-item:last-child, .item-row:last-child { border-bottom: none; }

            .chip { display: inline-block; padding: 4px 8px; border-radius: 999px; background: #eef4ff; color: #1d4ed8; font-weight: 700; font-size: 12px; }
            .price { margin-left: auto; font-weight: 800; color: #e50914; }
            .muted { color: #8a8fa0; }

            .empty { text-align: center; color: #99a0b0; padding: 40px 0; background: #fff; border-radius: 12px; border: 1px dashed #e4e7f3; }

            @media (max-width: 768px) {
                .grid { grid-template-columns: 1fr; }
                .order-head { align-items: flex-start; gap: 8px; }
            }
        </style>
    "/>
</jsp:include>

<div class="history-page">
  <div class="container">
    <div class="page-title">Vé của tôi</div>
    <c:choose>
      <c:when test="${empty orders}">
        <div class="empty">Chưa có đơn hàng đã thanh toán.</div>
      </c:when>
      <c:otherwise>
        <c:forEach var="ov" items="${orders}">
          <c:set var="order" value="${ov.order}" />
          <div class="order-card">
            <div class="order-head">
              <div style="display:flex; align-items:center; gap:12px;">
                <c:if test="${not empty ov.moviePoster}">
                  <img src="${pageContext.request.contextPath}/assets/image/${ov.moviePoster}" alt="${ov.movieTitle}" style="width:48px;height:48px;object-fit:cover;border-radius:8px;">
                </c:if>
                <div>
                  <div class="order-title">Mã vé #${order.maOrder}</div>
                  <div class="order-meta">
                    <c:if test="${not empty ov.movieTitle}"><strong>${ov.movieTitle}</strong></c:if>
                    <c:if test="${not empty ov.showDate}"> · ${ov.showDate}</c:if>
                    <c:if test="${not empty ov.startTime}"> · ${ov.startTime} - ${ov.endTime}</c:if>
                    <c:if test="${not empty ov.cinemaName}"> · ${ov.cinemaName}</c:if>
                    <c:if test="${not empty ov.roomName}"> · Phòng ${ov.roomName}</c:if>
                  </div>
                  <div class="order-tags">
                    <c:if test="${not empty ov.movieType}"><span class="tag">${ov.movieType}</span></c:if>
                    <c:if test="${not empty ov.subtitleLang}"><span class="tag">Phụ đề: ${ov.subtitleLang}</span></c:if>
                    <c:if test="${not empty ov.ageLimit}"><span class="tag">K${ov.ageLimit}+</span></c:if>
                    <c:if test="${not empty ov.duration}"><span class="tag">${ov.duration} phút</span></c:if>
                  </div>
                  <div class="order-meta">Trạng thái: <span class="chip">${order.trangThai}</span> · Tổng tiền: <span class="price">₫<fmt:formatNumber value="${order.tongTien}" pattern=",###"/></span> · Thanh toán lúc: ${ov.formattedPaidAt}</div>
                </div>
              </div>
              <a href="movie" class="order-link">Mua thêm vé →</a>
            </div>
            <div class="order-body">
              <div class="grid">
                <div class="section">
                  <div class="section-title">Vé / ghế</div>
                  <ul class="ticket-list">
                    <c:forEach var="sv" items="${ov.seatViews}">
                      <li class="ticket-item">
                        <span class="muted">Ghế</span> ${sv.label}
                        <span class="price">₫<fmt:formatNumber value="${sv.price}" pattern=",###"/></span>
                      </li>
                    </c:forEach>
                  </ul>
                </div>
                <div class="section">
                  <div class="section-title">Combo</div>
                  <ul class="item-list">
                    <c:choose>
                      <c:when test="${empty ov.itemViews}"><li class="item-row muted">Không có combo</li></c:when>
                      <c:otherwise>
                        <c:forEach var="iv" items="${ov.itemViews}">
                          <li class="item-row">
                            <c:if test="${not empty iv.thumb}">
                              <img src="${pageContext.request.contextPath}/assets/image/${iv.thumb}" alt="thumb" style="width:28px;height:28px;object-fit:cover;border-radius:4px;">
                            </c:if>
                            <span>${iv.name}</span>
                            <span class="muted">× ${iv.qty}</span>
                            <span class="price">₫<fmt:formatNumber value="${iv.subtotal}" pattern=",###"/></span>
                          </li>
                        </c:forEach>
                      </c:otherwise>
                    </c:choose>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </c:forEach>
      </c:otherwise>
    </c:choose>
  </div>
</div>

<jsp:include page="../layout/footer.jsp" />
