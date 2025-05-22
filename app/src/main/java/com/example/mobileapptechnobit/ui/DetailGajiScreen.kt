package com.example.mobileapptechnobit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobileapptechnobit.R

val robotoFontFamily = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_bold, FontWeight.Bold)
)

@Composable
fun DetailGajiScreen(navCtrl: NavController) {
    Scaffold(
        topBar = {
            Box {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color(0xFF2962FF))
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, start = 10.dp, end = 10.dp)
                ) {
                    IconButton(
                        onClick = { navCtrl.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Text(
                        text = "Detail Gaji",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight(550),
                        color = Color.White,
                        fontSize = 22.sp,
                        fontFamily = robotoFontFamily,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Total Gaji",
                fontSize = 16.sp,
                color = Color.Gray,
                fontFamily = robotoFontFamily,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Rp3.000.000",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = robotoFontFamily,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            SalaryRow("Gaji Pokok", "Rp4.000.000")
            SalaryRow("Tunjangan", "Rp1.000.000")
            SalaryRow("Bonus", "Rp500.000")
            SalaryRow("Pajak", "Rp700.000")
            SalaryRow("Insentif Pajak", "Rp700.000")
        }
    }
}

@Composable
fun SalaryRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = robotoFontFamily
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = robotoFontFamily,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

