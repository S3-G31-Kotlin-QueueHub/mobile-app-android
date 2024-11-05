package com.queue_hub.isis3510_s3_g31.ui.screens.profile
import android.app.Activity
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.places.mapper.toPlace
import com.queue_hub.isis3510_s3_g31.data.places.model.CommonPlace
import com.queue_hub.isis3510_s3_g31.ui.navigation.Detail
import com.queue_hub.isis3510_s3_g31.ui.navigation.Home
import com.queue_hub.isis3510_s3_g31.ui.navigation.Login
import com.queue_hub.isis3510_s3_g31.ui.screens.home.CommonPlaceCard
import com.queue_hub.isis3510_s3_g31.ui.screens.home.HomeViewState
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen(navController: NavController, modifier: Modifier, profileViewModel: ProfileViewModel) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Profile(modifier = Modifier, navController =  navController, profileViewModel)
    }
}

@Composable
fun Profile (modifier: Modifier, navController: NavController, profileViewModel: ProfileViewModel){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HeaderImage(modifier = Modifier, navController =navController, profileViewModel)
        Spacer(modifier = Modifier.padding(12.dp))
        HomeOptions(modifier = Modifier, profileViewModel)
        stats(modifier = Modifier, navController = navController, profileViewModel = profileViewModel)


    }

}

@Composable
fun stats(modifier: Modifier, navController: NavController, profileViewModel: ProfileViewModel) {

}

    @Composable
fun HeaderImage(modifier: Modifier, navController: NavController, profileViewModel: ProfileViewModel) {
    val logoImage = painterResource(R.drawable.queuehub_logo)
    val activity = LocalContext.current as? Activity
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val scope = rememberCoroutineScope()
    Row(
        modifier = modifier
            .padding(top = 30.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = logoImage,
            contentDescription = "Banner image",
            modifier = Modifier
                .size(width = 165.dp, height = 48.dp)
        )
        Row( modifier = modifier.clickable { navController.navigate(Login);
            scope.launch { profileViewModel.logOut()}},
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = "LogOut",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { }) {

                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Back",

                    modifier = Modifier.size(40.dp)
                )
            }
        }

    }
}



@Composable
fun HomeOptions(modifier: Modifier,profileViewModel: ProfileViewModel) {
    Column(

        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
    ) {


        Row (

            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()


        ){
            ClickableVerticalOption(

                image = painterResource(id = R.drawable.profile_example),
                modifier = Modifier,
                profileViewModel

            )


        }





    }

}

@Composable
fun ClickableVerticalOption(
    image: Painter,
    modifier: Modifier,
    profileViewModel: ProfileViewModel
) {
    Card(
        modifier = modifier
            .padding(3.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 30.dp),

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier

                .fillMaxWidth()
                .padding(30.dp,)

        ) {

            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(CircleShape)

            ) {
                Image(
                    painter = image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            };
            ;
            Text(
                text = profileViewModel.state.name,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Text(

                text = profileViewModel.state.email,
                style = MaterialTheme.typography.titleLarge.copy(),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Numero de filas hechas: " + profileViewModel.state.turns.size.toString(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )


        }

    }

    }





