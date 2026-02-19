package com.bonustrack02.lotterygenerator.presentation.history

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonustrack02.domain.model.GenerationHistory
import com.bonustrack02.domain.model.SortType
import com.bonustrack02.domain.usecase.DeleteGenerationHistoryUseCase
import com.bonustrack02.domain.usecase.GetGenerationHistoryUseCase
import com.bonustrack02.domain.usecase.SaveLotteryTicketImageUseCase
import com.bonustrack02.domain.usecase.ShareLotteryTicketImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getGenerationHistoryUseCase: GetGenerationHistoryUseCase,
    private val deleteGenerationHistoryUseCase: DeleteGenerationHistoryUseCase,
    private val saveLotteryTicketImageUseCase: SaveLotteryTicketImageUseCase,
    private val shareLotteryTicketImageUseCase: ShareLotteryTicketImageUseCase
) : ViewModel() {
    private val _sortType = MutableStateFlow(SortType.NEWEST)
    val sortType = _sortType.asStateFlow()

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

//    init {
//        loadHistories()
//    }

    val generationHistories: StateFlow<List<GenerationHistory>> = getGenerationHistoryUseCase().combine(_sortType) { list, sortType ->
        when (sortType) {
            SortType.NEWEST -> list.sortedByDescending { it.id }
            SortType.OLDEST -> list.sortedBy { it.id }
        }
    }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun updateSortType(sortType: SortType) {
        _sortType.value = sortType
    }

    fun deleteGenerationHistory(id: Int) {
        viewModelScope.launch {
            deleteGenerationHistoryUseCase(id)
        }
    }

    private fun loadHistories() {
        viewModelScope.launch {
            getGenerationHistoryUseCase().collect { list ->
                _uiState.update { it.copy(histories = list) }
            }
        }
    }

    fun onShareClick(historyId: Int) {
        viewModelScope.launch {
            val history = generationHistories.value.find { it.id == historyId }

            if (history != null) {
                _uiState.update { it.copy(shareRequest = history) }
            } else {
                showErrorMessage("에러 발생")
            }
        }
    }

    fun processAndShareBitmap(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                stream.close()

                val fileName = "${Instant.now().epochSecond}_ticket"
                val uriString = saveLotteryTicketImageUseCase(fileName, byteArray)

                shareLotteryTicketImageUseCase(uriString)

            } catch (e: Exception) {
                e.printStackTrace()
                showErrorMessage("공유 중 오류가 발생했습니다.")
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun showErrorMessage(message: String) {
        _uiState.update { it.copy(message = message) }
    }

    fun onErrorMessageShown() {
        _uiState.update { it.copy(message = null) }
    }

    fun onShareRequestConsumed() {
        _uiState.update { it.copy(shareRequest = null) }
    }
}