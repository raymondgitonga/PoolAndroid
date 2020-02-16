package com.tosh.poolandroid.view.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.google.android.material.snackbar.Snackbar
import com.tosh.poolandroid.R
import com.tosh.poolandroid.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_login.*



class LoginActivity : AppCompatActivity() {

    lateinit var containerLL: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        containerLL = findViewById(R.id.containerLL) as LinearLayout

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        login_register_btn.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)

            startActivity(intent)
            finish()
        }

        login_btn.setOnClickListener {
            val email = email_login.text.toString().trim()
            val password = password_login.text.toString().trim()

            if (email.isEmpty()){
                email_login.error = "Enter email"
                return@setOnClickListener
            }

            if (password.isEmpty()){
                password_login.error = "Enter password"
                return@setOnClickListener
            }

            loading()
            instantiateLoginViewModel(email, password)

        }
    }

    private fun instantiateLoginViewModel(email:String,  password:String) {
        containerLL.visibility = VISIBLE
        val mainViewModel: MainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        mainViewModel.userLogin(email, password).observe(this, Observer {

            if (it == "successful"){
                val intent = Intent(applicationContext, MainActivity::class.java)
                addToSharedPreferences(email)
                startActivity(intent)
                finish()
            }else{
                val contextView: View = findViewById(R.id.loginLayout)
                val snackbar = Snackbar
                    .make(contextView, it, Snackbar.LENGTH_LONG)
                snackbar.show()
                containerLL.visibility = GONE
            }
        })
    }

    fun addToSharedPreferences(email: String) {
        var preferences: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(this)
        var editor: SharedPreferences.Editor?

        editor = preferences?.edit()
        editor?.putString("email", email)
        editor?.apply()

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
