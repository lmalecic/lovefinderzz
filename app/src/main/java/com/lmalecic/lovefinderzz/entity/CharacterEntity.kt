package com.lmalecic.lovefinderzz.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

enum class CharacterStatus {
    ALIVE, DEAD, UNKNOWN
}

enum class Gender {
    MALE, FEMALE, GENDERLESS, UNKNOWN;
}

@Entity(
    tableName = "characters",
    foreignKeys = [
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["id"],
            childColumns = ["originLocationId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["id"],
            childColumns = ["currentLocationId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("originLocationId"),
        Index("currentLocationId")
    ]
)
data class CharacterEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String? = null,
    val gender: Gender,
    val originLocationId: Long? = null,
    val currentLocationId: Long? = null,
    val imageUrl: String
)

data class CharacterDetails(
    @Embedded
    val character: CharacterEntity,

    @Relation(parentColumn = "originLocationId", entityColumn = "id")
    val origin: LocationEntity?,

    @Relation(parentColumn = "currentLocationId", entityColumn = "id")
    val location: LocationEntity?,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CharacterEpisodeCrossReference::class,
            parentColumn = "characterId",
            entityColumn = "episodeId"
        )
    )
    val episodes: List<EpisodeEntity>
)
