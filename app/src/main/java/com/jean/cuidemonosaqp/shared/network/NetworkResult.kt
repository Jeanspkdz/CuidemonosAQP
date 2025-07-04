package com.jean.cuidemonosaqp.shared.network

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T): NetworkResult<T>()
    data class Error(val message: String): NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}
