package com.example.mobileapptechnobit.ui.feature_history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily
import java.util.Calendar

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

    Surface(
        shadowElevation = 4.dp,
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth().height(64.dp),
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
