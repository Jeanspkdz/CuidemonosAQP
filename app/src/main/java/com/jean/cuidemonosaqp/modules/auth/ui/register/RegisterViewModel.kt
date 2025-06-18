package com.jean.cuidemonosaqp.modules.auth.ui.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.cuidemonosaqp.modules.auth.data.model.RegisterResponse
import com.jean.cuidemonosaqp.modules.auth.domain.usecase.RegisterUseCase
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    fun registerUser(
        dni: RequestBody,
        firstName: RequestBody,
        lastName: RequestBody,
        dniExtension: RequestBody?,
        password: RequestBody,
        phone: RequestBody,
        email: RequestBody,
        address: RequestBody,
        reputationStatusId: RequestBody,
        dniPhoto: MultipartBody.Part?,
        profilePhoto: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            _registerState.value = RegisterState(isLoading = true)

            val result = registerUseCase(
                dni,
                firstName,
                lastName,
                dniExtension,
                password,
                phone,
                email,
                address,
                reputationStatusId,
                dniPhoto,
                profilePhoto
            )

            when (result) {
                is NetworkResult.Success<RegisterResponse> -> {
                    Log.d("REGISTER_SUCCESS", "Registro exitoso: ${result.data}")
                    _registerState.value = RegisterState(success = true)
                }
                is NetworkResult.Error -> {
                    Log.e("REGISTER_ERROR", "Error: ${result.message}")
                    _registerState.value = RegisterState(error = result.message)
                }
                is NetworkResult.Loading -> {
                    Log.d("REGISTER_LOADING", "Cargando...")
                }
            }

        }
    }

    fun clearError() {
        _registerState.value = RegisterState()
    }
}
