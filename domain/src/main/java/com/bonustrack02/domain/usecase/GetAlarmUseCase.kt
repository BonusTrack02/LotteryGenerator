package com.bonustrack02.domain.usecase

import com.bonustrack02.domain.model.AlarmTime
import com.bonustrack02.domain.repository.AlarmRepository
import javax.inject.Inject

class GetAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(): AlarmTime? {
        return repository.getSavedAlarmTime()
    }
}