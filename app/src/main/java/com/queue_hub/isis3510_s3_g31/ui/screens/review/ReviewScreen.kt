package com.queue_hub.isis3510_s3_g31.ui.screens.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.queue_hub.isis3510_s3_g31.R
import com.queue_hub.isis3510_s3_g31.ui.screens.userQueues.HeaderImage
import com.queue_hub.isis3510_s3_g31.ui.screens.userQueues.QueueList
import com.queue_hub.isis3510_s3_g31.ui.screens.userQueues.TurnCard

@Composable
fun ReviewScreen(
    navController: NavController,
    reviewViewModel: ReviewViewModel
) {

    Box(
        Modifier.fillMaxSize()
            .padding(20.dp)
    )

}

@Composable
fun Review(
    modifier: Modifier,
    viewModel: ReviewViewModel,
    navController: NavController
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HeaderImage(modifier = Modifier)
        Spacer(modifier = Modifier.padding(8.dp))
        TurnCard(modifier = Modifier, turnState = turnState, queueState = queueState, userQueuesViewModel = userQueuesViewModel)
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "Previous Queues",
            style = MaterialTheme.typography.headlineLarge,
        )
        Spacer(modifier = Modifier.padding(8.dp))
        QueueList(modifier = Modifier, queuesState = queuesState, navController = navController)
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


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlaceCard(){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .weight(1.2F, true)
                .padding(16.dp),
        ) {

            Text(
                text = stringResource(R.string.your_turn),
                style = MaterialTheme.typography.labelLarge,
                color = colorScheme.onSurfaceVariant
            )
            Text(
                text = "#${turn.turnNumber}",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = colorScheme.primary
            )
            Text(
                text = "Current queue turn: #${queue.currentTurnNumber}",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )
            Button(
                onClick = {
                    userQueuesViewModel.showCancelDialog()
                },
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.error,
                    contentColor = colorScheme.onError
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null,
                        modifier = Modifier.weight(0.5F)
                    )
                    Text(
                        modifier = Modifier.weight(0.5F),
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
        Card(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            GlideImage(
                model = queue.image,
                contentDescription = "Queue Image",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
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

@Composable
fun ReviewList(){

}

@Composable
fun ReviewCard(){

}