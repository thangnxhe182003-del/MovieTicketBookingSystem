<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/admin-header.jsp">
    <jsp:param name="pageTitle" value="Lịch chiếu phim"/>
    <jsp:param name="pageSubtitle" value="Xem lịch chiếu theo phòng và ngày"/>
    <jsp:param name="currentPage" value="showtimes"/>
    <jsp:param name="extraStyles" value="
        <style>
            .container { padding: 30px; }
            .calendar-container { max-width: 1400px; margin: 0 auto; }
            .calendar-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }
            .calendar-title { font-size: 24px; font-weight: 700; color: #333; }
            .date-navigation { display: flex; align-items: center; gap: 15px; }
            .btn-nav { padding: 8px 16px; border: 1px solid #ddd; border-radius: 6px; background: #fff; 
                      color: #333; text-decoration: none; transition: all 0.3s; }
            .btn-nav:hover { background: #f8f9fa; border-color: #e50914; color: #e50914; }
            .current-week { font-size: 16px; font-weight: 600; color: #e50914; }
            .calendar-grid { display: grid; grid-template-columns: 200px 1fr; gap: 0; border: 1px solid #e9ecef; border-radius: 8px; overflow: hidden; }
            .room-sidebar { background: #f8f9fa; border-right: 1px solid #e9ecef; }
            .room-header { padding: 20px; background: #e50914; color: white; font-weight: 600; text-align: center; }
            .room-list { max-height: 600px; overflow-y: auto; }
            .room-item { padding: 15px 20px; border-bottom: 1px solid #e9ecef; cursor: pointer; transition: all 0.3s; }
            .room-item:hover { background: #e9ecef; }
            .room-item.active { background: #e50914; color: white; }
            .room-item h4 { margin: 0 0 5px 0; font-size: 16px; font-weight: 600; }
            .room-item p { margin: 0; font-size: 14px; opacity: 0.8; }
            .calendar-main { background: #fff; }
            .calendar-header-row { display: grid; grid-template-columns: 200px repeat(7, 1fr); background: #f8f9fa; border-bottom: 1px solid #e9ecef; }
            .calendar-header-cell { padding: 15px 10px; text-align: center; font-weight: 600; color: #333; border-right: 1px solid #e9ecef; }
            .calendar-header-cell:last-child { border-right: none; }
            .calendar-body { display: grid; grid-template-columns: 200px repeat(7, 1fr); }
            .time-column { background: #f8f9fa; border-right: 1px solid #e9ecef; }
            .time-slot { padding: 20px 15px; border-bottom: 1px solid #e9ecef; text-align: center; font-weight: 600; color: #666; }
            .day-column { border-right: 1px solid #e9ecef; }
            .day-column:last-child { border-right: none; }
            .day-cell { padding: 20px 10px; border-bottom: 1px solid #e9ecef; min-height: 80px; }
            .showtime-item { background: linear-gradient(135deg, #e50914, #c90812); color: white; padding: 8px 12px; 
                            border-radius: 6px; margin-bottom: 5px; font-size: 12px; cursor: pointer; 
                            transition: all 0.3s; position: relative; }
            .showtime-item:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(229, 9, 20, 0.3); }
            .showtime-item:last-child { margin-bottom: 0; }
            .showtime-movie { font-weight: 600; margin-bottom: 2px; }
            .showtime-time { font-size: 11px; opacity: 0.9; }
            .showtime-language { position: absolute; top: 4px; right: 4px; background: rgba(255,255,255,0.2); 
                                padding: 2px 6px; border-radius: 10px; font-size: 10px; }
            .empty-cell { color: #ccc; font-style: italic; text-align: center; padding: 20px; }
            .legend { display: flex; gap: 20px; margin-top: 20px; padding: 15px; background: #f8f9fa; border-radius: 8px; }
            .legend-item { display: flex; align-items: center; gap: 8px; }
            .legend-color { width: 16px; height: 16px; border-radius: 4px; background: linear-gradient(135deg, #e50914, #c90812); }
            .legend-text { font-size: 14px; color: #333; }
        </style>
    "/>
</jsp:include>

<div class="container">
    <div class="calendar-container">
        <div class="calendar-header">
            <h1 class="calendar-title">Lịch chiếu phim</h1>
            <div class="date-navigation">
                <a href="#" class="btn-nav" onclick="changeWeek(-1)">
                    <i class="fas fa-chevron-left"></i> Tuần trước
                </a>
                <span class="current-week" id="currentWeek">
                    ${dateLabelMap[startDate]} - 
                    ${dateLabelMap[startDate.plusDays(6)]}
                </span>
                <a href="#" class="btn-nav" onclick="changeWeek(1)">
                    Tuần sau <i class="fas fa-chevron-right"></i>
                </a>
            </div>
        </div>

        <div class="calendar-grid">
            <!-- Room Sidebar -->
            <div class="room-sidebar">
                <div class="room-header">
                    <i class="fas fa-video"></i> Phòng chiếu
                </div>
                <div class="room-list">
                    <c:forEach var="room" items="${rooms}" varStatus="status">
                        <div class="room-item ${status.first ? 'active' : ''}" onclick="selectRoom(${room.maPhong}, this)">
                            <h4>${room.tenPhong}</h4>
                            <p>${room.tenRap}</p>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- Calendar Main -->
            <div class="calendar-main">
                <!-- Calendar Header -->
                <div class="calendar-header-row">
                    <div class="calendar-header-cell">Giờ</div>
                    <c:forEach var="i" begin="0" end="6">
                        <div class="calendar-header-cell">
                            <c:set var="date" value="${startDate.plusDays(i)}" />
                            <div style="font-size: 14px; font-weight: 700; color: #e50914;">
                                ${dateLabelMap[date]}
                            </div>
                            <div style="font-size: 12px; color: #666;">
                                ${dayLabelMap[date]}
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <!-- Calendar Body -->
                <div class="calendar-body">
                    <!-- Time Column -->
                    <div class="time-column">
                        <c:forEach var="hour" begin="9" end="23">
                            <div class="time-slot">${hour}:00</div>
                        </c:forEach>
                    </div>

                    <!-- Day Columns -->
                    <c:forEach var="i" begin="0" end="6">
                        <div class="day-column">
                            <c:set var="date" value="${startDate.plusDays(i)}" />
                            <c:forEach var="hour" begin="9" end="23">
                                <div class="day-cell" id="cell-${i}-${hour}">
                                    <!-- Showtimes will be populated by JavaScript -->
                                </div>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

        <!-- Legend -->
        <div class="legend">
            <div class="legend-item">
                <div class="legend-color"></div>
                <span class="legend-text">Suất chiếu đã gán phim</span>
            </div>
            <div class="legend-item">
                <div class="legend-color" style="background: #ffc107;"></div>
                <span class="legend-text">Suất chiếu chưa gán phim</span>
            </div>
        </div>
    </div>
</div>

<script>
    // Showtime data from server
    const showtimesByRoom = {
        <c:forEach var="roomEntry" items="${showtimesByRoom}">
        ${roomEntry.key}: {
            <c:forEach var="dateEntry" items="${roomEntry.value}">
            '${dateEntry.key}': [
                <c:forEach var="showtime" items="${dateEntry.value}" varStatus="st">
                {
                    id: ${showtime.maSuatChieu},
                    movie: '${showtime.tenPhim != null ? showtime.tenPhim : "Chưa gán phim"}',
                    startTime: '${timeLabelMap[showtime.maSuatChieu]}',
                    endTime: '${timeLabelMap[showtime.maSuatChieu]}',
                    language: '${showtime.ngonNguAmThanh}',
                    hasMovie: ${showtime.maPhim > 0}
                }${st.last ? '' : ','}
                </c:forEach>
            ]${st.last ? '' : ','}
            </c:forEach>
        }${st.last ? '' : ','}
        </c:forEach>
    };

    let selectedRoom = ${rooms[0].maPhong};
    let currentWeek = 0;

    function selectRoom(roomId, element) {
        // Update active state
        document.querySelectorAll('.room-item').forEach(item => item.classList.remove('active'));
        element.classList.add('active');
        
        selectedRoom = roomId;
        renderCalendar();
    }

    function changeWeek(offset) {
        currentWeek += offset;
        // In a real implementation, you would reload the page with new dates
        // For now, we'll just update the display
        const startDate = new Date();
        startDate.setDate(startDate.getDate() + (currentWeek * 7));
        
        document.getElementById('currentWeek').textContent = 
            formatDate(startDate) + ' - ' + formatDate(new Date(startDate.getTime() + 6 * 24 * 60 * 60 * 1000));
        
        renderCalendar();
    }

    function formatDate(date) {
        return date.toLocaleDateString('vi-VN');
    }

    function renderCalendar() {
        // Clear all cells
        for (let day = 0; day < 7; day++) {
            for (let hour = 9; hour <= 23; hour++) {
                const cell = document.getElementById(`cell-${day}-${hour}`);
                cell.innerHTML = '';
            }
        }

        // Get showtimes for selected room
        const roomShowtimes = showtimesByRoom[selectedRoom];
        if (!roomShowtimes) return;

        // Render showtimes
        Object.keys(roomShowtimes).forEach(dateStr => {
            const showtimes = roomShowtimes[dateStr];
            const date = new Date(dateStr);
            const dayOfWeek = (date.getDay() + 6) % 7; // Convert Sunday=0 to Monday=0

            showtimes.forEach(showtime => {
                const startHour = parseInt(showtime.startTime.split(':')[0]);
                const endHour = parseInt(showtime.endTime.split(':')[0]);
                
                // Find the cell for this time slot
                const cell = document.getElementById(`cell-${dayOfWeek}-${startHour}`);
                if (cell) {
                    const showtimeElement = document.createElement('div');
                    showtimeElement.className = 'showtime-item';
                    showtimeElement.style.background = showtime.hasMovie ? 
                        'linear-gradient(135deg, #e50914, #c90812)' : 
                        'linear-gradient(135deg, #ffc107, #ffb300)';
                    
                    showtimeElement.innerHTML = `
                        <div class="showtime-movie">${showtime.movie}</div>
                        <div class="showtime-time">${showtime.startTime} - ${showtime.endTime}</div>
                        <div class="showtime-language">${showtime.language}</div>
                    `;
                    
                    showtimeElement.onclick = function() {
                        // Open showtime details or edit form
                        window.location.href = `admin-showtimes?action=edit&maSuatChieu=${showtime.id}`;
                    };
                    
                    cell.appendChild(showtimeElement);
                }
            });
        });
    }

    // Initialize calendar
    renderCalendar();
</script>

<jsp:include page="../layout/admin-footer.jsp" />
