package com.example.mobileapptechnobit.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.ui.component.BottomNavigationBar
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HistoryScreen(modifier: Modifier = Modifier, navCtrl: NavController) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = {
            HistoryTopBar(
                modifier = Modifier,
                index = { index ->
                    selectedIndex = index
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(index = 1, navCtrl = navCtrl)
        },
    ){ padding ->
        Column {
            HistoryTopBar(
                modifier = Modifier,
                index = { index ->
                    selectedIndex = index
                }
            )
            Spacer(Modifier.height(30.dp))
            if(selectedIndex == 0) {
                HistoryCard(time = "pagi", status = "hadir")
            } else{
                HistoryCard(time = "malam", status = "cuti")
            }
        }
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
fun HistoryCard(modifier: Modifier = Modifier, time: String, status: String) {

    val time by remember { mutableStateOf(time) }
    val status by remember { mutableStateOf(status) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp))
        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = time,
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight(500),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            color = if (time.equals("pagi", ignoreCase = true)) Color(0xffFFF700)
                            else if (time.equals("siang", ignoreCase = true)) Color(0xffFFB834)
                            else Color(0xff89CDFF),
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(vertical = 5.dp)
                        .width(70.dp),
                )
                Spacer(Modifier.padding(horizontal = 5.dp))
                Icon(painter = painterResource(R.drawable.tabler_clock), contentDescription = null, Modifier.size(20.dp))
                Spacer(Modifier.padding(horizontal = 5.dp))
                Text("Selasa 04 Maret 2025", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray
            )
            Text("Lokasi Patroli", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp, modifier = Modifier.padding(top = 8.dp))
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
                        color = if (status.contains("hadir")) Color(0xff2685CA) else Color(0xffE4C65B),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = "Status Presensi: $status",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight(500),
                    color = if(status.contains("hadir", ignoreCase = true)) Color(0xff2685CA) else Color(0xffE4C65B),
                    )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryPrev() {
    HistoryScreen(navCtrl = rememberNavController())
}