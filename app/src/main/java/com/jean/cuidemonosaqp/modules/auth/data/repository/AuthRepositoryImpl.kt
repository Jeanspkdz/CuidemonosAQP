package com.jean.cuidemonosaqp.modules.auth.data.repository

import android.util.Log
import com.jean.cuidemonosaqp.modules.auth.data.model.*
import com.jean.cuidemonosaqp.modules.auth.data.remote.AuthApi
import com.jean.cuidemonosaqp.modules.auth.domain.repository.AuthRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.shared.preferences.Session
import com.jean.cuidemonosaqp.shared.preferences.SessionCache
import com.jean.cuidemonosaqp.shared.preferences.TokenManager
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionCache: SessionCache,
) : AuthRepository {

    companion object {
        const val TAG = "AuthRepositoryImpl"
    }

    override suspend fun login(emailOrDni: String, password: String): NetworkResult<LoginResponse> {
        return try {
            val response = authApi.login(LoginRequest(emailOrDni, password))
            Log.d(TAG, "Response: $response")
            if (response.isSuccessful && response.body() != null) {
                val bodyResponse = response.body()!!
                sessionCache.updateSession(
                    Session(
                        id = bodyResponse.id,
                        token = bodyResponse.accessToken
                    )
                )
                NetworkResult.Success(response.body()!!)
            } else {
                val errorResponse =response.errorBody()?.string()
                Log.d(TAG, "Not Successful: $errorResponse")
                val code = response.code()

                val errorMessage = when(code){
                    400 -> "DNI o Email y contraseña son requeridos"
                    401 -> "Credenciales Invalidas"
                    500 -> "Error del Servidor"
                    else -> "Algo salió mal"
                }
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Algo salió mal")
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