// Đầu tiên, tạo model cho Staff (tương tự Customer)
package model;

import java.sql.Date;

public class Staff {
    private int maNhanVien;
    private int maRap;
    private String tenDangNhap;
    private String matKhau;
    private String hoTen;
    private String chucVu;
    private String soDienThoai;
    private String email;
    private String khuVucQuanLy;
    private Date ngayTao;
    private Date ngayCapNhat;
    private String avatar;

    public Staff() {}

    // Constructor đầy đủ (thêm các getter/setter tương ứng)
    public Staff(int maNhanVien, int maRap, String tenDangNhap, String matKhau, String hoTen, String chucVu,
                 String soDienThoai, String email, String khuVucQuanLy, Date ngayTao, Date ngayCapNhat, String avatar) {
        this.maNhanVien = maNhanVien;
        this.maRap = maRap;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.chucVu = chucVu;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.khuVucQuanLy = khuVucQuanLy;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
        this.avatar = avatar;
    }

    // Getter và Setter
    public int getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(int maNhanVien) { this.maNhanVien = maNhanVien; }
    public int getMaRap() { return maRap; }
    public void setMaRap(int maRap) { this.maRap = maRap; }
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getKhuVucQuanLy() { return khuVucQuanLy; }
    public void setKhuVucQuanLy(String khuVucQuanLy) { this.khuVucQuanLy = khuVucQuanLy; }
    public Date getNgayTao() { return ngayTao; }
    public void setNgayTao(Date ngayTao) { this.ngayTao = ngayTao; }
    public Date getNgayCapNhat() { return ngayCapNhat; }
    public void setNgayCapNhat(Date ngayCapNhat) { this.ngayCapNhat = ngayCapNhat; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    @Override
    public String toString() {
        return "Staff{" + "maNhanVien=" + maNhanVien + ", hoTen=" + hoTen + ", chucVu=" + chucVu + '}';
    }
}