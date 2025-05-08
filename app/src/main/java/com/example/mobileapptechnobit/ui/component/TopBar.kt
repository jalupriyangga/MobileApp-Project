package com.example.mobileapptechnobit.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobileapptechnobit.R

@Composable
fun TopAppBar(modifier: Modifier = Modifier) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenRatio = (screenHeight / screenWidth)
    Image(
        painter = painterResource(R.drawable.background__1_),
        contentDescription = null,
        modifier = Modifier
//            .fillMaxSize().offset(y = -290.dp)
            .fillMaxWidth()
            .height(screenHeight / screenRatio * 0.713f),
        contentScale = ContentScale.Crop
    )
}

@Preview(showBackground = true)
@Composable
private fun TopBarPrev() {
    TopAppBar()
}