package com.bonustrack02.domain.scheduler

import com.bonustrack02.domain.model.AlarmTime

interface AlarmScheduler {
    fun schedule(alarmTime: AlarmTime)
    fun cancel()
}