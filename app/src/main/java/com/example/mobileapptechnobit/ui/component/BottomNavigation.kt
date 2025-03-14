package com.example.mobileapptechnobit.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.ui.theme.robotoFontFamily

@Composable
fun BottomNavigationBar(index: Int, navCtrl: NavController){

    var selectedIndex by remember { mutableStateOf(index) }

    Column {
        HorizontalDivider(
            thickness = 2.dp,
            color = Color.LightGray
        )
        NavigationBar(
            containerColor = Color.White
        ) {
            NavigationBarItem(
                selected = selectedIndex == 0,
                onClick = {
                    selectedIndex = 0
                    navCtrl.navigate("home_screen")
                },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.material_symbols_home_outline_rounded__1_),
                        contentDescription = "home icon",
                        tint = if(selectedIndex == 0) colorResource(R.color.primary100) else Color(0xFF626262),
                        modifier = Modifier.size(30.dp)
                    )
                },
                label = {
                    Text(
                        text = "Beranda",
                        color = if (selectedIndex == 0) colorResource(R.color.primary100) else Color(0xFF626262),
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight(400),
                        fontSize = 15.sp,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = selectedIndex == 1,
                onClick = {
                    selectedIndex = 1
                    navCtrl.navigate("history_screen")
                },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.material_symbols_history_rounded),
                        contentDescription = "history icon",
                        tint = if(selectedIndex == 1) colorResource(R.color.primary100) else Color(0xFF626262),
                        modifier = Modifier.size(30.dp)
                    )
                },
                label = {
                    Text(
                        text = "Histori",
                        color = if (selectedIndex == 1) colorResource(R.color.primary100) else Color(0xFF626262),
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight(400),
                        fontSize = 15.sp,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = selectedIndex == 2,
                onClick = {
                    selectedIndex = 2
                    navCtrl.navigate("profile_screen")
                },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.iconamoon_profile),
                        contentDescription = "profile icon",
                        tint = if(selectedIndex == 2) colorResource(R.color.primary100) else Color(0xFF626262),
                        modifier = Modifier.size(30.dp)
                    )
                },
                label = {
                    Text(
                        text = "Profil",
                        color = if (selectedIndex == 2) colorResource(R.color.primary100) else Color(0xFF626262),
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight(400),
                        fontSize = 15.sp,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview
@Composable
private fun BottomNavPrev() {
    BottomNavigationBar(index = 1, navCtrl = rememberNavController())
}