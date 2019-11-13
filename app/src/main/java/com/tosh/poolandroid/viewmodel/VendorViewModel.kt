package com.tosh.poolandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tosh.poolandroid.Remote.RetrofitClient
import com.tosh.poolandroid.model.Vendor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendorViewModel(application: Application) : AndroidViewModel(application) {

    private var vendorList: MutableLiveData<List<Vendor>>? = null

    val vendor: LiveData<List<Vendor>>
        get() {
            if (vendorList == null) {
                vendorList = MutableLiveData()

                loadVendors()
            }

            return vendorList as MutableLiveData<List<Vendor>>
        }

    private fun loadVendors() {

        RetrofitClient.makeRetrofitApi2().getVendor()
                .enqueue(object : Callback<List<Vendor>> {
            override fun onResponse(call: Call<List<Vendor>>, response: Response<List<Vendor>>) {
                vendorList!!.value = response.body()
            }

            override fun onFailure(call: Call<List<Vendor>>, t: Throwable) {

            }
        })
    }

}
