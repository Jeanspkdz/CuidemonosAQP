package com.jean.cuidemonosaqp.shared.data.remote

import com.jean.cuidemonosaqp.shared.data.model.LoginRequest
import com.jean.cuidemonosaqp.shared.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}