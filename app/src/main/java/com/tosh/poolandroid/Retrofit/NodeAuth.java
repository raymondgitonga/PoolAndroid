package com.tosh.poolandroid.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NodeAuth {
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


}
