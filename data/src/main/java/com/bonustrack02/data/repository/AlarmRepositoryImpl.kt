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
    private val datastore: DataStore<Preferences>
) : AlarmRepository {
    companion object {
        private val KEY_HOUR = intPreferencesKey("alarm_hour")
    }

    override suspend fun updateAlarm(time: AlarmTime?) {
        if (time != null) {
            datastore.edit { prefs ->
                prefs[KEY_HOUR] = time.hour
            }
        } else {
            datastore.edit { prefs ->
                prefs.remove(KEY_HOUR)
            }
        }
    }

    override suspend fun getSavedAlarmTime(): AlarmTime? {
        val prefs = datastore.data.first()
        val hour = prefs[KEY_HOUR] ?: return null
        return AlarmTime(hour)
    }
}