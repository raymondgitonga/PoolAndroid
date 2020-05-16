package com.tosh.poolandroid.view.fragment

import android.os.Bundle
import android.os.Handler
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
import com.tosh.poolandroid.model.CartItems
import com.tosh.poolandroid.model.MpesaRequest
import com.tosh.poolandroid.model.database.MainDatabase
import com.tosh.poolandroid.util.*
import com.tosh.poolandroid.util.Constants.ORDER_PENDING
import com.tosh.poolandroid.util.Constants.SHARED_CART_ID
import com.tosh.poolandroid.util.Constants.SHARED_LATITUDE
import com.tosh.poolandroid.util.Constants.SHARED_LONGITUDE
import com.tosh.poolandroid.view.activity.MainActivity
import com.tosh.poolandroid.viewmodel.MainViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
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
    //    private var cartId: Int? = null
    var cartId: Long? = null


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
                    phone = newPhone,
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
                postCart()
            } else {
                progressCheckout.visibility = GONE
                Toasty.error(context!!, "Payment not successful, check account", Toast.LENGTH_SHORT)
                    .show()
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
            context.let { it ->
                val cartItems = MainDatabase.getInstance(it!!)!!.cartItemDao().getCartItems()

                val cart = Cart(
                    userId = id!!,
                    cost = total.toDouble(),
                    deliveryCost = 150.0,
                    total = checkoutTotal.toDouble(),
                    state = ORDER_PENDING,
                    latitude = getSharedPreferencesValue(context!!, SHARED_LATITUDE),
                    longitude = getSharedPreferencesValue(context!!, SHARED_LONGITUDE),
                    createdDate = date
                )

                mainViewModel!!.postCart(cart).observe(viewLifecycleOwner, Observer {
                    addToSharedPreferences(context!!, it.toString())
                })
                Handler().postDelayed({
                    cartId = getSharedPreferencesValue(context!!, SHARED_CART_ID).toLong()
                    for (item in cartItems) {
                        val cartItem = CartItems(
                            cartOrderNumber = cartId!!,
                            productId = item.productId,
                            productName = item.productName,
                            extraId = item.extraId!!,
                            extraName = item.extraName!!,
                            extraPrice = item.extraPrice!!,
                            productQuantity = 0,
                            productPrice = item.productPrice,
                            totalPrice = item.total,
                            vendorId = item.vendorId,
                            vendorName = item.vendorName
                        )
                        mainViewModel!!.postCartItem(cartItem)
                            .observe(viewLifecycleOwner, Observer {
                                if (it == "Success") {
                                    mainViewModel!!.deleteCart()
                                    val ordersFragment = OrdersFragment()
                                    val bundle = Bundle()
                                    bundle.putInt("USER_ID", id!!)
                                    val transaction = activity!!.supportFragmentManager.beginTransaction()
                                    transaction.replace(R.id.details_fragment, ordersFragment)
                                    transaction.commit()
                                } else {
                                    Toasty.error(context!!, "Fail", Toast.LENGTH_SHORT).show()

                                }
                            })
                    }
                }, 3000)
            }
        }

    }
}
