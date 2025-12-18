package com.bonustrack02.lotterygenerator.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonustrack02.domain.model.GenerationHistory
import com.bonustrack02.domain.usecase.DeleteGenerationHistoryUseCase
import com.bonustrack02.domain.usecase.GetGenerationHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    getGenerationHistoryUseCase: GetGenerationHistoryUseCase,
    private val deleteGenerationHistoryUseCase: DeleteGenerationHistoryUseCase
) : ViewModel() {
    val generationHistories: StateFlow<List<GenerationHistory>> = getGenerationHistoryUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun deleteGenerationHistory(id: Int) {
        viewModelScope.launch {
            deleteGenerationHistoryUseCase(id)
        }
    }
}