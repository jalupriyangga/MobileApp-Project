package com.example.mobileapptechnobit.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.ViewModel.ProfileViewModel
import com.example.mobileapptechnobit.ViewModel.ProfileViewModelFactory
import com.example.mobileapptechnobit.data.repository.ProfileRepository
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString

fun formatCurrency(value: String?): String {
    if (value.isNullOrBlank()) return "-"
    return try {
        val number = value.toDouble()
        val symbols = DecimalFormatSymbols().apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        }
        val formatter = DecimalFormat("#,##0.00", symbols)
        "Rp${formatter.format(number)}"
    } catch (e: NumberFormatException) {
        value
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProfileScreen(navController: NavController, token: String) {
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
                    DetailProfileTitle(navCtrl = navController)
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            employeeProfile?.let { profileData ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    items(
                        listOf(
                            "Nama Lengkap" to (profileData.fullname ?: "-"),
                            "Nama Panggilan" to (profileData.nickname ?: "-"),
                            "NIP" to (profileData.idNumber ?: "-"),
                            "Jabatan" to (profileData.position ?: "-"),
                            "Departemen" to (profileData.department ?: "-"),
                            "Status Kerja" to (profileData.employmentStatus ?: "-"),
                            "No. Handphone" to (profileData.phone ?: "-"),
                            "Jenis Kelamin" to (profileData.gender ?: "-"),
                            "Tanggal Lahir" to (profileData.birthDate ?: "-"),
                            "Agama" to (profileData.religion ?: "-"),
                            "Golongan Darah" to (profileData.bloodType ?: "-"),
                            "Alamat" to (profileData.address ?: "-"),
                            "Gaji" to formatCurrency(profileData.salary)
                        )
                    ) { item ->
                        val (label, value) = item
                        ProfileAttributeCard(label, value)
                    }


                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfileEditCard(navController)
                    }
                }
            } ?: run {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun DetailProfileTitle(modifier: Modifier = Modifier, navCtrl: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp)
    ) {
        IconButton(
            onClick = { navCtrl.navigate("profile_screen") },
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
            text = "Profil Anda",
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

@Composable
fun ProfileAttributeCard(label: String, value: String) {

    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = robotoFontFamily
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = robotoFontFamily,
                modifier = Modifier.then(
                    if (label.equals("No. Handphone", ignoreCase = true)){
                        Modifier.clickable { clipboardManager.setText(AnnotatedString(value)) }
                    }
                    else{
                        Modifier
                    }
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Divider(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth(),
                color = Color.LightGray,
            )
        }
    }
}

@Composable
fun ProfileEditCard(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("edit_profile_screen") }
            .padding(horizontal = 16.dp).padding(bottom = 30.dp)
            .shadow(12.dp, RoundedCornerShape(16.dp), clip = true),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.edt_profile),
                    contentDescription = "edit",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    fontSize = 14.sp,
                    text = "Edit Profil",
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontFamily = robotoFontFamily
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Arrow",
                tint = Color.Blue,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=412dp, height=915dp, dpi=440")
@Composable
fun DetailProfileScreenPreview() {
    DetailProfileScreen(navController = rememberNavController(), token = "dummy_token")
}