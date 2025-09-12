package com.ddn.waypilot.data

import java.time.LocalDate

data class Trip(
    val id: String,
    val title: String,                 // e.g. "Trip to Paris"
    val destinationCity: String,       // "Paris"
    val country: String,               // "France"
    val startDate: LocalDate,
    val endDate: LocalDate,
    val travelersCount: Int = 1,
    val style: TripStyle = TripStyle.SOLO,
    val coverImageUrl: String? = null,
    val budget: Budget? = null,
    val flights: List<FlightSegment> = emptyList(),
    val hotels: List<HotelBooking> = emptyList(),
    val activities: List<ActivityItem> = emptyList(),
    val restaurants: List<RestaurantReservation> = emptyList(),
    val notes: String? = null
)