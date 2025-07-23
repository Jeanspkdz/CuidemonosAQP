package com.jean.cuidemonosaqp.shared.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.jean.cuidemonosaqp.navigation.Routes

data class TopLevelRoute(
    val icon: ImageVector,
    val label: String,
    val route: Routes
)

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentDestination: NavDestination?,
    userId: String
) {

    val navBarItems = listOf(
        TopLevelRoute(
            icon = Icons.Default.Home,
            label = "Mapa",
            route = Routes.Map
        ),
        TopLevelRoute(
            icon = Icons.Default.Star,
            label = "Puntos",
            route = Routes.SafeZone.List
        ),
        TopLevelRoute(
            icon = Icons.Default.Person,
            label = "Profile",
            route = Routes.Profile(id=userId)
        ),
    )

    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        navBarItems.map { topLevelRoute ->
            NavigationBarItem(
                icon = {
                    Icon(imageVector = topLevelRoute.icon, contentDescription = topLevelRoute.label)
                },
                label = { Text(topLevelRoute.label, fontSize = 12.sp) },
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(topLevelRoute.route::class)
                } ?: false,
                onClick = {
                    Log.d("NAVIGATION", "Clicking on: ${topLevelRoute.route}")
                    Log.d("NAVIGATION", "Current destination: ${navController.currentDestination?.route}")
                    Log.d("NAVIGATION", "Start destination: ${navController.graph.startDestinationId}")
                    Log.d("NAVIGATION", "findStartDestination ID: ${navController.graph.findStartDestination().id}")
                    Log.d("NAVIGATION", "Are they equal? ${navController.graph.startDestinationId == navController.graph.findStartDestination().id}")
                    navController.navigate(route = topLevelRoute.route){
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF2196F3),
                    selectedTextColor = Color(0xFF2196F3),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }


    }
}