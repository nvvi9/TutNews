package com.nvvi9.tutnews.db

import androidx.room.TypeConverter
import java.util.*

/**
 * Room database converters
 */

class Converters {

    @TypeConverter
    fun toDate(value: Long): Date = Date(value)

    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun toStringList(value: String): List<String> =
        value.split(", ")

    @TypeConverter
    fun fromStringList(list: List<String>): String =
        list.joinToString()
}