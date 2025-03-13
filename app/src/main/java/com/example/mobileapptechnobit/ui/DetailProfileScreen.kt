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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                            }
                        }
                        Box(modifier = Modifier.weight(6f), contentAlignment = Alignment.Center) {
                            Text(
                                text = "Profil Anda",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontSize = 22.sp
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                        }
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF2752E7)
                ),
                modifier = Modifier.height(120.dp),
            )
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
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(
                        listOf(
                            "Full Name" to profileData.fullname,
                            "Nickname" to profileData.nickname,
                            "Phone" to profileData.phone,
                            "Gender" to profileData.gender,
                            "Birth Date" to profileData.birthDate,
                            "Religion" to profileData.religion,
                            "Blood Type" to profileData.bloodType,
                            "ID Number" to profileData.idNumber,
                            "Address" to profileData.address,
                            "Department" to profileData.department,
                            "Position" to profileData.position,
                            "Employment Status" to profileData.employmentStatus,
                            "Salary" to profileData.salary
                        )
                    ) { item ->
                        val (label, value) = item
                        ProfileAttributeCard(label, value)
                    }

                    // Kartu Edit Profil di bagian bawah
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { navController.navigate("edit_profile_screen") },
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
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
                                        color = Color.Black
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
fun ProfileAttributeCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=412dp, height=915dp, dpi=440")
@Composable
fun DetailProfileScreenPreview() {
    DetailProfileScreen(navController = rememberNavController(), token = "dummy_token")
}