package com.example.cuma.tinder.Class;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class Etkinler {
   String nickname;

    public Etkinler(String nickname) {
        this.nickname = nickname;
    }
    public Etkinler(){

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
