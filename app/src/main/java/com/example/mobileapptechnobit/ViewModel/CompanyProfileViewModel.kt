package com.example.mobileapptechnobit.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapptechnobit.data.remote.CompanyProfileResponse
import com.example.mobileapptechnobit.data.repository.CompanyProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CompanyProfileViewModel : ViewModel() {
    private val repository = CompanyProfileRepository()

    private val _companyProfile = MutableStateFlow<CompanyProfileResponse?>(null)
    val companyProfile: StateFlow<CompanyProfileResponse?> = _companyProfile

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchCompanyProfile(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.fetchCompanyProfile(token)
                if (response.isSuccessful && response.body() != null) {
                    _companyProfile.value = response.body()
                } else {
                    _error.value = "Profil perusahaan tidak ditemukan"
                }
            } catch (e: Exception) {
                _error.value = "Gagal mengambil data: ${e.localizedMessage}"
            }
        }
    }
}