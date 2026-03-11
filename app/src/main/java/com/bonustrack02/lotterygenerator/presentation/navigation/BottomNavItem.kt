package com.bonustrack02.lotterygenerator.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.bonustrack02.lotterygenerator.R

sealed class BottomNavItem(@StringRes val label: Int, val route: Any, @DrawableRes val icon: Int) {
    data object Home : BottomNavItem(R.string.bottom_nav_home, HomeRoute, R.drawable.home_24dp_fill0_wght400_grad0_opsz24)
    data object History : BottomNavItem(R.string.bottom_nav_history, HistoryRoute, R.drawable.history_24dp_fill0_wght400_grad0_opsz24)
    data object Settings : BottomNavItem(R.string.bottom_nav_settings, SettingsRoute, R.drawable.settings_24dp_fill0_wght400_grad0_opsz24)
}