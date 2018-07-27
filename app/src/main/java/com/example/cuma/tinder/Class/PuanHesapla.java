package com.example.cuma.tinder.Class;

import android.util.Log;

public class PuanHesapla {
     private static int dogrucevapsayisi;

    public PuanHesapla(){
        dogrucevapsayisi++;
        Log.i("Sayısı",":"+dogrucevapsayisi);
    }
    public static int puanarti(){
        return dogrucevapsayisi;
    }

}
