package com.bonustrack02.domain.usecase

import com.bonustrack02.domain.repository.ShareRepository
import javax.inject.Inject

class SaveLotteryTicketImageUseCase @Inject constructor(
    private val repository: ShareRepository
) {
    suspend operator fun invoke(fileName: String, imageBytes: ByteArray): String {
        return repository.saveImage(fileName, imageBytes)
    }
}