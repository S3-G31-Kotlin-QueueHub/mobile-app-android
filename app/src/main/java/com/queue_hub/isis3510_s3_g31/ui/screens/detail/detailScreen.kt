package com.queue_hub.isis3510_s3_g31.ui.screens.detail
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.queue_hub.isis3510_s3_g31.R
import com.queue_hub.isis3510_s3_g31.ui.screens.home.Home
import com.queue_hub.isis3510_s3_g31.ui.theme.DarkGreen
import com.queue_hub.isis3510_s3_g31.ui.theme.LightGreen


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
        Buttons(modifier, navController)
        Spacer(modifier = Modifier.padding(8.dp))

    }

}


@Composable
fun Buttons(modifier: Modifier, navController: NavController) {
    Column(
        modifier = modifier
            .fillMaxWidth() // Asegura que la columna ocupe todo el ancho
    ) {
        Button(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth() ,
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGreen, // Color de fondo del botón
                contentColor = Color.White // Color del texto
            )
        ) {
            Text(text = "Give me a turn.")
        }

        Button(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth() ,
            colors = ButtonDefaults.buttonColors(
                containerColor = LightGreen, // Color de fondo del botón
                contentColor = Color.White // Color del texto
            )
        ) {
            Text(text = "See some reviews")
        }
    }
}
@Composable
fun HeaderImage(modifier: Modifier, navController: NavController) {
    val logoImage = painterResource(R.drawable.queuehub_logo)
    Row(
        modifier = modifier
            .padding(top = 30.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically, // Alinear verticalmente al centro
        horizontalArrangement = Arrangement.SpaceBetween // Espaciado entre los elementos
    ) {
        IconButton(onClick = { navController.navigate(Home) }) {
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
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(0.75f)

        ){

            ClickableVerticalOption(

                image = painterResource(id = R.drawable.img_1),
                modifier = Modifier.weight(1f),
                detailViewModel

            )


        }





    }

}

@Composable
fun ClickableVerticalOption(
    image: Painter,
    modifier: Modifier,
    detailViewModel: DetailViewModel
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

                .fillMaxWidth().padding(top = 20.dp)

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
            modifier = modifier.size(300.dp)
                .fillMaxWidth().padding(horizontal = 20.dp)


        ) {
            Text(
                text = "Average Waiting Time: ${detailViewModel.state.place.averageWaitingTime} min",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), // Poner en negrita
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Best arrival time: ${detailViewModel.state.place.bestAverageFrame}",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), // Poner en negrita
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "People on Queue: ${43}",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), // Poner en negrita
                modifier = Modifier.fillMaxWidth()
            )


        }
    }
}

