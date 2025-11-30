package com.bonustrack02.domain.usecase

import com.bonustrack02.domain.repository.GenerationHistoryRepository

class FetchGenerationHistoryUseCase(private val repository: GenerationHistoryRepository) {
    suspend operator fun invoke() {
        repository.getAllGenerationHistory()
    }
}