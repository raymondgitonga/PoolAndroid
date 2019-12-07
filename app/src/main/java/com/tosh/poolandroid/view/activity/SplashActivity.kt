package com.tosh.poolandroid.view.activity

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.tosh.poolandroid.R
import com.tosh.poolandroid.view.appIntro.AppIntroActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext())
        val value = mSharedPreference.getString("email", "default")

        Thread(Runnable {
            runProgressBar()

            if (value == "default") {
                startApp()
                finish()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }).start()
    }

    private fun runProgressBar() {
        var progress = 0
        while (progress < 100) {
            try {
                Thread.sleep(300)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            progress += 10
        }
    }

    private fun startApp() {
        val intent = Intent(this, AppIntroActivity::class.java)
        startActivity(intent)
    }
}