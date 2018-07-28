package com.example.cuma.tinder.Class;

public class Kupa {
    String kupa_ad,kupa_puan;
    int kupa_image;

    public Kupa(String kupa_ad, String kupa_puan, int kupa_image) {
        this.kupa_ad = kupa_ad;
        this.kupa_puan = kupa_puan;
        this.kupa_image = kupa_image;
    }

    public String getKupa_ad() {
        return kupa_ad;
    }

    public void setKupa_ad(String kupa_ad) {
        this.kupa_ad = kupa_ad;
    }

    public String getKupa_puan() {
        return kupa_puan;
    }

    public void setKupa_puan(String kupa_puan) {
        this.kupa_puan = kupa_puan;
    }

    public int getKupa_image() {
        return kupa_image;
    }

    public void setKupa_image(int kupa_image) {
        this.kupa_image = kupa_image;
    }

}
