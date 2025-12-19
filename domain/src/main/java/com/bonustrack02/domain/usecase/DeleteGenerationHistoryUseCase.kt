package com.bonustrack02.domain.usecase

import com.bonustrack02.domain.repository.GenerationHistoryRepository
import javax.inject.Inject

class DeleteGenerationHistoryUseCase @Inject constructor(
    private val repository: GenerationHistoryRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteGenerationHistory(id)
    }
}