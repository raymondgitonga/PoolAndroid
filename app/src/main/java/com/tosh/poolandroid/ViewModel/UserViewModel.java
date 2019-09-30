package com.tosh.poolandroid.ViewModel;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.tosh.poolandroid.Model.User;
import com.tosh.poolandroid.Remote.AuthRetrofitClient;
import com.tosh.poolandroid.Remote.NodeAuthService;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends AndroidViewModel {

    private NodeAuthService api;
    private SharedPreferences pref;

    private static MutableLiveData<List<User>> userDetails = new MutableLiveData<>();


    private Call<List<User>> call;

    public UserViewModel(@NonNull Application application) {
        super(application);
        api = AuthRetrofitClient.getInstance().create(NodeAuthService.class);
    }
    private String email = pref.getString("email", "");

    public void loadUser(){
        call = api.getUser(email);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users =  response.body();

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("USER",t.getMessage());
            }
        });

    }

    public MutableLiveData<List<User>> getUserDetails(){
        return userDetails;
    }
}
