package com.queue_hub.isis3510_s3_g31

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.queue_hub.isis3510_s3_g31.data.DataLayerFacade
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.places.local.PlacesDatabase
import com.queue_hub.isis3510_s3_g31.data.places.remote.PlacesApi
import com.queue_hub.isis3510_s3_g31.data.queues.QueuesRepository
import com.queue_hub.isis3510_s3_g31.data.queues.remote.FirebaseQueueData
import com.queue_hub.isis3510_s3_g31.data.turns.TurnsRepository
import com.queue_hub.isis3510_s3_g31.data.turns.local.TurnsDatabase
import com.queue_hub.isis3510_s3_g31.data.turns.remote.TurnApi
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import com.queue_hub.isis3510_s3_g31.ui.navigation.AppNavigation
import com.queue_hub.isis3510_s3_g31.ui.theme.ISIS3510S3G31Theme

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var usersRepository: UsersRepository
    private lateinit var placesRepository: PlacesRepository
    private lateinit var queuesRepository: QueuesRepository
    private lateinit var turnsRepository: TurnsRepository

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


        val placesDb = Room.databaseBuilder(this, PlacesDatabase::class.java , name="places_db " ).build()
        val placesDAO = placesDb.dao
        val turnDb = Room.databaseBuilder(this, TurnsDatabase::class.java , name="turns" ).build()
        val turnDAO = turnDb.dao
        usersRepository = UsersRepository(applicationContext, db = db, auth = auth)
        placesRepository = PlacesRepository(placesDAO, api = PlacesApi.instance, db = db)
        turnsRepository = TurnsRepository(turnsApi = TurnApi.instance2, db, turnsDao =turnDAO )
        queuesRepository = QueuesRepository()
        viewModel.checkAuthState(usersRepository)
        askNotificationPermission();

        val dataLayerFacade = DataLayerFacade(
            placesRepository = placesRepository,
            turnsRepository = turnsRepository,
            queuesRepository = queuesRepository,
            usersRepository = usersRepository
        )


        setContent {
            val startDestination by viewModel.startDestination.collectAsState()

            ISIS3510S3G31Theme {
                AppNavigation(
                    startDestination = startDestination,
                    placesRepository = placesRepository,
                    userRepository = usersRepository,
                    auth = auth,
                    db = db,
                    usersRepository = usersRepository,
                    turnsRepository = turnsRepository,
                    queuesRepository = queuesRepository,
                    context = this,
                    mainViewModel = viewModel,
                    dataLayerFacade = dataLayerFacade
                )
            }
        }
    }


    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if(isGranted){

        } else {

        }

    }

}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ISIS3510S3G31Theme {

    }
}