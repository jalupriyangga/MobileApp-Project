package com.example.mobileapptechnobit.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapptechnobit.ui.theme.primary100
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailPermitScreen(modifier: Modifier = Modifier, navCtrl: NavController) {
    Scaffold (
        topBar = {
            DetailPermitTitle(navCtrl = navCtrl)
        },
        bottomBar = {
            BottomButton()
//            Column (Modifier.padding(bottom = 20.dp)) {
//                BottomStatus(status = "disetujui")
//            }
        }
    ) { padding ->
        Column(Modifier
            .padding(padding)
            .padding(top = 50.dp)) {
            DetailPermitBody()
        }
    }
}

@Composable
fun DetailPermitTitle(modifier: Modifier = Modifier, navCtrl: NavController) {
    Column (Modifier.fillMaxWidth()){
        IconButton(
            onClick = { navCtrl.navigate("permission_screen") },
            Modifier
                .padding(start = 10.dp)
                .padding(top = 30.dp)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
        Spacer(Modifier.padding(5.dp))
        Text(
            text = "Detail Perizinan", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 25.sp, textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DetailPermitBody(modifier: Modifier = Modifier) {
//    Column (Modifier.padding(horizontal = 30.dp)){
//        Text(text = "Pegawai Pengganti", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
//        Spacer(Modifier.padding(3.dp))
//        Text(text = "Andi", fontFamily = robotoFontFamily, fontWeight = FontWeight(400), fontSize = 15.sp)
//
//        Spacer(Modifier.padding(10.dp))
//        Text(text = "Jadwal Lama", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
//        Spacer(Modifier.padding(5.dp))
//        ScheduleCard(time = "pagi", offset = 0)
//
//        Spacer(Modifier.padding(10.dp))
//        Text(text = "Jadwal Baru", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
//        Spacer(Modifier.padding(5.dp))
//        ScheduleCard(time = "pagi", offset = 0)
//    }
}

@Composable
fun BottomButton(modifier: Modifier = Modifier) {
    Row (Modifier.fillMaxWidth().padding(bottom = 20.dp).padding(horizontal = 30.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){

        OutlinedButton(
            onClick = {},
            shape = RoundedCornerShape(5.dp),
            border = ButtonDefaults.outlinedButtonBorder,
            modifier = Modifier.width(160.dp).height(50.dp)
        )  {
            Text(text = "Menolak", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp, color = primary100)
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {},
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(primary100),
            modifier = Modifier.width(160.dp).height(50.dp)
        ) {
            Text(text = "Setuju", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
        }
    }
}

@Composable
fun BottomStatus(modifier: Modifier = Modifier, status: String) {
    Column(
        Modifier.fillMaxWidth().padding(horizontal = 30.dp).height(50.dp)
            .border(
                width = 2.dp,
                color = if(status.equals("disetujui", ignoreCase = true)) Color(0xff3F845F)
                else Color(0xffE25C5C),
                shape = RoundedCornerShape(5.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(text = "Status Perizinan: $status", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 16.sp,
            color = if(status.equals("disetujui", ignoreCase = true)) Color(0xff3F845F)
            else Color(0xffE25C5C)
        )
    }
}

@Preview
@Composable
private fun DetailPermitPrev() {
    DetailPermitScreen(navCtrl = rememberNavController())
}