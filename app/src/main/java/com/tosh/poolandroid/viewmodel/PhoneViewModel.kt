package com.tosh.poolandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tosh.poolandroid.model.RegisterResponse
import com.tosh.poolandroid.Remote.RetrofitClient
import com.tosh.poolloginrebuild.database.UserEntity
import com.tosh.poolloginrebuild.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhoneViewModel(application: Application) :AndroidViewModel(application) {
    private val repository: UserRepository = UserRepository(application)

    fun addUserPhone(name: String, email: String, phone:String): LiveData<String> {
        val registerResponse = MutableLiveData<String>()

        RetrofitClient.makeRetrofitApi().addUserPhone(name, phone, email)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    registerResponse.value = t.message
                }

                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        registerResponse.value = response.body()?.message.toString()

                        if (response.body()?.message == "successful") {

                                val newUser = UserEntity(
                                    name = response.body()!!.user.name,
                                    email = response.body()!!.user.email,
                                    phone = response.body()!!.user.phone
                                )

                                insert(newUser)
                        }
                    } else {
                        registerResponse.value = response.body()?.message.toString()

                    }
                }

            })
        return registerResponse
    }

    fun insert(userEntity: UserEntity) {
        repository.insert(userEntity)
    }
}