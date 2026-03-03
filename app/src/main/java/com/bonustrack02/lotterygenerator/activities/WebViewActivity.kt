package com.bonustrack02.lotterygenerator.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import com.bonustrack02.lotterygenerator.presentation.webview.WebViewScreen
import com.bonustrack02.lotterygenerator.ui.theme.LotteryGeneratorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LotteryGeneratorTheme {
                WebViewScreen(
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
    }
}