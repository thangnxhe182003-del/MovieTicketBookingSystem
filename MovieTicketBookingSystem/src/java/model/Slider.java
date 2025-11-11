package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Slider implements Serializable {
    private static final long serialVersionUID = 1L;

    private int maSlider;
    private String tieuDe;
    private String moTa;
    private String anhSlide;
    private int thuTuHienThi;
    private String trangThai;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Slider() {
    }

    public Slider(int maSlider, String tieuDe, String moTa, String anhSlide, int thuTuHienThi, 
                  String trangThai, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc,
                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.maSlider = maSlider;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.anhSlide = anhSlide;
        this.thuTuHienThi = thuTuHienThi;
        this.trangThai = trangThai;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getMaSlider() {
        return maSlider;
    }

    public void setMaSlider(int maSlider) {
        this.maSlider = maSlider;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getAnhSlide() {
        return anhSlide;
    }

    public void setAnhSlide(String anhSlide) {
        this.anhSlide = anhSlide;
    }

    public int getThuTuHienThi() {
        return thuTuHienThi;
    }

    public void setThuTuHienThi(int thuTuHienThi) {
        this.thuTuHienThi = thuTuHienThi;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Slider{" +
                "maSlider=" + maSlider +
                ", tieuDe='" + tieuDe + '\'' +
                ", anhSlide='" + anhSlide + '\'' +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}

