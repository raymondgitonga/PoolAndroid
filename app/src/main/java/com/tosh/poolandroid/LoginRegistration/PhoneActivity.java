package com.tosh.poolandroid.LoginRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.hbb20.CountryCodePicker;
import com.tosh.poolandroid.MainActivity;
import com.tosh.poolandroid.R;

public class PhoneActivity extends AppCompatActivity {

    MaterialButton proceedBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        proceedBtn = findViewById(R.id.proceed_btn);

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
