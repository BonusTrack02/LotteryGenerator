package com.bonustrack02.lotterygenerator.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bonustrack02.lotterygenerator.MainViewModel
import com.bonustrack02.lotterygenerator.RandomNumberBallsWithButton

@Composable
fun LotteryBallScreen(viewModel: HomeViewModel, modifier: Modifier = Modifier) {
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