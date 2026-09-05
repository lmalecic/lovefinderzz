package com.lmalecic.lovefinderzz.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lmalecic.lovefinderzz.database.RickAndMortyDatabase
import com.lmalecic.lovefinderzz.entity.ReminderEntity
import com.lmalecic.lovefinderzz.entity.ReminderMode
import com.lmalecic.lovefinderzz.framework.toEpochMillis
import com.lmalecic.lovefinderzz.reminder.ReminderScheduler
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

sealed interface ReminderEvent {
    data class Created(val reminder: ReminderEntity) : ReminderEvent
    data object ExactAlarmAccessRequired : ReminderEvent
    data class Failed(val reason: String?) : ReminderEvent
}

class ReminderViewModel(application: Application) : AndroidViewModel(application) {

    private val reminderDao = RickAndMortyDatabase.getInstance(application).reminderDao()
    private val scheduler = ReminderScheduler(application)
    private val _events = Channel<ReminderEvent>(Channel.BUFFERED)

    val events = _events.receiveAsFlow()

    fun canScheduleExactAlarms(): Boolean = scheduler.canScheduleExactAlarms()
    fun exactAlarmAccessIntent(): Intent? = scheduler.exactAlarmAccessIntent()

    fun scheduleNotification(
        characterId: Long,
        date: LocalDate,
        time: LocalTime,
        message: String?
    ) {
        val triggerAt = date.toEpochMillis(time)

        if (triggerAt <= System.currentTimeMillis()) {
            viewModelScope.launch {
                _events.send(ReminderEvent.Failed("Reminder must be in the future"))
            }
            return
        }

        if (!scheduler.canScheduleExactAlarms()) {
            viewModelScope.launch {
                _events.send(ReminderEvent.ExactAlarmAccessRequired)
            }
            return
        }

        viewModelScope.launch {
            var savedReminder: ReminderEntity? = null

            try {
                val unsavedReminder = ReminderEntity(
                    characterId = characterId,
                    triggerAtEpochMillis = triggerAt,
                    message = message?.trim()
                        ?.takeIf { it.isNotEmpty() },
                    mode = ReminderMode.NOTIFICATION
                )

                val generatedId = reminderDao.insert(unsavedReminder)

                savedReminder = unsavedReminder.copy(id = generatedId)

                scheduler.scheduleNotification(savedReminder)
                _events.send(ReminderEvent.Created(savedReminder))
            } catch (cancellation: CancellationException) {
                savedReminder?.let {
                    scheduler.cancel(it)
                    reminderDao.delete(it)
                }

                throw cancellation
            } catch (error: Exception) {
                savedReminder?.let {
                    scheduler.cancel(it)
                    reminderDao.delete(it)
                }

                _events.send(ReminderEvent.Failed(error.message))
            }
        }
    }
}