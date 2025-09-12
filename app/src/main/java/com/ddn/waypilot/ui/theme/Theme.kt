package com.ddn.waypilot.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val AccentCoral = Color(0xFFFF6D6D)
val BgLight     = Color(0xFFF6F6F8)
val TextMuted   = Color(0xFF6B6B6B)
val IconBg      = Color(0xFFF0F0F2)
val Stroke      = Color(0x1A000000) // ~10% negro

@Composable
fun WaypilotTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = AccentCoral,
            onPrimary = Color.White,
            surface = Color.White,
            background = BgLight,
            onSurface = Color(0xFF1E1E1E)
        ),
        content = content
    )
}
