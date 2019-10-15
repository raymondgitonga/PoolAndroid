package com.tosh.poolandroid.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tosh.poolandroid.Model.LoginResponse
import com.tosh.poolandroid.Remote.RetrofitClient
import com.tosh.poolloginrebuild.database.UserEntity
import com.tosh.poolloginrebuild.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {
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

    fun insert(userEntity: UserEntity) {
        repository.insert(userEntity)
    }

    fun getUserDetails(): LiveData<List<UserEntity>> {
        return repository.getUserDetails()
    }
}