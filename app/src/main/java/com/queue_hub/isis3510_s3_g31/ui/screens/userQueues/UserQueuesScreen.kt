package com.queue_hub.isis3510_s3_g31.ui.screens.userQueues

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
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.google.firebase.Timestamp
import com.queue_hub.isis3510_s3_g31.R
import com.queue_hub.isis3510_s3_g31.data.queues.model.PreviousQueue
import com.queue_hub.isis3510_s3_g31.data.queues.model.Queue
import com.queue_hub.isis3510_s3_g31.data.turns.model.Turn
import com.queue_hub.isis3510_s3_g31.ui.screens.home.ConnectivityBanner
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


@Composable
fun UserQueuesScreen(
    navController: NavController,
    userQueuesViewModel: UserQueuesViewModel
) {

    val queuesState by userQueuesViewModel.queuesState.collectAsState()
    val turnState by userQueuesViewModel.turnState.collectAsState()
    val queueState by userQueuesViewModel.queueState.collectAsState()

    Box(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        UserQueues(modifier = Modifier, queuesState = queuesState, turnState = turnState, queueState = queueState, navController = navController, userQueuesViewModel = userQueuesViewModel)
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
fun UserQueues(
    modifier: Modifier,
    queuesState: UserQueuesViewState<List<PreviousQueue>>,
    navController: NavController,
    turnState: UserQueuesViewState<Turn>,
    queueState: UserQueuesViewState<Queue>,
    userQueuesViewModel: UserQueuesViewModel
){
    val isConnected by userQueuesViewModel.isConnected.collectAsState(initial = true)
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

@Composable
fun TurnCard(
    modifier: Modifier,
    turnState: UserQueuesViewState<Turn>,
    queueState: UserQueuesViewState<Queue>,
    userQueuesViewModel: UserQueuesViewModel
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = {})
            .height(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.onPrimary)
    ) {
        Row(
            modifier = Modifier
                .weight(0.5F, true)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            when {
                turnState is UserQueuesViewState.Success && queueState is UserQueuesViewState.Success -> {
                    val turn = turnState.data
                    val queue = queueState.data

                    when {
                        queue.currentTurnNumber == turn.turnNumber -> {
                            ActiveTurnInfo(queue = queue, userQueuesViewModel = userQueuesViewModel)
                        }
                        turn.idUser.isNotEmpty() -> {
                            WaitingTurnInfo(turn = turn, queue = queue, userQueuesViewModel = userQueuesViewModel)
                        }
                        else -> {
                            EmptyQueueState()
                        }
                    }
                }
                turnState is UserQueuesViewState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                turnState is UserQueuesViewState.Error -> {

                }
            }

        }
    }
}

@Composable
fun EmptyQueueState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(id = R.string.you_are_not_in_any_queue_right_now),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Medium
            ),
            color = colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.find_and_join_a_queue_to_start_tracking_your_turn),
            style = MaterialTheme.typography.bodyLarge,
            color = colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ActiveTurnInfo(queue: Queue, userQueuesViewModel: UserQueuesViewModel) {
    val isConnected by userQueuesViewModel.isConnected.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1.2f)
                .fillMaxHeight()
        ) {


            Text(
                text = stringResource(R.string.it_is_your_turn),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = colorScheme.primary
            )


            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = queue.name,
                style = MaterialTheme.typography.titleMedium,
                color = colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Current turn: #${queue.currentTurnNumber}",
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.primary
            )
            if(!isConnected){
                Text(
                    text = "No internet connection, arrived at the place to get your turn",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = colorScheme.error
                )
            }

        }

        Card(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxHeight()
                .padding(start = 16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            GlideImage(
                model = queue.image,
                contentDescription = "Queue Image",
                modifier = Modifier.fillMaxSize(),
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


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WaitingTurnInfo(turn: Turn, queue: Queue, userQueuesViewModel: UserQueuesViewModel) {

    val showCancelDialog by userQueuesViewModel.showCancelDialog.collectAsState()
    val isConnected by userQueuesViewModel.isConnected.collectAsState()

    if(showCancelDialog){
        CancelTurnDialog(
            onDismiss = { userQueuesViewModel.hideCancelDialog() },
            onConfirm = { userQueuesViewModel.cancelTurn() }
        )
    }

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

            if(!isConnected){
                Text(
                    text = "No internet connection, arrived at the place to check your turn",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = colorScheme.error,
                    textAlign = TextAlign.Center
                )
            } else {
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
fun CancelTurnDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.cancel)) },
        text = { Text(stringResource(R.string.are_you_sure_you_want_to_cancel_your_turn)) },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.keep_my_place))
            }
        }
    )
}


@Composable
fun QueueList(modifier: Modifier, queuesState: UserQueuesViewState<List<PreviousQueue>>, navController: NavController){

    if(queuesState is UserQueuesViewState.Success){
        val queues = queuesState.data
        if(queues.isEmpty()){
            Text(
                text = stringResource(R.string.you_haven_t_been_in_any_queue_yet),
                textAlign = TextAlign.Center,
            )
        } else {
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(bottom = 60.dp)){
                itemsIndexed(queuesState.data) { _, queue ->
                    QueueCard(queue = queue, onClick = {

                    })
                }
            }
        }
    } else if (queuesState is UserQueuesViewState.Error){

    } else if (queuesState is UserQueuesViewState.Loading){
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun QueueCard(queue: PreviousQueue, onClick: () -> Unit) {
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
                        text = queue.name,
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
                            text = "${queue.address}, ${queue.city}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

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
                    Text(
                        text = formatLastVisit(queue.lastVisit),
                        style = MaterialTheme.typography.labelMedium,
                        color = colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Card(
                modifier = Modifier.size(120.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                GlideImage(
                    model = queue.image,
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

private fun formatLastVisit(timestamp: Timestamp): String {
    val date = Date(timestamp.seconds * 1000)
    val now = Date()

    val diffInMillis = now.time - date.time
    val diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
    val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)

    return when {
        diffInHours < 24 -> {
            when (diffInHours) {
                0L -> "Visited recently"
                1L -> "Visited 1 hour ago"
                else -> "Visited $diffInHours hours ago"
            }
        }
        diffInDays == 1L -> "Visited yesterday"
        diffInDays < 7 -> "Visited $diffInDays days ago"
        else -> SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(date)
    }
}