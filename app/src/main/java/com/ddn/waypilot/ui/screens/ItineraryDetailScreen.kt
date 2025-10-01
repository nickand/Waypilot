package com.ddn.waypilot.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ddn.waypilot.data.Trip
import com.ddn.waypilot.plus
import com.ddn.waypilot.timeFormatter
import com.ddn.waypilot.ui.theme.components.ListCard
import com.ddn.waypilot.ui.theme.components.SectionHeader
import com.ddn.waypilot.ui.theme.components.TripCover
import com.ddn.waypilot.ui.trips.ItineraryDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryDetailScreen(
    trip: Trip,
    onBack: () -> Unit = {},
    onShare: () -> Unit = {},
    onMore: () -> Unit = {}, // This specific onMore might be replaced or augmented
    // onDeleteTrip: () -> Unit = {} // Replaced by dialog and ViewModel call
    vm: ItineraryDetailViewModel = hiltViewModel()
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            tripTitle = trip.title,
            onConfirm = {
                showDeleteDialog = false
                vm.deleteTrip(trip) // Assuming vm.deleteTrip(trip) will be implemented
                onBack() // Navigate back after deletion
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
                },
                title = { Text(trip.title) },
                actions = {
                    IconButton(onClick = onShare) { Icon(Icons.Default.Share, null) }
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More options")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Delete Trip") },
                                onClick = {
                                    showDeleteDialog = true
                                    showMenu = false
                                },
                                leadingIcon = { Icon(Icons.Filled.Delete, contentDescription = "Delete Trip") }
                            )
                            // You can add other items to onMore here if needed
                            // For example, if the original onMore had other specific actions:
                            // DropdownMenuItem(text = { Text("Other Option") }, onClick = { onMore(); showMenu = false })
                        }
                    }
                }
            )
        },
    ) { padding ->
        LazyColumn(
            contentPadding = padding + PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ===== Summary always visible =====
            item {
                TripCover(
                    cover = trip.coverImageUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Spacer(Modifier.height(12.dp))
                TripSummaryCard(trip = trip)
            }

            // ===== Flights =====
            if (trip.flights.isNotEmpty()) {
                item { SectionHeader(icon = Icons.Default.Flight, title = "Flights") }
                items(trip.flights) { seg ->
                    ListCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(seg.originCode, style = MaterialTheme.typography.titleMedium)
                                Text(seg.departure.format(timeFormatter), style = MaterialTheme.typography.labelMedium)
                            }
                            Text("✈︎", modifier = Modifier.align(Alignment.CenterVertically))
                            Column(horizontalAlignment = Alignment.End) {
                                Text(seg.destinationCode, style = MaterialTheme.typography.titleMedium)
                                Text(seg.arrival.format(timeFormatter), style = MaterialTheme.typography.labelMedium)
                            }
                        }
                        seg.airline?.let {
                            Spacer(Modifier.height(6.dp))
                            Text("$it ${seg.flightNumber ?: ""}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            } else {
                item { EmptySectionCard(icon = Icons.Default.Flight, title = "No flights yet", subtitle = "Add your flight to keep times handy.") }
            }

            // ===== Hotels =====
            if (trip.hotels.isNotEmpty()) {
                item { SectionHeader(icon = Icons.Default.Hotel, title = "Hotels") }
                items(trip.hotels) { h ->
                    ListCard {
                        Text(h.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(Modifier.height(2.dp))
                        Text("Check-in: ${h.checkIn.format(timeFormatter)}", style = MaterialTheme.typography.bodySmall)
                        h.address?.let {
                            Spacer(Modifier.height(4.dp))
                            Text(it, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            } else {
                item { EmptySectionCard(icon = Icons.Default.Hotel, title = "No hotels yet", subtitle = "Add your stay to see check-in/out.") }
            }

            // ===== Activities =====
            if (trip.activities.isNotEmpty()) {
                item { SectionHeader(icon = Icons.Default.Map, title = "Activities") }
                items(trip.activities) { a ->
                    ListCard {
                        Text(a.title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(Modifier.height(2.dp))
                        val time = when {
                            a.start != null && a.end != null ->
                                "${a.start.format(timeFormatter)} – ${a.end.format(timeFormatter)}"
                            a.start != null -> a.start.format(timeFormatter)
                            else -> "Time TBA"
                        }
                        Text(time, style = MaterialTheme.typography.bodySmall)
                        a.location?.let {
                            Spacer(Modifier.height(4.dp))
                            Text(it, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            } else {
                item { EmptySectionCard(icon = Icons.Default.Map, title = "No activities yet", subtitle = "Add plans to build your day-by-day.") }
            }

            // ===== Restaurants =====
            if (trip.restaurants.isNotEmpty()) {
                item { SectionHeader(icon = Icons.Default.Restaurant, title = "Restaurants") }
                items(trip.restaurants) { r ->
                    ListCard {
                        Text(r.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(Modifier.height(2.dp))
                        Text(
                            r.time?.format(timeFormatter) ?: "Time TBA",
                            style = MaterialTheme.typography.bodySmall
                        )
                        r.address?.let {
                            Spacer(Modifier.height(4.dp))
                            Text(it, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            } else {
                item { EmptySectionCard(icon = Icons.Default.Restaurant, title = "No restaurants yet", subtitle = "Save reservations and times here.") }
            }

            item { Spacer(Modifier.height(64.dp)) }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    tripTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Trip") },
        text = { Text("Are you sure you want to delete '$tripTitle'? This action cannot be undone.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun TripSummaryCard(trip: Trip) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(trip.destinationCity, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("${trip.startDate} – ${trip.endDate}", style = MaterialTheme.typography.bodyMedium)
            Text("Travelers: ${trip.travelersCount}", style = MaterialTheme.typography.bodyMedium)
            trip.budget?.let { b ->
                Text("Budget: ${b.currencyCode} ${"%,.2f".format(b.estimatedTotal)}", style = MaterialTheme.typography.bodyMedium)
            }
            trip.notes?.takeIf { it.isNotBlank() }?.let {
                Text(it, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun EmptySectionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(2.dp))
                Text(subtitle, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
