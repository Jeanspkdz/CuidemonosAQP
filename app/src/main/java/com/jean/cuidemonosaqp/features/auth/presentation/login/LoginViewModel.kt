package com.jean.cuidemonosaqp.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()


    fun onAction(action: LoginAction){
        when(action){
            is LoginAction.OnEmailOrDniChanged -> {
                _state.update {
                    it.copy(
                        email_or_dni = action.text
                    )
                }
            }
            is LoginAction.OnPasswordChanged -> {
                _state.update {
                    it.copy(
                        password = action.password
                    )
                }
            }
            else -> Unit
        }
    }
}