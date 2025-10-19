package model;

import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private int maPhong;
    private int maRap;
    private String tenPhong;
    private int soLuongGhe;

    // Constructors
    public Room() {
    }

    public Room(int maPhong, int maRap, String tenPhong, int soLuongGhe) {
        this.maPhong = maPhong;
        this.maRap = maRap;
        this.tenPhong = tenPhong;
        this.soLuongGhe = soLuongGhe;
    }

    // Getters and Setters
    public int getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(int maPhong) {
        this.maPhong = maPhong;
    }

    public int getMaRap() {
        return maRap;
    }

    public void setMaRap(int maRap) {
        this.maRap = maRap;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public int getSoLuongGhe() {
        return soLuongGhe;
    }

    public void setSoLuongGhe(int soLuongGhe) {
        this.soLuongGhe = soLuongGhe;
    }

    @Override
    public String toString() {
        return "Room{" +
                "maPhong=" + maPhong +
                ", tenPhong='" + tenPhong + '\'' +
                ", soLuongGhe=" + soLuongGhe +
                '}';
    }
}
