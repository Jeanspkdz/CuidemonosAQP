package com.jean.cuidemonosaqp.navigation

import kotlinx.serialization.Serializable

sealed class Routes {

    sealed class Auth {
        @Serializable
        data object Login

        @Serializable
        data object Register
    }

    @Serializable
    data object Profile

    @Serializable
    data object Home

    sealed class Points {
        @Serializable
        data object Create
    }
}