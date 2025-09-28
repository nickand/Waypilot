// ui/theme/screens/TripsHome.kt
package com.ddn.waypilot.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.TravelExplore
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ddn.waypilot.data.Trip
import com.ddn.waypilot.nav.Dest
import com.ddn.waypilot.ui.theme.*
import com.ddn.waypilot.ui.theme.components.TripCard
import com.ddn.waypilot.ui.trips.TripsHomeViewModel

@Composable
fun TripsHome(
    nav: NavHostController,
    vm: TripsHomeViewModel = hiltViewModel(),
    onTripClick: (Trip) -> Unit
) {
    LaunchedEffect(Unit) { vm.load() }
    val trips by vm.state.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TripsTopBar(
            onFilter = { /* TODO */ },
            onAdd = { nav.navigate(Dest.PlanTrip.route) }
        )
        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (trips.isEmpty()) {
                item { UpcomingTripsCard(onPlan = { nav.navigate(Dest.PlanTrip.route) }) }
                item { AiPlannerCard(onTry = { nav.navigate(Dest.AiPlanner.route) }) }
                item { Spacer(Modifier.height(88.dp)) }
            } else {
                // Título opcional “Saved trips”
                item {
                    Text(
                        "Saved trips",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                // Cards reales
                items(
                    items = trips,
                    key = { it.id } // opcional pero recomendado para estabilidad
                ) { trip ->
                    TripCard(
                        trip = trip,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onTripClick(trip) } // ✅ aquí va el trip clickeado
                    )
                }

                item { Spacer(Modifier.height(88.dp)) }
            }
        }
    }
}

@Composable
private fun TripsTopBar(onFilter: () -> Unit, onAdd: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surface, // <- blanco
        shadowElevation = 0.dp
    ) {
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoundIconButton(Icons.Outlined.Tune, onFilter)
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        "My Trips",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                RoundIconButton(Icons.Outlined.Add, onAdd)
            }
            HorizontalDivider(color = Color(0xFFEDEDEF), thickness = 1.dp)
        }
    }
}

@Composable
private fun UpcomingTripsCard(onPlan: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Stroke),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Outlined.TravelExplore, contentDescription = null,
                tint = AccentCoral, modifier = Modifier.size(56.dp))
            Spacer(Modifier.height(12.dp))
            Text(
                "No upcoming trips",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Your next adventure awaits. Let's\nplan it!",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onPlan,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentCoral)
            ) { Text("Plan a Trip") }
        }
    }
}

@Composable
private fun AiPlannerCard(onTry: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Stroke),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    "AI Trip Planner",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "Get a personalized\nitinerary in seconds",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                )
            }
            Button(
                onClick = onTry,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentCoral)
            ) {
                Text("Try Now")
                Spacer(Modifier.width(6.dp))
                Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
            }
        }
    }
}

@Composable
private fun RoundIconButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(IconBg),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick, modifier = Modifier.size(42.dp)) {
            Icon(icon, contentDescription = null, tint = Color(0xFF3D3D3D))
        }
    }
}
