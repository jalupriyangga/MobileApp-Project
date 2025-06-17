package com.example.mobileapptechnobit.ui.feature_history

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.mobileapptechnobit.data.remote.HistoryPresensiResponseItem
import com.example.mobileapptechnobit.ui.theme.primary100
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily
import kotlinx.coroutines.launch

// DetailPresensiScreen.kt - Responsive Design
@Composable
fun DetailPresensiScreen(navCtrl: NavController) {
    var hasShownToast by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val presenceItem = navCtrl.previousBackStackEntry!!
        ?.savedStateHandle?.get<HistoryPresensiResponseItem>("presenceItem")
    val formattedDate = navCtrl.previousBackStackEntry
        ?.savedStateHandle?.get<String>("formattedDate")

    if (presenceItem == null) {
        return
    }
    val nama = presenceItem.nama
    val tanggal = formattedDate!!
    val status = presenceItem.status
    val lokasi = presenceItem.lokasi
    val shift = presenceItem.shift ?: "Pagi"
    val foto = presenceItem.foto

    Scaffold(
        topBar = {
            Box {
                Column(Modifier.fillMaxWidth()) {
                    val linkfoto = "https://app.arunikaprawira.com/storage/$foto"
                    Log.d(
                        "DetailPresensiScreen",
                        "Link foto: $linkfoto, Nama: $nama, Tanggal: $tanggal, Status: $status, Lokasi: $lokasi, Shift: $shift"
                    )
                    HistoryPresensiTitle(modifier = Modifier, navCtrl)
                    Column(Modifier.background(Color.White).padding(horizontal = 24.dp)) {
                        HistoryPresensi(nama, tanggal, status, lokasi, shift, linkfoto, screenWidth, screenHeight)
                        ActionButtonPresensi(nama, tanggal, status, lokasi, shift, linkfoto, linkfoto, screenWidth)
                    }
                }
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {}
    }
}

@Composable
fun HistoryPresensiTitle(modifier: Modifier = Modifier, navCtrl: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = (screenWidth * 0.076f)) // Responsive padding
    ) {
        IconButton(
            onClick = {
                navCtrl.popBackStack()
            },
            Modifier.padding(start = (screenWidth * 0.013f)) // Responsive padding
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back button",
                tint = Color.Black,
                modifier = Modifier.size((screenWidth * 0.035f)) // Responsive icon size
            )
        }
    }
}

@Composable
fun HistoryPresensi(
    nama: String,
    tanggal: String,
    status: String,
    lokasi: String,
    shift: String,
    foto: String?,
    screenWidth: Dp,
    screenHeight: Dp
) {
    Column {
        Column(
            modifier = Modifier
                .padding()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(screenWidth * 0.076f)) // Responsive spacing
            Text(
                text = "Detail Presensi",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = (screenWidth.value *  0.064f).sp // Responsive font size
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.02f))
            Text(
                text = "Nama Karyawan \t:\t $nama",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = (screenWidth.value * 0.046f).sp // Responsive font size
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.02f))
            Text(
                text = "Tanggal \t :\t$tanggal",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = (screenWidth.value *  0.025f).sp // Responsive font size
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.02f))
            HorizontalDivider(
                modifier = Modifier.padding(start = screenWidth * 0.02f, end = screenWidth * 0.02f),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.02f))
        }

        Column(
            modifier = Modifier
                .padding(horizontal = screenWidth * 0.02f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Status",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = (screenWidth.value *  0.030f).sp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.01f))
            Text(
                text = "$status",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = (screenWidth.value *  0.030f).sp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.02f))
            Text(
                text = "Lokasi",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = (screenWidth.value *  0.030f).sp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.01f))
            Text(
                text = "$lokasi",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = (screenWidth.value *  0.030f).sp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.02f))
            Text(
                text ="Shift",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = (screenWidth.value *  0.030f).sp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.01f))
            Text(
                text = "$shift",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = (screenWidth.value *  0.030f).sp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.02f))
            AsyncImage(
                model = foto,
                contentDescription = "Foto Presensi",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.25f) // Responsive image height
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.04f))
        }
    }
}

