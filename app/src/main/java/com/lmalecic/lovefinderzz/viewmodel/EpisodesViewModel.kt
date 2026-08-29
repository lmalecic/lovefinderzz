package com.lmalecic.lovefinderzz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lmalecic.lovefinderzz.database.RickAndMortyDatabase
import com.lmalecic.lovefinderzz.entity.EpisodeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EpisodesViewModel(application: Application) : AndroidViewModel(application) {

    private val episodeDao = RickAndMortyDatabase
        .getInstance(application)
        .episodeDao()

    val episodes: Flow<List<EpisodeEntity>> = episodeDao.observeAll()

    fun observeDetails(episodeId: Long) = episodeDao.observeDetails(episodeId)

    fun setFavorite(locationId: Long, favorite: Boolean) {
        viewModelScope.launch {
            episodeDao.setFavorite(locationId, favorite)
        }
    }
}