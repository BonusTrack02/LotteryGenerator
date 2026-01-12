package com.bonustrack02.lotterygenerator.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.bonustrack02.domain.repository.AlarmRepository
import com.bonustrack02.domain.usecase.RescheduleAlarmUseCase
import com.bonustrack02.lotterygenerator.MainActivity
import com.bonustrack02.lotterygenerator.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var rescheduleAlarmUseCase: RescheduleAlarmUseCase

    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context)

        rescheduleNextAlarm()
    }

    private fun showNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "scheduled_alarm_channel_id"
        val channelName = context.getString(R.string.scheduled_daily_notification_name)
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description
            enableVibration(true)
        }
        manager.createNotificationChannel(channel)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.icon_app_foreground)
            .setContentTitle(context.getString(R.string.scheduled_daily_notification_name))
            .setContentText(context.getString(R.string.scheduled_daily_notification_content_text))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        manager.notify(220, builder.build())
    }

    private fun rescheduleNextAlarm() {
        val pendingResult = goAsync()
        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

        scope.launch {
            try {
                rescheduleAlarmUseCase()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                pendingResult.finish()
            }
        }
    }
}