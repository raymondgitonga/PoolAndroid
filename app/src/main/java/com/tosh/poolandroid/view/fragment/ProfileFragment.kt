package com.tosh.poolandroid.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tosh.poolandroid.R
import com.tosh.poolandroid.view.activity.MainActivity
import com.tosh.poolandroid.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private var mainViewModel: MainViewModel? = null
    lateinit var email: String
    lateinit var name: String
    lateinit var phone: String

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
        editDetailsFragmment()
    }

    private fun getUserDetails(){
        mainViewModel!!.getUserDetails().observe(viewLifecycleOwner, Observer { userEntities ->
            for (i in userEntities.indices) {
                email = userEntities[i].email
                name = userEntities[i].name
                phone = userEntities[i].phone
                profileUserName.text = name
                profileEmailAddress.text = email
                profilePhoneNumber.text = phone
            }
        })
    }

    private fun changePasswordFragment(){
        changePassword.setOnClickListener {

            val fragmentManager = activity!!.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragmentPassword = PasswordFragment()

            val args = Bundle()
            args.putString("EMAIL", email)
            fragmentPassword.arguments = args
            fragmentPassword.show(fragmentTransaction, "password")
        }
    }

    private fun editDetailsFragmment(){
        btn_edit_profile.setOnClickListener {
            val fragmentManager = activity!!.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragmentEdit = EditFragment()

            val args = Bundle()
            args.putString("EMAIL", email)
            args.putString("NAME", name)
            args.putString("PHONE", phone)
            fragmentEdit.arguments = args
            fragmentEdit.show(fragmentTransaction, "edit details")
        }
    }
}
