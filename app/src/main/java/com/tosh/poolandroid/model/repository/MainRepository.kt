package com.tosh.poolandroid.model.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.tosh.poolandroid.model.database.*

class MainRepository(application: Application) {

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

    fun getUserDetails(): LiveData<List<UserEntity>> {
        return userDetails
    }
}