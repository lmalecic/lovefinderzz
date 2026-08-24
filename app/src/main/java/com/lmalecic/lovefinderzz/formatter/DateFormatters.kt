package com.lmalecic.lovefinderzz.formatter

import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale

val US_LONG_DATE: DateTimeFormatter = DateTimeFormatterBuilder()
    .parseCaseInsensitive()
    .appendPattern("MMMM d, uuuu")
    .toFormatter(Locale.US)