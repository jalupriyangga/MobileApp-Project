package com.example.mobileapptechnobit.ViewModel

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mobileapptechnobit.data.API.ApiClient
import com.example.mobileapptechnobit.data.remote.ClockOutRequest
import com.example.mobileapptechnobit.data.remote.Presensi
import com.example.mobileapptechnobit.data.remote.PresensiResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File

class CameraPresViewModel : ViewModel() {

    private val _capturedBitmap = MutableStateFlow<Bitmap?>(null)
    val capturedBitmap: StateFlow<Bitmap?> get() = _capturedBitmap

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> get() = _token

    private val _clockInData = MutableStateFlow<Presensi?>(null)
    val clockInData: StateFlow<Presensi?> get() = _clockInData

    private val _clockInResponse = MutableStateFlow<PresensiResponse?>(null)
    val clockInResponse: StateFlow<PresensiResponse?> get() = _clockInResponse

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
        val clockInTime = sharedPrefs.getLong("clockInTime", 0L)
        Log.d("ClockInTime", "Retrieved ClockInTime: $clockInTime")
        return clockInTime
    }

    fun clearClockInTime(context: Context) {
        val sharedPrefs = context.getSharedPreferences("PresensiPrefs", Context.MODE_PRIVATE)
        Log.d("ClearClockInTime", "Clearing ClockInTime...")
        sharedPrefs.edit().remove("clockInTime").apply()
    }

    fun clearSessionData(context: Context) {
        val sharedPrefs = context.getSharedPreferences("PresensiPrefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().clear().apply()
        Log.d("CameraPresViewModel", "Session data cleared successfully")
    }

    suspend fun sendPresensiToApi(
        token: String,
        photo: MultipartBody.Part,
        file: File,
        employeeId: Int,
        companyPlaceId: Int,
        userNote: String,
        isManual: Boolean
    ): PresensiResponse? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("PresensiAPI", "Preparing request for employeeId=$employeeId, companyPlaceId=$companyPlaceId, file=${file.name}, fileExists=${file.exists()}, fileSize=${file.length()} bytes")

                val employeeIdBody = RequestBody.create("text/plain".toMediaTypeOrNull(), employeeId.toString())
                val companyPlaceIdBody = RequestBody.create("text/plain".toMediaTypeOrNull(), companyPlaceId.toString())
                val userNoteBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userNote)
                val isManualBody = RequestBody.create("text/plain".toMediaTypeOrNull(), isManual.toString())
                val photoPathBody = RequestBody.create("text/plain".toMediaTypeOrNull(), "presensi_new/${file.name}")

                Log.d("PresensiAPI", "Sending request to API...")
                val response = ApiClient.apiService.sendPresensi(
                    "Bearer $token",
                    photo,
                    photoPathBody,
                    employeeIdBody,
                    companyPlaceIdBody,
                    userNoteBody,
                    isManualBody
                )

                Log.d("PresensiAPI", "Response received. isSuccessful=${response.isSuccessful}")
                if (response.isSuccessful) {
                    val respString = response.body()?.string()
                    Log.d("PresensiAPI", "API Success Response Body: $respString")
                    respString?.let {
                        val presensiResponse = Gson().fromJson(it, PresensiResponse::class.java)
                        Log.d("PresensiAPI", "Parsed PresensiResponse: $presensiResponse")
                        return@withContext presensiResponse
                    }
                } else {
                    val errString = response.errorBody()?.string()
                    Log.e("PresensiAPI", "API Error Response Body: $errString")
                    errString?.let {
                        val errorResponse = Gson().fromJson(it, PresensiResponse::class.java)
                        Log.d("PresensiAPI", "Parsed Error PresensiResponse: $errorResponse")
                        return@withContext errorResponse
                    }
                }
                Log.e("PresensiAPI", "No response returned from API")
                null
            } catch (e: Exception) {
                Log.e("PresensiAPI", "Exception when sending presensi: ", e)
                null
            }
        }
    }

    suspend fun sendClockOutToApi(token: String) {
        withContext(Dispatchers.IO) {
            try {
                val clockInData = _clockInResponse.value?.data
                if (clockInData != null) {
                    val filename = clockInData.photo_path.substringAfterLast('/')

                    val requestBody = ClockOutRequest(
                        status = "Present",
                        filename = filename,
                        company_place_id = clockInData.company_place_id,
                        note = "-"
                    )

                    val response = ApiClient.apiService.sendClockOutPresensi("Bearer $token", requestBody)

                    if (response.isSuccessful) {
                        Log.d("ClockOutResponse", "Clock-Out berhasil: ${response.body()}")
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "No error body"
                        Log.e("ClockOutError", "Gagal Clock-Out: $errorBody")
                    }
                } else {
                    Log.e("ClockOutError", "Clock-In data is missing")
                }
            } catch (e: Exception) {
                Log.e("ClockOutException", "Exception during Clock-Out", e)
                throw e
            }
        }
    }
}