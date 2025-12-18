package com.bonustrack02.domain.repository

import com.bonustrack02.domain.model.GenerationHistory
import kotlinx.coroutines.flow.Flow

interface GenerationHistoryRepository {
    suspend fun saveGenerationHistory(history: GenerationHistory)

    suspend fun deleteGenerationHistory(id: Int)

    fun getGenerationHistoryStream(): Flow<List<GenerationHistory>>

    suspend fun clearGenerationHistoryTable()
}
