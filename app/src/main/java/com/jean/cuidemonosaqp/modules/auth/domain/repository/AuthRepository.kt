package com.jean.cuidemonosaqp.modules.auth.domain.repository

import com.jean.cuidemonosaqp.modules.auth.data.model.*
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(emailOrDni: String, password: String): NetworkResult<LoginResponse>

    suspend fun register(
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
    ): NetworkResult<RegisterResponse>

    // Nuevos métodos para manejo offline
    suspend fun saveRegistrationOffline(
        dni: String,
        firstName: String,
        lastName: String,
        dniExtension: String?,
        password: String,
        phone: String,
        email: String,
        address: String,
        reputationStatusId: String,
        profilePhotoPath: String?,
        dniPhotoPath: String?
    ): Long

    suspend fun getPendingRegistrationsCount(): Int

    fun getPendingRegistrationsCountFlow(): Flow<Int>

    suspend fun syncPendingRegistrations(): Boolean
}
