package com.tosh.poolandroid.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.MpesaRequest
import com.tosh.poolandroid.util.Constants.SHARED_LATITUDE
import com.tosh.poolandroid.util.Constants.SHARED_LONGITUDE
import com.tosh.poolandroid.util.deliveryDistance
import com.tosh.poolandroid.util.getAddress
import com.tosh.poolandroid.util.getSharedPreferencesValue
import com.tosh.poolandroid.view.activity.MainActivity
import com.tosh.poolandroid.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_checkout.*

class CheckoutFragment : BaseFragment() {

    private lateinit var checkoutTotal: String
    private lateinit var total: String
    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var deliveryLocation: String
    private var isLocationChanged: Boolean? = null
    private var mainViewModel: MainViewModel? = null

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

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        isLocationChanged = false

        checkoutTotal = arguments?.getString("CHECKOUT_TOTAL")!!
        total = arguments?.getString("TOTAL")!!
        latitude = getSharedPreferencesValue(context!!, SHARED_LATITUDE)
        longitude = getSharedPreferencesValue(context!!, SHARED_LONGITUDE)
        deliveryLocation = getAddress(context!!, latitude.toDouble(), longitude.toDouble())

        val mpesaRequest = MpesaRequest(
            amount = checkoutTotal,
            phone = getUserPhone()
        )

        btnCheckout.setOnClickListener {
            progressCheckout.visibility = VISIBLE
            makeMpesaRequest(mpesaRequest)
        }

        setUpCheckoutDetails()
        changeLocation()
    }

    private fun getUserPhone(): String {
        var phoneNumber: String? = null
        mainViewModel!!.getUserDetails().observe(viewLifecycleOwner, Observer { userEntities ->
            for (i in userEntities.indices) {
                phoneNumber = userEntities[i].phone
            }
        })

        return phoneNumber.toString()
    }

    private fun makeMpesaRequest(request: MpesaRequest) {
        mainViewModel!!.makeMpesaRequest(request).observe(viewLifecycleOwner, Observer {
            if (it.status == "Success") {
                progressCheckout.visibility = GONE
                Toast.makeText(context, "Processing", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error processing payment", Toast.LENGTH_SHORT).show()
            }
        })
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
            val fragmentManager = activity!!.supportFragmentManager

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
