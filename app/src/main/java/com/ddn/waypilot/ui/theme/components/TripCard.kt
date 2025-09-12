package com.ddn.waypilot.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
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
    val isPreview = false //LocalInspectionMode.current

    Card(
        onClick = { onClick(trip) },
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        // Cover image with gradient overlay (Airbnb-like)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.LightGray)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xAA000000)),
                            startY = size.height / 3f,
                            endY = size.height
                        )
                    )
                }
        ) {
            if (isPreview) {
                // En Preview no hay red: usa recurso local
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else if (!trip.coverImageUrl.isNullOrBlank()) {
                // Runtime: carga con Coil 3
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(trip.coverImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.ic_launcher_background),
                    error = painterResource(R.drawable.ic_launcher_background),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Fallback: icono genérico si no hay URL
                Image(
                    painter = painterResource(android.R.drawable.ic_menu_mapmode),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFECECEC)),
                    contentScale = ContentScale.Crop
                )
            }

            // Top-right badges (flights/hotels counts)
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CounterPill(icon = Icons.Default.Flight, count = trip.flights.size)
                CounterPill(icon = Icons.Default.Hotel, count = trip.hotels.size)
            }

            // Bottom text overlay
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
                    val pct = (trip.progressRatio() * 100).toInt()
                    LinearProgressIndicator(
                        progress = { trip.progressRatio() },
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

        // Bottom meta row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // budget hint
            val budgetText = trip.budget?.let { b ->
                val total = b.estimatedTotal ?: b.max ?: b.min
                total?.let { "~${b.currencyCode} ${"%,.0f".format(it)}" } ?: "—"
            } ?: "—"

            Text(
                text = "Budget: $budgetText",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            // small map button
            FilledTonalButton(
                onClick = { /* open map screen */ },
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(Icons.Default.Map, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Map")
            }
        }
    }
}