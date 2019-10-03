package com.tosh.poolandroid.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import com.tosh.poolandroid.R;
import com.tosh.poolandroid.RoomDb.UserRoom;
import com.tosh.poolandroid.ViewModel.LoginViewModel;

import static android.util.Patterns.EMAIL_ADDRESS;

public class LoginActivity extends AppCompatActivity{

    private MaterialButton registerBtn;
    private MaterialButton loginBtn;
    private MaterialButton forgotBtn;

    private TextInputEditText inputPassword, inputEmail;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String email;
    private String password;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                addToSharedPreferences(email);
                loginUser(email, password);
            }
        });
        insatantiateLoginViewModel();
    }

    private void insatantiateLoginViewModel(){
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        loginViewModel.getLoginResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("success")){
                    addToSharedPreferences(email);
                    UserRoom user = new UserRoom(null, email);
                    loginViewModel.insert(user);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "wrong email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser(final String email, String password) {

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

        loginViewModel.loginUser(email,password);

    }
    public void addToSharedPreferences(String email){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        editor.putString("email", email);
        editor.apply();

    }


}
