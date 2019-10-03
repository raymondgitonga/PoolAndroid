package com.tosh.poolandroid.ViewModel;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.tosh.poolandroid.Remote.AuthRetrofitClient;
import com.tosh.poolandroid.Remote.NodeAuthService;
import com.tosh.poolandroid.Repository.UserRepository;
import com.tosh.poolandroid.RoomDb.UserRoom;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends AndroidViewModel {
    private static MutableLiveData<String> loginResult = new MutableLiveData<>();

    private NodeAuthService api;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private UserRepository repository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        api = AuthRetrofitClient.getInstance().create(NodeAuthService.class);
        repository = new UserRepository(application);
    }

    public void insert(UserRoom user){
        repository.insert(user);
    }

    public void delete(UserRoom user){
        repository.delete(user);
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
