package com.ddn.waypilot

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection
import com.ddn.waypilot.data.Trip
import com.ddn.waypilot.data.TripStyle
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d")
val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")

fun Trip.datesShort(): String =
    "${startDate.format(dateFormatter)} – ${endDate.format(dateFormatter)}"

fun daysBetween(start: LocalDate, end: LocalDate): Long =
    ChronoUnit.DAYS.between(start, end).coerceAtLeast(0)

fun Trip.durationDays(): Long = daysBetween(startDate, endDate) + 1

fun Trip.progressRatio(today: LocalDate = LocalDate.now()): Float {
    val total = durationDays().coerceAtLeast(1)
    val elapsed = (daysBetween(startDate, today) + 1).coerceAtMost(total)
    return (elapsed.toFloat() / total.toFloat()).coerceIn(0f, 1f)
}

fun TripStyle.toChipText(): String = when (this) {
    TripStyle.SOLO -> "Solo"
    TripStyle.COUPLE -> "Couple"
    TripStyle.FAMILY -> "Family"
    TripStyle.BUSINESS -> "Business"
    TripStyle.BACKPACKER -> "Backpacker"
}

operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    return PaddingValues(
        start = this.calculateStartPadding(LayoutDirection.Ltr) + other.calculateStartPadding(LayoutDirection.Ltr),
        top = this.calculateTopPadding() + other.calculateTopPadding(),
        end = this.calculateEndPadding(LayoutDirection.Ltr) + other.calculateEndPadding(LayoutDirection.Ltr),
        bottom = this.calculateBottomPadding() + other.calculateBottomPadding()
    )
}