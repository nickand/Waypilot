package com.ddn.waypilot

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ddn.waypilot.nav.Dest
import com.ddn.waypilot.ui.screens.AddTripScreen
import com.ddn.waypilot.ui.theme.WaypilotTheme
import com.ddn.waypilot.ui.screens.ItineraryDetailScreen
import com.ddn.waypilot.ui.screens.PlaceholderScreen
import com.ddn.waypilot.ui.screens.TripsHome
import com.ddn.waypilot.ui.trips.ItineraryDetailViewModel
import com.ddn.waypilot.ui.trips.TripsHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { WaypilotTheme { RootNav() } }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun RootNav() {
    val nav = rememberNavController()

    Scaffold(bottomBar = { BottomBar(nav) }) { inner ->
        NavHost(
            navController = nav,
            startDestination = Dest.Trips.route,
            modifier = Modifier.padding(inner)
        ) {
            // Home: lista de viajes desde Room vía ViewModel
            composable(Dest.Trips.route) {
                val vm: TripsHomeViewModel = hiltViewModel()
                TripsHome(
                    nav = nav,
                    onTripClick = { trip -> nav.navigate(Dest.ItineraryDetail.route(trip.id)) }
                )
            }

            // Detalle: carga por tripId usando ViewModel + SavedStateHandle
            composable(
                route = Dest.ItineraryDetail.route,
                arguments = listOf(
                    navArgument(Dest.ItineraryDetail.ARG_TRIP_ID) { type = NavType.StringType }
                )
            ) {
                val vm: ItineraryDetailViewModel = hiltViewModel()
                val trip = vm.trip // state sencillo (null mientras carga)
                if (trip != null) {
                    ItineraryDetailScreen(
                        trip = trip,
                        onBack = { nav.popBackStack() }
                    )
                } else {
                    // UI mínima de carga / vacío
                    BoxWithConstraints { CircularProgressIndicator() }
                }
            }

            // Agregar viaje: formulario básico que guarda en Room y vuelve
            // (si ya tienes Dest.PlanTrip, puedes enrutarlo aquí también)
            composable(Dest.AddTrip.route) {
                AddTripScreen(onDone = { nav.popBackStack() })
            }
            // Alias opcional si ya navegabas a PlanTrip
            composable(Dest.PlanTrip.route) {
                AddTripScreen(onDone = { nav.popBackStack() })
            }

            composable(Dest.Explore.route)  { PlaceholderScreen("Explore") }
            composable(Dest.Wishlist.route) { PlaceholderScreen("Wishlist") }
            composable(Dest.Profile.route)  { PlaceholderScreen("Profile") }
            composable(Dest.AiPlanner.route){ PlaceholderScreen("AI Trip Planner") }
        }
    }
}
