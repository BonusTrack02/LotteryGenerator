package com.bonustrack02.lotterygenerator.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonustrack02.domain.model.GenerationHistory
import com.bonustrack02.domain.model.SortType
import com.bonustrack02.domain.usecase.DeleteGenerationHistoryUseCase
import com.bonustrack02.domain.usecase.GetGenerationHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    getGenerationHistoryUseCase: GetGenerationHistoryUseCase,
    private val deleteGenerationHistoryUseCase: DeleteGenerationHistoryUseCase
) : ViewModel() {
    private val _sortType = MutableStateFlow(SortType.NEWEST)
    val sortType = _sortType.asStateFlow()

    val generationHistories: StateFlow<List<GenerationHistory>> = getGenerationHistoryUseCase().combine(_sortType) { list, sortType ->
        when (sortType) {
            SortType.NEWEST -> list.sortedByDescending { it.id }
            SortType.OLDEST -> list.sortedBy { it.id }
        }
    }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun updateSortType(sortType: SortType) {
        _sortType.value = sortType
    }

    fun deleteGenerationHistory(id: Int) {
        viewModelScope.launch {
            deleteGenerationHistoryUseCase(id)
        }
    }
}