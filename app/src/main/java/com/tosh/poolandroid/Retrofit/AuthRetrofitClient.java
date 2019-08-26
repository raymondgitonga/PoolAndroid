package com.tosh.poolandroid.Retrofit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AuthRetrofitClient {
    private static Retrofit instance;
    private static final String  BASE_URL = "http://10.0.2.2:7000/";


    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static Retrofit getInstance(){
        if (instance == null)
            instance = new Retrofit.Builder()
                                    .baseUrl(BASE_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                    .build();
        return instance;
    }

    public static Retrofit getUser(){


        if (instance == null)
            instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        return instance;
    }
}
