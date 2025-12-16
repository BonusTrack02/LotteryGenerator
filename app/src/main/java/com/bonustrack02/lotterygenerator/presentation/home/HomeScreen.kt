package com.bonustrack02.lotterygenerator.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bonustrack02.lotterygenerator.EmptyLotteryBall
import com.bonustrack02.lotterygenerator.LotteryBall
import com.bonustrack02.lotterygenerator.R

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
                    LotteryBall(number)
                }
            } else {
                repeat(6) {
                    EmptyLotteryBall()
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