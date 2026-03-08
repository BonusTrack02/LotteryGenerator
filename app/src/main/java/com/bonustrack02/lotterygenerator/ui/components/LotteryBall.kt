package com.bonustrack02.lotterygenerator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bonustrack02.lotterygenerator.ui.theme.LotteryBlue
import com.bonustrack02.lotterygenerator.ui.theme.LotteryGray
import com.bonustrack02.lotterygenerator.ui.theme.LotteryGreen
import com.bonustrack02.lotterygenerator.ui.theme.LotteryRed
import com.bonustrack02.lotterygenerator.ui.theme.LotteryYellow

@Composable
fun LotteryBall(
    number: Int,
    modifier: Modifier = Modifier
) {
    val ballColor = when (number) {
        in 1..10 -> LotteryYellow
        in 11..20 -> LotteryBlue
        in 21..30 -> LotteryRed
        in 31..40 -> LotteryGray
        else -> LotteryGreen
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(40.dp)
            .background(color = ballColor, shape = CircleShape)
    ) {
        Text(
            text = number.toString(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun EmptyLotteryBall(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(40.dp) // LotteryBall과 동일한 크기 유지
            .background(
                color = Color.LightGray.copy(alpha = 0.3f),
                shape = CircleShape
            )
    )
}