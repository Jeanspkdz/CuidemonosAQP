package com.jean.cuidemonosaqp.modules.auth.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    val authRemoteDatasource: AuthRemoteDatasource,
    // localDatasource
) : AuthRepository{
    override suspend fun login(identifier: String, password: String): LoginResponse {
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