package com.tosh.poolloginrebuild.repository

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.tosh.poolloginrebuild.database.UserDao
import com.tosh.poolloginrebuild.database.UserDatabase
import com.tosh.poolloginrebuild.database.UserEntity

class UserRepository(application: Application) {

    private var userDao: UserDao

    private var userDetails: LiveData<List<UserEntity>>

    init {
        val userDb: UserDatabase = UserDatabase.getInstance(
            application.applicationContext
        )!!

        userDao = userDb.userDao()
        userDetails = userDao.getUserDetails()
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

    fun getUserDetails(): LiveData<List<UserEntity>> {
        return userDetails
    }


}