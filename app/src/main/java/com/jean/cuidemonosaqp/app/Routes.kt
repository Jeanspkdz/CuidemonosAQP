package com.jean.cuidemonosaqp.app

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    data object Auth
}