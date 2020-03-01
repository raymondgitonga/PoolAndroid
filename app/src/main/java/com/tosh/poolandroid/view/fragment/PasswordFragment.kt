package com.tosh.poolandroid.view.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

import com.tosh.poolandroid.R

class PasswordFragment : BaseDialogFragment() {

    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

}
