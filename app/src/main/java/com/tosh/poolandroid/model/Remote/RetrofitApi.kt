package com.tosh.poolandroid.Remote

import com.tosh.poolandroid.model.LoginResponse
import com.tosh.poolandroid.model.RegisterResponse
import com.tosh.poolandroid.model.Vendor
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
}