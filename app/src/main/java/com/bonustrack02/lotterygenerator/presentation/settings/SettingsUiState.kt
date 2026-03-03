package com.bonustrack02.lotterygenerator.presentation.settings

import com.bonustrack02.domain.model.AlarmTime
import com.google.android.gms.ads.nativead.NativeAd

data class SettingsUiState(
    val nativeAd: NativeAd? = null,
    val alarmState: AlarmTime? = null,
    val isLoading: Boolean = false
)
