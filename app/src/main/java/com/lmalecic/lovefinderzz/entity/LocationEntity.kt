package com.lmalecic.lovefinderzz.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "locations",
    indices = [
        Index("type"),
        Index("dimension")
    ]
)
data class LocationEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val type: String,
    val dimension: String
)

data class LocationDetails(
    @Embedded
    val location: LocationEntity,

    @Relation(parentColumn = "id", entityColumn = "currentLocationId")
    val residents: List<CharacterEntity>
)