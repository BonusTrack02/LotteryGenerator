package com.bonustrack02.lotterygenerator.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdmobBanner(
    modifier: Modifier = Modifier,
    adSize: AdSize,
    adId: String
) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(adSize)

                adUnitId = adId

                loadAd(AdRequest.Builder().build())
            }
        }
    )
}