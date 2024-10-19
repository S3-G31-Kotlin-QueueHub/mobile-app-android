package com.queue_hub.isis3510_s3_g31.navigation

import LoginViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.queue_hub.isis3510_s3_g31.MainScreen
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import com.queue_hub.isis3510_s3_g31.ui.screens.detail.DetailScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.detail.DetailViewModel
import com.queue_hub.isis3510_s3_g31.ui.screens.home.HomeScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.home.HomeViewModel
import com.queue_hub.isis3510_s3_g31.ui.screens.login.LoginScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.recommended.RecommendedScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.recommended.RecommendedViewModel
import com.queue_hub.isis3510_s3_g31.ui.screens.signup.SignUpScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.signup.SignUpViewModel


@Composable
fun AppNavigation(
    placesRepository: PlacesRepository,
    userRepository: UsersRepository,
    auth: FirebaseAuth,
    db: FirebaseFirestore
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination= Login
    ){
        composable<Login> {
           LoginScreen(viewModel = LoginViewModel(usersRepository = userRepository) , navController = navController, auth = auth )
        }
        composable<Main> {
            MainScreen(navController = navController, placesRepository = placesRepository)
        }
        composable<Home> {
            HomeScreen(navController = navController, modifier = Modifier, homeViewModel = HomeViewModel( placesRepository ))
        }
        composable<Recommended>{
            RecommendedScreen(navController = navController, recommendedViewModel = RecommendedViewModel( placesRepository ))
        }
        composable<Detail>{

            DetailScreen(navController = navController, modifier = Modifier, detailViewModel = DetailViewModel( placesRepository = placesRepository))
        }
        composable<SignUp> {
            SignUpScreen(navController = navController, viewModel = SignUpViewModel(auth = auth), auth = auth, db = db)
        }

    }



}