package com.bonustrack02.domain.repository

import com.bonustrack02.domain.model.AlarmTime

interface AlarmRepository {
    suspend fun setDailyAlarm(time: AlarmTime)
    suspend fun cancelAlarm()
    suspend fun getSavedAlarmTime(): AlarmTime?
}