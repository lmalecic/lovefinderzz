package com.lmalecic.lovefinderzz.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Upsert
import com.lmalecic.lovefinderzz.entity.CharacterEntity
import com.lmalecic.lovefinderzz.entity.CharacterEpisodeCrossReference
import com.lmalecic.lovefinderzz.entity.EpisodeEntity
import com.lmalecic.lovefinderzz.entity.LocationEntity

@Dao
interface SyncDao {

    @Upsert
    suspend fun upsertCharacters(characters: List<CharacterEntity>)

    @Upsert
    suspend fun upsertLocations(locations: List<LocationEntity>)

    @Upsert
    suspend fun upsertEpisodes(episodes: List<EpisodeEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEpisodeReferences(references: List<CharacterEpisodeCrossReference>)
}