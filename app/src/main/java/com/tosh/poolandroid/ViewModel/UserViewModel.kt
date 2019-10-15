package com.tosh.poolandroid.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.tosh.poolloginrebuild.database.UserEntity
import com.tosh.poolloginrebuild.repository.UserRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository = UserRepository(application)

    fun getUserDetails(): LiveData<List<UserEntity>> {
        return repository.getUserDetails()
    }
}