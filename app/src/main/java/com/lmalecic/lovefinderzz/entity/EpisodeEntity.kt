package com.lmalecic.lovefinderzz.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate
import java.util.Date

@Entity(
    tableName = "episodes",
)
data class EpisodeEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val airDate: LocalDate,
    val episode: String
)

data class EpisodeDetails(
    @Embedded
    val episode: EpisodeEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CharacterEpisodeCrossReference::class,
            parentColumn = "episodeId",
            entityColumn = "characterId"
        )
    )
    val characters: List<CharacterEntity>
)