@Composable
fun ActionButtonPatroli(
    nama: String?,
    tanggal: String,
    status: String,
    lokasi: String,
    shift: String,
    catatan : String,
    foto: String?,
    linkfoto: String
) {
    var pdfUri by remember { mutableStateOf<Uri?>(null) }

    val pdfGenerator = PdfGenerator()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        androidx.compose.material3. Button(
            onClick = {
                scope.launch {
                    val uri = pdfGenerator.generatePresensiPDFPatroli(context, nama, tanggal, status, lokasi, shift, catatan, foto)
                    if (uri != null) pdfUri = uri
                }
            },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primary100)
        ){
            Icon(Icons.Default.SaveAlt, contentDescription = "Download", tint = Color.White)
        }

        androidx.compose.material3.Button(
            onClick = {
                if (pdfUri != null) {
                    pdfGenerator.sharePdf(context, pdfUri!!)
                } else {
                    Toast.makeText(context, "PDF belum dibuat", Toast.LENGTH_SHORT).show()
                }
            },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primary100)
        ) {
            Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
        }
    }
}

// Responsive Action Buttons
@Composable
fun ActionButtonPatroli(
    nama: String?,
    tanggal: String,
    status: String,
    lokasi: String,
    shift: String,
    catatan : String,
    foto: String?,
    linkfoto: String,
    screenWidth: Dp
) {
    var pdfUri by remember { mutableStateOf<Uri?>(null) }

    val pdfGenerator = PdfGenerator()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = screenWidth * 0.02f, vertical = screenWidth * 0.025f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        androidx.compose.material3.Button(
            onClick = {
                scope.launch {
                    val uri = pdfGenerator.generatePresensiPDFPatroli(context, nama, tanggal, status, lokasi, shift, catatan, foto)
                    if (uri != null) pdfUri = uri
                }
            },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(screenWidth * 0.08f),
            colors = ButtonDefaults.buttonColors(containerColor = primary100)
        ){
            Icon(Icons.Default.SaveAlt, contentDescription = "Download", tint = Color.White)
        }

        androidx.compose.material3.Button(
            onClick = {
                if (pdfUri != null) {
                    pdfGenerator.sharePdf(context, pdfUri!!)
                } else {
                    Toast.makeText(context, "PDF belum dibuat", Toast.LENGTH_SHORT).show()
                }
            },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(screenWidth * 0.08f),
            colors = ButtonDefaults.buttonColors(containerColor = primary100)
        ) {
            Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
        }
    }
}

@Composable
fun ActionButtonPresensi(
    nama: String?,
    tanggal: String,
    status: String,
    lokasi: String,
    shift: String,
    foto: String?,
    linkfoto: String,
    screenWidth: Dp
) {
    var pdfUri by remember { mutableStateOf<Uri?>(null) }

    val pdfGenerator = PdfGenerator()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = screenWidth * 0.02f, vertical = screenWidth * 0.025f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        androidx.compose.material3.Button(
            onClick = {
                scope.launch {
                    val uri = pdfGenerator.generatePresensiPDFPresensi(context, nama, tanggal, status, lokasi, shift, foto)
                    if (uri != null) pdfUri = uri
                }
            },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(screenWidth * 0.08f),
            colors = ButtonDefaults.buttonColors(containerColor = primary100)
        ){
            Icon(Icons.Default.SaveAlt, contentDescription = "Download", tint = Color.White)
        }

        androidx.compose.material3.Button(
            onClick = {
                if (pdfUri != null) {
                    pdfGenerator.sharePdf(context, pdfUri!!)
                } else {
                    Toast.makeText(context, "PDF belum dibuat", Toast.LENGTH_SHORT).show()
                }
            },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(screenWidth * 0.08f),
            colors = ButtonDefaults.buttonColors(containerColor = primary100)
        ) {
            Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPresenceScreenPreview() {
    DetailPresensiScreen(navCtrl = rememberNavController())
}