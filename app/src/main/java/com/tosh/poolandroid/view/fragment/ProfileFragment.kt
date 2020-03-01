package com.tosh.poolandroid.view.fragment


import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.tosh.poolandroid.R
import com.tosh.poolandroid.view.activity.MainActivity
import com.tosh.poolandroid.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private var mainViewModel: MainViewModel? = null
    lateinit var email: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        (activity as MainActivity).setupToolbar(getString(R.string.profile))

        getUserDetails()
        changePasswordFragment()

    }

    private fun getUserDetails(){
        mainViewModel!!.getUserDetails().observe(viewLifecycleOwner, Observer { userEntities ->
            for (i in userEntities.indices) {
                email = userEntities[i].email
                profileUserName.text = userEntities[i].name
                profileEmailAddress.text = userEntities[i].email
                profilePhoneNumber.text = userEntities[i].phone
            }
        })
    }

    private fun changePasswordFragment(){
        changePassword.setOnClickListener {
            Log.e("EMAIL --->>>", "$email")

            val fragmentManager = activity!!.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragmentPassword = PasswordFragment()

            fragmentPassword.show(fragmentTransaction, "password")
        }
    }
}
