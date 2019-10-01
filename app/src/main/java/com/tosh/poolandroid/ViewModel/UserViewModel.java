package com.tosh.poolandroid.ViewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tosh.poolandroid.Model.User;
import com.tosh.poolandroid.Model.Vendor;
import com.tosh.poolandroid.Remote.AuthRetrofitClient;
import com.tosh.poolandroid.Remote.NodeAuthService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends AndroidViewModel {

    private NodeAuthService api;
    private SharedPreferences pref;
    private static MutableLiveData<String> userName = new MutableLiveData<>();



    public UserViewModel(@NonNull Application application) {
        super(application);
        api = AuthRetrofitClient.getInstance().create(NodeAuthService.class);
        pref = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
    }

    public void loadUser(){
        Call<List<User>> call;
        call = api.getUser(pref.getString("email", null));
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                userName.postValue(response.body().get(0).getUserName());

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("USER",t.getMessage());
            }
        });

    }

   public MutableLiveData<String>getUserName(){
        return userName;
   }
}
