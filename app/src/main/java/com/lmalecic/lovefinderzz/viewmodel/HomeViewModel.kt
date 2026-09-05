package com.lmalecic.lovefinderzz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lmalecic.lovefinderzz.database.RickAndMortyDatabase

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val reminderDao = RickAndMortyDatabase.getInstance(application)
        .reminderDao()

    fun observeUpcomingReminderDetails() = reminderDao.observeUpcomingDetails(now = System.currentTimeMillis())
}