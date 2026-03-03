package com.bonustrack02.lotterygenerator.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonustrack02.domain.usecase.GenerateLotteryNumbersUseCase
import com.bonustrack02.domain.usecase.SaveGenerationHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val generateLotteryNumbers: GenerateLotteryNumbersUseCase,
    private val saveGenerationHistory: SaveGenerationHistoryUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun generateNewNumbers() {
        viewModelScope.launch {
            val history = generateLotteryNumbers()
            saveGenerationHistory(history)

            _uiState.update { currentState ->
                currentState.copy(
                    lotteryNumbers = history.numbers,
                    isLoading = false
                )
            }
        }
    }
}