package com.tosh.poolandroid.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.tosh.poolandroid.R
import com.tosh.poolandroid.util.show
import com.tosh.poolandroid.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_phone.*

class PhoneActivity : AppCompatActivity() {
    lateinit var containerLL: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)

        containerLL = findViewById<LinearLayout>(R.id.containerLL)
        val intent = intent
        var email:String = intent.getStringExtra("Email")
        var name:String = intent.getStringExtra("Name")

        proceed_btn.setOnClickListener {
            val phone = phone_input.text.toString().trim()

            if (phone.isEmpty()){
                phone_input.error = "Enter a phone number"
                Toast.makeText(this, "Enter a phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (phone.length !=9){
                phone_input.error = "Enter a valid number"
                Toast.makeText(this, "Enter a valid number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loading()
            instantiatePhoneViewModel(name, email,phone)
        }
    }

    private fun instantiatePhoneViewModel(name:String, email:String, phone:String) {
        containerLL.visibility = View.VISIBLE
        val phoneViewModel: MainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        phoneViewModel.addUserPhone(name, email, phone).observe(this, Observer {
            if (it == "successful"){
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(applicationContext,it, Toast.LENGTH_LONG).show()
                containerLL.visibility = View.GONE
            }
        })

    }

    private fun loading(){
        var lazyLoader = LazyLoader(this, 15, 5,
            ContextCompat.getColor(this, R.color.loader_selected),
            ContextCompat.getColor(this, R.color.loader_selected),
            ContextCompat.getColor(this, R.color.loader_selected))
            .apply {
                animDuration = 500
                firstDelayDuration = 100
                secondDelayDuration = 200
                interpolator = DecelerateInterpolator() as Interpolator
            }
        containerLL.addView(lazyLoader)
    }
}
