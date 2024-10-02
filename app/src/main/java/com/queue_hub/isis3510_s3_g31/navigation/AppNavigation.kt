package com.queue_hub.isis3510_s3_g31.navigation

import LoginViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.queue_hub.isis3510_s3_g31.MainScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.home.HomeScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.login.LoginScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.recommended.RecommendedScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()



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
            RecommendedScreen(navController = navController)
        }

    }

}