package com.example.mobileapptechnobit.ViewModel

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobileapptechnobit.data.remote.HistoryPatroliResponseItem
import com.example.mobileapptechnobit.data.remote.HistoryPresensiResponseItem
import com.example.mobileapptechnobit.data.repository.HistoryRepository
import com.example.mobileapptechnobit.ui.feature_history.DayInfo
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
    private val _originalHistoryPresensiItems = MutableStateFlow<List<HistoryPresensiResponseItem>>(emptyList())
    private val _originalHistoryPatroliItems = MutableStateFlow<List<HistoryPatroliResponseItem>>(emptyList())

    // Filtered data that's displayed in the UI
    private val _historyPresensiItems = MutableStateFlow<List<HistoryPresensiResponseItem>>(emptyList())
    val historyPresensiItems: StateFlow<List<HistoryPresensiResponseItem>> = _historyPresensiItems

    private val _historyPatroliItems = MutableStateFlow<List<HistoryPatroliResponseItem>>(emptyList())
    val historyPatroliItems: StateFlow<List<HistoryPatroliResponseItem>> = _historyPatroliItems

    // Separate loading states for each tab
    private val _isLoadingPresensi = MutableStateFlow(false)
    val isLoadingPresensi: StateFlow<Boolean> = _isLoadingPresensi

    private val _isLoadingPatroli = MutableStateFlow(false)
    val isLoadingPatroli: StateFlow<Boolean> = _isLoadingPatroli

    // Combined loading state for backward compatibility
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedDays = MutableStateFlow("")
    val selectedDay: StateFlow<String> = _selectedDays

    // Store current week days information
    private val _weekDays = MutableStateFlow<List<DayInfo>>(emptyList())

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
        } else {
            // If today is not in current week, select first day
            _selectedDays.value = daysInfo.firstOrNull()?.name ?: "Senin"
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
            _isLoadingPresensi.value = true
            _isLoading.value = true
            _error.value = null

            try {
                Log.d("HistoryViewModel", "Fetching presensi with token: ${token.take(10)}...")
                val resultHistoryPresensi = repository.fetchHistoryPresensi(token)

                if (resultHistoryPresensi != null) {
                    // Store the original data
                    _originalHistoryPresensiItems.value = resultHistoryPresensi
                    Log.d("HistoryViewModel", "Fetched ${resultHistoryPresensi.size} presensi history items")

                    // Apply filters
                    applyFilters()
                } else {
                    Log.e("HistoryViewModel", "Fetching presensi history returned null")
                    _error.value = "Gagal memuat data presensi"
                }
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Error fetching presensi history", e)
                _error.value = e.message ?: "Terjadi kesalahan saat memuat data presensi"
            } finally {
                _isLoadingPresensi.value = false
                // Only set main loading to false if both are not loading
                if (!_isLoadingPatroli.value) {
                    _isLoading.value = false
                }
            }
        }
    }

    fun fetchHistoryPatroli(token: String) {
        viewModelScope.launch {
            _isLoadingPatroli.value = true
            _isLoading.value = true
            _error.value = null

            try {
                Log.d("HistoryViewModel", "Fetching patroli with token: ${token.take(10)}...")
                val result = repository.fetchHistoryPatroli(token)

                result.fold(
                    onSuccess = { patroliList ->
                        // Store the original data
                        _originalHistoryPatroliItems.value = patroliList
                        Log.d("HistoryViewModel", "Fetched ${patroliList.size} patroli history items")

                        // Debug: Print first few items
                        patroliList.take(3).forEach { item ->
                            Log.d("HistoryViewModel", "Patroli item: ID=${item.id}, Date=${item.createdAt}, Status=${item.status}")
                        }

                        // Apply filters
                        applyFiltersPatroli()
                    },
                    onFailure = { exception ->
                        Log.e("HistoryViewModel", "Fetching patroli history failed: ${exception.message}")
                        _error.value = exception.message ?: "Gagal memuat data patroli"
                    }
                )
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Error fetching patroli history", e)
                _error.value = e.message ?: "Terjadi kesalahan saat memuat data patroli"
            } finally {
                _isLoadingPatroli.value = false
                // Only set main loading to false if both are not loading
                if (!_isLoadingPresensi.value) {
                    _isLoading.value = false
                }
            }
        }
    }

    private fun applyFilters() {
        // Find the API date format for the selected day
        val selectedDayInfo = _weekDays.value.find { it.name == _selectedDays.value }
        val selectedApiDate = selectedDayInfo?.apiDateFormat

        Log.d("HistoryPresViewModel", "Filtering presensi for day: ${_selectedDays.value}, API date: $selectedApiDate")

        val filtered = _originalHistoryPresensiItems.value.filter {
            Log.d("HistoryPresViewModel", "Processing presensi item date: ${it.tanggal}")

            // Check if date matches selected day
            val isCorrectDay = if (selectedApiDate != null) {
                // Direct comparison with API date format (yyyy-MM-dd)
                it.tanggal == selectedApiDate
            } else {
                // Fallback to the old method if something went wrong
                val dayFromDate = extractDayFromDateString(it.tanggal)
                dayFromDate == _selectedDays.value
            }

            // For presensi tab, we typically don't filter by location type
            // but you can uncomment below if needed
            /*
            val isCorrectType = if (_selectedTab.value == 0) {
                // Presensi tab - show items without "patrol" in location
                !it.lokasi.contains("patrol", ignoreCase = true)
            } else {
                true // Show all for other tabs
            }
            */

            Log.d("HistoryPresViewModel", "Presensi item ${it.tanggal}: isCorrectDay=$isCorrectDay")
            isCorrectDay // && isCorrectType
        }

        Log.d("HistoryPresViewModel", "Filtered to ${filtered.size} presensi items for day: ${_selectedDays.value}")
        _historyPresensiItems.value = filtered
    }

    private fun applyFiltersPatroli() {
        val selectedDayInfo = _weekDays.value.find { it.name == _selectedDays.value }
        val selectedApiDate = selectedDayInfo?.apiDateFormat

        Log.d("HistoryPatrViewModel", "Filtering patroli for day: ${_selectedDays.value}, API date: $selectedApiDate")
        Log.d("HistoryPatrViewModel", "Original patroli items count: ${_originalHistoryPatroliItems.value.size}")

        val filtered = _originalHistoryPatroliItems.value.filter { patroliItem ->
            Log.d("HistoryPatrViewModel", "Processing patroli item: ID=${patroliItem.id}, Date=${patroliItem.createdAt}")

            val isCorrectDay = if (!patroliItem.createdAt.isNullOrEmpty() && selectedApiDate != null) {
                try {
                    // Try multiple date formats since API might return different formats
                    val possibleFormats = listOf(
                        "yyyy-MM-dd HH:mm:ss",
                        "yyyy-MM-dd'T'HH:mm:ss",
                        "yyyy-MM-dd'T'HH:mm:ss'Z'",
                        "yyyy-MM-dd"
                    )

                    var parsedDate: String? = null

                    for (format in possibleFormats) {
                        try {
                            val inputFormat = SimpleDateFormat(format, Locale("id", "ID"))
                            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
                            val date = inputFormat.parse(patroliItem.createdAt)
                            parsedDate = outputFormat.format(date!!)
                            break
                        } catch (e: Exception) {
                            continue // Try next format
                        }
                    }

                    val result = parsedDate == selectedApiDate
                    Log.d("HistoryPatrViewModel", "Date comparison: ${patroliItem.createdAt} -> $parsedDate == $selectedApiDate -> $result")
                    result
                } catch (e: Exception) {
                    Log.e("HistoryPatrViewModel", "Date parsing error for ${patroliItem.createdAt}: ${e.message}")
                    false
                }
            } else {
                Log.w("HistoryPatrViewModel", "Empty createdAt or selectedApiDate for item ID: ${patroliItem.id}")
                false
            }

            Log.d("HistoryPatrViewModel", "Patroli item ${patroliItem.id}: isCorrectDay=$isCorrectDay")
            isCorrectDay
        }

        Log.d("HistoryPatrViewModel", "Filtered to ${filtered.size} patroli items for day: ${_selectedDays.value}")
        _historyPatroliItems.value = filtered
    }

    // Keep your existing method for backward compatibility
    private fun extractDayFromDateString(dateString: String): String {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
            val outputFormat = SimpleDateFormat("EEEE", Locale("id", "ID"))
            val date = inputFormat.parse(dateString)

            if (date != null) {
                return outputFormat.format(date).replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
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
