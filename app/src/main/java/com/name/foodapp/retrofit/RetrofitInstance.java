package com.name.foodapp.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.17.239/foodapp/";

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            // Cấu hình Gson với chế độ Lenient
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            // Thêm Gson vào Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
