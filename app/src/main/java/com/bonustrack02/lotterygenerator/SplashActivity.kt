package com.bonustrack02.lotterygenerator

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bonustrack02.lotterygenerator.ui.theme.LotteryGeneratorTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            LotteryGeneratorTheme {
                SplashScreen()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun SplashScreen() {
        val alpha = remember { Animatable(0f) }
        LaunchedEffect(Unit) {
            alpha.animateTo(targetValue = 1f, animationSpec = tween(500))
            delay(1000)

            Intent(applicationContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }.run {
                startActivity(this)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val painter = painterResource(R.mipmap.icon_app_foreground)
            Image(
                modifier = Modifier
                    .width(180.dp)
                    .height(180.dp)
                    .alpha(alpha.value),
                painter = painter,
                contentDescription = stringResource(R.string.app_icon)
            )
        }
    }
}