package model;

import java.io.Serializable;

public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    private int maGhe;
    private int maPhong;
    private String hangGhe;  // A, B, C, ... K
    private int soGhe;       // 1, 2, 3, ... 12 (as int)
    private String loaiGhe;  // Thuong, VIP, Couple
    private String ghiChu;
    private String trangThai; // Có sẵn, Đã đặt, Bảo trì

    // Constructors
    public Seat() {
    }

    public Seat(int maGhe, int maPhong, String hangGhe, int soGhe,
            String loaiGhe, String trangThai) {
        this.maGhe = maGhe;
        this.maPhong = maPhong;
        this.hangGhe = hangGhe;
        this.soGhe = soGhe;
        this.loaiGhe = loaiGhe;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getMaGhe() {
        return maGhe;
    }

    public void setMaGhe(int maGhe) {
        this.maGhe = maGhe;
    }

    public int getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(int maPhong) {
        this.maPhong = maPhong;
    }

    public String getHangGhe() {
        return hangGhe;
    }

    public void setHangGhe(String hangGhe) {
        this.hangGhe = hangGhe;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }

    public String getLoaiGhe() {
        return loaiGhe;
    }

    public void setLoaiGhe(String loaiGhe) {
        this.loaiGhe = loaiGhe;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getSeatName() {
        return hangGhe + soGhe;
    }

    public String getFullSeatCode() {
        return hangGhe + String.format("%02d", soGhe);
    }

    @Override
    public String toString() {
        return "Seat{"
                + "maGhe=" + maGhe
                + ", hangGhe='" + hangGhe + '\''
                + ", soGhe=" + soGhe
                + ", trangThai='" + trangThai + '\''
                + '}';
    }
}
