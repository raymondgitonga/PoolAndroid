package com.tosh.poolandroid.view.appIntro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.tosh.poolandroid.R

class AppIntroActivity : AppCompatActivity() {

    internal lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_intro)

        viewPager = findViewById(R.id.viewPager)

        val adapter = IntroAdapter(supportFragmentManager)
        viewPager.adapter = adapter
    }
}