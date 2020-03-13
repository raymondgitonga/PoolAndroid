package com.tosh.poolandroid.view.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.Cart
import com.tosh.poolandroid.model.CartItem
import com.tosh.poolandroid.model.MpesaRequest
import com.tosh.poolandroid.model.database.MainDatabase
import com.tosh.poolandroid.util.Constants
import com.tosh.poolandroid.util.Constants.SHARED_EMAIL
import com.tosh.poolandroid.util.Constants.SHARED_LATITUDE
import com.tosh.poolandroid.util.Constants.SHARED_LONGITUDE
import com.tosh.poolandroid.util.deliveryDistance
import com.tosh.poolandroid.util.getAddress
import com.tosh.poolandroid.util.getSharedPreferencesValue
import com.tosh.poolandroid.view.activity.MainActivity
import com.tosh.poolandroid.viewmodel.MainViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CheckoutFragment : BaseFragment() {

    private lateinit var checkoutTotal: String
    private lateinit var total: String
    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var deliveryLocation: String
    private var isLocationNear: Boolean? = false
    private var mainViewModel: MainViewModel? = null
    lateinit var date: String
    private var id: Int? = null


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

        mainViewModel!!.getUserDetails().observe(viewLifecycleOwner, Observer { userEntities ->
            for (i in userEntities.indices) {
                id = userEntities[i].id
            }
        })


        mpesaNumber.setText(getSharedPreferencesValue(context!!, Constants.SHARED_PHONE))



        checkoutTotal = arguments?.getString("CHECKOUT_TOTAL")!!
        total = arguments?.getString("TOTAL")!!
        latitude = getSharedPreferencesValue(context!!, SHARED_LATITUDE)
        longitude = getSharedPreferencesValue(context!!, SHARED_LONGITUDE)
        deliveryLocation = getAddress(context!!, latitude.toDouble(), longitude.toDouble())

        val pattern = "yyyyMMddHHmmss"
        val simpleDateFormat = SimpleDateFormat(pattern)
        date = simpleDateFormat.format(Date())

        btnCheckout.setOnClickListener {
            if (isLocationNear == true) {
                progressCheckout.visibility = VISIBLE
                getUserPhone(checkoutTotal, date)
            } else {
                Toasty.info(context!!, "We don't deliver to your location yet", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        setUpCheckoutDetails()
        changeLocation()
    }

    private fun getUserPhone(amount: String, date: String) {
        val newPhone = mpesaNumber.text.toString().trim()

        mainViewModel!!.getUserDetails().observe(viewLifecycleOwner, Observer { userEntities ->
            for (i in userEntities.indices) {
                val mpesaRequest = MpesaRequest(
                    amount = amount,
                    phone = "254$newPhone",
                    timestamp = date
                )
                makeMpesaRequest(mpesaRequest)
            }
        })
    }

    private fun makeMpesaRequest(request: MpesaRequest) {
        mainViewModel!!.makeMpesaRequest(request).observe(viewLifecycleOwner, Observer {
            if (it.status == "Success") {
                Toast.makeText(context, "Processing", Toast.LENGTH_SHORT).show()
                getMpesaResult()
            } else {
                Toast.makeText(context, "Error processing payment", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getMpesaResult() {
        mainViewModel!!.getMpesaResult().observe(viewLifecycleOwner, Observer {
            if (it == "Success") {
                progressCheckout.visibility = GONE
                Toasty.success(context!!, "Order Successfull", Toast.LENGTH_SHORT).show()
            } else {
                progressCheckout.visibility = GONE
                Toasty.error(context!!, "Payment not successful, check account", Toast.LENGTH_SHORT)
                    .show()
                postCart()
            }
        })
    }

    private fun setUpCheckoutDetails() {
        shoppingCost.text = "$total KES"
        grandTotal.text = "$checkoutTotal KES"
        deliveryAddress.text = deliveryLocation
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
                    "CLOSE" -> {
                        isLocationNear = true
                        locationError.visibility = GONE
                    }
                    "FAR" -> {
                        isLocationNear = false
                        locationError.visibility = VISIBLE
                    }
                }
            }
            fragmentPlaces.show(fragmentTransaction, "places")

        }
    }

    private fun postCart() {
        launch {
            context.let {
                val cartItems = MainDatabase.getInstance(it!!)!!.cartItemDao().getCartItems()

                val cart = Cart(
                    id = getSharedPreferencesValue(context!!, SHARED_EMAIL),
                    primaryId = id!!
                )

                mainViewModel!!.postCart(cart).observe(viewLifecycleOwner, Observer {
                    Log.e("CART---->", " ${it}")
                })

                for (item in cartItems) {
                    val cartItem = CartItem(
                        cartPrimaryId = getSharedPreferencesValue(context!!, SHARED_EMAIL),
                        productId = item.productId,
                        productName = item.productName,
                        extraId = item.extraId!!,
                        extraName = item.extraName!!,
                        extraPrice = item.extraPrice!!,
                        productQuantity = item.productQuantity!!,
                        productPrice = item.productPrice,
                        totalPrice = item.total,
                        vendorId = item.vendorId
                    )

                    Handler().postDelayed({
                        mainViewModel!!.postCartItem(cartItem)
                            .observe(viewLifecycleOwner, Observer {
                                Log.e("CARTITEM--->", " ${it}")
                            })
                    }, 2000)


                }


            }
        }

    }
}
