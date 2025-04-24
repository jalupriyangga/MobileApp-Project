package com.example.mobileapptechnobit.ViewModel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CameraPresViewModel : ViewModel() {
    private val _capturedBitmap = MutableStateFlow<Bitmap?>(null)
    val capturedBitmap: StateFlow<Bitmap?> get() = _capturedBitmap

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> get() = _token

    fun onTakePhoto(bitmap: Bitmap, token: String) {
        Log.d("CameraPresViewModel", "Setting Bitmap and Token: $bitmap, $token")
        _capturedBitmap.value = bitmap
        _token.value = token
    }

    fun clearData() {
        Log.d("CameraPresViewModel", "Clearing data")
        _capturedBitmap.value = null
        _token.value = null
    }

    fun saveClockInTime(context: Context, clockInTime: Long) {
        val sharedPrefs = context.getSharedPreferences("PresensiPrefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putLong("clockInTime", clockInTime).apply()
    }

    fun getClockInTime(context: Context): Long {
        val sharedPrefs = context.getSharedPreferences("PresensiPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getLong("clockInTime", 0L)
    }

    fun clearSessionData(context: Context) {
        val sharedPrefs = context.getSharedPreferences("PresensiPrefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().clear().apply()
    }
}