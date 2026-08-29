package com.lmalecic.lovefinderzz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lmalecic.lovefinderzz.database.RickAndMortyDatabase
import com.lmalecic.lovefinderzz.entity.LocationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LocationsViewModel(application: Application) : AndroidViewModel(application) {

    private val locationDao = RickAndMortyDatabase
        .getInstance(application)
        .locationDao()

    val locations: Flow<List<LocationEntity>> = locationDao.observeAll()

    fun observeDetails(locationId: Long) = locationDao.observeDetails(locationId)
    fun setFavorite(locationId: Long, favorite: Boolean) {
        viewModelScope.launch {
            locationDao.setFavorite(locationId, favorite)
        }
    }
}