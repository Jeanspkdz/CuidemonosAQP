package com.jean.cuidemonosaqp.features.auth.data

import com.jean.cuidemonosaqp.features.auth.domain.AuthRepository
import com.jean.cuidemonosaqp.shared.data.model.LoginRequest
import com.jean.cuidemonosaqp.shared.data.remote.AuthApi
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun login(identifier: String, password: String): Result<String> {
        return try {
            val response = api.login(LoginRequest(identifier, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.token)
            } else {
                Result.failure(Exception("Credenciales inválidas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
