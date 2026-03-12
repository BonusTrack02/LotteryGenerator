package com.bonustrack02.lotterygenerator.presentation.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed interface Route {
    @Serializable
    data object Home : Route
    @Serializable
    data object History : Route
    @Serializable
    data object Settings : Route
    @Serializable
    data class WebView(
        val historyId: Int? = null
    ) : Route
}