package com.tosh.poolandroid.model.network

import com.google.gson.GsonBuilder
import com.tosh.poolandroid.model.*
import com.tosh.poolandroid.util.Constants.AUTH_BASE_URL
import com.tosh.poolandroid.util.Constants.ORDER_API
import com.tosh.poolandroid.util.Constants.PAYMENT_API
import com.tosh.poolandroid.util.Constants.VENDOR_PRODUCT_URL
import io.reactivex.Single
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


class RetrofitClient {

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private var httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        .callTimeout(2L, java.util.concurrent.TimeUnit.MINUTES)
        .connectTimeout(2L, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(3L, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(3L, java.util.concurrent.TimeUnit.SECONDS)

    private fun httpInterceptor(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    private val userApi = Retrofit.Builder()
        .baseUrl(AUTH_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(httpInterceptor())
        .client(httpClient.build())
        .build()
        .create(RetrofitApi::class.java)

    private val vendorApi = Retrofit.Builder()
        .baseUrl(VENDOR_PRODUCT_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(httpInterceptor())
        .build()
        .create(RetrofitApi::class.java)

    private val paymentApi = Retrofit.Builder()
        .baseUrl(PAYMENT_API)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(httpInterceptor())
        .build()
        .create(RetrofitApi::class.java)

    private val ordersApi = Retrofit.Builder()
        .baseUrl(ORDER_API)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(httpInterceptor())
        .build()
        .create(RetrofitApi::class.java)


    fun userLogin(email: String, password: String): Single<LoginResponse>{
        return  userApi.userLogin(email, password)
    }

    fun addUserPhone(name: String, phone: String, email: String):Single<RegisterResponse>{
        return userApi.addUserPhone(name, phone, email)
    }

    fun userRegister(name: String, email: String, password: String, confirmPassword: String): Single<RegisterResponse> {
        return userApi.userRegister(name, email, password, confirmPassword)
    }

    fun getVendorExtras(productId: Int): Single<List<Extra>>{
        return vendorApi.getProductExtras(productId)
    }

    fun getVendor(): Single<List<Vendor>>{
        return vendorApi.getVendor()
    }

    fun getCategoryProducts(vendorId: Int): Single<List<Category>>{
        return vendorApi.getCategoryProducts(vendorId)
    }

    fun makeMpesaRequesst(request: MpesaRequest): Single<MpesaResponse>{
        return paymentApi.makeMpesaRequest(request)
    }

    fun getMpesaResult(): Single<String>{
        return paymentApi.getMpesaResult()
    }

    fun updateUserDetails(updateDetails: UserUpdate): Single<UpdateResponse>{
        return userApi.updateUserDetails(updateDetails)
    }

    fun updatePassword(updatePassword: UpdatePassword): Single<String>{
        return userApi.updateUserPassword(updatePassword)
    }

    fun postCart(cart: Cart): Single<Long>{
        return ordersApi.postCart(cart)
    }

    fun postCartItem(cartItems: CartItems): Single<String>{
        return ordersApi.postCartItem(cartItems)
    }

    fun getOrders(userId:Int): Single<List<Order>>{
        return ordersApi.getOrders(userId)
    }

}