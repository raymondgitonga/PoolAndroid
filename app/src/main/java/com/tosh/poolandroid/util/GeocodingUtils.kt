package com.tosh.poolandroid.util

import android.content.Context
import android.location.Geocoder
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

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
    return address
}

fun deliveryDistance(latitude: Double, longitude: Double): Int {

    val lat1 = -1.298219
    val lon1 = 36.762513
    val theta = lon1 - longitude
    var dist =
        sin(deg2rad(lat1)) * sin(deg2rad(latitude)) + cos(
            deg2rad(lat1)
        ) * cos(deg2rad(latitude)) * cos(deg2rad(theta))
    dist = acos(dist)
    dist = rad2deg(dist)
    dist *= 60 * 1.1515

    dist *= 1.609344

    return if (dist > 0.5){
        0
    }else {
        1
    }
}

// This function converts decimal degrees to radians
fun deg2rad(deg: Double): Double {
    return deg * Math.PI / 180.0
}

//  This function converts radians to decimal degrees
fun rad2deg(rad: Double): Double {
    return rad * 180.0 / Math.PI
}
