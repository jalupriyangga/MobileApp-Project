package com.example.mobileapptechnobit.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.ViewModel.ProfileViewModel
import com.example.mobileapptechnobit.ViewModel.ProfileViewModelFactory
import com.example.mobileapptechnobit.data.repository.ProfileRepository
import com.example.mobileapptechnobit.ui.theme.primary100
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(navController: NavController, token: String) {
    val context = LocalContext.current
    val repository = ProfileRepository(context)
    val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(repository))
    val profile by viewModel.employeesProfile.collectAsState()
    val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val authToken = sharedPref.getString("AUTH_TOKEN", null) ?: token

    var fullname by remember { mutableStateOf(profile?.fullname ?: "") }
    var nickname by remember { mutableStateOf((profile?.nickname ?: "")) }
    var phone by remember { mutableStateOf((profile?.phone ?: "")) }
    var gender by remember { mutableStateOf(profile?.gender ?: "") }
    var birthDate by remember { mutableStateOf((profile?.birthDate ?: "")) }
    var religion by remember { mutableStateOf(profile?.religion ?: "") }
    var bloodType by remember { mutableStateOf(profile?.bloodType ?: "") }
    var address by remember { mutableStateOf((profile?.address ?: "")) }
    var emergencyPhone by remember { mutableStateOf((profile?.emergencyPhone ?: "")) }

    val genderOptions = listOf("male", "female")
    val religionOptions = listOf("Islam", "Kristen", "Katolik", "Hindu", "Buddha", "Konghucu")
    val bloodTypeOptions = listOf("A", "B", "AB", "O")
    var genderExpanded by remember { mutableStateOf(false) }
    var religionExpanded by remember { mutableStateOf(false) }
    var bloodTypeExpanded by remember { mutableStateOf(false) }

    fullname = profile?.fullname ?: ""
    nickname = profile?.nickname ?: ""
    phone = profile?.phone ?: ""
    birthDate = profile?.birthDate ?: ""
    address = profile?.address ?: ""
    emergencyPhone = profile?.emergencyPhone ?: ""

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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = "Nama Lengkap",
                        fontSize = 16.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = fullname,
                        onValueChange = { fullname = it },
                        placeholder = { Text(text = profile?.fullname ?: "", fontSize = 16.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight.Medium) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.Black
                        )
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Nama Panggilan",
                        fontSize = 16.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        placeholder = { Text(text = profile?.nickname ?: "", fontSize = 16.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight.Medium) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.Black
                        )
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No. Handphone",
                        fontSize = 16.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text(text = profile?.phone ?: "", fontSize = 16.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight.Medium) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.Black
                        )
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Jenis Kelamin",
                        fontSize = 16.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = genderExpanded,
                        onExpandedChange = { genderExpanded = !genderExpanded }
                    ) {
                        OutlinedTextField(
                            value = TextFieldValue(gender.ifEmpty { profile?.gender ?: "" }),
                            onValueChange = { },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.Gray,
                                unfocusedBorderColor = Color.Gray,
                                cursorColor = Color.Black
                            ),
                            trailingIcon = {
                                Icon(
                                    imageVector = if (genderExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                    contentDescription = "Dropdown Icon"
                                )
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = genderExpanded,
                            onDismissRequest = { genderExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            genderOptions.forEach { genderOption ->
                                DropdownMenuItem(
                                    text = { Text(genderOption, color = Color.Black) },
                                    onClick = {
                                        gender = genderOption
                                        genderExpanded = false
                                    },
                                    modifier = Modifier
                                        .background(Color.White)
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Tanggal Lahir (thn-bln-hr)",
                        fontSize = 16.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = birthDate,
                        onValueChange = { birthDate = it },
                        placeholder = { Text(text = profile?.birthDate ?: "", fontSize = 16.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight.Medium) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.Black
                        ),
                        trailingIcon = {
                            Image(
                                modifier = Modifier .size(24.dp),
                                painter = painterResource(id = R.drawable.date_editprofile),
                                contentDescription = null
                            )
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Agama",
                        fontSize = 16.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = religionExpanded,
                        onExpandedChange = { religionExpanded = !religionExpanded }
                    ) {
                        OutlinedTextField(
                            value = TextFieldValue(religion.ifEmpty { profile?.religion ?: "" }),
                            onValueChange = { },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.Gray,
                                unfocusedBorderColor = Color.Gray,
                                cursorColor = Color.Black
                            ),
                            trailingIcon = {
                                Icon(
                                    imageVector = if (religionExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                    contentDescription = "Dropdown Icon"
                                )
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = religionExpanded,
                            onDismissRequest = { religionExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            religionOptions.forEach { religionOption ->
                                DropdownMenuItem(
                                    text = { Text(religionOption, color = Color.Black) },
                                    onClick = {
                                        religion = religionOption
                                        religionExpanded = false
                                    },
                                    modifier = Modifier
                                        .background(Color.White)
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }

                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Golongan Darah",
                        fontSize = 16.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = bloodTypeExpanded,
                        onExpandedChange = { bloodTypeExpanded = !bloodTypeExpanded }
                    ) {
                        OutlinedTextField(
                            value = TextFieldValue(bloodType.ifEmpty { profile?.bloodType ?: "" }),
                            onValueChange = { },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.Gray,
                                unfocusedBorderColor = Color.Gray,
                                cursorColor = Color.Black
                            ),
                            trailingIcon = {
                                Icon(
                                    imageVector = if (bloodTypeExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                    contentDescription = "Dropdown Icon"
                                )
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = bloodTypeExpanded,
                            onDismissRequest = { bloodTypeExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            bloodTypeOptions.forEach { bloodTypeOption ->
                                DropdownMenuItem(
                                    text = { Text(bloodTypeOption, color = Color.Black) },
                                    onClick = {
                                        bloodType = bloodTypeOption
                                        bloodTypeExpanded = false
                                    },
                                    modifier = Modifier
                                        .background(Color.White)
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Alamat",
                        fontSize = 16.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        placeholder = { Text(text = profile?.address ?: "", fontSize = 16.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight.Medium) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.Black
                        )
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No. Handphone Darurat",
                        fontSize = 16.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = emergencyPhone,
                        onValueChange = { emergencyPhone = it },
                        placeholder = { Text(text = profile?.emergencyPhone ?: "", fontSize = 16.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight.Medium) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.Black
                        )
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(25.dp))
                    Button(
                        onClick = {
                            Log.d("EditProfile", "Updating profile with: FullName=${fullname}, Nickname=${nickname}, Phone=${phone}, Gender=$gender, BirthDate=${birthDate}, Religion=$religion, BloodType=$bloodType, Address=${address}, Emergency Phone=${emergencyPhone}")
                            viewModel.updateProfile(
                                authToken,
                                fullname,
                                nickname,
                                phone,
                                gender,
                                birthDate,
                                religion,
                                bloodType,
                                address,
                                emergencyPhone
                            )
                            navController.navigate("edit_sukses_screen")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primary100),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(75.dp)
                            .padding(bottom = 16.dp)
                    ) {
                        Text(text = "Simpan", fontSize = 16.sp, fontWeight = FontWeight.Medium, fontFamily = robotoFontFamily)
                    }
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