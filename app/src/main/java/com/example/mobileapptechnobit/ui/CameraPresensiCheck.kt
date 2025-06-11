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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobileapptechnobit.ViewModel.CameraPresViewModel
import com.example.mobileapptechnobit.ViewModel.ProfileViewModel
import com.example.mobileapptechnobit.ViewModel.ProfileViewModelFactory
import com.example.mobileapptechnobit.data.API.UserProfileResponse
import com.example.mobileapptechnobit.data.repository.ProfileRepository
import com.example.mobileapptechnobit.ui.theme.primary100
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
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
    var showErrorDialog by remember { mutableStateOf(false) }

    val repository = ProfileRepository(context)
    val viewMode: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(repository))
    val profileState = viewMode.employeesProfile.collectAsState()
    val profile = profileState.value


    Log.d("CameraPresensiCheck", "Bitmap: $bitmap, Token: $token")

    if (bitmap == null || token == null) {
        Log.d("CameraPresensiCheck", "Bitmap or token is null. Navigating back.")
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    LaunchedEffect(token) {
        viewMode.fetchEmployeesProfile(token)
    }

    Scaffold(
        topBar = {
            Box {
                TopAppBar(
                    title = {},
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = colorResource(id = R.color.primary100)
                    ),
                    modifier = Modifier.height(112.dp)
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                ) {
                    CameraPresCheckTitle(navCtrl = navController)
                }
            }
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
                                        Log.d("PresensiUpload", "File path: ${savedFile.absolutePath}, exists=${savedFile.exists()}, size=${savedFile.length()} bytes")

                                        val fileInputStream = FileInputStream(savedFile)
                                        val byteArray = fileInputStream.readBytes()
                                        fileInputStream.close()
                                        Log.d("PresensiUpload", "Byte array size: ${byteArray.size}")

                                        val requestFile = RequestBody.create(
                                            "image/jpeg".toMediaTypeOrNull(),
                                            byteArray
                                        )
                                        val photoPart = MultipartBody.Part.createFormData(
                                            "photo_file",
                                            savedFile.name,
                                            requestFile
                                        )

                                        Log.d("PresensiUpload", "Uploading image with filename: ${savedFile.name}")

                                        val response = viewModel.sendPresensiToApi(
                                            token = token,
                                            photo = photoPart,
                                            file = savedFile,
                                            employeeId = profile?.id ?: 0,
                                            companyPlaceId = profile?.company_id ?: 0,
                                            userNote = "-",
                                            isManual = false
                                        )

                                        // [2] Log response dari API
                                        withContext(Dispatchers.Main) {
                                            isLoading = false
                                            Log.d("PresensiUpload", "API Response: $response")
                                            if (response?.success == false &&
                                                response.message?.contains("Presensi gagal karena kamu sudah presensi sebelumnya", ignoreCase = true) == true
                                            ) {
                                                showErrorDialog = true
                                            } else if (response?.success == true) {
                                                viewModel.saveClockInTime(context, System.currentTimeMillis())
                                                navController.navigate(Screen.PresensiSukses.route)
                                            } else {
                                                Toast.makeText(context, response?.message ?: "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    } catch (e: Exception) {
                                        Log.e("PresensiUpload", "Exception saat upload gambar", e)
                                        withContext(Dispatchers.Main) {
                                            isLoading = false
                                            if (e.message?.contains("Presensi gagal karena kamu sudah presensi sebelumnya", ignoreCase = true) == true) {
                                                showErrorDialog = true
                                            } else {
                                                Toast.makeText(context, "Periksa internet anda dan coba lagi", Toast.LENGTH_SHORT).show()
                                            }
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
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = {
                Text(
                    text = "Presensi Gagal",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = robotoFontFamily
                )
            },
            text = {
                Text(
                    text = "Kamu sudah melakukan presensi",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = robotoFontFamily
                )
            },
            confirmButton = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { showErrorDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = primary100),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Text(
                                text = "Kembali",
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

@Composable
fun CameraPresCheckTitle(modifier: Modifier = Modifier, navCtrl: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp)
    ) {
        IconButton(
            onClick = { navCtrl.popBackStack() },
            Modifier.padding(start = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back button",
                tint = androidx.compose.ui.graphics.Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = "Konfirmasi Foto",
            textAlign = TextAlign.Center,
            fontFamily = robotoFontFamily,
            fontWeight = androidx.compose.ui.text.font.FontWeight(500),
            color = androidx.compose.ui.graphics.Color.White,
            fontSize = 25.sp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return photoFile
}