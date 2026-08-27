package com.lmalecic.lovefinderzz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lmalecic.lovefinderzz.database.RickAndMortyDatabase
import com.lmalecic.lovefinderzz.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

class CharactersViewModel(application: Application) : AndroidViewModel(application) {

    private val characterDao = RickAndMortyDatabase
        .getInstance(application)
        .characterDao()

    val characters: Flow<List<CharacterEntity>> = characterDao.observeAll()

    fun observeDetails(characterId: Long) = characterDao.observeDetails(characterId)
}