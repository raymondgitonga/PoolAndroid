package com.tosh.poolandroid.LoginRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.tosh.poolandroid.MainActivity;
import com.tosh.poolandroid.R;
import com.tosh.poolandroid.Retrofit.AuthRetrofitClient;
import com.tosh.poolandroid.Retrofit.NodeAuth;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static android.util.Patterns.EMAIL_ADDRESS;

public class LoginActivity extends AppCompatActivity {

    MaterialButton registerBtn;
    MaterialButton loginBtn;
    MaterialButton forgotBtn;

    TextInputEditText inputPassword, inputEmail;

    NodeAuth api;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //init api
        Retrofit retrofit = AuthRetrofitClient.getInstance();
        api = retrofit.create(NodeAuth.class);

        // views
        inputEmail =  findViewById(R.id.email_login);
        inputPassword = findViewById(R.id.password_login);
        loginBtn = findViewById(R.id.login_btn);
        forgotBtn = findViewById(R.id.btn_forgotPassword);
        registerBtn = findViewById(R.id.login_register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(inputEmail.getText().toString(), inputPassword.getText().toString());
            }
        });
    }

    private void loginUser(String email, String password) {

        if(TextUtils.isEmpty(email)){
            inputEmail.setError("Enter email");
            return;
        }
        if(!EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Enter valid email");
            return;
        }
        if(TextUtils.isEmpty(password)){
            inputPassword.setError("Enter password");
            return;
        }
        compositeDisposable.add(api.loginUser(email, password)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String res) throws Exception {

                System.out.println("YESSSSSSSSSSSSSSSSSSSSSSSSSSSS"+ res);

                if (res .equals("successful")){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, ""+res, Toast.LENGTH_SHORT).show();
                }
            }
        })
        );

    }

}
