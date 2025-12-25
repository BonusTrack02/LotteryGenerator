package com.bonustrack02.lotterygenerator.presentation.settings

import android.graphics.Typeface
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonustrack02.lotterygenerator.R
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var showDeletionConfirmDialog by remember { mutableStateOf(false) }
    val nativeAd by viewModel.nativeAdState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadNativeAd(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { showDeletionConfirmDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete_all_generation_history))
        }
    }

    if (showDeletionConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeletionConfirmDialog = false },
            title = { Text(stringResource(R.string.delete_all_generation_history)) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(stringResource(R.string.delete_all_generation_history_confirm))

                    if (nativeAd != null) {
                        NativeAdViewComposable(nativeAd!!)
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(Color.LightGray.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Ad Loading...", fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllGenerationHistory()
                        showDeletionConfirmDialog = false
                    }
                ) {
                    Text(stringResource(R.string.delete), color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeletionConfirmDialog = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun NativeAdViewComposable(nativeAd: NativeAd) {
    AndroidView(
        factory = { context ->
            val adView = NativeAdView(context)

            val container = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(24, 24, 24, 24)
                setBackgroundColor(android.graphics.Color.parseColor("#F5F5F5"))
            }

            val headerContainer = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
            }

            val adBadge = TextView(context).apply {
                text = "Ad"
                textSize = 10f
                setTextColor(android.graphics.Color.WHITE)
                setBackgroundColor(android.graphics.Color.parseColor("#FFCC00"))
                setPadding(8, 2, 8, 2)
            }

            val headlineView = TextView(context).apply {
                textSize = 16f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(android.graphics.Color.BLACK)
                layoutParams = LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
                ).apply { marginStart = 16 }
                maxLines = 1
                ellipsize = TextUtils.TruncateAt.END
            }

            headerContainer.addView(adBadge)
            headerContainer.addView(headlineView)

            val bodyView = TextView(context).apply {
                textSize = 14f
                setTextColor(android.graphics.Color.DKGRAY)
                maxLines = 2
                ellipsize = TextUtils.TruncateAt.END
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply { topMargin = 16 }
            }

            val ctaView = Button(context).apply {
                textSize = 14f
                setTextColor(android.graphics.Color.WHITE)
                setBackgroundColor(android.graphics.Color.parseColor("#4285F4"))
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    100
                ).apply { topMargin = 24 }
            }

            container.addView(headerContainer)
            container.addView(bodyView)
            container.addView(ctaView)

            adView.addView(container)

            adView.headlineView = headlineView
            adView.bodyView = bodyView
            adView.callToActionView = ctaView

            adView
        },
        update = { adView ->
            (adView.headlineView as TextView).text = nativeAd.headline
            (adView.bodyView as TextView).text = nativeAd.body
            (adView.callToActionView as Button).text = nativeAd.callToAction

            adView.setNativeAd(nativeAd)
        }
    )
}