package com.tosh.poolandroid.view.fragment


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.LOG

import com.tosh.poolandroid.R
import com.tosh.poolandroid.util.Constants.CLOSE
import com.tosh.poolandroid.util.Constants.FAR
import com.tosh.poolandroid.util.Constants.SHARED_LATITUDE
import com.tosh.poolandroid.util.Constants.SHARED_LONGITUDE
import com.tosh.poolandroid.util.deliveryDistance
import com.tosh.poolandroid.util.getAddress
import com.tosh.poolandroid.util.getSharedPreferencesValue
import com.tosh.poolandroid.view.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_checkout.*

class CheckoutFragment : BaseFragment() {

    private lateinit var checkoutTotal: String
    private lateinit var total: String
    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var deliveryLocation:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setupToolbar(getString(R.string.checkout_details))

        checkoutTotal = arguments?.getString("CHECKOUT_TOTAL")!!
        total = arguments?.getString("TOTAL")!!
        latitude = getSharedPreferencesValue(context!!, SHARED_LATITUDE)
        longitude = getSharedPreferencesValue(context!!, SHARED_LONGITUDE)
        deliveryLocation = getAddress(context!!, latitude.toDouble(), longitude.toDouble())

        setUpCheckoutDetails()
    }

    private fun setUpCheckoutDetails(){
        shoppingCost.text = "$total KES"
        grandTotal.text = "$checkoutTotal KES"
        deliveryAddress.text = deliveryLocation

        val distance = deliveryDistance(latitude.toDouble(), longitude.toDouble())

        when(distance){
            FAR -> locationError.visibility = VISIBLE
            CLOSE -> locationError.visibility = GONE
        }
    }
}
