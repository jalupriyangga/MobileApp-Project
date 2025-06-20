package com.example.mobileapptechnobit.ui.feature_history

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.mobileapptechnobit.ViewModel.ProfileViewModel
import com.example.mobileapptechnobit.ViewModel.ProfileViewModelFactory
import com.example.mobileapptechnobit.data.remote.HistoryPatroliResponseItem
import com.example.mobileapptechnobit.data.repository.ProfileRepository
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily

// DetailPatroliScreen.kt - Responsive Design
@Composable
fun DetailPatroliScreen(navCtrl: NavController, token: String) {
    var hasShownToast by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val context = LocalContext.current
    val repository = ProfileRepository(context)
    val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(repository))
    val employeeProfile by viewModel.employeesProfile.collectAsState()
    val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val authToken = sharedPref.getString("AUTH_TOKEN", null) ?: token

    LaunchedEffect(authToken) {
        println("Fetching profile with token: $authToken")
        viewModel.fetchEmployeesProfile(authToken)
        Log.d("DPS", "Token yang diterima: $authToken")
    }

    val presenceItem = navCtrl.previousBackStackEntry!!
        ?.savedStateHandle?.get<HistoryPatroliResponseItem>("presenceItem")
    val formattedDate = navCtrl.previousBackStackEntry
        ?.savedStateHandle?.get<String>("formattedDate")

    if (presenceItem == null) {
        return
    }
    val nama = employeeProfile?.fullname

    Log.d(
        "DetailPatroliScreen",
        "Nama Karyawan: $nama, Tanggal: $formattedDate, Status: ${presenceItem.status}, Lokasi: ${presenceItem.patrolLocation}, Shift: ${presenceItem.shiftId}"
    )
    val tanggal = formattedDate!!
    val status = presenceItem.status
    val lokasi = presenceItem.patrolLocation
    val shift = presenceItem.shiftId.toString() ?: "Pagi"
    var artiKodeShift : String = ""
    val catatan = presenceItem.catatan ?: "Tidak ada catatan"
    val foto = presenceItem.photoUrl

    Scaffold(
        topBar = {
            Box {
                Column(Modifier.fillMaxWidth()) {
                    val linkfoto = "$foto"
                    Log.d("DetailPatroliScreen", "Link Foto: $linkfoto")
                    HistoryPatroliTitle(modifier = Modifier, navCtrl)
                    Column(Modifier.background(Color.White).padding(horizontal = 24.dp)) {
                        when(shift){
                            "1" -> {
                                artiKodeShift = "Pagi"
                            }
                            "2" -> {
                                artiKodeShift = "Siang"
                            }
                            else -> {artiKodeShift = "Malam"}
                        }
                        HistoryPatroli(nama, tanggal, status, lokasi, artiKodeShift, catatan, linkfoto, screenWidth, screenHeight)
                        ActionButtonPatroli(nama, tanggal, status, lokasi, artiKodeShift, catatan, foto, linkfoto, screenWidth)
                    }
                }
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {}
    }
}

@Composable
fun HistoryPatroliTitle(modifier: Modifier = Modifier, navCtrl: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = (screenWidth * 0.076f))
    ) {
        IconButton(
            onClick = {
                navCtrl.popBackStack()
            },
            Modifier.padding(start = (screenWidth * 0.013f))
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back button",
                tint = Color.Black,
                modifier = Modifier.size((screenWidth * 0.060f))
            )
        }
    }
}

@Composable
fun HistoryPatroli(
    nama: String?,
    tanggal: String,
    status: String,
    lokasi: String,
    shift: String,
    catatan: String,
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
            Spacer(modifier = Modifier.height(screenWidth * 0.076f))
            Text(
                text = "Detail Patroli",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = (screenWidth.value * 0.064f).sp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.02f))
            Text(
                text = "Nama Karyawan \t:\t $nama",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = (screenWidth.value * 0.046f).sp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.02f))
            Text(
                text = "Tanggal \t :\t$tanggal",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = (screenWidth.value * 0.025f).sp
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
                fontSize = (screenWidth.value * 0.030f).sp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.01f))
            Text(
                text = "$status",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = (screenWidth.value * 0.030f).sp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.02f))
            Text(
                text = "Lokasi",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = (screenWidth.value * 0.030f).sp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.01f))
            Text(
                text = "$lokasi",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = (screenWidth.value * 0.030f).sp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.02f))
            Text(
                text ="Shift",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = (screenWidth.value * 0.030f).sp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.01f))
            Text(
                text = shift,
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = (screenWidth.value * 0.030f).sp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.02f))
            Text(
                text ="Catatan",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = (screenWidth.value * 0.030f).sp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.01f))
            Text(
                text = catatan,
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = (screenWidth.value * 0.030f).sp
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.02f))
            AsyncImage(
                model = foto,
                contentDescription = "Foto Patroli",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.25f)
            )
            Spacer(modifier = Modifier.height(screenWidth * 0.04f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPatroliScreenPreview() {
    DetailPresensiScreen(navCtrl = rememberNavController())
}