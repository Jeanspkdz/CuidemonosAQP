package com.jean.cuidemonosaqp.app

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    data object Auth

    @Serializable
    data object Profile

    @Serializable
    data object Register
}