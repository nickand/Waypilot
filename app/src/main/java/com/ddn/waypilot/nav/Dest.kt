package com.ddn.waypilot.nav

sealed class Dest(val route: String) {
    data object Trips : Dest("trips")
    data object AddTrip : Dest("add_trip")
    data object PlanTrip : Dest("plan_trip") // si ya existía, la dejamos como alias
    data object AiPlanner : Dest("ai_planner")
    data object Explore : Dest("explore")
    data object Wishlist : Dest("wishlist")
    data object Profile : Dest("profile")
    data object ItineraryDetail : Dest("itinerary/{tripId}") {
        const val ARG_TRIP_ID = "tripId"
        fun route(id: String) = "itinerary/$id"
    }
}
