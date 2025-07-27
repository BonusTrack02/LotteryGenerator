package com.bonustrack02.lotterygenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bonustrack02.lotterygenerator.presentation.navigation.BottomNavItem
import com.bonustrack02.lotterygenerator.presentation.navigation.bottomNavItems
import com.bonustrack02.lotterygenerator.ui.theme.*

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
                        composable(BottomNavItem.Home.route) { LotteryBallScreen(Modifier) }
                        composable(BottomNavItem.History.route) { HistoryScreen() }
                        composable(BottomNavItem.Settings.route) { SettingsScreen() }
                    }
                }
            }
        }
    }
}

@Composable
fun LotteryBallScreen(modifier: Modifier = Modifier) {
    val viewModel: MainViewModel = viewModel()
    val numbers = viewModel.lotteryNumbers.collectAsState().value

    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center,
        ) {
            RandomNumberBallsWithButton(
                numbers = numbers,
                onGenerateClick = { viewModel.generateNewNumbers() }
            )
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

@Composable
fun HistoryScreen() { }

@Composable
fun SettingsScreen() { }