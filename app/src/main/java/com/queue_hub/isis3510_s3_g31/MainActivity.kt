package com.queue_hub.isis3510_s3_g31

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.places.local.PlacesDatabase
import com.queue_hub.isis3510_s3_g31.data.places.remote.PlacesApi
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import com.queue_hub.isis3510_s3_g31.data.users.remote.UserApi
import com.queue_hub.isis3510_s3_g31.navigation.AppNavigation
import com.queue_hub.isis3510_s3_g31.ui.theme.ISIS3510S3G31Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val placesDb = Room.databaseBuilder(this, PlacesDatabase::class.java , name="places_db " ).build()
        val placesDAO = placesDb.dao
        val repositoryPlaces= PlacesRepository(placesDAO, api = PlacesApi.instance)
        val repositoryUsers = UsersRepository(apiUsers = UserApi.instance2)
        setContent {
            ISIS3510S3G31Theme {
                AppNavigation(placesRepository = repositoryPlaces, userRepository = repositoryUsers)
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