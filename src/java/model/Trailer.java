package model;

import java.io.Serializable;

public class Trailer implements Serializable {
    private static final long serialVersionUID = 1L;

    private int maTrailer;
    private int maPhim;
    private String linkTrailer;
    private String moTa;

    // Constructors
    public Trailer() {
    }

    public Trailer(int maTrailer, int maPhim, String linkTrailer, String moTa) {
        this.maTrailer = maTrailer;
        this.maPhim = maPhim;
        this.linkTrailer = linkTrailer;
        this.moTa = moTa;
    }

    // Getters and Setters
    public int getMaTrailer() {
        return maTrailer;
    }

    public void setMaTrailer(int maTrailer) {
        this.maTrailer = maTrailer;
    }

    public int getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(int maPhim) {
        this.maPhim = maPhim;
    }

    public String getLinkTrailer() {
        return linkTrailer;
    }

    public void setLinkTrailer(String linkTrailer) {
        this.linkTrailer = linkTrailer;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "maTrailer=" + maTrailer +
                ", maPhim=" + maPhim +
                ", linkTrailer='" + linkTrailer + '\'' +
                '}';
    }
}
