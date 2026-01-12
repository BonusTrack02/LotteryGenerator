package com.bonustrack02.lotterygenerator.presentation.settings

import android.graphics.Typeface
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonustrack02.lotterygenerator.R
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    var showDeletionConfirmDialog by remember { mutableStateOf(false) }
    val nativeAd by viewModel.nativeAdState.collectAsState()

    val alarmState by viewModel.alarmState.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val isAlarmOn = alarmState != null

    LaunchedEffect(Unit) {
        viewModel.loadNativeAd(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                    alpha = 0.5f
                )
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.settings_notification_use),
                            fontSize = with(density) { 16.dp.toSp() },
                        )
                        if (isAlarmOn) {
                            Text(
                                text = stringResource(R.string.settings_notification_description),
                                fontSize = with(density) { 12.dp.toSp() },
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = isAlarmOn,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                viewModel.setAlarm(9)
                            } else {
                                viewModel.cancelAlarm()
                            }
                        }
                    )
                }

                if (isAlarmOn) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showBottomSheet = true },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.notification_time),
                            fontSize = with(density) { 16.dp.toSp() },
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.notification_time_format,
                                    alarmState!!.hour
                                ),
                                fontSize = with(density) { 16.dp.toSp() },
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

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
                            Text("Ad Loading...", fontSize = with(density) { 14.dp.toSp() })
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

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            HourPickerBottomSheetContent(
                initialHour = alarmState?.hour ?: 9,
                onConfirm = { selectedHour ->
                    viewModel.setAlarm(selectedHour)
                    showBottomSheet = false
                }
            )
        }
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

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun HourPickerBottomSheetContent(
    initialHour: Int = 0,
    onConfirm: (Int) -> Unit = {}
) {
    val density = LocalDensity.current
    val hours = (0..23).toList()
    val pagerState = rememberPagerState(initialPage = initialHour) { hours.size }

    val pickerHeight = 150.dp
    val itemHeight = 50.dp

    val verticalPadding = (pickerHeight - itemHeight) / 2

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.select_notification_time),
            fontSize = with(density) { 16.dp.toSp() },
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Box(
            modifier = Modifier
                .height(pickerHeight)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(itemHeight)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            )

            VerticalPager(
                state = pagerState,
                contentPadding = PaddingValues(vertical = verticalPadding),
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val hour = hours[page]

                val pageOffset =
                    ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                val scale = lerp(1.2f, 0.8f, pageOffset.coerceIn(0f, 1f))
                val alpha = lerp(1f, 0.3f, pageOffset.coerceIn(0f, 1f))
                val color = if (pageOffset < 0.5f) MaterialTheme.colorScheme.primary else Color.Gray

                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.notification_time_format, hour),
                        fontSize = with(LocalDensity.current) { 18.dp.toSp() },
                        fontWeight = FontWeight.Bold,
                        color = color,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onConfirm(pagerState.currentPage) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(stringResource(R.string.save), fontSize = with(density) { 16.dp.toSp() })
        }
    }
}