package com.example.sisteminformasimenejemensatpam.ViewModel

import android.content.Context
import android.os.Handler
import android.os.Looper

fun generateOTP(): String{
    val otp = (100000..999999).random()
    return otp.toString()
}

fun saveOTP(context: Context, otp: String, email: String){
    val sharedPref = context.getSharedPreferences("otp_pref", Context.MODE_PRIVATE)
    sharedPref.edit().putString("otp_$email", otp).apply()
    Handler(Looper.getMainLooper()).postDelayed({
        clearOTP(context, email)
    }, 60000)
}

fun getOTP(context: Context, email: String): String? {
    val sharedPref = context.getSharedPreferences("otp_pref", Context.MODE_PRIVATE)
    return sharedPref.getString("otp_$email", null)
}

fun clearOTP(context: Context, email: String){
    val sharedPref = context.getSharedPreferences("otp_pref", Context.MODE_PRIVATE)
    sharedPref.edit().remove("otp_$email").apply()
}