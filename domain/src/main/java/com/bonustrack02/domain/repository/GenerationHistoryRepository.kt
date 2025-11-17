package com.bonustrack02.domain.repository

import com.bonustrack02.domain.model.GenerationHistory

interface GenerationHistoryRepository {
    suspend fun saveGenerationHistory(history: GenerationHistory)
}
