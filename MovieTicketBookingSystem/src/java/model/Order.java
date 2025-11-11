package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int maOrder;
    private int maKH;
    private String orderCode;
    private String kenhDat;
    private String trangThai;
    private BigDecimal tongTien;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private Integer maGiamGia;
    
    // Constructors
    public Order() {
    }
    
    public Order(int maOrder, int maKH, String orderCode, String kenhDat, 
                 String trangThai, BigDecimal tongTien, String ghiChu, 
                 LocalDateTime createdAt, LocalDateTime paidAt, Integer maGiamGia) {
        this.maOrder = maOrder;
        this.maKH = maKH;
        this.orderCode = orderCode;
        this.kenhDat = kenhDat;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.ghiChu = ghiChu;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
        this.maGiamGia = maGiamGia;
    }
    
    // Getters and Setters
    public int getMaOrder() {
        return maOrder;
    }
    
    public void setMaOrder(int maOrder) {
        this.maOrder = maOrder;
    }
    
    public int getMaKH() {
        return maKH;
    }
    
    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }
    
    public String getOrderCode() {
        return orderCode;
    }
    
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
    
    public String getKenhDat() {
        return kenhDat;
    }
    
    public void setKenhDat(String kenhDat) {
        this.kenhDat = kenhDat;
    }
    
    public String getTrangThai() {
        return trangThai;
    }
    
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    
    public BigDecimal getTongTien() {
        return tongTien;
    }
    
    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }
    
    public String getGhiChu() {
        return ghiChu;
    }
    
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getPaidAt() {
        return paidAt;
    }
    
    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
    
    public Integer getMaGiamGia() {
        return maGiamGia;
    }
    
    public void setMaGiamGia(Integer maGiamGia) {
        this.maGiamGia = maGiamGia;
    }
}
