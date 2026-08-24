package com.lmalecic.lovefinderzz.database

import androidx.room.TypeConverter
import java.time.LocalDate

class RoomConverters {

    @TypeConverter
    fun localDateToString(date: LocalDate?): String? =
        date?.toString()

    @TypeConverter
    fun stringToLocalDate(value: String?): LocalDate? =
        value?.let(LocalDate::parse)
}