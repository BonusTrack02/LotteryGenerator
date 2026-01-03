package com.bonustrack02.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.bonustrack02.domain.model.AlarmTime
import com.bonustrack02.domain.repository.AlarmRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.util.Calendar
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val datastore: DataStore<Preferences>
) : AlarmRepository {
    companion object {
        private val KEY_HOUR = intPreferencesKey("alarm_hour")
        private const val ALARM_ACTION = "com.bonustrack02.lottergenerator.ACTION_DAILY_ALARM"
        private const val ALARM_REQUEST_CODE = 1
    }
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override suspend fun updateAlarm(time: AlarmTime?) {
        if (time != null) {
            datastore.edit { prefs ->
                prefs[KEY_HOUR] = time.hour
            }
            registerAlarm(time.hour)
        } else {
            cancelAlarm()
        }
    }

    override suspend fun getSavedAlarmTime(): AlarmTime? {
        val prefs = datastore.data.first()
        val hour = prefs[KEY_HOUR] ?: return null
        return AlarmTime(hour)
    }

    private fun registerAlarm(hour: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val intent = Intent(ALARM_ACTION).apply {
            setPackage(context.packageName)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun cancelAlarm() {
        val intent = Intent(ALARM_ACTION).apply {
            `package` = context.packageName
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}