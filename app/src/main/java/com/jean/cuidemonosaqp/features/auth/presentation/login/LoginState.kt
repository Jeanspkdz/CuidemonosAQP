package com.jean.cuidemonosaqp.features.auth.presentation.login

import androidx.compose.runtime.Immutable

@Immutable
data class LoginState(
    val email_or_dni: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)