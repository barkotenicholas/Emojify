package com.barkote.kiosk.emojify.retrofit.Interface;


import com.barkote.kiosk.emojify.retrofit.model.Emoji;
import com.barkote.kiosk.emojify.retrofit.model.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NetworkCalls {

    @Headers({"Content-Type: application/json"})

    @POST("/emojify")
    Call<Result> sendMessage(@Body Emoji emoji);


}
