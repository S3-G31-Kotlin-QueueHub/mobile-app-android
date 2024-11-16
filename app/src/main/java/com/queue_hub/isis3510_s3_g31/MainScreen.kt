package com.queue_hub.isis3510_s3_g31

import android.Manifest
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.queue_hub.isis3510_s3_g31.data.DataLayerFacade
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.queues.QueuesRepository
import com.queue_hub.isis3510_s3_g31.data.turns.TurnsRepository
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import com.queue_hub.isis3510_s3_g31.ui.components.BottomNavItem
import com.queue_hub.isis3510_s3_g31.ui.screens.home.HomeScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.home.HomeViewModel
import com.queue_hub.isis3510_s3_g31.ui.screens.profile.ProfileScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.profile.ProfileViewModel
import com.queue_hub.isis3510_s3_g31.ui.screens.recommended.RecommendedScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.recommended.RecommendedViewModel
import com.queue_hub.isis3510_s3_g31.ui.screens.userQueues.UserQueuesScreen
import com.queue_hub.isis3510_s3_g31.ui.screens.userQueues.UserQueuesViewModel
import com.queue_hub.isis3510_s3_g31.ui.theme.ISIS3510S3G31Theme
import com.queue_hub.isis3510_s3_g31.utils.location_services.LocationData
import com.queue_hub.isis3510_s3_g31.utils.location_services.LocationProvider

@Composable
fun MainScreen(navController: NavController,
               placesRepository: PlacesRepository,
               usersRepository: UsersRepository,
               turnsRepository: TurnsRepository,
               queuesRepository: QueuesRepository,
               locationProvider: LocationProvider,
               context: Context,
               mainViewModel: MainViewModel,
               dataLayerFacade: DataLayerFacade) {

    val navItemList = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            label = "Queues",
            icon = Icons.Default.Place
        ),
        BottomNavItem(
            label = "Recommended",
            icon = Icons.Default.Favorite
        ),
        BottomNavItem(
            label = "Profile",
            icon = Icons.Default.Face
        ),
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    val location = mainViewModel.location.value
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions() ,
        onResult = { permissions ->
            if( permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                &&
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            ) {
                locationProvider.requestLocationUpdates(mainViewModel)
            } else {
                // ask for permission
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION ) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            context as MainActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION )

                if(rationaleRequired) {
                    Toast.makeText(context,"This feature require location permission",Toast.LENGTH_LONG).show()
                }else{
                    // need to set the permission from setting.
                    Toast.makeText(context,"Please enable location permission from android setting",Toast.LENGTH_LONG).show()
                }
            }
        })

    LaunchedEffect(Unit) {
        if(locationProvider.hasLocationPermission(context)) {
            locationProvider.requestLocationUpdates(mainViewModel = mainViewModel)
            Log.d("Localizacion", "${location}")
        }else {
            // Request location permission
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
    ISIS3510S3G31Theme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar(
                    containerColor = colorScheme.onPrimary,
                    tonalElevation = 5.dp,
                ) {
                    navItemList.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = {
                                selectedIndex = index
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    fontSize = 11.sp

                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = colorScheme.onPrimary,
                                unselectedIconColor = colorScheme.primary,
                                selectedTextColor = colorScheme.primary,
                                unselectedTextColor = colorScheme.onSurface.copy(alpha = 0.6f),
                                indicatorColor = colorScheme.primary
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->

        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            selectedIndex = selectedIndex,
            navController = navController,
            placesRepository = placesRepository,
            usersRepository = usersRepository,
            turnsRepository = turnsRepository,
            queuesRepository = queuesRepository,
            location = location,
            dataLayerFacade = dataLayerFacade
            )

        }
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier,
                  selectedIndex: Int,
                  navController: NavController,
                  placesRepository: PlacesRepository,
                  usersRepository: UsersRepository,
                  turnsRepository: TurnsRepository,
                  queuesRepository: QueuesRepository,
                  location: LocationData?,
                  dataLayerFacade: DataLayerFacade){

    when(selectedIndex){

        0 -> HomeScreen(
            navController = navController,
            modifier = Modifier,
            homeViewModel = HomeViewModel(
                dataLayerFacade
            ),
            location = location
        )
        1 -> UserQueuesScreen(navController = navController, userQueuesViewModel = UserQueuesViewModel(queuesRepository = queuesRepository, usersRepository = usersRepository, turnsRepository = turnsRepository))
        2 -> RecommendedScreen(navController = navController, recommendedViewModel = RecommendedViewModel(dataLayerFacade = dataLayerFacade))
        3 -> ProfileScreen(navController = navController , profileViewModel = ProfileViewModel(placesRepository = placesRepository, usersRepository = usersRepository, turnsRepository= turnsRepository) ,  modifier = Modifier)
    }
}