package model;

import java.time.LocalDateTime;

public class Rating {
    private Integer maDanhGia;
    private Integer maKH;
    private Integer maPhim;
    private Integer rating;
    private String comment;
    private LocalDateTime dateDanhGia;

    // Constructors
    public Rating() {
    }

    public Rating(Integer maDanhGia, Integer maKH, Integer maPhim, Integer rating, 
                 String comment, LocalDateTime dateDanhGia) {
        this.maDanhGia = maDanhGia;
        this.maKH = maKH;
        this.maPhim = maPhim;
        this.rating = rating;
        this.comment = comment;
        this.dateDanhGia = dateDanhGia;
    }

    // Getters and Setters
    public Integer getMaDanhGia() {
        return maDanhGia;
    }

    public void setMaDanhGia(Integer maDanhGia) {
        this.maDanhGia = maDanhGia;
    }

    public Integer getMaKH() {
        return maKH;
    }

    public void setMaKH(Integer maKH) {
        this.maKH = maKH;
    }

    public Integer getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(Integer maPhim) {
        this.maPhim = maPhim;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
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