package com.jean.cuidemonosaqp.features.auth.presentation.register

import androidx.compose.runtime.Immutable
import android.net.Uri

@Immutable
data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val dniPhotoUri: Uri? = null,
    val profilePhotoUri: Uri? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

