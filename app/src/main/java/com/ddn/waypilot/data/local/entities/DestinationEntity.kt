package com.ddn.waypilot.data.local.entities

import androidx.room.*

@Entity(
    tableName = "trip_destinations",
    foreignKeys = [ForeignKey(
        entity = TripEntity::class,
        parentColumns = ["id"],
        childColumns = ["tripId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("tripId"), Index("orderIndex")]
)
data class DestinationEntity(
    @PrimaryKey(autoGenerate = true) val destinationId: Long = 0,
    val tripId: String,
    val orderIndex: Int,
    val placeId: String,
    val name: String,
    val address: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val notes: String? = null
)
