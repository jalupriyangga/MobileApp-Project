package com.example.mobileapptechnobit.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.ui.component.BottomNavigationBar
import com.example.mobileapptechnobit.ui.component.TopAppBar
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(modifier: Modifier = Modifier, navCtrl: NavController) {
    Scaffold(
        topBar = {
            Box(
                contentAlignment = Alignment.TopStart
            ){
                TopAppBar()
                Column {
                    ProfileSection()
                    ScheduleCard(navCtrl = navCtrl)
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navCtrl = navCtrl, index = 0)
        },
    ){ padding ->
            Column (Modifier.padding(padding).fillMaxSize().zIndex(1f))
            {
                MainMenu()
            }
    }
}

@Composable
fun ProfileSection(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp, start = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.iconamoon_profile), // Ganti dengan gambar profil
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text("Daniel Budianto", fontSize = 23.sp, color = Color.White, fontFamily = robotoFontFamily, fontWeight = FontWeight(500))
            Text("Staff", fontSize = 16.sp, color = Color.White, fontFamily = robotoFontFamily, fontWeight = FontWeight(400))
        }
    }
}

@Composable
fun ScheduleCard(modifier: Modifier = Modifier, navCtrl: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 60.dp)
            .offset(y = (-20).dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Selasa 04 Maret 2025", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
                Spacer(Modifier.weight(1f))
                Text("Lihat Jadwal", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.clickable{navCtrl.navigate("schedule_screen")})
                Spacer(Modifier.padding(start = 10.dp))
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, Modifier.size(20.dp).clickable{navCtrl.navigate("schedule_screen")})
            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                Modifier.padding(horizontal = 25.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(painter = painterResource(R.drawable.frame_27__1_),
                    contentDescription = null,
                    modifier = Modifier.size(45.dp),
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
                    modifier = Modifier.size(45.dp),
                    tint = Color.Unspecified
                )
                Column (
                    Modifier.padding(start = 8.dp)
                ){
                    Text(text = "Selesai", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 16.sp)
                    Text(text = "16.00 WIB", fontFamily = robotoFontFamily)
                }
            }
        }
    }
}

@Composable
fun MainMenu(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp).padding(top = 20.dp).padding(bottom = 10.dp)
    ) {
        Text("Menu Utama", fontWeight = FontWeight.Bold, fontSize = 25.sp, fontFamily = robotoFontFamily)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MenuItem(painter = painterResource(R.drawable.presensi), label = "Presensi")
            MenuItem(painter = painterResource(R.drawable.patroli__1_), label = "Patroli")
        }
    }
}

@Composable
fun MenuItem(modifier: Modifier = Modifier, painter: Painter, label: String) {
    Card (
        modifier = Modifier
        .size(160.dp)
        .clickable { },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(painter = painter, contentDescription = label, modifier = Modifier.size(100.dp), tint = Color.Unspecified)
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, fontFamily = robotoFontFamily, fontWeight = FontWeight(400), fontSize = 17.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePrev() {
    HomeScreen(navCtrl = rememberNavController())
}