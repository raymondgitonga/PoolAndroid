package com.tosh.poolandroid.util

import android.content.Context
import android.location.Geocoder
import android.widget.Toast
import es.dmoral.toasty.Toasty
import java.lang.StringBuilder
import java.util.*

fun getAddress(context: Context, latitude: Double, longitude: Double): String {
    lateinit var address: String

    val geocoder = Geocoder(context, Locale.getDefault())


    val addresses = geocoder.getFromLocation(latitude, longitude, 1)

    var returnAddresses = addresses.get(0)
    var fullAddress = StringBuilder("")
    var length: Int = returnAddresses.maxAddressLineIndex
    for (i in 0..length) {
        fullAddress.append(returnAddresses.getAddressLine(i)).append("\n")
    }

    address = fullAddress.toString()
    Toasty.success(context, "" + address, Toast.LENGTH_LONG).show()



    return address
}