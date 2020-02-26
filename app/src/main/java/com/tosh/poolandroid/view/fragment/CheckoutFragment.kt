package com.tosh.poolandroid.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.tosh.poolandroid.R
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
    private lateinit var deliveryLocation: String
    private var isLocationChanged: Boolean? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setupToolbar(getString(R.string.checkout_details))

        isLocationChanged = false

        checkoutTotal = arguments?.getString("CHECKOUT_TOTAL")!!
        total = arguments?.getString("TOTAL")!!
        latitude = getSharedPreferencesValue(context!!, SHARED_LATITUDE)
        longitude = getSharedPreferencesValue(context!!, SHARED_LONGITUDE)
        deliveryLocation = getAddress(context!!, latitude.toDouble(), longitude.toDouble())

        setUpCheckoutDetails()
        changeLocation()
    }

    private fun setUpCheckoutDetails() {
        shoppingCost.text = "$total KES"
        grandTotal.text = "$checkoutTotal KES"
        deliveryAddress.text = deliveryLocation

        when (deliveryDistance(latitude.toDouble(), longitude.toDouble())) {
            "CLOSE" -> locationError.visibility = GONE
            "FAR" -> locationError.visibility = VISIBLE
        }
    }

    private fun changeLocation() {
        changeLocation.setOnClickListener {
            val fragmentManager = childFragmentManager

            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragmentPlaces = PlacesFragment()
            fragmentPlaces.newLocation = {
                deliveryAddress.text = it
            }
            fragmentPlaces.newLatLon = {
                when (deliveryDistance(it[0].toDouble(), it[1].toDouble())) {
                    "CLOSE" -> locationError.visibility = GONE
                    "FAR" -> locationError.visibility = VISIBLE
                }
            }
            fragmentPlaces.show(fragmentTransaction, "places")

        }
    }
}
