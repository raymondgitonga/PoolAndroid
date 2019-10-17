package com.tosh.poolandroid.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tosh.poolandroid.R
import com.tosh.poolandroid.util.show
import com.tosh.poolandroid.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

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

            instantiateLoginViewModel(email, password)

        }
    }

    private fun instantiateLoginViewModel(email:String,  password:String) {
        val userViewModel: UserViewModel = ViewModelProviders.of(this)[UserViewModel::class.java]
        userViewModel.userLogin(email, password).observe(this, Observer {

            if (it.equals("successful")){
                val intent = Intent(applicationContext, MainActivity::class.java)
                addToSharedPreferences(email)
                login_progress.show()
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(applicationContext,it, Toast.LENGTH_LONG).show()
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
}
