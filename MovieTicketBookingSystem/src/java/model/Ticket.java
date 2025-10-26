package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Model class cho Ticket
 */
public class Ticket {
    private int maVe;
    private int maSuatChieu;
    private int maGhe;
    private String loaiGhe;
    private BigDecimal giaVe;
    private String trangThai; // Có sẵn, Đã đặt, Hold, Hủy
    private LocalDateTime thoiGianTao;
    private LocalDateTime thoiGianCapNhat;
    private String ghiChu;
    
    // Constructors
    public Ticket() {}
    
    public Ticket(int maSuatChieu, int maGhe, String loaiGhe, BigDecimal giaVe, String trangThai) {
        this.maSuatChieu = maSuatChieu;
        this.maGhe = maGhe;
        this.loaiGhe = loaiGhe;
        this.giaVe = giaVe;
        this.trangThai = trangThai;
        this.thoiGianTao = LocalDateTime.now();
        this.thoiGianCapNhat = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getMaVe() {
        return maVe;
    }
    
    public void setMaVe(int maVe) {
        this.maVe = maVe;
    }
    
    public int getMaSuatChieu() {
        return maSuatChieu;
    }
    
    public void setMaSuatChieu(int maSuatChieu) {
        this.maSuatChieu = maSuatChieu;
    }
    
    public int getMaGhe() {
        return maGhe;
    }
    
    public void setMaGhe(int maGhe) {
        this.maGhe = maGhe;
    }
    
    public String getLoaiGhe() {
        return loaiGhe;
    }
    
    public void setLoaiGhe(String loaiGhe) {
        this.loaiGhe = loaiGhe;
    }
    
    public BigDecimal getGiaVe() {
        return giaVe;
    }
    
    public void setGiaVe(BigDecimal giaVe) {
        this.giaVe = giaVe;
    }
    
    public String getTrangThai() {
        return trangThai;
    }
    
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    
    public LocalDateTime getThoiGianTao() {
        return thoiGianTao;
    }
    
    public void setThoiGianTao(LocalDateTime thoiGianTao) {
        this.thoiGianTao = thoiGianTao;
    }
    
    public LocalDateTime getThoiGianCapNhat() {
        return thoiGianCapNhat;
    }
    
    public void setThoiGianCapNhat(LocalDateTime thoiGianCapNhat) {
        this.thoiGianCapNhat = thoiGianCapNhat;
    }
    
    public String getGhiChu() {
        return ghiChu;
    }
    
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}