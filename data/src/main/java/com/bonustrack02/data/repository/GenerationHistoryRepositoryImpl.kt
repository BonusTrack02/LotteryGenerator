package com.bonustrack02.data.repository

import com.bonustrack02.data.dao.GenerationHistoryDao
import com.bonustrack02.data.entity.GenerationHistoryEntity
import com.bonustrack02.data.mapper.toModel
import com.bonustrack02.domain.model.GenerationHistory
import com.bonustrack02.domain.repository.GenerationHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GenerationHistoryRepositoryImpl @Inject constructor(
    private val dao: GenerationHistoryDao
) : GenerationHistoryRepository {
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

    override fun getGenerationHistoryStream(): Flow<List<GenerationHistory>> {
        return dao.getAll().map { entities ->
            entities.map { entity ->
                entity.toModel()
            }
        }.flowOn(Dispatchers.IO)
    }
}
