package com.bonustrack02.lotterygenerator.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bonustrack02.lotterygenerator.BuildConfig
import com.bonustrack02.lotterygenerator.R
import com.bonustrack02.lotterygenerator.presentation.history.HistoryScreen
import com.bonustrack02.lotterygenerator.presentation.home.HomeScreen
import com.bonustrack02.lotterygenerator.presentation.navigation.BottomNavItem
import com.bonustrack02.lotterygenerator.presentation.navigation.Route
import com.bonustrack02.lotterygenerator.presentation.settings.SettingsScreen
import com.bonustrack02.lotterygenerator.ui.components.AdmobBanner
import com.bonustrack02.lotterygenerator.ui.theme.LotteryGeneratorTheme
import com.google.android.gms.ads.AdSize
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            LotteryGeneratorTheme {
                val navController = rememberNavController()
                val navBackStackEntry = navController.currentBackStackEntryAsState().value
                val currentDestination = navBackStackEntry?.destination
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = when {
                                        currentDestination?.hasRoute(Route.Home::class) == true -> stringResource(
                                            R.string.bottom_nav_home
                                        )

                                        currentDestination?.hasRoute(Route.History::class) == true -> stringResource(
                                            R.string.bottom_nav_history
                                        )

                                        currentDestination?.hasRoute(Route.Settings::class) == true -> stringResource(
                                            R.string.bottom_nav_settings
                                        )
                                        else -> ""
                                    }
                                )
                            }
                        )
                    },
                    bottomBar = {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AdmobBanner(
                                adSize = AdSize.BANNER,
                                adId = BuildConfig.admobBannerId
                            )
                            HorizontalDivider(
                                color = Color.Gray,
                                thickness = 1.dp
                            )
                            NavigationBar {
                                val screens = listOf(
                                    BottomNavItem.Home,
                                    BottomNavItem.History,
                                    BottomNavItem.Settings
                                )
                                screens.forEach { screen ->
                                    val isSelected = currentDestination?.hierarchy?.any {
                                        it.hasRoute(screen.route::class)
                                    } == true
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                painter = painterResource(screen.icon),
                                                contentDescription = stringResource(screen.label)
                                            )
                                        },
                                        label = {
                                            Text(stringResource(screen.label))
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Route.Home,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Route.Home> { HomeScreen() }
                        composable<Route.History> { HistoryScreen(
                            onNavigateToPurchase = { historyId ->
                                navController.navigate(Route.WebView(historyId))
                            }
                        ) }
                        composable<Route.Settings> { SettingsScreen() }
                        activity<Route.WebView> {
                            activityClass = WebViewActivity::class
                        }
                    }
                }
            }
        }
    }
}