package com.ddn.waypilot.data.local.entities

import androidx.room.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey val id: String,
    val title: String,
    val destinationCity: String,
    val country: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val travelersCount: Int,
    val style: String?, // guardar nombre del enum
    val coverImageUrl: String?,
    val currencyCode: String?,        // de Budget
    val estimatedTotal: Double?,      // de Budget
    val notes: String?
)

@Entity(
    tableName = "flight_segments",
    foreignKeys = [ForeignKey(
        entity = TripEntity::class,
        parentColumns = ["id"],
        childColumns = ["tripId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("tripId")]
)
data class FlightSegmentEntity(
    @PrimaryKey(autoGenerate = true) val flightId: Long = 0,
    val tripId: String,
    val originCode: String,
    val destinationCode: String,
    val departure: LocalDateTime,
    val arrival: LocalDateTime,
    val airline: String?,
    val flightNumber: String?
)

@Entity(
    tableName = "hotel_bookings",
    foreignKeys = [ForeignKey(
        entity = TripEntity::class,
        parentColumns = ["id"],
        childColumns = ["tripId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("tripId")]
)
data class HotelBookingEntity(
    @PrimaryKey(autoGenerate = true) val hotelId: Long = 0,
    val tripId: String,
    val name: String,
    val address: String?,
    val checkIn: LocalDateTime,
    val checkOut: LocalDateTime,
    val confirmationCode: String?
)

@Entity(
    tableName = "activities",
    foreignKeys = [ForeignKey(
        entity = TripEntity::class,
        parentColumns = ["id"],
        childColumns = ["tripId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("tripId")]
)
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val activityId: Long = 0,
    val tripId: String,
    val title: String,
    val start: LocalDateTime?,
    val end: LocalDateTime?,
    val location: String?,
    val category: String? // nombre del enum
)

@Entity(
    tableName = "restaurants",
    foreignKeys = [ForeignKey(
        entity = TripEntity::class,
        parentColumns = ["id"],
        childColumns = ["tripId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("tripId")]
)
data class RestaurantEntity(
    @PrimaryKey(autoGenerate = true) val restaurantId: Long = 0,
    val tripId: String,
    val name: String,
    val time: LocalDateTime?,
    val address: String?,
    val confirmationCode: String?
)
