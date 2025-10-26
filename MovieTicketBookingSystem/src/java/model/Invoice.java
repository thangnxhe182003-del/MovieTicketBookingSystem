package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Invoice {
    private Long maHoaDon;
    private Long maOrder;
    private BigDecimal tongTien;
    private String chiTiet;
    private LocalDateTime ngayXuat;

    // Constructors
    public Invoice() {
    }

    public Invoice(Long maHoaDon, Long maOrder, BigDecimal tongTien, String chiTiet, LocalDateTime ngayXuat) {
        this.maHoaDon = maHoaDon;
        this.maOrder = maOrder;
        this.tongTien = tongTien;
        this.chiTiet = chiTiet;
        this.ngayXuat = ngayXuat;
    }

    // Getters and Setters
    public Long getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(Long maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public Long getMaOrder() {
        return maOrder;
    }

    public void setMaOrder(Long maOrder) {
        this.maOrder = maOrder;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public String getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(String chiTiet) {
        this.chiTiet = chiTiet;
    }

    public LocalDateTime getNgayXuat() {
        return ngayXuat;
    }

    public void setNgayXuat(LocalDateTime ngayXuat) {
        this.ngayXuat = ngayXuat;
    }
}
