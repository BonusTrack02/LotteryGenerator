package com.bonustrack02.lotterygenerator.presentation.splash

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

@Composable
fun AppOpenAdEffect(
    activity: Activity,
    adUnitId: String,
    onComplete: () -> Unit
) {
    val latestOnComplete by rememberUpdatedState(onComplete)

    LaunchedEffect(activity, adUnitId) {
        if (adUnitId.isBlank()) {
            latestOnComplete()
            return@LaunchedEffect
        }

        val appOpenAd = withTimeoutOrNull(timeMillis = AD_LOAD_TIMEOUT_MILLIS) {
            loadAppOpenAd(activity, adUnitId)
        }

        if (appOpenAd != null && !activity.isFinishing && !activity.isDestroyed) {
            showAppOpenAd(activity, appOpenAd)
        }

        latestOnComplete()
    }
}

private suspend fun loadAppOpenAd(activity: Activity, adUnitId: String): AppOpenAd? =
    suspendCancellableCoroutine { continuation ->
        AppOpenAd.load(
            activity.applicationContext,
            adUnitId,
            AdRequest.Builder().build(),
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(appOpenAd: AppOpenAd) {
                    if (continuation.isActive) {
                        continuation.resume(appOpenAd)
                    }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    if (continuation.isActive) {
                        continuation.resume(null)
                    }
                }
            }
        )
    }

private suspend fun showAppOpenAd(activity: Activity, appOpenAd: AppOpenAd) =
    suspendCancellableCoroutine { continuation ->
        appOpenAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                if (continuation.isActive) {
                    continuation.resume(Unit)
                }
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                if (continuation.isActive) {
                    continuation.resume(Unit)
                }
            }
        }
        appOpenAd.show(activity)
    }

private const val AD_LOAD_TIMEOUT_MILLIS = 4_000L
