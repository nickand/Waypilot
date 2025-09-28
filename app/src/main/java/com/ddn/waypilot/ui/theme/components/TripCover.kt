// ui/theme/components/TripCover.kt
package com.ddn.waypilot.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.ddn.waypilot.R

@Composable
fun TripCover(
    cover: String?,
    modifier: Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    if (cover.isNullOrBlank()) {
        Image(
            painter = painterResource(R.drawable.bg_itinerary_default),
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        AsyncImage(
            model = cover,   // URL o content://
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale,
            placeholder = painterResource(R.drawable.bg_itinerary_default),
            error = painterResource(R.drawable.bg_itinerary_default)
        )
    }
}
