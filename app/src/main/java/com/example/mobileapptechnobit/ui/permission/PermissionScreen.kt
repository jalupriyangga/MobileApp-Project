package com.example.mobileapptechnobit.ui.permission

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.ViewModel.PermissionViewModel
import com.example.mobileapptechnobit.data.remote.AlternatePermissionResponseItem
import com.example.mobileapptechnobit.data.remote.PermissionResponseItem
import com.example.mobileapptechnobit.ui.theme.primary100
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedBoxWithConstraintsScope")
@Composable
fun PermissionScreen(modifier: Modifier = Modifier, navCtrl: NavController, token: String, viewModel: PermissionViewModel) {

    var selectedIndex by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
//    val repository = remember { PermissionRepository() }
//    val viewModel: PermissionViewModel = viewModel(factory = PermissionViewModelFactory(repository = repository, context = context))
    val permissionItems by viewModel.permissionItems.observeAsState()
    val altPermissionItems by viewModel.altPermissionItems.observeAsState()
    val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val authToken = sharedPref.getString("AUTH_TOKEN", null) ?: token

    LaunchedEffect(Unit) {
        viewModel.fetchPermission(authToken)
        viewModel.fetchAlternatePermission(authToken)
    }

    Scaffold (
        topBar = {
            PermitTopBar (
                modifier = modifier,
                navCtrl = navCtrl,
                index = { index ->
                    selectedIndex = index
                }
            )
        },
        bottomBar = {
            Column (
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp).padding(end = 30.dp)
                    .background(color = Color.Transparent)
            ) {
                FloatingButton(navCtrl = navCtrl)
            }
        }
    ){ padding ->
        if(selectedIndex == 0){
            LazyColumn (Modifier.padding(padding).fillMaxSize()) {
                permissionItems?.let {
                    items(it){ item ->
                        if(!isBeforeToday(item.date)){
                            PermissionCard(
                                permissionItem = item,
                                viewModel = viewModel,
                                navCtrl = navCtrl,
                                token = token
                            )
                        }
                    }
                }
                altPermissionItems?.let {
                    items(it){ item ->
                        if(!isBeforeToday(item.date)){
                            PermissionCard(
                                permissionItem = item,
                                viewModel = viewModel,
                                navCtrl = navCtrl,
                                token = token
                            )
                        }
                    }
                }
            }
        } else{
            LazyColumn (Modifier.padding(padding).fillMaxSize()) {
                permissionItems?.let {
                    items(it){ item ->
                        if(isBeforeToday(item.date)){
                            PermissionCard(
                                permissionItem = item,
                                viewModel = viewModel,
                                navCtrl = navCtrl,
                                token = token
                            )
                        }
                    }
                }
                altPermissionItems?.let {
                    items(it){ item ->
                        if(isBeforeToday(item.date)){
                            PermissionCard(
                                permissionItem = item,
                                viewModel = viewModel,
                                navCtrl = navCtrl,
                                token = token
                            )
                        }
                    }
                }
            }
        }
    }
}

fun isAfterToday(date: String): Boolean{
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Sesuaikan format input kamu
    val inputDate = LocalDate.parse(date, formatter)
    val today = LocalDate.now()

    return inputDate.isAfter(today)
}

fun isBeforeToday(date: String): Boolean{
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Sesuaikan format input kamu
    val inputDate = LocalDate.parse(date, formatter)
    val today = LocalDate.now()

    return inputDate.isBefore(today)
}

