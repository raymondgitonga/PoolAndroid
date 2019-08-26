package com.tosh.poolandroid.Retrofit;

import com.google.gson.JsonArray;
import com.tosh.poolandroid.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.QueryName;

public interface NodeAuthService {
    @POST("users/register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("name") String name,
                                    @Field("email") String email,
                                    @Field("password") String password,
                                    @Field("password2") String password2);

    @POST("users/login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                    @Field("password") String password);

    @PUT("users/phone")
    @FormUrlEncoded
    Observable<String>sendSms( @Field("name") String name, @Field("phone") String phone,@Field("email") String email );


    @GET("users/{email}")
    Call<List<User>> getUser(@Path("email") String email);





}
