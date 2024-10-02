package com.queue_hub.isis3510_s3_g31.data.places.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.queue_hub.isis3510_s3_g31.data.places.local.entity.PlaceEntity

@Dao
interface PlacesDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place:PlaceEntity)

    @Query("SELECT * FROM PlaceEntity")
    suspend fun getPlaces(): List<PlaceEntity>
}