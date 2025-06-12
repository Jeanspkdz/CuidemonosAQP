package com.jean.cuidemonosaqp.modules.auth.ui.login

data class LoginState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
