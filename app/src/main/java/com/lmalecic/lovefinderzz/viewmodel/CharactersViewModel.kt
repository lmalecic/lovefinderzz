package com.lmalecic.lovefinderzz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lmalecic.lovefinderzz.database.RickAndMortyDatabase
import com.lmalecic.lovefinderzz.entity.CharacterEntity
import com.lmalecic.lovefinderzz.handler.ImageGallerySaver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CharactersViewModel(application: Application) : AndroidViewModel(application) {

    private val characterDao = RickAndMortyDatabase
        .getInstance(application)
        .characterDao()

    val characters: Flow<List<CharacterEntity>> = characterDao.observeAll()

    fun observeDetails(characterId: Long) = characterDao.observeDetails(characterId)
    fun setFavorite(characterId: Long, favorite: Boolean) {
        viewModelScope.launch {
            characterDao.setFavorite(characterId, favorite)
        }
    }

    fun saveImageToGallery(imageUrl: String, characterName: String) {
        viewModelScope.launch {
            runCatching {
                ImageGallerySaver.save(
                    context = getApplication(),
                    imageUrl = imageUrl,
                    baseFileName = characterName
                )
            }.onSuccess { uri ->
                // TODO: Emit a Snackbar event containing uri
            }.onFailure { error ->
                // TODO: Emit a failure Snackbar event
            }
        }
    }
}