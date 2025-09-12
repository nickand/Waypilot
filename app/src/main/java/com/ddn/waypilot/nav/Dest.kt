package com.ddn.waypilot.nav

sealed class Dest(val route: String) {
    data object Trips : Dest("trips")
    data object Explore : Dest("explore")
    data object Wishlist : Dest("wishlist")
    data object Profile : Dest("profile")
    data object PlanTrip : Dest("planTrip")
    data object AiPlanner : Dest("aiPlanner")
    data object ItineraryDetail : Dest("itinerary/{tripId}") {
        fun route(tripId: String) = "itinerary/$tripId"
        const val ARG_TRIP_ID = "tripId"
    }
}