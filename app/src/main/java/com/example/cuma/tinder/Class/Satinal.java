package com.example.cuma.tinder.Class;

import android.widget.ImageView;

public class Satinal {
    private String title,fiyat;
    private int image;

    public Satinal(String title, int image, String fiyat) {
        this.title = title;
        this.image = image;
        this.fiyat = fiyat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getFiyat() {
        return fiyat;
    }

    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }
}
