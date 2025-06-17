package com.jean.cuidemonosaqp.modules.auth.ui.register

data class RegisterState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
