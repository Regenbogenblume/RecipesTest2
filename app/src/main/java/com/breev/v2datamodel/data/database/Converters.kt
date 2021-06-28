package com.breev.v2datamodel.data.database

import androidx.room.TypeConverter
import java.util.*
import kotlin.collections.ArrayList


class Converters {

    @TypeConverter
    fun fromStringToList(flatStringList: String): List<String> {
        return flatStringList.split(",")
    }
    @TypeConverter
    fun fromListToString(listOfString: List<String>): String {
        return listOfString.joinToString(",")
    }


    @TypeConverter
    fun fromLongToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun fromDateToLong(date: Date?): Long? {
        return date?.time?.toLong()
    }
}

