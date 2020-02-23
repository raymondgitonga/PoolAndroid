package com.tosh.poolandroid.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.tosh.poolandroid.util.Constants.SHARED_DEFAULT
import com.tosh.poolandroid.util.Constants.SHARED_EMAIL
import com.tosh.poolandroid.util.Constants.SHARED_LATITUDE
import com.tosh.poolandroid.util.Constants.SHARED_LONGITUDE
import com.tosh.poolandroid.util.Constants.SHARED_PHONE

fun addToSharedPreferences(context: Context, email: String?, phone: String?) {
    var preferences: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)
    var editor: SharedPreferences.Editor?

    editor = preferences?.edit()
    editor?.putString(SHARED_EMAIL, email)
    editor?.putString(SHARED_PHONE, phone)
    editor?.apply()

}

fun addLocationPreferences(context: Context, latitude: String?, longitude: String?) {

    val settings = PreferenceManager.getDefaultSharedPreferences(context)
    settings.edit().remove(SHARED_LATITUDE).apply()
    settings.edit().remove(SHARED_LATITUDE).apply()

    val preferences: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)
    val editor: SharedPreferences.Editor?

    editor = preferences?.edit()
    editor?.putString(SHARED_LATITUDE, latitude)
    editor?.putString(SHARED_LONGITUDE, longitude)
    editor?.apply()

}

fun getSharedPreferencesValue(context: Context, key: String?): String{
    val mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
    return mSharedPreference.getString(key, SHARED_DEFAULT)!!
}