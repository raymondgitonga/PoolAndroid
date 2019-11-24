package com.tosh.poolandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tosh.poolandroid.model.LoginResponse
import com.tosh.poolandroid.model.remote.RetrofitClient
import com.tosh.poolandroid.model.Category
import com.tosh.poolandroid.model.RegisterResponse
import com.tosh.poolandroid.model.Vendor
import com.tosh.poolandroid.model.database.UserEntity
import com.tosh.poolandroid.model.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel(application: Application) : AndroidViewModel(application) {


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

    fun loadCategories(id:Int): MutableLiveData<List<Category>>? {
       var categoryList: MutableLiveData<List<Category>>? = MutableLiveData<List<Category>>()

        RetrofitClient.makeRetrofitApi2().getCategoryProducts(id)
                .enqueue(object : Callback<List<Category>> {
                    override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                        categoryList?.value = response.body()
                    }

                    override fun onFailure(call: Call<List<Category>>, t: Throwable) {

                    }
                })

        return categoryList
    }

    fun loadVendors():MutableLiveData<List<Vendor>>?{
        var vendorList: MutableLiveData<List<Vendor>>? = MutableLiveData()

        RetrofitClient.makeRetrofitApi2().getVendor()
                .enqueue(object : Callback<List<Vendor>> {
                    override fun onResponse(call: Call<List<Vendor>>, response: Response<List<Vendor>>) {
                        vendorList?.value = response.body()
                    }

                    override fun onFailure(call: Call<List<Vendor>>, t: Throwable) {

                    }
                })

        return vendorList
    }

    fun insert(userEntity: UserEntity) {
        repository.insert(userEntity)
    }

    fun getUserDetails(): LiveData<List<UserEntity>> {
        return repository.getUserDetails()
    }


}