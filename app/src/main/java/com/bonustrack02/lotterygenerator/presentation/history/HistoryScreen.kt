package com.bonustrack02.lotterygenerator.presentation.history

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bonustrack02.domain.model.GenerationHistory
import com.bonustrack02.domain.model.SortType
import com.bonustrack02.lotterygenerator.LotteryBall
import com.bonustrack02.lotterygenerator.R
import com.bonustrack02.lotterygenerator.util.ShareUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val generationHistories by viewModel.generationHistories.collectAsStateWithLifecycle()
    val currentSortType by viewModel.sortType.collectAsStateWithLifecycle()
    var previousSortType by remember { mutableStateOf(currentSortType) }
    val listState = rememberLazyListState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val compositionContext = rememberCompositionContext()

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedHistoryId by remember { mutableStateOf<Int?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(generationHistories) {
        if (previousSortType != currentSortType) {
            listState.scrollToItem(0)
            previousSortType = currentSortType
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                        selectedHistoryId = id
                        showBottomSheet = true
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

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val historyToShare =
                                generationHistories.find { it.id == selectedHistoryId }

                            if (historyToShare != null) {
                                scope.launch {
                                    val bitmap = ShareUtils.captureComposableAsBitmap(
                                        context = context,
                                        compositionContext = compositionContext
                                    ) {
                                        GeneratedTicketImage(
                                            selectedNumbers = historyToShare.numbers,
                                            timestamp = historyToShare.generationTimestamp
                                        )
                                    }

                                    withContext(Dispatchers.IO) {
                                        val uri = ShareUtils.saveBitmapToCache(context, bitmap)
                                        uri?.let { ShareUtils.shareImage(context, it) }
                                    }

                                    showBottomSheet = false
                                }
                            }
                        }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = stringResource(R.string.share_number_set_image),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(R.string.share_number_set_image),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedHistoryId?.let { id ->
                                viewModel.deleteGenerationHistory(id)
                            }
                            showBottomSheet = false
                        }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(R.string.delete),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
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
    val haptics = LocalHapticFeedback.current
    val cardShape = RoundedCornerShape(12.dp)
    val configuration = LocalConfiguration.current
    val isWideScreen = configuration.screenWidthDp > 600
    val ballHorizontalArrangement =
        if (isWideScreen) Arrangement.spacedBy(8.dp) else Arrangement.SpaceAround

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
                        onLongClick(history.id)
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

                FlowRow(
                    horizontalArrangement = ballHorizontalArrangement,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    history.numbers.forEach { number ->
                        LotteryBall(number = number)
                    }
                }
            }
        }
    }
}

fun Long.toFormattedDateString(): String {
    val instant = Instant.ofEpochSecond(this)
    val zoneId = ZoneId.systemDefault()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return instant.atZone(zoneId).format(formatter)
}