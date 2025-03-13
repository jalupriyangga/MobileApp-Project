package com.example.mobileapptechnobit.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapptechnobit.ViewModel.ProfileViewModel
import com.example.mobileapptechnobit.ViewModel.ProfileViewModelFactory
import com.example.mobileapptechnobit.data.repository.ProfileRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(navController: NavController, token: String) {
    val context = LocalContext.current
    val repository = ProfileRepository(context)
    val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(repository))
    val employeeProfile by viewModel.employeesProfile.collectAsState()
    val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val authToken = sharedPref.getString("AUTH_TOKEN", null) ?: token

    var fullname by remember { mutableStateOf(TextFieldValue(employeeProfile?.fullname ?: "")) }
    var nickname by remember { mutableStateOf(TextFieldValue(employeeProfile?.nickname ?: "")) }
    var phone by remember { mutableStateOf(TextFieldValue(employeeProfile?.phone ?: "")) }
    var gender by remember { mutableStateOf(TextFieldValue(employeeProfile?.gender ?: "")) }
    var birthDate by remember { mutableStateOf(TextFieldValue(employeeProfile?.birthDate ?: "")) }
    var religion by remember { mutableStateOf(TextFieldValue(employeeProfile?.religion ?: "")) }
    var bloodType by remember { mutableStateOf(TextFieldValue(employeeProfile?.bloodType ?: "")) }
    var address by remember { mutableStateOf(TextFieldValue(employeeProfile?.address ?: "")) }
    var emergencyPhone by remember { mutableStateOf(TextFieldValue(employeeProfile?.emergencyPhone ?: "")) }

    LaunchedEffect(authToken) {
        viewModel.fetchEmployeesProfile(authToken)
        Log.d("EditProfile", "Token yang diterima: $authToken")
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
                                text = "Edit Profil",
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = fullname,
                    onValueChange = { fullname = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Nickname") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = { Text("Gender") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = birthDate,
                    onValueChange = { birthDate = it },
                    label = { Text("Birth Date") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = religion,
                    onValueChange = { religion = it },
                    label = { Text("Religion") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = bloodType,
                    onValueChange = { bloodType = it },
                    label = { Text("Blood Type") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = emergencyPhone,
                    onValueChange = { emergencyPhone = it },
                    label = { Text("Emergency Phone") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        Log.d("EditProfile", "Updating profile with: FullName=${fullname.text}, Nickname=${nickname.text}, Phone=${phone.text}, Gender=${gender.text}, BirthDate=${birthDate.text}, Religion=${religion.text}, BloodType=${bloodType.text}, Address=${address.text}, Emergency Phone=${emergencyPhone.text}")
                        viewModel.updateProfile(
                            authToken,
                            fullname.text,
                            nickname.text,
                            phone.text,
                            gender.text,
                            birthDate.text,
                            religion.text,
                            bloodType.text,
                            address.text,
                            emergencyPhone.text
                        )
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = "Simpan", fontSize = 16.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=412dp, height=915dp, dpi=440")
@Composable
fun EditProfilePreview() {
    EditProfile(navController = rememberNavController(), token = "dummy_token")
}