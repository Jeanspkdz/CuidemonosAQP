package com.jean.cuidemonosaqp.modules.auth.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import javax.inject.Inject

interface AuthRemoteDatasource {
    suspend fun login(identifier: String, password: String): LoginResponse

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
    ): RegisterResponse
}

// Retrofit
class AuthRetrofitDatasource @Inject constructor(
    private val authService: AuthService
) : AuthRemoteDatasource {
    override suspend fun login(identifier: String, password: String): LoginResponse {
        return authService.login(LoginRequest(identifier, password))
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
        dniPhoto: MultipartBody.Part?,
        profilePhoto: MultipartBody.Part?
    ): RegisterResponse {
        return authService.register(
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

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @Multipart
    @POST("auth/register")
    suspend fun register(
        @Part("dni") dni: RequestBody,
        @Part("first_name") firstName: RequestBody,
        @Part("last_name") lastName: RequestBody,
        @Part("dni_extension") dniExtension: RequestBody?, // opcional
        @Part("password") password: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("email") email: RequestBody,
        @Part("address") address: RequestBody,
        @Part("reputation_status_id") reputationStatusId: RequestBody,
        @Part dniPhoto: MultipartBody.Part?,
        @Part profilePhoto: MultipartBody.Part?
    ): RegisterResponse
}
