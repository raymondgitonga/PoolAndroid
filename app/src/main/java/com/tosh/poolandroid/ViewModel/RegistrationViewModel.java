package com.tosh.poolandroid.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.tosh.poolandroid.Remote.AuthRetrofitClient;
import com.tosh.poolandroid.Remote.NodeAuthService;

import com.tosh.poolandroid.RoomDb.UserRoom;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RegistrationViewModel  extends AndroidViewModel {
    private static MutableLiveData<String> registerResult = new MutableLiveData<>();

    private NodeAuthService api;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        api = AuthRetrofitClient.getInstance().create(NodeAuthService.class);

    }


    public void registerUser(final String name, final String email, String password, String confirmPassword) {
        compositeDisposable.add(api.registerUser(name,email,password,confirmPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String res) throws Exception {

                        if(res.equals("successful")){
                            registerResult.postValue("success");
                        }else{
                           registerResult.postValue(res);
                        }
                    }
                }));
    }

    public MutableLiveData<String> getRegistrationResult(){
        return registerResult;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
