package com.lmalecic.lovefinderzz.model

import java.time.LocalDateTime

class Location(
    val id: Long,
    val name: String,
    val type: LocationType,
    val dimension: Dimension
) {
}