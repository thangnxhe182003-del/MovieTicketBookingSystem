package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Rating implements Serializable {
    private static final long serialVersionUID = 1L;

    private int maDanhGia;
    private int maKH;
    private int maPhim;
    private int rating; // 1..10
    private String comment;
    private LocalDateTime dateDanhGia;

    public Rating() {
    }

    public int getMaDanhGia() {
        return maDanhGia;
    }

    public void setMaDanhGia(int maDanhGia) {
        this.maDanhGia = maDanhGia;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public int getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(int maPhim) {
        this.maPhim = maPhim;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDateDanhGia() {
        return dateDanhGia;
    }

    public void setDateDanhGia(LocalDateTime dateDanhGia) {
        this.dateDanhGia = dateDanhGia;
    }
}