@Composable
fun PermitTopBar(modifier: Modifier = Modifier, index: (Int) -> Unit, navCtrl: NavController) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    Box{
        IconButton(
            onClick = { navCtrl.navigate("home_screen") },
            Modifier.padding(start = 10.dp).padding(top = 20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back button",
                tint = Color.Black,
                modifier = Modifier.size(28.dp)
            )
        }
        Column (
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Perizinan",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
                fontSize = 25.sp,
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight(500)
            )
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(0.5f)
                        .clickable{
                            selectedIndex = 0
                            index(selectedIndex)
                        }
                ) {
                    Text(
                        text = "Aktif",
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight(500),
                        modifier = Modifier.padding(bottom = 10.dp),
                        color = if (selectedIndex == 0) Color(0xff2752E7) else Color.Unspecified
                    )
                    HorizontalDivider(
                        color = if (selectedIndex == 0) Color(0xff2752E7) else Color.LightGray,
                        thickness = 2.dp
                    )
                }
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(1f)
                        .clickable{
                            selectedIndex = 1
                            index(selectedIndex)
                        }
                ){
                    Text(
                        text = "Riwayat",
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight(500),
                        modifier = Modifier.padding(bottom = 10.dp),
                        color = if (selectedIndex == 1) Color(0xff2752E7) else Color.Unspecified

                    )
                    HorizontalDivider(
                        color = if (selectedIndex == 1) Color(0xff2752E7) else Color.LightGray,
                        thickness = 2.dp
                    )
                }
            }
        }
    }
}

@Composable
fun FloatingButton(modifier: Modifier = Modifier, navCtrl: NavController) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.background(
            color = primary100,
            shape = CircleShape
        )
            .size(50.dp)
    ){
        IconButton(onClick = { navCtrl.navigate("permission_form_screen")}) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
        }
    }
}

fun dateFormatter(date: String): String {
    return try {
        val formatInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatOutput = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        val dayFormat = SimpleDateFormat("EEEE", Locale("id", "ID"))

        val parsedDate = formatInput.parse(date)
        if (parsedDate != null) {
            val formattedDay = dayFormat.format(parsedDate).replaceFirstChar { it.uppercase() }
            val formattedDate = formatOutput.format(parsedDate)
            "$formattedDay, $formattedDate"
        } else {
            "Tanggal tidak valid"
        }
    } catch (e: Exception) {
        "Format salah"
    }
}

fun createdDateFormatter(date: String): String {
    return try {
        val formatInput = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formatOutput = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        val parsedDate = formatInput.parse(date)

        if (parsedDate != null) {
            val formattedDate = formatOutput.format(parsedDate)
            formattedDate
        } else {
            "Tanggal tidak valid"
        }
    } catch (e: Exception) {
        "Format salah"
    }
}

@Composable
fun PermissionCard(modifier: Modifier = Modifier, permissionItem: PermissionResponseItem, viewModel: PermissionViewModel, navCtrl: NavController, token: String) {

    val status by remember { mutableStateOf(permissionItem.status) }
    var isDeleted by remember { mutableStateOf(false) }

    val isRequester = true
    val context = LocalContext.current

 Card(
     Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(top = 20.dp).clickable { navCtrl.navigate("detail_permission_screen/${permissionItem.id}/$isRequester") },
     elevation = CardDefaults.cardElevation(5.dp),
     shape = RoundedCornerShape(15.dp),
     colors = CardDefaults.cardColors(Color.White)
 ) {
     Column (Modifier.padding(10.dp)){
         Row(verticalAlignment = Alignment.CenterVertically) {
             Icon(painter = painterResource(R.drawable.hugeicons_date_time), contentDescription = null, Modifier.size(28.dp), tint = Color.Gray)
             Spacer(Modifier.padding(horizontal = 5.dp))
             Text(text = dateFormatter(permissionItem.date), fontSize = 17.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight(500))
             Spacer(Modifier.weight(1f))
             IconButton(
                 onClick = {
                    permissionItem.id?.let { viewModel.deletePermission(token, it) }
                     isDeleted = true
                     Toast.makeText(context, "Perizinan berhasil dihapus!", Toast.LENGTH_SHORT).show()
                 }) {
                 Icon(imageVector = Icons.Default.Delete, contentDescription = null, Modifier.size(25.dp), tint = Color.Gray)
             }
         }
         LaunchedEffect(isDeleted) {
             viewModel.fetchPermission(token)
             isDeleted = false
         }
         HorizontalDivider(Modifier.padding(vertical = 10.dp))
         Row (verticalAlignment = Alignment.CenterVertically){
             Column {
                 Text(text = "Jenis Izin: ${permissionItem.permission}", fontSize = 13.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight(400))
                 Spacer(Modifier.padding(vertical = 3.dp))
                 val createdDate = createdDateFormatter(permissionItem.created_at)
                 Text(text = "Tanggal pengajuan: $createdDate", fontSize = 13.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight(400))
             }
             Column (horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                 Text(
                     text = status, fontSize = 15.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight(400), textAlign = TextAlign.Center,
                     modifier = Modifier.background (
                         color =  if (status.equals("pending", ignoreCase = true)) { if(permissionItem.alternateId !=  null) Color(0xff2685CA) else Color(0xffE4C65B) }
                         else if (status.equals("approved", ignoreCase = true)) Color(0xff3F845F)
                         else if (status.equals("persetujuan", ignoreCase = true)) Color(0xff2685CA)
                         else Color(0xffE25C5C),
                         shape = RoundedCornerShape(5.dp)
                     )
                         .padding(vertical = 5.dp).width(100.dp)
                 )
             }
         }
     }
 }
}

