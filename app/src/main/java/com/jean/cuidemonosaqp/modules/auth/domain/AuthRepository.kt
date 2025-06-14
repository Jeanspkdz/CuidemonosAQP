package com.jean.cuidemonosaqp.modules.auth.domain

import com.jean.cuidemonosaqp.modules.auth.data.model.LoginResponse
import com.jean.cuidemonosaqp.modules.auth.data.model.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AuthRepository {
    suspend fun login(identifier: String, password: String): Result<LoginResponse>

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
        dniPhoto: MultipartBody.Part,
        profilePhoto: MultipartBody.Part
    ): RegisterResponse
}