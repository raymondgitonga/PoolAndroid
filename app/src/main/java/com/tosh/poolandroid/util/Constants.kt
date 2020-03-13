package com.tosh.poolandroid.util

object Constants {

    //Network calls
    const val AUTH_BASE_URL = "http://poolauth-env-1.y3jkszyd3w.eu-west-2.elasticbeanstalk.com/"
    const val VENDOR_PRODUCT_URL = "http://vendors-env.y3jkszyd3w.eu-west-2.elasticbeanstalk.com/"
    const val PAYMENT_API = "http://payment-env.y3jkszyd3w.eu-west-2.elasticbeanstalk.com/"
    const val ORDER_API = "http://10.0.2.2:1112/"

    //distance calculation
    const val FAR = 0
    const val CLOSE = 1

    //shared preferences
    const val SHARED_EMAIL = "email"
    const val SHARED_PHONE = "phone"
    const val SHARED_DEFAULT = "default"
    const val SHARED_LATITUDE = "latitude"
    const val SHARED_LONGITUDE = "longitude"
}