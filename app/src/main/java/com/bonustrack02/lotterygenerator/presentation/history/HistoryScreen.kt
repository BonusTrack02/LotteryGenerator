package com.bonustrack02.lotterygenerator.presentation.history

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bonustrack02.domain.model.GenerationHistory
import com.bonustrack02.domain.model.SortType
import com.bonustrack02.lotterygenerator.LotteryBall
import com.bonustrack02.lotterygenerator.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val generationHistories by viewModel.generationHistories.collectAsStateWithLifecycle()
    val currentSortType by viewModel.sortType.collectAsStateWithLifecycle()
    var previousSortType by remember { mutableStateOf(currentSortType) }
    val listState = rememberLazyListState()

    LaunchedEffect(generationHistories) {
        if (previousSortType != currentSortType) {
            listState.scrollToItem(0)
            previousSortType = currentSortType
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = currentSortType == SortType.NEWEST,
                onClick = { viewModel.updateSortType(SortType.NEWEST) },
                label = { Text(stringResource(R.string.sort_newest)) },
                leadingIcon = {
                    if (currentSortType == SortType.NEWEST) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            FilterChip(
                selected = currentSortType == SortType.OLDEST,
                onClick = { viewModel.updateSortType(SortType.OLDEST) },
                label = { Text(stringResource(R.string.sort_oldest)) },
                leadingIcon = {
                    if (currentSortType == SortType.OLDEST) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            )
        }

        LazyColumn(
            state = listState
        ) {
            items(
                items = generationHistories,
                key = { it.id }
            ) { history ->
                GenerationHistoryItem(
                    history = history,
                    onLongClick = { id ->
                        viewModel.deleteGenerationHistory(id)
                    },
                    modifier = Modifier.animateItem(
                        fadeOutSpec = tween(durationMillis = 150),
                        placementSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                )
            }
        }
    }
}

@Composable
fun GenerationHistoryItem(
    history: GenerationHistory,
    onLongClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current
    val cardShape = RoundedCornerShape(12.dp)
    Box(modifier = modifier) {
        Card(
            shape = cardShape,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .clip(cardShape)
                .combinedClickable(
                    onClick = {},
                    onLongClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        isMenuExpanded = true
                    }
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = history.generationTimestamp.toFormattedDateString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                FlowRow (
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    history.numbers.forEach { number ->
                        LotteryBall(number = number)
                    }
                }
            }
        }

        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
            offset = DpOffset(x = 16.dp, y = 0.dp)
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete)) },
                onClick = {
                    isMenuExpanded = false
                    onLongClick(history.id)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete),
                    )
                }
            )
        }
    }
}

fun Long.toFormattedDateString(): String {
    val instant = Instant.ofEpochSecond(this)
    val zoneId = ZoneId.systemDefault()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return instant.atZone(zoneId).format(formatter)
}