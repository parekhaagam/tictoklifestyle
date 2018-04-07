package com.example.android.uidemo;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Honey on 23-Jul-16.
 */
public class RetrofitInstance {
    public static final String BASE_URL = "https://script.google.com/macros/s/";
    private static Retrofit retrofit = null;


    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
