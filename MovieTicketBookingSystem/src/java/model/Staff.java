package model;

import java.time.LocalDateTime;

public class Staff {
    
    private Integer maNhanVien;
    private Integer maRap;
    private String tenDangNhap;
    private String matKhau;
    private String hoTen;
    private String chucVu; // Staff, Manager, Admin
    private String soDienThoai;
    private String email;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
    private String trangThai; // Active, Inactive, Suspended

    public Staff() {
    }

    // Constructor đầy đủ
    public Staff(Integer maNhanVien, Integer maRap, String tenDangNhap, String matKhau, String hoTen, 
            String chucVu, String soDienThoai, String email, LocalDateTime ngayTao, 
            LocalDateTime ngayCapNhat, String trangThai) {
        this.maNhanVien = maNhanVien;
        this.maRap = maRap;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.chucVu = chucVu;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
        this.trangThai = trangThai;
    }

    // Constructor cho tạo mới
    public Staff(Integer maRap, String tenDangNhap, String matKhau, String hoTen, 
            String chucVu, String soDienThoai, String email) {
        this.maRap = maRap;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.chucVu = chucVu;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.trangThai = "Active";
    }

    // Getters and Setters
    public Integer getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(Integer maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public Integer getMaRap() {
        return maRap;
    }

    public void setMaRap(Integer maRap) {
        this.maRap = maRap;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    // Helper methods
    public Integer getMaNV() {
        return maNhanVien;
    }

    public void setMaNV(Integer maNV) {
        this.maNhanVien = maNV;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "maNhanVien=" + maNhanVien +
                ", maRap=" + maRap +
                ", tenDangNhap='" + tenDangNhap + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", chucVu='" + chucVu + '\'' +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}