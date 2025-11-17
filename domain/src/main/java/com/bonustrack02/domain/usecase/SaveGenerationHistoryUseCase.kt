package com.bonustrack02.domain.usecase

import com.bonustrack02.domain.model.GenerationHistory
import com.bonustrack02.domain.repository.GenerationHistoryRepository

class SaveGenerationHistoryUseCase(private val repository: GenerationHistoryRepository) {
    suspend operator fun invoke(history: GenerationHistory) {
        repository.saveGenerationHistory(history)
    }
}
