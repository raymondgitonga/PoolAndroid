package com.tosh.poolandroid.view.appIntro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tosh.poolandroid.R
import com.tosh.poolandroid.view.activity.LoginActivity

class ThirdFragment : Fragment() {
    private val getStartedBtn: Button? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third, container, false)
        val btnStarted: TextView = view.findViewById(R.id.btn_getStarted)
        btnStarted.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }
        return view
    }
}