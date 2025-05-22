package com.example.mobileapptechnobit.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapptechnobit.data.remote.PermissionResponseItem
import com.example.mobileapptechnobit.data.repository.PermissionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PermissionViewModel(private val repository: PermissionRepository, private val context: Context): ViewModel() {

    private val _permissionMessage = MutableLiveData<String>()
    val permissionMessage: LiveData<String> get() = _permissionMessage

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private val _deleteMessage = MutableLiveData<String>()
    val deleteMessage: LiveData<String> get() = _deleteMessage

    private val _permissionItems = MutableLiveData<List<PermissionResponseItem>>()
    val permissionItems: LiveData<List<PermissionResponseItem>> get() = _permissionItems

    private val _fetchMessage = MutableLiveData<String>()
    val fetchMessage: LiveData<String> get() = _fetchMessage

    fun sendPermission(token: String, date: String, permission: String, reason: String) {

        val authToken = "Bearer $token"

        viewModelScope.launch {
            try {
                val response = repository.sendPermission(token, date, permission, reason)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string() ?: "Empty response"
                    Log.d("SendPermission", "Response: $responseBody")
                    _permissionMessage.postValue("Izin berhasil dibuat")
                    withContext(Dispatchers.Main) {
                        _isSuccess.value = true // langsung update tanpa delay
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("SendPermission", "Failed: ${response.code()}, Error: $errorBody")
                    _permissionMessage.postValue("Gagal: ${response.code()} - ${response.message()}")
                    _isSuccess.value = false
                }
            } catch (e: Exception) {
                Log.e("ForgotPassword", "Error: ${e.message}")
                _permissionMessage.postValue("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }

    fun deletePermission(id: String) {

        viewModelScope.launch {
            try {
                val response = repository.deletePermission(id)
                if (response.isSuccessful) {
                    Log.d("delete permission", "Response: success")
                    _deleteMessage.postValue("Izin berhasil dihapus")
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("delete permission", "Failed: ${response.code()}, Error: $errorBody")
                    _deleteMessage.postValue("Gagal: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("delete permission", "Error: ${e.message}")
                _deleteMessage.postValue("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }

    fun fetchPermission(token: String) {

        viewModelScope.launch {
            try {
                val response = repository.fetchPermission(token)
                if (response.isSuccessful) {
                    Log.d("fetch permission", "Response: success")
                    _fetchMessage.postValue("Data perizinan berhasil di")
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("fetch permission", "Failed: ${response.code()}, Error: $errorBody")
                    _fetchMessage.postValue("Gagal: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("fetch permission", "Error: ${e.message}")
                _fetchMessage.postValue("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }

}