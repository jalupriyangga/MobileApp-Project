package com.example.mobileapptechnobit

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
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
import androidx.navigation.NavController
import com.example.mobileapptechnobit.ViewModel.CameraPresViewModel
import com.example.mobileapptechnobit.ui.theme.primary100
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

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

    // State untuk menampilkan dialog
    var showDialog by remember { mutableStateOf(false) }

    Log.d("CameraPresensiCheck", "Bitmap: $bitmap, Token: $token")

    // Validasi jika bitmap atau token null
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
                                color = androidx.compose.ui.graphics.Color.White,
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
                            .height(58.dp) // Menambahkan tinggi tombol
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
                            showDialog = true // Tampilkan dialog
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primary100),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f)
                            .height(58.dp) // Menambahkan tinggi tombol
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
                verticalArrangement = Arrangement.Top // Mengatur konten ke bagian atas
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 71.dp) // Menambahkan padding atas
                        .aspectRatio(3f / 4f)
                )
            }
        }
    )

    // Dialog Konfirmasi Kirim atau Batal
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false // Tutup dialog jika pengguna menekan di luar dialog
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
                                coroutineScope.launch(Dispatchers.IO) { // Gunakan Dispatchers.IO untuk operasi I/O
                                    // Simpan gambar ke penyimpanan publik
                                    saveBitmapToPublicPictures(context, bitmap)

                                    // Simpan waktu Clock In ke SharedPreferences
                                    viewModel.saveClockInTime(context, System.currentTimeMillis())

                                    withContext(Dispatchers.Main) { // Kembali ke Main Thread untuk navigasi dan UI
                                        Toast.makeText(context, "Gambar berhasil disimpan!", Toast.LENGTH_LONG).show()
                                        navController.navigate(Screen.PresensiSukses.route) // Navigasi ke layar sukses
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
}

// Fungsi untuk menyimpan bitmap ke penyimpanan publik
fun saveBitmapToPublicPictures(context: Context, bitmap: Bitmap): File {
    val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    if (!picturesDir.exists()) picturesDir.mkdirs()

    val photoFile = File(picturesDir, "photo_${System.currentTimeMillis()}.jpg")
    FileOutputStream(photoFile).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }
    return photoFile
}