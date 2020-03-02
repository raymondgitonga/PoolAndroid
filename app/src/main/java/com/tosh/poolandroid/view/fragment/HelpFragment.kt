package com.tosh.poolandroid.view.fragment


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tosh.poolandroid.R
import com.tosh.poolandroid.view.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_help.*

/**
 * A simple [Fragment] subclass.
 */
class HelpFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setupToolbar(getString(R.string.help))

        callHelpLine()
    }

    private fun callHelpLine(){
        val phoneNo = "0729320243"
       call.setOnClickListener {
           val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel: $phoneNo"))
           activity!!.startActivity(intent)
       }
    }
}
