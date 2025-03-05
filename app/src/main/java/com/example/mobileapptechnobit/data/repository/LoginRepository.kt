package com.example.mobileapptechnobit.repository

import android.content.Context
import com.example.mobileapptechnobit.data.remote.ApiClient
import org.json.JSONObject

class LoginRepository(context: Context) {
    private val apiClient = ApiClient

    init {
        apiClient.initialize(context)
    }

    fun login(email: String, password: String, onSuccess: (JSONObject) -> Unit, onError: (String) -> Unit) {
        apiClient.login(email, password, onSuccess, onError)
    }
}