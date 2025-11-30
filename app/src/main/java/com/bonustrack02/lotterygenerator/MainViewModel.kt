package com.bonustrack02.lotterygenerator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonustrack02.domain.usecase.FetchGenerationHistoryUseCase
import com.bonustrack02.domain.usecase.GenerateLotteryNumbersUseCase
import com.bonustrack02.domain.usecase.SaveGenerationHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val generateLotteryNumbers: GenerateLotteryNumbersUseCase,
    private val saveGenerationHistory: SaveGenerationHistoryUseCase,
    private val fetchGenerationHistory: FetchGenerationHistoryUseCase,
) : ViewModel() {
    private val _lotteryNumbers = MutableStateFlow<List<Int>>(emptyList())
    val lotteryNumbers: StateFlow<List<Int>> = _lotteryNumbers.asStateFlow()

    fun generateNewNumbers() {
        viewModelScope.launch {
            val history = generateLotteryNumbers()
            saveGenerationHistory(history)
            _lotteryNumbers.value = history.numbers
        }
    }
}
