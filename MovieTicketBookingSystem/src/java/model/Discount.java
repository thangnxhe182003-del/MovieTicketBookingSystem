package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Discount implements Serializable {
    private static final long serialVersionUID = 1L;

    private int maGiamGia;
    private String maCode;
    private String tenGiamGia;
    private String moTa;
    private String loaiGiamGia;      // Vé / Đồ ăn / Toàn đơn
    private String hinhThucGiam;     // PhanTram hoặc TienMat
    private BigDecimal giaTriGiam;
    private BigDecimal giaTriToiDa;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private String trangThai;        // Hoạt Động / Hết hạn / Không hoạt động
    private Integer soLanSuDung;
    private int daSuDung;
    private LocalDateTime createdAt;

    // Constructors
    public Discount() {
    }

    public Discount(int maGiamGia, String maCode, String tenGiamGia, String moTa, 
                    String loaiGiamGia, String hinhThucGiam, BigDecimal giaTriGiam, 
                    BigDecimal giaTriToiDa, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, 
                    String trangThai, Integer soLanSuDung, int daSuDung, LocalDateTime createdAt) {
        this.maGiamGia = maGiamGia;
        this.maCode = maCode;
        this.tenGiamGia = tenGiamGia;
        this.moTa = moTa;
        this.loaiGiamGia = loaiGiamGia;
        this.hinhThucGiam = hinhThucGiam;
        this.giaTriGiam = giaTriGiam;
        this.giaTriToiDa = giaTriToiDa;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
        this.soLanSuDung = soLanSuDung;
        this.daSuDung = daSuDung;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getMaGiamGia() {
        return maGiamGia;
    }

    public void setMaGiamGia(int maGiamGia) {
        this.maGiamGia = maGiamGia;
    }

    public String getMaCode() {
        return maCode;
    }

    public void setMaCode(String maCode) {
        this.maCode = maCode;
    }

    public String getTenGiamGia() {
        return tenGiamGia;
    }

    public void setTenGiamGia(String tenGiamGia) {
        this.tenGiamGia = tenGiamGia;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getLoaiGiamGia() {
        return loaiGiamGia;
    }

    public void setLoaiGiamGia(String loaiGiamGia) {
        this.loaiGiamGia = loaiGiamGia;
    }

    public String getHinhThucGiam() {
        return hinhThucGiam;
    }

    public void setHinhThucGiam(String hinhThucGiam) {
        this.hinhThucGiam = hinhThucGiam;
    }

    public BigDecimal getGiaTriGiam() {
        return giaTriGiam;
    }

    public void setGiaTriGiam(BigDecimal giaTriGiam) {
        this.giaTriGiam = giaTriGiam;
    }

    public BigDecimal getGiaTriToiDa() {
        return giaTriToiDa;
    }

    public void setGiaTriToiDa(BigDecimal giaTriToiDa) {
        this.giaTriToiDa = giaTriToiDa;
    }

    public LocalDateTime getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDateTime ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDateTime getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDateTime ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Integer getSoLanSuDung() {
        return soLanSuDung;
    }

    public void setSoLanSuDung(Integer soLanSuDung) {
        this.soLanSuDung = soLanSuDung;
    }

    public int getDaSuDung() {
        return daSuDung;
    }

    public void setDaSuDung(int daSuDung) {
        this.daSuDung = daSuDung;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "maGiamGia=" + maGiamGia +
                ", maCode='" + maCode + '\'' +
                ", tenGiamGia='" + tenGiamGia + '\'' +
                ", loaiGiamGia='" + loaiGiamGia + '\'' +
                ", hinhThucGiam='" + hinhThucGiam + '\'' +
                ", giaTriGiam=" + giaTriGiam +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}

