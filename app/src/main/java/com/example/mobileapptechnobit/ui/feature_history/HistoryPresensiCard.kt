package com.example.mobileapptechnobit.ui.feature_history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.Screen
import com.example.mobileapptechnobit.data.remote.HistoryPresensiResponseItem
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily

@Composable
fun HistoryPresensiCard(
    historyItem: HistoryPresensiResponseItem,
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
            navController.navigate(Screen.DetailHistoryPresensi.route)
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