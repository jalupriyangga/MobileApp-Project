package com.example.mobileapptechnobit.ui.feature_history

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobileapptechnobit.Screen
import com.example.mobileapptechnobit.data.remote.HistoryPatroliResponseItem
import com.example.mobileapptechnobit.ui.theme.error100
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily
import com.example.mobileapptechnobit.ui.theme.success100

@Composable
fun HistoryPatroliCard(
    historyItem: HistoryPatroliResponseItem,
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
            navController.navigate(Screen.DetailHistoryPatroli.route)
        }
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Safe shift handling
                val shiftId = historyItem.shiftId?.toString() ?: "0"
                val shiftText = when(shiftId) {
                    "1" -> "Pagi"
                    "2" -> "Siang"
                    "3" -> "Malam"
                    else -> "Tidak Diketahui"
                }

                val shiftColor = when(shiftId) {
                    "1" -> Color(0xffFFF700)
                    "2" -> Color(0xffFFB834)
                    "3" -> Color(0xff89CDFF)
                    else -> Color.LightGray
                }

                Text(
                    text = shiftText,
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight(500),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            color = shiftColor,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(vertical = 5.dp)
                        .width(70.dp),
                )
                Spacer(Modifier.padding(horizontal = 5.dp))

                Text(
                    text = historyItem.patrolLocation ?: "Lokasi tidak tersedia",
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight(500),
                    fontSize = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray
            )
            Spacer(Modifier.height(10.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = formattedDate,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight(500),
                        fontSize = 17.sp
                    )

                    // Safe time extraction
                    val timeText = extractTimeFromDateTime(historyItem.createdAt)
                    Text(
                        text = timeText,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight(400),
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 5.dp),
                        color = if (timeText == "Waktu tidak tersedia") Color.Gray else Color.Unspecified
                    )
                }
            }

            Text(
                text = "Catatan : ",
                fontFamily = robotoFontFamily,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            val catatanText = historyItem.catatan?.takeIf { it.isNotBlank() } ?: "Tidak ada catatan"
            Text(
                text = catatanText,
                fontFamily = robotoFontFamily,
                color = if (historyItem.catatan.isNullOrBlank()) Color.Gray else Color.Unspecified
            )
        }
    }
}

// Helper function to safely extract time
fun extractTimeFromDateTime(createdAt: String?): String {
    return try {
        if (createdAt.isNullOrBlank() || createdAt.length <= 11) {
            "Waktu tidak tersedia"
        } else {
            val timeString = createdAt.substring(11)
            "$timeString WIB"
        }
    } catch (e: Exception) {
        Log.e("HistoryPatroliCard", "Error extracting time from: $createdAt", e)
        "Waktu tidak tersedia"
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryPatroliCardPreview() {
    HistoryPatroliCard(
        historyItem = HistoryPatroliResponseItem(
            id = 1,
            shiftId = 1,
            placeId = 1,
            patrolLocation = "Jakarta",
            status = "Aman",
            catatan = "Semua baik-baik saja",
            photo = "",
            createdAt = "2023-10-01T08:00:00Z",
            updatedAt = "2023-10-01T08:00:00Z",
            photoUrl = ""
        ),
        navController = NavController(context = LocalContext.current),
        formattedDate = "01 Oktober 2023"
    )
}