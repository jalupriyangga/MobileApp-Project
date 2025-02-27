package com.example.sisteminformasimenejemensatpam.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.sisteminformasimenejemensatpam.data.ApiClient
import com.example.sisteminformasimenejemensatpam.data.User

class UserRepository {
    private val api = ApiClient.instance

    suspend fun getUsers(): List<User> {
        return api.getUsers()
    }

    suspend fun updateUser(user: User){
        api.updateUser(user.id, user)
    }
}