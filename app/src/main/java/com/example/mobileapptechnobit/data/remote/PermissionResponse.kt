package com.example.mobileapptechnobit.data.remote

import android.os.Parcelable
import androidx.compose.runtime.Updater
import kotlinx.parcelize.Parcelize

data class PermissionResponse (
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: List<PermissionResponseItem>
)

@Parcelize
data class PermissionResponseItem(
    val id: String? = "",
    val employee_id: String = "",
    val alternate_id: String = "",
    val employee_schedule_id: String = "",
    val alternate_schedule_id: String = "",
    val date: String = "",
    val permission: String = "",
    val employee_is_confirmed: String = "",
    val alternate_is_confirmed: String = "",
    val status: String = "",
    val type: String = "",
    val reason: String = "",
    val created_at: String = "",
    val update_at: String = ""
) : Parcelable