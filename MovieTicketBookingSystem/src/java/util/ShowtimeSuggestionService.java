package util;

import model.Movie;
import model.Showtime;
import model.TimeSlot;
import dal.ShowtimeDAO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service để đề xuất suất chiếu thông minh
 */
public class ShowtimeSuggestionService {
    
    private static final LocalTime EARLIEST_SHOWTIME = LocalTime.of(9, 0);  // 9:00 sáng
    private static final LocalTime LATEST_SHOWTIME = LocalTime.of(23, 0);   // 11:00 tối
    private static final int CLEANUP_TIME = 30; // 30 phút dọn dẹp giữa các suất
    
    private ShowtimeDAO showtimeDAO;
    
    public ShowtimeSuggestionService() {
        this.showtimeDAO = new ShowtimeDAO();
    }
    
    /**
     * Đề xuất các khung giờ cho suất chiếu mới dựa trên khoảng trống thực tế
     */
    public List<TimeSlot> suggestShowtimes(int maPhong, LocalDate ngayChieu, Movie movie) {
        List<TimeSlot> suggestions = new ArrayList<>();
        
        // Lấy danh sách suất chiếu hiện có trong ngày
        List<Showtime> existingShowtimes = showtimeDAO.getShowtimesByRoomAndDate(maPhong, ngayChieu);
        
        // Debug: In ra thông tin suất chiếu hiện có
        System.out.println("=== KIỂM TRA SUẤT CHIẾU HIỆN CÓ TRONG DATABASE ===");
        System.out.println("Phòng: " + maPhong + ", Ngày: " + ngayChieu);
        System.out.println("Số suất chiếu hiện có: " + existingShowtimes.size());
        
        if (existingShowtimes.isEmpty()) {
            System.out.println(">>> NGÀY TRỐNG - Không có suất chiếu nào");
        } else {
            System.out.println(">>> ĐÃ CÓ SUẤT CHIẾU - Danh sách:");
            for (int i = 0; i < existingShowtimes.size(); i++) {
                Showtime s = existingShowtimes.get(i);
                System.out.println("  Suất #" + (i+1) + ": " + 
                    s.getGioBatDau().toLocalTime() + " - " + s.getGioKetThuc().toLocalTime() + 
                    " (Ngày: " + s.getNgayChieu().toLocalDate() + ")");
            }
        }
        System.out.println("================================================");
        System.out.println();
        
        // Tìm các khoảng trống có thể chứa phim mới
        List<TimeSlot> availableSlots = findAvailableSlotsForMovie(existingShowtimes, movie.getThoiLuong());
        
        System.out.println("=== KHOẢNG TRỐNG TÌM ĐƯỢC ===");
        System.out.println("Số khoảng trống: " + availableSlots.size());
        for (int i = 0; i < availableSlots.size(); i++) {
            TimeSlot slot = availableSlots.get(i);
            System.out.println("  Khoảng trống #" + (i+1) + ": " + slot.getStartTime() + " - " + slot.getEndTime());
        }
        System.out.println("==============================");
        System.out.println();
        
        // Tạo đề xuất cho từng khoảng trống
        // LUÔN TẠO NHIỀU SUẤT CHIẾU trong mỗi khoảng trống
        System.out.println(">>> TẠO ĐỀ XUẤT - Nhiều suất chiếu cho mỗi khoảng trống");
        for (TimeSlot slot : availableSlots) {
            List<TimeSlot> movieSlots = generateMultipleSlotsInRange(slot.getStartTime(), slot.getEndTime(), movie.getThoiLuong());
            suggestions.addAll(movieSlots);
        }
        
        // Loại bỏ các suất chiếu trùng lặp
        suggestions = removeOverlappingSlots(suggestions);
        
        // LỌC BỎ TẤT CẢ SUẤT CHIẾU NGOÀI KHUNG GIỜ 9H-23H
        List<TimeSlot> validSuggestions = new ArrayList<>();
        for (TimeSlot slot : suggestions) {
            // Chỉ giữ lại suất chiếu bắt đầu >= 9h và kết thúc <= 23h
            if (!slot.getStartTime().isBefore(EARLIEST_SHOWTIME) && 
                !slot.getEndTime().isAfter(LATEST_SHOWTIME)) {
                validSuggestions.add(slot);
            } else {
                System.out.println("Debug - Loại bỏ suất chiếu ngoài khung giờ: " + 
                                 slot.getStartTime() + " - " + slot.getEndTime());
            }
        }
        suggestions = validSuggestions;
        
        // Nếu không có khoảng trống nào, tạo đề xuất từ 9h sáng
        if (suggestions.isEmpty()) {
            System.out.println("Debug - Không có khoảng trống, tạo fallback slots");
            suggestions = generateFallbackSlots(movie.getThoiLuong());
        }
        
        // Debug: In ra kết quả cuối cùng
        System.out.println("Debug - Số đề xuất cuối cùng: " + suggestions.size());
        for (TimeSlot slot : suggestions) {
            System.out.println("Debug - Đề xuất: " + slot.getStartTime() + " - " + slot.getEndTime() + " (Available: " + slot.isAvailable() + ")");
        }
        
        return suggestions;
    }
    
