package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
    private Long maOrder;
    private Integer maKH;
    private String orderCode;
    private String kenhDat;
    private String trangThai;
    private BigDecimal tongTien;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    // Constructors
    public Order() {
    }

    public Order(Long maOrder, Integer maKH, String orderCode, String kenhDat, 
                String trangThai, BigDecimal tongTien, String ghiChu, 
                LocalDateTime createdAt, LocalDateTime paidAt) {
        this.maOrder = maOrder;
        this.maKH = maKH;
        this.orderCode = orderCode;
        this.kenhDat = kenhDat;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.ghiChu = ghiChu;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
    }

    // Getters and Setters
    public Long getMaOrder() {
        return maOrder;
    }

    public void setMaOrder(Long maOrder) {
        this.maOrder = maOrder;
    }

    public Integer getMaKH() {
        return maKH;
    }

    public void setMaKH(Integer maKH) {
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
}
