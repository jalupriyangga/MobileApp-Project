package com.example.mobileapptechnobit.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.Screen
import com.example.mobileapptechnobit.ViewModel.CameraPresViewModel
import com.example.mobileapptechnobit.ui.theme.primary100
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClockOutScreen(
    navController: NavHostController,
    clockInTime: Long,
    token: String,
    viewModel: CameraPresViewModel,
    onClockOut: () -> Unit
) {
    val context = LocalContext.current
    val clockInTime = remember { viewModel.getClockInTime(context) }
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(clockInTime) {
        Log.d("ClockOutScreen", "clockInTime = $clockInTime")
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = System.currentTimeMillis()
            kotlinx.coroutines.delay(1000)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            IconButton(onClick = { navController.navigate(Screen.Home.route) }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                            }
                        }
                        Box(modifier = Modifier.weight(6f), contentAlignment = Alignment.Center) {
                            Text(
                                text = "Clock Out",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = robotoFontFamily
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                        }
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = colorResource(id = R.color.primary100)
                ),
                modifier = Modifier.height(112.dp)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 34.dp, start = 20.dp, end = 20.dp)
                    .background(color = Color.White),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            showDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primary100),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f)
                            .height(58.dp)
                    ) {
                        Text(
                            text = "Clock Out",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            fontFamily = robotoFontFamily
                        )
                    }
                }
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val workDuration = if (clockInTime > 0) currentTime - clockInTime else 0L
                    val hours = (workDuration / (1000 * 60 * 60)).toInt()
                    val minutes = ((workDuration / (1000 * 60)) % 60).toInt()
                    val seconds = ((workDuration / 1000) % 60).toInt()

                    Image(
                        painter = painterResource(id = R.drawable.walkthrough2),
                        contentDescription = "Success Screen",
                        modifier = Modifier
                            .size(250.dp)
                    )

                    Text(
                        text = "Akhiri Presensi",
                        fontSize = 22.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Akhiri presensi anda hari ini",
                        fontSize = 14.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Total jam Kerja: %02d.%02d.%02d".format(hours, minutes, seconds),
                        fontSize = 14.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = {
                Text(
                    text = "Clock out sekarang?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = robotoFontFamily
                )
            },
            text = {
                Text(
                    text = "Pastikan tidak ada tugas yang tertinggal",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = robotoFontFamily
                )
            },
            confirmButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = { showDialog = false },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.Gray),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "Batal",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = robotoFontFamily
                            )
                        }
                        Button(
                            onClick = {
                                showDialog = false
                                isLoading = true
                                navController.navigate(Screen.CameraPresensiOut.route)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = primary100),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "Clock Out",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                fontFamily = robotoFontFamily
                            )
                        }
                    }
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.background(Color.Transparent)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewClockOutScreen() {
    val navController = rememberNavController()
    ClockOutScreen(
        navController = navController,
        clockInTime = System.currentTimeMillis() - 3600000,
        token = "dummy_token",
        viewModel = CameraPresViewModel(),
        onClockOut = {}
    )
}