    /**
     * Đề xuất suất chiếu cho phim mới dựa trên thời gian trống
     */
    public List<TimeSlot> suggestShowtimesForNewMovie(int maPhong, LocalDate ngayChieu, Movie movie) {
        List<TimeSlot> suggestions = new ArrayList<>();
        
        // Lấy danh sách suất chiếu hiện có
        List<Showtime> existingShowtimes = showtimeDAO.getShowtimesByRoomAndDate(maPhong, ngayChieu);
        
        // Tìm các khoảng trống
        List<TimeSlot> availableSlots = findAvailableSlots(existingShowtimes, movie.getThoiLuong());
        
        // Tạo đề xuất cho từng khoảng trống
        for (TimeSlot slot : availableSlots) {
            List<TimeSlot> movieSlots = generateMultipleSlotsInRange(slot.getStartTime(), slot.getEndTime(), movie.getThoiLuong());
            suggestions.addAll(movieSlots);
        }
        
        return suggestions;
    }
    
    /**
     * Tạo các suất chiếu fallback khi không có khoảng trống (từ 9h sáng)
     */
    private List<TimeSlot> generateFallbackSlots(int movieDuration) {
        List<TimeSlot> slots = new ArrayList<>();
        
        // Các khung giờ chính (bắt đầu từ 9h, cách nhau 2.5 giờ)
        LocalTime[] mainStartTimes = {
            LocalTime.of(9, 0),   // 9:00
            LocalTime.of(11, 30), // 11:30
            LocalTime.of(14, 0),  // 14:00
            LocalTime.of(16, 30), // 16:30
            LocalTime.of(19, 0),  // 19:00
            LocalTime.of(21, 30)  // 21:30
        };
        
        for (LocalTime startTime : mainStartTimes) {
            LocalTime endTime = startTime.plusMinutes(movieDuration);
            
            // Chỉ thêm nếu suất chiếu kết thúc trước 11h tối
            if (endTime.isBefore(LATEST_SHOWTIME) || endTime.equals(LATEST_SHOWTIME)) {
                slots.add(new TimeSlot(startTime, endTime, true));
            }
        }
        
        return slots;
    }
    
    /**
     * Tạo danh sách khung giờ chính từ 9h đến 11h tối (method cũ - giữ lại để tương thích)
     */
    private List<TimeSlot> generateTimeSlots(int movieDuration) {
        return generateFallbackSlots(movieDuration);
    }
    
