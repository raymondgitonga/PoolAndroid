package com.tosh.poolandroid.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

fun addToSharedPreferences(context: Context, email: String?, phone: String?) {
    var preferences: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)
    var editor: SharedPreferences.Editor?

    editor = preferences?.edit()
    editor?.putString("email", email)
    editor?.putString("phone", phone)
    editor?.apply()

}

fun getSharedPreferencesValue(context: Context, key: String?): String{
    val mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
    return mSharedPreference.getString(key, "default")!!
}