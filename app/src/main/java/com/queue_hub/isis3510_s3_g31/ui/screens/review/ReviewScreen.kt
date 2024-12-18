package com.queue_hub.isis3510_s3_g31.ui.screens.review

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.queue_hub.isis3510_s3_g31.R
import com.queue_hub.isis3510_s3_g31.data.places.model.Place
import com.queue_hub.isis3510_s3_g31.data.reviews.model.Review

@Composable
fun ReviewScreen(
    navController: NavController,
    reviewViewModel: ReviewViewModel
) {
    val reviewState by reviewViewModel.reviewState.collectAsState()
    val placeState by reviewViewModel.placeState.collectAsState()
    Box(
        Modifier.fillMaxSize()
            .padding(20.dp)
    ){
        Review(
            modifier = Modifier.fillMaxSize(),
            viewModel = reviewViewModel,
            navController = navController,
            reviewState = reviewState,
            placeState = placeState
        )
    }

}

@Composable
fun Review(
    modifier: Modifier,
    viewModel: ReviewViewModel,
    navController: NavController,
    reviewState: ReviewViewState<List<Review>>,
    placeState: ReviewViewState<Place>
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HeaderImage(modifier = Modifier)
        Spacer(modifier = Modifier.padding(8.dp))
        PlaceCard(placeState = placeState)
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "Reviews",
            style = MaterialTheme.typography.headlineLarge,
        )
        Spacer(modifier = Modifier.padding(8.dp))
        ReviewList(reviewState = reviewState)
    }
}

@Composable
fun HeaderImage(modifier: Modifier) {
    val logoImage = painterResource(R.drawable.queuehub_logo)
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


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlaceCard(
    modifier: Modifier = Modifier,
    placeState: ReviewViewState<Place>
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.onPrimary)
    ) {
        when (placeState) {
            is ReviewViewState.Success -> {
                val place = placeState.data
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = place.name,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = place.address,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.onSurfaceVariant
                        )
                    }

                    GlideImage(
                        model = place.image,
                        contentDescription = "Place Image",
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(end = 16.dp),
                        contentScale = ContentScale.Crop
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

            is ReviewViewState.Loading -> {
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = colorScheme.primary
                )
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }

            is ReviewViewState.Error -> {
                Text(
                    text = "Error",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = colorScheme.primary
                )
                Text(
                    text = "Error",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }
        }

    }
}

@Composable
fun ReviewList(reviewState: ReviewViewState<List<Review>>) {
    when (reviewState) {
        is ReviewViewState.Success -> {
            val reviews = reviewState.data
            if (reviews.isEmpty()) {
                Text(
                    text = "No reviews yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(reviews) { index, review ->
                        ReviewCard(review = review)
                    }
                }

            }
        }

        is ReviewViewState.Loading -> {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )
        }

        is ReviewViewState.Error -> {
            Text(
                text = "Error",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}




@Composable
fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.onPrimary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = review.userName,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = colorScheme.primary
                )
                Text(
                    text = review.comment,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }
        }
    }
}



