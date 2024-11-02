package com.queue_hub.isis3510_s3_g31.ui.screens.detail
import android.app.Activity
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.ui.navigation.Home
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.vector.rememberVectorPainter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
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
import com.queue_hub.isis3510_s3_g31.R
import com.queue_hub.isis3510_s3_g31.ui.screens.home.Home
import com.queue_hub.isis3510_s3_g31.ui.theme.DarkGreen
import com.queue_hub.isis3510_s3_g31.ui.theme.LightGreen
import kotlinx.coroutines.launch


@Composable
fun DetailScreen(navController: NavController, modifier: Modifier, detailViewModel: DetailViewModel) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Detail(modifier = Modifier, navController =  navController, detailViewModel)
    }
}

@Composable
fun Detail (modifier: Modifier, navController: NavController, detailViewModel: DetailViewModel){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HeaderImage(modifier = Modifier, navController =navController)
        Spacer(modifier = Modifier.padding(8.dp))

        Spacer(modifier = Modifier.padding(8.dp))
        HomeOptions(modifier = Modifier, detailViewModel)
        Spacer(modifier = Modifier.padding(8.dp))
        Buttons(modifier, navController, detailViewModel)
        Spacer(modifier = Modifier.padding(8.dp))

    }

}


@Composable
fun Buttons(modifier: Modifier, navController: NavController, detailViewModel: DetailViewModel) {
    val scope = rememberCoroutineScope()
    val queuedState by detailViewModel.queued_state.observeAsState(initial = false)

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Button(
            onClick = {
                scope.launch {
                if(!queuedState){
                    detailViewModel.addTurn()

                    navController.navigateUp()

                }else{

                    navController.navigate(Home);

                }

            }},
            modifier = Modifier.fillMaxWidth() ,
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGreen,
                contentColor = Color.White
            )
        ) {
            Text(text = "Give me a turn.")
        }

        Button(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth() ,
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
fun HomeOptions(modifier: Modifier,detailViewModel: DetailViewModel) {
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

                image = painterResource(id = R.drawable.img_1),
                modifier = Modifier.weight(1f),
                detailViewModel,
                detailViewModel.userId

            )


        }





    }

}

@Composable
fun ClickableVerticalOption(
    image: Painter,
    modifier: Modifier,
    detailViewModel: DetailViewModel,
    userId: String?
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 30.dp),

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier

                .fillMaxWidth()
                .padding(top = 20.dp)

        ) {
            Text(
                text = detailViewModel.state.place.nombre,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .fillMaxWidth()

            )


        }
        Column(

            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .size(300.dp)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),



        ) {
            Text(
                text = "Average Waiting Time: ${userId} min",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Best arrival time: ${detailViewModel.state.place.bestAverageFrame}",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "People on Queue: ${43}",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )


        }
    }
}

