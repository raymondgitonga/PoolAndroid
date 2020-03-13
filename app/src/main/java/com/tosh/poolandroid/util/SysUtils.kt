package com.tosh.poolandroid.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

fun vibrate(context: Context, milliSecs:Long){
    val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        v.vibrate(VibrationEffect.createOneShot(milliSecs, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        v.vibrate(milliSecs)
    }
}