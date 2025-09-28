package com.ddn.waypilot.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.ddn.waypilot.R
import com.ddn.waypilot.data.Trip
import com.ddn.waypilot.datesShort
import com.ddn.waypilot.progressRatio
import com.ddn.waypilot.toChipText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripCard(
    trip: Trip,
    modifier: Modifier = Modifier,
    onClick: (Trip) -> Unit = {}
) {
    val isPreview = LocalInspectionMode.current

    Card(
        onClick = { onClick(trip) },
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        // ======== Cover area (imagen + gradiente + overlays) ========
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            // Imagen (URL/URI con Coil) o placeholder local
            if (!isPreview && !trip.coverImageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(trip.coverImageUrl)   // admite https:// o content://
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.bg_itinerary_default),
                    error = painterResource(R.drawable.bg_itinerary_default)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.bg_itinerary_default),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Gradiente para legibilidad del texto
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xAA000000)),
                            startY = 0.45f * 220.dp.value, // aprox mitad inferior
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            // Badges top-right (conteos)
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CounterPill(icon = Icons.Default.Flight, count = trip.flights.size)
                CounterPill(icon = Icons.Default.Hotel, count = trip.hotels.size)
            }

            // Texto inferior sobre la imagen
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = trip.title,
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${trip.destinationCity}, ${trip.country} • ${trip.datesShort()}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.9f)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AssistChip(
                        onClick = {},
                        label = { Text(trip.style.toChipText()) }
                    )
                    Spacer(Modifier.width(12.dp))
                    val ratio = trip.progressRatio()
                    val pct = (ratio * 100).toInt()
                    LinearProgressIndicator(
                        progress = { ratio },
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(50)),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.25f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("$pct%", color = Color.White, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        // ======== Fila inferior con meta ========
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Budget (tu Budget solo tiene currencyCode + estimatedTotal)
            val budgetText = trip.budget?.let { b ->
                b.estimatedTotal?.let { "~${b.currencyCode} ${"%,.0f".format(it)}" }
            } ?: "—"

            Text(
                text = "Budget: $budgetText",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            FilledTonalButton(
                onClick = { /* TODO: open map screen */ },
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(Icons.Default.Map, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Map")
            }
        }
    }
}
