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

    fun setFavorite(episodeId: Long, favorite: Boolean) {
        viewModelScope.launch {
            episodeDao.setFavorite(episodeId, favorite)
        }
    }

    fun setRating(episodeId: Long, rating: Float) {
        require(rating in 0.5f..5f) {
            "Episode rating must be a float in between 0.5 and 5!"
        }

        viewModelScope.launch {
            episodeDao.setRating(episodeId, rating)
        }
    }

    fun clearRating(episodeId: Long) {
        viewModelScope.launch {
            episodeDao.clearRating(episodeId)
        }
    }
}