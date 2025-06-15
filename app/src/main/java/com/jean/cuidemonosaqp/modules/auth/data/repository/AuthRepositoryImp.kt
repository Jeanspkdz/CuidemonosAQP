package com.jean.cuidemonosaqp.modules.auth.data.repository

import com.jean.cuidemonosaqp.modules.auth.data.model.LoginResponse
import com.jean.cuidemonosaqp.modules.auth.data.model.RegisterResponse
import com.jean.cuidemonosaqp.modules.auth.data.remote.AuthRemoteDatasource
import com.jean.cuidemonosaqp.modules.auth.domain.AuthRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    val authRemoteDatasource: AuthRemoteDatasource,
    // localDatasource
) : AuthRepository {
    override suspend fun login(identifier: String, password: String): Result<LoginResponse> {
        return authRemoteDatasource.login(identifier, password)
    }

    override suspend fun register(
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
    ): RegisterResponse {
        return authRemoteDatasource.register(
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
    }
}