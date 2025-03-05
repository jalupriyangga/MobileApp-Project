package com.example.mobileapptechnobit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.compose.foundation.*
import androidx.compose.foundation.pager.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.Screen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalkthroughScreen(navController: NavController) {
    val pages = listOf(
        WalkthroughPage(
            title = "Presensi Mudah dan Cepat",
            description = "Scan QR untuk presensi yang cepat dan akurat",
            imageRes = R.drawable.walkthrough1
        ),
        WalkthroughPage(
            title = "Jadwal Shift Lebih Teratur",
            description = "Lihat jadwal shift dengan mudah",
            imageRes = R.drawable.walkthrough2
        ),
        WalkthroughPage(
            title = "Laporan dan Izin Praktis",
            description = "Ajukan izin dan buat laporan dengan mudah",
            imageRes = R.drawable.walkthrough3
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            PageIndicator(currentPage = pagerState.currentPage, pageCount = pages.size)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .align(Alignment.TopEnd), // Pastikan tombol sejajar di atas kanan
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                    Text(
                        "Lewati",
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
            }
        }

        // Page untuk walkthrough
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            WalkthroughPageView(page = pages[page])
        }

        // Tombol "Berikutnya"
        Button(
            onClick = {
                coroutineScope.launch {
                    if (pagerState.currentPage == pages.size - 1) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Walkthrough.route) { inclusive = true }
                        }
                    } else {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2752E7)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Berikutnya",
                fontSize = 16.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// Data class untuk tiap halaman
data class WalkthroughPage(val title: String, val description: String, val imageRes: Int)

// Tampilan untuk setiap halaman walkthrough
@Composable
fun WalkthroughPageView(page: WalkthroughPage) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = page.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = page.description,
            fontSize = 14.sp,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}

@Composable
fun PageIndicator(currentPage: Int, pageCount: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 22.dp)
    ) {
        repeat(pageCount) { index ->
            Indicator(isSelected = index == currentPage)
            if (index < pageCount - 1) {
                Spacer(modifier = Modifier.width(5.dp))
            }
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(9.dp)
            .clip(RoundedCornerShape(50))
            .background(if (isSelected) Color(0xFF2752E7) else Color.LightGray)
    )
}
