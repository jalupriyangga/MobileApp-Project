package com.example.mobileapptechnobit

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
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
import android.os.Build
import android.os.Environment
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mobileapptechnobit.ViewModel.CameraPresViewModel
import com.example.mobileapptechnobit.ui.CameraPatTitle
import com.example.mobileapptechnobit.ui.RequestLocationPermissions
import com.example.mobileapptechnobit.ui.theme.black20
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraPresensi(viewModel: CameraPresViewModel, navController: NavHostController, token: String) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }
    val bitmaps by viewModel.capturedBitmap.collectAsState()
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

    RequestLocationPermissions()

    Scaffold(
        topBar = {
            Box {
                TopAppBar(
                    title = {},
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = colorResource(id = R.color.black100)
                    ),
                    modifier = Modifier.height(112.dp)
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                ) {
                    CameraPresTitle(navCtrl = navController)
                }
            }
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f),
                    contentAlignment = Alignment.Center
                ) {
                    CameraPreview(controller = controller, modifier = Modifier.fillMaxSize())

                    Image(
                        painter = painterResource(id = R.drawable.camera_frame),
                        contentDescription = "Camera Frame",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = (-56).dp)
                            .background(colorResource(id = R.color.black100).copy(alpha = 0.5f))
                            .padding(4.dp),
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp, start = 32.dp, end = 32.dp)
            ) {
                IconButton(
                    onClick = {
                        triggerVibration(context)
                        takePhoto(
                            controller = controller,
                            context = context,
                            token = token,
                            onPhotoTaken = { bitmap ->
                                viewModel.onTakePhoto(bitmap, token)
                            },
                            navController = navController
                        )
                    },
                    modifier = Modifier
                        .size(72.dp)
                        .align(Alignment.Center)
                        .background(color = androidx.compose.ui.graphics.Color.White, shape = CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.camera),
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
                        painter = painterResource(id = R.drawable.camera_switch),
                        contentDescription = "Ganti Kamera",
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }

    }
}

@Composable
fun CameraPresTitle(modifier: Modifier = Modifier, navCtrl: NavController) {
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
            text = "Presensi Masuk",
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

                val location = getCurrentLocation(context)
                val address = getAddressFromLocation(context, location)

                val bitmapWithWatermark = addWatermark(
                    context = context,
                    bitmap = rotatedBitmap,
                    location = location,
                    address = address
                )
                onPhotoTaken(bitmapWithWatermark)
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
private fun getCurrentLocation(context: Context): Location? {
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

private fun getAddressFromLocation(context: Context, location: Location?): String {
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
        textSize = 85f
        isAntiAlias = true
    }

    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val dateTime = sdf.format(Date())

    val locationText = "Lat: ${location?.latitude ?: "Unknown"}, Lng: ${location?.longitude ?: "Unknown"}"
    val addressText = address

    val fullText = "$dateTime\n$locationText\n$addressText"

    val x = 10f
    val y = canvas.height - 500f

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val staticLayout = android.text.StaticLayout.Builder
            .obtain(fullText, 0, fullText.length, android.text.TextPaint(paint), canvas.width - 20)
            .setAlignment(android.text.Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(1f, 1f)
            .setIncludePad(false)
            .build()
        canvas.save()
        canvas.translate(x, y)
        staticLayout.draw(canvas)
        canvas.restore()
    } else {
        val lines = fullText.split("\n")
        var currentY = y
        for (line in lines) {
            canvas.drawText(line, x, currentY, paint)
            currentY += paint.textSize + 10
        }
    }
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

private fun triggerVibration(context: Context) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(200)
    }
}