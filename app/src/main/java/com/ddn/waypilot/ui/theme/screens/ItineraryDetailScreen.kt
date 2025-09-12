package com.ddn.waypilot.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ddn.waypilot.data.Trip
import com.ddn.waypilot.plus
import com.ddn.waypilot.timeFormatter
import com.ddn.waypilot.ui.theme.components.ListCard
import com.ddn.waypilot.ui.theme.components.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryDetailScreen(
    trip: Trip,
    onBack: () -> Unit = {},
    onShare: () -> Unit = {},
    onMore: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
                },
                title = { Text(trip.title) },
                actions = {
                    IconButton(onClick = onShare) { Icon(Icons.Default.Share, null) }
                    IconButton(onClick = onMore) { Icon(Icons.Default.MoreVert, null) }
                }
            )
        },
    ) { padding ->
        LazyColumn(
            contentPadding = padding + PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Flights
            if (trip.flights.isNotEmpty()) {
                item {
                    SectionHeader(icon = Icons.Default.Flight, title = "Flights")
                }
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
                            Text("$it ${seg.flightNumber ?: ""}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }
                }
            }

            // Hotels
            if (trip.hotels.isNotEmpty()) {
                item { SectionHeader(icon = Icons.Default.Hotel, title = "Hotels") }
                items(trip.hotels) { h ->
                    ListCard {
                        Text(h.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(Modifier.height(2.dp))
                        Text("Check-in: ${h.checkIn.format(timeFormatter)}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        h.address?.let {
                            Spacer(Modifier.height(4.dp))
                            Text(it, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            // Activities
            if (trip.activities.isNotEmpty()) {
                item { SectionHeader(icon = painterResource(android.R.drawable.ic_menu_week), title = "Activities") }
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
                        Text(time, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        a.location?.let {
                            Spacer(Modifier.height(4.dp))
                            Text(it, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            // Restaurants
            if (trip.restaurants.isNotEmpty()) {
                item { SectionHeader(icon = Icons.Default.Restaurant, title = "Restaurants") }
                items(trip.restaurants) { r ->
                    ListCard {
                        Text(r.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(Modifier.height(2.dp))
                        Text(
                            r.time?.format(timeFormatter) ?: "Time TBA",
                            style = MaterialTheme.typography.bodySmall, color = Color.Gray
                        )
                        r.address?.let {
                            Spacer(Modifier.height(4.dp))
                            Text(it, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            // Spacer bottom
            item { Spacer(Modifier.height(64.dp)) }
        }
    }
}
