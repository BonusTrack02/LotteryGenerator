package com.bonustrack02.domain.usecase

import com.bonustrack02.domain.repository.GenerationHistoryRepository
import javax.inject.Inject

class FetchGenerationHistoryUseCase @Inject constructor(
    private val repository: GenerationHistoryRepository
) {
    suspend operator fun invoke() {
        repository.getAllGenerationHistory()
    }
}