/*package com.jean.cuidemonosaqp.modules.auth.ui.register

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.cuidemonosaqp.modules.auth.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    fun onFullNameChange(value: String) {
        _fullName.value = value
    }

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun onConfirmPasswordChange(value: String) {
        _confirmPassword.value = value
    }

    fun onAddressChange(value: String) {
        _address.value = value
    }

    fun onPhoneNumberChange(value: String) {
        _phoneNumber.value = value
    }

    fun onClickRegisterButton(){

    }

}*/