@Composable
fun PermissionCard(modifier: Modifier = Modifier, permissionItem: AlternatePermissionResponseItem, viewModel: PermissionViewModel, navCtrl: NavController, token: String) {

    val status by remember { mutableStateOf(permissionItem.status) }
    var isDeleted by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val isRequester = false

    Card(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(top = 20.dp).clickable { navCtrl.navigate("detail_permission_screen/${permissionItem.id}/$isRequester") },
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column (Modifier.padding(10.dp)){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(R.drawable.hugeicons_date_time), contentDescription = null, Modifier.size(28.dp), tint = Color.Gray)
                Spacer(Modifier.padding(horizontal = 5.dp))
                Text(text = dateFormatter(permissionItem.date), fontSize = 17.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight(500))
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = {
                        permissionItem.id?.let { viewModel.deletePermission(token, it) }
                        isDeleted = true
                        Toast.makeText(context, "Perizinan berhasil dihapus!", Toast.LENGTH_SHORT).show()
                    }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, Modifier.size(25.dp), tint = Color.Gray)
                }
            }
            LaunchedEffect(isDeleted) {
                viewModel.fetchPermission(token)
                isDeleted = false
            }
            HorizontalDivider(Modifier.padding(vertical = 10.dp))
            Row (verticalAlignment = Alignment.CenterVertically){
                Column {
                    Text(text = "Jenis Izin : ${permissionItem.permission}", fontSize = 13.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight(400))
                    Text(text = "Pengaju    : ${permissionItem.requesterName}", fontSize = 13.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight(400))

                    Spacer(Modifier.padding(vertical = 3.dp))
//                    val createdDate = createdDateFormatter(permissionItem.created_at)
//                    Text(text = "Tanggal pengajuan: $createdDate", fontSize = 13.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight(400))
                }
                Spacer(Modifier.weight(1f))
                Column (horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().padding(end = 15.dp)) {
                    Text(
                        text = status, fontSize = 15.sp, fontFamily = robotoFontFamily, fontWeight = FontWeight(400), textAlign = TextAlign.Center,
                        modifier = Modifier.background (
                            color =  if (status.equals("pending", ignoreCase = true)) { if(permissionItem.alternateId !=  null) Color(0xff2685CA) else Color(0xffE4C65B) }
                            else if (status.equals("approved", ignoreCase = true)) Color(0xff3F845F)
                            else if (status.equals("persetujuan", ignoreCase = true)) Color(0xff2685CA)
                            else Color(0xffE25C5C),
                            shape = RoundedCornerShape(5.dp)
                        )
                            .padding(vertical = 5.dp).width(100.dp)
                    )
                }
            }
        }
    }
}
