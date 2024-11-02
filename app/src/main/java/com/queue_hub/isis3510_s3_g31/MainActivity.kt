package com.queue_hub.isis3510_s3_g31

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.places.local.PlacesDatabase
import com.queue_hub.isis3510_s3_g31.data.places.remote.PlacesApi
import com.queue_hub.isis3510_s3_g31.data.turns.TurnsRepository
import com.queue_hub.isis3510_s3_g31.data.turns.remote.TurnApi
import com.queue_hub.isis3510_s3_g31.data.users.UserPreferencesRepository
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import com.queue_hub.isis3510_s3_g31.data.users.remote.UserApi
import com.queue_hub.isis3510_s3_g31.ui.navigation.AppNavigation
import com.queue_hub.isis3510_s3_g31.ui.theme.ISIS3510S3G31Theme

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }

        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        db = Firebase.firestore
        userPreferencesRepository = UserPreferencesRepository(applicationContext)
        val placesDb = Room.databaseBuilder(this, PlacesDatabase::class.java , name="places_db " ).build()
        val placesDAO = placesDb.dao
        val repositoryPlaces= PlacesRepository(placesDAO, api = PlacesApi.instance)
        val repositoryUsers = UsersRepository(apiUsers = UserApi.instance2)
        val repositoryTurns = TurnsRepository(turnsApi = TurnApi.instance2, db)

        viewModel.checkAuthState(userPreferencesRepository)

        setContent {
            val startDestination by viewModel.startDestination.collectAsState()

            ISIS3510S3G31Theme {
                AppNavigation(
                    startDestination = startDestination,
                    placesRepository = repositoryPlaces,
                    userRepository = repositoryUsers,
                    auth = auth,
                    db = db,
                    userPreferencesRepository = userPreferencesRepository,
                    turnsRepository = repositoryTurns
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ISIS3510S3G31Theme {

    }
}