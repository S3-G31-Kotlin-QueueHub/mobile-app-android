package com.queue_hub.isis3510_s3_g31.data.places.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.queue_hub.isis3510_s3_g31.data.places.local.entity.PlaceEntity

@Database(entities = [PlaceEntity::class], version = 1, exportSchema = false)
abstract class PlacesDatabase : RoomDatabase() {
    abstract val dao: PlacesDao
}