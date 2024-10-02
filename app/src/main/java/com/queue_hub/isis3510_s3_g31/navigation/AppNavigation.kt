package com.queue_hub.isis3510_s3_g31.navigation

import LoginViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.queue_hub.isis3510_s3_g31.MainScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.home.HomeScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.login.LoginScreen


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

    }



}