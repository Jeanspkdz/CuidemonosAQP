package com.jean.cuidemonosaqp.navigation

import kotlinx.serialization.Serializable

sealed class Routes {
    sealed class Auth : Routes() {
        @Serializable
        data object Login : Auth()

        @Serializable
        data object Register : Auth()
    }

    @Serializable
    data class Profile(val id: String) : Routes()

    @Serializable
    data object Map : Routes()

    sealed class SafeZone: Routes(){
        @Serializable
        data object Create: SafeZone()

        @Serializable
        data class Detail(val id: String): SafeZone()

        @Serializable
        data object List: SafeZone()
    }

}