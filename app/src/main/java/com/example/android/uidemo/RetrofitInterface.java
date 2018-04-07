package com.example.android.uidemo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Honey on 23-Jul-16.
 */
public interface RetrofitInterface {
    @GET("AKfycbzGvKKUIaqsMuCj7-A2YRhR-f7GZjl4kSxSN1YyLkS01_CfiyE/exec")
    Call<SheetContent> getData(@Query("id") String key,
                               @Query("sheet") String sheet);
}
