package com.tosh.poolandroid.ViewModel;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tosh.poolandroid.LoginRegistration.LoginActivity;
import com.tosh.poolandroid.Model.Vendor;
import com.tosh.poolandroid.Retrofit.AuthRetrofitClient;
import com.tosh.poolandroid.Retrofit.NodeAuthService;
import com.tosh.poolandroid.View.MainActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LoginViewModel extends AndroidViewModel {
    private static Retrofit instance;
    private static final String  BASE_URL = "http://10.0.2.2:1000/";

    private static MutableLiveData<String> loginResult = new MutableLiveData<>();

    private NodeAuthService api;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        api = AuthRetrofitClient.getInstance().create(NodeAuthService.class);
    }


    public void loginUser( final String email, String password) {

        compositeDisposable.add(api.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String res) throws Exception {

                        if (res.equals("successful")){
                            loginResult.postValue("success");
                        }else {
                            loginResult.postValue("fail");
                        }
                    }
                })


        );

    }
    public MutableLiveData<String> getLoginResult(){
        return loginResult;
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        compositeDisposable.clear();
    }
}
