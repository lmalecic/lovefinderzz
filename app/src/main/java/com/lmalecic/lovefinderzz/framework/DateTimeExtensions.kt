package com.lmalecic.lovefinderzz.framework

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

fun LocalDate.toEpochMillis(time: LocalTime, zoneId: ZoneId = ZoneId.systemDefault()): Long =
    atTime(time.withSecond(0).withNano(0))
        .atZone(zoneId)
        .toInstant()
        .toEpochMilli()

fun Long.datePickerMillisToLocalDate(): LocalDate =
    Instant.ofEpochMilli(this)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()

fun LocalDate.toDatePickerMillis(): Long =
    atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()

fun Long.toZonedDateTime(zoneId: ZoneId = ZoneId.systemDefault()) =
    Instant.ofEpochMilli(this)
        .atZone(zoneId)