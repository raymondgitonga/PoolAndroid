package com.tosh.poolandroid.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.WindowManager
import android.widget.Toast
import androidx.core.util.PatternsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tosh.poolandroid.R
import com.tosh.poolandroid.util.show
import com.tosh.poolandroid.viewmodel.RegistrationViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        register_login_btn.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)

            startActivity(intent)
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
            instantiateRegisterViewModel(name, email, password, confirmPassword)
        }
    }

    private fun instantiateRegisterViewModel(name:String, email:String,  password:String, confirmPassord:String) {
        val registrationViewModel: RegistrationViewModel = ViewModelProviders.of(this)[RegistrationViewModel::class.java]
        registrationViewModel.userRegister(name,email, password, confirmPassord).observe(this, Observer {

            if (it == "successful"){
                val intent = Intent(applicationContext, PhoneActivity::class.java)
                intent.putExtra("Name", name)
                intent.putExtra("Email", email)
                addToSharedPreferences(email)
                register_progress.show()
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
