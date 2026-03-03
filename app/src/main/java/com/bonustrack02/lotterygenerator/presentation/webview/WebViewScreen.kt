package com.bonustrack02.lotterygenerator.presentation.webview

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WebViewScreen(
    viewModel: WebViewViewModel = hiltViewModel(),
    modifier: Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (uiState.url.isNotEmpty()) {
            AndroidView(
                modifier = modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        with(settings) {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                        }
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                viewModel.onLoadingFinished()
                            }
                        }
                        loadUrl(uiState.url)
                    }
                }
            )
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}