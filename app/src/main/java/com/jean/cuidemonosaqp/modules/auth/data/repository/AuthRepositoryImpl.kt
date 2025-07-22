package com.jean.cuidemonosaqp.modules.auth.data.repository

import android.util.Log
import com.jean.cuidemonosaqp.modules.auth.data.model.LoginRequest
import com.jean.cuidemonosaqp.modules.auth.data.model.LoginResponse
import com.jean.cuidemonosaqp.modules.auth.data.model.RegisterResponse
import com.jean.cuidemonosaqp.modules.auth.data.remote.AuthApi
import com.jean.cuidemonosaqp.modules.auth.domain.repository.AuthRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.shared.preferences.Session
import com.jean.cuidemonosaqp.shared.preferences.SessionRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionCache: SessionRepository,
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
        addressLatitude: RequestBody,
        addressLongitude: RequestBody,
        dniPhoto: MultipartBody.Part?,
        profilePhoto: MultipartBody.Part?
    ): NetworkResult<RegisterResponse> {
        return try {
            Log.d(TAG, "=== REGISTER REQUEST DEBUG ===")
            Log.d(TAG, "AddressLatitude RequestBody content: ${addressLatitude.contentType()}")
            Log.d(TAG, "AddressLongitude RequestBody content: ${addressLongitude.contentType()}")
            // Para ver el contenido real (esto es un hack pero útil para debug):
            val buffer = okio.Buffer()
            addressLatitude.writeTo(buffer)
            Log.d(TAG, "AddressLatitude actual content: ${buffer.readUtf8()}")

            val buffer2 = okio.Buffer()
            addressLongitude.writeTo(buffer2)
            Log.d(TAG, "AddressLongitude actual content: ${buffer2.readUtf8()}")
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
                addressLatitude,
                addressLongitude,
                dniPhoto,
                profilePhoto
            )
            Log.d(TAG, "Register response code: ${response.code()}")
            Log.d(TAG, "Register response body: ${response.body()}")
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(response.errorBody()?.string() ?: "Register failed")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.localizedMessage ?: "Ha ocurrido un error inesperado")
        }
    }
}