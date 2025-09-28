package com.ddn.waypilot.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class TripWithRelations(
    @Embedded val trip: TripEntity,
    @Relation(parentColumn = "id", entityColumn = "tripId")
    val flights: List<FlightSegmentEntity>,
    @Relation(parentColumn = "id", entityColumn = "tripId")
    val hotels: List<HotelBookingEntity>,
    @Relation(parentColumn = "id", entityColumn = "tripId")
    val activities: List<ActivityEntity>,
    @Relation(parentColumn = "id", entityColumn = "tripId")
    val restaurants: List<RestaurantEntity>
)
