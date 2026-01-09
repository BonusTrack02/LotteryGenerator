package com.bonustrack02.lotterygenerator

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonustrack02.lotterygenerator.presentation.splash.SplashEvent
import com.bonustrack02.lotterygenerator.presentation.splash.SplashViewModel
import com.bonustrack02.lotterygenerator.ui.theme.LotteryGeneratorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
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

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    @Preview(showBackground = true)
    @Composable
    fun SplashScreen(
        viewModel: SplashViewModel = hiltViewModel(),
        onNavigateToMain: () -> Unit = { navigateToMain() }
    ) {
        val context = LocalContext.current

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    onNavigateToMain()
                } else {
                    Toast.makeText(
                        context,
                        "알림 권한을 거부하여 알람이 울리지 않을 수 있습니다.",
                        Toast.LENGTH_LONG
                    ).show()

                    onNavigateToMain()
                }
            }
        )

        LaunchedEffect(viewModel.event) {
            viewModel.event.collectLatest { event ->
                when (event) {
                    is SplashEvent.RequestNotificationPermission -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                    is SplashEvent.NavigateToMain -> {
                        onNavigateToMain()
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val painter = painterResource(R.mipmap.icon_app_foreground)

            Image(
                modifier = Modifier
                    .width(180.dp)
                    .height(180.dp),
                painter = painter,
                contentDescription = stringResource(R.string.app_name)
            )
        }
    }
}