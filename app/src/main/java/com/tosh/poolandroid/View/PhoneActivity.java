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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import com.tosh.poolandroid.R;
import com.tosh.poolandroid.Remote.AuthRetrofitClient;
import com.tosh.poolandroid.Remote.NodeAuthService;
import com.tosh.poolandroid.ViewModel.PhoneViewModel;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Retrofit;

public class PhoneActivity extends AppCompatActivity {

    private MaterialButton proceedBtn;
    private EditText inputPhone;

    private String email;
    private String name;

    private SharedPreferences pref;
    private PhoneViewModel phoneViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        pref= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        email=pref.getString("email", "default");
        name = pref.getString("name", "default");

        //init views
        proceedBtn = findViewById(R.id.proceed_btn);
        inputPhone = findViewById(R.id.phone_input);

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhone(name, inputPhone.getText().toString(), email);
            }
        });

        instatiatePhoneViewModel();


    }

    private void getPhone(String name, String phone, String email) {
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Enter phone", Toast.LENGTH_SHORT).show();
            return;
        }
        if(phone.length()!= 9){
            Toast.makeText(this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        phoneViewModel.getPhone(name,phone,email);
    }
    private void instatiatePhoneViewModel(){
        phoneViewModel = ViewModelProviders.of(this).get(PhoneViewModel.class);

        phoneViewModel.getPhoneStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("success")){
                    Intent intent = new Intent(PhoneActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                }else{
                    Toast.makeText(PhoneActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
