package com.example.mobileapptechnobit.data.remote

import com.google.gson.annotations.SerializedName

data class EmployeeResponse(
//    val message: String,
//    val status: Int,
//    val success: Boolean,
    val data: Employees
)

data class Employees (
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    val fullname: String,
    val nickname: String,
    val phone: String,
    @SerializedName("emergency_contact") val emergencyContact: String,
    @SerializedName("emergency_phone") val emergencyPhone: String,
    val gender: String,
    @SerializedName("birth_date") val birthDate: String,
    @SerializedName("birth_place") val birthPlace: String,
    @SerializedName("marital_status") val maritalStatus: String,
    val nationality: String,
    val religion: String,
    @SerializedName("blood_type") val bloodType: String,
    @SerializedName("id_number") val idNumber: String,
    @SerializedName("tax_number") val taxNumber: String,
    @SerializedName("social_security_number") val socialSecurityNumber: String,
    @SerializedName("health_insurance_number") val healthInsuranceNumber: String,
    val address: String,
    val city: String,
    val province: String,
    @SerializedName("postal_code") val postalCode: String,
    val department: String,
    val position: String,
    @SerializedName("employment_status") val employmentStatus: String,
    @SerializedName("hire_date") val hireDate: String,
    @SerializedName("contract_end_date") val contractEndDate: String,
    val salary: String,
    @SerializedName("bank_name") val bankName: String,
    @SerializedName("bank_account_number") val bankAccountNumber: String,
    val active: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)