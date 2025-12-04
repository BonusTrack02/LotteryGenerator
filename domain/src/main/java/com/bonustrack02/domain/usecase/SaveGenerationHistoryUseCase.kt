package com.bonustrack02.domain.usecase

import com.bonustrack02.domain.model.GenerationHistory
import com.bonustrack02.domain.repository.GenerationHistoryRepository
import javax.inject.Inject

class SaveGenerationHistoryUseCase @Inject constructor(
    private val repository: GenerationHistoryRepository
) {
    suspend operator fun invoke(history: GenerationHistory) {
        repository.saveGenerationHistory(history)
    }
}
