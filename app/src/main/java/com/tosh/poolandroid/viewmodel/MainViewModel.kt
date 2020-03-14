package com.tosh.poolandroid.viewmodel

import android.app.Application
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tosh.poolandroid.model.*
import com.tosh.poolandroid.model.database.CartItemEntity
import com.tosh.poolandroid.model.database.UserEntity
import com.tosh.poolandroid.model.network.RetrofitClient
import com.tosh.poolandroid.model.repository.MainRepository
import com.tosh.poolandroid.util.addToSharedPreferences
import io.reactivex.Single
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
                                    id = user.id,
                                    name = user.name,
                                    email = user.email,
                                    phone = user.phone
                                )

                                insert(newUser)
                                addToSharedPreferences(
                                    getApplication(),
                                    newUser.email,
                                    newUser.phone
                                )
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
                                    id = it.user.id,
                                    name = it.user.name,
                                    email = it.user.email,
                                    phone = it.user.phone
                                )

                                insert(newUser)
                                addToSharedPreferences(
                                    getApplication(),
                                    newUser.email,
                                    newUser.phone
                                )
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

    fun userRegister(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): LiveData<String> {
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

    fun getCartTotal(): MutableLiveData<Double> {
        var cartTotal: MutableLiveData<Double>? = MutableLiveData()

        launch {
            cartTotal!!.value = repository.getCartTotal()
        }
        return cartTotal!!
    }

    fun deleteCartItem(id: Int) {
        launch {
            repository.deleteCartItem(id)
        }
    }

    fun getUserDetails(): LiveData<List<UserEntity>> {
        return repository.getUserDetails()
    }

    fun getCartItemCount(productId: Int): MutableLiveData<Int>? {
        var itemCount: MutableLiveData<Int>? = MutableLiveData()
        launch {
            itemCount!!.value = repository.getCartItemCount(productId)
        }

        return itemCount
    }

    fun deleteUser() {
        launch {
            repository.deleteUser()
        }
    }

    fun deleteCart() {
        launch {
            repository.deleteCart()
        }
    }

    fun makeMpesaRequest(request: MpesaRequest): MutableLiveData<MpesaResponse> {
        val mpesaResponse: MutableLiveData<MpesaResponse> = MutableLiveData()

        disposable.add(
            client.makeMpesaRequesst(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        mpesaResponse.value = it
                    },
                    {
                        Log.e("MPESA REQUEST", " ${it.localizedMessage}")
                    }
                )
        )

        return mpesaResponse
    }

    fun getMpesaResult(): MutableLiveData<String> {
        val mpesaResult: MutableLiveData<String> = MutableLiveData()


        Handler().postDelayed({
            disposable.add(
                client.getMpesaResult()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            mpesaResult.value = it
                        },
                        {
                            Log.e("MPESA RESULT", " ${it.localizedMessage}")
                        }
                    )
            )
        }, 10000)

        return mpesaResult
    }

    fun getCartItemSize(): LiveData<Int> {
        var cartCount: LiveData<Int> = MutableLiveData()
        cartCount = repository.getCartItemSize()

        return cartCount
    }

    fun updateUserDetails(update: UserUpdate): MutableLiveData<Boolean> {
        val updateResponse: MutableLiveData<Boolean> = MutableLiveData()

        disposable.add(
            client.updateUserDetails(update)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        updateResponse.value = it.isSuccessful
                    },
                    {
                        // error
                    }
                )
        )

        return updateResponse
    }

    fun updatePassword(updatePassword: UpdatePassword): MutableLiveData<String> {
        val passwordResponse: MutableLiveData<String> = MutableLiveData()

        disposable.add(
            client.updatePassword(updatePassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        passwordResponse.value = it
                    },
                    {

                    }
                )
        )

        return passwordResponse
    }

    fun postCart(cart: Cart): MutableLiveData<Long> {
        val cartResponse: MutableLiveData<Long> = MutableLiveData()

        disposable.add(
            client.postCart(cart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        cartResponse.value = it
                    },
                    {
                        cartResponse.value = 0
                    }
                )
        )

        return cartResponse
    }

    fun postCartItem(cartItems: CartItems): MutableLiveData<String> {
        val cartItemResponse: MutableLiveData<String> = MutableLiveData()

        disposable.add(
            client.postCartItem(cartItems)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        cartItemResponse.value = it
                    },
                    {

                    }
                )
        )

        return cartItemResponse
    }

    fun getOrders(userId: Int): MutableLiveData<List<Order>>{
        val orders: MutableLiveData<List<Order>> = MutableLiveData()

        disposable.add(
            client.getOrders(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        orders.value = it
                    },
                    {
                        //error
                    }
                )
        )

        return orders
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}