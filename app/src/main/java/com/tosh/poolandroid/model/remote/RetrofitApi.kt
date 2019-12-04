package com.tosh.poolandroid.model.remote

import com.tosh.poolandroid.model.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitApi {
    @FormUrlEncoded
    @POST("user/login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("user/register")
    fun userRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("user/phone")
    fun addUserPhone(
        @Field("name") name: String,
        @Field("phone") phone:String,
        @Field("email") email: String
    ): Call<RegisterResponse>

    @GET("api/v1/vendor/all")
    fun getVendor(): Call<List<Vendor>>

    @GET("/api/v1/category/vendor/{vendor_id}")
    fun getCategoryProducts(@Path("vendor_id") vendorId:Int): Call<List<Category>>

    @GET("/api/v1/extra/product/{productId}")
    fun getProductExtras(@Path("productId") productId:Int): Call<List<Extra>>
}