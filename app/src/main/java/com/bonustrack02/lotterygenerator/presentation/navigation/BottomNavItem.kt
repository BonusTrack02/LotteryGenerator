package com.bonustrack02.lotterygenerator.presentation.navigation

import androidx.annotation.StringRes
import com.bonustrack02.lotterygenerator.R

sealed class BottomNavItem(@StringRes val label: Int, val route: String) {
    data object Main : BottomNavItem(R.string.bottom_nav_main, "main")
    data object History : BottomNavItem(R.string.bottom_nav_history, "history")
    data object Settings : BottomNavItem(R.string.bottom_nav_settings, "settings")
}

val bottomNavItems = listOf(
    BottomNavItem.Main,
    BottomNavItem.History,
    BottomNavItem.Settings
)