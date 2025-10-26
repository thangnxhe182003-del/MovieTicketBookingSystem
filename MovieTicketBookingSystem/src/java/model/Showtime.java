package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Showtime implements Serializable {
    private static final long serialVersionUID = 1L;

    private int maSuatChieu;
    private int maPhim;
    private int maPhong;
    private LocalDateTime ngayChieu;
    private LocalDateTime gioBatDau;
    private LocalDateTime gioKetThuc;
    private BigDecimal giaVeCoSo;
    private String ngonNguAmThanh;

    // Additional fields for displaying
    private String tenPhim;
    private String tenPhong;
    private String tenRap;
    private String poster;

    public Showtime(int maSuatChieu, int maPhim, int maPhong, LocalDateTime ngayChieu, LocalDateTime gioBatDau, LocalDateTime gioKetThuc, BigDecimal giaVeCoSo, String ngonNguAmThanh, String tenPhim, String tenPhong, String tenRap, String poster) {
        this.maSuatChieu = maSuatChieu;
        this.maPhim = maPhim;
        this.maPhong = maPhong;
        this.ngayChieu = ngayChieu;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.giaVeCoSo = giaVeCoSo;
        this.ngonNguAmThanh = ngonNguAmThanh;
        this.tenPhim = tenPhim;
        this.tenPhong = tenPhong;
        this.tenRap = tenRap;
        this.poster = poster;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
    
    // Constructors
    public Showtime() {
    }

    public Showtime(int maSuatChieu, int maPhim, int maPhong, LocalDateTime ngayChieu,
                    LocalDateTime gioBatDau, LocalDateTime gioKetThuc, BigDecimal giaVeCoSo,
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

    public LocalDateTime getNgayChieu() {
        return ngayChieu;
    }

    public void setNgayChieu(LocalDateTime ngayChieu) {
        this.ngayChieu = ngayChieu;
    }

    public LocalDateTime getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(LocalDateTime gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public LocalDateTime getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(LocalDateTime gioKetThuc) {
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
