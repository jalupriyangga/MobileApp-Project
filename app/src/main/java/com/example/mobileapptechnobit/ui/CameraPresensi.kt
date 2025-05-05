package com.example.mobileapptechnobit

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SwitchCamera
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import com.example.mobileapptechnobit.ViewModel.CameraPresViewModel
import com.example.mobileapptechnobit.ui.theme.black20
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraPresensi(private val context: Context) {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CameraScreen(viewModel: CameraPresViewModel, navController: NavHostController, token: String) {

        val controller = remember {
            LifecycleCameraController(context).apply {
                setEnabledUseCases(
                    CameraController.IMAGE_CAPTURE
                )
            }
        }
        val bitmaps by viewModel.capturedBitmap.collectAsState()
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val permissionsLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                permissions[Manifest.permission.CAMERA] == true) {
                Toast.makeText(context, "Permissions granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }

        LaunchedEffect(Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (
                        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        )) {
                permissionsLauncher.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA
                ))
            }
            controller.bindToLifecycle(lifecycleOwner)
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
                                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = androidx.compose.ui.graphics.Color.White)
                                }
                            }
                            Box(modifier = Modifier.weight(6f), contentAlignment = Alignment.Center) {
                                Text(
                                    text = "Presensi Masuk",
                                    color = androidx.compose.ui.graphics.Color.White,
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
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = colorResource(id = R.color.black100)
                    ),
                    modifier = Modifier.height(112.dp)
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(R.color.black100)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Camera Preview
                    // Camera Preview dengan frame dan arahan
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 4f),
                        contentAlignment = Alignment.Center // Untuk memastikan elemen di tengah
                    ) {
                        CameraPreview(controller = controller, modifier = Modifier.fillMaxSize())

                        // Tambahkan frame kamera dari drawable
                        Image(
                            painter = painterResource(id = R.drawable.camera_frame), // Frame kamera
                            contentDescription = "Camera Frame",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                        )

                        // Arahan untuk pengguna
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter) // Posisi text berada di dalam frame
                                .offset(y = (-56).dp) // Geser ke atas dengan offset negatif
                                .background(colorResource(id = R.color.black100).copy(alpha = 0.5f))
                                .padding(4.dp), // Padding untuk teks
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Pastikan wajah terlihat dengan jelas",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = robotoFontFamily,
                                color = androidx.compose.ui.graphics.Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }

                // Tombol di bawah (kamera tengah + switch kanan)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp, start = 32.dp, end = 32.dp)
                ) {
                    // Tombol kamera (posisi tengah)
                    IconButton(
                        onClick = {
                            takePhoto(
                                controller = controller,
                                context = context, // Pastikan Anda meneruskan context
                                token = token, // Pastikan token diteruskan
                                onPhotoTaken = { bitmap ->
                                    // Simpan bitmap ke ViewModel atau lakukan operasi lainnya
                                    viewModel.onTakePhoto(bitmap, token)
                                },
                                navController = navController // Teruskan navController untuk navigasi
                            )
                        },
                        modifier = Modifier
                            .size(72.dp)
                            .align(Alignment.Center)
                            .background(color = androidx.compose.ui.graphics.Color.White, shape = CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.camera), // Ganti `ic_camera` dengan nama resource Anda
                            contentDescription = "Ambil Foto",
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            controller.cameraSelector =
                                if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                    CameraSelector.DEFAULT_FRONT_CAMERA
                                } else CameraSelector.DEFAULT_BACK_CAMERA
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .align(Alignment.CenterEnd)
                            .background(color = androidx.compose.ui.graphics.Color.White, shape = CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.camera_switch), // Ganti `ic_camera` dengan nama resource Anda
                            contentDescription = "Ganti Kamera",
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }

        }
    }

    private fun takePhoto(
        controller: LifecycleCameraController,
        context: Context,
        token: String,
        onPhotoTaken: (Bitmap) -> Unit,
        navController: NavHostController
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(context),
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }

                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(), 0, 0, image.width, image.height, matrix, true
                    )

                    val location = getCurrentLocation()
                    val address = getAddressFromLocation(location)

                    val bitmapWithWatermark = addWatermark(
                        context = context,
                        bitmap = rotatedBitmap,
                        location = location,
                        address = address
                    )

                    onPhotoTaken(bitmapWithWatermark)

                    // Navigasi hanya setelah data disimpan
                    navController.navigate(Screen.CameraPresensiCheck.route)

                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Toast.makeText(context, "Gagal mengambil gambar: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(): Location? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val location = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || location.accuracy < bestLocation.accuracy) {
                bestLocation = location
            }
        }
        return bestLocation
    }

    private fun getAddressFromLocation(location: Location?): String {
        return if (location != null) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    addresses[0].getAddressLine(0)
                } else {
                    "Alamat tidak ditemukan"
                }
            } catch (e: Exception) {
                Log.e("CameraPresensi", "Error getting address: ${e.message}", e)
                "Error mendapatkan alamat"
            }
        } else {
            "Lokasi tidak tersedia"
        }
    }

    private fun addWatermark(context: Context, bitmap: Bitmap, location: Location?, address: String): Bitmap {
        val result = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)
        val paint = Paint().apply {
            color = Color.RED
            textSize = 60f
            isAntiAlias = true
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dateTime = sdf.format(Date())

        val locationText = "Lat: ${location?.latitude ?: "Unknown"}, Lng: ${location?.longitude ?: "Unknown"}"
        val addressText = address

        canvas.drawText(dateTime, 10f, canvas.height - 150f, paint)
        canvas.drawText(locationText, 10f, canvas.height - 80f, paint)
        canvas.drawText(addressText, 10f, canvas.height - 10f, paint)

        return result
    }

    @Composable
    fun CameraPreview(controller: LifecycleCameraController, modifier: Modifier = Modifier) {
        val lifecycleOwner = LocalLifecycleOwner.current
        AndroidView(
            factory = { context ->
                PreviewView(context).apply {
                    this.controller = controller
                    controller.bindToLifecycle(lifecycleOwner)
                }
            },
            modifier = modifier
        )
    }
}