package com.tosh.poolandroid

import android.app.Application
import com.facebook.stetho.Stetho
import timber.log.Timber
import timber.log.Timber.DebugTree

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        //timber
        Timber.plant(DebugTree())

    }
}