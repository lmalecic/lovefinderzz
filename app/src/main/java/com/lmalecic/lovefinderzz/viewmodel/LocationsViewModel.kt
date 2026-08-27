package com.lmalecic.lovefinderzz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lmalecic.lovefinderzz.database.RickAndMortyDatabase
import com.lmalecic.lovefinderzz.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

class LocationsViewModel(application: Application) : AndroidViewModel(application) {

    private val locationDao = RickAndMortyDatabase
        .getInstance(application)
        .locationDao()

    val locations: Flow<List<LocationEntity>> = locationDao.observeAll()

    fun observeDetails(locationId: Long) = locationDao.observeDetails(locationId)
}