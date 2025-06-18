package com.example.mobileapptechnobit.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.mobileapptechnobit.ViewModel.CompanyProfileViewModel
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformasiPerusahaan(navController: NavController, token: String) {
    val viewModel: CompanyProfileViewModel = viewModel()
    val companyProfile by viewModel.companyProfile.collectAsState()
    val error by viewModel.error.collectAsState()

    // Panggil fetch saat komposisi pertama
    LaunchedEffect(Unit) {
        viewModel.fetchCompanyProfile(token)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                            }
                        }
                        Box(modifier = Modifier.weight(6f), contentAlignment = Alignment.Center) {
                            Text(
                                text = "Informasi Perusahaan",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = robotoFontFamily
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {}
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF2752E7)
                ),
                modifier = Modifier.height(120.dp),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    companyProfile != null -> {
                        val logo = companyProfile!!.logo
                        val logoUrl = "https://app.arunikaprawira.com/storage/company/logo/$logo"
                        Log.d("InformasiPerusahaan", "Logo URL: $logoUrl")

                        AsyncImage(
                            model = logoUrl,
                            contentDescription = "Logo Perusahaan",
                            modifier = Modifier
                                .height(200.dp)
                                .padding(32.dp),
                            contentScale = ContentScale.Fit
                        )

                        // Tampilkan informasi lain jika diperlukan
                        Text(
                            text = companyProfile!!.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = companyProfile!!.description,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    error != null -> {
                        Text(
                            text = error ?: "Terjadi kesalahan",
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    else -> {
                        CircularProgressIndicator(modifier = Modifier.padding(24.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=412dp, height=915dp, dpi=440")
@Composable
fun InformasiPerusahaanPreview() {
    InformasiPerusahaan(navController = rememberNavController(), token = "dummy_token")
}
