package com.tosh.poolandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tosh.poolandroid.model.RegisterResponse
import com.tosh.poolandroid.Remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationViewModel:ViewModel(){
    fun userRegister(name:String, email:String, password:String, confirmPassword:String):LiveData<String>{
        val registerResponse = MutableLiveData<String>()
        RetrofitClient.makeRetrofitApi().userRegister(name, email, password, confirmPassword)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    registerResponse.value = t.message
                }

                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful){
                        registerResponse.value = response.body()?.message.toString()
                    }else{
                        registerResponse.value = response.body()?.message.toString()
                    }
                }

            })
        return registerResponse
    }

}