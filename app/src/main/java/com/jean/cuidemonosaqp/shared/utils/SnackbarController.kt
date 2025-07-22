package com.jean.cuidemonosaqp.shared.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


object SnackBarController {
    private val _events = Channel<SnackBarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(e: SnackBarEvent){
        _events.send(e)
    }
}

data class SnackBarEvent(
    val message: String,
    val snackBarAction: SnackBarAction? = null
)

data class SnackBarAction(
    val name: String,
    val action: () -> Unit
)