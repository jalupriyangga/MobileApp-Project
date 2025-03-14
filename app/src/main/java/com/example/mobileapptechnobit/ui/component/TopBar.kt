package com.example.mobileapptechnobit.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobileapptechnobit.R

@Composable
fun TopAppBar(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.background__1_),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize().offset(y = -290.dp)
    )
}

@Preview
@Composable
private fun TopBarPrev() {
    TopAppBar()
}