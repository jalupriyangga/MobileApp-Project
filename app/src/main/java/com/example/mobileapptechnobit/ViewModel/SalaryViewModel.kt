package com.example.mobileapptechnobit.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapptechnobit.data.API.ApiClient
import com.example.mobileapptechnobit.data.remote.SalaryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SalaryViewModel : ViewModel() {

    private val _salaryData = MutableStateFlow<SalaryItem?>(null)
    val salaryData: StateFlow<SalaryItem?> = _salaryData

    fun fetchSalary(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getSalaryDetail("Bearer $token")
                if (response.isSuccessful) {
                    val salaryList = response.body()?.data
                    if (!salaryList.isNullOrEmpty()) {
                        _salaryData.value = salaryList.first() // Auto ambil gaji terbaru
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}