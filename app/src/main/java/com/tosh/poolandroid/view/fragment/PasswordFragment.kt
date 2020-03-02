package com.tosh.poolandroid.view.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.UpdatePassword
import com.tosh.poolandroid.util.Constants
import com.tosh.poolandroid.view.activity.LoginActivity
import com.tosh.poolandroid.viewmodel.MainViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_password.*

class PasswordFragment : BaseDialogFragment() {

    private lateinit var rootView: View
    private lateinit var email: String
    private var mainViewModel: MainViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_password, container, false)

        if (dialog != null && dialog!!.window != null) {
            dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE);
        }
        return rootView
    }

    override fun onResume() {
        super.onResume()

        val window = dialog!!.window
        window!!.setLayout(900, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        changePassword()
    }

    private fun changePassword() {
        email = arguments?.get("EMAIL").toString()

        btnChangePass.setOnClickListener {
            val oldPass = oldPassword.text.toString().trim()
            val newPass = newPassword.text.toString().trim()
            val confirmPassword = confirmNewPassword.text.toString().trim()

            if (oldPass.isEmpty()) {
                oldPassword.error = "Enter old password"
                return@setOnClickListener
            }
            if (newPass.isEmpty()) {
                newPassword.error = "Enter new password"
                return@setOnClickListener
            }
            if (confirmPassword.isEmpty()) {
                confirmNewPassword.error = "Confirm new password"
                return@setOnClickListener
            }
            if (newPass != confirmPassword) {
                confirmNewPassword.error = "Passwords don't much"
                return@setOnClickListener
            }

            val updatedPassword = UpdatePassword(
                email = email,
                newPassword = newPass,
                password = oldPass
            )
            progressChangePass.visibility = VISIBLE

            mainViewModel!!.updatePassword(updatedPassword).observe(viewLifecycleOwner, Observer {
                if (it == "Success") {
                    Toasty.success(context!!, "Password change successful", Toasty.LENGTH_LONG).show()
                    val settings = PreferenceManager.getDefaultSharedPreferences(context)
                    settings.edit().remove(Constants.SHARED_EMAIL).apply()
                    settings.edit().remove(Constants.SHARED_PHONE).apply()
                    settings.edit().remove(Constants.SHARED_LATITUDE).apply()
                    settings.edit().remove(Constants.SHARED_LONGITUDE).apply()
                    mainViewModel!!.deleteUser()

                    progressChangePass.visibility = GONE

                    val intent = Intent(context, LoginActivity::class.java)
                    dismiss()
                    startActivity(intent)
                    activity!!.finish()
                }else{
                    Toasty.error(context!!, "Password change failed, please check details", Toasty.LENGTH_LONG).show()
                    progressChangePass.visibility = GONE
                    dismiss()
                }
            })

        }
    }
}
