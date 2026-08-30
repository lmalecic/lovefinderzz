package com.lmalecic.lovefinderzz.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lmalecic.lovefinderzz.database.RickAndMortyDatabase
import com.lmalecic.lovefinderzz.entity.CharacterEntity
import com.lmalecic.lovefinderzz.handler.ImageGallerySaver
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CharactersViewModel(application: Application) : AndroidViewModel(application) {

    private val characterDao = RickAndMortyDatabase
        .getInstance(application)
        .characterDao()

    val characters: Flow<List<CharacterEntity>> = characterDao.observeAll()

    private val _imageSaveEvents = Channel<ImageSaveEvent>(Channel.BUFFERED)
    val imageSaveEvents = _imageSaveEvents.receiveAsFlow()

    fun observeDetails(characterId: Long) = characterDao.observeDetails(characterId)
    fun setFavorite(characterId: Long, favorite: Boolean) {
        viewModelScope.launch {
            characterDao.setFavorite(characterId, favorite)
        }
    }

    fun saveImageToGallery(imageUrl: String, characterName: String) {
        viewModelScope.launch {
            val event = try {
                val uri = ImageGallerySaver.save(
                    context = getApplication(),
                    imageUrl = imageUrl,
                    baseFileName = characterName
                )

                ImageSaveEvent.Saved(uri)
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (error: Exception) {
                ImageSaveEvent.Failed(error.message)
            }

            _imageSaveEvents.send(event)
        }
    }
}

sealed interface ImageSaveEvent {
    data class Saved(val uri: Uri) : ImageSaveEvent
    data class Failed(val reason: String?) : ImageSaveEvent
}