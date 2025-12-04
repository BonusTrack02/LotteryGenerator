package com.bonustrack02.lotterygenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bonustrack02.lotterygenerator.presentation.history.HistoryScreen
import com.bonustrack02.lotterygenerator.presentation.home.HomeViewModel
import com.bonustrack02.lotterygenerator.presentation.home.LotteryBallScreen
import com.bonustrack02.lotterygenerator.presentation.navigation.BottomNavItem
import com.bonustrack02.lotterygenerator.presentation.navigation.bottomNavItems
import com.bonustrack02.lotterygenerator.presentation.settings.SettingsScreen
import com.bonustrack02.lotterygenerator.ui.theme.LotteryBlue
import com.bonustrack02.lotterygenerator.ui.theme.LotteryGeneratorTheme
import com.bonustrack02.lotterygenerator.ui.theme.LotteryGray
import com.bonustrack02.lotterygenerator.ui.theme.LotteryGreen
import com.bonustrack02.lotterygenerator.ui.theme.LotteryRed
import com.bonustrack02.lotterygenerator.ui.theme.LotteryYellow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            LotteryGeneratorTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry = navController.currentBackStackEntryAsState().value
                            val currentRoute = navBackStackEntry?.destination?.route
                            bottomNavItems.forEach { item ->
                                NavigationBarItem(
                                    selected = currentRoute == item.route,
                                    onClick = {
                                        if (currentRoute != item.route) {
                                            navController.navigate(item.route) {
                                                // 스택 쌓임 및 중복 방지
                                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            painter = painterResource(item.icon),
                                            contentDescription = stringResource(item.label)
                                        )
                                    },
                                    label = { Text(stringResource(item.label)) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = BottomNavItem.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(BottomNavItem.Home.route) {
                            val viewModel: HomeViewModel = hiltViewModel()
                            LotteryBallScreen(viewModel = viewModel)
                        }
                        composable(BottomNavItem.History.route) { HistoryScreen() }
                        composable(BottomNavItem.Settings.route) { SettingsScreen() }
                    }
                }
            }
        }
    }
}

@Composable
fun RandomNumberBallsWithButton(
    numbers: List<Int>,
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .height(40.dp)
        ) {
            if (numbers.isNotEmpty()) {
                numbers.forEach { number ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = when (number) {
                                    in 1..10 -> LotteryYellow
                                    in 11..20 -> LotteryBlue
                                    in 21..30 -> LotteryRed
                                    in 31..40 -> LotteryGray
                                    else -> LotteryGreen
                                },
                                shape = CircleShape
                            )
                    ) {
                        Text(
                            text = number.toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                repeat(6) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color.LightGray.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                    )
                }
            }
        }

        Button(
            onClick = onGenerateClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(stringResource(R.string.generate_new_number_set))
        }
    }
}
