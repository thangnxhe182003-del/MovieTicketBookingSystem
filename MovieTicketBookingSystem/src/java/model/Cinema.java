package model;

import java.io.Serializable;

public class Cinema implements Serializable {
    private static final long serialVersionUID = 1L;

    private int maRap;
    private String tenRap;
    private String diaChi;
    private String khuVuc;

    // Constructors
    public Cinema() {
    }

    public Cinema(int maRap, String tenRap, String diaChi, String khuVuc) {
        this.maRap = maRap;
        this.tenRap = tenRap;
        this.diaChi = diaChi;
        this.khuVuc = khuVuc;
    }

    // Getters and Setters
    public int getMaRap() {
        return maRap;
    }

    public void setMaRap(int maRap) {
        this.maRap = maRap;
    }

    public String getTenRap() {
        return tenRap;
    }

    public void setTenRap(String tenRap) {
        this.tenRap = tenRap;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getKhuVuc() {
        return khuVuc;
    }

    public void setKhuVuc(String khuVuc) {
        this.khuVuc = khuVuc;
    }

    @Override
    public String toString() {
        return "Cinema{" +
                "maRap=" + maRap +
                ", tenRap='" + tenRap + '\'' +
                ", diaChi='" + diaChi + '\'' +
                '}';
    }
}
