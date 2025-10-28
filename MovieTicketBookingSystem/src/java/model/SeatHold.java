package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SeatHold implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long holdId;
    private int maSuatChieu;
    private int maGhe;
    private Integer maKH; // nullable
    private LocalDateTime heldAt;
    private LocalDateTime expiresAt;
    private String ghiChu;
    
    // Constructors
    public SeatHold() {
    }
    
    public SeatHold(Long holdId, int maSuatChieu, int maGhe, Integer maKH, 
                    LocalDateTime heldAt, LocalDateTime expiresAt, String ghiChu) {
        this.holdId = holdId;
        this.maSuatChieu = maSuatChieu;
        this.maGhe = maGhe;
        this.maKH = maKH;
        this.heldAt = heldAt;
        this.expiresAt = expiresAt;
        this.ghiChu = ghiChu;
    }
    
    // Getters and Setters
    public Long getHoldId() {
        return holdId;
    }
    
    public void setHoldId(Long holdId) {
        this.holdId = holdId;
    }
    
    public int getMaSuatChieu() {
        return maSuatChieu;
    }
    
    public void setMaSuatChieu(int maSuatChieu) {
        this.maSuatChieu = maSuatChieu;
    }
    
    public int getMaGhe() {
        return maGhe;
    }
    
    public void setMaGhe(int maGhe) {
        this.maGhe = maGhe;
    }
    
    public Integer getMaKH() {
        return maKH;
    }
    
    public void setMaKH(Integer maKH) {
        this.maKH = maKH;
    }
    
    public LocalDateTime getHeldAt() {
        return heldAt;
    }
    
    public void setHeldAt(LocalDateTime heldAt) {
        this.heldAt = heldAt;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public String getGhiChu() {
        return ghiChu;
    }
    
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    
    public boolean isExpired() {
        return expiresAt != null && java.time.LocalDateTime.now().isAfter(expiresAt);
    }
}

