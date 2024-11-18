package com.queue_hub.isis3510_s3_g31.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi

import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.queue_hub.isis3510_s3_g31.R
import com.queue_hub.isis3510_s3_g31.data.places.mapper.toPlace
import com.queue_hub.isis3510_s3_g31.data.places.model.CommonPlace
import com.queue_hub.isis3510_s3_g31.ui.navigation.Detail
import com.queue_hub.isis3510_s3_g31.utils.location_services.LocationData

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier,
    homeViewModel: HomeViewModel,


) {
    val state by homeViewModel.uiState.collectAsState()

    Box(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Home(modifier = Modifier, state = state, navController = navController, homeViewModel = homeViewModel)
    }
}

@Composable
fun Home (
    modifier: Modifier,
    state: HomeViewState,
    navController: NavController,
    homeViewModel: HomeViewModel
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HeaderImage(modifier = Modifier)
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = stringResource(id = R.string.home_header),
            style = MaterialTheme.typography.headlineLarge,
        )
        Spacer(modifier = Modifier.padding(8.dp))
        HomeOptions(modifier = Modifier)
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = stringResource(id = R.string.home_common_places),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.padding(8.dp))
        CommonPlacesList(modifier = Modifier, state = state, navController = navController, homeViewModel = homeViewModel)
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
fun HomeOptions(modifier: Modifier) {
    Column(

        modifier = modifier.fillMaxWidth()
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            ClickableVerticalOption(
                image = painterResource(id = R.drawable.comida__1_),
                text = stringResource(id = R.string.home_restaurants),
                onClick = {  },
                modifier = Modifier.weight(1f)

            )
            ClickableVerticalOption(
                image = painterResource(id = R.drawable.comercio),
                text = stringResource(id = R.string.home_stores),
                onClick = {  },
                modifier = Modifier.weight(1f)

            )
        }

        ClickableHorizontalOption(
            image = painterResource(id = R.drawable.caja_de_herramientas),
            text = stringResource(id = R.string.home_other_places),
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        )

    }

}

@Composable
fun ClickableVerticalOption(
    image: Painter,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.onPrimary)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()

        ) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
               )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = text)
        }
    }
}

@Composable
fun ClickableHorizontalOption(
    image: Painter,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.onPrimary)

        ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier.size(80.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text)
        }
    }
}
@Composable
fun CommonPlacesList(
    modifier: Modifier,
    state: HomeViewState,
    navController: NavController,
    homeViewModel: HomeViewModel
) {

    val locationData by homeViewModel.locationData.collectAsState(initial = null)

    when (state) {
        is HomeViewState.Loading -> {
            CircularProgressIndicator()
        }
        is HomeViewState.Success -> {
            if (state.commonPlaces.isEmpty()) {
                if (locationData != null) {
                    Text(
                        text = "Latitude ${locationData!!.latitude}, Longitude ${locationData!!.longitude}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = stringResource(R.string.home_no_common_places),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                LazyColumn(
                    modifier = modifier,
                    contentPadding = PaddingValues(bottom = 60.dp)
                ) {
                    itemsIndexed(state.commonPlaces) { _, commonPlace ->
                        CommonPlaceCard(place = commonPlace, onClick = {
                            homeViewModel.setPlace(commonPlace.toPlace())
                            navController.navigate(Detail)
                        })
                    }
                }
            }
        }
        is HomeViewState.Error -> {
            Text(
                text = state.message,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CommonPlaceCard(place: CommonPlace, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
            .height(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.onPrimary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = colorScheme.onSurface
                )

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${place.address}, ${place.city}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Card(
                modifier = Modifier.size(120.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                GlideImage(
                    model = place.image,
                    contentDescription = "Queue Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillHeight,
                ) { requestBuilder ->
                    requestBuilder
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.comida__1_)
                        .error(R.drawable.comida__1_)
                        .transition(DrawableTransitionOptions.withCrossFade())
                }
            }
        }
    }
}