// BottomBar.kt
package com.ddn.waypilot

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ddn.waypilot.nav.Dest
import com.ddn.waypilot.ui.theme.AccentCoral

@Composable
fun BottomBar(nav: NavHostController) {
    val items = listOf(Dest.Trips, Dest.Explore, Dest.Wishlist, Dest.Profile)
    val current by nav.currentBackStackEntryAsState()

    // SIN Surface (=> sin sombra)
    NavigationBar(containerColor = Color.White) {
        items.forEach { dest ->
            val (label, icon) = when (dest) {
                Dest.Trips -> "Trips" to Icons.Outlined.Map
                Dest.Explore -> "Explore" to Icons.Outlined.Public
                Dest.Wishlist -> "Wishlist" to Icons.Outlined.FavoriteBorder
                Dest.Profile -> "Profile" to Icons.Outlined.Person
                else -> "" to Icons.Outlined.Info
            }
            val selected = current?.destination?.route == dest.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    nav.navigate(dest.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(Dest.Trips.route) { saveState = true }
                    }
                },
                icon = { Icon(icon, null) },
                label = { Text(label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AccentCoral,
                    selectedTextColor = AccentCoral,
                    unselectedIconColor = Color(0xFF7E7E86),
                    unselectedTextColor = Color(0xFF7E7E86),
                    indicatorColor = AccentCoral.copy(alpha = 0.16f) // “pill” suave
                )
            )
        }
    }
}
