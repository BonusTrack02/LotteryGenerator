package com.bonustrack02.lotterygenerator.presentation.history

import com.bonustrack02.domain.model.GenerationHistory
import com.bonustrack02.domain.model.SortType

data class HistoryUiState(
    val histories: List<GenerationHistory> = emptyList(),
    val sortType: SortType = SortType.NEWEST,
    val isLoading: Boolean = false,
    val message: String? = null,
    val shareRequest: GenerationHistory? = null
)
