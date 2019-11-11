package com.tosh.poolandroid.Remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:7000/"

    private const val BASE_URL2 = "http://10.0.2.2:1000/"

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    fun makeRetrofitApi(): RetrofitApi{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpInterceptor())
            .build()
            .create(RetrofitApi::class.java)

        return retrofit
    }


    fun makeRetrofitApi2(): RetrofitApi {
        val retrofit2 = Retrofit.Builder()
                .baseUrl(BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpInterceptor())
                .build()
                .create(RetrofitApi::class.java)

        return retrofit2

    }

    fun httpInterceptor(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }
}