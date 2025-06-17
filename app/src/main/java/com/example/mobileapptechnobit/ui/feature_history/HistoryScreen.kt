package com.example.mobileapptechnobit.ui.feature_history

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapptechnobit.ViewModel.HistoryViewModel
import com.example.mobileapptechnobit.ViewModel.HistoryViewModelFactory
import com.example.mobileapptechnobit.ViewModel.ProfileViewModel
import com.example.mobileapptechnobit.ViewModel.ProfileViewModelFactory
import com.example.mobileapptechnobit.data.API.ApiClient.apiService
import com.example.mobileapptechnobit.data.remote.HistoryPatroliResponseItem
import com.example.mobileapptechnobit.data.repository.HistoryRepository
import com.example.mobileapptechnobit.data.repository.ProfileRepository
import com.example.mobileapptechnobit.ui.component.BottomNavigationBar
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    navCtrl: NavController,
    token: String
) {
    val context = LocalContext.current
    val repository = ProfileRepository(context)
    val viewModelProfile: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(repository))
    val employeeProfile by viewModelProfile.employeesProfile.collectAsState()
    val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    // State untuk mengelola token yang valid
    var validToken by remember { mutableStateOf<String?>(null) }
    var isTokenReady by remember { mutableStateOf(false) }

    // Effect untuk mendapatkan dan memvalidasi token
    LaunchedEffect(token) {
        // Prioritaskan token dari SharedPreferences, fallback ke parameter token
        val authToken = sharedPref.getString("AUTH_TOKEN", null)?.takeIf { it.isNotBlank() }
            ?: token.takeIf { it.isNotBlank() && it != "dummy_token" }

        if (!authToken.isNullOrBlank()) {
            validToken = authToken
            isTokenReady = true
            println("Valid token found: ${authToken.take(10)}...")
            Log.d("DPS", "Token yang valid ditemukan: ${authToken.take(10)}...")
        } else {
            Log.w("HistoryScreen", "No valid token available")
            isTokenReady = false
        }
    }

    // Effect untuk fetch profile hanya ketika token sudah siap
    LaunchedEffect(validToken, isTokenReady) {
        if (isTokenReady && !validToken.isNullOrBlank()) {
            println("Fetching profile with token: ${validToken!!.take(10)}...")
            viewModelProfile.fetchEmployeesProfile(validToken!!)
            Log.d("DPS", "Token yang diterima: ${validToken!!.take(10)}...")
        }
    }

    // Create ViewModel instance
    val factory = remember { HistoryViewModelFactory(HistoryRepository(apiService = apiService)) }
    val viewModel: HistoryViewModel = viewModel(factory = factory)

    // Effect untuk fetch data history hanya ketika token sudah siap
    LaunchedEffect(validToken, isTokenReady) {
        if (isTokenReady && !validToken.isNullOrBlank()) {
            Log.d("HistoryScreen", "Fetching history presensi with token: ${validToken!!.take(10)}...")
            viewModel.fetchHistoryPresensi(validToken!!)

            Log.d("HistoryScreen", "Fetching history patroli with token: ${validToken!!.take(10)}...")
            viewModel.fetchHistoryPatroli(validToken!!)
        }
    }

    // Collect state from ViewModel
    val historyPresensiItems = viewModel.historyPresensiItems.collectAsState()
    Log.d("HistoryScreen", "History presensi items: ${historyPresensiItems.value}")
    val historyPatroliItems = viewModel.historyPatroliItems.collectAsState()
    Log.d("HistoryScreen", "History patroli items: ${historyPatroliItems.value}")
    val selectedDays = viewModel.selectedDay.collectAsState()
    val selectedTab = viewModel.selectedTab.collectAsState()
    val isLoadingPresensi by viewModel.isLoading.collectAsState()
    val isLoadingPatroli by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Debug logs
    LaunchedEffect(historyPresensiItems.value) {
        Log.d("HistoryPresensiScreen", "History presensi items in UI: ${historyPresensiItems.value.size}")
    }
    LaunchedEffect(historyPatroliItems.value) {
        Log.d("HistoryPatroliScreen", "History patroli items in UI: ${historyPatroliItems.value.size}")
    }

    // Tampilkan loading jika token belum siap
    if (!isTokenReady) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Memverifikasi token...",
                    color = Color.Gray
                )
            }
        }
        return
    }

    // Tampilkan error jika tidak ada token valid
    if (validToken.isNullOrBlank()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Token tidak valid. Silakan login kembali.",
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // Navigate ke login screen
                        navCtrl.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                ) {
                    Text("Login Kembali")
                }
            }
        }
        return
    }

    Scaffold(
        topBar = {
            HistoryTopBar(index = { index -> viewModel.setSelectedTab(index) },
                selectedTab = selectedTab.value)
        },
        bottomBar = {
            BottomNavigationBar(index = 1, navCtrl = navCtrl)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HistoryDaysSelector(
                selectedDay = selectedDays.value,
                onDaySelected = { day -> viewModel.setSelecteDays(day) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Content based on loading/error state
            when {
                isLoadingPresensi || isLoadingPatroli -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (selectedTab.value == 0) "Memuat history presensi..." else "Memuat history patroli...",
                                color = Color.Gray
                            )
                        }
                    }
                }
                !error.isNullOrEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.Red
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = error ?: "Terjadi kesalahan",
                                color = Color.Red,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    if (!validToken.isNullOrBlank()) {
                                        if (selectedTab.value == 0) {
                                            viewModel.fetchHistoryPresensi(validToken!!)
                                        } else {
                                            viewModel.fetchHistoryPatroli(validToken!!)
                                        }
                                    }
                                }
                            ) {
                                Text("Coba Lagi")
                            }
                        }
                    }
                }
                selectedTab.value == 0 && historyPresensiItems.value.isEmpty() -> {
                    // Empty state
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "History Presensi di hari ${selectedDays.value} tidak ada",
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                selectedTab.value == 1 && historyPatroliItems.value.isEmpty() -> {
                    // Empty state
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "History Patroli di hari ${selectedDays.value} tidak ada",
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                selectedTab.value == 0 && historyPresensiItems.value.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(historyPresensiItems.value) { presensiItem ->
                            // Validasi data sebelum render
                            if (presensiItem != null && !presensiItem.tanggal.isNullOrBlank()) {
                                val formattedDate = formatDateForDisplay(presensiItem.tanggal)
                                Log.d("HistoryPresensiScreen", "Rendering presensi item: ${presensiItem.tanggal}, ${presensiItem.status}")
                                HistoryPresensiCard(
                                    historyItem = presensiItem,
                                    navController = navCtrl,
                                    formattedDate = formattedDate
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            } else {
                                Log.w("HistoryScreen", "Skipping null or invalid presensi item")
                            }
                        }
                    }
                }
                // Replace this section in your HistoryScreen composable:

                selectedTab.value == 1 && historyPatroliItems.value.isNotEmpty() -> {
                    Log.d(
                        "HistoryPatroliScreen",
                        "Rendering patroli items: ${historyPatroliItems.value.size} items"
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(items = historyPatroliItems.value) { patroliItem: HistoryPatroliResponseItem ->
                            // Pre-validate data before passing to Composable
                            val isValidItem = patroliItem != null && !patroliItem.createdAt.isNullOrBlank()

                            if (isValidItem) {
                                // Do all the processing and validation BEFORE the Composable call
                                val safeCreatedAt = patroliItem.createdAt ?: ""
                                val formattedDate = try {
                                    formatDateTimeForDisplay(safeCreatedAt)
                                } catch (e: Exception) {
                                    Log.e("HistoryScreen", "Error formatting date: $safeCreatedAt", e)
                                    "Tanggal tidak valid"
                                }

                                Log.d("HistoryScreen", "Rendering patroli item: ${patroliItem.createdAt}, ${patroliItem.status ?: "unknown"}")

                                // Now call the Composable with validated data
                                Column {
                                    HistoryPatroliCard(
                                        historyItem = patroliItem,
                                        navController = navCtrl,
                                        formattedDate = formattedDate
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            } else {
                                // Render error state as a separate Composable
                                Log.w("HistoryScreen", "Skipping null or invalid patroli item: $patroliItem")
                                InvalidItemCard()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InvalidItemCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Data tidak dapat ditampilkan",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// Helper function to format date from "2025-05-05" to "Senin 05 Mei 2025" - DENGAN NULL SAFETY
fun formatDateForDisplay(dateString: String?): String {
    return try {
        // Tambahkan null check
        if (dateString.isNullOrBlank()) {
            return "Tanggal tidak tersedia"
        }

        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID")).apply {
            isLenient = false
        }
        val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        Log.e("HistoryScreen", "Error formatting date: $dateString", e)
        dateString ?: "Tanggal tidak valid"
    }
}

// PERBAIKAN FUNGSI DENGAN NULL SAFETY
fun formatDateTimeForDisplay(dateTimeString: String?): String {
    return try {
        // Tambahkan null check di awal
        if (dateTimeString.isNullOrBlank()) {
            return "Tanggal tidak tersedia"
        }

        // Handle different possible datetime formats
        val possibleFormats = listOf(
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("id", "ID")),
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("id", "ID")),
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID")),
            SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        )

        val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))

        for (inputFormat in possibleFormats) {
            try {
                val date = inputFormat.parse(dateTimeString)
                return date?.let { outputFormat.format(it) } ?: dateTimeString
            } catch (e: Exception) {
                // Try next format
                continue
            }
        }

        return dateTimeString
    } catch (e: Exception) {
        Log.e("HistoryScreen", "Error formatting datetime: $dateTimeString", e)
        return dateTimeString ?: "Tanggal tidak valid"
    }
}

@Composable
fun HistoryTopBar(modifier: Modifier = Modifier, index: (Int) -> Unit, selectedTab: Int = 0) {
    var selectedIndex by remember { mutableIntStateOf(selectedTab) }
    Column (
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Histori",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
            fontSize = 30.sp,
            fontFamily = robotoFontFamily,
            fontWeight = FontWeight(500)
        )
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(0.5f)
                    .clickable{
                        selectedIndex = 0
                        index(selectedIndex)
                    }
            ) {
                Text(
                    text = "Presensi",
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.padding(bottom = 10.dp),
                    color = if (selectedIndex == 0) Color(0xff2752E7) else Color.Unspecified
                )
                HorizontalDivider(
                    color = if (selectedIndex == 0) Color(0xff2752E7) else Color.LightGray,
                    thickness = 2.dp
                )
            }
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(1f)
                    .clickable{
                        selectedIndex = 1
                        index(selectedIndex)
                    }
            ){
                Text(
                    text = "Patroli",
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.padding(bottom = 10.dp),
                    color = if (selectedIndex == 1) Color(0xff2752E7) else Color.Unspecified

                )
                HorizontalDivider(
                    color = if (selectedIndex == 1) Color(0xff2752E7) else Color.LightGray,
                    thickness = 2.dp
                )
            }
        }
    }
}

fun getIndonesianMonthShort(month: Int): String {
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

fun getApiDateFormat(calendar: Calendar): String {
    val year = calendar.get(Calendar.YEAR)
    // Add 1 to month because Calendar months are 0-indexed
    val month = (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
    val day = calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
    return "$year-$month-$day"
}

@Preview(showBackground = true)
@Composable
private fun HistoryPrev() {
    HistoryScreen(navCtrl = rememberNavController(), token = "dummy_token")
}