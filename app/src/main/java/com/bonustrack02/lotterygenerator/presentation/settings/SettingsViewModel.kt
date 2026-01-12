package com.bonustrack02.lotterygenerator.presentation.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonustrack02.domain.model.AlarmTime
import com.bonustrack02.domain.usecase.CancelAlarmUseCase
import com.bonustrack02.domain.usecase.ClearGenerationHistoryTableUseCase
import com.bonustrack02.domain.usecase.GetAlarmUseCase
import com.bonustrack02.domain.usecase.SaveAlarmUseCase
import com.bonustrack02.lotterygenerator.BuildConfig
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val clearGenerationHistoryTableUseCase: ClearGenerationHistoryTableUseCase,
    private val getAlarmUseCase: GetAlarmUseCase,
    private val saveAlarmUseCase: SaveAlarmUseCase,
    private val cancelAlarmUseCase: CancelAlarmUseCase,
) : ViewModel() {
    private val _nativeAdState = MutableStateFlow<NativeAd?>(null)
    val nativeAdState = _nativeAdState.asStateFlow()

    private val _alarmState = MutableStateFlow<AlarmTime?>(null)
    val alarmState = _alarmState.asStateFlow()

    init {
        fetchAlarmStatus()
    }

    fun loadNativeAd(context: Context) {
        val adLoader = AdLoader.Builder(context, BuildConfig.admobNativeId).forNativeAd { ad ->
            viewModelScope.launch { _nativeAdState.value = ad }
        }.build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun clearAllGenerationHistory() {
        viewModelScope.launch {
            clearGenerationHistoryTableUseCase()
        }
    }

    private fun fetchAlarmStatus() {
        viewModelScope.launch {
            _alarmState.value = getAlarmUseCase()
        }
    }

    fun setAlarm(hour: Int) {
        viewModelScope.launch {
            val newTime = AlarmTime(hour)
            saveAlarmUseCase(hour)
            _alarmState.value = newTime
        }
    }

    fun cancelAlarm() {
        viewModelScope.launch {
            cancelAlarmUseCase()
            _alarmState.value = null
        }
    }
}