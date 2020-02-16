package com.tosh.poolandroid.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tosh.poolandroid.model.*
import com.tosh.poolandroid.model.database.CartItemEntity
import com.tosh.poolandroid.model.remote.RetrofitClient
import com.tosh.poolandroid.model.database.UserEntity
import com.tosh.poolandroid.model.repository.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : BaseViewModel(application) {


    private val repository: MainRepository = MainRepository(application)
    private val disposable = CompositeDisposable()
    private val client = RetrofitClient()


    fun userLogin(email: String, password: String): LiveData<String> {
        val loginResponse = MutableLiveData<String>()

        disposable.add(
            client.userLogin(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        loginResponse.value = it.message
                        if (it.message == "successful") {
                            for (user in it.user) {
                                val newUser = UserEntity(
                                    name = user.name,
                                    email = user.email,
                                    phone = user.phone
                                )

                                insert(newUser)
                            }
                        } else {
                            loginResponse.value = it.message
                        }

                    },
                    {
                        // add error handling
                    }
                )
        )

        return loginResponse
    }

    fun addUserPhone(name: String, email: String, phone: String): LiveData<String> {
        val registerResponse = MutableLiveData<String>()

        disposable.add(
            client.addUserPhone(name, phone, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {

                        if (it.isSuccessful) {
                            registerResponse.value = it.message
                            if (it.message == "successful") {

                                val newUser = UserEntity(
                                    name = it.user.name,
                                    email = it.user.email,
                                    phone = it.user.phone
                                )

                                insert(newUser)
                            } else {
                                registerResponse.value = it.message
                            }
                        }
                    },
                    {
                        // add error message
                    }
                )
        )
        return registerResponse
    }

    fun userRegister(name: String, email: String, password: String, confirmPassword: String): LiveData<String> {
        val registerResponse = MutableLiveData<String>()

        disposable.add(
            client.userRegister(name, email, password, confirmPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (it.isSuccessful) {
                            registerResponse.value = it.message
                        } else {
                            registerResponse.value = it.message
                        }
                    },
                    {
                        // returm error
                    }
                )
        )
        return registerResponse
    }

    fun loadCategories(id: Int): MutableLiveData<List<Category>>? {
        var categoryList: MutableLiveData<List<Category>>? = MutableLiveData()

        disposable.add(
            client.getCategoryProducts(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        categoryList?.value = it
                    },
                    {
                        // add error handling
                    }
                )
        )
        return categoryList
    }

    fun loadProductExtras(id: Int): MutableLiveData<List<Extra>>? {
        var productExtrasList: MutableLiveData<List<Extra>>? = MutableLiveData()

        disposable.add(
            client.getVendorExtras(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        productExtrasList?.value = it
                    },
                    {
                        // add error handling
                    }
                )
        )

        return productExtrasList
    }

    fun loadVendors(): MutableLiveData<List<Vendor>>? {
        val vendorList: MutableLiveData<List<Vendor>>? = MutableLiveData()

        disposable.add(
            client.getVendor()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        vendorList?.value = it
                    },
                    {
                        // add error handling
                    }
                )
        )
        return vendorList
    }

    fun insert(userEntity: UserEntity) {
        repository.insert(userEntity)
    }

    fun insert(cartItemEntity: CartItemEntity) {
        repository.insert(cartItemEntity)
    }

    fun getCartTotal(): MutableLiveData<Double>{
       var cartTotal: MutableLiveData<Double>? = MutableLiveData()

        launch {
            cartTotal!!.value = repository.getCartTotal()
        }
        return cartTotal!!
    }

    fun deleteCartItem(id: Int){
        launch {
            repository.deleteCartItem(id)
        }
    }

    fun getUserDetails(): LiveData<List<UserEntity>> {
        return repository.getUserDetails()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}