package com.queue_hub.isis3510_s3_g31.navigation

import LoginViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.queue_hub.isis3510_s3_g31.MainScreen
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.places.local.PlacesDatabase
import com.queue_hub.isis3510_s3_g31.ui.screens.home.HomeScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.login.LoginScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.recommended.RecommendedScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.recommended.RecommendedViewModel


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val placesDb = Room.databaseBuilder(context = context, PlacesDatabase::class.java , name="places_db " ).build()
    val placesDAO = placesDb.dao
    val repository = PlacesRepository(placesDAO)

    NavHost(
        navController = navController,
        startDestination= Login
    ){
        composable<Login> {
           LoginScreen(viewModel = LoginViewModel() , navController = navController )
        }
        composable<Main> {
            MainScreen(navController = navController)
        }
        composable<Home> {
            HomeScreen(navController = navController)
        }
        composable<Recommended>{
            RecommendedScreen(navController = navController, recommendedViewModel = RecommendedViewModel( placesRepository = repository))
        }

    }



}