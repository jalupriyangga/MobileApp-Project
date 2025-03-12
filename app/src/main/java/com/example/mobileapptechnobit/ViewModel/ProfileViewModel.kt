package com.example.mobileapptechnobit.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobileapptechnobit.data.repository.ProfileRepository
import com.example.mobileapptechnobit.data.remote.Employees
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {
    private val _employeesProfile = MutableStateFlow<Employees?>(null)
    val employeesProfile: StateFlow<Employees?> = _employeesProfile

    fun fetchEmployeesProfile(token: String) {
        viewModelScope.launch {
            try {
                Log.d("API_REQUEST", "Fetching profile with token: $token")

                val profile = repository.fetchEmployeesProfile(token)
                if (profile != null) {
                    _employeesProfile.value = profile
                } else {
                    // Jika response kosong, set nilai berbeda agar UI bisa menampilkan pesan error
                    _employeesProfile.value = Employees(
                        id = 0,
                        userId = 0,
                        fullname = "Data tidak ditemukan",
                        nickname = "",
                        phone = "",
                        emergencyContact = "",
                        emergencyPhone = "",
                        gender = "",
                        birthDate = "",
                        birthPlace = "",
                        maritalStatus = "",
                        nationality = "",
                        religion = "",
                        bloodType = "",
                        idNumber = "",
                        taxNumber = "",
                        socialSecurityNumber = "",
                        healthInsuranceNumber = "",
                        address = "",
                        city = "",
                        province = "",
                        postalCode = "",
                        department = "",
                        position = "",
                        employmentStatus = "",
                        hireDate = "",
                        contractEndDate = "",
                        salary = "",
                        bankName = "",
                        bankAccountNumber = "",
                        active = 0,
                        createdAt = "",
                        updatedAt = ""
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class ProfileViewModelFactory(private val repository: ProfileRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}