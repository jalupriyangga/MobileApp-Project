package com.example.mobileapptechnobit.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.mobileapptechnobit.ui.component.TopAppBar
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScheduleScreen(modifier: Modifier = Modifier, navCtrl: NavController) {
    Scaffold (
        topBar = {
            Box(){
                TopAppBar()
                Column (
                    Modifier.fillMaxWidth()
                ) {
                    ScheduleTitle(navCtrl = navCtrl)
                    Column (Modifier.background(Color.White)){
                        Spacer(Modifier.height(40.dp))
                        ScheduleCard(time = "Pagi")
                        ScheduleCard(time = "Siang")
                        ScheduleCard(time = "Malam")
                    }
                }
            }
        }
    ){ padding ->
        Column (Modifier.padding(padding)) {}
    }
}

@Composable
fun ScheduleTitle(modifier: Modifier = Modifier, navCtrl: NavController) {
    Box (
        modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp)
    ) {
        IconButton(
            onClick = { navCtrl.navigate("home_screen") },
            Modifier.padding(start = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back button",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
                )
        }
        Text(
            text = "Jadwal Patroli",
            textAlign = TextAlign.Center,
            fontFamily = robotoFontFamily,
            fontWeight = FontWeight(500),
            color = Color.White,
            fontSize = 25.sp,
            modifier = Modifier.fillMaxWidth().align(Alignment.Center))
    }
}
@Composable
fun ScheduleCard(modifier: Modifier = Modifier, time: String) {

    val time by remember { mutableStateOf(time) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 20.dp)
            .offset(y = (-20).dp),
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
                        .padding(vertical = 5.dp).width(70.dp),
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
                Icon(painter = painterResource(R.drawable.frame_27__1_),
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

                Icon(painter = painterResource(R.drawable.frame_28__1_),
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
            Text("Rekan tugas: ", fontFamily = robotoFontFamily, fontWeight = FontWeight(400), fontSize = 10.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScheduleScreenPrev() {
    ScheduleScreen(navCtrl = rememberNavController())
}