    /**
     * Kiểm tra xem khung giờ có khả dụng không
     */
    private boolean isTimeSlotAvailable(TimeSlot slot, List<Showtime> existingShowtimes) {
        for (Showtime existing : existingShowtimes) {
            if (isTimeOverlap(slot.getStartTime(), slot.getEndTime(), 
                             existing.getGioBatDau(), existing.getGioKetThuc())) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Kiểm tra xem hai khoảng thời gian có trùng nhau không
     */
    private boolean isTimeOverlap(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
    
    private boolean isTimeOverlap(LocalTime start1, LocalTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2.toLocalTime()) && start2.toLocalTime().isBefore(end1);
    }
    
    /**
     * Tìm tất cả các khoảng trống có thể chứa phim mới trong ngày
     */
    private List<TimeSlot> findAvailableSlotsForMovie(List<Showtime> existingShowtimes, int movieDuration) {
        List<TimeSlot> availableSlots = new ArrayList<>();
        
        if (existingShowtimes.isEmpty()) {
            // Nếu không có suất chiếu nào, trả về khoảng trống từ 9h sáng
            availableSlots.add(new TimeSlot(EARLIEST_SHOWTIME, LATEST_SHOWTIME, true));
            return availableSlots;
        }
        
        // Sắp xếp suất chiếu theo thời gian bắt đầu
        existingShowtimes.sort((s1, s2) -> s1.getGioBatDau().compareTo(s2.getGioBatDau()));
        
        System.out.println("Debug - Số suất chiếu hiện có: " + existingShowtimes.size());
        for (Showtime s : existingShowtimes) {
            System.out.println("Debug - Suất chiếu: " + s.getGioBatDau().toLocalTime() + " - " + s.getGioKetThuc().toLocalTime());
        }
        
        // Tìm khoảng trống trước suất chiếu đầu tiên
        Showtime firstShowtime = existingShowtimes.get(0);
        LocalTime firstStartTime = firstShowtime.getGioBatDau().toLocalTime();
        
        if (firstStartTime.isAfter(EARLIEST_SHOWTIME)) {
            LocalTime gapStart = EARLIEST_SHOWTIME;
            LocalTime gapEnd = firstStartTime;
            
            // Tính khoảng cách (phút)
            long availableMinutes = java.time.Duration.between(gapStart, gapEnd).toMinutes();
            
            System.out.println("Debug - Kiểm tra khoảng trống đầu ngày: " + gapStart + " - " + gapEnd + 
                             " (Có: " + availableMinutes + " phút, Phim cần: " + movieDuration + " phút)");
            
            // Điều kiện: Khoảng trống >= thời lượng phim
            if (availableMinutes >= movieDuration) {
                availableSlots.add(new TimeSlot(gapStart, gapEnd, true));
                System.out.println("Debug - ✓ Khoảng trống đầu ngày PHÙ HỢP");
            } else {
                System.out.println("Debug - ✗ Khoảng trống đầu ngày KHÔNG ĐỦ");
            }
        }
        
        // Tìm các khoảng trống giữa các suất chiếu
        for (int i = 0; i < existingShowtimes.size() - 1; i++) {
            Showtime currentShowtime = existingShowtimes.get(i);
            Showtime nextShowtime = existingShowtimes.get(i + 1);
            
            LocalTime currentEndTime = currentShowtime.getGioKetThuc().toLocalTime();
            LocalTime nextStartTime = nextShowtime.getGioBatDau().toLocalTime();
            
            // Tính khoảng cách TOÀN BỘ giữa 2 suất (phút)
            long totalGapMinutes = java.time.Duration.between(currentEndTime, nextStartTime).toMinutes();
            
            // Thời gian bắt đầu khoảng trống (sau suất chiếu hiện tại + 30 phút dọn dẹp)
            LocalTime gapStart = currentEndTime.plusMinutes(CLEANUP_TIME);
            LocalTime gapEnd = nextStartTime;
            
            // Tính khoảng cách SAU KHI TRỪ 30 PHÚT DỌN DẸP (phút)
            long availableMinutes = java.time.Duration.between(gapStart, gapEnd).toMinutes();
            
            System.out.println("Debug - Kiểm tra khoảng trống giữa: " + currentEndTime + " - " + nextStartTime + 
                             " (Toàn bộ: " + totalGapMinutes + " phút, Sau trừ 30p dọn dẹp: " + availableMinutes + " phút, Phim cần: " + movieDuration + " phút)");
            
            // Điều kiện: Khoảng trống sau khi trừ 30 phút dọn dẹp >= thời lượng phim
            if (availableMinutes >= movieDuration) {
                availableSlots.add(new TimeSlot(gapStart, gapEnd, true));
                System.out.println("Debug - ✓ Khoảng trống giữa PHÙ HỢP");
            } else {
                System.out.println("Debug - ✗ Khoảng trống giữa KHÔNG ĐỦ");
            }
        }
        
        // Tìm khoảng trống sau suất chiếu cuối cùng
        Showtime lastShowtime = existingShowtimes.get(existingShowtimes.size() - 1);
        LocalTime lastEndTime = lastShowtime.getGioKetThuc().toLocalTime();
        LocalTime lastStartTime = lastShowtime.getGioBatDau().toLocalTime();
        
        // Kiểm tra xem suất chiếu có kết thúc sau nửa đêm không (sang ngày mới)
        // Nếu lastEndTime < lastStartTime thì suất chiếu sang ngày hôm sau
        boolean isCrossMidnight = lastEndTime.isBefore(lastStartTime) || 
                                 (lastStartTime.isAfter(LocalTime.of(20, 0)) && lastEndTime.isBefore(LocalTime.of(5, 0)));
        
        if (isCrossMidnight) {
            // Suất chiếu sang ngày mới, không tìm khoảng trống sau nó
            System.out.println("Debug - Suất chiếu cuối sang ngày mới (" + lastStartTime + " - " + lastEndTime + "), bỏ qua khoảng trống sau");
        } else if (lastEndTime.isBefore(LATEST_SHOWTIME)) {
            // Thời gian bắt đầu khoảng trống (sau suất chiếu cuối + 30 phút dọn dẹp)
            LocalTime gapStart = lastEndTime.plusMinutes(CLEANUP_TIME);
            LocalTime gapEnd = LATEST_SHOWTIME;
            
            // Tính khoảng cách SAU KHI TRỪ 30 PHÚT DỌN DẸP (phút)
            long availableMinutes = java.time.Duration.between(gapStart, gapEnd).toMinutes();
            
            System.out.println("Debug - Kiểm tra khoảng trống cuối ngày: " + gapStart + " - " + gapEnd + 
                             " (Có: " + availableMinutes + " phút, Phim cần: " + movieDuration + " phút)");
            
            // Điều kiện: Khoảng trống sau khi trừ 30 phút dọn dẹp >= thời lượng phim
            if (availableMinutes >= movieDuration) {
                availableSlots.add(new TimeSlot(gapStart, gapEnd, true));
                System.out.println("Debug - ✓ Khoảng trống cuối ngày PHÙ HỢP");
            } else {
                System.out.println("Debug - ✗ Khoảng trống cuối ngày KHÔNG ĐỦ");
            }
        }
        
        System.out.println("Debug - Tổng số khoảng trống tìm được: " + availableSlots.size());
        return availableSlots;
    }
    
    /**
     * Tìm các khoảng trống trong ngày (method cũ - giữ lại để tương thích)
     */
    private List<TimeSlot> findAvailableSlots(List<Showtime> existingShowtimes, int movieDuration) {
        return findAvailableSlotsForMovie(existingShowtimes, movieDuration);
    }
    
    /**
     * Tạo NHIỀU suất chiếu trong khoảng trống (cho ngày trống)
     * Ví dụ: 9h-23h -> Tạo 9:00-11:08, 11:38-13:46, 14:16-16:24, ...
     */
    private List<TimeSlot> generateMultipleSlotsInRange(LocalTime startTime, LocalTime endTime, int movieDuration) {
        List<TimeSlot> slots = new ArrayList<>();
        
        // Đảm bảo startTime >= 9h
        LocalTime currentTime = startTime.isBefore(EARLIEST_SHOWTIME) ? EARLIEST_SHOWTIME : startTime;
        
        // Đảm bảo endTime <= 23h
        LocalTime effectiveEndTime = endTime.isAfter(LATEST_SHOWTIME) ? LATEST_SHOWTIME : endTime;
        
        System.out.println("  >> Tạo nhiều suất trong khoảng: " + currentTime + " - " + effectiveEndTime);
        
        int slotNumber = 0;
        int maxSlots = 10; // Giới hạn tối đa để tránh vòng lặp vô tận
        
        // Tạo các suất chiếu, mỗi suất cách nhau (thời lượng phim + 30 phút)
        while (slotNumber < maxSlots) {
            // KIỂM TRA 1: Đảm bảo currentTime vẫn trong khung giờ hợp lệ
            if (currentTime.isAfter(effectiveEndTime) || currentTime.equals(effectiveEndTime)) {
                System.out.println("  >> Dừng: Đã hết khoảng trống");
                break;
            }
            
            LocalTime slotEndTime = currentTime.plusMinutes(movieDuration);
            
            // KIỂM TRA 2: Suất chiếu có qua nửa đêm không
            if (slotEndTime.isBefore(currentTime)) {
                System.out.println("  >> Dừng: Suất chiếu qua nửa đêm (" + currentTime + " - " + slotEndTime + ")");
                break;
            }
            
            // KIỂM TRA 3: Suất chiếu có kết thúc sau 23h không
            if (slotEndTime.isAfter(LATEST_SHOWTIME)) {
                System.out.println("  >> Dừng: Suất chiếu sẽ kết thúc sau 23h (" + currentTime + " - " + slotEndTime + ")");
                break;
            }
            
            // KIỂM TRA 4: Suất chiếu có vượt quá khoảng trống không (QUAN TRỌNG!)
            if (slotEndTime.isAfter(effectiveEndTime)) {
                System.out.println("  >> Dừng: Suất chiếu vượt quá khoảng trống (" + currentTime + " - " + slotEndTime + " > " + effectiveEndTime + ")");
                break;
            }
            
            // Thêm suất chiếu hợp lệ
            slots.add(new TimeSlot(currentTime, slotEndTime, true));
            slotNumber++;
            System.out.println("  >> Đề xuất #" + slotNumber + ": " + currentTime + " - " + slotEndTime);
            
            // Chuyển sang suất chiếu tiếp theo (kết thúc + 30 phút dọn dẹp)
            currentTime = slotEndTime.plusMinutes(CLEANUP_TIME);
        }
        
        System.out.println("  >> Tổng: " + slots.size() + " suất chiếu");
        return slots;
    }

    
    
    /**
     * Loại bỏ các suất chiếu trùng lặp (chỉ giữ lại 1 suất chiếu nếu có nhiều suất giống nhau)
     */
    private List<TimeSlot> removeOverlappingSlots(List<TimeSlot> slots) {
        if (slots.isEmpty()) {
            return slots;
        }
        
        // Sắp xếp theo thời gian bắt đầu
        slots.sort((s1, s2) -> s1.getStartTime().compareTo(s2.getStartTime()));
        
        List<TimeSlot> result = new ArrayList<>();
        
        for (TimeSlot currentSlot : slots) {
            // Kiểm tra xem đã có suất chiếu giống hệt chưa
            boolean isDuplicate = false;
            for (TimeSlot existingSlot : result) {
                if (existingSlot.getStartTime().equals(currentSlot.getStartTime()) && 
                    existingSlot.getEndTime().equals(currentSlot.getEndTime())) {
                    isDuplicate = true;
                    break;
                }
            }
            
            if (!isDuplicate) {
                result.add(currentSlot);
            } else {
                System.out.println("Debug - Bỏ qua suất chiếu trùng lặp: " + 
                                 currentSlot.getStartTime() + " - " + currentSlot.getEndTime());
            }
        }
        
        return result;
    }
    
    /**
     * Format thời gian để hiển thị
     */
    public String formatTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
