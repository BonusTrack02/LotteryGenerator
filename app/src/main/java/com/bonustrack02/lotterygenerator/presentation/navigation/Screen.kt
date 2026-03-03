package com.bonustrack02.lotterygenerator.presentation.navigation

sealed class Screen(val route: String) {
    object Home: Screen(BottomNavItem.Home.route)
    object History: Screen(BottomNavItem.History.route)
    object Settings: Screen(BottomNavItem.Settings.route)

    object WebView: Screen("web_view")
}