package com.jean.cuidemonosaqp.features.auth.presentation.register

import android.net.Uri

sealed interface RegisterAction {
    data class OnNameChanged(val text: String) : RegisterAction
    data class OnEmailChanged(val text: String) : RegisterAction
    data class OnPasswordChanged(val text: String) : RegisterAction
    data class OnConfirmPasswordChanged(val text: String) : RegisterAction
    data class OnAddressChanged(val text: String) : RegisterAction
    data class OnPhoneNumberChanged(val text: String) : RegisterAction
    data class OnDniPhotoSelected(val uri: Uri?) : RegisterAction
    data class OnProfilePhotoSelected(val uri: Uri?) : RegisterAction
    object OnRegisterClicked : RegisterAction
}
