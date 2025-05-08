package com.example.mobileapptechnobit.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily

@Composable
fun SuccessScreen(modifier: Modifier = Modifier, navCtrl: NavController, message: String, route: String) {
    Column (
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(painter = painterResource(R.drawable.group_1__1_), contentDescription = null, Modifier.size(300.dp))
        Text(
            text = "Selesai!",
            fontFamily = robotoFontFamily,
            fontWeight = FontWeight(500),
            fontSize = 25.sp
        )
        Text(
            text = message,
            fontFamily = robotoFontFamily,
            fontWeight = FontWeight(400),
            modifier = Modifier.padding(top = 6.dp),
            color = Color.Gray
        )
    }
    Column (
        Modifier.fillMaxSize().padding(bottom = 50.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Button(
            onClick = {
                navCtrl.navigate(route)
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF2752E7)),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(50.dp),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(
                text = "Kembali",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight(500),
                fontSize = 17.sp,
                color = Color.White
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun SuccessPrev() {
    SuccessScreen(navCtrl = rememberNavController(), message = "Password anda berhasil diperbarui", route = "")
}