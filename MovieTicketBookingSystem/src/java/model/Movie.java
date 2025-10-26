package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;

    private int maPhim;
    private String tenPhim;
    private String theLoai;
    private String loaiPhim;  // 2D/3D/IMAX
    private String daoDien;
    private String dienVien;
    private int doTuoiGioiHan;
    private int thoiLuong;  // phút
    private String noiDung;
    private LocalDateTime ngayKhoiChieu;
    private String poster;
    private String ngonNguPhuDe;
    private String trangThai;  // Đang chiếu, Sắp chiếu, Ngừng chiếu
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    // Constructors
    public Movie() {
    }

    public Movie(int maPhim, String tenPhim, String theLoai, String loaiPhim,
                 String daoDien, String dienVien, int doTuoiGioiHan, int thoiLuong,
                 String noiDung, LocalDateTime ngayKhoiChieu, String poster,
                 String ngonNguPhuDe) {
        this.maPhim = maPhim;
        this.tenPhim = tenPhim;
        this.theLoai = theLoai;
        this.loaiPhim = loaiPhim;
        this.daoDien = daoDien;
        this.dienVien = dienVien;
        this.doTuoiGioiHan = doTuoiGioiHan;
        this.thoiLuong = thoiLuong;
        this.noiDung = noiDung;
        this.ngayKhoiChieu = ngayKhoiChieu;
        this.poster = poster;
        this.ngonNguPhuDe = ngonNguPhuDe;
    }

    // Getters and Setters
    public int getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(int maPhim) {
        this.maPhim = maPhim;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public String getLoaiPhim() {
        return loaiPhim;
    }

    public void setLoaiPhim(String loaiPhim) {
        this.loaiPhim = loaiPhim;
    }

    public String getDaoDien() {
        return daoDien;
    }

    public void setDaoDien(String daoDien) {
        this.daoDien = daoDien;
    }

    public String getDienVien() {
        return dienVien;
    }

    public void setDienVien(String dienVien) {
        this.dienVien = dienVien;
    }

    public int getDoTuoiGioiHan() {
        return doTuoiGioiHan;
    }

    public void setDoTuoiGioiHan(int doTuoiGioiHan) {
        this.doTuoiGioiHan = doTuoiGioiHan;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public LocalDateTime getNgayKhoiChieu() {
        return ngayKhoiChieu;
    }

    public void setNgayKhoiChieu(LocalDateTime ngayKhoiChieu) {
        this.ngayKhoiChieu = ngayKhoiChieu;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getNgonNguPhuDe() {
        return ngonNguPhuDe;
    }

    public void setNgonNguPhuDe(String ngonNguPhuDe) {
        this.ngonNguPhuDe = ngonNguPhuDe;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public LocalDateTime getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(LocalDateTime ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "maPhim=" + maPhim +
                ", tenPhim='" + tenPhim + '\'' +
                ", theLoai='" + theLoai + '\'' +
                ", loaiPhim='" + loaiPhim + '\'' +
                ", thoiLuong=" + thoiLuong +
                ", ngayKhoiChieu=" + ngayKhoiChieu +
                '}';
    }
}
