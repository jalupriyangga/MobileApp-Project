package com.example.mobileapptechnobit.ui

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
        Column(modifier = Modifier.padding(20.dp))
        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = historyItem.shiftId.toString(),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight(500),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            color = when (historyItem.shiftId.toString().lowercase()) {
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
                Text("Lokasi Patroli", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray
            )
            Spacer(Modifier.height(10.dp))
            Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Absolute.SpaceBetween) {
                Column {
                    Text(formattedDate, fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
                    if (historyItem.patrolLocation.isNotEmpty()) {
//                Text(historyItem.status, fontFamily = robotoFontFamily, fontWeight = FontWeight(400), fontSize = 15.sp)
                        Text(text = "08.00 WIB", fontFamily = robotoFontFamily, fontWeight = FontWeight(400), fontSize = 15.sp, modifier = Modifier.padding(top = 5.dp))
                    }
                }
                Text(
                    text = historyItem.shiftId.toString(),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight(500),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            color = when (historyItem.shiftId.toString().lowercase()) {
                                "Aman" -> success100
                                "Tidak Aman" -> error100
                                else -> Color(0xff89CDFF) // malam or default
                            },
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(vertical = 5.dp)
                        .width(40.dp),
                )
            }
            Text(text = "Catatan : ", fontFamily = robotoFontFamily, modifier = Modifier.padding(vertical = 8.dp))
            Text(text = historyItem.catatan, fontFamily = robotoFontFamily)
        }
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