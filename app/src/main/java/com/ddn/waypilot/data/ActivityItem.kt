package com.ddn.waypilot.data

import java.time.LocalDateTime

data class ActivityItem(
    val title: String,
    val start: LocalDateTime?,
    val end: LocalDateTime?,
    val location: String? = null,
    val priceEstimate: Double? = null,
    val currencyCode: String? = null,
    val category: ActivityCategory = ActivityCategory.OTHER,
    val notes: String? = null
)
