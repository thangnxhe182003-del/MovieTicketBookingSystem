<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Đặt vé xem phim"/>
    <jsp:param name="extraStyles" value="
        <style>
            .booking-page {
                background: #f5f5f5;
                padding: 30px 0;
                min-height: 100vh;
            }

            .booking-container {
                max-width: 1000px;
                margin: 0 auto;
                padding: 0 20px;
            }

            .booking-header {
                background: white;
                padding: 20px;
                border-radius: 8px;
                margin-bottom: 20px;
                box-shadow: 0 3px 10px rgba(0,0,0,0.1);
                display: grid;
                grid-template-columns: 120px 1fr;
                gap: 20px;
                align-items: center;
            }

            .booking-poster img {
                width: 100%;
                border-radius: 6px;
                height: auto;
            }

            .booking-info h1 {
                font-size: 28px;
                margin: 0 0 10px 0;
                color: #222;
            }

            .booking-info p {
                color: #666;
                margin: 5px 0;
                font-size: 15px;
            }

            .showtime-selection {
                background: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 3px 10px rgba(0,0,0,0.1);
            }

            .selection-title {
                font-size: 22px;
                font-weight: bold;
                color: #222;
                margin-bottom: 20px;
                border-left: 4px solid #e50914;
                padding-left: 10px;
            }

            .showtime-group {
                margin-bottom: 25px;
            }

            .date-header {
                font-size: 16px;
                font-weight: 600;
                color: #333;
                background: #f9f9f9;
                padding: 10px 15px;
                border-radius: 6px;
                margin-bottom: 12px;
                display: inline-block;
            }

            .showtime-buttons {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
                gap: 12px;
                margin-bottom: 20px;
            }

            .showtime-btn {
                padding: 15px;
                background: #f9f9f9;
                border: 2px solid #e0e0e0;
                border-radius: 8px;
                cursor: pointer;
                text-decoration: none;
                text-align: center;
                transition: all 0.3s;
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                color: #333;
            }

            .showtime-btn:hover {
                border-color: #e50914;
                background: white;
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(229, 9, 20, 0.2);
            }

            .showtime-time {
                font-size: 20px;
                font-weight: bold;
                color: #e50914;
                margin-bottom: 5px;
            }

            .showtime-room {
                font-size: 12px;
                color: #666;
                margin-bottom: 5px;
            }

            .showtime-price {
                font-size: 13px;
                color: #999;
            }

            .empty-state {
                text-align: center;
                padding: 50px 20px;
                color: #999;
            }

            .empty-state-icon {
                font-size: 48px;
                margin-bottom: 15px;
            }

            .empty-state p {
                font-size: 16px;
                margin: 10px 0;
            }

            .back-btn {
                display: inline-block;
                margin-top: 20px;
                padding: 10px 20px;
                background: #f0f0f0;
                color: #333;
                text-decoration: none;
                border-radius: 6px;
                font-weight: 600;
                transition: all 0.3s;
            }

            .back-btn:hover {
                background: #e0e0e0;
            }

            @media (max-width: 768px) {
                .booking-header {
                    grid-template-columns: 1fr;
                }

                .showtime-buttons {
                    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
                }
            }
        </style>
    "/>
</jsp:include>

<div class="booking-page">
    <div class="booking-container">
        <!-- MOVIE INFO HEADER -->
        <div class="booking-header">
            <div class="booking-poster">
                <img src="${pageContext.request.contextPath}/assets/image/${movie.poster}" alt="${movie.tenPhim}" onerror="this.src='https://via.placeholder.com/120x160?text=No+Image'">
            </div>
            <div class="booking-info">
                <h1>${movie.tenPhim}</h1>
                <p>📊 ${movie.theLoai} | ⏱️ ${movie.thoiLuong} phút</p>
                <p>🎬 ${movie.loaiPhim}</p>
                <c:if test="${movie.doTuoiGioiHan > 0}">
                    <p>🔞 K${movie.doTuoiGioiHan}+</p>
                </c:if>
            </div>
        </div>

        <!-- SHOWTIME SELECTION -->
        <div class="showtime-selection">
            <h2 class="selection-title">📅 Chọn suất chiếu</h2>

            <c:choose>
                <c:when test="${empty showtimes}">
                    <div class="empty-state">
                        <div class="empty-state-icon">📭</div>
                        <p>Hiện tại không có suất chiếu nào</p>
                        <p style="font-size: 14px; color: #ccc;">Vui lòng quay lại sau để xem lịch chiếu mới nhất</p>
                        <a href="movie?action=list" class="back-btn">← Quay lại trang chủ</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <%
                        // Group showtimes by date
                        java.util.Map<java.time.LocalDate, java.util.List<Object>> showtimesByDate = new java.util.LinkedHashMap<>();
                        for (Object obj : (java.util.List<?>) request.getAttribute("showtimes")) {
                            model.Showtime st = (model.Showtime) obj;
                            showtimesByDate.computeIfAbsent(st.getNgayChieu(), k -> new java.util.ArrayList<>()).add(st);
                        }
                        request.setAttribute("showtimesByDate", showtimesByDate);
                    %>
                    <c:forEach var="dateEntry" items="${showtimesByDate}">
                        <div class="showtime-group">
                            <div class="date-header">
                                📅 <fmt:formatDate value="${dateEntry.key}" pattern="EEEE, dd/MM/yyyy" locale="vi_VN"/>
                            </div>
                            <div class="showtime-buttons">
                                <c:forEach var="showtime" items="${dateEntry.value}">
                                    <a href="booking?action=selectSeats&maSuatChieu=${showtime.maSuatChieu}" class="showtime-btn">
                                        <div class="showtime-time">
                                            <fmt:formatDate value="${showtime.gioBatDau}" pattern="HH:mm"/>
                                        </div>
                                        <div class="showtime-room">
                                            ${showtime.tenPhong}
                                        </div>
                                        <div class="showtime-room" style="color: #999; font-size: 11px;">
                                            ${showtime.tenRap}
                                        </div>
                                        <div class="showtime-price">
                                            <fmt:formatNumber value="${showtime.giaVeCoSo}" type="currency" currencySymbol="₫"/>
                                        </div>
                                    </a>
                                </c:forEach>
                            </div>
                        </div>
                    </c:forEach>

                    <a href="movie?action=detail&maPhim=${movie.maPhim}" class="back-btn">← Quay lại chi tiết phim</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="../layout/footer.jsp" />
