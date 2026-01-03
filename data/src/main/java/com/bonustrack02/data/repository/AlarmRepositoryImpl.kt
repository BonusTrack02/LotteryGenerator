package com.bonustrack02.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.bonustrack02.domain.model.AlarmData
import com.bonustrack02.domain.repository.AlarmRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AlarmRepository {
    companion object {
        private const val ALARM_ACTION = "com.bonustrack02.lottergenerator.ACTION_DAILY_ALARM"
        private const val ALARM_REQUEST_CODE = 1
    }
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override suspend fun setDailyAlarm(time: AlarmData) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, time.hour)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)

            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val intent = Intent(ALARM_ACTION).apply {
            `package` = context.packageName
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    override suspend fun cancelAlarm() {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedAlarmTime(): AlarmData? {
        TODO("Not yet implemented")
    }
}