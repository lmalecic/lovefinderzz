package com.lmalecic.lovefinderzz.reminder

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.core.net.toUri
import com.lmalecic.lovefinderzz.entity.ReminderEntity
import com.lmalecic.lovefinderzz.entity.ReminderMode

const val remindersUri = "lovefinderzz://reminders"

class ReminderScheduler(context: Context) {

    private val appContext = context.applicationContext
    private val alarmManager = appContext.getSystemService(AlarmManager::class.java)

    fun canScheduleExactAlarms(): Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()

    fun exactAlarmAccessIntent(): Intent? =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S)
            null
        else
            Intent(
                Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                "package:${appContext.packageName}".toUri()
            )

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    fun scheduleNotification(reminder: ReminderEntity) {
        require(reminder.id > 0) {
            "Reminder must be inserted before scheduling"
        }

        require(reminder.mode == ReminderMode.NOTIFICATION) {
            "Alarm mode will be scheduled by the alarm-mode implementation"
        }

        check(canScheduleExactAlarms()) {
            "Exact alarm access has not been granted"
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            reminder.triggerAtEpochMillis,
            operation(reminder.id)
        )
    }

    fun cancel(reminder: ReminderEntity) {
        alarmManager.cancel(operation(reminder.id))
    }

    private fun operation(reminderId: Long): PendingIntent {
        val intent = Intent(
            appContext,
            ReminderReceiver::class.java
        ).apply {
            data = "$remindersUri/$reminderId".toUri()

            putExtra(
                ReminderReceiver.EXTRA_REMINDER_ID,
                reminderId
            )
        }

        return PendingIntent.getBroadcast(
            appContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }


}