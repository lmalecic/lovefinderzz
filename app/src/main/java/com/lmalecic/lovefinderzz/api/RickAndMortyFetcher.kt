package com.lmalecic.lovefinderzz.api

import androidx.room.withTransaction
import com.lmalecic.lovefinderzz.database.RickAndMortyDatabase
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class RickAndMortyFetcher(
    private val api: RickAndMortyApi,
    private val database: RickAndMortyDatabase
) {
    private val syncDao = database.syncDao()

    suspend fun fetchAll() {
        val locations = fetchAllPages(api::fetchLocations)
        val episodes = fetchAllPages(api::fetchEpisodes)
        val characters = fetchAllPages(api::fetchCharacters)

        val locationEntities = locations.map(LocationItem::toEntity)
        val episodeEntities = episodes.map(EpisodeItem::toEntity)
        val characterEntities = characters.map(CharacterItem::toEntity)

        val references = characters.flatMap(CharacterItem::toEpisodeReferences)

        database.withTransaction {
            syncDao.upsertLocations(locationEntities)
            syncDao.upsertEpisodes(episodeEntities)
            syncDao.upsertCharacters(characterEntities)
            syncDao.insertEpisodeReferences(references)
        }
    }

    private suspend fun <T> fetchAllPages(fetchPage: suspend (Int) -> ApiResponse<T>): List<T> {
        val results = mutableListOf<T>()
        var page = 1

        while (true) {
            val response = fetchPage(page)
            results += response.results

            delay(500.milliseconds)

            if (response.info.next == null) {
                return results
            }

            page++
        }
    }
}