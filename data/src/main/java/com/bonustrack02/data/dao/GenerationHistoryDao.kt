package com.bonustrack02.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bonustrack02.data.entity.GenerationHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GenerationHistoryDao {
    @Insert
    suspend fun insert(generationHistoryEntity: GenerationHistoryEntity)

    @Query("DELETE FROM generation_history WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM generation_history")
    suspend fun deleteAllGenerationHistory()

    @Query("SELECT * FROM generation_history ORDER BY id DESC")
    fun getAll(): Flow<List<GenerationHistoryEntity>>
}