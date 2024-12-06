package com.queue_hub.isis3510_s3_g31.data.clusters

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint

data class Cluster(
    val cluster_id : String,

    val center: LatLng,
    val places_num: Int,

){
    constructor() : this(
        cluster_id = "",
        center= LatLng(0.0,0.0),
        places_num = 0


    )
}
