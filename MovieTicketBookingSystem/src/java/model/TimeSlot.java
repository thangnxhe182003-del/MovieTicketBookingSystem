package model;

import java.time.LocalTime;

/**
 * Model class cho TimeSlot (khung giờ đề xuất)
 */
public class TimeSlot {
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isAvailable;
    private String reason; // Lý do không khả dụng (nếu có)
    
    public TimeSlot() {}
    
    public TimeSlot(LocalTime startTime, LocalTime endTime, boolean isAvailable) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
    }
    
    public TimeSlot(LocalTime startTime, LocalTime endTime, boolean isAvailable, String reason) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
        this.reason = reason;
    }
    
    // Getters and Setters
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    @Override
    public String toString() {
        return startTime + " - " + endTime + (isAvailable ? " (Có thể)" : " (Không thể: " + reason + ")");
    }
}
