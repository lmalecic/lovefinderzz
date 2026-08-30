package com.lmalecic.lovefinderzz.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate

@Entity(
    tableName = "episodes",
)
data class EpisodeEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val airDate: LocalDate,
    val episode: String,
    @ColumnInfo(defaultValue = "0")
    val favorite: Boolean = false,
    val rating: Float? = null
) {
    init {
        require(rating?.let { it in 0.5f..5f } != false) {
            "Episode rating must be between 0.5 and 5.0"
        }

        require(rating?.let { (rating * 2f) % 1f == 0.0f } != false) {
            "Episode rating must be a multiple of 0.5"
        }
    }
}

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
