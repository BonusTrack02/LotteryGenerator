package com.bonustrack02.lotterygenerator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _lotteryNumbers = MutableStateFlow<List<Int>>(emptyList())
    val lotteryNumbers: StateFlow<List<Int>> = _lotteryNumbers.asStateFlow()

    fun generateNewNumbers() {
        _lotteryNumbers.value = generateRandomNumbers()
    }

    private fun generateRandomNumbers(): List<Int> {
        return (1..45).shuffled().take(6).sorted()
    }
}