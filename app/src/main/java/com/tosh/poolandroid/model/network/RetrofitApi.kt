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

    @POST("/mobile/stk")
    fun makeMpesaRequest(@Body request: MpesaRequest): Single<MpesaResponse>

    @GET("/mobile/result_code")
    fun getMpesaResult():Single<String>

    @PATCH("/user/updateDetails")
    fun updateUserDetails(@Body userUpdate: UserUpdate): Single<UpdateResponse>

    @PATCH("/user/updatePassword")
    fun updateUserPassword(@Body update: UpdatePassword): Single<String>

    @POST("/api/v1/cart/post-cart")
    fun postCart(@Body cart: Cart): Single<Long>

    @POST("/api/v1/cart-item/post-item")
    fun postCartItem(@Body cartItems: CartItems): Single<String>

    @GET("/api/v1/cart/user/{user_id}")
    fun getOrders(@Path("user_id") userId: Int): Single<List<Order>>

}