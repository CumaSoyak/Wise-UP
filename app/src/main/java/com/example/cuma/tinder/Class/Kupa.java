package com.example.cuma.tinder.Class;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Kupa {
    String nickname;
    double siralama;
    int kupa_image;

    public Kupa(String nickname, double siralama, int kupa_image) {
        this.nickname =  nickname;
        this.siralama =  siralama;
        this.kupa_image = kupa_image;
    }

    public Kupa() {

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public double getSiralama() {
        return siralama;
    }

    public void setSiralama(double siralama) {
        this.siralama = siralama;
    }

    public int getKupa_image() {
        return kupa_image;
    }

    public void setKupa_image(int kupa_image) {
        this.kupa_image = kupa_image;
    }
}
