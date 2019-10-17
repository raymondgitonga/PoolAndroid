package com.tosh.poolandroid.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tosh.poolandroid.model.Vendor;
import com.tosh.poolandroid.Remote.RetrofitApi;
import com.tosh.poolandroid.Remote.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VendorViewModel extends AndroidViewModel {


    private RetrofitApi api;

    private MutableLiveData<List<Vendor>> vendorList;

    public VendorViewModel(@NonNull Application application) {
        super(application);
        api = RetrofitClient.INSTANCE.makeRetrofitApi();
    }

    public LiveData<List<Vendor>> getVendor(){
        if (vendorList == null){
            vendorList = new MutableLiveData<List<Vendor>>();

            loadVendors();
        }

        return vendorList;
    }

    private void loadVendors() {
        Call<List<Vendor>> call = api.getVendor();

        call.enqueue(new Callback<List<Vendor>>() {
            @Override
            public void onResponse(Call<List<Vendor>> call, Response<List<Vendor>> response) {
                vendorList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Vendor>> call, Throwable t) {

            }
        });
    }


}
