package com.example.android.uidemo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Honey on 23-Jul-16.
 */
public interface RetrofitInterface {
    @GET("AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec")
    Call<SheetContent> getData(@Query("id") String key);
}
