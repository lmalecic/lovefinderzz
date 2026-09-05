package com.lmalecic.lovefinderzz.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lmalecic.lovefinderzz.database.RickAndMortyDatabase
import com.lmalecic.lovefinderzz.entity.ReminderMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra(EXTRA_REMINDER_ID, INVALID_REMINDER_ID)

        if (reminderId == INVALID_REMINDER_ID) {
            return
        }

        val pendingResult = goAsync()

        CoroutineScope(Job() + Dispatchers.IO).launch {
            try {
                val reminderDao = RickAndMortyDatabase.getInstance(context).reminderDao()
                val details = reminderDao.getDetailsById(reminderId)

                if (details != null && details.reminder.enabled && details.reminder.mode == ReminderMode.NOTIFICATION) {
                    ReminderNotifications.show(context = context, details = details)
                    reminderDao.markAsDelivered(id = reminderId, deliveredAt = System.currentTimeMillis())
                }
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        const val EXTRA_REMINDER_ID = "com.lmalecic.lovefinderzz.REMINDER_ID"
        private const val INVALID_REMINDER_ID = -1L
    }
}