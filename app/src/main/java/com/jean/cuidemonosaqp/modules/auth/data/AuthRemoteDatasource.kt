package com.jean.cuidemonosaqp.modules.auth.data

import retrofit2.http.Field
import retrofit2.http.POST
import javax.inject.Inject

interface AuthRemoteDatasource {
    suspend fun login(identifier: String, password: String): User
}

// Retrofit
class AuthRetrofitDatasource @Inject constructor(
    private val authService: AuthService
) : AuthRemoteDatasource {
    override suspend fun login(identifier: String, password: String): User {
        return authService.login(identifier = identifier, password= password)
    }
}

interface AuthService{
    @POST("auth/login")
    suspend fun login(
        @Field("identifier") identifier: String,
        @Field("password") password: String
    ): User
}
