package com.bonustrack02.domain.usecase

import com.bonustrack02.domain.repository.AlarmRepository
import com.bonustrack02.domain.scheduler.AlarmScheduler
import javax.inject.Inject

class RescheduleAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke() {
        val savedAlarmTime = alarmRepository.getSavedAlarmTime()

        if (savedAlarmTime != null) {
            alarmScheduler.schedule(savedAlarmTime)
        }
    }
}