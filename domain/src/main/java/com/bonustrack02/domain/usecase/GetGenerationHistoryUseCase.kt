package com.bonustrack02.domain.usecase

import com.bonustrack02.domain.model.GenerationHistory
import com.bonustrack02.domain.repository.GenerationHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGenerationHistoryUseCase @Inject constructor(
    private val repository: GenerationHistoryRepository
) {
    operator fun invoke(): Flow<List<GenerationHistory>> {
        return repository.getGenerationHistoryStream()
    }
}