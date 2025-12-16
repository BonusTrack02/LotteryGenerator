package com.bonustrack02.domain.model

import java.time.Instant

data class GenerationHistory(
    val id: Int = 0,
    val numbers: List<Int>,
    val generationTimestamp: Long = Instant.now().epochSecond
)
