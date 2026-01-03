package com.bonustrack02.domain.repository

import com.bonustrack02.domain.model.AlarmData

interface AlarmRepository {
    suspend fun setDailyAlarm(time: AlarmData)
    suspend fun cancelAlarm()
    suspend fun getSavedAlarmTime(): AlarmData?
}