package com.example.mobileapptechnobit.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.ViewModel.AuthViewModel
import com.example.mobileapptechnobit.ViewModel.AuthViewModelFactory
import com.example.mobileapptechnobit.data.repository.AuthRepository
import com.example.mobileapptechnobit.ui.theme.primary100
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily
import com.example.mobileapptechnobit.ViewModel.LoginState
import com.example.mobileapptechnobit.ViewModel.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel(), onLoginSuccess: () -> Unit, navCtrl: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val repository = remember { AuthRepository() }
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(repository = repository))
    val loginMessage by authViewModel.loginMessage.observeAsState()

    LaunchedEffect(viewModel.loginState) {
        when (viewModel.loginState) {
            LoginState.SUCCESS -> {
                Toast.makeText(context, "Login Berhasil", Toast.LENGTH_LONG).show()
                onLoginSuccess()
            }
            LoginState.ERROR -> Toast.makeText(context, "Login Gagal", Toast.LENGTH_LONG).show()
            LoginState.FORBIDDEN -> Toast.makeText(context, "Akun anda salah", Toast.LENGTH_LONG).show()
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Masuk", fontSize = 22.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(13.dp))
                Text("Masuk ke akun anda selanjutnya", fontSize = 16.sp, fontFamily = robotoFontFamily)
            }

            Column(
                modifier = Modifier.padding(top = 40.dp, start = 40.dp)
            ) {
                Text("Email", fontSize = 16.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight.Medium)
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Masukkan email anda") },
                    modifier = Modifier.fillMaxWidth().padding(end = 40.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text("Password", fontSize = 16.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight.Medium)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Masukkan password anda") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Image(
                            painter = painterResource(id = if (passwordVisible) R.drawable.eyes_closed else R.drawable.eyes_open),
                            contentDescription = "Toggle Password Visibility",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { passwordVisible = !passwordVisible }
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(end = 40.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.Black
                    )
                )

                Text(
                    text = "Ubah Password",
                    textDecoration = TextDecoration.Underline,
                    color = primary100,
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .clickable {
                            navCtrl.navigate("forgot_password_screen")
                        }
                        .align(Alignment.End)
                        .padding(40.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
//                            viewModel.performLogin(email, password)
                            authViewModel.login(email, password)
                        } else {
                            Toast.makeText(context, "Email dan password harus diisi", Toast.LENGTH_LONG).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primary100),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp).padding(end = 40.dp)
                ) {
                    Text("Masuk", fontSize = 16.sp, fontFamily = robotoFontFamily, color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=412dp, height=915dp, dpi=440")
@Composable
fun LoginScreenPreview() {
    LoginScreen(onLoginSuccess = {}, navCtrl = rememberNavController())
}