package com.tosh.poolandroid.model.network

import com.tosh.poolandroid.model.*
import io.reactivex.Single
import retrofit2.http.*

interface RetrofitApi {
    @FormUrlEncoded
    @POST("user/login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Single<LoginResponse>

    @FormUrlEncoded
    @POST("user/register")
    fun userRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String
    ): Single<RegisterResponse>

    @FormUrlEncoded
    @POST("user/phone")
    fun addUserPhone(
        @Field("name") name: String,
        @Field("phone") phone:String,
        @Field("email") email: String
    ): Single<RegisterResponse>

    @GET("api/v1/vendor/all")
    fun getVendor(): Single<List<Vendor>>

    @GET("/api/v1/category/vendor/{vendor_id}")
    fun getCategoryProducts(@Path("vendor_id") vendorId:Int): Single<List<Category>>

    @GET("/api/v1/extra/product/{productId}")
    fun getProductExtras(@Path("productId") productId:Int): Single<List<Extra>>

    @POST("api/v1/billing/stk-push")
    fun makeMpesaRequest(@Body request: MpesaRequest): Single<MpesaResponse>

    @GET("api/v1/billing/user-response")
    fun mpesaRequestStatus(): Single<MpesaResponse>
}