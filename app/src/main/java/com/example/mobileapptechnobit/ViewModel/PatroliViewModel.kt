package com.example.mobileapptechnobit.ViewModel

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapptechnobit.data.API.ApiClient
import com.example.mobileapptechnobit.data.remote.Presensi
import com.example.mobileapptechnobit.data.remote.PresensiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class PatroliViewModel : ViewModel() {

    private val _capturedBitmap = MutableStateFlow<Bitmap?>(null)
    val capturedBitmap: StateFlow<Bitmap?> get() = _capturedBitmap

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> get() = _token

    private val _presensiResponse = MutableStateFlow<PresensiResponse?>(null)
    val presensiResponse: StateFlow<PresensiResponse?> get() = _presensiResponse

    fun onTakePhoto(bitmap: Bitmap, token: String) {
        _capturedBitmap.value = bitmap
        _token.value = token
        Log.d("PatroliViewModel", "Bitmap dan token disimpan: $bitmap, $token")
    }

    fun clearData() {
        Log.d("PatroliViewModel", "Clearing captured data.")
        _capturedBitmap.value = null
        _token.value = null
    }

    suspend fun sendPatroliDataToApi(
        token: String,
        photoBase64: String,
        filename: String
    ) {
        withContext(Dispatchers.IO) {
            try {
                val requestBody = Presensi(
                    status = "On Patrol",
                    photo_data = photoBase64,
                    filename = filename,
                    company_place_id = 1,
                    note = "Patroli check-in"
                )
                val response = ApiClient.apiService.sendPresensi("Bearer $token", requestBody)

                if (response.isSuccessful) {
                    _presensiResponse.value = response.body()
                    Log.d("PatroliResponse", "Patroli data successfully sent: ${response.body()}")
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("PatroliError", "Failed to send patrol data: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("PatroliException", "Exception during patrol data submission", e)
            }
        }
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}