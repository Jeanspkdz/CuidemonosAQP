// RegisterState.kt - Estado de registro actualizado
package com.jean.cuidemonosaqp.modules.auth.ui.register

data class RegisterState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val isOffline: Boolean = false // Nuevo campo para distinguir registros offline
)
