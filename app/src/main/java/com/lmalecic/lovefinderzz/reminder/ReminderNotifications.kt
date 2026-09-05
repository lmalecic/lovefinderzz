package com.lmalecic.lovefinderzz.reminder

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.lmalecic.lovefinderzz.MainActivity
import com.lmalecic.lovefinderzz.R
import com.lmalecic.lovefinderzz.entity.ReminderDetails
import com.lmalecic.lovefinderzz.handler.loadCachedImageBitmap

object ReminderNotifications {

    const val CHANNEL_ID = "character_reminders"

    fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Character reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Reminders about Rick and Morty characters"
        }

        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    suspend fun show(context: Context, details: ReminderDetails): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            && ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        createChannel(context)

        val contentIntent = PendingIntent.getActivity(
            context,
            details.reminder.id.hashCode(),
            Intent(context, MainActivity::class.java).apply {
                data = "$remindersUri/${details.reminder.id}".toUri()
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val reminderMessage = details.reminder.message?.takeIf { it.isNotBlank() } ?: context.getString(R.string.defaultReminderMessage)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_calendar)
            .setContentTitle(details.character.name)
            .setContentText(reminderMessage)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(reminderMessage))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setWhen(details.reminder.triggerAtEpochMillis)
            .setShowWhen(true)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .apply { context.loadCachedImageBitmap(details.character.imageUrl)?.let(::setLargeIcon) }
            .build()

        NotificationManagerCompat.from(context)
            .notify(details.reminder.id.hashCode(), notification)

        return true
    }
}