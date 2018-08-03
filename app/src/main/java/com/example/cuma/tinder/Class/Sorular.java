package com.example.cuma.tinder.Class;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Sorular {
    String cevap;
    String soru;

    public Sorular(String cevap, String soru) {
        this.cevap = cevap;
        this.soru = soru;
    }
    public Sorular(){

    }


    public String getCevap() {
        return cevap;
    }

    public void setCevap(String cevap) {
        this.cevap = cevap;
    }

    public String getSoru() {
        return soru;
    }

    public void setSoru(String soru) {
        this.soru = soru;
    }
}
