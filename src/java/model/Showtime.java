package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.math.BigDecimal;

public class Showtime implements Serializable {
    private static final long serialVersionUID = 1L;

    private int maSuatChieu;
    private int maPhim;
    private int maPhong;
    private LocalDate ngayChieu;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
    private BigDecimal giaVeCoSo;
    private String ngonNguAmThanh;

    // Additional fields for displaying
    private String tenPhim;
    private String tenPhong;
    private String tenRap;

    // Constructors
    public Showtime() {
    }

    public Showtime(int maSuatChieu, int maPhim, int maPhong, LocalDate ngayChieu,
                    LocalTime gioBatDau, LocalTime gioKetThuc, BigDecimal giaVeCoSo,
                    String ngonNguAmThanh) {
        this.maSuatChieu = maSuatChieu;
        this.maPhim = maPhim;
        this.maPhong = maPhong;
        this.ngayChieu = ngayChieu;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.giaVeCoSo = giaVeCoSo;
        this.ngonNguAmThanh = ngonNguAmThanh;
    }

    // Getters and Setters
    public int getMaSuatChieu() {
        return maSuatChieu;
    }

    public void setMaSuatChieu(int maSuatChieu) {
        this.maSuatChieu = maSuatChieu;
    }

    public int getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(int maPhim) {
        this.maPhim = maPhim;
    }

    public int getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(int maPhong) {
        this.maPhong = maPhong;
    }

    public LocalDate getNgayChieu() {
        return ngayChieu;
    }

    public void setNgayChieu(LocalDate ngayChieu) {
        this.ngayChieu = ngayChieu;
    }

    public LocalTime getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(LocalTime gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public LocalTime getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(LocalTime gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public BigDecimal getGiaVeCoSo() {
        return giaVeCoSo;
    }

    public void setGiaVeCoSo(BigDecimal giaVeCoSo) {
        this.giaVeCoSo = giaVeCoSo;
    }

    public String getNgonNguAmThanh() {
        return ngonNguAmThanh;
    }

    public void setNgonNguAmThanh(String ngonNguAmThanh) {
        this.ngonNguAmThanh = ngonNguAmThanh;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public String getTenRap() {
        return tenRap;
    }

    public void setTenRap(String tenRap) {
        this.tenRap = tenRap;
    }

    @Override
    public String toString() {
        return "Showtime{" +
                "maSuatChieu=" + maSuatChieu +
                ", ngayChieu=" + ngayChieu +
                ", gioBatDau=" + gioBatDau +
                ", giaVeCoSo=" + giaVeCoSo +
                '}';
    }
}
