package com.ddn.waypilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ddn.waypilot.nav.Dest
import com.ddn.waypilot.ui.theme.WaypilotTheme
import com.ddn.waypilot.ui.theme.screens.ItineraryDetailScreen
import com.ddn.waypilot.ui.theme.screens.PlaceholderScreen
import com.ddn.waypilot.ui.theme.screens.TripsHome
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { WaypilotTheme { RootNav() } }
    }
}

@Composable
fun RootNav() {
    val nav = rememberNavController()
    val repo = remember { FakeTripsRepo }  // ← usa el repo en memoria

    Scaffold(bottomBar = { BottomBar(nav) }) { inner ->
        NavHost(
            navController = nav,
            startDestination = Dest.Trips.route,
            modifier = Modifier.padding(inner)
        ) {
            composable(Dest.Trips.route) {
                TripsHome(
                    nav = nav,
                    trips = repo.list(),
                    onTripClick = { trip -> nav.navigate(Dest.ItineraryDetail.route(trip.id)) }
                )
            }

            composable(
                route = Dest.ItineraryDetail.route,
                arguments = listOf(navArgument(Dest.ItineraryDetail.ARG_TRIP_ID) { type = NavType.StringType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString(Dest.ItineraryDetail.ARG_TRIP_ID).orEmpty()
                val trip = repo.get(tripId)
                if (trip != null) {
                    ItineraryDetailScreen(
                        trip = trip,
                        onBack = { nav.popBackStack() }
                    )
                } else {
                    // Fallback simple si no lo encontró
                    androidx.compose.material3.Text("Trip not found")
                }
            }

            composable(Dest.Explore.route) { PlaceholderScreen("Explore") }
            composable(Dest.Wishlist.route) { PlaceholderScreen("Wishlist") }
            composable(Dest.Profile.route) { PlaceholderScreen("Profile") }
            composable(Dest.PlanTrip.route) { PlaceholderScreen("Plan a Trip") }
            composable(Dest.AiPlanner.route) { PlaceholderScreen("AI Trip Planner") }
        }
    }
}
