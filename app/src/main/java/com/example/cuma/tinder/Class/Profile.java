package com.example.cuma.tinder.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("question")
    @Expose
    public String question;

    @SerializedName("answer")
    @Expose
    public String answer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Profile(String answer) {  //Sadece cevap gitsin
        this.answer = answer;
    }
}
