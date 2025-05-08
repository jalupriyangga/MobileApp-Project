package com.example.mobileapptechnobit.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.ui.component.TopAppBar
import com.example.mobileapptechnobit.ui.theme.primary100
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun PermitFormScreen(modifier: Modifier = Modifier, navCtrl: NavController) {
    Scaffold (
        topBar = {
            Box(Modifier
                .height(130.dp)
                .graphicsLayer { clip = true }){
                TopAppBar()
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                ) {
                    PermissionFormTitle(navCtrl = navCtrl)
                }
            }
        },
    ){ padding ->
        Column (Modifier
            .padding(padding)
            .fillMaxSize()) {
            PermitForm(navCtrl = navCtrl)
        }
    }
}

@Composable
fun PermissionFormTitle(modifier: Modifier = Modifier, navCtrl: NavController) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp)
    ) {
        IconButton(
            onClick = { navCtrl.navigate("home_screen") },
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
            text = "Tambah Perizinan",
            textAlign = TextAlign.Center,
            fontFamily = robotoFontFamily,
            fontWeight = FontWeight(500),
            color = Color.White,
            fontSize = 25.sp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermitForm(modifier: Modifier = Modifier, navCtrl: NavController) {

    var jenisIzin by remember { mutableStateOf("Pilih") }
    val pilihanIzin = listOf("Sakit", "Izin", "Cuti")
    var keterangan by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    val initialDate = remember {
        "%02d/%02d/%04d".format(currentDay, currentMonth + 1, currentYear)
    }

//    var selectedDate by remember { mutableStateOf(initialDate) }
//    var showDialog by remember { mutableStateOf(false) }

    var showConfirmDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

//    val datePickerDialog = remember {
//        DatePickerDialog(
//            context,
//            { _, year, month, dayOfMonth ->
//                selectedDate = "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
//            },
//            currentYear, currentMonth, currentDay
//        ).apply {
//            // Tambahkan tombol Cancel agar dialog benar-benar tertutup
//            setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel") { dialog, _ ->
//                dialog.dismiss()
//            }
//        }
//    }

    val state = rememberDatePickerState()
    var showDialog by remember { mutableStateOf(false) }
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    var selectedDate by remember { mutableStateOf(formatter.format(Date())) }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = state.selectedDateMillis
                    if (millis != null) {
                        selectedDate = formatter.format(Date(millis))
                    }
                    showDialog = false
                }) {
                    Text("OK", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text("CANCEL", color = Color.Black)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White, // Latar dialog
                titleContentColor = Color.Blue, // "Select date"
                headlineContentColor = Color.Blue, // Tanggal besar (Apr 22, 2025)
                weekdayContentColor = Color.Black,
                subheadContentColor = primary100,
                selectedDayContainerColor = primary100, // Lingkaran biru di tanggal terpilih
                selectedDayContentColor = primary100,
                todayContentColor = primary100
            )
        ) {
            DatePicker(state = state)
        }
    }

    Column(
        Modifier
            .padding(top = 30.dp)
            .padding(horizontal = 25.dp)
            .padding(bottom = 30.dp)
            .fillMaxWidth()
    ){
        Text("Tanggal", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 16.sp)
        Spacer(Modifier.padding(top = 5.dp))
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            trailingIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(painter = painterResource(R.drawable.uiw_date), contentDescription = null, modifier = Modifier.size(25.dp))
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.padding(top = 20.dp))

        Text("Jenis Izin", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 16.sp)
        Spacer(Modifier.padding(top = 5.dp))
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded = !expanded }) {
            OutlinedTextField(
                value = jenisIzin,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown Icon"
                    )
                },
                modifier = Modifier.fillMaxWidth().menuAnchor().background(Color.White)

            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
                pilihanIzin.forEach{ opsi ->
                    DropdownMenuItem(
                        text = { Text(text = opsi) },
                        onClick = {
                            jenisIzin = opsi
                            expanded = false
                        },
                        modifier = Modifier
                            .fillMaxWidth().background(Color.White)
                    )
                }
            }
        }

        Spacer(Modifier.padding(top = 20.dp))
        Text("Keterangan", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 16.sp)
        Spacer(Modifier.padding(top = 5.dp))
        OutlinedTextField(
            value = keterangan,
            onValueChange = { keterangan = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text("Keterangan") },
            maxLines = 4
        )

        Spacer(Modifier.weight(1f))
        Button(
            onClick = { showConfirmDialog = true },
            Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(primary100),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(text = "Kirim", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 16.sp)
        }

        ConfirmDialog(
            showDialog = showConfirmDialog,
            navCtrl = navCtrl,
            onDismiss = { showDialog = false}
        )
    }
}

@Composable
fun ConfirmDialog(modifier: Modifier = Modifier, showDialog: Boolean, navCtrl: NavController, onDismiss: () -> Unit) {

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    text = "Kirim Perizinan?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = robotoFontFamily
                )
            },
            text = {
                Text(
                    text = "Pastikan formulir sudah sesuai!",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = robotoFontFamily
                )
            },
            confirmButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                navCtrl.navigate("success_screen/Perizinan Anda Berhasil Dikirim!/permission_screen")
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = primary100),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "Kirim",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                fontFamily = robotoFontFamily
                            )
                        }
                        OutlinedButton(
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.Gray),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "Batal",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = robotoFontFamily
                            )
                        }
                    }
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.background(Color.Transparent)
        )
    }
}

@Preview
@Composable
private fun PermitFormPrev() {
    PermitFormScreen(navCtrl = rememberNavController())
}