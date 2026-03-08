package com.bonustrack02.domain.usecase

import javax.inject.Inject

class GetPurchaseUrlUseCase @Inject constructor() {
    operator fun invoke(): String {
        return "https://m.dhlottery.co.kr/"
    }
}