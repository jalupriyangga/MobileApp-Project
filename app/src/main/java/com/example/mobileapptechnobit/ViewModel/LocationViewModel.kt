package com.example.mobileapptechnobit.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapptechnobit.data.remote.CompanyLocation
import com.example.mobileapptechnobit.data.repository.LocationRepository
import kotlinx.coroutines.launch

class LocationViewModel(private val repository: LocationRepository, private val context: Context): ViewModel() {

    private val _LocationMessage = MutableLiveData<String>()
    val LocationMessage: LiveData<String> get() = _LocationMessage

    private val _companyLocation = MutableLiveData<CompanyLocation>()
    val companyLocation: LiveData<CompanyLocation> get() = _companyLocation

//    val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//    val authToken = sharedPref.getString("AUTH_TOKEN", null)
//
//    init {
//        getCompanyLocation(authToken.toString())
////        Log.d("location viewModel", "getCompanyLocation DIPANGGIL")
//    }

    fun getCompanyLocation(token: String){

        viewModelScope.launch {
            Log.d("location view model", "getCompanyLocation DIPANGGIL")
            try {
                val response = repository.getCompanyLocation(token)
                if (response.isSuccessful) {
                    Log.d("CompanyLocation", "Response: success, ${response.body().toString()}")
                    _LocationMessage.postValue("Data perizinan berhasil di ambil")
                    _companyLocation.postValue(response.body())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("CompanyLocation", "Failed: ${response.code()}, Error: $errorBody")
                    _LocationMessage.postValue("Gagal: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("getCompanyLocation", "Error: ${e.message}")
                _LocationMessage.postValue("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }
}