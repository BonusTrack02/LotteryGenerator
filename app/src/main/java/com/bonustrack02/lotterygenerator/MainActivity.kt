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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bonustrack02.lotterygenerator.ui.theme.LotteryBlue
import com.bonustrack02.lotterygenerator.ui.theme.LotteryGeneratorTheme
import com.bonustrack02.lotterygenerator.ui.theme.LotteryGray
import com.bonustrack02.lotterygenerator.ui.theme.LotteryGreen
import com.bonustrack02.lotterygenerator.ui.theme.LotteryRed
import com.bonustrack02.lotterygenerator.ui.theme.LotteryYellow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LotteryGeneratorTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            LotteryBallScreen(Modifier.padding(innerPadding))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LotteryBallScreen(modifier: Modifier) {
    val viewModel: MainViewModel = viewModel()
    val numbers = viewModel.lotteryNumbers.collectAsState().value

    RandomNumberBallsWithButton(
        numbers = numbers,
        onGenerateClick = { viewModel.generateNewNumbers() },
        modifier = modifier
    )
}

@Composable
fun RandomNumberBallsWithButton(
    numbers: List<Int>,
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
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
        }

        Button(
            onClick = onGenerateClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(stringResource(R.string.generate_new_number_set))
        }
    }
}
