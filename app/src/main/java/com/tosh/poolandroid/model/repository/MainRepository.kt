package com.tosh.poolandroid.model.repository

import android.app.Application
import android.os.AsyncTask
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tosh.poolandroid.model.database.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainRepository(application: Application)  {

    private lateinit var job: Job

    private var userDao: UserDao
    private var cartItemDao: CartItemDao
    private var userDetails: LiveData<List<UserEntity>>

    init {
        val mainDb: MainDatabase = MainDatabase.getInstance(
            application.applicationContext
        )!!

        userDao = mainDb.userDao()
        userDetails = userDao.getUserDetails()

        cartItemDao = mainDb.cartItemDao()
    }

    fun insert(userEntity: UserEntity) {
        class SaveUser : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                userDao.insertDetails(userEntity)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
            }

        }

        SaveUser().execute()
    }

    fun insert(cartItemEntity: CartItemEntity) {
        class SaveItem : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                cartItemDao.insertDetails(cartItemEntity)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
            }

        }

        SaveItem().execute()
    }

    suspend fun getCartTotal(): Double{
        var total: Double? = null
        coroutineScope {
           launch {
               total = cartItemDao.cartTotal()
           }
        }
        return total!!
    }

    fun getUserDetails(): LiveData<List<UserEntity>> {
        return userDetails
    }
}