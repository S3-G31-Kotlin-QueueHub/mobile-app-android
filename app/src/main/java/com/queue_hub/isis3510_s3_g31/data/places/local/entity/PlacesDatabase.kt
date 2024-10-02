package com.queue_hub.isis3510_s3_g31.data.places.local.entity

import androidx.room.Database
import androidx.room.RoomDatabase
import com.queue_hub.isis3510_s3_g31.data.places.local.PlacesDao

@Database(entities = [PlaceEntity::class], version = 1)
abstract class PlacesDatabase : RoomDatabase() {
    abstract val dao: PlacesDao
}