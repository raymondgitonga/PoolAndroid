package com.tosh.poolandroid.ViewModel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tosh.poolandroid.Model.User;
import com.tosh.poolandroid.Model.Vendor;
import com.tosh.poolandroid.Retrofit.NodeAuthService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserViewModel extends ViewModel {
    private static Retrofit instance;
    private static final String  BASE_URL = "http://10.0.2.2:7000/";
    private String email;
    public String userEmail;
    public String userName;

    private MutableLiveData<List<User>> userDetails;

    public LiveData<List<User>> getUser(){
        if (userDetails == null){
            userDetails = new MutableLiveData<List<User>>();

            loadUserDetails();
        }

        return userDetails;
    }

    private void loadUserDetails() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NodeAuthService api = retrofit.create(NodeAuthService.class);

        Call<List<User>> call = api.getUser(email);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    userEmail = response.body().get(0).getUserEmail();
                    userName = response.body().get(0).getUserName();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }
}
