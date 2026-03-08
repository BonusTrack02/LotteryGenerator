package com.bonustrack02.lotterygenerator.presentation.webview

data class WebViewUiState(
    val url: String = "",
    val selectedNumbers: List<Int>? = null,
    val isLoading: Boolean = false
)
