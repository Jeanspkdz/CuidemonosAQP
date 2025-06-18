package com.jean.cuidemonosaqp.modules.auth.data.repository

import com.jean.cuidemonosaqp.modules.auth.data.model.*
import com.jean.cuidemonosaqp.modules.auth.data.remote.AuthApi
import com.jean.cuidemonosaqp.modules.auth.domain.repository.AuthRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.shared.preferences.TokenManager
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(emailOrDni: String, password: String): NetworkResult<LoginResponse> {
        return try {
            val response = authApi.login(LoginRequest(emailOrDni, password))
            if (response.isSuccessful && response.body() != null) {
                tokenManager.saveAccessToken(response.body()!!.accessToken)
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(response.errorBody()?.string() ?: "Login failed")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Unknown error")
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
    ): NetworkResult<RegisterResponse> {
        return try {
            val response = authApi.register(
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
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(response.errorBody()?.string() ?: "Register failed")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error(e.localizedMessage ?: "Ha ocurrido un error inesperado durante el registro.")
        }
    }

}