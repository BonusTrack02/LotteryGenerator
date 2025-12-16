package com.bonustrack02.lotterygenerator.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.bonustrack02.lotterygenerator.R

sealed class BottomNavItem(@StringRes val label: Int, val route: String, @DrawableRes val icon: Int) {
    data object Home : BottomNavItem(R.string.bottom_nav_home, "home", R.drawable.home_24dp_fill0_wght400_grad0_opsz24)
    data object History : BottomNavItem(R.string.bottom_nav_history, "history", R.drawable.history_24dp_fill0_wght400_grad0_opsz24)
    data object Settings : BottomNavItem(R.string.bottom_nav_settings, "settings", R.drawable.settings_24dp_fill0_wght400_grad0_opsz24)
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.History,
    BottomNavItem.Settings
)