package com.queue_hub.isis3510_s3_g31.ui.screens.recommended

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.queue_hub.isis3510_s3_g31.R
import com.queue_hub.isis3510_s3_g31.data.places.model.Place


@Composable
fun RecommendedScreen(
    navController: NavController,
    recommendedViewModel: RecommendedViewModel
){
    val state = recommendedViewModel.state

    if (state.isLoading) {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CircularProgressIndicator()
        }

    } else {

        Box(
            Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Recommended(modifier = Modifier, state = state)
        }


    }
}

@Composable
fun Recommended (modifier: Modifier, state: RecommendedViewState){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HeaderImage(modifier = Modifier)
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = stringResource(R.string.recomendaciones),
            style = MaterialTheme.typography.headlineLarge,
        )
        Spacer(modifier = Modifier.padding(8.dp))
        FilterChip(selected = true,
            onClick = { /*TODO*/ },
            label = {
                Text(
                    text = "Menos tiempo de espera en la última hora")
            },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = colorScheme.secondary
            )
        )
        Spacer(modifier = Modifier.padding(8.dp))
        RecommendedPlacesList(modifier = Modifier, places = state.places)
    }
}

@Composable
fun RecommendedPlacesList(modifier: Modifier, places: List<Place>){
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(places) { place ->
            PlaceCard(place = place, onClick = { /* Manejar clic */ })

        }
    }
}

@Composable
fun HeaderImage(modifier: Modifier) {
    val logoImage = painterResource(R.drawable.queuehub_logo)
    Row (
        modifier = modifier
            .padding(top = 30.dp)
            .fillMaxWidth()
    ){
        Image(
            painter = logoImage,
            contentDescription = "Banner image",
            modifier = Modifier
                .size(width = 165.dp, height = 48.dp)
        )
    }
}

@Composable
fun PlaceCard(place: Place, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.onPrimary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {

            Column {
                Text(
                    text = place.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "A 40 km de distancia",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Image(
                painter = painterResource(id = R.drawable.comercio),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}
