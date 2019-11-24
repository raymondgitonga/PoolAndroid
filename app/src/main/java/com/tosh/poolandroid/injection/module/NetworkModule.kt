package com.tosh.poolandroid.injection.module

import com.tosh.poolandroid.model.remote.UrlConstant
import com.tosh.poolandroid.remote.RetrofitApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
object NetworkModule{
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitApi(retrofit: Retrofit): RetrofitApi{
        return retrofit.create(RetrofitApi::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(): Retrofit{
        return Retrofit.Builder()
                .baseUrl(UrlConstant.VENDOR_PRODUCT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }
}