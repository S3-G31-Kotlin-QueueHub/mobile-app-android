package com.queue_hub.isis3510_s3_g31.data.places.model

import com.google.firebase.firestore.GeoPoint

data class CommonPlace(
    val id : String,
    val name: String,
    val address: String,
    val phone: String,
    val image: String,
    val lastVisit: String,
    val city: String,
    val localization: GeoPoint,
)