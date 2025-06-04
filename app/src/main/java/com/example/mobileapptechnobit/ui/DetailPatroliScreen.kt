package com.example.mobileapptechnobit.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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

@Composable
fun DetailPatroliScreen(navCtrl: NavController) {
    var hasShownToast by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val repository = ProfileRepository(context)
    val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(repository))
    val profile by viewModel.employeesProfile.collectAsState()

    val presenceItem = navCtrl.previousBackStackEntry!!
        ?.savedStateHandle?.get<HistoryPatroliResponseItem>("presenceItem")
    val formattedDate = navCtrl.previousBackStackEntry
        ?.savedStateHandle?.get<String>("formattedDate")

    if (presenceItem == null) {
        return
    }
    val nama = profile?.fullname
    Log.d(
        "DetailPatroliScreen",
        "Nama Karyawan: $nama, Tanggal: $formattedDate, Status: ${presenceItem.status}, Lokasi: ${presenceItem.patrolLocation}, Shift: ${presenceItem.shiftId}"
    )
    val tanggal = formattedDate!!
    val status = presenceItem.status
    val lokasi = presenceItem.patrolLocation
    val shift = presenceItem.shiftId.toString() ?: "Pagi"
    val foto = presenceItem.photoUrl

    Scaffold(
        topBar = {
            Box {
                Column(Modifier.fillMaxWidth()) {
                    val linkfoto = "https://app.arunikaprawira.com$foto"
                    HistoryPatroliTitle(modifier = Modifier, navCtrl)
                    Column(Modifier.background(Color.White)) {
                        HistoryPatroli(nama, tanggal, status, lokasi, shift, linkfoto)
                        ActionButton(nama, tanggal, status, lokasi, shift, linkfoto)
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp)
    ) {
        IconButton(
            onClick = {
                navCtrl.popBackStack()
            },
            Modifier.padding(start = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back button",
                tint = Color.Black,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun HistoryPatroli(nama: String?, tanggal: String, status: String, lokasi: String, shift: String, foto: String?) {
    Column {
        Column(
            modifier = Modifier
                .padding()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Detail Presensi",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Nama Karyawan \t:\t$nama",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Tanggal \t :\t$tanggal",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Status",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$status",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Lokasi",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$lokasi",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text ="Shift",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$shift",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = foto,
                contentDescription = "Foto Presensi",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPatroliScreenPreview() {
    DetailPresensiScreen(navCtrl = rememberNavController())
}