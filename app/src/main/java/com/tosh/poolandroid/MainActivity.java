package com.tosh.poolandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.tosh.poolandroid.LoginRegistration.LoginActivity;
import com.tosh.poolandroid.Retrofit.AuthRetrofitClient;
import com.tosh.poolandroid.Retrofit.NodeAuthService;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    private Button btnLogout;
    private Context context;
    private NodeAuthService api;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String email;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnLogout = findViewById(R.id.btn_logout);

        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


        email = pref.getString("email", "default");

        Retrofit retrofit = AuthRetrofitClient.getUser();
        api = retrofit.create(NodeAuthService.class);

        Call<List<User>> users = api.getUser(email);

        users.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users =  response.body();
//                  String name = response.body().get(0).getUserName();
//                  String phone = response.body().get(0).getUserPhone();
//                  editor = pref.edit();
//                  editor.putString("name", name);
//                  editor.putString("phone", phone);
//                  editor.commit();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("USER",t.getMessage());
            }
        });



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


    }



    private void logout(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.edit().remove("email").commit();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
