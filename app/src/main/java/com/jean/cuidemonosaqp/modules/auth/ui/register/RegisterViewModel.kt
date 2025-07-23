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
import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.update

import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    // Estados individuales para cada campo - mismo patrón que LoginViewModel
    private val _dni = MutableStateFlow("")
    val dni = _dni.asStateFlow()

    private val _firstName = MutableStateFlow("")
    val firstName = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName = _lastName.asStateFlow()

    private val _dniExtension = MutableStateFlow("")
    val dniExtension = _dniExtension.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    private val _phone = MutableStateFlow("")
    val phone = _phone.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _address = MutableStateFlow("")
    val address = _address.asStateFlow()

    private val _reputationStatusId = MutableStateFlow("1")
    val reputationStatusId = _reputationStatusId.asStateFlow()

    // Estados para las imágenes
    private val _profilePhotoUri = MutableStateFlow<Uri?>(null)
    val profilePhotoUri = _profilePhotoUri.asStateFlow()

    private val _dniPhotoUri = MutableStateFlow<Uri?>(null)
    val dniPhotoUri = _dniPhotoUri.asStateFlow()

    // Estado del registro - mismo patrón que LoginViewModel
    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    // NUEVO: Estado para validación de contraseñas
    val passwordsMatch = combine(_password, _confirmPassword) { password, confirmPassword ->
        if (confirmPassword.isEmpty()) true // No mostrar error si confirmPassword está vacío
        else password == confirmPassword
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )


    // Métodos para actualizar cada campo - mismo patrón que LoginViewModel
    fun onDniChanged(dni: String) {
        _dni.update { dni }
    }

    fun onFirstNameChanged(firstName: String) {
        _firstName.update { firstName }
    }

    fun onLastNameChanged(lastName: String) {
        _lastName.update { lastName }
    }

    fun onDniExtensionChanged(dniExtension: String) {
        _dniExtension.update { dniExtension }
    }

    fun onPasswordChanged(password: String) {
        _password.update { password }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _confirmPassword.update {confirmPassword}
    }

    fun onPhoneChanged(phone: String) {
        _phone.update { phone }
    }

    fun onEmailChanged(email: String) {
        _email.update { email }
    }

    fun onAddressChanged(address: String) {
        _address.update { address }
    }

    fun onReputationStatusIdChanged(reputationStatusId: String) {
        _reputationStatusId.update { reputationStatusId }
    }

    // Métodos para manejar las imágenes
    fun onProfilePhotoSelected(uri: Uri?) {
        _profilePhotoUri.update { uri }
    }

    fun onDniPhotoSelected(uri: Uri?) {
        _dniPhotoUri.update { uri }
    }

    // Méthodo principal de registro - refactorizado para usar los estados internos
    fun onRegisterClicked() {
        if (!isFormValid()) {
            _registerState.value = RegisterState(error = "Por favor complete todos los campos obligatorios.")
            return
        }

        if (!passwordsMatch.value) {
            _registerState.value = RegisterState(error = "Las contraseñas no coinciden.")
            return
        }

        viewModelScope.launch {
            clearError()
            _registerState.value = RegisterState(isLoading = true)

            try {
                // Preparar los datos usando los estados actuales
                val dniBody = _dni.value.toRequestBody("text/plain".toMediaTypeOrNull())
                val firstNameBody = _firstName.value.toRequestBody("text/plain".toMediaTypeOrNull())
                val lastNameBody = _lastName.value.toRequestBody("text/plain".toMediaTypeOrNull())
                val passwordBody = _password.value.toRequestBody("text/plain".toMediaTypeOrNull())
                val phoneBody = _phone.value.toRequestBody("text/plain".toMediaTypeOrNull())
                val emailBody = _email.value.toRequestBody("text/plain".toMediaTypeOrNull())
                val addressBody = _address.value.toRequestBody("text/plain".toMediaTypeOrNull())
                val repStatusBody = _reputationStatusId.value.toRequestBody("text/plain".toMediaTypeOrNull())
                val dniExtBody = _dniExtension.value.takeIf { it.isNotBlank() }
                    ?.toRequestBody("text/plain".toMediaTypeOrNull())
                val contentResolver = context.contentResolver

                // Preparar las imágenes si están disponibles
                val dniPart = _dniPhotoUri.value?.let { uri ->
                    uriToPart("dni_photo", uri, contentResolver)
                }
                val profilePart = _profilePhotoUri.value?.let { uri ->
                    uriToPart("profile_photo", uri, contentResolver)
                }

                // Llamar al use case con NetworkResult pattern - mismo que LoginViewModel
                when (val result:  NetworkResult<RegisterResponse> = registerUseCase(
                    dniBody,
                    firstNameBody,
                    lastNameBody,
                    dniExtBody,
                    passwordBody,
                    phoneBody,
                    emailBody,
                    addressBody,
                    repStatusBody,
                    dniPart,
                    profilePart
                )) {
                    is NetworkResult.Success -> {
                        Log.d("REGISTER_SUCCESS", "Registro exitoso")
                        Log.d("REGISTER_DATA", "Datos: ${result.data}")
                        _registerState.value = RegisterState(success = true)
                        clearAllFields()
                    }
                    is NetworkResult.Error -> {
                        Log.e("REGISTER_ERROR", "Error: ${result.message}")
                        _registerState.value = RegisterState(error = result.message ?: "Error desconocido")
                    }
                    is NetworkResult.Loading -> {
                        Log.d("REGISTER_LOADING", "Cargando...")
                        // El loading ya está manejado arriba
                    }
                }
            } catch (e: Exception) {
                Log.e("REGISTER_EXCEPTION", "Excepción durante registro: ${e.message}", e)
                _registerState.value = RegisterState(error = "Error inesperado: ${e.message}")
            }

        }
    }

    // Método para limpiar errores - mismo que LoginViewModel
    fun clearError() {
        _registerState.value = RegisterState()
    }

    // Método para limpiar todos los campos (opcional, útil después de registro exitoso)
    fun clearAllFields() {
        _dni.value = ""
        _firstName.value = ""
        _lastName.value = ""
        _dniExtension.value = ""
        _password.value = ""
        _confirmPassword.value = ""
        _phone.value = ""
        _email.value = ""
        _address.value = ""
        _reputationStatusId.value = "1"
        _profilePhotoUri.value = null
        _dniPhotoUri.value = null
//        _registerState.value = RegisterState()
    }

    // Método de validación (opcional, para usar en la UI)
    fun isFormValid(): Boolean {
        return _dni.value.isNotBlank() &&
                _firstName.value.isNotBlank() &&
                _lastName.value.isNotBlank() &&
                _email.value.isNotBlank() &&
                _password.value.isNotBlank() &&
                _confirmPassword.value.isNotBlank() &&
                _phone.value.isNotBlank() &&
                _address.value.isNotBlank() &&
                _password.value == _confirmPassword.value
    }
}

// Función helper para convertir URI a MultipartBody.Part
// Esta función debería estar en un archivo de utilidades, pero la incluyo aquí para referencia
private fun uriToPart(name: String, uri: Uri, contentResolver: ContentResolver): MultipartBody.Part? {
    return try {
        val inputStream = contentResolver.openInputStream(uri)
        val mimeType = contentResolver.getType(uri) ?: "image/*"
        val fileName = "image.${mimeType.substringAfter("/")}"

        val requestBody = inputStream?.readBytes()?.toRequestBody(mimeType.toMediaTypeOrNull())
        requestBody?.let {
            MultipartBody.Part.createFormData(name, fileName, it)
        }
    } catch (e: Exception) {
        Log.e("URI_TO_PART", "Error convertiendo URI a Part: ${e.message}")
        null
    }
}
