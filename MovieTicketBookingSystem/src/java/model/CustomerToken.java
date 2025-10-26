/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

/**
 * Model class for CustomerToken table which stores OTP codes for various purposes
 * like email verification and password reset.
 * 
 * @author thang
 */
public class CustomerToken {
    
    private Long tokenId;
    private Integer maKH;
    private String purpose;
    private String otpCode;
    private LocalDateTime expiresAt;
    private boolean isUsed;
    private LocalDateTime usedAt;
    private LocalDateTime createdAt;
    
    // Constants for token purposes
    public static final String PURPOSE_REGISTER_VERIFY = "REGISTER_VERIFY";
    public static final String PURPOSE_PASSWORD_RESET = "PASSWORD_RESET";
    
    public CustomerToken() {
    }

    public CustomerToken(Long tokenId, Integer maKH, String purpose, String otpCode, 
            LocalDateTime expiresAt, boolean isUsed, LocalDateTime usedAt, 
            LocalDateTime createdAt) {
        this.tokenId = tokenId;
        this.maKH = maKH;
        this.purpose = purpose;
        this.otpCode = otpCode;
        this.expiresAt = expiresAt;
        this.isUsed = isUsed;
        this.usedAt = usedAt;
        this.createdAt = createdAt;
    }
    
    // Constructor for creating new tokens (without ID, usedAt, createdAt)
    public CustomerToken(Integer maKH, String purpose, String otpCode, 
            LocalDateTime expiresAt) {
        this.maKH = maKH;
        this.purpose = purpose;
        this.otpCode = otpCode;
        this.expiresAt = expiresAt;
        this.isUsed = false;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public Integer getMaKH() {
        return maKH;
    }

    public void setMaKH(Integer maKH) {
        this.maKH = maKH;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isIsUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Check if the token is expired
     * @return true if current time is after expiration time
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    /**
     * Check if the token is valid (not used and not expired)
     * @return true if the token is valid
     */
    public boolean isValid() {
        return !isUsed && !isExpired();
    }
}
