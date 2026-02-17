package com.bonustrack02.domain.usecase

import com.bonustrack02.domain.repository.ShareRepository
import javax.inject.Inject

class ShareLotteryTicketImageUseCase @Inject constructor(
    private val repository: ShareRepository
) {
    suspend operator fun invoke(uriString: String) {
        repository.shareImage(uriString)
    }
}