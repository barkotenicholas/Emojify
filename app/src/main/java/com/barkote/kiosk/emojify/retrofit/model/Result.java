package com.barkote.kiosk.emojify.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("ans")
    @Expose
    private String ans;
    private String  a;

    public Result(String ans, String a) {
        this.ans = ans;
        this.a = a;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }
}
