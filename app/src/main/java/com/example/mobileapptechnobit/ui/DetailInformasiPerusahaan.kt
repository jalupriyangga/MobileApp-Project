package com.example.mobileapptechnobit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun DetailInformasiPerusahaanScreen(navController: NavController) {
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
                        onClick = { navController.popBackStack() },
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
                        text = "Informasi Perusahaan",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight(550),
                        color = Color.White,
                        fontSize = 22.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    ) { padding ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "LOGO",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6F00),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "PT. ARUNIKA PRAWIRATAMA INDONESIA",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi feugiat quam magna. Suspendisse egestas lectus nec nulla posuere, non tristique nisi tristique. Nunc vel malesuada orci. Vestibulum nec nibh metus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.\n\n" +
                        "Praesent sit amet augue et diam dapibus pulvinar quis at purus. Nam pharetra, purus vitae accumsan fermentum, ipsum nisi imperdiet dui, vitae efficitur libero mauris egestas nisl. Fusce tincidunt ipsum id nisl varius feugiat. Praesent a dui dignissim augue commodo hendrerit quis at odio.\n\n" +
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi feugiat quam magna. Suspendisse egestas lectus nec nulla posuere, non tristique nisi tristique. Nunc vel malesuada orci. Vestibulum nec nibh metus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.",
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            ContactItem(Icons.Outlined.Email, "info@gmail.com")
            ContactItem(Icons.Outlined.Phone, "+62 812 1234 5678")
            ContactItem(Icons.Outlined.LocationOn, "Jl. Teknologi No. 12, Malang")
            ContactItem(Icons.Outlined.Language, "www.website.id")

        }
    }
}

@Composable
fun ContactItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 14.sp)
    }
}
