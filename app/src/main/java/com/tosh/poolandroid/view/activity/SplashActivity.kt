package com.tosh.poolandroid.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tosh.poolandroid.R
import com.tosh.poolandroid.util.getSharedPreferencesValue
import com.tosh.poolandroid.view.appIntro.AppIntroActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val value = getSharedPreferencesValue(baseContext, "email")

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