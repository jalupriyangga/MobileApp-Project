package com.example.mobileapptechnobit

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobileapptechnobit.ViewModel.CameraPresViewModel
import com.example.mobileapptechnobit.ui.theme.primary100
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraPresensiCheck(
    viewModel: CameraPresViewModel,
    navController: NavController,
    context: Context
) {
    val bitmap = viewModel.capturedBitmap.collectAsState().value
    val token = viewModel.token.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Log.d("CameraPresensiCheck", "Bitmap: $bitmap, Token: $token")

    if (bitmap == null || token == null) {
        Log.d("CameraPresensiCheck", "Bitmap or token is null. Navigating back.")
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
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
                        Box(modifier = Modifier.weight(6f), contentAlignment = Alignment.Center) {
                            Text(
                                text = "Konfirmasi Foto",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = robotoFontFamily
                            )
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
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color.Gray),
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f)
                            .height(58.dp)
                    ) {
                        Text(
                            text = "Ulangi",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = robotoFontFamily
                        )
                    }
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
                            text = "Kirim",
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(color = Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 71.dp)
                        .aspectRatio(3f / 4f)
                )
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
                    text = "Kirim Presensi?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = robotoFontFamily
                )
            },
            text = {
                Text(
                    text = "Pastikan foto sudah sesuai",
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
                                coroutineScope.launch(Dispatchers.IO) {
                                    try {
                                        val savedFile = saveBitmapToPublicPictures(context, bitmap)
                                        val photoBase64 = bitmapToBase64(bitmap)
                                        Log.d("PhotoBase64Validation", "Base64 Length: ${photoBase64.length}, Sample: ${photoBase64.take(50)}")


                                        viewModel.sendClockInToApi(
                                            token = token,
                                            photoBase64 = photoBase64,
                                            filename = savedFile.name
                                        )

                                        viewModel.saveClockInTime(context, System.currentTimeMillis())

                                        withContext(Dispatchers.Main) {
                                            isLoading = false
                                            navController.navigate(Screen.PresensiSukses.route)
                                        }
                                    } catch (e: Exception) {
                                        Log.e("CameraPresensiCheck", "Error during Clock-In", e)
                                        withContext(Dispatchers.Main) {
                                            isLoading = false
                                            Toast.makeText(context, "Periksa internet anda dan coba lagi", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = primary100),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "Kirim",
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
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

fun saveBitmapToPublicPictures(context: Context, bitmap: Bitmap): File {
    val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    if (!picturesDir.exists()) picturesDir.mkdirs()

    val currentTime = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(currentTime))
    val photoFileName = "presensi_$formattedDate.jpg"

    val photoFile = File(picturesDir, photoFileName)
    FileOutputStream(photoFile).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
    return photoFile
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val resizedBitmap = resizeBitmap(bitmap, maxWidth = 800, maxHeight = 800)
    val outputStream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)

    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}

fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val aspectRatio = width.toFloat() / height.toFloat()
    val newWidth: Int
    val newHeight: Int

    if (width > height) {
        newWidth = maxWidth
        newHeight = (newWidth / aspectRatio).toInt()
    } else {
        newHeight = maxHeight
        newWidth = (newHeight * aspectRatio).toInt()
    }

    return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
}