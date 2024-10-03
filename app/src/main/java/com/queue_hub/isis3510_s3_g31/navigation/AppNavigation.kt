package com.queue_hub.isis3510_s3_g31.navigation

import LoginViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.queue_hub.isis3510_s3_g31.MainScreen
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.places.local.PlacesDatabase
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import com.queue_hub.isis3510_s3_g31.ui.screens.home.HomeScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.login.LoginScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.recommended.RecommendedScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.recommended.RecommendedViewModel


@Composable
fun AppNavigation(placesRepository: PlacesRepository, userRepository: UsersRepository) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination= Login
    ){
        composable<Login> {
           LoginScreen(viewModel = LoginViewModel(usersRepository = userRepository) , navController = navController )
        }
        composable<Main> {
            MainScreen(navController = navController, placesRepository = placesRepository)
        }
        composable<Home> {
            HomeScreen(navController = navController, modifier = Modifier)
        }
        composable<Recommended>{
            RecommendedScreen(navController = navController, recommendedViewModel = RecommendedViewModel( placesRepository ))
        }

    }



}