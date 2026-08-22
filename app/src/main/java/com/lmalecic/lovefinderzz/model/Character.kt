package com.lmalecic.lovefinderzz.model

import java.time.LocalDateTime

sealed class Character(
    val id: Long,
    val name: String,
    val status: CharacterStatus,
    val species: Species,
    val type: CharacterType,
    val gender: Gender,
    val origin: Location,
    val location: Location,
    val imageUrl: String
) {
}