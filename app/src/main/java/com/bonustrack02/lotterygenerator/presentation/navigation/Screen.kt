package com.bonustrack02.lotterygenerator.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen(val route: String) {
    object Home: Screen(BottomNavItem.Home.route)
    object History: Screen(BottomNavItem.History.route)
    object Settings: Screen(BottomNavItem.Settings.route)

    object WebView: Screen("web_view")
}

@Serializable
data class WebViewRoute(
    val historyId: Int? = null
)