package com.example.mobileapptechnobit.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.mobileapptechnobit.ui.theme.primary100
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily

@Composable
fun EditSuksesScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_editsukses),
                contentDescription = "Success Screen",
                modifier = Modifier
                    .size(250.dp),
            )

            Text(
                text = "Selesai!",
                fontSize = 22.sp,
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Profil anda berhasil diperbarui",
                fontSize = 14.sp,
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        }

        Button(
            onClick = { navController.navigate("profile_screen") },
            colors = ButtonDefaults.buttonColors(containerColor = primary100),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(58.dp)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "Kembali",
                fontSize = 16.sp,
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=412dp, height=915dp, dpi=440")
@Composable
fun EditSuksesScreenPreview() {
    EditSuksesScreen(navController = rememberNavController())
}