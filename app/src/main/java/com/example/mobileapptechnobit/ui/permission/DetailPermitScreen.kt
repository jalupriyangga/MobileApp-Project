package com.example.mobileapptechnobit.ui.permission

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.ViewModel.PermissionViewModel
import com.example.mobileapptechnobit.ViewModel.ScheduleViewModel
import com.example.mobileapptechnobit.data.remote.AlternatePermissionResponseItem
import com.example.mobileapptechnobit.data.remote.PatrolScheduleResponse
import com.example.mobileapptechnobit.data.remote.PermissionResponseItem
import com.example.mobileapptechnobit.ui.theme.primary100
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailPermitScreen(modifier: Modifier = Modifier, navCtrl: NavController, token: String, id: Int, viewModel: PermissionViewModel, isRequester: Boolean) {

    val context = LocalContext.current
    val permissionItems by viewModel.permissionItems.observeAsState()
    val alternatePermissionItem by viewModel.altPermissionItems.observeAsState()
    val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val authToken = sharedPref.getString("AUTH_TOKEN", null) ?: token

    val permissionItem = permissionItems?.find { it.id == id }
    val altPermissionItem = alternatePermissionItem?.find { it.id == id }

    val scheduleViewModel: ScheduleViewModel = viewModel()
    val schedules = scheduleViewModel.scheduleList
    val empSchedule = schedules.find { it.tanggal == permissionItem?.date }
    val altSchedule = schedules.find { it.tanggal == altPermissionItem?.date }

    LaunchedEffect(Unit) {
        scheduleViewModel.fetchSchedules(token)
    }

    Scaffold (
        topBar = {
            DetailPermitTitle(navCtrl = navCtrl)
        },
        bottomBar = {
            Column(Modifier.padding(bottom = 10.dp)) {
                if(isRequester) {
                    if (permissionItems?.isNotEmpty() == true){
                        if (permissionItem != null) {
                            if (permissionItem.status.equals("pending", ignoreCase = true)){
                                if(permissionItem.alternateId != null){
                                    if(permissionItem.employeeIsConfirmed.equals("pending")){
                                        BottomButton(viewModel = viewModel, token = authToken, id = id, isRequester = isRequester, navCtrl = navCtrl)
                                    }
                                }
                                else{
                                    Column (Modifier.padding(bottom = 20.dp)) {
                                        BottomStatus(status = permissionItem.status)
                                    }
                                }
                            } else{
                                Column (Modifier.padding(bottom = 20.dp)) {
                                    BottomStatus(status = permissionItem.status)
                                }
                            }
                        }
                    }
                } else{
                    if (alternatePermissionItem?.isNotEmpty() == true){
                        if (altPermissionItem != null) {
                            if (altPermissionItem.status.equals("pending", ignoreCase = true)){
                                if(altPermissionItem.alternateId != null){
                                    if(altPermissionItem.employeeIsConfirmed.equals("pending")){
                                        BottomButton(viewModel = viewModel, token = authToken, id = id, isRequester = isRequester, navCtrl = navCtrl)
                                    }
                                }
                                else{
                                    Column (Modifier.padding(bottom = 20.dp)) {
                                        BottomStatus(status = altPermissionItem.status)
                                    }
                                }
                            } else{
                                Column (Modifier.padding(bottom = 20.dp)) {
                                    BottomStatus(status = altPermissionItem.status)
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(Modifier
            .padding(padding)
            .padding(top = 50.dp)) {
            if (isRequester){
                DetailPermitBody(item = permissionItem!!, schedules = schedules)
            } else{
                DetailPermitBody(item = altPermissionItem!!, schedules = schedules)
            }
        }
    }
}

@Composable
fun DetailPermitTitle(modifier: Modifier = Modifier, navCtrl: NavController) {
    Column (Modifier.fillMaxWidth()){
        IconButton(
            onClick = { navCtrl.navigate("permission_screen") },
            Modifier
                .padding(start = 10.dp)
                .padding(top = 30.dp)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
        Spacer(Modifier.padding(5.dp))
        Text(
            text = "Detail Perizinan", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 25.sp, textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DetailPermitBody(modifier: Modifier = Modifier, item: PermissionResponseItem, schedules: List<PatrolScheduleResponse>) {

    val schedule = schedules.find { it.tanggal == item.date }

    Column (Modifier.padding(horizontal = 30.dp)){
        if(item.alternateId != null){

            Text(text = "Pegawai Pengganti", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
            Spacer(Modifier.padding(3.dp))
            Text(text = item.alternateName, fontFamily = robotoFontFamily, fontWeight = FontWeight(400), fontSize = 15.sp)

            if(item.DetailAltSchedule.isNullOrEmpty() || item.DetailEmpSchedule.isNullOrEmpty() || item.date.isNullOrEmpty()){

                if(schedule != null && item.DetailEmpSchedule.isNullOrEmpty()){
                    Spacer(Modifier.padding(10.dp))
                    Text(text = "Jadwal Lama", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
                    Spacer(Modifier.padding(5.dp))
//                    PermissionScheduleCard(time = item.DetailEmpSchedule, date = item.date)
                    PermissionScheduleCardBySchedule(schedule = schedule)

                    Spacer(Modifier.padding(10.dp))
                    Text(text = "Jadwal Baru", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
                    Spacer(Modifier.padding(5.dp))
                    PermissionScheduleCard(time = item.DetailAltSchedule, date = item.date)

                } else if(item.DetailAltSchedule != null){
                    Spacer(Modifier.padding(10.dp))
                    Text(text = "Jadwal Baru", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
                    Spacer(Modifier.padding(5.dp))
                    PermissionScheduleCard(time = item.DetailAltSchedule, date = item.date)
                } else{
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement =  Arrangement.Center, modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Maaf, jadwal baru belum ditentukan", fontFamily = robotoFontFamily, fontWeight = FontWeight(400), fontSize = 17.sp,color = Color.Gray, textAlign = TextAlign.Center)
                    }
                }
            } else{
//                Text(text = "Pegawai Pengganti", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
//                Spacer(Modifier.padding(3.dp))
//                Text(text = item.alternateName, fontFamily = robotoFontFamily, fontWeight = FontWeight(400), fontSize = 15.sp)

                Spacer(Modifier.padding(10.dp))
                Text(text = "Jadwal Lama", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
                Spacer(Modifier.padding(5.dp))
                PermissionScheduleCard(time = item.DetailEmpSchedule, date = item.date)

                Spacer(Modifier.padding(10.dp))
                Text(text = "Jadwal Baru", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
                Spacer(Modifier.padding(5.dp))
                PermissionScheduleCard(time = item.DetailAltSchedule, date = item.date)
            }

        } else{
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement =  Arrangement.Center, modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Maaf, karyawan pengganti belum ditentukan", fontFamily = robotoFontFamily, fontWeight = FontWeight(400), fontSize = 17.sp,color = Color.Gray, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun DetailPermitBody(modifier: Modifier = Modifier, item: AlternatePermissionResponseItem, schedules: List<PatrolScheduleResponse>) {

    val schedule = schedules.find { it.tanggal == item.date }

    Column (Modifier.padding(horizontal = 30.dp)){
        if(item.alternateId != null){

            Text(text = "Pegawai Pengganti", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
            Spacer(Modifier.padding(3.dp))
            Text(text = item.requesterName, fontFamily = robotoFontFamily, fontWeight = FontWeight(400), fontSize = 15.sp)

            if(item.DetailAltSchedule.isNullOrEmpty() || item.DetailEmpSchedule.isNullOrEmpty() || item.date.isNullOrEmpty()){

                if(schedule!!.tanggal.isNotEmpty() && item.DetailEmpSchedule.isNullOrEmpty()){
                    Spacer(Modifier.padding(10.dp))
                    Text(text = "Jadwal Lama", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
                    Spacer(Modifier.padding(5.dp))
//                    PermissionScheduleCard(time = item.DetailEmpSchedule, date = item.date)
                    PermissionScheduleCardBySchedule(schedule = schedule)

                    Spacer(Modifier.padding(10.dp))
                    Text(text = "Jadwal Baru", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
                    Spacer(Modifier.padding(5.dp))
                    PermissionScheduleCard(time = item.DetailAltSchedule, date = item.date)

                } else{
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement =  Arrangement.Center, modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Maaf, jadwal baru belum ditentukan", fontFamily = robotoFontFamily, fontWeight = FontWeight(400), fontSize = 17.sp,color = Color.Gray, textAlign = TextAlign.Center)
                    }
                }

            } else{
                Text(text = "Pegawai Pengganti", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
                Spacer(Modifier.padding(3.dp))
                Text(text = item.requesterName, fontFamily = robotoFontFamily, fontWeight = FontWeight(400), fontSize = 15.sp)

                Spacer(Modifier.padding(10.dp))
                Text(text = "Jadwal Lama", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
                Spacer(Modifier.padding(5.dp))
                PermissionScheduleCard(time = item.DetailEmpSchedule, date = item.date)

                Spacer(Modifier.padding(10.dp))
                Text(text = "Jadwal Baru", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
                Spacer(Modifier.padding(5.dp))
                PermissionScheduleCard(time = item.DetailAltSchedule, date = item.date)
            }

        } else{
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement =  Arrangement.Center, modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Maaf, karyawan pengganti belum ditentukan", fontFamily = robotoFontFamily, fontWeight = FontWeight(400), fontSize = 17.sp,color = Color.Gray, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun PermissionScheduleCard(modifier: Modifier = Modifier, time: String, date: String) {

    val waktu = time.substringBefore(" (")
    val jam = time.substringAfter("(").substringBefore(")")
    val jamMulai = jam.split("-")[0]
    val jamSelesai = jam.split("-")[1]

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Shift dan Tanggal
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .background(
                            color = when (waktu.lowercase()) {
                                "pagi" -> Color(0xFFFFFF00)
                                "siang" -> Color(0xFFFFB800)
                                "malam" -> Color(0xFF7EC8E3)
                                else -> Color.Gray
                            },
                            shape = RoundedCornerShape(4.dp)
                        ).padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = waktu.replaceFirstChar { it.uppercase() }, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = date, fontSize = 16.sp, color = Color.Black)
            }
            Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

//            Spacer(modifier = Modifier.height(2.dp))
//
//            // Lokasi patrol
//            Text(
//                text = "Lokasi: ${schedule.}",
//                fontWeight = FontWeight.SemiBold,
//                fontSize = 18.sp
//            )
            Spacer(modifier = Modifier.height(8.dp))

            // Baris Jam Mulai - Selesai
            Row(Modifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(R.drawable.frame_27), contentDescription = null, modifier = Modifier.size(45.dp), tint = Color.Unspecified)
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(text = "Mulai", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 16.sp)
                    Text(text = jamMulai, fontFamily = robotoFontFamily)
                }

                Spacer(Modifier.weight(1f))

                VerticalDivider(thickness = 1.dp, color = Color.LightGray, modifier = Modifier.height(50.dp))

                Spacer(Modifier.weight(1f))

                Icon(painter = painterResource(R.drawable.frame_28), contentDescription = null, modifier = Modifier.size(45.dp), tint = Color.Unspecified)
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(text = "Selesai", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 16.sp)
                    Text(text = jamSelesai, fontFamily = robotoFontFamily)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info lokasi & rekan

//            Text(
//                "Rekan tugas: ${schedule.rekan_tugas.joinToString(", ")}",
//                fontSize = 14.sp,
//                fontFamily = robotoFontFamily
//            )
        }
    }
}

@Composable
fun PermissionScheduleCardBySchedule(modifier: Modifier = Modifier, schedule: PatrolScheduleResponse) {

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Shift dan Tanggal
            Row(verticalAlignment = Alignment.CenterVertically) {
//                Box(
//                    modifier = Modifier
//                        .background(
//                            color = when (waktu.lowercase()) {
//                                "pagi" -> Color(0xFFFFFF00)
//                                "siang" -> Color(0xFFFFB800)
//                                "malam" -> Color(0xFF7EC8E3)
//                                else -> Color.Gray
//                            },
//                            shape = RoundedCornerShape(4.dp)
//                        ).padding(horizontal = 12.dp, vertical = 8.dp)
//                ) {
//                    Text(
//                        text = waktu.replaceFirstChar { it.uppercase() }, fontWeight = FontWeight.Bold, fontSize = 16.sp)
//                }

//                Spacer(modifier = Modifier.width(8.dp))

                Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = schedule.tanggal, fontSize = 16.sp, color = Color.Black)
            }
            Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

//            Spacer(modifier = Modifier.height(2.dp))
//
//            // Lokasi patrol
//            Text(
//                text = "Lokasi: ${schedule.}",
//                fontWeight = FontWeight.SemiBold,
//                fontSize = 18.sp
//            )
            Spacer(modifier = Modifier.height(8.dp))

            // Baris Jam Mulai - Selesai
            Row(Modifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(R.drawable.frame_27), contentDescription = null, modifier = Modifier.size(45.dp), tint = Color.Unspecified)
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(text = "Mulai", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 16.sp)
                    Text(text = schedule.jam_mulai!!, fontFamily = robotoFontFamily)
                }

                Spacer(Modifier.weight(1f))

                VerticalDivider(thickness = 1.dp, color = Color.LightGray, modifier = Modifier.height(50.dp))

                Spacer(Modifier.weight(1f))

                Icon(painter = painterResource(R.drawable.frame_28), contentDescription = null, modifier = Modifier.size(45.dp), tint = Color.Unspecified)
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(text = "Selesai", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 16.sp)
                    Text(text = schedule.jam_selesai!!, fontFamily = robotoFontFamily)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info lokasi & rekan

//            Text(
//                "Rekan tugas: ${schedule.rekan_tugas.joinToString(", ")}",
//                fontSize = 14.sp,
//                fontFamily = robotoFontFamily
//            )
        }
    }
}



@Composable
fun BottomButton(modifier: Modifier = Modifier, viewModel: PermissionViewModel, token: String, id: Int, isRequester: Boolean, navCtrl: NavController) {

    val isRequester by remember { mutableStateOf(isRequester) }
    val isEmpApproved by viewModel.isEmpApproved.observeAsState()
    val isAltApproved by viewModel.isAltApproved.observeAsState()

    Row (
        Modifier
        .fillMaxWidth()
        .padding(bottom = 20.dp)
        .padding(horizontal = 30.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){

        OutlinedButton(
            onClick = {viewModel.sendApproval(token, id, "rejected")},
            shape = RoundedCornerShape(5.dp),
            border = ButtonDefaults.outlinedButtonBorder,
            modifier = Modifier
                .width(160.dp)
                .height(50.dp)
        )  {
            Text(text = "Menolak", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp, color = primary100)
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                if(isRequester){
                    viewModel.sendApproval(token, id, "approved")
                } else{
                    viewModel.sendApproval(token, id, "approved")
                }
            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(primary100),
            modifier = Modifier
                .width(160.dp)
                .height(50.dp)
        ) {
            Text(text = "Setuju", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 17.sp)
        }

        LaunchedEffect(isEmpApproved) {
            if(isEmpApproved == true){
                navCtrl.navigate("success_screen/Anda telah menyetujui perizinan!/permission_screen")
                viewModel.resetStatus()
            }
        }
        LaunchedEffect(isAltApproved) {
            if(isAltApproved == true){
                navCtrl.navigate("success_screen/Anda telah menyetujui perizinan!/permission_screen")
                viewModel.resetStatus()
            }
        }
    }
}

@Composable
fun BottomStatus(modifier: Modifier = Modifier, status: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .height(50.dp)
            .border(
                width = 2.dp,
                color = if (status.equals("approved", ignoreCase = true)) Color(0xff3F845F)
                else if (status.equals("pending", ignoreCase = true)) Color(0xffE4C65B)
                else Color(0xffE25C5C),
                shape = RoundedCornerShape(5.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(text = "Status Perizinan: $status", fontFamily = robotoFontFamily, fontWeight = FontWeight(500), fontSize = 16.sp,
            color = if(status.equals("approved", ignoreCase = true)) Color(0xff3F845F)
            else if(status.equals("pending", ignoreCase = true)) Color(0xffE4C65B)
            else Color(0xffE25C5C)
        )
    }
}

//@Preview
//@Composable
//private fun DetailPermitPrev() {
//    DetailPermitScreen(navCtrl = rememberNavController(), token = "", item = )
//}