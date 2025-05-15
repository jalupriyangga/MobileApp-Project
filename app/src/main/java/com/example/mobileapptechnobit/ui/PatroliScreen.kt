package com.example.mobileapptechnobit.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.Screen
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatroliScreen(
    navCtrl: NavController,
    activity: Activity
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val token = sharedPref.getString("AUTH_TOKEN", "") ?: ""

    var hasPermissions by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasPermissions = permissions[Manifest.permission.CAMERA] == true
    }

    LaunchedEffect(Unit) {
        if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
        } else {
            hasPermissions = true
        }
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
                    PatroliTitle(navCtrl = navCtrl)
                }
            }
        }
    ) { innerPadding ->
        if (hasPermissions) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                CameraPreviewWithQRReaderWithFrame(
                    onQRCodeScanned = { qrCode ->
                        Toast.makeText(activity, "QR Code: $qrCode", Toast.LENGTH_LONG).show()
                        val qrToken = "exampleQrToken/with_special_characters"
                        val encodedQrToken = Uri.encode(qrToken)

                        navCtrl.navigate(Screen.CameraPatroli.route.replace("{qrToken}", encodedQrToken))                    }
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Camera permission is required to use this feature.")
            }
        }
    }
}

@Composable
fun PatroliTitle(modifier: Modifier = Modifier, navCtrl: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp)
    ) {
        IconButton(
            onClick = { navCtrl.navigate(Screen.Home.route) },
            Modifier.padding(start = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back button",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = "Scan Kode QR",
            textAlign = TextAlign.Center,
            fontFamily = robotoFontFamily,
            fontWeight = androidx.compose.ui.text.font.FontWeight(500),
            color = Color.White,
            fontSize = 25.sp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )
    }
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@Composable
fun CameraPreviewWithQRReaderWithFrame(onQRCodeScanned: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isFlashOn by remember { mutableStateOf(false) }
    var cameraControl by remember { mutableStateOf<androidx.camera.core.CameraControl?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = androidx.camera.core.Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val cameraSelector = androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
                    val imageAnalysis = androidx.camera.core.ImageAnalysis.Builder()
                        .setBackpressureStrategy(androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    imageAnalysis.setAnalyzer(
                        java.util.concurrent.Executors.newSingleThreadExecutor()
                    ) { imageProxy ->
                        val mediaImage = imageProxy.image
                        if (mediaImage != null) {
                            try {
                                val inputImage = InputImage.fromMediaImage(
                                    mediaImage,
                                    imageProxy.imageInfo.rotationDegrees
                                )
                                val scanner = BarcodeScanning.getClient()
                                scanner.process(inputImage)
                                    .addOnSuccessListener { barcodes ->
                                        for (barcode in barcodes) {
                                            if (barcode.valueType == Barcode.TYPE_TEXT || barcode.rawValue != null) {
                                                onQRCodeScanned(barcode.rawValue ?: "")
                                                imageProxy.close()
                                                return@addOnSuccessListener
                                            }
                                        }
                                        imageProxy.close()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("QRScanner", "Error processing image: ${e.message}")
                                        imageProxy.close()
                                    }
                            } catch (e: Exception) {
                                imageProxy.close()
                            }
                        } else {
                            imageProxy.close()
                        }
                    }

                    val camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )

                    cameraControl = camera.cameraControl
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.patroli_frame),
                    contentDescription = "Camera Frame",
                    modifier = Modifier
                        .offset(y = -55.dp)
                        .size(302.dp)
                )

                Box(
                    modifier = Modifier
                        .offset(y = 190.dp)
                        .background(colorResource(id = R.color.black100).copy(alpha = 0.5f))
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Arahkan kamera ke QR code",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = robotoFontFamily,
                        color = androidx.compose.ui.graphics.Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp, end = 50.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            IconButton(
                onClick = { isFlashOn = !isFlashOn },
                modifier = Modifier.size(50.dp)
            ) {
                Image(
                    painter = painterResource(
                        id = if (isFlashOn) R.drawable.patroli_flashon else R.drawable.patroli_flashoff
                    ),
                    contentDescription = if (isFlashOn) "Turn off flash" else "Turn on flash",
                )
            }
        }
    }

    LaunchedEffect(isFlashOn) {
        cameraControl?.enableTorch(isFlashOn)
    }
}

@Preview(showBackground = true, device = "spec:width=412dp, height=915dp, dpi=440")
@Composable
fun PatroliScreenPreview() {
    val navController = rememberNavController()
    val dummyActivity = Activity()

    PatroliScreen(
        navCtrl = navController,
        activity = dummyActivity
    )
}