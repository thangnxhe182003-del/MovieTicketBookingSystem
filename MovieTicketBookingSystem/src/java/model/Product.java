package model;

import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private int maSP;
    private String tenSP;
    private double donGia;
    private String thumbnailUrl;
    private String trangThai;

    // Constructors
    public Product() {
    }

    public Product(int maSP, String tenSP, double donGia, String thumbnailUrl, String trangThai) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donGia = donGia;
        this.thumbnailUrl = thumbnailUrl;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "Product{" +
                "maSP=" + maSP +
                ", tenSP='" + tenSP + '\'' +
                ", donGia=" + donGia +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                '}';
    }
}
