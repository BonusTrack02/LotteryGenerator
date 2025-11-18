package com.bonustrack02.data.repository

import com.bonustrack02.data.dao.GenerationHistoryDao
import com.bonustrack02.data.entity.GenerationHistoryEntity
import com.bonustrack02.domain.model.GenerationHistory
import com.bonustrack02.domain.repository.GenerationHistoryRepository

class GenerationHistoryRepositoryImpl(private val dao: GenerationHistoryDao) : GenerationHistoryRepository {
    override suspend fun saveGenerationHistory(history: GenerationHistory) {
        val entity = GenerationHistoryEntity(
            timestamp = history.generationTimestamp,
            number1 = history.numbers[0],
            number2 = history.numbers[1],
            number3 = history.numbers[2],
            number4 = history.numbers[3],
            number5 = history.numbers[4],
            number6 = history.numbers[5],
        )
        dao.insert(entity)
    }

    override suspend fun getAllGenerationHistory(): List<GenerationHistory> {
        return dao.getAll().map { entity ->
            GenerationHistory(
                numbers = listOf(
                    entity.number1,
                    entity.number2,
                    entity.number3,
                    entity.number4,
                    entity.number5,
                    entity.number6
                ),
                generationTimestamp = entity.timestamp
            )
        }
    }
}
