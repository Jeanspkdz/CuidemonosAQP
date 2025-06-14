package com.jean.cuidemonosaqp.shared.utils

import com.jean.cuidemonosaqp.R


sealed class CustomException : Exception() {
    class InvalidCredentialsException: CustomException()
    class InternalServerException: CustomException()
    class UnknownException: CustomException()
    class RequiredFieldsException: CustomException()
}


fun Throwable.handleExceptionToStringRes(): Int {
    return if(this is CustomException){
        when(this){
            is CustomException.InternalServerException -> R.string.internal_server_error
            is CustomException.InvalidCredentialsException -> R.string.invalid_credentials_error
            is CustomException.RequiredFieldsException -> R.string.required_fields_error
            is CustomException.UnknownException -> R.string.unknown_error
        }
    }else {
        R.string.unknown_error
    }
}