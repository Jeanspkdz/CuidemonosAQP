package com.jean.cuidemonosaqp.features.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.net.Uri

class RegisterViewModel : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onAction(action: RegisterAction) {
        when(action) {
            is RegisterAction.OnNameChanged -> {
                _state.update { it.copy(name = action.text) }
            }
            is RegisterAction.OnEmailChanged -> {
                _state.update { it.copy(email = action.text) }
            }
            is RegisterAction.OnPasswordChanged -> {
                _state.update { it.copy(password = action.text) }
            }
            is RegisterAction.OnConfirmPasswordChanged -> {
                _state.update { it.copy(confirmPassword = action.text) }
            }
            is RegisterAction.OnAddressChanged -> {
                _state.update { it.copy(address = action.text) }
            }
            is RegisterAction.OnPhoneNumberChanged -> {
                _state.update { it.copy(phoneNumber = action.text) }
            }
            is RegisterAction.OnDniPhotoSelected -> {
                _state.update { it.copy(dniPhotoUri = action.uri) }
            }
            is RegisterAction.OnProfilePhotoSelected -> {
                _state.update { it.copy(profilePhotoUri = action.uri) }
            }
            RegisterAction.OnRegisterClicked -> {
                // Aquí iría la lógica de registro
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true, errorMessage = null) }
                    // Simular registro
                    kotlinx.coroutines.delay(1500)
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                }
            }
        }
    }
}
