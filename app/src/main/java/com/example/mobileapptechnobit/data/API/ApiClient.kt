package com.example.mobileapptechnobit.data.API

import android.annotation.SuppressLint
import android.content.Context
import com.example.mobileapptechnobit.data.remote.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

@SuppressLint("StaticFieldLeak")

object ApiClient {

    private lateinit var context: Context

    fun init(context: Context) {
        ApiClient.context = context
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val BASE_URL = "https://app.arunikaprawira.com/api/"

    private val ALTERNATE_URL = "https://103.82.240.20/api/"

    private val LOCAL_URL = "http://10.0.2.2:8000/"

    private val LOCAL_URL2 = "http://192.168.100.114:8000/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}