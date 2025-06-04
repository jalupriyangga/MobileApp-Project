package com.example.mobileapptechnobit.ViewModel

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapptechnobit.data.API.ApiClient
import com.example.mobileapptechnobit.data.remote.PatroliRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class PatroliViewModel : ViewModel() {
    private val _capturedBitmap = MutableStateFlow<Bitmap?>(null)
    val capturedBitmap: StateFlow<Bitmap?> get() = _capturedBitmap

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> get() = _token

    private val _submitSuccess = MutableStateFlow<Boolean?>(null)
    val submitSuccess: StateFlow<Boolean?> get() = _submitSuccess

    private val _submitMessage = MutableStateFlow<String?>(null)
    val submitMessage: StateFlow<String?> get() = _submitMessage

    fun onTakePhoto(bitmap: Bitmap, token: String) {
        _capturedBitmap.value = bitmap
        _token.value = token
    }

    fun clearData() {
        _capturedBitmap.value = null
        _token.value = null
    }

    fun submitPatroli(
        token: String?,
        photoBase64: String,
        filename: String,
        shiftId: Int,
        catatan: String,
        kondisi: String,
        placeId: Int = 1,
        latitude: String = "-7.9666",
        longitude: String = "112.6326"
    ) {
        viewModelScope.launch {
            try {
                val request = PatroliRequest(
                    photo_base64 = photoBase64,
                    filename = filename,
                    shift_id = shiftId,
                    place_id = placeId,
                    catatan = catatan,
                    kondisi = kondisi,
                    latitude = latitude,
                    longitude = longitude
                )
                val response = ApiClient.apiService.submitPatroli("Bearer $token", request)
                _submitSuccess.value = response.isSuccessful
                if (response.isSuccessful) {
                    val responseBodyString = response.body()?.string()
                    _submitMessage.value = responseBodyString
                    Log.d("PatroliViewModel", "Sukses submit patroli: $responseBodyString")
                } else {
                    val errMsg = response.errorBody()?.string()
                    _submitMessage.value = errMsg
                    Log.e("PatroliViewModel", "Gagal submit: $errMsg")
                }
            } catch (e: Exception) {
                _submitSuccess.value = false
                _submitMessage.value = e.message
                Log.e("PatroliViewModel", "Exception submit patroli: ${e.message}")
            }
        }
    }
}