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

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
//import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    Button btnLogout;
    Context context;
    SharedPreferences preferences;
    TextView txtName;
    private NodeAuthService api;
    String email = "gitongaraymondd@gmail.com";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnLogout = findViewById(R.id.btn_logout);
        txtName = (TextView) findViewById(R.id.txt_name);

        Retrofit retrofit = AuthRetrofitClient.getUser();
        api = retrofit.create(NodeAuthService.class);

        Call<List<User>> users = api.getUser(email);

        users.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users =  response.body();
                if(users!=null && users.size()>0)
                {
                    txtName.setText(response.body().get(0).getUserPhone());
                }
                else
                {
                    txtName.setText("No User found");
                }
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
