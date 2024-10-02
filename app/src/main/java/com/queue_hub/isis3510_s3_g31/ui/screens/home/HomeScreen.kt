package com.queue_hub.isis3510_s3_g31.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.queue_hub.isis3510_s3_g31.R

@Composable
fun HomeScreen(navController: NavController) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(20.dp) ){
        Home()
    }
}

@Composable
fun Home (){
    HeaderImage()
}

@Composable
fun HeaderImage() {
    val logoImage = painterResource(R.drawable.queuehub_logo)
    Image(
        painter = logoImage,
        contentDescription = "Banner image",
    )
}