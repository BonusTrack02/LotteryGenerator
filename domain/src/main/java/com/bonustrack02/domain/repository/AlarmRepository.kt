package com.bonustrack02.domain.repository

import com.bonustrack02.domain.model.AlarmTime

interface AlarmRepository {
    suspend fun updateAlarm(time: AlarmTime?)
    suspend fun getSavedAlarmTime(): AlarmTime?
}