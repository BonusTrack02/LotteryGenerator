package com.bonustrack02.lotterygenerator.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonustrack02.domain.usecase.ClearGenerationHistoryTableUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val clearGenerationHistoryTableUseCase: ClearGenerationHistoryTableUseCase
) : ViewModel() {
    fun clearAllGenerationHistory() {
        viewModelScope.launch {
            clearGenerationHistoryTableUseCase()
        }
    }
}