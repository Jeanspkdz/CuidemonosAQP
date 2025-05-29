package com.jean.cuidemonosaqp.features.auth.presentation.login

sealed interface LoginAction {
    data class OnEmailOrDniChanged(val text: String): LoginAction
    data class OnPasswordChanged(val password: String): LoginAction
    object OnLoginClicked : LoginAction
}