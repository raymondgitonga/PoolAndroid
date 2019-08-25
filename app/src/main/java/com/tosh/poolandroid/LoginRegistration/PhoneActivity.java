package com.tosh.poolandroid.LoginRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.hbb20.CountryCodePicker;
import com.tosh.poolandroid.MainActivity;
import com.tosh.poolandroid.R;
import com.tosh.poolandroid.Retrofit.AuthRetrofitClient;
import com.tosh.poolandroid.Retrofit.NodeAuth;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class PhoneActivity extends AppCompatActivity {

    MaterialButton proceedBtn;
    EditText inputPhone;

    String email;
    String name;

    SharedPreferences pref;



    private NodeAuth api;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


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
        setContentView(R.layout.activity_phone);

        pref= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        email=pref.getString("email", "default");
        name = pref.getString("name", "default");






        //init api
        Retrofit retrofit = AuthRetrofitClient.getInstance();
        api = retrofit.create(NodeAuth.class);


        //init views
        proceedBtn = findViewById(R.id.proceed_btn);
        inputPhone = findViewById(R.id.phone_input);

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhone(name, inputPhone.getText().toString(), email);
            }
        });


    }

    private void getPhone(String name, String phone, String email) {
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Enter phone", Toast.LENGTH_SHORT).show();
            return;
        }
        if(phone.length()<9){
            Toast.makeText(this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        compositeDisposable.add(api.sendSms(name,phone, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String res) throws Exception {

                        if (res .equals("phone")){
                            Intent intent = new Intent(PhoneActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(PhoneActivity.this, ""+res, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        );

    }
}
