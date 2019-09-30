package com.tosh.poolandroid.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.common.api.Api;
import com.tosh.poolandroid.Model.Vendor;
import com.tosh.poolandroid.Retrofit.NodeAuthService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class VendorViewModel extends ViewModel {

    private static Retrofit instance;
    private static final String  BASE_URL = "http://10.0.2.2:1000/";

    private MutableLiveData<List<Vendor>> vendorList;

    public LiveData<List<Vendor>> getVendor(){
        if (vendorList == null){
            vendorList = new MutableLiveData<List<Vendor>>();

            loadVendors();
        }

        return vendorList;
    }

    private void loadVendors() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NodeAuthService api = retrofit.create(NodeAuthService.class);

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
