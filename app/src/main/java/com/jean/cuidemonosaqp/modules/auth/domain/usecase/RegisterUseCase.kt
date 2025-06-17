package com.jean.cuidemonosaqp.modules.auth.domain.usecase

import com.jean.cuidemonosaqp.modules.auth.domain.repository.AuthRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
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
    ) = repository.register(
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
