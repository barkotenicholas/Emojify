package com.barkote.kiosk.emojify.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Emoji {


    @SerializedName("emo")
    @Expose
    private String emo;

    public Emoji(String emo) {
        this.emo = emo;
    }

    public String getEmo() {
        return emo;
    }

    public void setEmo(String emo) {
        this.emo = emo;
    }
}


