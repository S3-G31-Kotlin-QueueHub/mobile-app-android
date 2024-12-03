package com.queue_hub.isis3510_s3_g31.utils.location_services

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class LocationProvider(private val context: Context) {

    private var _fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)


    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }
    @SuppressLint("MissingPermission")

    fun requestLocationUpdates(): Flow<LocationData> = callbackFlow {
        if (!hasLocationPermission()) {
            close()
            return@callbackFlow
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    trySend(LocationData(it.latitude, it.longitude)).isSuccess
                }
            }
        }


        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
        _fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())


        awaitClose {
            _fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }


    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): LocationData? {
        return if (hasLocationPermission()) {
            val location = _fusedLocationClient.lastLocation.await()
            location?.let { LocationData(it.latitude, it.longitude) }
        } else {
            null
        }
    }
}
