package com.ddn.waypilot.data

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.ddn.waypilot.data.Destination
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

@ProvidedTypeConverter
class DestinationListConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromList(value: List<Destination>?): String? =
        value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toList(value: String?): List<Destination> =
        value?.let { json.decodeFromString(it) } ?: emptyList()
}
