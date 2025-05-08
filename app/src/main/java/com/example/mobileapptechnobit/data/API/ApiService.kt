package com.example.mobileapptechnobit.data.API

import com.example.mobileapptechnobit.data.remote.ClockOutRequest
import com.example.mobileapptechnobit.data.remote.EmployeeResponse
import com.example.mobileapptechnobit.data.remote.Presensi
import com.example.mobileapptechnobit.data.remote.PresensiResponse
import com.example.mobileapptechnobit.data.remote.Permission
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService{

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("v1/login")
    suspend fun login(@Body request: HashMap<String, String>): Response<ResponseBody>

    @POST("v1/android/request-otp")
    suspend fun requestOtp(@Body email: HashMap<String, String>): Response<ResponseBody>

    @POST("v1/android/reset-password")
    suspend fun resetPassword(@Body request: HashMap<String, String>): Response<ResponseBody>

    @POST("v1/android/verify-otp")
    suspend fun verifyOtp(@Body request: HashMap<String, String>): Response<ResponseBody>

    @PUT("v1/android/change-password")
    suspend fun changePassword(@Header("Authorization") authorization: String, @Body request: HashMap<String, String>): Response<ResponseBody>

    @GET("v1/profile/employee")
    suspend fun fetchEmployeeProfile(@Header("Authorization") authorization: String): Response<EmployeeResponse>

    @PUT("v1/profile/employee")
    suspend fun updateEmployeeProfile(@Header("Authorization") authorization: String, @Body request: Map<String, String>): Response<Unit>

    @GET("v1/profile/user")
    suspend fun getUserProfile(@Header("Authorization") token: String): Response<UserProfileResponse>

    @POST("v1/android/presensi")
    suspend fun sendPresensi(@Header("Authorization") token: String, @Body requestBody: Presensi): Response<PresensiResponse> // Untuk Clock In

    @POST("v1/android/presensi")
    suspend fun sendClockOutPresensi(@Header("Authorization") token: String, @Body requestBody: ClockOutRequest): Response<String>

    @GET("")
    suspend fun fetchPermission(@Header("Authorization") authorization: String): Response<Permission>

    @GET("")
    suspend fun getPermission(@Header("Authorization") authorization: String): Response<Permission>

    @DELETE("")
    suspend fun deletePermission(@Header("Authorization") authorization: String): Response<Permission>
}