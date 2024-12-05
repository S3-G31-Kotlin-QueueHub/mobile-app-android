package com.queue_hub.isis3510_s3_g31.ui.screens.detail
import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.text.font.FontWeight
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.ui.navigation.Home
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.queue_hub.isis3510_s3_g31.R
import com.queue_hub.isis3510_s3_g31.ui.navigation.Review
import com.queue_hub.isis3510_s3_g31.ui.navigation.Wait
import com.queue_hub.isis3510_s3_g31.ui.screens.review.ReviewViewModel
import com.queue_hub.isis3510_s3_g31.ui.theme.DarkGreen
import com.queue_hub.isis3510_s3_g31.ui.theme.LightGreen
import com.queue_hub.isis3510_s3_g31.ui.theme.Pink40
import kotlinx.coroutines.delay


@Composable
fun DetailScreen(navController: NavController, modifier: Modifier, detailViewModel: DetailViewModel, reviewViewModel: ReviewViewModel) {

    Box (
        Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ){
        Box(
            Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {


            Detail(
                modifier = Modifier,
                navController = navController,
                detailViewModel = detailViewModel,
                reviewViewModel = reviewViewModel
            )
        }
    }

}

@Composable
fun Detail (modifier: Modifier, navController: NavController, detailViewModel: DetailViewModel, reviewViewModel: ReviewViewModel) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HeaderImage(modifier = Modifier, navController =navController)

        Spacer(modifier = Modifier.padding(8.dp))

        val isLoading = detailViewModel.isLoading.value
        var showContent by remember { mutableStateOf(false) }

        LaunchedEffect(isLoading) {
            if (isLoading) {
                showContent = false
            } else {
                delay(1000)
                showContent = true
            }
        }
        if (showContent) {
        Spacer(modifier = Modifier.padding(8.dp))
        HomeOptions(modifier = Modifier, detailViewModel, reviewViewModel)
        Spacer(modifier = Modifier.padding(8.dp))
        Buttons(modifier, navController, detailViewModel)
        Spacer(modifier = Modifier.padding(8.dp))
        } else {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                CircularProgressIndicator()
            }
        }
    }
    }


@Composable
fun Buttons(
    modifier: Modifier = Modifier,
    navController: NavController,
    detailViewModel: DetailViewModel
) {
    val context = LocalContext.current
    val queuedState = detailViewModel.activeTurn.value
    val isConnected by detailViewModel.isConnected.collectAsState(initial = false)

    LaunchedEffect(isConnected) {
        if (!isConnected) {

            Toast.makeText(context, "No hay conexión a internet", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val context = LocalContext.current
        Button(
            onClick = {
                if (!isConnected){
                    Toast.makeText(context, "There is not internet connection.", Toast.LENGTH_SHORT).show()

                }
                else if (!queuedState) {
                    detailViewModel.addTurn()
                    navController.navigate(Wait) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }

                }
                else{
                    navController.navigate(Wait) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }

            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isConnected && !queuedState ) DarkGreen else if (!isConnected)  Pink40 else Color.Gray,
                contentColor = Color.White
            )
        ) {
            Text(text = if (!isConnected) "No internet connection" else if (queuedState) "See my actual turn" else "Give me a turn.")
        }

        Button(
            onClick = {
                navController.navigate(Review)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightGreen,
                contentColor = Color.White
            )
        ) {
            Text(text = "See some reviews")
        }
    }
}

@Composable
fun HeaderImage(modifier: Modifier, navController: NavController) {
    val logoImage = painterResource(R.drawable.queuehub_logo)
    val activity = LocalContext.current as? Activity
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Row(
        modifier = modifier
            .padding(top = 30.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onBackPressedDispatcher?.onBackPressed() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)
            )
        }

        Image(
            painter = logoImage,
            contentDescription = "Banner image",
            modifier = Modifier
                .size(width = 165.dp, height = 48.dp)
        )
    }
}



@Composable
fun HomeOptions(modifier: Modifier,detailViewModel: DetailViewModel, reviewViewModel: ReviewViewModel) {
    Column(

        modifier = modifier.fillMaxWidth()
    ) {


        Row (

            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)

        ){

            ClickableVerticalOption(


                modifier = Modifier.weight(1f),
                detailViewModel,
                reviewViewModel

            )


        }





    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ClickableVerticalOption(

    modifier: Modifier,
    detailViewModel: DetailViewModel,
    reviewViewModel: ReviewViewModel
) {
    val onQueue = detailViewModel.onQueue.value
    val place = detailViewModel.place.value
    val queuedState = detailViewModel.activeTurn.value
    reviewViewModel.onPlaceIdChange(place.id)

    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 30.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.onPrimary)

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier

                .fillMaxWidth()
                .padding(top = 20.dp)

        ) {
            Text(
                text = place.name,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Text(
                text = place.address,
                style = MaterialTheme.typography.titleLarge.copy(),
                textAlign = TextAlign.Center
            )
            GlideImage(
                model =place.image,
                contentDescription = "Queue Image",
                modifier = Modifier
                    .size(300.dp)
                    .fillMaxWidth(),
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
        Column(

            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .size(300.dp)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),



        ) {

            Text(
                text = "Average Waiting Time: ${place.averageWaitingTime} min",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Best arrival time: ${place.bestAverageFrame}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "People on Queue: ${onQueue}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )


        }
    }

}

