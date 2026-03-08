package com.bonustrack02.lotterygenerator.presentation.webview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.bonustrack02.domain.usecase.GetGenerationHistoryUseCase
import com.bonustrack02.domain.usecase.GetPurchaseUrlUseCase
import com.bonustrack02.lotterygenerator.presentation.navigation.WebViewRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val getPurchaseUrlUseCase: GetPurchaseUrlUseCase,
    private val getGenerationHistoryUseCase: GetGenerationHistoryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(WebViewUiState())
    val uiState: StateFlow<WebViewUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(url = getPurchaseUrlUseCase()) }

        val route = savedStateHandle.toRoute<WebViewRoute>()
        if (route.historyId != null) {
            viewModelScope.launch {
                getGenerationHistoryUseCase(route.historyId)?.run {
                    _uiState.update { it.copy(selectedNumbers = numbers) }
                }
            }
        }
    }

    fun dismissNumberBanner() {
        _uiState.update { it.copy(selectedNumbers = null) }
    }

    fun onLoadingFinished() {
        _uiState.update { it.copy(isLoading = false) }
    }
}