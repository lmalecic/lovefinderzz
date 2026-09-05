package com.lmalecic.lovefinderzz.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

enum class ReminderMode {
    NOTIFICATION, ALARM
}

@Entity(
    tableName = "reminders",
    foreignKeys = [
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["id"],
            childColumns = ["characterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [ Index("characterId") ]
)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val characterId: Long,
    val triggerAtEpochMillis: Long,
    val message: String?,
    val mode: ReminderMode,
    val soundUri: String? = null,
    val enabled: Boolean = true,
    val deliveredAtEpochMillis: Long? = null
)

data class ReminderDetails(
    @Embedded
    val reminder: ReminderEntity,

    @Relation(parentColumn = "characterId", entityColumn = "id")
    val character: CharacterEntity
)