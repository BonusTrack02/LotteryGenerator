package com.bonustrack02.lotterygenerator.presentation.splash

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _event = Channel<SplashEvent>()
    val event = _event.receiveAsFlow()

    fun checkPermissions() {
        viewModelScope.launch {
            delay(timeMillis = SPLASH_MIN_DURATION_MILLIS)
            if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                _event.send(SplashEvent.RequestNotificationPermission)
            } else {
                _event.send(SplashEvent.NavigateToMain)
            }
        }
    }

    private companion object {
        const val SPLASH_MIN_DURATION_MILLIS = 1_000L
    }
}

sealed class SplashEvent {
    object RequestNotificationPermission : SplashEvent()
    object NavigateToMain : SplashEvent()
}
