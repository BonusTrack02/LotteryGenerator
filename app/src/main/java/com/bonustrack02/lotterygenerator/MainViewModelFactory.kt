package com.bonustrack02.lotterygenerator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bonustrack02.domain.usecase.GenerateLotteryNumbersUseCase
import com.bonustrack02.domain.usecase.SaveGenerationHistoryUseCase

class MainViewModelFactory(
    private val generateLotteryNumbers: GenerateLotteryNumbersUseCase,
    private val saveGenerationHistory: SaveGenerationHistoryUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(generateLotteryNumbers, saveGenerationHistory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
