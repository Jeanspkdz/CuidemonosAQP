package com.jean.cuidemonosaqp.navigation

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    data object Auth

    @Serializable
    data object Profile
}