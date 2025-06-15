package com.jean.cuidemonosaqp.modules.auth.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.cuidemonosaqp.modules.auth.domain.AuthRepository
import com.jean.cuidemonosaqp.shared.utils.UiText
import com.jean.cuidemonosaqp.shared.utils.handleExceptionToStringRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _emailOrDni = MutableStateFlow("")
    val emailOrDni = _emailOrDni.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<UiText?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    //Channel
    private val _event = Channel<LoginEvent>()
    val event = _event.receiveAsFlow()

    fun onEmailChanged(emailOrDni: String) {
        _emailOrDni.update {
            emailOrDni
        }
    }

    fun onPasswordChanged(password: String) {
        _password.update {
            password
        }
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val response = authRepository.login(emailOrDni.value, password.value)

            response
                .onSuccess {
                    Log.d("GAAAA", "onLoginClicked: AUTNETICADO CORRECTAMETNE ")
                    _event.send(LoginEvent.NavigateToHome)
                }
                .onFailure { exception ->
                    _errorMessage.update {
                        val stringRes = exception.handleExceptionToStringRes()
                        UiText.StringResource(stringRes)
                    }
                }

            _isLoading.value = false
        }
    }
}


sealed class LoginEvent {
    object NavigateToHome : LoginEvent()
}