/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author thang
 */
public class Customer {

    private String maKH;
    private String tenDangNhap;
    private String matKhau;
    private String hoTen;
    private String ngaySinh;
    private String gioiTinh;
    private String soDienThoai;
    private String email;
    private String diemHienCo;
    private String chucVu;
    private boolean isActivated;
    private String otp;  // Thay activationToken bằng otp

    public Customer() {

    }

    // Constructor đầy đủ (bao gồm OTP)
    public Customer(String maKH, String tenDangNhap, String matKhau, String hoTen, String ngaySinh, String gioiTinh, String soDienThoai, String email, String diemHienCo, String chucVu, boolean isActivated, String otp) {
        this.maKH = maKH;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diemHienCo = diemHienCo;
        this.chucVu = chucVu;
        this.isActivated = isActivated;
        this.otp = otp;
    }

    // Constructor cũ (backward compatibility, không có OTP)
    public Customer(String maKH, String tenDangNhap, String matKhau, String hoTen, String ngaySinh, String gioiTinh, String soDienThoai, String email, String diemHienCo, String chucVu) {
        this(maKH, tenDangNhap, matKhau, hoTen, ngaySinh, gioiTinh, soDienThoai, email, diemHienCo, chucVu, false, null);
    }

    // Getters/Setters cũ (giữ nguyên)
    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
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

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
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

    public String getDiemHienCo() {
        return diemHienCo;
    }

    public void setDiemHienCo(String diemHienCo) {
        this.diemHienCo = diemHienCo;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    // Getters/Setters mới cho OTP
    public boolean getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
