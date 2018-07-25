package com.example.cuma.tinder.Class;

import android.util.Log;

public class PuanHesapla {
      int dogrucevapsayisi;

    public  int puanarti(){
        dogrucevapsayisi++;
        Log.i("Puan Hesapla",": : :"+dogrucevapsayisi);
        return dogrucevapsayisi;
    }


    public PuanHesapla(int dogrucevapsayisi) {
        this.dogrucevapsayisi = dogrucevapsayisi;
    }
}
