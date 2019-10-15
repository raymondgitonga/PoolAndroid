package com.tosh.poolandroid.Remote

import com.tosh.poolandroid.Model.LoginResponse
import com.tosh.poolandroid.Model.RegisterResponse
import com.tosh.poolandroid.Model.Vendor
import retrofit2.Call
import retrofit2.http.*

interface RetrofitApi {
    @FormUrlEncoded
    @POST("users/login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("users/register")
    fun userRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("users/phone")
    fun addUserPhone(
        @Field("name") name: String,
        @Field("phone") phone:String,
        @Field("email") email: String
    ): Call<RegisterResponse>

    @GET("vendors/vendor-list")
    abstract fun getVendor(): Call<List<Vendor>>


}