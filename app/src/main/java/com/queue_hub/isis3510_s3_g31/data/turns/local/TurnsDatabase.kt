package com.queue_hub.isis3510_s3_g31.data.turns.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.queue_hub.isis3510_s3_g31.data.turns.local.entity.TurnEntity


@Database(entities = [TurnEntity::class], version = 1, exportSchema = false)
abstract class TurnsDatabase : RoomDatabase() {
    abstract val dao: TurnDao
}