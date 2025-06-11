package com.jean.cuidemonosaqp.modules.auth.data

import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST
import javax.inject.Inject

interface AuthRemoteDatasource {
    suspend fun login(identifier: String, password: String): LoginResponse
}

// Retrofit
class AuthRetrofitDatasource @Inject constructor(
    private val authService: AuthService
) : AuthRemoteDatasource {
    override suspend fun login(identifier: String, password: String): LoginResponse {
        return authService.login(LoginRequest(identifier, password))
    }
}

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
