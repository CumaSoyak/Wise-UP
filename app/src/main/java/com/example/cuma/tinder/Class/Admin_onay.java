package com.example.cuma.tinder.Class;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class Admin_onay {
    String cevap;
    String soru;

    public Admin_onay(){

    }
    public Admin_onay(String cevap, String soru) {
        this.cevap = cevap;
        this.soru = soru;
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
