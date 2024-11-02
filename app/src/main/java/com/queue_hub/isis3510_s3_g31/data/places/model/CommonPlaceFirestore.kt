package com.queue_hub.isis3510_s3_g31.data.places.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class CommonPlaceFirestore(
    val id: String,
    val name: String,
    val address: String,
    val phone: String,
    val image: String,
    val lastVisit: Timestamp,
    val city: String,
    val localization: GeoPoint,
    val type: String,
    val visitCount: Long
)
