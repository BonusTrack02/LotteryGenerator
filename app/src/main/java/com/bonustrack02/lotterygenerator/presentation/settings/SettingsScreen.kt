package com.bonustrack02.lotterygenerator.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonustrack02.lotterygenerator.R

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    var showDeletionConfirmDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { showDeletionConfirmDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete_all_generation_history))
        }
    }

    if (showDeletionConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeletionConfirmDialog = false },
            title = { Text(stringResource(R.string.delete_all_generation_history)) },
            text = { Text(stringResource(R.string.delete_all_generation_history_confirm)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllGenerationHistory()
                        showDeletionConfirmDialog = false
                    }
                ) {
                    Text(stringResource(R.string.delete), color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeletionConfirmDialog = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}