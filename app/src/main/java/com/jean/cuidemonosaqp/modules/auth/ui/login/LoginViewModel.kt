package com.jean.cuidemonosaqp.modules.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.cuidemonosaqp.modules.auth.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess = _isSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

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


        }
    }
}
