package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private Long maThanhToan;
    private Long maOrder;
    private BigDecimal soTien;
    private String phuongThuc;
    private String trangThai;
    private LocalDateTime ngayThanhToan;
    private Integer maNhanVien;

    // Constructors
    public Payment() {
    }

    public Payment(Long maThanhToan, Long maOrder, BigDecimal soTien, String phuongThuc, 
                  String trangThai, LocalDateTime ngayThanhToan, Integer maNhanVien) {
        this.maThanhToan = maThanhToan;
        this.maOrder = maOrder;
        this.soTien = soTien;
        this.phuongThuc = phuongThuc;
        this.trangThai = trangThai;
        this.ngayThanhToan = ngayThanhToan;
        this.maNhanVien = maNhanVien;
    }

    // Getters and Setters
    public Long getMaThanhToan() {
        return maThanhToan;
    }

    public void setMaThanhToan(Long maThanhToan) {
        this.maThanhToan = maThanhToan;
    }

    public Long getMaOrder() {
        return maOrder;
    }

    public void setMaOrder(Long maOrder) {
        this.maOrder = maOrder;
    }

    public BigDecimal getSoTien() {
        return soTien;
    }

    public void setSoTien(BigDecimal soTien) {
        this.soTien = soTien;
    }

    public String getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDateTime getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(LocalDateTime ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public Integer getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(Integer maNhanVien) {
        this.maNhanVien = maNhanVien;
    }
}
