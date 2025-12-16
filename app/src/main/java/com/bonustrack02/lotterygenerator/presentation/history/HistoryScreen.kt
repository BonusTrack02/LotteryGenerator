package com.bonustrack02.lotterygenerator.presentation.history

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val generationHistories by viewModel.generationHistories.collectAsStateWithLifecycle()

    LazyColumn {
        items(
            items = generationHistories,
            key = { it.id }
        ) { history ->
            Text(text = history.generationTimestamp.toString())
        }
    }
}