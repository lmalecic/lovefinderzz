package com.lmalecic.lovefinderzz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lmalecic.lovefinderzz.database.RickAndMortyDatabase
import com.lmalecic.lovefinderzz.entity.CharacterEntity
import com.lmalecic.lovefinderzz.entity.EpisodeEntity
import kotlinx.coroutines.flow.Flow

class EpisodesViewModel(application: Application) : AndroidViewModel(application) {

    private val episodeDao = RickAndMortyDatabase
        .getInstance(application)
        .episodeDao()

    val episodes: Flow<List<EpisodeEntity>> = episodeDao.observeAll()
}