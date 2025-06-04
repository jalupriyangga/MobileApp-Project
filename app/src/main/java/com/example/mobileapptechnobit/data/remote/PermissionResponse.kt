package com.example.mobileapptechnobit.data.remote

import android.os.Parcelable
import androidx.compose.runtime.Updater
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PermissionResponse (
//    val status: Int,
//    val success: Boolean,
//    val message: String,
    val data: List<PermissionResponseItem>
)

@Parcelize
data class PermissionResponseItem(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("employee_id") val employeeId: Int? = 0,
    @SerializedName("employee_name") val employeeName: String = "",
    @SerializedName("alternate_id") val alternateId: Int? = 0,
    @SerializedName("alternate_name") val alternateName: String = "",
    @SerializedName("employee_schedule_id") val employeeScheduleId: Int? = 0,
    @SerializedName("details_emp_schedule") val DetailEmpSchedule: String = "",
    @SerializedName("alternate_schedule_id") val alternateScheduleId: Int? = 0,
    @SerializedName("details_alt_schedule") val DetailAltSchedule: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("permission") val permission: String = "",
    @SerializedName("employee_is_confirmed") val employeeIsConfirmed: String = "",
    @SerializedName("alternate_is_confirmed") val alternateIsConfirmed: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("reason") val reason: String = "",
    @SerializedName("created_at") val created_at: String = "",
    @SerializedName("updated_at") val updated_at: String = ""
) : Parcelable

data class AlternatePermissionResponse (

    val data: List<AlternatePermissionResponseItem>

)

@Parcelize
data class AlternatePermissionResponseItem(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("employee_id") val employeeId: Int? = 0,
    @SerializedName("alternate_id") val alternateId: Int? = 0,
    @SerializedName("employee_schedule_id") val employeeScheduleId: Int? = 0,
    @SerializedName("details_emp_schedule") val DetailEmpSchedule: String = "",
    @SerializedName("details_alt_schedule") val DetailAltSchedule: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("permission") val permission: String = "",
    @SerializedName("reason") val reason: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("employee_is_confirmed") val employeeIsConfirmed: String = "",
    @SerializedName("alternate_is_confirmed") val alternateIsConfirmed: String = "",
    @SerializedName("employee_name") val requesterName: String = ""
) : Parcelable