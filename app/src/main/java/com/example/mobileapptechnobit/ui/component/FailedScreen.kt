package com.example.mobileapptechnobit.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily

@Composable
fun FailedScreen(modifier: Modifier = Modifier, navCtrl: NavController, route: String) {
    Column (
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(painter = painterResource(R.drawable.selesai), contentDescription = null, Modifier.size(300.dp))
        Text(
            text = "Gagal Melakukan Presensi!",
            fontFamily = robotoFontFamily,
            fontWeight = FontWeight(500),
            fontSize = 25.sp
        )
        Text(
            text = "Anda berada di luar radius lokasi yang ditentukan.",
            fontFamily = robotoFontFamily,
            fontWeight = FontWeight(400),
            modifier = Modifier.padding(top = 6.dp).padding(horizontal = 15.dp).fillMaxWidth(),
            color = Color.Gray,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
        Text(
            text = "Silakan pastikan Anda berada di area yang sesuai untuk melakukan presensi.",
            fontFamily = robotoFontFamily,
            fontWeight = FontWeight(400),
            modifier = Modifier.padding(horizontal = 15.dp).fillMaxWidth(),
            color = Color.Gray,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
    }
    Column (
        Modifier.fillMaxSize().padding(bottom = 50.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        OutlinedButton(
            onClick = {
                navCtrl.navigate(route)
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(50.dp)
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(5.dp)
                ),
            shape = RoundedCornerShape(5.dp),
        ) {
            Text(
                text = "Kembali",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight(500),
                fontSize = 17.sp,
                color = Color.Black
            )
        }
    }
}