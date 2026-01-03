package com.bonustrack02.domain.usecase

import com.bonustrack02.domain.model.AlarmData
import com.bonustrack02.domain.repository.AlarmRepository
import javax.inject.Inject

class SetAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(hour: Int) {
        val alarmData = AlarmData(hour)

        repository.setDailyAlarm(alarmData)
    }
}