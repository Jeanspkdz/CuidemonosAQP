package com.jean.cuidemonosaqp.navigation

import kotlinx.serialization.Serializable

sealed class Routes(val route: String) {
    sealed class Auth(route: String) : Routes(route) {
        object Login : Auth("login_screen") // Rutas específicas para Auth
        object Register : Auth("") // Rutas específicas para Auth
    }
    object Profile : Routes("profile_screen") // Ruta para Profile
}
