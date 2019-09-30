package com.tosh.poolandroid.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.tosh.poolandroid.Remote.AuthRetrofitClient;
import com.tosh.poolandroid.Remote.NodeAuthService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class PhoneViewModel extends AndroidViewModel {

    private static MutableLiveData<String> phoneStatus = new MutableLiveData<>();

    private NodeAuthService api;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public PhoneViewModel(@NonNull Application application) {
        super(application);
        api = AuthRetrofitClient.getInstance().create(NodeAuthService.class);
    }

    public void getPhone(String name, String phone, String email) {
        compositeDisposable.add(api.sendSms(name,phone, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String res) throws Exception {

                        if (res.equals("phone")){
                            phoneStatus.postValue("success");
                        }else {
                            phoneStatus.postValue(res);
                        }
                    }
                })
        );

    }

    public MutableLiveData<String> getPhoneStatus(){
        return phoneStatus;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        compositeDisposable.clear();
    }
}
