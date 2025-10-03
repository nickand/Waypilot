package com.ddn.waypilot.data

data class Destination(
    val placeId: String,
    val name: String,
    val address: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val notes: String? = null
)