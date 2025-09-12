package com.ddn.waypilot.data

import java.time.LocalDateTime

data class RestaurantReservation(
    val name: String,
    val time: LocalDateTime?,
    val address: String? = null,
    val confirmationCode: String? = null
)