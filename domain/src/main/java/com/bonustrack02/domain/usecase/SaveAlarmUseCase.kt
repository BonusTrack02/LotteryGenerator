package com.bonustrack02.domain.usecase

import com.bonustrack02.domain.model.AlarmTime
import com.bonustrack02.domain.repository.AlarmRepository
import com.bonustrack02.domain.scheduler.AlarmScheduler
import javax.inject.Inject

class SaveAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke(hour: Int) {
        val alarmTime = AlarmTime(hour)

        repository.updateAlarm(alarmTime)

        alarmScheduler.schedule(alarmTime)
    }
}