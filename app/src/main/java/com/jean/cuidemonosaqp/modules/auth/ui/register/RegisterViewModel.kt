package com.jean.cuidemonosaqp.modules.auth.ui.register

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.cuidemonosaqp.R
import com.jean.cuidemonosaqp.modules.auth.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName

    private val _dni = MutableStateFlow("")
    val dni: StateFlow<String> = _dni

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

    private val _dniImageUri = MutableStateFlow<Uri?>(null)
    val dniImageUri: StateFlow<Uri?> = _dniImageUri

    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri: StateFlow<Uri?> = _profileImageUri

    private val _registerSuccess = MutableSharedFlow<Unit>()
    val registerSuccess = _registerSuccess.asSharedFlow()

    fun onFirstNameChange(value: String) {
        _firstName.value = value
    }

    fun onLastNameChange(value: String) {
        _lastName.value = value
    }

    fun onDniChange(value: String) {
        _dni.value = value
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

    fun onDniImageSelected(uri: Uri) {
        _dniImageUri.value = uri
    }

    fun onProfileImageSelected(uri: Uri) {
        _profileImageUri.value = uri
    }

    fun onClickRegisterButton(context: Context) {
        viewModelScope.launch {
            try {
                val dniPart = dniImageUri.value?.let {
                    context.contentResolver.openInputStream(it)?.readBytes()?.toRequestBody("image/*".toMediaTypeOrNull())
                        ?.let { body -> MultipartBody.Part.createFormData("dni_photo", "dni.jpg", body) }
                } ?: getDummyImagePart(context, "dni_photo")

                val profilePart = profileImageUri.value?.let {
                    context.contentResolver.openInputStream(it)?.readBytes()?.toRequestBody("image/*".toMediaTypeOrNull())
                        ?.let { body -> MultipartBody.Part.createFormData("profile_photo", "profile.jpg", body) }
                } ?: getDummyImagePart(context, "profile_photo")

                val response = authRepository.register(
                    dni = dni.value.toPlainRequestBody(),
                    firstName = firstName.value.toPlainRequestBody(),
                    lastName = lastName.value.toPlainRequestBody(),
                    dniExtension = null, // si deseas agregarlo: "PE".toPlainRequestBody()
                    password = password.value.toPlainRequestBody(),
                    phone = phoneNumber.value.toPlainRequestBody(),
                    email = email.value.toPlainRequestBody(),
                    address = address.value.toPlainRequestBody(),
                    reputationStatusId = "1".toPlainRequestBody(),
                    dniPhoto = dniPart,
                    profilePhoto = profilePart
                )
                Log.d(TAG, "Registro exitoso: $response")

                _registerSuccess.emit(Unit)
            } catch (e: Exception) {
                Log.e(TAG, "Error al registrar: ${e.message}", e)
            }
        }
    }


    private fun String.toPlainRequestBody(): RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun getDummyImagePart(context: Context, name: String): MultipartBody.Part {
        val inputStream = context.resources.openRawResource(R.raw.dummy) // asegúrate de tener dummy.jpg en res/raw
        val bytes = inputStream.readBytes()
        val requestBody = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, "$name.jpg", requestBody)
    }
}