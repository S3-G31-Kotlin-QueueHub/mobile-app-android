package com.queue_hub.isis3510_s3_g31.ui.screens.wait

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.queue_hub.isis3510_s3_g31.R
import com.queue_hub.isis3510_s3_g31.data.queues.model.Queue
import com.queue_hub.isis3510_s3_g31.data.turns.model.Turn
import com.queue_hub.isis3510_s3_g31.ui.navigation.Main
import com.queue_hub.isis3510_s3_g31.ui.screens.login.ConnectivityBanner


@Composable
fun WaitScreen(
    navController: NavController,
    waitViewModel: WaitViewModel
    ) {

    val state by waitViewModel.uiState.collectAsState()

    Box(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Wait(modifier = Modifier, navController =  navController, state = state, waitViewModel = waitViewModel)
    }
}



@Composable
fun Wait (
    modifier: Modifier,
    navController: NavController,
    state: WaitViewState,
    waitViewModel: WaitViewModel
){
    val isConnected by waitViewModel.isConnected.collectAsState(true)
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HeaderImage(modifier = Modifier, navController = navController )
        Spacer(modifier = Modifier.padding(16.dp))
        if (!isConnected) {
            Spacer(modifier = Modifier.height(12.dp))
            ConnectivityBanner()
        }
        WaitCard(modifier = Modifier, navController = navController, state = state, waitViewModel = waitViewModel)
        Spacer(modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun HeaderImage(modifier: Modifier, navController: NavController) {
    val logoImage = painterResource(R.drawable.queuehub_logo)

    Row(
        modifier = modifier
            .padding(top = 30.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { navController.navigate(Main) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
fun WaitCard(
    modifier: Modifier,
    navController: NavController,
    state: WaitViewState,
    waitViewModel: WaitViewModel
){

    when (state) {
        is WaitViewState.Loading -> {
            CircularProgressIndicator()
        }
        is WaitViewState.Success -> {
            val turn = state.turn
            val queue = state.queue

            if(queue.currentTurnNumber == turn.turnNumber){
                TurnInformation(navController = navController, modifier = modifier)
            }else{
                WaitInformation(navController = navController, modifier = modifier, turn = turn, queue = queue, waitViewModel = waitViewModel)
            }

        }
        is WaitViewState.Error -> {
            Text(text = state.message)
        }

    }
}


@Composable
fun WaitInformation(navController: NavController, modifier: Modifier, turn: Turn, queue: Queue, waitViewModel: WaitViewModel){
    val isConnected by waitViewModel.isConnected.collectAsState(true)
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
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.thank_you_for_being_patient),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = colorScheme.secondary
            )
            Text(
                text = stringResource(R.string.your_current_position_is),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            TurnNumber(number = turn.turnNumber, modifier = Modifier)
            Text(
                text = stringResource(R.string.the_queue_is_currently_on_turn),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            TurnNumber(number = queue.currentTurnNumber, modifier = Modifier)
            if (!isConnected) {
                Text(
                    text = "No internet connection, please go to the place and ask for your turn",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.error
                )
            }else{
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        waitViewModel.cancelTurn()
                        navController.navigate(Main)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Text(
                        text = stringResource(R.string.i_don_t_need_it_anymore)
                    )
                }
            }
        }
    }
}

@Composable
fun TurnInformation(navController: NavController, modifier: Modifier){
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
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.thank_you_for_being_patient),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.it_is_your_turn),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.go_to_the_place_and_take_your_order),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    navController.navigate(Main)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = stringResource(R.string.understood)
                )
            }
        }
    }
}



@Composable
fun TurnNumber(
    number: Long,
    modifier: Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            fontSize = 96.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.secondary,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.LightGray,
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            )
        )
    }
}
