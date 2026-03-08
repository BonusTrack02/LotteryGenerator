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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val clearGenerationHistoryTableUseCase: ClearGenerationHistoryTableUseCase,
    private val getAlarmUseCase: GetAlarmUseCase,
    private val saveAlarmUseCase: SaveAlarmUseCase,
    private val cancelAlarmUseCase: CancelAlarmUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchAlarmStatus()
    }

    fun loadNativeAd(context: Context) {
        val adLoader = AdLoader.Builder(context, BuildConfig.admobNativeId).forNativeAd { ad ->
            viewModelScope.launch {
                _uiState.update { it.copy(nativeAd = ad) }
            }
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
            val savedAlarm = getAlarmUseCase()
            _uiState.update { it.copy(alarmState = savedAlarm) }
        }
    }

    fun setAlarm(hour: Int) {
        viewModelScope.launch {
            val newTime = AlarmTime(hour)
            saveAlarmUseCase(hour)
            _uiState.update { it.copy(alarmState = newTime) }
        }
    }

    fun cancelAlarm() {
        viewModelScope.launch {
            cancelAlarmUseCase()
            _uiState.update { it.copy(alarmState = null) }
        }
    }
}