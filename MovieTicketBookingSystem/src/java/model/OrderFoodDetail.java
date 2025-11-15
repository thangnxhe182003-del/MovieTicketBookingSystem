package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderFoodDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int maOrder;
    private int maSP;
    private int soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    
    // Constructors
    public OrderFoodDetail() {
    }
    
    public OrderFoodDetail(int maOrder, int maSP, int soLuong, BigDecimal donGia, BigDecimal thanhTien) {
        this.maOrder = maOrder;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }
    
    // Getters and Setters
    public int getMaOrder() {
        return maOrder;
    }
    
    public void setMaOrder(int maOrder) {
        this.maOrder = maOrder;
    }
    
    public int getMaSP() {
        return maSP;
    }
    
    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }
    
    public int getSoLuong() {
        return soLuong;
    }
    
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    
    public BigDecimal getDonGia() {
        return donGia;
    }
    
    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }
    
    public BigDecimal getThanhTien() {
        return thanhTien;
    }
    
    public void setThanhTien(BigDecimal thanhTien) {
        this.thanhTien = thanhTien;
    }
}

