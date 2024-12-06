package com.queue_hub.isis3510_s3_g31
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.queue_hub.isis3510_s3_g31.data.DataLayerFacade
import com.queue_hub.isis3510_s3_g31.data.clusters.Cluster
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.places.local.PlacesDatabase
import com.queue_hub.isis3510_s3_g31.data.places.remote.PlacesApi
import com.queue_hub.isis3510_s3_g31.data.queues.QueuesRepository
import com.queue_hub.isis3510_s3_g31.data.reviews.ReviewsRepository
import com.queue_hub.isis3510_s3_g31.data.turns.TurnsRepository
import com.queue_hub.isis3510_s3_g31.data.turns.local.TurnsDatabase
import com.queue_hub.isis3510_s3_g31.data.turns.remote.TurnApi
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import com.queue_hub.isis3510_s3_g31.utils.location_services.LocationProvider
import com.queue_hub.isis3510_s3_g31.utils.network_services.AndroidNetworkManager
import com.queue_hub.isis3510_s3_g31.utils.network_services.NetworkManager
import kotlinx.coroutines.launch
import kotlin.random.Random

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var db: FirebaseFirestore
    private lateinit var mMap: GoogleMap
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var name: String = ""
    private var mapViewModel: MapViewModel ?= null
    private lateinit var usersRepository: UsersRepository
    private lateinit var placesRepository: PlacesRepository
    private lateinit var queuesRepository: QueuesRepository
    private lateinit var turnsRepository: TurnsRepository
    private lateinit var locationProvider: LocationProvider
    private lateinit var networkManager: NetworkManager
    private lateinit var reviewsRepository: ReviewsRepository
    private lateinit var auth: FirebaseAuth
    private lateinit var clusters: List<Cluster>

    override fun onCreate(savedInstanceState: Bundle?) {

        db = Firebase.firestore

        auth = FirebaseAuth.getInstance()

        val placesDb = Room.databaseBuilder(this, PlacesDatabase::class.java , name="places_db " ).build()
        val placesDAO = placesDb.dao
        val turnDb = Room.databaseBuilder(this, TurnsDatabase::class.java , name="turns" ).build()
        val turnDAO = turnDb.dao

        networkManager = AndroidNetworkManager(this)
        locationProvider = LocationProvider(this)
        usersRepository = UsersRepository(applicationContext, db = db, auth = auth)
        placesRepository = PlacesRepository(placesDAO, api = PlacesApi.instance, db = db)
        turnsRepository = TurnsRepository(turnsApi = TurnApi.instance2, db, turnsDao =turnDAO )
        queuesRepository = QueuesRepository()
        reviewsRepository = ReviewsRepository(db = db)

        val dataLayerFacade = DataLayerFacade(
            placesRepository = placesRepository,
            turnsRepository = turnsRepository,
            queuesRepository = queuesRepository,
            usersRepository = usersRepository,
            locationProvider = locationProvider,
            networkManager = networkManager,
            reviewsRepository = reviewsRepository
        )

        mapViewModel = MapViewModel(dataLayerFacade = dataLayerFacade)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)
        name = intent.getStringExtra("name") ?: "Unknown Location"
        println("Founded Dada: $name $latitude $longitude")

        val mapFragment = supportFragmentManager

            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        lifecycleScope.launch {
            mapViewModel?.let { viewModel ->
            viewModel.clusterList.collect { clusters ->
                this@MapActivity.clusters = clusters
                var cont = 0
                var red = 0
                var green =0
                var blue =0
                val alpha = 90
                var color  = android.graphics.Color.argb(0,0,0,0)

                for (cluster in clusters) {

                    red = Random.nextInt(0, 256)
                    green = Random.nextInt(0, 256)
                    blue = Random.nextInt(0, 256)
                    color = android.graphics.Color.argb(alpha, red, green, blue)
                    cont+=1
                    val location = LatLng(cluster.center.latitude, cluster.center.longitude)
                    mMap.addCircle(
                        CircleOptions()
                            .center(location)
                            .radius(500.0)


                            .fillColor(android.graphics.Color.argb (alpha, red, blue, green))

                    )

                    mMap.addMarker(
                        MarkerOptions()
                            .position(location)
                            .title("Cluster ${cont}")
                            .icon(BitmapDescriptorFactory.defaultMarker())
                            .icon(BitmapDescriptorFactory.defaultMarker(210f))

                    )
                }
            }
            }
        }
        val location = LatLng(latitude, longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        mMap.addMarker(MarkerOptions().position(location).title(name))
        mMap.setOnMapLoadedCallback {
            println("El mapa se cargó correctamente.")
        }


    }
}
