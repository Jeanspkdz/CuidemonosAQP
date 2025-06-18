package com.jean.cuidemonosaqp.navigation

import kotlinx.serialization.Serializable

sealed class Routes(val route: String) {
    sealed class Auth(route: String) : Routes(route) {
        object Login : Auth("login_screen") // Rutas específicas para Auth
        object Register : Auth("register_screen")
    }
    object Profile : Routes("profile_screen") // Ruta para Profile
    object Map : Routes("profile_map") // Ruta para Profile
    object CreateSafeZone : Routes("create_safe_zone_screen")
}
