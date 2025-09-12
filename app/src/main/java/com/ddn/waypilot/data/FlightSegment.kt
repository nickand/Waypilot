package com.ddn.waypilot.data

import java.time.LocalDateTime

data class FlightSegment(
    val originCode: String,           // "SFO"
    val destinationCode: String,      // "CDG"
    val departure: LocalDateTime,
    val arrival: LocalDateTime,
    val airline: String? = null,
    val flightNumber: String? = null,
    val terminal: String? = null,
    val gate: String? = null
)
