package com.bonustrack02.lotterygenerator.presentation.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonustrack02.domain.usecase.ClearGenerationHistoryTableUseCase
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
    private val clearGenerationHistoryTableUseCase: ClearGenerationHistoryTableUseCase
) : ViewModel() {
    private val _nativeAdState = MutableStateFlow<NativeAd?>(null)
    val nativeAdState = _nativeAdState.asStateFlow()

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
}