package com.bonustrack02.lotterygenerator.presentation.history

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonustrack02.domain.model.SortType
import com.bonustrack02.domain.usecase.DeleteGenerationHistoryUseCase
import com.bonustrack02.domain.usecase.GetAllGenerationHistoryUseCase
import com.bonustrack02.domain.usecase.SaveLotteryTicketImageUseCase
import com.bonustrack02.domain.usecase.ShareLotteryTicketImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAllGenerationHistoryUseCase: GetAllGenerationHistoryUseCase,
    private val deleteGenerationHistoryUseCase: DeleteGenerationHistoryUseCase,
    private val saveLotteryTicketImageUseCase: SaveLotteryTicketImageUseCase,
    private val shareLotteryTicketImageUseCase: ShareLotteryTicketImageUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadHistories()
    }

    fun deleteGenerationHistory(id: Int) {
        viewModelScope.launch {
            deleteGenerationHistoryUseCase(id)
        }
    }

    private fun loadHistories() {
        viewModelScope.launch {
            getAllGenerationHistoryUseCase().collect { originalList ->
                _uiState.update { currentState ->
                    val sortedList = when (currentState.sortType) {
                        SortType.NEWEST -> originalList.sortedByDescending { it.id }
                        SortType.OLDEST -> originalList.sortedBy { it.id }
                    }
                    currentState.copy(histories = sortedList)
                }
            }
        }
    }

    fun updateSortType(sortType: SortType) {
        _uiState.update { currentState ->
            val sortedList = when (sortType) {
                SortType.NEWEST -> currentState.histories.sortedByDescending { it.id }
                SortType.OLDEST -> currentState.histories.sortedBy { it.id }
            }
            currentState.copy(
                sortType = sortType,
                histories = sortedList
            )
        }
    }

    fun onShareClick(historyId: Int) {
        viewModelScope.launch {
            val history = _uiState.value.histories.find { it.id == historyId }

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