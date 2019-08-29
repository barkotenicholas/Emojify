package com.barkote.kiosk.emojify.retrofit.instance;



import com.barkote.kiosk.emojify.retrofit.Interface.NetworkCalls;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Emo {

    private static final String BASE_URL = "http://chergeionline.com:8000";
    private static Retrofit retrofit = null;

    public static NetworkCalls getApiClient() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(NetworkCalls.class);
    }

}
