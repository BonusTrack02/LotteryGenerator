package com.bonustrack02.lotterygenerator.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bonustrack02.domain.usecase.RescheduleAlarmUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    @Inject lateinit var rescheduleAlarmUseCase: RescheduleAlarmUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(javaClass.simpleName, "Boot completed. Reschedule alarm.")

            val pendingResult = goAsync()

            val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
            scope.launch {
                try {
                    rescheduleAlarmUseCase()
                } catch (e: Exception) {
                    Log.e("BootReceiver", "Error rescheduling alarm", e)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}