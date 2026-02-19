package com.bonustrack02.lotterygenerator.presentation.history

import com.bonustrack02.domain.model.GenerationHistory

data class HistoryUiState(
    val histories: List<GenerationHistory> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null,
    val shareRequest: GenerationHistory? = null
)
