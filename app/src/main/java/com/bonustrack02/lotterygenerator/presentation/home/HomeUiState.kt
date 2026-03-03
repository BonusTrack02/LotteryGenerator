package com.bonustrack02.lotterygenerator.presentation.home

data class HomeUiState(
    val lotteryNumbers: List<Int> = emptyList(),
    val isLoading: Boolean = false
)
