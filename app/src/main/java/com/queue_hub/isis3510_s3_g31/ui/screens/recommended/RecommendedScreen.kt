package com.queue_hub.isis3510_s3_g31.ui.screens.recommended

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun RecommendedScreen(
    navController: NavController,
    recommendedViewModel: RecommendedViewModel
){
    val state = recommendedViewModel.state

    if (state.isLoading) {
        CircularProgressIndicator() // Asegúrate de importarlo
    } else {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(state.places) { place ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = place.nombre)
                    Text(text = place.direccion)
                    HorizontalDivider()
                }
            }
        }
    }
}