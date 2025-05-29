package com.jean.cuidemonosaqp.features.auth.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.cuidemonosaqp.features.auth.domain.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase): ViewModel() {
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
            is LoginAction.OnLoginClicked -> {
                login()
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val result = loginUseCase(
                identifier = state.value.email_or_dni,
                password = state.value.password
            )

            result
                .onSuccess { token ->
                    Log.d("LoginViewModel", "Login exitoso. Token: $token")

                    // Guarda token en SharedPreferences si deseas
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { e ->
                    Log.e("LoginViewModel", "Error de login: ${e.message}")
                    _state.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
        }
    }
}