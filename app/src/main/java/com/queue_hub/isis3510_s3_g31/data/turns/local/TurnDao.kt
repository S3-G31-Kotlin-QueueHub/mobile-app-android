package com.queue_hub.isis3510_s3_g31.data.turns.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.queue_hub.isis3510_s3_g31.data.turns.local.entity.TurnEntity

@Dao
interface TurnDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTurn(turn:TurnEntity)

    @Query("SELECT * FROM TurnEntity")
    suspend fun getPlaces(): List<TurnEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTurns(turns: List<TurnEntity>)
}