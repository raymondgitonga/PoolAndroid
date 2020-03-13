package com.tosh.poolandroid.view.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.util.PatternsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.UserUpdate
import com.tosh.poolandroid.model.database.UserEntity
import com.tosh.poolandroid.util.Constants
import com.tosh.poolandroid.util.addToSharedPreferences
import com.tosh.poolandroid.viewmodel.MainViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_edit.*

class EditFragment : BaseDialogFragment() {

    private lateinit var rootView: View
    private lateinit var email: String
    private lateinit var name: String
    private lateinit var phone: String
    private var mainViewModel: MainViewModel? = null
    private var userId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit, container, false)

        if (dialog != null && dialog!!.window != null) {
            dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE);
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        editUserDetails()
    }

    override fun onResume() {
        super.onResume()

        val window = dialog!!.window
        window!!.setLayout(900, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
    }

    private fun editUserDetails() {
        email = arguments?.get("EMAIL").toString()
        name = arguments?.get("NAME").toString()
        phone = arguments?.get("PHONE").toString()
        userId = arguments?.getInt("USERID")

        etName.setText(name)
        etPhone.setText(phone.substring(3))
        etEmail.setText(email)

        btnEdit.setOnClickListener {
            val newName = etName.text.toString().trim()
            val newEmail = etEmail.text.toString().trim()
            val newPhone = etPhone.text.toString().trim()

            if (newName.isEmpty()) {
                etName.error = "Enter name"
                return@setOnClickListener
            }
            if (newEmail.isEmpty()) {
                etEmail.error = "Enter new email"
                return@setOnClickListener
            }
            if (newPhone.isEmpty()) {
                etPhone.error = "Enter new phone"
                return@setOnClickListener
            }
            if (!PatternsCompat.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                etEmail.error = "Enter a valid email"
                Toast.makeText(context, "Enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (newPhone.first().equals("0")) {
                etPhone.error = "Enter a valid phone number"
                Toast.makeText(context, "Enter a valid phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (newPhone.length != 9) {
                etPhone.error = "Enter a valid phone number"
                Toast.makeText(context, "Enter a valid phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressDetails.visibility = VISIBLE

            val updatedDetails = UserUpdate(
                name = newName,
                phone = newPhone,
                oldEmail = email,
                newEmail = newEmail
                )

            val userEntity = UserEntity(
                id = userId!!,
                email = newEmail,
                name = newName,
                phone = "254$newPhone"
            )

            mainViewModel!!.updateUserDetails(updatedDetails).observe(viewLifecycleOwner, Observer {
                if (it == true ) {
                    progressDetails.visibility = GONE
                    mainViewModel!!.deleteUser()
                    mainViewModel!!.insert(userEntity)
                    val settings = PreferenceManager.getDefaultSharedPreferences(context)
                    settings.edit().remove(Constants.SHARED_EMAIL).apply()
                    settings.edit().remove(Constants.SHARED_PHONE).apply()
                    addToSharedPreferences(context!!, newEmail, newPhone)
                    dismiss()
                    val fragmentManager = activity!!.supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.details_fragment, ProfileFragment())
                    fragmentTransaction.commit()
                    Toasty.success(context!!, "Details updated", Toast.LENGTH_LONG).show()
                } else {
                    progressDetails.visibility = GONE
                    Toasty.error(context!!, "Something went wrong", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            })


        }
    }

}
