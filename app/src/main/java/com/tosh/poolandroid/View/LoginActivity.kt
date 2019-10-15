package com.tosh.poolandroid.View

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tosh.poolandroid.R
import com.tosh.poolandroid.ViewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_register_btn.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)

            startActivity(intent)
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

            instantiateLoginViewModel(email, password)

        }
    }

    private fun instantiateLoginViewModel(email:String,  password:String) {
        val loginViewModel: LoginViewModel = ViewModelProviders.of(this)[LoginViewModel::class.java]
        loginViewModel.userLogin(email, password).observe(this, Observer {

            if (it.equals("successful")){
                val intent = Intent(applicationContext, MainActivity::class.java)
                addToSharedPreferences(email)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(applicationContext,it, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun addToSharedPreferences(email: String) {
        var preferences: SharedPreferences? = null
        var editor: SharedPreferences.Editor?
        var settings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (preferences != null) {
            if (preferences.contains("email")){
                settings.edit().remove("email").apply()
                preferences = PreferenceManager.getDefaultSharedPreferences(this)
                editor = preferences.edit()
                editor.putString("email", email)
                editor.apply()
            }
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        editor = preferences.edit()
        editor.putString("email", email)
        editor.apply()

    }
}
