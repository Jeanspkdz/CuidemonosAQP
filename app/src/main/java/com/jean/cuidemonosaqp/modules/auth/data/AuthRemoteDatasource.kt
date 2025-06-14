package com.jean.cuidemonosaqp.modules.auth.data

import android.util.Log
import com.jean.cuidemonosaqp.shared.utils.CustomException
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import javax.inject.Inject

interface AuthRemoteDatasource {
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
        dniPhoto: MultipartBody.Part?,
        profilePhoto: MultipartBody.Part?
    ): RegisterResponse
}

// Retrofit
class AuthRetrofitDatasource @Inject constructor(
    private val authService: AuthService
) : AuthRemoteDatasource {
    companion object {
        const val RETROFIT_DATASOURCE = "retrofit_datasource"
    }

    override suspend fun login(identifier: String, password: String): Result<LoginResponse> {
        val response = authService.login(LoginRequest(identifier, password))

        if (response.isSuccessful) {
            val body = response.body()
            return if (body != null) {
                Result.success(body)
            } else {
                Result.failure(CustomException.UnknownException()) // o alguna excepción como EmptyResponseException
            }
        } else {
            val code = response.code()

            val error = response.errorBody()?.string()
            Log.d(RETROFIT_DATASOURCE, "Error: $error")

            val exception = when (code) {
                400 -> CustomException.RequiredFieldsException()
                401 -> CustomException.InvalidCredentialsException()
                500 -> CustomException.InternalServerException()
                else -> CustomException.UnknownException()
            }

            return Result.failure(exception)
        }

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
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

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
