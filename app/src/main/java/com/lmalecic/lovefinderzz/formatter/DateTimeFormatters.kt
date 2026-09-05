package com.lmalecic.lovefinderzz.formatter

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.Locale

val US_LONG_DATE: DateTimeFormatter = DateTimeFormatterBuilder()
    .parseCaseInsensitive()
    .appendPattern("MMMM d, uuuu")
    .toFormatter(Locale.US)

fun LocalDate.toLocalizedString(formatStyle: FormatStyle): String = this.format(
    DateTimeFormatter.ofLocalizedDate(formatStyle)
        .withLocale(Locale.getDefault()))

fun LocalTime.toLocalizedString(formatStyle: FormatStyle): String = this.format(
    DateTimeFormatter.ofLocalizedTime(formatStyle)
        .withLocale(Locale.getDefault())
)