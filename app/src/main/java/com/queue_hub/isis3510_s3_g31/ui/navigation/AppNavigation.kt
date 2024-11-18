package com.queue_hub.isis3510_s3_g31.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.queue_hub.isis3510_s3_g31.MainScreen
import com.queue_hub.isis3510_s3_g31.MainViewModel
import com.queue_hub.isis3510_s3_g31.data.DataLayerFacade
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.queues.QueuesRepository
import com.queue_hub.isis3510_s3_g31.data.turns.TurnsRepository
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import com.queue_hub.isis3510_s3_g31.ui.screens.detail.DetailScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.detail.DetailViewModel
import com.queue_hub.isis3510_s3_g31.ui.screens.home.HomeScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.home.HomeViewModel
import com.queue_hub.isis3510_s3_g31.ui.screens.login.LoginScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.login.LoginViewModel
import com.queue_hub.isis3510_s3_g31.ui.screens.profile.ProfileScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.profile.ProfileViewModel
import com.queue_hub.isis3510_s3_g31.ui.screens.recommended.RecommendedScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.recommended.RecommendedViewModel
import com.queue_hub.isis3510_s3_g31.ui.screens.signup.SignUpScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.signup.SignUpViewModel
import com.queue_hub.isis3510_s3_g31.ui.screens.wait.WaitScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.wait.WaitViewModel
import com.queue_hub.isis3510_s3_g31.utils.location_services.LocationProvider


@Composable
fun AppNavigation(
    placesRepository: PlacesRepository,
    userRepository: UsersRepository,
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    usersRepository: UsersRepository,
    turnsRepository: TurnsRepository,
    startDestination: Any,
    queuesRepository: QueuesRepository,
    context: Context,
    mainViewModel: MainViewModel,
    dataLayerFacade: DataLayerFacade,
    locationProvider: LocationProvider,

    ) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination= startDestination,


    ){
        composable<Login> {
           LoginScreen(viewModel = LoginViewModel(usersRepository = userRepository) , navController = navController, auth = auth )
        }
        composable<Main> {
            MainScreen(navController = navController, placesRepository = placesRepository, usersRepository = usersRepository, turnsRepository = turnsRepository, queuesRepository = queuesRepository, context = context, locationProvider = LocationProvider(context), mainViewModel = mainViewModel, dataLayerFacade = dataLayerFacade )
        }
        composable<Home> {
            HomeScreen(navController = navController, modifier = Modifier, homeViewModel = HomeViewModel( dataLayerFacade ))
        }
        composable<Recommended>{
            RecommendedScreen(navController = navController, recommendedViewModel = RecommendedViewModel( dataLayerFacade ))
        }
        composable<Detail>{
            DetailScreen(navController = navController, modifier = Modifier, detailViewModel = DetailViewModel( dataLayerFacade))
        }
        composable<SignUp> {
            SignUpScreen(navController = navController, viewModel = SignUpViewModel(auth = auth, usersRepository = usersRepository), auth = auth, db = db)
        }
        composable<Wait> {
            WaitScreen(navController = navController, waitViewModel = WaitViewModel(turnsRepository = turnsRepository, usersRepository = usersRepository, queuesRepository = queuesRepository))
        }
        composable<Profile> {
            ProfileScreen(navController = navController, modifier = Modifier, profileViewModel = ProfileViewModel( placesRepository = placesRepository, usersRepository=usersRepository, turnsRepository = turnsRepository))
        }

    }



}