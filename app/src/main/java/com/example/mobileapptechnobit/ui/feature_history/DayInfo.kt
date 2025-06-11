package com.example.mobileapptechnobit.ui.feature_history

data class DayInfo(
    val name: String,
    val date: Int,
    val month: String,
    val dateString: String,
    val isToday: Boolean,
    val apiDateFormat: String
)