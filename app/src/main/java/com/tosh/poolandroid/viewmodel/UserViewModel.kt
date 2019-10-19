package com.tosh.poolandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tosh.poolandroid.Remote.RetrofitApi
import com.tosh.poolandroid.model.LoginResponse
import com.tosh.poolandroid.Remote.RetrofitClient
import com.tosh.poolandroid.model.RegisterResponse
import com.tosh.poolandroid.model.Vendor
import com.tosh.poolloginrebuild.database.UserEntity
import com.tosh.poolloginrebuild.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(application: Application) : AndroidViewModel(application) {
     private val repository: UserRepository = UserRepository(application)

    fun userLogin(email: String, password: String): LiveData<String> {
        val loginResponse = MutableLiveData<String>()

        RetrofitClient.makeRetrofitApi().userLogin(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loginResponse.value = t.message
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        loginResponse.value = response.body()?.message.toString()

                        if (response.body()?.message == "successful") {
                            for (user in response.body()?.user!!) {
                                val newUser = UserEntity(
                                    name = user.name,
                                    email = user.email,
                                    phone = user.phone
                                )

                                insert(newUser)
                            }
                        }
                    } else {
                        loginResponse.value = response.body()?.message.toString()

                    }
                }

            })
        return loginResponse
    }

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

    fun insert(userEntity: UserEntity) {
        repository.insert(userEntity)
    }

    fun getUserDetails(): LiveData<List<UserEntity>> {
        return repository.getUserDetails()
    }
}