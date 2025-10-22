/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author thang
 */
public class Movie {

    private int maPhim;
    private String tenPhim;
    private String theLoai;
    private String loaiPhim;
    private String daoDien;
    private String dienVien;
    private int doTuoiGioiHan;
    private int thoiLuong;
    private String noiDung;
    private String ngayKhoiChieu;
    private String poster;
    private String ngonNguPhuDe;

    public Movie() {
    }

    public Movie(int maPhim, String tenPhim, String theLoai, String loaiPhim, String daoDien, String dienVien, int doTuoiGioiHan, int thoiLuong, String noiDung, String ngayKhoiChieu, String poster, String ngonNguPhuDe) {
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

    public String getNgayKhoiChieu() {
        return ngayKhoiChieu;
    }

    public void setNgayKhoiChieu(String ngayKhoiChieu) {
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

}
