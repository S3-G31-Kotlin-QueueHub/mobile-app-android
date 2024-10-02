package com.queue_hub.isis3510_s3_g31.data.places.local

import androidx.room.Dao
import androidx.room.Query
import com.queue_hub.isis3510_s3_g31.data.places.local.entity.PlaceEntity

@Dao
interface PlacesDao {

    @Query("SELECT * FROM PlaceEntity")
    suspend fun getPlaces(): List<PlaceEntity>
}