package com.tosh.poolandroid.view.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.util.PatternsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.google.android.material.snackbar.Snackbar
import com.tosh.poolandroid.R
import com.tosh.poolandroid.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    lateinit var containerLL: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        containerLL = findViewById<LinearLayout>(R.id.containerLL)
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        register_login_btn.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)

            startActivity(intent)
            finish()
        }

        register_btn.setOnClickListener {
            val name = name_register.text.toString().trim()
            val email = email_register.text.toString().trim()
            val password = password_register.text.toString().trim()
            val confirmPassword = confirm_password_register.text.toString().trim()

            if (name.isEmpty()){
                name_register.error = "Enter name"
                return@setOnClickListener
            }
            if (email.isEmpty()){
                email_register.error = "Enter email"
                return@setOnClickListener
            }
            if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
                email_register.error = "Enter a valid email"
                return@setOnClickListener
            }
            if (password.isEmpty()){
                password_register.error = "Enter password"
                return@setOnClickListener
            }

            if (password.length <6){
                password_register.error = "Password should be more than 6 characters long"
                return@setOnClickListener
            }

            if (!password.equals(confirmPassword)){
                confirm_password_register.error = "Passwords dont match"
                return@setOnClickListener
            }
            loading()
            instantiateRegisterViewModel(name, email, password, confirmPassword)
        }
    }

    private fun instantiateRegisterViewModel(name:String, email:String,  password:String, confirmPassord:String) {
        containerLL.bringToFront()
        containerLL.visibility = View.VISIBLE
        val registrationViewModel: MainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        registrationViewModel.userRegister(name,email, password, confirmPassord).observe(this, Observer {

            if (it == "successful"){
                val intent = Intent(applicationContext, PhoneActivity::class.java)
                intent.putExtra("Name", name)
                intent.putExtra("Email", email)
                addToSharedPreferences(email)
                startActivity(intent)
                finish()
            }else{
                val contextView: View = findViewById(R.id.registerLayout)
                val snackbar = Snackbar
                    .make(contextView, it, Snackbar.LENGTH_LONG)
                snackbar.show()
                containerLL.visibility = View.GONE
            }
        })
    }

    fun addToSharedPreferences(email: String) {
        var preferences: SharedPreferences?
        var editor: SharedPreferences.Editor?

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        editor = preferences.edit()
        editor.putString("email", email)
        editor.apply()

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
