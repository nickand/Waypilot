package com.ddn.waypilot

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.ddn.waypilot.data.ActivityCategory
import com.ddn.waypilot.data.ActivityItem
import com.ddn.waypilot.data.Budget
import com.ddn.waypilot.data.FlightSegment
import com.ddn.waypilot.data.HotelBooking
import com.ddn.waypilot.data.RestaurantReservation
import com.ddn.waypilot.data.Trip
import com.ddn.waypilot.data.TripStyle
import com.ddn.waypilot.ui.theme.WaypilotTheme
import com.ddn.waypilot.ui.theme.screens.TripsHome
import java.time.LocalDate
import java.time.LocalDateTime

fun fakeTrip(): Trip {
    val start = LocalDate.now().plusDays(14)
    val end = start.plusDays(5)
    return Trip(
        id = "t-paris-001",
        title = "Paris Getaway",
        destinationCity = "Paris",
        country = "France",
        startDate = start,
        endDate = end,
        travelersCount = 2,
        style = TripStyle.COUPLE,
        coverImageUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?q=80&w=1200",
        budget = Budget(currencyCode = "EUR", estimatedTotal = 1500.0),
        flights = listOf(
            FlightSegment(
                originCode = "BOG",
                destinationCode = "CDG",
                departure = LocalDateTime.now().plusDays(14).withHour(14).withMinute(0),
                arrival = LocalDateTime.now().plusDays(14).withHour(23).withMinute(40),
                airline = "Air France",
                flightNumber = "AF423"
            )
        ),
        hotels = listOf(
            HotelBooking(
                name = "Hôtel Particulier",
                address = "Montmartre, Paris",
                checkIn = LocalDateTime.now().plusDays(15).withHour(15),
                checkOut = LocalDateTime.now().plusDays(20).withHour(11),
                confirmationCode = "ABCD1234"
            )
        ),
        activities = listOf(
            ActivityItem(
                title = "Eiffel Tower",
                start = LocalDateTime.now().plusDays(16).withHour(10),
                end = LocalDateTime.now().plusDays(16).withHour(12),
                location = "Champ de Mars",
                category = ActivityCategory.SIGHTSEEING
            )
        ),
        restaurants = listOf(
            RestaurantReservation(
                name = "Le Jules Verne",
                time = LocalDateTime.now().plusDays(16).withHour(19),
                address = "Eiffel Tower, Paris",
                confirmationCode = "LJV-001"
            )
        ),
        notes = "Anniversary trip"
    )
}

@Preview(showBackground = true)
@Composable
private fun TripsHomeEmptyPreview() {
    WaypilotTheme {
        TripsHome(nav = rememberNavController(), trips = emptyList(), onTripClick = {})
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun TripsHomeWithItemPreview() {
    WaypilotTheme {
        TripsHome(nav = rememberNavController(), trips = listOf(fakeTrip()), onTripClick = {})
    }
}
