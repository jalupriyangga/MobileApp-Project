package com.example.mobileapptechnobit.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapptechnobit.data.repository.ProfileRepository
import com.example.mobileapptechnobit.data.remote.Employees
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {
    private val _employeesProfile = MutableStateFlow<Employees?>(null)
    val employeesProfile: StateFlow<Employees?> = _employeesProfile

    var authToken: String = ""

    fun fetchEmployeesProfile(token: String) {
        authToken = token
        viewModelScope.launch {
            try {
                Log.d("API_REQUEST", "Fetching profile with token: $token")
                val profile = repository.fetchEmployeesProfile(token)
                if (profile != null) {
                    _employeesProfile.value = profile
                    Log.d("API_REQUEST", "Profile fetched successfully: $profile")
                } else {
                    Log.e("API_REQUEST", "Profile fetch failed, profile is null")
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
                        updatedAt = "",
                        company_id = 0
                    )
                }
            } catch (e: Exception) {
                Log.e("API_REQUEST", "Error fetching profile", e)
            }
        }
    }

    fun updateProfile(
        token: String,
        fullname: String?,
        nickname: String?,
        phone: String?,
        gender: String?,
        birthDate: String?,
        religion: String?,
        bloodType: String?,
        address: String?,
        emergencyPhone: String?
    ) {
        viewModelScope.launch {
            try {
                Log.d("API_REQUEST", "Updating profile with token: $token")
                Log.d("API_REQUEST", "Data to update: FullName=$fullname, Nickname=$nickname, Phone=$phone, Gender=$gender, BirthDate=$birthDate, Religion=$religion, BloodType=$bloodType, Address=$address, EmergencyPhone=$emergencyPhone")

                val profile = _employeesProfile.value
                val updateFields = mutableMapOf<String, String>()

                if (!fullname.isNullOrEmpty()) updateFields["fullname"] = fullname else profile?.fullname?.let { updateFields["fullname"] = it }
                if (!nickname.isNullOrEmpty()) updateFields["nickname"] = nickname else profile?.nickname?.let { updateFields["nickname"] = it }
                if (!phone.isNullOrEmpty()) updateFields["phone"] = phone else profile?.phone?.let { updateFields["phone"] = it }
                if (!gender.isNullOrEmpty()) updateFields["gender"] = gender else profile?.gender?.let { updateFields["gender"] = it }
                if (!birthDate.isNullOrEmpty()) updateFields["birth_date"] = birthDate else profile?.birthDate?.let { updateFields["birth_date"] = it }
                if (!religion.isNullOrEmpty()) updateFields["religion"] = religion else profile?.religion?.let { updateFields["religion"] = it }
                if (!bloodType.isNullOrEmpty()) updateFields["blood_type"] = bloodType else profile?.bloodType?.let { updateFields["blood_type"] = it }
                if (!address.isNullOrEmpty()) updateFields["address"] = address else profile?.address?.let { updateFields["address"] = it }
                if (!emergencyPhone.isNullOrEmpty()) updateFields["emergency_phone"] = emergencyPhone else profile?.emergencyPhone?.let { updateFields["emergency_phone"] = it }

                Log.d("API_REQUEST", "Update fields prepared: $updateFields")

                val updateSuccess = repository.updateProfile(token, updateFields)
                if (updateSuccess) {
                    Log.d("API_REQUEST", "Profile update successful, fetching updated profile")
                    fetchEmployeesProfile(token) // Refresh profile data after update
                    Log.d("API_REQUEST", "Profile updated and fetched successfully")
                } else {
                    Log.e("API_REQUEST", "Profile update failed")
                }
            } catch (e: Exception) {
                Log.e("API_REQUEST", "Error updating profile", e)
            }
        }
    }
}