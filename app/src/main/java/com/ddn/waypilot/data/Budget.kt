package com.ddn.waypilot.data

data class Budget(
    val currencyCode: String,         // e.g. "USD", "EUR"
    val estimatedTotal: Double? = null,
    val min: Double? = null,
    val max: Double? = null
)