package com.jean.cuidemonosaqp.modules.auth.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.cuidemonosaqp.modules.auth.domain.usecase.LoginUseCase
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.shared.preferences.SessionCache
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val sessionCache: SessionCache,
) : ViewModel() {

    private val _emailOrDni = MutableStateFlow("")
    val emailOrDni = _emailOrDni.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()



    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    fun onEmailChanged(emailOrDni: String) {
        _emailOrDni.update { emailOrDni }
    }

    fun onPasswordChanged(password: String) {
        _password.update { password }
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _loginState.value = LoginState(isLoading = true)

            try {
                // Usando LoginUseCase (método recomendado)
                when (val result = loginUseCase(emailOrDni.value, password.value)) {
                    is NetworkResult.Success -> {

                        val jwt    = result.data.accessToken
                        val userId = result.data.id.toString()

                        // Guardamos en DataStore
                        sessionCache.updateToken(jwt)
                        sessionCache.updateUserId(userId)
                        Log.d("LoginVM", "Token guardado: $jwt, UserId: $userId")

                        Log.d("LOGIN_SUCCESS", "Login exitoso")
                        Log.d("LOGIN_DATA", "Datos: ${result.data}")

                        _loginState.value = LoginState(success = true)
                    }
                    is NetworkResult.Error -> {
                        Log.d("LOGIN_ERROR", "Error: ${result.message}")
                        _loginState.value = LoginState(error = result.message)
                    }
                    is NetworkResult.Loading -> {
                        Log.d("LOGIN_LOADING", "Cargando...")
                    }
                }
            } catch (e: Exception) {
                Log.e("LOGIN_EXCEPTION", "Excepción durante login: ${e.message}", e)
                _loginState.value = LoginState(error = "Error inesperado: ${e.message}")
            }
        }
    }



    fun clearError() {
        _loginState.value = LoginState()
    }
}