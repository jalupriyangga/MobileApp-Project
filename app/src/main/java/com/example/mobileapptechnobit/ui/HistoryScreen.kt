package com.example.mobileapptechnobit.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.Screen
import com.example.mobileapptechnobit.ViewModel.HistoryViewModel
import com.example.mobileapptechnobit.ViewModel.HistoryViewModelFactory
import com.example.mobileapptechnobit.data.API.ApiClient.apiService
import com.example.mobileapptechnobit.data.remote.HistoryResponseItem
import com.example.mobileapptechnobit.data.repository.HistoryRepository
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
    // Create ViewModel instance
    val factory = remember { HistoryViewModelFactory(HistoryRepository(apiService = apiService)) }
    val viewModel: HistoryViewModel = viewModel(factory = factory)

    // Collect state from ViewModel
    val historyItems = viewModel.historyItems.collectAsState()
    val selectedDays = viewModel.selectedDay.collectAsState()
    val selectedTab = viewModel.selectedTab.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Debug logs
    LaunchedEffect(historyItems.value) {
        Log.d("HistoryScreen", "History items in UI: ${historyItems.value.size}")
    }

    // Fetch data when composable appears
    LaunchedEffect(Unit) {
        viewModel.fetchHistory(token)
    }

    Scaffold(
        topBar = {
            HistoryTopBar(index = { index -> viewModel.setSelectedTab(index) })
        },
        bottomBar = {
            BottomNavigationBar(index = 1, navCtrl = navCtrl)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize() // Make sure the column takes full size
        ) {
            // Month selector
            HistoryDaysSelector(
                selectedDay = selectedDays.value,
                onDaySelected = { day -> viewModel.setSelecteDays(day) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Content based on loading/error state
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                !error.isNullOrEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = error ?: "Unknown error", color = Color.Red)
                    }
                }
                historyItems.value.isEmpty() -> {
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
                                text = "History di hari ${selectedDays.value} tidak ada",
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                else -> {
                    // Display history items
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(historyItems.value){

                        }
                        items(historyItems.value) { item ->
                            // Convert the date format from "2025-05-05" to "Senin 05 Mei 2025"
                            val formattedDate = formatDateForDisplay(item.tanggal)

                            Log.d("HistoryScreen", "Rendering item: ${item.tanggal}, ${item.status}")
                            HistoryCard(
                                historyItem = item,
                                navController = navCtrl,
                                formattedDate = formattedDate
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

// Helper function to format date from "2025-05-05" to "Senin 05 Mei 2025"
fun formatDateForDisplay(dateString: String): String {
    try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))

        val date = inputFormat.parse(dateString)
        return date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        Log.e("HistoryScreen", "Error formatting date: $dateString", e)
        return dateString
    }
}

@Composable
fun HistoryTopBar(modifier: Modifier = Modifier, index: (Int) -> Unit) {
    var selectedIndex by remember { mutableIntStateOf(0) }
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

@Composable
fun HistoryCard(
    historyItem: HistoryResponseItem,
    modifier: Modifier = Modifier,
    navController: NavController,
    formattedDate: String
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White),
        onClick = {
            navController.currentBackStackEntry?.savedStateHandle?.set("presenceItem", historyItem)
            navController.currentBackStackEntry?.savedStateHandle?.set("formattedDate", formattedDate)
            navController.navigate(Screen.DetailHistory.route)
        }
    ) {
        Column(modifier = Modifier.padding(20.dp))
        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = historyItem.shift.toString(),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight(500),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            color = when (historyItem.shift.toString().lowercase()) {
                                "pagi" -> Color(0xffFFF700)
                                "siang" -> Color(0xffFFB834)
                                else -> Color(0xff89CDFF) // malam or default
                            },
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(vertical = 5.dp)
                        .width(70.dp),
                )
                Spacer(Modifier.padding(horizontal = 5.dp))
                Icon(painter = painterResource(R.drawable.tabler_clock), contentDescription = null, Modifier.size(20.dp))
                Spacer(Modifier.padding(horizontal = 5.dp))
                Text(formattedDate, fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray
            )
            Text("Lokasi Patroli", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp, modifier = Modifier.padding(top = 8.dp))
            if (historyItem.lokasi.isNotEmpty()) {
                Text(historyItem.lokasi, fontFamily = robotoFontFamily, fontWeight = FontWeight(400), fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                Modifier.padding(horizontal = 25.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(painter = painterResource(R.drawable.frame_27),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = Color.Unspecified
                )
                Column (
                    Modifier.padding(start = 8.dp)
                ){
                    Text(text = "Mulai", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 16.sp)
                    Text(text = "08.00 WIB", fontFamily = robotoFontFamily)
                }

                Spacer(Modifier.weight(1f))
                VerticalDivider(
                    thickness = 1.dp,
                    color = Color.LightGray,
                    modifier = Modifier.height(50.dp)
                )
                Spacer(Modifier.weight(1f))

                Icon(painter = painterResource(R.drawable.frame_28),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = Color.Unspecified
                )
                Column (
                    Modifier.padding(start = 8.dp)
                ){
                    Text(text = "Selesai", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 16.sp)
                    Text(text = "16.00 WIB", fontFamily = robotoFontFamily)
                }
            }
            Spacer(Modifier.height(10.dp))
            Column (
                Modifier
                    .border(
                        width = 2.dp,
                        color = if (historyItem.status.contains("hadir", ignoreCase = true)) Color(0xff2685CA) else Color(0xffE4C65B),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = "Status Presensi: ${historyItem.status}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight(500),
                    color = if(historyItem.status.contains("hadir", ignoreCase = true)) Color(0xff2685CA) else Color(0xffE4C65B),
                )
            }
        }
    }
}

@Composable
fun HistoryDaysSelector(
    selectedDay: String,
    onDaySelected: (String) -> Unit
) {
    // Get current date and calendar
    val currentDate = remember { Calendar.getInstance() }
    val currentDayOfWeek = remember { currentDate.get(Calendar.DAY_OF_WEEK) }

    // Calculate the date for each day of the current week
    val weekDays = remember {
        val dayNames = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
        val daysInfo = mutableListOf<DayInfo>()

        // Find Monday of current week
        val monday = Calendar.getInstance()
        monday.time = currentDate.time

        // In Calendar.DAY_OF_WEEK, Monday is 2, Sunday is 1
        val daysUntilMonday = if (currentDayOfWeek == Calendar.SUNDAY) 6 else currentDayOfWeek - Calendar.MONDAY
        monday.add(Calendar.DAY_OF_MONTH, -daysUntilMonday)

        // Create info for each day
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
        daysInfo
    }

    androidx.compose.material3.Surface(
        shadowElevation = 4.dp,
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth().height(64.dp), // Increased height to fit date
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 14.dp, end = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items(weekDays) { dayInfo ->
                val isSelected = dayInfo.name == selectedDay

                Card(
                    modifier = Modifier
                        .width(70.dp)
                        .height(50.dp), // Increased height to fit both day and date
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF2685CA) else Color.White,
                    ),
                    onClick = {
                        onDaySelected(dayInfo.name)
                    }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = dayInfo.name,
                            color = if (isSelected) Color.White else Color.Black,
                            fontSize = 14.sp,
                            fontFamily = robotoFontFamily,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = dayInfo.dateString,
                            color = if (isSelected) Color.White else Color.Gray,
                            fontSize = 12.sp,
                            fontFamily = robotoFontFamily,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

data class DayInfo(
    val name: String,
    val date: Int,
    val month: String,
    val dateString: String,
    val isToday: Boolean,
    val apiDateFormat: String
)

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