package com.bonustrack02.lotterygenerator.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.bonustrack02.lotterygenerator.BuildConfig
import com.bonustrack02.lotterygenerator.presentation.webview.WebViewScreen
import com.bonustrack02.lotterygenerator.ui.theme.LotteryGeneratorTheme
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LotteryGeneratorTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    bottomBar = {
                        AdmobWebViewBanner()
                    }
                ) { innerPadding ->
                    WebViewScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AdmobWebViewBanner(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.FULL_BANNER)

                adUnitId = BuildConfig.admobWebViewBannerId

                loadAd(AdRequest.Builder().build())
            }
        }
    )
}