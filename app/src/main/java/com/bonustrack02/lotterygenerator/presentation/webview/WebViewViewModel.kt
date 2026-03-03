package com.bonustrack02.lotterygenerator.presentation.webview

import androidx.lifecycle.ViewModel
import com.bonustrack02.domain.usecase.GetPurchaseUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val getPurchaseUrlUseCase: GetPurchaseUrlUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(WebViewUiState())
    val uiState: StateFlow<WebViewUiState> = _uiState.asStateFlow()

    init {
        val url = getPurchaseUrlUseCase()
        _uiState.update { it.copy(url = url) }
    }

    fun onLoadingFinished() {
        _uiState.update { it.copy(isLoading = false) }
    }
}