package com.tosh.poolandroid.Remote

import com.google.gson.GsonBuilder
import com.tosh.poolandroid.model.remote.UrlConstant
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient





object RetrofitClient {

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    var httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .callTimeout(2L, java.util.concurrent.TimeUnit.MINUTES)
            .connectTimeout(2L, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(3L, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(3L, java.util.concurrent.TimeUnit.SECONDS)

    fun makeRetrofitApi(): RetrofitApi{
        val retrofit = Retrofit.Builder()
            .baseUrl(UrlConstant.AUTH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpInterceptor())
                .client(httpClient.build())
            .build()
            .create(RetrofitApi::class.java)

        return retrofit
    }


    fun makeRetrofitApi2(): RetrofitApi {
        val retrofit2 = Retrofit.Builder()
                .baseUrl(UrlConstant.VENDOR_PRODUCT_URL)
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