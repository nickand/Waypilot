package com.ddn.waypilot.data

import java.time.LocalDateTime

data class HotelBooking(
    val name: String,
    val address: String? = null,
    val checkIn: LocalDateTime,
    val checkOut: LocalDateTime,
    val confirmationCode: String? = null
)