package com.queue_hub.isis3510_s3_g31.utils.location_services

import kotlin.math.*

class DistanceCalculator private constructor() {
    companion object {

        @Volatile
        private var instance: DistanceCalculator? = null

        fun getInstance(): DistanceCalculator {
            return instance ?: synchronized(this) {
                instance ?: DistanceCalculator().also { instance = it }
            }
        }
    }


    fun calculateHaversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadiusKm = 6371.0

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))


        return earthRadiusKm * c
    }
}
