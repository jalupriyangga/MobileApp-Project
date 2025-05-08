package com.example.mobileapptechnobit.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapptechnobit.data.remote.Permission
import com.example.mobileapptechnobit.data.repository.PermissionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PermissionViewModel(private val repository: PermissionRepository): ViewModel() {

    private val _permissionMessage = MutableLiveData<String>()
    val permissionMessage: LiveData<String> get() = _permissionMessage

    private val _permissionList = MutableStateFlow<List<Permission>>(emptyList())
    val permissionList: StateFlow<List<Permission>> = _permissionList

    fun fetchPermission(token: String, currentPassword: String, newPassword: String){

        val authToken = "Bearer $token"
        Log.d("ProfileRepository", "Auth Token: $authToken")

        viewModelScope.launch {
            try {
                val permissionList = repository.fetchPermission(authToken)
                if(permissionList.isSuccessful){
                    val responseBody = permissionList.body()?.toString() ?: "Empty response"
                    Log.d("change password", "Response: $responseBody")
                    _permissionMessage.postValue("Password berhasil diperbarui!")
                } else{
                    val errorBody = permissionList.errorBody()?.string() ?: "Unknown error"
                    Log.e("change password", "Failed: ${permissionList.code()}, Error: $errorBody")
                    _permissionMessage.postValue("Gagal: $errorBody")
                }
            } catch (e: Exception){
                Log.e("change Password", "Exception: ${e.message}")
                _permissionMessage.postValue("Error: ${e.message}")
            }
        }
    }
}