package com.bonustrack02.data.mapper

import com.bonustrack02.data.entity.GenerationHistoryEntity
import com.bonustrack02.domain.model.GenerationHistory

fun GenerationHistoryEntity.toModel(): GenerationHistory {
    return GenerationHistory(
        numbers = listOf(
            number1,
            number2,
            number3,
            number4,
            number5,
            number6,
        ),
        generationTimestamp = timestamp
    )
}