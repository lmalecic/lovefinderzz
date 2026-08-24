package com.lmalecic.lovefinderzz.api

import com.lmalecic.lovefinderzz.entity.CharacterEntity
import com.lmalecic.lovefinderzz.entity.CharacterEpisodeCrossReference
import com.lmalecic.lovefinderzz.entity.CharacterStatus
import com.lmalecic.lovefinderzz.entity.EpisodeEntity
import com.lmalecic.lovefinderzz.entity.Gender
import com.lmalecic.lovefinderzz.entity.LocationEntity
import com.lmalecic.lovefinderzz.framework.parseUsLongDate

private fun CharacterLocationItem.extractId(): Long? =
    url.substringAfterLast('/').toLongOrNull()

private fun String.toCharacterStatus(): CharacterStatus =
    when (lowercase()) {
        "alive" -> CharacterStatus.ALIVE
        "dead" -> CharacterStatus.DEAD
        else -> CharacterStatus.UNKNOWN
    }

private fun String.toGender(): Gender =
    when (lowercase()) {
        "male" -> Gender.MALE
        "female" -> Gender.FEMALE
        "genderless" -> Gender.GENDERLESS
        else -> Gender.UNKNOWN
    }

fun CharacterItem.toEntity() = CharacterEntity(
    id = id,
    name = name,
    status = status.toCharacterStatus(),
    species = species,
    type = type.trim().takeIf { it.isNotEmpty() },
    gender = gender.toGender(),
    originLocationId = origin.extractId(),
    currentLocationId = location.extractId(),
    imageUrl = imageUrl
)

fun LocationItem.toEntity() = LocationEntity(
    id = id,
    name = name,
    type = type,
    dimension = dimension
)

fun EpisodeItem.toEntity() = EpisodeEntity(
    id = id,
    name = name,
    airDate = airDate.parseUsLongDate(),
    episode = episode
)

fun CharacterItem.toEpisodeReferences() =
    episodes.mapNotNull { episodeUrl ->
        episodeUrl.substringAfterLast('/').toLongOrNull()?.let { episodeId ->
            CharacterEpisodeCrossReference(
                characterId = id,
                episodeId = episodeId
            )
        }
    }