package com.bonustrack02.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.bonustrack02.data.entity.GenerationHistoryEntity

@Dao
interface GenerationHistoryDao {
    @Insert
    suspend fun insert(generationHistoryEntity: GenerationHistoryEntity)

    @Delete
    suspend fun delete(generationHistoryEntity: GenerationHistoryEntity)
}