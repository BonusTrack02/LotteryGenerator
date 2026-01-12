package com.bonustrack02.domain.usecase

import com.bonustrack02.domain.repository.AlarmRepository
import javax.inject.Inject

class CancelAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke() {
        repository.updateAlarm(null)
    }
}