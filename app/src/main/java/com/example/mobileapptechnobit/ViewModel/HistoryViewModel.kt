package com.example.mobileapptechnobit.ViewModel

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobileapptechnobit.data.remote.HistoryPatroliResponseItem
import com.example.mobileapptechnobit.data.remote.HistoryPresensiResponseItem
import com.example.mobileapptechnobit.data.repository.HistoryRepository
import com.example.mobileapptechnobit.ui.DayInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryViewModelFactory(
    private val repository: HistoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {
    // Original unfiltered data
    private val _originalHistoryPresensiItems = MutableStateFlow< List<HistoryPresensiResponseItem>>(emptyList())
    private val _originalHistoryPatroliItems = MutableStateFlow<Result<List<HistoryPatroliResponseItem>>>(
        Result.success(emptyList())
    )

    // Filtered data that's displayed in the UI
    private val _historyPresensiItems = MutableStateFlow<List<HistoryPresensiResponseItem>>(emptyList())
    val historyPresensiItems: StateFlow<List<HistoryPresensiResponseItem>> = _historyPresensiItems

    private val _historyPatroliItems = MutableStateFlow<List<HistoryPatroliResponseItem>>(emptyList())
    val historyPatroliItems: StateFlow<List<HistoryPatroliResponseItem>> = _historyPatroliItems

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedDays = MutableStateFlow("")
    val selectedDay: StateFlow<String> = _selectedDays

    // Store current week days information
    private val _weekDays = MutableStateFlow<List<DayInfo>>(emptyList())
//    val weekDays: StateFlow<List<DayInfo>> = _weekDays

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab

    init {
        initializeWeekDays()
    }

    private fun initializeWeekDays() {
        val currentDate = Calendar.getInstance()
        val currentDayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK)

        val dayNames = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
        val daysInfo = mutableListOf<DayInfo>()

        // Find Monday of current week
        val monday = Calendar.getInstance()
        monday.time = currentDate.time

        // In Calendar.DAY_OF_WEEK, Monday is 2, Sunday is 1
        val daysUntilMonday = if (currentDayOfWeek == Calendar.SUNDAY) 6 else currentDayOfWeek - Calendar.MONDAY
        monday.add(Calendar.DAY_OF_MONTH, -daysUntilMonday)

        for (i in 0..6) {
            val day = Calendar.getInstance()
            day.time = monday.time
            day.add(Calendar.DAY_OF_MONTH, i)

            val dayName = dayNames[i]
            val dayNumber = day.get(Calendar.DAY_OF_MONTH)
            val monthShort = getIndonesianMonthShort(day.get(Calendar.MONTH))

            daysInfo.add(
                DayInfo(
                    name = dayName,
                    date = dayNumber,
                    month = monthShort,
                    dateString = "$dayNumber $monthShort",
                    isToday = day.get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR)
                            && day.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR),
                    apiDateFormat = getApiDateFormat(day) // yyyy-MM-dd format for API
                )
            )
        }

        _weekDays.value = daysInfo

        // Set the default selected day to today if it's in the current week
        val today = daysInfo.find { it.isToday }
        if (today != null) {
            _selectedDays.value = today.name
        }
    }

    fun setSelecteDays(day: String) {
        Log.d("HistoryViewModel", "Atur ke hari: $day")
        _selectedDays.value = day
        applyFilters()
        applyFiltersPatroli()
    }

    fun setSelectedTab(tabIndex: Int) {
        _selectedTab.value = tabIndex
        applyFilters()
        applyFiltersPatroli()
    }

    fun fetchHistoryPresensi(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val resultHistoryPresensi = repository.fetchHistoryPresensi(token)
                if (resultHistoryPresensi != null) {
                    // Store the original data
                    _originalHistoryPresensiItems.value = resultHistoryPresensi
                    Log.d("HistoryViewModel", "Fetched ${resultHistoryPresensi.size} history items")

                    // Apply filters
                    applyFilters()
                } else {
                    Log.e("HistoryViewModel", "Fetching history returned null with token: $token")
                    _error.value = "Failed to load history data"
                }
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Error fetching history", e)
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchHistoryPatroli(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val resultHistoryPatroli = repository.fetchHistoryPatroli(token)
                if (resultHistoryPatroli != null) {
                    // Store the original data
                    _originalHistoryPatroliItems.value = resultHistoryPatroli
                    Log.d("HistoryViewModel", "Fetched ${resultHistoryPatroli.isSuccess } history items")

                    // Apply filters
                    applyFiltersPatroli()
                } else {
                    Log.e("HistoryViewModel", "Fetching history returned null with token: $token")
                    _error.value = "Failed to load history data Patroli"
                }
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Error fetching history", e)
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun applyFilters() {
        // Find the API date format for the selected day
        val selectedDayInfo = _weekDays.value.find { it.name == _selectedDays.value }
        val selectedApiDate = selectedDayInfo?.apiDateFormat

        Log.d("HistoryViewModel", "Filtering for day: ${_selectedDays.value}, API date: $selectedApiDate")

        val filtered = _originalHistoryPresensiItems.value.filter {
            Log.d("HistoryViewModel", "Processing item date: ${it.tanggal}")

            // Check if date matches selected day
            val isCorrectDay = if (selectedApiDate != null) {
                // Direct comparison with API date format (yyyy-MM-dd)
                it.tanggal == selectedApiDate
            } else {
                // Fallback to the old method if something went wrong
                val dayFromDate = extractDayFromDateString(it.tanggal)
                dayFromDate == _selectedDays.value
            }

            val isCorrectType = if (_selectedTab.value == 0) {
                // Presensi tab - show items without "patrol" in location
                !it.lokasi.contains("patrol", ignoreCase = true)
            } else {
                // Patrol tab - show only items with "patrol" in location
                it.lokasi.contains("patrol", ignoreCase = true)
            }

            Log.d("HistoryViewModel", "Item ${it.tanggal}: isCorrectDay=$isCorrectDay, isCorrectType=$isCorrectType")
            isCorrectDay && isCorrectType
        }

        Log.d("HistoryViewModel", "Filtered to ${filtered.size} items for day: ${_selectedDays.value}, tab: ${_selectedTab.value}")
        _historyPresensiItems.value = filtered
    }

    private fun applyFiltersPatroli(){
        val selectedDayInfo = _weekDays.value.find { it.name == _selectedDays.value }
        val selectedApiDate = selectedDayInfo?.apiDateFormat

        Log.d("HistoryViewModel", "Filtering for day: ${_selectedDays.value}, API date: $selectedApiDate")

        val filtered = _originalHistoryPatroliItems.value.getOrNull()?.filter {
            Log.d("HistoryViewModel", "Processing item date: ${it.createdAt}")
            val isCorrectDay = if (!it.createdAt.isNullOrEmpty() && selectedApiDate != null) {
                try {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))
                    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
                    val date = inputFormat.parse(it.createdAt)
                    val formattedDate = outputFormat.format(date!!)
                    formattedDate == selectedApiDate
                } catch (e: Exception) {
                    Log.e("HistoryViewModel", "Date parsing error: ${e.message}")
                    false
                }
            } else {
                false
            }

            Log.d("HistoryViewModel", "Item ${it.createdAt}: isCorrectDay=$isCorrectDay")
            isCorrectDay
        } ?: emptyList()

        Log.d("HistoryViewModel", "Filtered to ${filtered.size} items for day: ${_selectedDays.value}, tab: ${_selectedTab.value}")
        _historyPatroliItems.value = filtered
    }

    // Keep your existing method for backward compatibility
    private fun extractDayFromDateString(dateString: String): String {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
            val outputFormat = SimpleDateFormat("EEEE", Locale("id", "ID"))
            val date = inputFormat.parse(dateString)

            if (date != null) {
                return outputFormat.format(date).capitalize()
            }
        } catch (e: Exception) {
            Log.e("HistoryViewModel", "Error extracting day from date: $dateString", e)
        }
        return "Senin" // Default
    }

    private fun getIndonesianMonthShort(month: Int): String {
        return when (month) {
            Calendar.JANUARY -> "Jan"
            Calendar.FEBRUARY -> "Feb"
            Calendar.MARCH -> "Mar"
            Calendar.APRIL -> "Apr"
            Calendar.MAY -> "Mei"
            Calendar.JUNE -> "Jun"
            Calendar.JULY -> "Jul"
            Calendar.AUGUST -> "Agu"
            Calendar.SEPTEMBER -> "Sep"
            Calendar.OCTOBER -> "Okt"
            Calendar.NOVEMBER -> "Nov"
            Calendar.DECEMBER -> "Des"
            else -> ""
        }
    }

    private fun getApiDateFormat(calendar: Calendar): String {
        val year = calendar.get(Calendar.YEAR)
        // Add 1 to month because Calendar months are 0-indexed
        val month = (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        val day = calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
        return "$year-$month-$day"
    }
}