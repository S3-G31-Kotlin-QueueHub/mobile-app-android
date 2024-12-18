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
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Button
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
import com.queue_hub.isis3510_s3_g31.ui.navigation.Recommended
import com.queue_hub.isis3510_s3_g31.utils.location_services.DistanceCalculator
import com.queue_hub.isis3510_s3_g31.utils.location_services.LocationData

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier,
    homeViewModel: HomeViewModel
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
fun ConnectivityBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorScheme.errorContainer)
    ) {
        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ){
            Icon(
                imageVector = Icons.Rounded.Info,
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp),
                tint = colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "No internet connection",
                color = colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}

@Composable
fun Home (
    modifier: Modifier,
    state: HomeViewState,
    navController: NavController,
    homeViewModel: HomeViewModel
){
    val isConnected by homeViewModel.isConnected.collectAsState(initial = true)
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HeaderImage(modifier = Modifier)
        Spacer(modifier = Modifier.padding(8.dp))

        if (!isConnected) {
            ConnectivityBanner()
        }

        WelcomeCard(modifier = Modifier)
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
fun WelcomeCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.onPrimary)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.welcome_to_queuehub),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.join_virtual_queues),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
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
    val isConnected by homeViewModel.isConnected.collectAsState(initial = true)
    when (state) {
        is HomeViewState.Loading -> {
            CircularProgressIndicator()
        }
        is HomeViewState.Success -> {
            if (state.commonPlaces.isEmpty()) {

                if(!isConnected){
                    Text(
                        text = stringResource(R.string.check_your_network_connection),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }else{
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
                        },locationData)
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
fun CommonPlaceCard(
    place: CommonPlace,
    onClick: () -> Unit,
    locationData: LocationData?
) {

    val distance = locationData?.let { location ->
        DistanceCalculator.getInstance().calculateHaversine(
            location.latitude,
            location.longitude,
            place.localization.latitude,
            place.localization.longitude
        )
    }

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
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
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
                            imageVector = Icons.Rounded.Info,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        if(distance != null){
                            Text(
                                text = "${String.format("%.1f", distance)} km away",
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.onSurfaceVariant
                            )
                        }else{
                            Text(
                                text = "Distance unknown",
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
                    }


                }
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