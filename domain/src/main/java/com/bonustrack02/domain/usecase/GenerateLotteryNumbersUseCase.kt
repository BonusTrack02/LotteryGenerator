package com.bonustrack02.domain.usecase

import com.bonustrack02.domain.model.GenerationHistory

class GenerateLotteryNumbersUseCase {
    operator fun invoke(): GenerationHistory {
        val numbers = (1..45).shuffled().take(6).sorted()
        return GenerationHistory(numbers)
    }
}
