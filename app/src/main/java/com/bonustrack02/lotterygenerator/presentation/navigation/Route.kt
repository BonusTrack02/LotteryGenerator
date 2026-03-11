package com.bonustrack02.lotterygenerator.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute
@Serializable
data object HistoryRoute
@Serializable
data object SettingsRoute

@Serializable
data class WebViewRoute(
    val historyId: Int? = null
)